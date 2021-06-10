package rainbow.kuzwlu.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author kuzwlu
 * @Description TODO
 * @Date 2021/3/8 22:26
 * @Version 1.0
 */

@Data
@ToString
@Component
public class UserConfig {

    private static final UserConfig userConfig = new UserConfig();

    private UserConfig() { }

    public static UserConfig getInstance() {
        return userConfig;
    }

    private String master;

    private String logDir;

    private String javaDir;

    private List<String> blackList;

    private List<String> writeList;

}
