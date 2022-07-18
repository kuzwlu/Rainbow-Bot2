package rainbow.kuzwlu.core.plugins.compiler;

import lombok.extern.slf4j.Slf4j;
import rainbow.kuzwlu.exception.CompileException;
import rainbow.kuzwlu.utils.PathUtil;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.CharBuffer;
import java.util.*;
import java.util.jar.JarEntry;

/**
 * Created by xidongzhou1 on 2020/1/17.
 * Url:https://www.jianshu.com/p/32a1e01070d7
 * 自修过：
 * 1.可设置：禁止导入系统包 notImport
 * 2.加入：DiagnosticCollector，获取编译出错的日志（具体第几行）
 * 3.-Xlint:all  编译时启用所有警告提示
 * 4.-XDuseUnsharedTable  禁用SharedNameTable，防止内存溢出（JDK9之后以修复）
 * 5.通过Map结构存储所有需要编译的java（一次性编译所有）
 */
@Slf4j
public class RainbowClassLoader {

    static class CustomJavaFileObject implements JavaFileObject {
        private String binaryName;
        private URI uri;
        private String name;

        public String binaryName() {
            return binaryName;
        }

        public CustomJavaFileObject(String binaryName, URI uri) {
            this.uri = uri;
            this.binaryName = binaryName;
            name = uri.getPath() == null ? uri.getSchemeSpecificPart() : uri.getPath();
        }

        @Override
        public Kind getKind() {
            return Kind.CLASS;
        }

        @Override
        public boolean isNameCompatible(String simpleName, Kind kind) {
            String baseName = simpleName + kind.extension;
            return kind.equals(getKind()) && (baseName.equals(getName()) || getName().endsWith("/" + baseName));
        }

        @Override
        public NestingKind getNestingKind() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Modifier getAccessLevel() {
            throw new UnsupportedOperationException();
        }

        @Override
        public URI toUri() {
            return uri;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public InputStream openInputStream() throws IOException {
            return uri.toURL().openStream();
        }

        @Override
        public OutputStream openOutputStream() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Writer openWriter() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public long getLastModified() {
            return 0;
        }

        @Override
        public boolean delete() {
            throw new UnsupportedOperationException();
        }
    }

    static class MemoryInputJavaFileObject extends SimpleJavaFileObject {
        final String code;

        MemoryInputJavaFileObject(String name, String code) {
            super(URI.create(name.replaceAll("\\.", "/") + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharBuffer getCharContent(boolean ignoreEncodingErrors) {
            return CharBuffer.wrap(code);
        }
    }

    static class MemoryOutputJavaFileObject extends SimpleJavaFileObject {
        final String name;
        Map<String, byte[]> class_out;

        MemoryOutputJavaFileObject(String name, Map<String, byte[]> out) {
            super(URI.create(name.replaceAll("\\.", "/") + Kind.SOURCE.extension), Kind.CLASS);
            this.name = name;
            this.class_out = out;
        }

        @Override
        public OutputStream openOutputStream() {
            return new FilterOutputStream(new ByteArrayOutputStream()) {
                @Override
                public void close() throws IOException {
                    out.close();
                    ByteArrayOutputStream bos = (ByteArrayOutputStream) out;
                    class_out.put(name, bos.toByteArray());
                }
            };
        }
    }

    class SpringBootJarFileManager implements JavaFileManager {
        private URLClassLoader classLoader;
        private StandardJavaFileManager standardJavaFileManager;
        final Map<String, byte[]> classBytes = new HashMap<>();

        SpringBootJarFileManager(StandardJavaFileManager standardJavaFileManager, URLClassLoader systemLoader)  {
            this.classLoader = new URLClassLoader(systemLoader.getURLs(), systemLoader);
            this.standardJavaFileManager = standardJavaFileManager;
        }

        @Override
        public ClassLoader getClassLoader(Location location) {
            return classLoader;
        }

        private List<JavaFileObject> find(String packageName) {
            List<JavaFileObject> result = new ArrayList<>();
            String javaPackageName = packageName.replaceAll("\\.", "/");
            try {
                Enumeration<URL> urls = classLoader.findResources(javaPackageName);
                while (urls.hasMoreElements()) {
                    URL ll = urls.nextElement();
                    String ext_form = ll.toExternalForm();
                    String jar = ext_form.substring(0, ext_form.lastIndexOf("!"));
                    String pkg = ext_form.substring(ext_form.lastIndexOf("!") + 1);

                    JarURLConnection conn = (JarURLConnection) ll.openConnection();
                    conn.connect();
                    Enumeration<JarEntry> jar_items = conn.getJarFile().entries();
                    while (jar_items.hasMoreElements()) {
                        JarEntry item = jar_items.nextElement();
                        if (item.isDirectory() || (!item.getName().endsWith(".class"))) {
                            continue;
                        }
                        if (item.getName().lastIndexOf("/") != (pkg.length() - 1)) {
                            continue;
                        }
                        String name = item.getName();
                        URI uri = URI.create(jar + "!/" + name);
                        String binaryName = name.replaceAll("/", ".");
                        binaryName = binaryName.substring(0, binaryName.indexOf(JavaFileObject.Kind.CLASS.extension));
                        result.add(new CustomJavaFileObject(binaryName, uri));
                    }
                }
            }catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException){
                //logger.info("import package '"+packageNames+"' retained by the system! [正在导入系统保留的包！]");
            }catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        public Iterable<JavaFileObject> list(Location location, String packageName, Set<JavaFileObject.Kind> kinds, boolean recurse) throws IOException {
            Iterable<JavaFileObject> ret = null;
            if (location == StandardLocation.PLATFORM_CLASS_PATH) {
                ret = standardJavaFileManager.list(location, packageName, kinds, recurse);
            } else if (location == StandardLocation.CLASS_PATH && kinds.contains(JavaFileObject.Kind.CLASS)) {
                ret = find(packageName);
                if (ret == null || (!ret.iterator().hasNext())) {
                    ret = standardJavaFileManager.list(location, packageName, kinds, recurse);
                }
            } else {
                ret = Collections.emptyList();
            }
            return ret;
        }

        @Override
        public String inferBinaryName(Location location, JavaFileObject file) {
            String ret = "";
            if (file instanceof CustomJavaFileObject) {
                ret = ((CustomJavaFileObject)file).binaryName;
            } else {
                ret = standardJavaFileManager.inferBinaryName(location, file);
            }
            return ret;
        }

        @Override
        public boolean isSameFile(FileObject a, FileObject b) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean handleOption(String current, Iterator<String> remaining) {
            return standardJavaFileManager.handleOption(current, remaining);
        }

        @Override
        public boolean hasLocation(Location location) {
            return location == StandardLocation.CLASS_PATH || location == StandardLocation.PLATFORM_CLASS_PATH;
        }

        @Override
        public JavaFileObject getJavaFileForInput(Location location, String className, JavaFileObject.Kind kind) {
            throw new UnsupportedOperationException();
        }

        @Override
        public FileObject getFileForInput(Location location, String packageName, String relativeName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public FileObject getFileForOutput(Location location, String packageName, String relativeName, FileObject sibling) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() {
            classBytes.clear();
        }

        @Override
        public int isSupportedOption(String option) {
            return -1;
        }

        public Map<String, byte[]> getClassBytes() {
            return new HashMap<>(this.classBytes);
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind,
                                                   FileObject sibling) throws IOException {
            if (kind == JavaFileObject.Kind.CLASS) {
                return new MemoryOutputJavaFileObject(className, classBytes);
            } else {
                return standardJavaFileManager.getJavaFileForOutput(location, className, kind, sibling);
            }
        }
    }

    public Class<?> loadClass(String name, Map<String, byte[]> classBytes) {
        ClassLoader loader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                Class<?> r = null;
                if (classBytes.containsKey(name)) {
                    byte[] buf = classBytes.get(name);
                    r = defineClass(name, buf, 0, buf.length);
                } else {
                    r = systemClassLoader.loadClass(name);
                }
                return r;
            }
        };
        try {
            return loader.loadClass(name);
        } catch (ClassNotFoundException e) {
            throw new CompileException(e);
        }
    }

    private URLClassLoader systemClassLoader;
    public RainbowClassLoader(URLClassLoader loader) {
        systemClassLoader = loader;
    }

    public Map<String, byte[]> compile(Map<String,String> javaInfo,File libPath)  {
        StringBuffer lib = new StringBuffer();
        if (libPath != null ){
            if (libPath.exists()) {
                try {
                    Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
                    addURL.setAccessible(true);
                    for (File file : libPath.listFiles()) {
                        if (PathUtil.isHiddenFile(file)) continue;
                        lib.append(file.getPath()).append(PathUtil.isWindows ? ";" : ":");
                        addURL.invoke(systemClassLoader, new Object[]{file.toURI().toURL()});
                    }
                } catch (NoSuchMethodException | MalformedURLException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager stdManager = compiler.getStandardFileManager(diagnostics, null, null);
        SpringBootJarFileManager springBootJarFileManager = new SpringBootJarFileManager(stdManager, systemClassLoader);
        List<JavaFileObject> javaFileObjectList = new ArrayList<>();

        javaInfo.forEach((javaName,java) ->{
            JavaFileObject javaFileObject = new MemoryInputJavaFileObject(javaName, java);
            javaFileObjectList.add(javaFileObject);
        });
        List<String> options = new ArrayList<>();
        options.addAll(Arrays.asList(
                "-classpath", lib+System.getProperty("java.class.path"),
                "-bootclasspath", System.getProperty("sun.boot.class.path"),
                "-extdirs", System.getProperty("java.ext.dirs"),
                "-encoding","utf-8",
                "-Xlint:all",
                "-XDuseUnsharedTable "
        ));
        JavaCompiler.CompilationTask task = compiler.getTask(null, springBootJarFileManager, diagnostics, options, null, javaFileObjectList);
        Boolean compileRet= task.call();

        if (compileRet == null || (!compileRet.booleanValue())) {
            diagnostics.getDiagnostics().forEach(e ->{
                log.debug(e.toString());
                if (e.getKind().name() == "ERROR" || e.getKind().name().equals("ERROR")) {
                    throw new CompileException(e.toString());
                }
            });
        }
        for (String key : springBootJarFileManager.getClassBytes().keySet()) {
            log.debug("class: " + key + " length: " + Integer.valueOf(springBootJarFileManager.getClassBytes().get(key).length).toString());
            log.debug("class["+key+"]\t加载依赖Jar：\n"+lib);
        }
        return springBootJarFileManager.getClassBytes();
    }
}