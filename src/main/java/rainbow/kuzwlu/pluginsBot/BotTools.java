package rainbow.kuzwlu.pluginsBot;

import catcode.CatCodeUtil;
import lombok.Builder;
import rainbow.kuzwlu.config.UserConfig;
import rainbow.kuzwlu.utils.HttpUtil;
import rainbow.kuzwlu.utils.ThreadUtil;
import rainbow.kuzwlu.utils.quartz.QuartzUtil;

@Builder
public class BotTools {

    private CatCodeUtil catCodeUtil;

    private UserConfig userConfig;

    private HttpUtil httpUtil;

    private ThreadUtil threadUtil;

    private QuartzUtil quartzUtil;

    public CatCodeUtil getCatCodeUtil() {
        return catCodeUtil;
    }

    protected void setCatCodeUtil(CatCodeUtil catCodeUtil) {
        this.catCodeUtil = catCodeUtil;
    }

    public UserConfig getUserConfig() {
        return userConfig;
    }

    protected void setUserConfig(UserConfig userConfig) {
        this.userConfig = userConfig;
    }

    public HttpUtil getHttpUtil() {
        return httpUtil;
    }

    protected void setHttpUtil(HttpUtil httpUtil) {
        this.httpUtil = httpUtil;
    }

    public ThreadUtil getThreadUtil() {
        return threadUtil;
    }

    protected void setThreadUtil(ThreadUtil threadUtil) {
        this.threadUtil = threadUtil;
    }

    public QuartzUtil getQuartzUtil() {
        return quartzUtil;
    }

    protected void setQuartzUtil(QuartzUtil quartzUtil) {
        this.quartzUtil = quartzUtil;
    }
}
