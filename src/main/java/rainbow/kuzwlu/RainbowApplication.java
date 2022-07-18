package rainbow.kuzwlu;

import love.forte.simbot.annotation.SimbotApplication;
import love.forte.simbot.annotation.SimbotResource;
import love.forte.simbot.core.SimbotApp;
import love.forte.simbot.core.SimbotContext;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import rainbow.kuzwlu.config.GlobalVariableContext;
import rainbow.kuzwlu.pluginsBot.BotTools;
import rainbow.kuzwlu.core.plugins.annotation.Cron;
import rainbow.kuzwlu.core.plugins.compiler.java.InvokeObject;
import rainbow.kuzwlu.pluginsBot.RainbowBotManager;
import rainbow.kuzwlu.pluginsBot.interfaces.ListenPlugins;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author kuzwlu
 * @Description TODO
 * @Date 2021/3/21 14:22
 * @Version 1.0
 */

@SimbotApplication(@SimbotResource(value = "application.yml"))
@ComponentScan("rainbow.kuzwlu")
public class RainbowApplication {

    public static void main(String[] args) {
        SpringApplication.run(RainbowApplication.class, args);
        SimbotContext context = SimbotApp.run(new RainbowApplication(), args);

        Map<Class,Object> invokeParams = new HashMap<>();
        invokeParams.put(RainbowBotManager.class, new RainbowBotManager(context.getBotManager()));
        invokeParams.put(BotTools.class, GlobalVariableContext.getInstance().getBotTools());

        InvokeObject.invokeAllByMethodName("init", ListenPlugins.class, invokeParams);
        //执行定时任务
        InvokeObject.invokeAllByAnnotation(Cron.class, invokeParams, true);
    }

}
