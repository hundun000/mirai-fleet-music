package hundun.miraifleet.music.botlogic;

import hundun.miraifleet.framework.core.botlogic.BaseJavaBotLogic;
import hundun.miraifleet.music.share.function.music.MusicCompositeFunction;
import hundun.miraifleet.music.share.function.music.MusicSimpleFunction;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;

/**
 * @author hundun
 * Created on 2021/08/06
 */
public class MusicBotLogic extends BaseJavaBotLogic {

    MusicSimpleFunction musicSimpleFunction;
    MusicCompositeFunction musicCompositeFunction;
    
    public MusicBotLogic(JavaPlugin plugin) {
        super(plugin, "音乐");
        
        musicSimpleFunction = new MusicSimpleFunction(this, plugin, characterName);
        musicSimpleFunction.setSkipRegisterCommand(false);
        functions.add(musicSimpleFunction);
        
        musicCompositeFunction = new MusicCompositeFunction(this, plugin, characterName);
        musicCompositeFunction.setSkipRegisterCommand(false);
        functions.add(musicCompositeFunction);
    }
    
}
