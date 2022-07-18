package rainbow.kuzwlu.core.listener;

import catcode.CatCodeUtil;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Async;
import love.forte.simbot.annotation.ListenBreak;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.component.mirai.message.event.MiraiNudgedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rainbow.kuzwlu.config.UserConfig;
import rainbow.kuzwlu.core.annotation.OnNudged;
import rainbow.kuzwlu.core.plugins.compiler.RainbowClassLoader;
import rainbow.kuzwlu.utils.*;
import rainbow.kuzwlu.utils.quartz.QuartzUtil;
import rainbow.kuzwlu.utils.quartz.Task;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Beans
public class ExecListener {

    private static final Logger logger = LogManager.getLogger(ExecListener.class);

    private String classNamePattern = "class .[a-zA-Z0-9]*";

    private String path = PathUtil.getInstance(ExecListener.class).getPath("ExecListener/openGroup.txt");

    private ThreadUtil threadUtil = ThreadUtil.getInstance();

    private String className;

    private Object result;

    private Integer execTime = 5;

    @OnGroup
    @Async
    public void execJava(GroupMsg groupMsg, Sender sender){
        String groupCode = groupMsg.getGroupInfo().getGroupCode();
        String sendCode = groupMsg.getAccountInfo().getAccountCode();
        List<String> openGroup = FileTXTUtil.loadFileList(path);
        boolean atFlag = true;
        if (openGroup.contains(groupCode)){
            if(groupMsg.getMsg().startsWith("#java~\n") || groupMsg.getMsg().startsWith("#java~~\n")){
                String java = "";
                if (groupMsg.getMsg().startsWith("#java~~\n")){
                    atFlag = false;
                    java = groupMsg.getText().substring(8).trim().replace("\u2028","\n");
                }else {
                    java = groupMsg.getText().substring(7).trim().replace("\u2028","\n");
                }
                if (!java.contains("public class ")){
                    java = java.replace("class ","public class ");
                }
                if (java.contains("for(;;)") || java.contains("while(true)")){
                    sender.sendGroupMsg(groupCode, CatCodeUtil.getInstance().toCat("at", "code=" + sendCode) + " \nCPU会爆的啦QwQ");
                    return;
                }
                String finalJava = java;
                boolean finalAtFlag = atFlag;
                Future<?> future = threadUtil.executorService.submit(() -> {
                    exec(finalJava,groupCode,sendCode,sender, finalAtFlag);
                });
                try {
                    future.get(execTime,TimeUnit.SECONDS);
                } catch (Exception e) {
                    sender.sendGroupMsg(groupCode, CatCodeUtil.getInstance().toCat("at", "code=" + sendCode) + " 运行出错：\n程序运行超时,已被清理");
                    logger.error(e);
                }
            }
        }
    }

    public void exec(String finalJava,String groupCode,String sendCode,Sender sender,boolean atFlag){
            Pattern classCompile = Pattern.compile(classNamePattern);
            Matcher matcher = classCompile.matcher(finalJava);
            if (matcher.find()) {
                className = matcher.group(0).replace("class ", "");
            } else {
                sender.sendGroupMsg(groupCode, CatCodeUtil.getInstance().toCat("at", "code=" + sendCode) + " 类名错误，请检查");
                return;
            }
            String info = "运行人：" + sendCode + "\t运行时间：" + getDate() + "\t运行类名：" + className + "\t运行代码：" + finalJava;
            LogUtil.saveLogToFile(info + "\n", "ExecListener/log/" + getDate() + "/code.txt");
            Map<String, String> javaInfo = new HashMap<>();
            javaInfo.put(className, finalJava);
            Map<String, byte[]> compile;
            RainbowClassLoader rainbowClassLoader = new RainbowClassLoader((URLClassLoader) Thread.currentThread().getContextClassLoader());
            try {
                compile = rainbowClassLoader.compile(javaInfo, null);
            } catch (Exception e) {
                sender.sendGroupMsg(groupCode, CatCodeUtil.getInstance().toCat("at", "code=" + sendCode) + " 编译出错：\n" + e.getMessage().replace(className + ".java:", "行"));
                LogUtil.saveLogToFile(info + "\n" + LogUtil.exceptionLog(e), "ExecListener/log/" + getDate() + "/compile.txt");
                return;
            }
            try {
                JavassistUtil.getInstance().safetyCheck(compile);
            } catch (Exception e) {
                logger.error(e.getMessage());
                logger.error(info);
                LogUtil.saveLogToFile(info + "\n" + LogUtil.exceptionLog(e), "ExecListener/log/" + getDate() + "/javassist.txt");
                //sender.sendGroupMsg(groupCode,CatCodeUtil.getInstance().getCat(sendCode,"at")+" 编译出错：\n"+ e);
                return;
            }
            Class<?> aClass = rainbowClassLoader.loadClass(className, compile);
            for (Method method : aClass.getMethods()) {
                if (method.getName().equals("main")) {
                    try {
                        Object o = aClass.newInstance();
                        if (method.getReturnType().getName().equals("void")) {
                            ByteArrayOutputStream baoStream = new ByteArrayOutputStream(1024);
                            PrintStream cacheStream = new PrintStream(baoStream);
                            PrintStream oldStream = System.out;
                            System.setOut(cacheStream);//不打印到控制台
                            if (method.getParameters().length >= 1) {
                                method.invoke(o, (Object) new String[]{});
                            } else {
                                method.invoke(o);
                            }
                            result = baoStream.toString();
                            System.setOut(oldStream);
                        } else {
                            if (method.getParameters().length >= 1) {
                                method.invoke(o, (Object) new String[]{});
                            } else {
                                method.invoke(o);
                            }
                        }
                        if (atFlag){
                            sender.sendGroupMsg(groupCode, CatCodeUtil.getInstance().toCat("at", "code=" + sendCode) + " 运行结果：\n" + result);
                        }else{
                            sender.sendGroupMsg(groupCode, result+"");
                        }
                    } catch (InvocationTargetException e) {
                        logger.error(info);
                        LogUtil.saveLogToFile(info + "\n" + LogUtil.exceptionLog(e), "ExecListener/log/" + getDate() + "/invoke.txt");
                        sender.sendGroupMsg(groupCode, CatCodeUtil.getInstance().toCat("at", "code=" + sendCode) + " 运行出错：\n" + e.getTargetException());
                        return;
                    } catch (Exception e) {
                        logger.error(info);
                        LogUtil.saveLogToFile(info + "\n" + LogUtil.exceptionLog(e), "ExecListener/log/" + getDate() + "/invoke.txt");
                        sender.sendGroupMsg(groupCode, CatCodeUtil.getInstance().toCat("at", "code=" + sendCode) + " 运行出错：\n" + e);
                        return;
                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    public String getDateCron(){
        Calendar now = Calendar.getInstance();
        StringBuffer sb = new StringBuffer();
        sb.append(now.get(Calendar.SECOND)).append(" ")
                        .append(now.get(Calendar.MINUTE)).append(" ")
                        .append(now.get(Calendar.HOUR_OF_DAY)).append(" ")
                        .append(now.get(Calendar.DAY_OF_MONTH)).append(" ")
                        .append((now.get(Calendar.MONTH) + 1) + " ? ")
                .append(now.get(Calendar.YEAR));
        return sb.toString();
    }

    @OnGroup
    @ListenBreak
    public void setOnOrOff(GroupMsg groupMsg,Sender sender){
        if (groupMsg.getMsg().equals("开启java~") && UserConfig.getInstance().getAdmin_list().contains(groupMsg.getAccountInfo().getAccountCode())){
            FileTXTUtil.add(groupMsg.getGroupInfo().getGroupCode(), path);
        }else if(groupMsg.getMsg().equals("关闭java~") && UserConfig.getInstance().getAdmin_list().contains(groupMsg.getAccountInfo().getAccountCode())){
            FileTXTUtil.del(groupMsg.getGroupInfo().getGroupCode(), path);
        }

    }

    public String getDate(){
       return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

}
