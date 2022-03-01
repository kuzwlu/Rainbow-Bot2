package rainbow.kuzwlu.core.plugins.compiler.interfaces;

/**
 * @Author kuzwlu
 * @Description TODO
 * @Date 2021/11/19 15:55
 * @Version 1.0
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import rainbow.kuzwlu.core.plugins.annotation.RainbowPlugins;
import rainbow.kuzwlu.core.plugins.annotation.impl.RainbowPluginsImpl;
import rainbow.kuzwlu.pluginsBot.interfaces.ListenPlugins;
import rainbow.kuzwlu.utils.FileTXTUtil;
import rainbow.kuzwlu.utils.PathUtil;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public interface ScriptCompile extends PluginsResource {

    Logger logger = LogManager.getLogger(ScriptCompile.class);

    /**
     * 编译单个插件
     * @return 是否成功
     */
    void compile(@NotNull String pluginsName);

    /**
     * 编译全部插件
     * @return 是否成功
     */
    default void compileAll(){
        getPluginsResource().forEach((pluginsName,file)->{
            compile(pluginsName);
        });
    }

    @Override
    default RainbowPlugins getPluginsDevelopers(List<Object> objectLists){
        AtomicReference<RainbowPlugins> rainbowPlugins = new AtomicReference<>();
        objectLists.stream().filter(obj -> {
            try{
                return obj.getClass().getGenericInterfaces()[0].equals(ListenPlugins.class) && null != obj.getClass().getAnnotation(RainbowPlugins.class);
            }catch (Exception e){
                return false;
            }
        }).forEach(o -> {
            rainbowPlugins.set(o.getClass().getAnnotation(RainbowPlugins.class));
        });
        if (rainbowPlugins.get() == null) return new RainbowPluginsImpl();
        return rainbowPlugins.get();
    }

    void destroyAll();

    /**
     * 加载全部插件
     */
    void initAll();

    /**
     * 获取每个插件目录
     * @return Map<String,File>  插件名，插件目录File
     */
    @Override
    default Map<String, File> getPluginsResource() {
        return Arrays.stream(Objects.requireNonNull(new File(PathUtil.getInstance(ScriptCompile.class).getPath("plugins/" + getPluginsType().getValue().toLowerCase()+"/")).listFiles())).filter(File::isDirectory).filter(file -> !file.isHidden() || !file.getName().startsWith(".")).filter(file -> file.list().length > 0).collect(HashMap::new, (m, file) -> m.put(file.getName(), file), HashMap::putAll);
    }

    /**
     * 保存插件名
     */
    default void initScriptCompile(){
        Arrays.stream(Objects.requireNonNull(new File(PathUtil.getInstance(ScriptCompile.class).getPath("plugins/" + getPluginsType().getValue().toLowerCase()+"/")).listFiles())).filter(File::isDirectory).filter(file -> !file.isHidden() || !file.getName().startsWith(".")).filter(file -> file.list().length > 0).forEach(file -> pluginsTotalList.add(file.getName()));
    }

    /**
     * 获取插件下所有编译文件
     * @return Map<String,Map<String, String>> 插件名， 文件名，文件字符串内容
     */
    default Map<String, Map<String, String>> getPluginsString(){
        Map<String, Map<String, String>> pluginsString = new HashMap<>();
        getPluginsResource().forEach((pluginsName,pluginsFile)-> pluginsString.put(pluginsName,Arrays.stream(Objects.requireNonNull(pluginsFile.listFiles())).filter(File::isFile).filter(file -> !file.isHidden() || !file.getName().startsWith(".")).filter(file -> file.getName().endsWith(".java")).collect(HashMap::new, (m, file) -> m.put(file.getName(), FileTXTUtil.loadFileString(file.getPath())), HashMap::putAll)));
        return pluginsString;
    }

    @Override
    default Map<String, String> getPluginsString(String pluginsName){
        return getPluginsString().get(pluginsName);
    }

    @Override
    default void setPluginsRunList(String pluginsName){
        pluginsRunList.add(pluginsName);
        logger.info("插件类型：{"+getPluginsType()+"}\t成功加载插件["+pluginsName+"]");
    }

    @Override
    default void removePluginsRunList(String pluginsName){
        pluginsRunList.remove(pluginsName);
        logger.info("插件类型：{"+getPluginsType()+"}\t成功卸载插件["+pluginsName+"]");
    }

}
