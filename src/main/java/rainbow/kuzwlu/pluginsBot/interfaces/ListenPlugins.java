package rainbow.kuzwlu.pluginsBot.interfaces;

import rainbow.kuzwlu.pluginsBot.BotTools;
import rainbow.kuzwlu.core.plugins.annotation.RainbowPlugins;
import rainbow.kuzwlu.pluginsBot.RainbowBotManager;

public interface ListenPlugins {

    void init(RainbowBotManager rainbowBotManager, RainbowPlugins rainbowPlugins, BotTools botTools);

    void destroy(RainbowBotManager rainbowBotManager,RainbowPlugins rainbowPlugins,BotTools botTools);

}
