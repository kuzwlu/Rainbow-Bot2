package rainbow.kuzwlu.core.plugins.compiler.java;

import org.jetbrains.annotations.NotNull;
import rainbow.kuzwlu.core.plugins.compiler.RainbowClassLoader;
import rainbow.kuzwlu.core.plugins.compiler.interfaces.ScriptCompile;
import rainbow.kuzwlu.core.plugins.enums.PluginsType;
import rainbow.kuzwlu.core.plugins.compiler.pojo.NObject;
import rainbow.kuzwlu.pluginsBot.interfaces.ListenPlugins;
import rainbow.kuzwlu.exception.CompileException;
import rainbow.kuzwlu.utils.JavassistUtil;
import rainbow.kuzwlu.utils.PathUtil;

import java.io.File;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class JavaScriptCompile implements ScriptCompile {

    private static final String PLUGINS="plugins.";

    private final List<NObject> classAClazzList = new ArrayList<>() ;

    public List<NObject> getClassAClazzList() {
        return classAClazzList;
    }

    @Override
    public PluginsType getPluginsType() {
        return PluginsType.JAVA;
    }

    @Override
    public void compile(@NotNull String pluginsName) throws CompileException {
        List<Object> objectList = new ArrayList<>();
        if (pluginsName.trim().isEmpty()) return;
        NObject classAClazz = NObject.builder().pluginsName(pluginsName).build();
        long count = 0;
        RainbowClassLoader classLoader = new RainbowClassLoader((URLClassLoader) Thread.currentThread().getContextClassLoader());
        //文件名，文本内容，需要插件名.java.文件名（保证文件唯一性）
        Map<String, String> javaInfo = new HashMap<>();
        getPluginsString(pluginsName).forEach((fileName,fileString)->{
            if (fileString.isEmpty()) return;
            javaInfo.put(PLUGINS+PluginsType.JAVA.getValue().toLowerCase()+"."+pluginsName+"."+fileName.replaceAll(".java",""),fileString);
        });
        Map<String, byte[]> compile;
        try{
            if (javaInfo.isEmpty()) {return;}
             compile = classLoader.compile(javaInfo,new File(PathUtil.getInstance(JavaScriptCompile.class).getPath("plugins/java/"+pluginsName+"/lib/")));
            JavassistUtil.getInstance().safetyCheck(compile);
        }catch (Exception e){
            logger.error("插件类型：{{}}\t插件名[{}]-> 编译出错！\n{}",getPluginsType(),pluginsName,e.getMessage());
            return;
        }
        for (String javaName : compile.keySet()) {
            Class<?> aClass = classLoader.loadClass(javaName, compile);
            if (javaName.contains("$")) continue;
            try {
                Object o = aClass.newInstance();
                //防止同一插件实现多个ListenPlugins
                count += Arrays.stream(o.getClass().getGenericInterfaces()).filter(interfaces -> interfaces.equals(ListenPlugins.class)).count();
                objectList.add(o);
            } catch (Exception e) {
                throw new CompileException("插件类型：{"+getPluginsType()+"}\t插件名["+pluginsName+"]-> 类："+aClass.getName()+" 实例化错误！",e);
            }
        }
        if (count != 1 && count > 0) {
            throw new CompileException("插件类型：{"+getPluginsType()+"}\t插件名["+pluginsName+"]->  同一插件请勿实现多个ListenPlugins！");
        }
        classAClazz.setObjList(objectList);
        classAClazz.setRainbowPlugins(getPluginsDevelopers(objectList));
        for (NObject nObject : classAClazzList) {
            if (classAClazz.getPluginsName().equals(nObject.getPluginsName())){
                classAClazzList.remove(nObject);
                pluginsRunList.remove(pluginsName);
            }
        }
        classAClazzList.add(classAClazz);
        setPluginsRunList(pluginsName);
    }

    @Override
    public void destroyAll() {
        classAClazzList.clear();
        List<String> pluginsName = new ArrayList<>();
        Collections.copy(pluginsName,getPluginsRunList());
        for (int i = 0; i < getPluginsRunList().size(); i++) {
            removePluginsRunList(pluginsName.get(i));
        }
    }

    @Override
    public void initAll() {
        List<String> pluginsTotalList = getPluginsTotalList();
        pluginsTotalList.forEach(this::init);
    }

    @Override
    public void destroy(String pluginsName) {
        if (classAClazzList.isEmpty()) return;
        AtomicReference<NObject> nObject = new AtomicReference<>();
        classAClazzList.stream().filter(nObject1 -> nObject1.getPluginsName().equals(pluginsName)).forEach(nObject::set);
        if (nObject.get() != null){
            classAClazzList.remove(nObject.get());
            removePluginsRunList(pluginsName);
        }
    }

    @Override
    public void init(String pluginsName) {
        compile(pluginsName);
    }

}
