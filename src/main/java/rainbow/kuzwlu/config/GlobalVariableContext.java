package rainbow.kuzwlu.config;

import org.springframework.stereotype.Component;
import rainbow.kuzwlu.core.plugins.PluginsRuntime;
import rainbow.kuzwlu.utils.*;
import rainbow.kuzwlu.utils.quartz.QuartzUtil;

@Component
public class GlobalVariableContext {

    private static final GlobalVariableContext globalVariableContext = new GlobalVariableContext();

    private GlobalVariableContext(){};

    public static GlobalVariableContext getInstance(){
        return globalVariableContext;
    }

    private HttpUtil httpUtil;

    private JavassistUtil javassistUtil;

    private PathUtil pathUtil;

    private ThreadUtil threadPool;

    private SpringBeanUtil springBeanUtil;

    private QuartzUtil quartzUtil;

    private PluginsRuntime pluginsRuntime;

    public HttpUtil getHttpUtil() {
        return httpUtil;
    }

    protected void setHttpUtil(HttpUtil httpUtil) {
        this.httpUtil = httpUtil;
    }

    public JavassistUtil getJavassistUtil() {
        return javassistUtil;
    }

    protected void setJavassistUtil(JavassistUtil javassistUtil) {
        this.javassistUtil = javassistUtil;
    }

    public PathUtil getPathUtil() {
        return pathUtil;
    }

    protected void setPathUtil(PathUtil pathUtil) {
        this.pathUtil = pathUtil;
    }

    public ThreadUtil getThreadPool() {
        return threadPool;
    }

    protected void setThreadPool(ThreadUtil threadPool) {
        this.threadPool = threadPool;
    }

    public SpringBeanUtil getSpringBeanUtil() {
        return springBeanUtil;
    }

    protected void setSpringBeanUtil(SpringBeanUtil springBeanUtil) {
        this.springBeanUtil = springBeanUtil;
    }

    public QuartzUtil getQuartzUtil() {
        return quartzUtil;
    }

    protected void setQuartzUtil(QuartzUtil quartzUtil) {
        this.quartzUtil = quartzUtil;
    }

    public PluginsRuntime getPluginsRuntime() {
        return pluginsRuntime;
    }

    protected void setPluginsRuntime(PluginsRuntime pluginsRuntime) {
        this.pluginsRuntime = pluginsRuntime;
    }
}
