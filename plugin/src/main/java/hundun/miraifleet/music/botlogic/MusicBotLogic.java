package hundun.miraifleet.music.botlogic;

import hundun.miraifleet.framework.core.botlogic.BaseJavaBotLogic;
import hundun.miraifleet.music.share.function.music.MusicMidiFunction;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;

/**
 * @author hundun
 * Created on 2021/08/06
 */
public class MusicBotLogic extends BaseJavaBotLogic {

    public MusicBotLogic(JavaPlugin plugin) {
        super(plugin, "音乐人");

    }

    @Override
    protected void onFunctionsEnable() {
        var musicMidiFunction = new MusicMidiFunction(this, plugin, characterName);
        musicMidiFunction.setSkipRegisterCommand(false);

        registerFunction(musicMidiFunction);
    }
    
}
