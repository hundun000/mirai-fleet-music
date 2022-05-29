package hundun.miraifleet.music.share.function.music.search;

import hundun.miraifleet.framework.core.botlogic.BaseBotLogic;
import hundun.miraifleet.framework.core.function.BaseFunction;
import hundun.miraifleet.framework.core.function.FunctionReplyReceiver;
import lombok.Getter;
import hundun.miraifleet.music.share.function.music.search.khjxiaogu.MiraiSongLogic.CardStyle;
import hundun.miraifleet.music.share.function.music.search.khjxiaogu.MiraiSongLogic.MusicSourceId;
import net.mamoe.mirai.console.command.AbstractCommand;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;

/**
 * @author hundun
 * Created on 2022/02/09
 */
public class MusicSearchCompositeFunction extends BaseFunction<Void> {
    
    private final MusicBridgeHelper bridgeHelper;
    @Getter
    private final CompositeCommandFunctionComponent commandComponent;
    
    public MusicSearchCompositeFunction(
            BaseBotLogic botLogic,
            JvmPlugin plugin, 
            String characterName
            ) {
        super(
                botLogic,
                plugin, 
                characterName, 
                "MusicCompositeFunction",
                null
                );
        this.bridgeHelper = MusicBridgeHelper.getInstance(plugin);
        this.commandComponent = new CompositeCommandFunctionComponent(plugin, characterName, functionName);
    }
    
    @Override
    public AbstractCommand provideCommand() {
        return commandComponent;
    }

    public class CompositeCommandFunctionComponent extends AbstractCompositeCommandFunctionComponent {

        public CompositeCommandFunctionComponent(JvmPlugin plugin, String characterName, String functionName) {
            super(plugin, characterName, functionName, "音乐v2");
        }
            
        @SubCommand(value = "QQ音乐")
        public void searchQQ(CommandSender sender, String firstKeyword, String... keywords) {
            if (!checkCosPermission(sender)) {
                return;
            }
            String keyword = MusicBridgeHelper.merge(firstKeyword, keywords);
            bridgeHelper.musicSearch(
                    new FunctionReplyReceiver(sender, plugin.getLogger()),
                    keyword,
                    CardStyle.MIRAI,
                    MusicSourceId.QQ
                    );
        }
        
        @SubCommand(value = {"网易", "网易云"})
        public void searchNetEase(CommandSender sender, String firstKeyword, String... keywords) {
            if (!checkCosPermission(sender)) {
                return;
            }
            String keyword = MusicBridgeHelper.merge(firstKeyword, keywords);
            bridgeHelper.musicSearch(
                    new FunctionReplyReceiver(sender, plugin.getLogger()),
                    keyword,
                    CardStyle.MIRAI,
                    MusicSourceId.NetEase
                    );
        }
    }


}
