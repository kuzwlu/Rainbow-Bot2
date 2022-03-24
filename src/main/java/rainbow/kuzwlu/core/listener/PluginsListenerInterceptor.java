package rainbow.kuzwlu.core.listener;

import love.forte.simbot.intercept.InterceptionType;
import love.forte.simbot.listener.ListenerInterceptContext;
import love.forte.simbot.listener.ListenerInterceptor;
import org.jetbrains.annotations.NotNull;

public class PluginsListenerInterceptor implements ListenerInterceptor {
    @NotNull
    @Override
    public InterceptionType intercept(@NotNull ListenerInterceptContext context) {
        return InterceptionType.PASS;
    }
}
