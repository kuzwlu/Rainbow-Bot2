package rainbow.kuzwlu.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface OnNudged {

    @Retention(RetentionPolicy.RUNTIME)
    @interface ByFriend{

    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface ByMember{

    }

}
