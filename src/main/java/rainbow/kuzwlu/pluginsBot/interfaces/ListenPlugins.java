package rainbow.kuzwlu.pluginsBot.interfaces;

import catcode.CatCodeUtil;
import love.forte.simbot.bot.BotManager;
import rainbow.kuzwlu.core.plugins.annotation.RainbowPlugins;
import rainbow.kuzwlu.pluginsBot.RainbowBotManager;

public interface ListenPlugins {

    void init(RainbowBotManager rainbowBotManager, CatCodeUtil catCodeUtil, RainbowPlugins rainbowPlugins);

    void destroy(RainbowBotManager rainbowBotManager, CatCodeUtil catCodeUtil,RainbowPlugins rainbowPlugins);

}
