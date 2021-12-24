package rainbow.kuzwlu.config.enums;

/**
 * @Author kuzwlu
 * @Description TODO
 * @Date 2021/4/1 14:31
 * @Version 1.0
 */

public enum UserConfigEnum {

    ADMIN_LIST("rainbow.kuzwlu.bot.admin-list"),
    START_FLAG("rainbow.kuzwlu.bot.start-flag");

    private String value;

    UserConfigEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
