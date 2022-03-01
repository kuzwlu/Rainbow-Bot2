package rainbow.kuzwlu.pluginsBot;

import love.forte.simbot.bot.Bot;
import love.forte.simbot.bot.BotManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RainbowBotManager{

    private BotManager botManager;

    public RainbowBotManager(BotManager botManager){
        this.botManager = botManager;
    }

    public List<Bot> getBots() {
        return botManager.getBots();
    }

    public Bot getDefaultBot() {
        return botManager.getDefaultBot();
    }

    public Bot getBot(@NotNull String id) {
        return botManager.getBot(id);
    }

    public Bot getBotOrNull(@NotNull String id) {
        return botManager.getBotOrNull(id);
    }
}
