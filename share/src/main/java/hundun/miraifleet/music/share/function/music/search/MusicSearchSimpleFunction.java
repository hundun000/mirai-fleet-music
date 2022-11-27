package hundun.miraifleet.music.share.function.music.search;


import hundun.miraifleet.framework.core.botlogic.BaseBotLogic;
import hundun.miraifleet.framework.core.function.BaseFunction;
import hundun.miraifleet.framework.core.function.FunctionReplyReceiver;
import lombok.Getter;
import hundun.miraifleet.music.share.function.music.search.khjxiaogu.MiraiSongLogic.CardStyle;
import net.mamoe.mirai.console.command.AbstractCommand;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;

/**
 * @author hundun
 * Created on 2022/02/09
 */
public class MusicSearchSimpleFunction extends BaseFunction {
    
    private final MusicBridgeHelper bridgeHelper;
    
    @Getter
    private final CompositeCommandFunctionComponent commandComponent;
    
    
    public MusicSearchSimpleFunction(
            BaseBotLogic botLogic,
            JvmPlugin plugin, 
            String characterName
            ) {
        super(
                botLogic,
                plugin, 
                characterName, 
                "MusicSimpleFunction"
                );
        this.bridgeHelper = MusicBridgeHelper.getInstance(plugin);
        this.commandComponent = new CompositeCommandFunctionComponent();
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

        public CompositeCommandFunctionComponent() {
            super(plugin, botLogic, new UserLevelFunctionComponentConstructPack(characterName, functionName));
        }
            
        @Handler
        public void fromCommand(CommandSender sender, String firstKeyword, String... keywords) {
            if (!checkCosPermission(sender)) {
                return;
            }
            String keyword = MusicBridgeHelper.merge(firstKeyword, keywords);
            bridgeHelper.musicSearch(
                    new FunctionReplyReceiver(sender, plugin.getLogger()), 
                    keyword, 
                    CardStyle.MIRAI, 
                    null);
        }
    }
    
    
    
    




}
