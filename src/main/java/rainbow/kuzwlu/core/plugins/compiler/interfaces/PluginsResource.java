package rainbow.kuzwlu.core.plugins.compiler.interfaces;

import rainbow.kuzwlu.core.plugins.annotation.RainbowPlugins;
import rainbow.kuzwlu.core.plugins.enums.PluginsType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public interface PluginsResource {

    List<String> pluginsTotalList = new CopyOnWriteArrayList<>();

    List<String> pluginsRunList = new CopyOnWriteArrayList<>();

    default List<String> getPluginsTotalList(){
        return pluginsTotalList.stream().distinct().collect(Collectors.toList());
    }

    default List<String> getPluginsRunList(){
        return pluginsRunList.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 获取每个插件目录
     * @return Map<String,File>  插件名，插件目录File
     */
    Map<String, File> getPluginsResource();

    /**
     * 获取插件下所有编译文件
     * @return Map<String, String>  文件名，文件字符串内容
     */
    Map<String, Map<String, String>> getPluginsString();

    /**
     * 设置插件编译类型
     * @return PluginsType
     */
    PluginsType getPluginsType();

    /**
     * 获取单个插件文本内容
     * @param pluginsName
     * @return
     */
    Map<String, String> getPluginsString(String pluginsName);

    /**
     * 设置插件运行列表
     * @param pluginsName
     */
    void setPluginsRunList(String pluginsName);

    /**
     * 移除插件运行列表
     * @param pluginsName
     */
    void removePluginsRunList(String pluginsName);

    /**
     * 获取插件开发者信息
     * @return
     */
    RainbowPlugins getPluginsDevelopers(List<Object> objectLists);

    /**
     * 卸载插件
     * @param pluginsName
     */
    void destroy(String pluginsName);

    void destroyAll();

    /**
     * 加载插件
     * @param pluginsName
     */
    void init(String pluginsName);

}
