package rainbow.kuzwlu.config;

import love.forte.common.configuration.impl.YamlParser;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import rainbow.kuzwlu.enums.UserConfigEnum;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author kuzwlu
 * @Description TODO
 * @Date 2021/4/1 14:25
 * @Version 1.0
 */

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class InitRunner implements ApplicationRunner {

    private UserConfig userConfig = UserConfig.getInstance();

    @Resource
    private Environment environment;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        userConfig.setMaster(getPropertyString(UserConfigEnum.MASTER.getValue()));
        userConfig.setLogDir(getPropertyString(UserConfigEnum.LOG_DIR.getValue()));
        userConfig.setJavaDir(getPropertyString(UserConfigEnum.JAVA_DIR.getValue()));
        userConfig.setBlackList(getPropertyList(UserConfigEnum.BLACK_LIST.getValue()));
        userConfig.setWriteList(getPropertyList(UserConfigEnum.WRITE_LIST.getValue()));
    }

    public String getPropertyString(String value) {
        return environment.getProperty(value);
    }

    public List getPropertyList(String value){
        return environment.getProperty(value,List.class);
    }

    public Map getPropertyMap(String value) {
        return environment.getProperty(value,Map.class);
    }

}
