package rainbow.kuzwlu.config;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author kuzwlu
 * @Description TODO
 * @Date 2021/3/8 22:26
 * @Version 1.0
 */

@Component
public class UserConfig {

    private static final UserConfig userConfig = new UserConfig();

    private UserConfig() { }

    public static UserConfig getInstance() {
        return userConfig;
    }

    private List<String> admin_list;

    private String log_dir;

    private Boolean start_flag;

    private List<String> black_list;

    private List<String> write_list;

    public List<String> getAdmin_list() {
        return admin_list;
    }

    protected void setAdmin_list(List<String> admin_list) {
        this.admin_list = admin_list;
    }

    public String getLog_dir() {
        return log_dir;
    }

    protected void setLog_dir(String log_dir) {
        this.log_dir = log_dir;
    }

    public Boolean getStart_flag() {
        return start_flag;
    }

    protected void setStart_flag(Boolean start_flag) {
        this.start_flag = start_flag;
    }

    public List<String> getBlack_list() {
        return black_list;
    }

    protected void setBlack_list(List<String> black_list) {
        this.black_list = black_list;
    }

    public List<String> getWrite_list() {
        return write_list;
    }

    protected void setWrite_list(List<String> write_list) {
        this.write_list = write_list;
    }

}
