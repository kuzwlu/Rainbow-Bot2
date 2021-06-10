package rainbow.kuzwlu.util;

import rainbow.kuzwlu.config.UserConfig;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.bytecode.ConstPool;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author kuzwlu
 * @Description TODO
 * @Date 2020/11/6 19:43
 */

public class JavassistUtil {

    private UserConfig userConfig = UserConfig.getInstance();

    private static final JavassistUtil javacAndRun = new JavassistUtil();

    private JavassistUtil() { }

    public static JavassistUtil getInstance() {
        return javacAndRun;
    }

    /**
     * 通过编译后的byte[]获取一个CtClass
     * @param inputStream
     * @return  getCtClass
     */
    public CtClass getCtClass(InputStream inputStream){
        CtClass ctClass = null;
        try {
            ctClass = ClassPool.getDefault().makeClass(inputStream);
            inputStream.close();
        } catch (IOException e) {
            String str = LogUtil.exceptionLog(e);
            LogUtil.saveLogToFile(userConfig.getMaster(), str, userConfig.getLogDir() + "/JavassistUtil/ctClass.txt");
            throw new RuntimeException("获取CtClass出错，日志文件保存在log/JavassistUtil/ctClass.txt，请查看日志！\n"+str);
        }
        return ctClass;
    }

    /**
     * 判断依赖是否被禁止使用---notImport
     * URL：https://zhuanlan.zhihu.com/p/148645206
     *
     * @param inputStream
     * @return set
     */
    public Set<String> getDependencies(InputStream inputStream) {
        HashSet<String> set = new HashSet<>();
        try {
            CtClass ctClass = getCtClass(inputStream);
            inputStream.close();
            ConstPool constPool = ctClass.getClassFile().getConstPool();
            for (int ix = 1, size = constPool.getSize(); ix < size; ix++) {
                int descriptorIndex;
                if (constPool.getTag(ix) == ConstPool.CONST_Class) {
                    set.add(constPool.getClassInfo(ix));
                } else if (constPool.getTag(ix) == ConstPool.CONST_NameAndType) {
                    descriptorIndex = constPool.getNameAndTypeDescriptor(ix);
                    String desc = constPool.getUtf8Info(descriptorIndex);
                    for (int p = 0; p < desc.length(); p++) {
                        if (desc.charAt(p) == 'L') {
                            set.add(desc.substring(++p, p = desc.indexOf(';', p)).replace('/', '.'));
                        }
                    }
                }
            }
        } catch (Exception e) {
            String str = LogUtil.exceptionLog(e);
            LogUtil.saveLogToFile(userConfig.getMaster(), str, userConfig.getLogDir() + "/JavassistUtil/classFile.txt");
            throw new RuntimeException("获取ClassFile出错，日志文件保存在log/JavassistUtil/classFile.txt，请查看日志！\n"+str);
        }
        return set;
    }

    /**
     * 黑白名单--依赖
     * @param inputStream
     * @throws Exception
     */
    public void safetyCheck(InputStream inputStream) throws Exception {
        Set<String> dependencies = getDependencies(inputStream);
        inputStream.close();
        //白名单
        List<String> notSupportedPackages = new ArrayList<>();
        for (String d : dependencies) {
            boolean supported = false;
            for (String supportPackage : userConfig.getWriteList()) {
                if (d.startsWith(supportPackage)) {
                    supported = true;
                    break;
                }
            }
            if (!supported) {
                notSupportedPackages.add(d);
            }
        }

        //黑名单
        for (String d : dependencies) {
            boolean unsupported = false;
            for (String supportPackage : userConfig.getBlackList()) {
                if (d.startsWith(supportPackage)) {
                    unsupported = true;
                    break;
                }
            }

            if (unsupported) {
                notSupportedPackages.add(d);
            }
        }

        if (!notSupportedPackages.isEmpty()) {
            String message = String.join(",", notSupportedPackages);
            LogUtil.saveLogToFile(userConfig.getMaster(), "不支持以下类的使用：" +message, userConfig.getLogDir() + "/JavassistUtil/notSupportedPackages.txt");
            throw new RuntimeException("不支持以下类的使用：" + message);
        }
    }


}
