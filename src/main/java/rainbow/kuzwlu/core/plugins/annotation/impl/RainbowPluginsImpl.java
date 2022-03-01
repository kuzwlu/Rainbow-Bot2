package rainbow.kuzwlu.core.plugins.annotation.impl;

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
    public developers[] developers() {
        return new developers[0];
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
