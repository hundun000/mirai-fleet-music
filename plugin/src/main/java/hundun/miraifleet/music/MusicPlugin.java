package hundun.miraifleet.music;

import org.jetbrains.annotations.NotNull;

import hundun.miraifleet.music.botlogic.MusicBotLogic;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;

/**
 * @author hundun
 * Created on 2021/08/09
 */
public class MusicPlugin extends JavaPlugin {
    public static final MusicPlugin INSTANCE = new MusicPlugin(); 
    
    MusicBotLogic botLogic;
    
    public MusicPlugin() {
        super(new JvmPluginDescriptionBuilder(
                "hundun.fleet.music",
                "0.1.0"
            )
            .build());
    }
    
    @Override
    public void onLoad(@NotNull PluginComponentStorage $this$onLoad) {
        
    }
    
    @Override
    public void onEnable() {
        botLogic = new MusicBotLogic(this);
        botLogic.onBotLogicEnable();
    }
    
    @Override
    public void onDisable() {
        botLogic.onDisable();
        // 由GC回收即可
        botLogic = null;
    }
}
