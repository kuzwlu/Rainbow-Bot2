package rainbow.kuzwlu.core.plugins.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;

public class PluginsFile extends File {

    private static String PATH = "";

    public PluginsFile(){
        super(PATH);
        if (PATH.isEmpty()){
            throw new RuntimeException("请勿实例化PluginsFile！");
        }
    }

    private PluginsFile(PluginsFile pluginsFile, String s) {
        super(pluginsFile,s);
    }

    private PluginsFile(String s) {
        super(s);
    }

    @NotNull
    @Override
    public String getPath() {
        return super.getName();
    }

    private String getRealPath(){
        return super.getPath();
    }

    @Override
    public PluginsFile getParentFile() {
        return new PluginsFile();
    }

    @Override
    public String getParent() {
        return super.getName();
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public void deleteOnExit() {
        return;
    }

    @Nullable
    @Override
    public PluginsFile[] listFiles() {
        String[] ss = list();
        if (ss == null) return null;
        int n = ss.length;
        PluginsFile[] fs = new PluginsFile[n];
        for (int i = 0; i < n; i++) {
            fs[i] = new PluginsFile(this, ss[i]);
        }
        return fs;
    }

    @Nullable
    @Override
    public PluginsFile[] listFiles(@Nullable FileFilter filter) {
        String ss[] = list();
        if (ss == null) return null;
        ArrayList<PluginsFile> files = new ArrayList<>();
        for (String s : ss) {
            PluginsFile f = new PluginsFile(this, s);
            if ((filter == null) || filter.accept(f))
                files.add(f);
        }
        return files.toArray(new PluginsFile[files.size()]);
    }

    @Nullable
    @Override
    public PluginsFile[] listFiles(@Nullable FilenameFilter filter) {
        String ss[] = list();
        if (ss == null) return null;
        ArrayList<PluginsFile> files = new ArrayList<>();
        for (String s : ss)
            if ((filter == null) || filter.accept(this, s))
                files.add(new PluginsFile(this, s));
        return files.toArray(new PluginsFile[files.size()]);
    }

    public PluginsFile newPluginsFile(@NotNull String pathName){
        return new PluginsFile(getRealPath()+separator+pathName);
    }

    @Override
    public boolean createNewFile() throws IOException {
        return super.createNewFile();
    }

    @NotNull
    public static PluginsFile[] listRoots() {
        ArrayList<PluginsFile> files = new ArrayList<>();
        return files.toArray(new PluginsFile[files.size()]);
    }

    public FileInputStream fileInputStream(String pathName) throws FileNotFoundException {
        return new FileInputStream(getRealPath()+PluginsFile.separator+pathName);
    }

    public FileOutputStream fileOutputStream(String pathName) throws FileNotFoundException {
        return new FileOutputStream(getRealPath()+PluginsFile.separator+pathName);
    }

}
