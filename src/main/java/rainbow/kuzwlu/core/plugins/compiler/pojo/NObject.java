package rainbow.kuzwlu.core.plugins.compiler.pojo;

import lombok.Builder;
import lombok.Data;
import rainbow.kuzwlu.core.plugins.annotation.RainbowPlugins;
import rainbow.kuzwlu.core.plugins.compiler.RainbowClassLoader;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class NObject implements Serializable {

    private String pluginsName;

    private List<Object> objList;

    private RainbowPlugins rainbowPlugins;

}
