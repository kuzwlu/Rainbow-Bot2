package rainbow.kuzwlu.enums;

/**
 * @Author kuzwlu
 * @Description TODO
 * @Date 2021/4/1 14:31
 * @Version 1.0
 */

public enum UserConfigEnum {

    MASTER("rainbow.kuzwlu.master"),
    LOG_DIR("rainbow.kuzwlu.logDir"),
    JAVA_DIR("rainbow.kuzwlu.javaDir"),
    BLACK_LIST("rainbow.kuzwlu.blackList"),
    WRITE_LIST("rainbow.kuzwlu.writeList");

    private String value;

    UserConfigEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
