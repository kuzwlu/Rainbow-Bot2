package rainbow.kuzwlu.pluginsBot.interfaces;

import catcode.CatCodeUtil;
import love.forte.simbot.api.message.events.MsgGet;
import love.forte.simbot.bot.BotManager;
import rainbow.kuzwlu.core.plugins.annotation.RainbowPlugins;
import rainbow.kuzwlu.pluginsBot.RainbowBotManager;

public interface MenuPlugins {

    void sendMenu(RainbowBotManager rainbowBotManager, MsgGet msgGet, CatCodeUtil catCodeUtil, RainbowPlugins rainbowPlugins);

}
