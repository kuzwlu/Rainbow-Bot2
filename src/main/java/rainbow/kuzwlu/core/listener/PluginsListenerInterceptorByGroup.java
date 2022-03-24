package rainbow.kuzwlu.core.listener;

import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.bot.BotManager;
import love.forte.simbot.core.intercept.FixedRangeGroupedListenerInterceptor;
import love.forte.simbot.intercept.InterceptionType;
import love.forte.simbot.listener.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rainbow.kuzwlu.config.GlobalVariableContext;
import rainbow.kuzwlu.pluginsBot.BotTools;
import rainbow.kuzwlu.pluginsBot.RainbowBotManager;
import rainbow.kuzwlu.pluginsBot.SwitchListener;

import java.util.HashMap;
import java.util.Map;

@Beans
public class PluginsListenerInterceptorByGroup extends FixedRangeGroupedListenerInterceptor {

    @Depend
    private BotManager botManager;


    @NotNull
    @Override
    protected String[] getGroupRange() {
        return new String[]{"Plugins"};
    }

    @NotNull
    @Override
    protected InterceptionType doIntercept(@NotNull ListenerInterceptContext context, @Nullable String group) {
        if (group.equals("Plugins")) {
            Map<Class, Object> objectClassMap = new HashMap<>();
            objectClassMap.put(RainbowBotManager.class, new RainbowBotManager(botManager));
            objectClassMap.put(MsgSender.class,botManager.getDefaultBot().getSender());
            objectClassMap.put(BotTools.class, GlobalVariableContext.getInstance().getBotTools());
            SwitchListener.choose(context.getMsgGet(), objectClassMap);
        }
        return InterceptionType.PASS;
    }
}
