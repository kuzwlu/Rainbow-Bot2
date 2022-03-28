package rainbow.kuzwlu.core.plugins.annotation.impl;

import love.forte.simbot.annotation.Listens;
import love.forte.simbot.constant.PriorityConstant;
import rainbow.kuzwlu.core.plugins.annotation.RainbowPlugins;

import java.lang.annotation.Annotation;

public class RainbowPluginsImpl implements RainbowPlugins {
    @Override
    public String id() {
        return null;
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public String version() {
        return null;
    }

    @Override
    public Priority priority() {
        return null;
    }

    @Override
    public Developers[] developers() {
        return new Developers[0];
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
