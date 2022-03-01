package rainbow.kuzwlu.pluginsBot;

import lombok.SneakyThrows;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MsgGet;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.bot.BotManager;
import rainbow.kuzwlu.config.UserConfig;
import rainbow.kuzwlu.core.plugins.PluginsRuntime;
import rainbow.kuzwlu.core.plugins.compiler.java.InvokeObject;
import rainbow.kuzwlu.core.plugins.compiler.java.JavaScriptCompile;
import rainbow.kuzwlu.core.plugins.enums.PluginsType;
import rainbow.kuzwlu.pluginsBot.interfaces.ListenPlugins;
import rainbow.kuzwlu.pluginsBot.interfaces.MenuPlugins;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Menu {

    private static JavaScriptCompile javaScriptCompile =(JavaScriptCompile)  PluginsRuntime.getRuntime().getScriptCompile(PluginsType.JAVA);

    public static void adminMenu(MsgGet msgGet, Map<Class,Object> invokeParams){
        MsgSender sender = ((RainbowBotManager) invokeParams.get(RainbowBotManager.class)).getDefaultBot().getSender();
        boolean msgFlag = false;
        if (msgGet instanceof GroupMsg) msgFlag = true;
        String msg = msgGet.getText();
        if (msg.endsWith("菜单")){
            javaScriptCompile.getPluginsRunList().forEach(pluginsName ->{
                if (msg.substring(0,msg.length()-2).equals(pluginsName)){
                    if (msg.equals(pluginsName+"菜单")){
                        InvokeObject.invokeByMethodName(pluginsName,"sendMenu", MenuPlugins.class,invokeParams);
                    }
                }
            });
        }
        if (UserConfig.getInstance().getAdmin_list().indexOf(msgGet.getAccountInfo().getAccountCode()) == -1 && !msgGet.getAccountInfo().getAccountCode().equals(sender.GETTER.getBotInfo().getBotCode())){
            if (msg.endsWith("菜单")){
                javaScriptCompile.getPluginsRunList().forEach(pluginsName ->{
                    if (msg.substring(0,msg.length()-2).equals(pluginsName)){
                        if (msg.equals(pluginsName+"菜单")){
                            InvokeObject.invokeByMethodName(pluginsName,"sendMenu", MenuPlugins.class,invokeParams);
                        }
                    }
                });
            }
            return;
        }
        if (msg.equals("菜单")){
            sendMsg(msgFlag,msgGet,sender,"菜单如下：\n1、插件列表\n2、加载插件[插件名]\n3、加载所有插件\n4、卸载插件[插件名]\n5、卸载所有插件");
            return;
        }
        if (msg.startsWith("加载插件")){
            String substring = msg.substring(4);
            javaScriptCompile.init(substring);
            InvokeObject.invokeByMethodName(substring,"init", ListenPlugins.class,invokeParams);
            return;
        }
        if (msg.equals("加载所有插件")){
            javaScriptCompile.initAll();
            InvokeObject.invokeAllByMethodName("init", ListenPlugins.class,invokeParams);
            return;
        }
        if (msg.startsWith("卸载插件")){
            String substring = msg.substring(4);
            InvokeObject.invokeByMethodName(substring,"destroy",ListenPlugins.class,invokeParams);
            javaScriptCompile.destroy(substring);
            return;
        }
        if (msg.equals("卸载所有插件")){
            InvokeObject.invokeAllByMethodName("destroy",ListenPlugins.class,invokeParams);
            javaScriptCompile.destroyAll();
            return;
        }
        if (msg.equals("插件列表")){
            StringBuilder sb = new StringBuilder();
            sb.append("插件列表:\n");
            javaScriptCompile.getPluginsTotalList().forEach(pluginsName-> pluginsList(javaScriptCompile.getPluginsRunList(), txtModule -> txtModule.contains(pluginsName) ? sb.append(pluginsName + "\t[已加载]\n") : sb.append(pluginsName + "\t[已卸载]\n")));
            sendMsg(msgFlag,msgGet,sender,sb.substring(0,sb.length()-1));
            return;
        }
    }

    private static StringBuilder pluginsList(List runList, Function<List, StringBuilder> function) {
        return function.apply(runList);
    }

    private static void sendMsg(boolean msgFlag,MsgGet msgGet, MsgSender sender,String msg){
        if (msgFlag){
            sender.SENDER.sendGroupMsg(((GroupMsg)msgGet).getGroupInfo().getGroupCode(),msg);
        }else{
            sender.SENDER.sendPrivateMsg(msgGet.getAccountInfo().getAccountCode(),msg);
        }
    }

}
