package rainbow.kuzwlu.core;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import org.junit.Test;
import rainbow.kuzwlu.core.annotation.Cron;

import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author kuzwlu
 * @Description TODO
 * @Date 2021/4/1 16:00
 * @Version 1.0
 */

public class JavaTest {

    @Test
    public void javaTest() throws Exception {
        SpringBootClassLoader springBootClassLoader = new SpringBootClassLoader((URLClassLoader) Thread.currentThread().getContextClassLoader());
        String javaName = "rainbow.kuzwlu.core.javaTestOne";
        String java = "package rainbow.kuzwlu.core;\n" +
                "import love.forte.simbot.annotation.Filter;\n" +
                "import love.forte.simbot.annotation.OnGroup;\n"+
                "import rainbow.kuzwlu.util.HttpUtil;\n" +
                "import rainbow.kuzwlu.core.annotation.Cron;\n" +
                "public class javaTestOne {\n" +
                "    \n" +
                "    private static HttpUtil httpUtil = HttpUtil.getInstance();\n" +
                "\n" +
                "    public static void main() {\n" +
                "        System.out.println(\"1111\");\n" +
                "        //HttpUtil.Result result = httpUtil.get(\"http://www.bilibili.com\");\n" +
                "        //System.out.println(result.getResult());\n" +
                "    }\n" +
                "    \n" +
                "    @Filter(atBot = true)\n" +
                "    public static void filter() {\n" +
                "        System.out.println(\"filter\");\n" +
                "    }\n" +
                "    \n" +
                "    @OnGroup()\n" +
                "    public static void group() {\n" +
                "        System.out.println(\"group\");\n" +
                "    }\n" +
                "    \n" +
                "    @Cron(schedulingPattern = \"*/2 * * * * *\",title = \"cron测试\",description = \"cron描述测试\")"+
                "    public static void cronTest() {\n" +
                "        System.out.println(\"cronTest!!!!!!!!!!\");\n" +
                "    }\n" +
                "    @Cron(schedulingPattern = \"*/3 * * * * *\",title = \"cron测试\",description = \"cron描述测试\")"+
                "    public static void cronTest2() {\n" +
                "        System.out.println(\"cronTest2???????????\");\n" +
                "    }\n" +
                "    \n" +
                "}\n";
        Map<String,String> javaInfo = new HashMap<>();
        javaInfo.put(javaName,java);
        Map<String, byte[]> compile = springBootClassLoader.compile(javaInfo);
        Class<?> aClass = springBootClassLoader.loadClass(javaName, compile);
        Object o = ReflectUtil.newInstance(aClass);
        ReflectUtil.invoke(o,"main");
        Method[] method = ReflectUtil.getMethods(aClass);
        Arrays.stream(method).forEach(method1 -> {
            //有用，把aClass注入bean，后可以Filter
            Filter filter = method1.getAnnotation(Filter.class);
            if (filter != null) {

            }
            OnGroup onGroup = method1.getAnnotation(OnGroup.class);
            if (onGroup != null) {
                ReflectUtil.invoke(o,method1.getName());
            }
            Cron annotation = method1.getAnnotation(Cron.class);
            if (annotation != null && ("void").equals(method1.getReturnType().getName())) {
                String schedule = CronUtil.schedule(annotation.schedulingPattern(), (Task) () -> ReflectUtil.invoke(o, method1.getName()));
                System.err.println("scheduleId："+schedule);
            }
        });
        CronUtil.setMatchSecond(true);
        CronUtil.start();
        //会动态加载定时任务
        //CronUtil.schedule("*/5 * * * * *", (Task) () -> System.out.println("test~~~~~~"));


        //API测试
//        System.out.println("\n\n\n\n\n===================API测试===================");
//        String java_version = RuntimeUtil.execForStr("java -version");
//        System.out.println(java_version);
//        System.out.print("=========================分割线========================");
//
//        List<String> strings = RuntimeUtil.execForLines("java -version");
//        strings.forEach(System.out::println);
//
//
//        OsInfo osInfo = SystemUtil.getOsInfo();
//        System.out.println(osInfo.toString());
//        JavaInfo javaInfo1 = SystemUtil.getJavaInfo();
//        System.out.println(javaInfo1);
//        UserInfo userInfo = SystemUtil.getUserInfo();
//        System.out.println(userInfo);


    }


}
