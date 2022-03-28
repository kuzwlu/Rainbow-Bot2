package rainbow.kuzwlu.core.plugins.annotation;

import love.forte.simbot.annotation.Listens;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface RainbowPlugins {

    String id();

    String name();

    String description();

    String version();

    Priority priority();

    Developers[] developers();

    @interface Priority{
        int value() default Listens.DEFAULT_PRIORITY;
    }

    @interface Developers{
        String name();

        String roles();

        String email();

        String contact();

        String website();
    }

}

