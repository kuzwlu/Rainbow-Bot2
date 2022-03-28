package rainbow.kuzwlu.core.plugins.tool;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import rainbow.kuzwlu.core.plugins.compiler.interfaces.ScriptCompile;
import rainbow.kuzwlu.utils.PathUtil;

import java.io.File;

public class PluginsDirectoryListener extends FileAlterationListenerAdaptor {

    private ScriptCompile scriptCompile;

    private boolean flag = true;

    public PluginsDirectoryListener(ScriptCompile scriptCompile){
        this.scriptCompile = scriptCompile;
    }

    @Override
    public void onDirectoryChange(File directory) {
        super.onDirectoryChange(directory);
        if (!PathUtil.isHiddenDirectory(directory) && flag) {
            scriptCompile.init(directory.getName());
        }
        flag = true;
    }

    @Override
    public void onDirectoryDelete(File directory) {
        super.onDirectoryDelete(directory);
        if (scriptCompile.pluginsTotalList.size() <= 0) return;
        scriptCompile.pluginsTotalList.remove(directory.getName());
        scriptCompile.pluginsRunList.remove(directory.getName());
        scriptCompile.destroy(directory.getName());
    }

    @Override
    public void onDirectoryCreate(File directory) {
        super.onDirectoryCreate(directory);
        if (PathUtil.isHiddenDirectory(directory)) return;
        scriptCompile.pluginsTotalList.add(directory.getName());
        scriptCompile.pluginsRunList.add(directory.getName());
        scriptCompile.init(directory.getName());
        flag = false;
    }



}
