package hundun.miraifleet.music.share.function.music;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;



import hundun.miraifleet.framework.core.botlogic.BaseBotLogic;
import hundun.miraifleet.framework.core.function.BaseFunction;
import hundun.miraifleet.framework.core.function.FunctionReplyReceiver;
import hundun.miraifleet.music.share.function.music.khjxiaogu.MiraiSongLogic;
import hundun.miraifleet.music.share.function.music.khjxiaogu.MusicCardProvider;
import hundun.miraifleet.music.share.function.music.khjxiaogu.MusicInfo;
import hundun.miraifleet.music.share.function.music.khjxiaogu.MusicSource;
import lombok.Getter;
import hundun.miraifleet.music.share.function.music.khjxiaogu.MiraiSongLogic.CardStyle;
import hundun.miraifleet.music.share.function.music.khjxiaogu.MiraiSongLogic.MusicMainCommand;
import hundun.miraifleet.music.share.function.music.khjxiaogu.MiraiSongLogic.MusicSourceId;
import net.mamoe.mirai.console.command.AbstractCommand;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;

/**
 * @author hundun
 * Created on 2022/02/09
 */
public class MusicCompositeFunction extends BaseFunction<Void> {
    
    private final MusicBridgeHelper bridgeHelper;
    
    @Getter
    private final CompositeCommandFunctionComponent commandComponent;
    
    public MusicCompositeFunction(
            BaseBotLogic botLogic,
            JvmPlugin plugin, 
            String characterName,
            boolean skipRegisterCommand
            ) {
        super(
                botLogic,
                plugin, 
                characterName, 
                "MusicCompositeFunction", 
                skipRegisterCommand,
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
        public void fromCommand(CommandSender sender, String firstKeyword, String... keywords) {
            if (!checkCosPermission(sender)) {
                return;
            }
            String keyword = bridgeHelper.merge(firstKeyword, keywords);
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
            String keyword = bridgeHelper.merge(firstKeyword, keywords);
            bridgeHelper.musicSearch(
                    new FunctionReplyReceiver(sender, plugin.getLogger()),
                    keyword,
                    CardStyle.MIRAI,
                    MusicSourceId.NetEase
                    );
        }
    }
    
    
    
    




}
