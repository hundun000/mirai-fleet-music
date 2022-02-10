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
import net.mamoe.mirai.console.command.AbstractCommand;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;

/**
 * @author hundun
 * Created on 2022/02/09
 */
public class MusicSimpleFunction extends BaseFunction<Void> {
    
    private final MusicBridgeHelper bridgeHelper;
    
    @Getter
    private final CompositeCommandFunctionComponent commandComponent;
    
    
    public MusicSimpleFunction(
            BaseBotLogic botLogic,
            JvmPlugin plugin, 
            String characterName,
            boolean skipRegisterCommand
            ) {
        super(
                botLogic,
                plugin, 
                characterName, 
                "MusicSimpleFunction", 
                skipRegisterCommand,
                null
                );
        this.bridgeHelper = MusicBridgeHelper.getInstance(plugin);
        this.commandComponent = new CompositeCommandFunctionComponent(plugin, characterName, functionName);
    }
    
//    @EventHandler
//    public void onMessage(@NotNull GroupMessageEvent event) throws Exception { 
//        if (!checkCosPermission(event)) {
//            return;
//        }
//        String[] args = Utils.getPlainText(event.getMessage()).split(MiraiSongPluginLogic.spliter);
//        MusicMainCommand musicMainCommand = MusicMainCommand.fromCommandTextOrNone(args[0]);
//        BiConsumer<MessageEvent, String[]> exec = MiraiSongPluginLogic.commands.get(musicMainCommand);
//        if (exec != null) {
//            exec.accept(event, args);
//        }
//    }
    
    @Override
    public AbstractCommand provideCommand() {
        return commandComponent;
    }

    public class CompositeCommandFunctionComponent extends AbstractSimpleCommandFunctionComponent {

        public CompositeCommandFunctionComponent(JvmPlugin plugin, String characterName, String functionName) {
            super(plugin, characterName, functionName, "音乐");
        }
            
        @Handler
        public void fromCommand(CommandSender sender, String keyword) {
            if (!checkCosPermission(sender)) {
                return;
            }
            bridgeHelper.musicSearch(
                    new FunctionReplyReceiver(sender, plugin.getLogger()), 
                    keyword, 
                    CardStyle.MIRAI, 
                    null);
        }
    }
    
    
    
    




}
