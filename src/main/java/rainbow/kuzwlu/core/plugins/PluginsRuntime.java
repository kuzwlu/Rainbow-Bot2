package rainbow.kuzwlu.core.plugins;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rainbow.kuzwlu.config.UserConfig;
import rainbow.kuzwlu.core.plugins.compiler.interfaces.ScriptCompile;
import rainbow.kuzwlu.core.plugins.compiler.java.JavaScriptCompile;
import rainbow.kuzwlu.core.plugins.enums.PluginsType;
import rainbow.kuzwlu.core.plugins.utils.PluginsDirectoryListener;
import rainbow.kuzwlu.utils.PathUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author kuzwlu
 * @Description 插件中心--保存编译的信息
 * @Date 2021/11/19 14:53
 * @Version 1.0
 */

public class PluginsRuntime {

    private static final Logger logger = LogManager.getLogger(PluginsRuntime.class);

    private Map<PluginsType, ScriptCompile> scriptCompileMap = new HashMap<>();

    /**
     * 应当唯一存在的runtime对象
     */
    private static final class SingletonHolder {
        private static final PluginsRuntime runtime = new PluginsRuntime();
    }

    private PluginsRuntime() {
        initJavaScriptCompile();
        logo();
        pluginsListener();
        //初始化编译
        if (Boolean.TRUE.equals(UserConfig.getInstance().getStart_flag())) {
            getAllPluginsType().forEach(pluginsType -> getScriptCompile(pluginsType).initAll());
        }
    }

    public static PluginsRuntime getRuntime() {
        return SingletonHolder.runtime;
    }

    /**
     * 获取对应的编译类
     * @param pluginsType
     * @return
     */
    public ScriptCompile getScriptCompile(PluginsType pluginsType) {
        return scriptCompileMap.get(pluginsType);
    }

    /**
     * 注册编译类
     * @param pluginsType
     * @param scriptCompile
     */
    public void registerScriptCompile(PluginsType pluginsType,ScriptCompile scriptCompile){
        scriptCompileMap.put(pluginsType,scriptCompile);
        pluginsListener();
    }


    /** java脚本初始化
     */
    public void initJavaScriptCompile() {
        scriptCompileMap.put(PluginsType.JAVA,new JavaScriptCompile());
        //加载插件列表
        getScriptCompile(PluginsType.JAVA).initScriptCompile();
    }

    /**
     * 获取注册的所有编译器
     * @return
     */
    public List<PluginsType> getAllPluginsType() {
        List<PluginsType> pluginsTypeList = new ArrayList<>();
        scriptCompileMap.forEach((pluginsType, scriptCompile) -> pluginsTypeList.add(pluginsType));
        return pluginsTypeList;
    }

    /**
     * logo
     */
    private void logo(){
        String logo = "               _       _                            _ _      \n" +
                "              (_)     | |                          (_) |     \n" +
                " ___  ___ _ __ _ _ __ | |_ ___ ___  _ __ ___  _ __  _| | ___ \n" +
                "/ __|/ __| '__| | '_ \\| __/ __/ _ \\| '_ ` _ \\| '_ \\| | |/ _ \\\n" +
                "\\__ \\ (__| |  | | |_) | || (_| (_) | | | | | | |_) | | |  __/\n" +
                "|___/\\___|_|  |_| .__/ \\__\\___\\___/|_| |_| |_| .__/|_|_|\\___|\n" +
                "                | |                          | |             \n" +
                "                |_|                          |_|             \n";
        logger.info("\n"+logo);
    }

    private void pluginsListener(){
        getAllPluginsType().forEach(pluginsType -> {
            File file = new File(PathUtil.getInstance(PluginsRuntime.class).getPath("plugins/"+pluginsType.getValue().toLowerCase()+"/"));
            // 轮询间隔 5 秒
            long interval = TimeUnit.SECONDS.toMillis(1);
            // 创建过滤器
            IOFileFilter directories = FileFilterUtils.and(
                    FileFilterUtils.directoryFileFilter(),
                    HiddenFileFilter.VISIBLE);
            IOFileFilter filter = FileFilterUtils.or(directories);
            // 使用过滤器
            FileAlterationObserver observer = new FileAlterationObserver(file, filter);
            //不使用过滤器
            observer.addListener(new PluginsDirectoryListener(getScriptCompile(pluginsType)));
            //创建文件变化监听器
            FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);
            // 开始监控
            try {
                monitor.start();
            } catch (Exception e) {
                logger.error("监听插件目录["+file.getPath()+"]失败：\n"+e.getMessage());
            }
            logger.debug("监听插件目录["+file.getPath()+"]");
        });
    }

}
