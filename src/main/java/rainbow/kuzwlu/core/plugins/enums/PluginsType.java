package rainbow.kuzwlu.core.plugins.enums;

public enum PluginsType {

    JAVA("java");

    private final String value;

    PluginsType(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}
