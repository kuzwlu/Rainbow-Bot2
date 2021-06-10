package rainbow.kuzwlu.listener.groupListener;

import catcode.CatCodeUtil;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.containers.GroupAccountInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.MsgSender;
import rainbow.kuzwlu.config.UserConfig;
import rainbow.kuzwlu.core.SpringBootClassLoader;

import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author kuzwlu
 * @Description TODO
 * @Date 2021/3/6 09:02
 * @Version 1.0
 */

@Beans
public class GroupListener {

    private UserConfig userConfig = UserConfig.getInstance();

    @OnGroup
    public void groupMessage(GroupMsg groupMsg , MsgSender sender, CatCodeUtil catCodeUtil) {
        GroupAccountInfo accountInfo = groupMsg.getAccountInfo();
        String groupCode = groupMsg.getGroupInfo().getGroupCode();
        if ("955877318".equals(groupCode)) {
            if("2601029774".equals(accountInfo.getAccountCode())) {
                //sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(),"lsp");
            }
            //System.out.println("群聊："+groupMsg.getGroupInfo().getGroupName()+"("+groupMsg.getGroupInfo().getGroupCode()+")\t 发信人："+accountInfo.getAccountNickname()+"("+accountInfo.getAccountCode()+")\t 消息内容："+groupMsg.getMsg());
        }
        if ("967627696".equals(groupCode) || "955877318".equals(groupCode)) {
            System.out.println("群聊："+groupMsg.getGroupInfo().getGroupName()+"("+groupMsg.getGroupInfo().getGroupCode()+")\t 发信人："+accountInfo.getAccountNickname()+"("+accountInfo.getAccountCode()+")\t 消息内容："+groupMsg.getMsg());
            if (groupMsg.getMsg().startsWith("发送")) {
                String code = groupMsg.getMsg().substring(2);
                SpringBootClassLoader springBootClassLoader = new SpringBootClassLoader((URLClassLoader) Thread.currentThread().getContextClassLoader());
                String javaName = "Send";
                String java = "package rainbow.kuzwlu.core;\n" +
                        "\n" +
                        "\n" +
                        "import catcode.CatCodeUtil;\n" +
                        "import love.forte.simbot.api.message.events.GroupMsg;\n" +
                        "import love.forte.simbot.api.sender.MsgSender;\n" +
                        "\n" +
                        "public class Send {\n" +
                        "\n" +
                        "    public void sendMsg(GroupMsg groupMsg, MsgSender sender, CatCodeUtil catCodeUtil){\n" +
                        "        sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(),\""+code+"\");\n" +
                        "    }\n" +
                        "\n" +
                        "}";
                Map<String,String> javaInfo = new HashMap<>();
                javaInfo.put("rainbow.kuzwlu.core."+javaName,java);
                try {
                    Map<String, byte[]> compile = springBootClassLoader.compile(javaInfo);
                    Class<?> aClass = springBootClassLoader.loadClass("rainbow.kuzwlu.core." + javaName, compile);
                    Object o = aClass.newInstance();
                    Method[] methods = aClass.getMethods();
                    for (Method method : methods) {
                        if (method.getName().equals("sendMsg")) {
                            method.invoke(o, groupMsg, sender,catCodeUtil);
                        }
                    }
                }catch (Exception e){
                    System.err.println(e);
                }
            }
        }
        //System.out.println("群聊："+groupMsg.getGroupInfo().getGroupName()+"("+groupMsg.getGroupInfo().getGroupCode()+")\t 发信人："+accountInfo.getAccountNickname()+"("+accountInfo.getAccountCode()+")\t 消息内容："+groupMsg.getText());
    }

}
