package rainbow.kuzwlu.core.plugins.compiler.java;

import love.forte.common.ioc.annotation.Depend;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rainbow.kuzwlu.core.plugins.annotation.Cron;
import rainbow.kuzwlu.core.plugins.PluginsRuntime;
import rainbow.kuzwlu.core.plugins.annotation.RainbowPlugins;
import rainbow.kuzwlu.core.plugins.compiler.pojo.NObject;
import rainbow.kuzwlu.core.plugins.enums.PluginsType;
import rainbow.kuzwlu.core.plugins.utils.PluginsFile;
import rainbow.kuzwlu.utils.PathUtil;
import rainbow.kuzwlu.utils.ThreadUtil;
import rainbow.kuzwlu.utils.exception.QuartzException;
import rainbow.kuzwlu.utils.quartz.QuartzUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

public class  InvokeObject {

    private static final JavaScriptCompile javaScriptCompile = (JavaScriptCompile) PluginsRuntime.getRuntime().getScriptCompile(PluginsType.JAVA);
    
    private static final Logger logger = LogManager.getLogger(InvokeObject.class);

    private InvokeObject(){}

    /**
     * 依赖注入
     * @param pluginsName 插件名称
     * @param nObject 实例
     * @param invokeParams 依赖
     */
    private static void injectionDepend(String pluginsName,NObject nObject,Map<Class, Object>  invokeParams){
            nObject.getObjList().forEach(
            obj -> Arrays.stream(obj.getClass().getDeclaredFields()).forEach(field -> {
                if (field.getAnnotation(Depend.class) != null) {
                    invokeParams.forEach((classes, invokeObject) -> {
                        if (field.getType().equals(classes)) {
                            try {
                                field.setAccessible(true);
                                field.set(obj, invokeObject);
                            } catch (IllegalAccessException e) {
                                logger.error("插件类型：{java}\t插件名：[" + pluginsName + "]-> 注入" + invokeObject.getClass().getName() + "失败！", e);
                            }
                        }
                    });
                }
            }));
    }

    /**
     * 设置PluginsFile
     * @param pluginsName
     * @return
     */
    private static PluginsFile setPluginsFile(String pluginsName){
        PluginsFile pluginsFile = null;
        try {
            Class<?> aClass = Class.forName("rainbow.kuzwlu.core.plugins.utils.PluginsFile");
            for (Field declaredField : aClass.getDeclaredFields()) {
                if (declaredField.getName().equals("PATH")){
                    declaredField.setAccessible(true);
                    declaredField.set("", PathUtil.getInstance(InvokeObject.class).getPath("plugins/"+PluginsType.JAVA.getValue().toLowerCase()+"/"+pluginsName));
                }
            }
            pluginsFile = (PluginsFile) aClass.newInstance();
//            for (Field declaredField : aClass.getDeclaredFields()) {
//                if (declaredField.getName().equals("PATH")){
//                    declaredField.setAccessible(true);
//                    declaredField.set("", "");
//                }
//            }
        } catch (Exception e) {
            logger.error("插件类型：{java}\t插件名：["+pluginsName+"]-> 注入PluginsFile失败！",e);
        }
        return pluginsFile;
    }

    /**
     * 通过注解执行方法   issue：执行方法时，会根据方法名称进行排序，然后依次执行，不可靠（建议使用注解区分排序）
     * @param annotationClass
     * @param invokeParams
     * @param cronFlag
     * @param <T>
     */
    public static  <T extends Annotation> void invokeByAnnotation(String pluginsName,Class<T> annotationClass,Map<Class, Object> invokeParams,Boolean cronFlag){
        javaScriptCompile.getClassAClazzList().stream().filter(nObject -> null != nObject.getRainbowPlugins().priority()).sorted(Comparator.comparing(nObject -> nObject.getRainbowPlugins().priority().value()))
            .forEach(nObject -> {
            if (!nObject.getPluginsName().equals(pluginsName)) return;
            nObject.getObjList().stream().forEach(obj -> Arrays.stream(obj.getClass().getMethods())
                    .filter(method -> null != method.getAnnotation(annotationClass))//.filter(method -> method.getReturnType().getName().equals("void"))
                    .sorted(Comparator.comparing(method -> {
                        RainbowPlugins.Priority priority = method.getAnnotation(RainbowPlugins.Priority.class);
                        if (null != priority){
                            return priority.value();
                        }else {
                            return 9999;
                        }
                    })).forEach(method -> {
                            List<Object> params = new ArrayList<>();
                            invokeParams.put(RainbowPlugins.class, nObject.getRainbowPlugins());
                            invokeParams.put(PluginsFile.class,setPluginsFile(pluginsName));
                            InvokeObject.injectionDepend(pluginsName,nObject,invokeParams);
                            params.addAll(invokeParamTList(method, invokeParams));
                            if (cronFlag) {
                                Cron cron = method.getAnnotation(Cron.class);
                                try {
                                    QuartzUtil.getInstance().addJob(pluginsName + "-" + cron.jobName(), cron.description(), () -> execMethod(pluginsName, method, obj, params), cron.schedulingPattern());
                                } catch (QuartzException e) {
                                    logger.error("插件类型：{java}\t插件名：[" + pluginsName + "]-> 定时任务[" + pluginsName + "-" + cron.jobName() + "]失败！", e);
                                }
                            } else {
                                execMethod(pluginsName, method, obj, params);
                            }
                    }));
        });
    }


    public static  <T extends Annotation> void invokeByAnnotation(NObject nObject,String pluginsName,Class<T> annotationClass,Map<Class, Object> invokeParams,Boolean cronFlag){
                    if (!nObject.getPluginsName().equals(pluginsName)) return;
                    nObject.getObjList().stream().forEach(obj -> Arrays.stream(obj.getClass().getMethods())
                            .filter(method -> null != method.getAnnotation(annotationClass))//.filter(method -> method.getReturnType().getName().equals("void"))
                            .sorted(Comparator.comparing(method -> {
                                RainbowPlugins.Priority priority = method.getAnnotation(RainbowPlugins.Priority.class);
                                if (null != priority){
                                    return priority.value();
                                }else {
                                    return 9999;
                                }
                            })).forEach(method -> {
                                List<Object> params = new ArrayList<>();
                                invokeParams.put(RainbowPlugins.class, nObject.getRainbowPlugins());
                                invokeParams.put(PluginsFile.class,setPluginsFile(pluginsName));
                                InvokeObject.injectionDepend(pluginsName,nObject,invokeParams);
                                params.addAll(invokeParamTList(method, invokeParams));
                                if (cronFlag) {
                                    Cron cron = method.getAnnotation(Cron.class);
                                    try {
                                        QuartzUtil.getInstance().addJob(pluginsName + "-" + cron.jobName(), cron.description(), () -> execMethod(pluginsName, method, obj, params), cron.schedulingPattern());
                                    } catch (QuartzException e) {
                                        logger.error("插件类型：{java}\t插件名：[" + pluginsName + "]-> 定时任务[" + pluginsName + "-" + cron.jobName() + "]失败！", e);
                                    }
                                } else {
                                    execMethod(pluginsName, method, obj, params);
                                }
                            }));
    }

    /**
     * 通过注解执行方法
     * @param annotationClass
     * @param invokeParams
     * @param <T>
     */
    public static  <T extends Annotation> void invokeByAnnotation(String pluginsName,Class<T> annotationClass,Map<Class, Object> invokeParams){
        invokeByAnnotation(pluginsName,annotationClass,invokeParams,false);
    }

//    public static  <T extends Annotation> void invokeAllByAnnotation(Class<T> annotationClass,Map<Class, Object> invokeParams){
//        //javaScriptCompile.getPluginsRunList().forEach(pluginsName -> invokeByAnnotation(pluginsName,annotationClass,invokeParams,false));
//        javaScriptCompile.getPluginsRunList().stream()
//                .filter(pluginsName ->javaScriptCompile.getClassAClazzList().stream().filter(nObject -> nObject.getPluginsName().equals(pluginsName)).count() == 1)
//                .forEach(pluginsName -> ThreadUtil.getInstance().executorService.execute(()->invokeByAnnotation(pluginsName,annotationClass,invokeParams,false)));
//    }

    public static  <T extends Annotation> void invokeAllByAnnotation(Class<T> annotationClass,Map<Class, Object> invokeParams){
        //javaScriptCompile.getPluginsRunList().forEach(pluginsName -> invokeByAnnotation(pluginsName,annotationClass,invokeParams,false));
        javaScriptCompile.getClassAClazzList().stream().filter(nObject -> null != nObject.getRainbowPlugins().priority()).sorted(Comparator.comparing(nObject -> nObject.getRainbowPlugins().priority().value()))
                .forEach(nObject -> {
            javaScriptCompile.getPluginsRunList().forEach(pluginsName ->{
                if (nObject.getPluginsName().equals(pluginsName)){
                    ThreadUtil.getInstance().executorService.execute(()->invokeByAnnotation(nObject,pluginsName,annotationClass,invokeParams,false));
                }
            });
        });
    }

    public static  <T extends Annotation> void invokeAllByAnnotation(Class<T> annotationClass,Map<Class, Object> invokeParams,boolean cronFlag){
        //javaScriptCompile.getPluginsRunList().forEach(pluginsName ->invokeByAnnotation(pluginsName,annotationClass,invokeParams,cronFlag));
        javaScriptCompile.getPluginsRunList().stream()
                .filter(pluginsName ->javaScriptCompile.getClassAClazzList().stream().filter(nObject -> nObject.getPluginsName().equals(pluginsName)).count() == 1)
                .forEach(pluginsName ->ThreadUtil.getInstance().executorService.execute(()->invokeByAnnotation(pluginsName,annotationClass,invokeParams,cronFlag)));
    }

    public static <T> void invokeByMethodName(String pluginsName, String methodName, Class<T> interfaceClass,Map<Class,Object> invokeParams){
        javaScriptCompile.getClassAClazzList().stream().filter(nObject -> null != nObject.getRainbowPlugins().priority()).sorted(Comparator.comparing(nObject -> nObject.getRainbowPlugins().priority().value()))
            .forEach(nObject -> {
            if (!nObject.getPluginsName().equals(pluginsName)) return;
            nObject.getObjList().stream().filter(clazz1 -> {
                try {
                    return clazz1.getClass().getGenericInterfaces()[0].equals(interfaceClass);
                } catch (Exception e) {
                    return false;
                }
            }).forEach(obj -> Arrays.stream(obj.getClass().getMethods()).filter(method -> method.getName().equals(methodName)).forEach(method -> {
                List<Object> params = new ArrayList<>();
                invokeParams.put(RainbowPlugins.class, nObject.getRainbowPlugins());
                invokeParams.put(PluginsFile.class,setPluginsFile(pluginsName));
                InvokeObject.injectionDepend(pluginsName,nObject,invokeParams);
                params.addAll(invokeParamTList(method, invokeParams));
                execMethod(pluginsName, method, obj, params);
            }));
        });
    }

    public static  <T> void invokeAllByMethodName(String methodName,Class<T> interfaceClass,Map<Class, Object> invokeParams){
        //javaScriptCompile.getPluginsRunList().stream().forEach(pluginsName -> invokeByMethodName(pluginsName,methodName,interfaceClass,invokeParams));
        javaScriptCompile.getPluginsRunList().stream()
                .filter(pluginsName ->javaScriptCompile.getClassAClazzList().stream().filter(nObject -> nObject.getPluginsName().equals(pluginsName)).count() == 1)
                .forEach(pluginsName -> ThreadUtil.getInstance().executorService.execute(() ->invokeByMethodName(pluginsName,methodName,interfaceClass,invokeParams)));
    }

    /**
     * 获得所有对象（编译后实例化的）
     * @return
     */
    public static List<Object> getInvokeObjectList(){
        List<Object> objectList = new ArrayList<>();
        javaScriptCompile.getClassAClazzList().forEach((clazz)-> objectList.addAll(clazz.getObjList()));
        return objectList;
    }


    /**
     * 执行方法
     * @param method
     * @param obj
     * @param params
     */
    public static void execMethod(String pluginsName,Method method,Object obj,List<Object> params){
        try {
            method.invoke(obj,params.toArray());
        }catch (IllegalArgumentException e){
            logger.error("插件类型：{java}\t插件名：["+pluginsName+"]-> 执行方法["+obj.getClass().getName()+"."+method.getName()+"]失败！\n参数类型不匹配！！",e);
        }catch (IllegalAccessException e) {
            logger.error("插件类型：{java}\t插件名：["+pluginsName+"]-> 执行方法["+obj.getClass().getName()+"."+method.getName()+"]失败！\n请检测是否使用了未支持的传入参数！",e);
        }catch (InvocationTargetException  e){
            logger.error("插件类型：{java}\t插件名：["+pluginsName+"]-> 执行方法["+obj.getClass().getName()+"."+method.getName()+"]失败！",e.getTargetException());
        }catch (Exception e){
            logger.error("插件类型：{java}\t插件名：["+pluginsName+"]-> 执行方法["+obj.getClass().getName()+"."+method.getName()+"]失败！",e);
        }
    }

    /**
     * 获得方法参数
     * @param method
     * @param invokeParams
     * @return
     */
    public static List<Object> invokeParamTList(Method method,Map<Class, Object> invokeParams){
        List<Object> params = new ArrayList<>();
        Arrays.stream(method.getParameters()).forEach(parameter -> invokeParams.forEach((classes, invokeObject)->{
            if (parameter.getType().equals(classes) ){
                params.add(invokeObject);
            }
        }));
        return params.stream().distinct().collect(Collectors.toList());
    }

}
