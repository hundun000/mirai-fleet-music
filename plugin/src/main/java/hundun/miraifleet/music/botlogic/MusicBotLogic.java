package hundun.miraifleet.music.botlogic;

import hundun.miraifleet.framework.core.botlogic.BaseJavaBotLogic;
import hundun.miraifleet.music.share.function.music.MusicMidiFunction;
import hundun.miraifleet.music.share.function.music.search.MusicSearchCompositeFunction;
import hundun.miraifleet.music.share.function.music.search.MusicSearchSimpleFunction;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;

/**
 * @author hundun
 * Created on 2021/08/06
 */
public class MusicBotLogic extends BaseJavaBotLogic {

//    MusicSearchSimpleFunction musicSimpleFunction;
//    MusicSearchCompositeFunction musicCompositeFunction;
    MusicMidiFunction musicMidiFunction;
    
    public MusicBotLogic(JavaPlugin plugin) {
        super(plugin, "音乐人");
        
//        musicSimpleFunction = new MusicSearchSimpleFunction(this, plugin, characterName);
//        musicSimpleFunction.setSkipRegisterCommand(false);
//        functions.add(musicSimpleFunction);
//        
//        musicCompositeFunction = new MusicSearchCompositeFunction(this, plugin, characterName);
//        musicCompositeFunction.setSkipRegisterCommand(false);
//        functions.add(musicCompositeFunction);
        
        musicMidiFunction = new MusicMidiFunction(this, plugin, characterName);
        musicMidiFunction.setSkipRegisterCommand(false);
        functions.add(musicMidiFunction);
    }
    
}
