package rainbow.kuzwlu.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import rainbow.kuzwlu.RainbowApplication;
import rainbow.kuzwlu.config.enums.UserConfigEnum;
import rainbow.kuzwlu.core.plugins.PluginsRuntime;
import rainbow.kuzwlu.utils.*;
import rainbow.kuzwlu.utils.config.UtilsUserConfig;
import rainbow.kuzwlu.utils.quartz.QuartzUtil;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @Author kuzwlu
 * @Description TODO
 * @Date 2021/4/1 14:25
 * @Version 1.0
 */

@Configuration
public class InitRunner implements ApplicationRunner {

    private UserConfig userConfig = UserConfig.getInstance();

    private GlobalVariableContext globalVariableContext = GlobalVariableContext.getInstance();

    @Resource
    private Environment environment;

    @Resource
    private SpringBeanUtil springBeanUtil;

    @Override
    public void run(ApplicationArguments args) {
        //配置用户设置
        userConfig.setAdmin_list(Arrays.stream(PropertiesUtil.getProperty(environment,UserConfigEnum.ADMIN_LIST.getValue(),String.class).split(" ")).map(s->s.replaceAll("-","")).distinct().collect(Collectors.toList()));
        userConfig.setStart_flag(PropertiesUtil.getProperty(environment,UserConfigEnum.START_FLAG.getValue(),Boolean.class));
        userConfig.setLog_dir(UtilsUserConfig.getInstance().getLogDir());
        userConfig.setWrite_list(UtilsUserConfig.getInstance().getWriteList());
        userConfig.setBlack_list(UtilsUserConfig.getInstance().getBlackList());
        //设置全局变量(以后使用注解注入)
        globalVariableContext.setHttpUtil(HttpUtil.getInstance());
        globalVariableContext.setJavassistUtil(JavassistUtil.getInstance());
        globalVariableContext.setPathUtil(PathUtil.getInstance(RainbowApplication.class));
        globalVariableContext.setThreadPool(ThreadUtil.getInstance());
        globalVariableContext.setSpringBeanUtil(springBeanUtil);
        globalVariableContext.setQuartzUtil(QuartzUtil.getInstance());
        //初始化插件中心
        globalVariableContext.setPluginsRuntime(PluginsRuntime.getRuntime());

    }

}
