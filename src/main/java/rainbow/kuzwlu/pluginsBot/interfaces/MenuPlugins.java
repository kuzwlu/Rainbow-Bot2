package rainbow.kuzwlu.pluginsBot.interfaces;

import love.forte.simbot.api.message.events.MsgGet;
import rainbow.kuzwlu.pluginsBot.BotTools;
import rainbow.kuzwlu.core.plugins.annotation.RainbowPlugins;
import rainbow.kuzwlu.pluginsBot.RainbowBotManager;

public interface MenuPlugins {

    void sendMenu(RainbowBotManager rainbowBotManager,MsgGet msgGet, RainbowPlugins rainbowPlugins, BotTools botTools);

}
