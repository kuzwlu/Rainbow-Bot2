package rainbow.kuzwlu.core.listener;

import catcode.CatCodeUtil;
import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.bot.BotManager;
import love.forte.simbot.intercept.InterceptionType;
import love.forte.simbot.listener.*;
import org.jetbrains.annotations.NotNull;
import rainbow.kuzwlu.config.GlobalVariableContext;
import rainbow.kuzwlu.config.UserConfig;
import rainbow.kuzwlu.pluginsBot.RainbowBotManager;
import rainbow.kuzwlu.pluginsBot.SwitchListener;
import rainbow.kuzwlu.utils.HttpUtil;
import rainbow.kuzwlu.utils.ThreadUtil;
import rainbow.kuzwlu.utils.quartz.QuartzUtil;

import java.util.HashMap;
import java.util.Map;

@Beans
public class ScriptListenerInterceptor implements ListenerInterceptor {

    @Depend
    private BotManager botManager;

    @NotNull
    @Override
    public InterceptionType intercept(@NotNull ListenerInterceptContext context) {
        Map<Class,Object> objectClassMap = new HashMap<>();
        objectClassMap.put(RainbowBotManager.class, new RainbowBotManager(botManager));
        objectClassMap.put(CatCodeUtil.class,CatCodeUtil.getInstance());
        //objectClassMap.put(UserConfig.class,UserConfig.getInstance());
        objectClassMap.put(HttpUtil.class,GlobalVariableContext.getInstance().getHttpUtil());
        objectClassMap.put(ThreadUtil.class,GlobalVariableContext.getInstance().getThreadPool());
        objectClassMap.put(QuartzUtil.class,GlobalVariableContext.getInstance().getQuartzUtil());
        SwitchListener.choose(context.getMsgGet(),objectClassMap);
        return InterceptionType.PASS;
    }
}
