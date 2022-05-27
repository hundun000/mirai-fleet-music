package hundun.miraifleet.music.share.function.music;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.function.Function;


import hundun.miraifleet.framework.core.botlogic.BaseBotLogic;
import hundun.miraifleet.framework.core.function.BaseFunction;
import hundun.miraifleet.framework.core.function.FunctionReplyReceiver;
import hundun.miraifleet.framework.core.helper.file.CacheableFileHelper;
import hundun.miraifleet.music.share.function.music.khjxiaogu.MiraiSongLogic;
import hundun.miraifleet.music.share.function.music.khjxiaogu.MusicCardProvider;
import hundun.miraifleet.music.share.function.music.khjxiaogu.MusicInfo;
import hundun.miraifleet.music.share.function.music.khjxiaogu.MusicSource;
import hundun.miraifleet.music.share.midi.MidiProduceCore;
import lombok.Getter;
import hundun.miraifleet.music.share.function.music.khjxiaogu.MiraiSongLogic.CardStyle;
import hundun.miraifleet.music.share.function.music.khjxiaogu.MiraiSongLogic.MusicMainCommand;
import hundun.miraifleet.music.share.function.music.khjxiaogu.MiraiSongLogic.MusicSourceId;
import net.mamoe.mirai.console.command.AbstractCommand;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;
import net.mamoe.mirai.message.data.Audio;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.utils.ExternalResource;

/**
 * @author hundun
 * Created on 2022/02/09
 */
public class MusicCompositeFunction extends BaseFunction<Void> {
    
    private final MusicBridgeHelper bridgeHelper;
    private final MidiProduceCore midiProduceCore = MidiProduceCore.INSTANCE;
    private CacheableFileHelper midiCacheableFileHelper;

    @Getter
    private final CompositeCommandFunctionComponent commandComponent;
    
    public MusicCompositeFunction(
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
        this.midiCacheableFileHelper = new CacheableFileHelper(resolveFunctionCacheFileFolder(), "midi", plugin.getLogger());
    }

    private String midiCodeToFileName(String midiCode) {
        return  "midi_" + midiCode.replace(">","]");
    }

    
    @Override
    public AbstractCommand provideCommand() {
        return commandComponent;
    }

    public class CompositeCommandFunctionComponent extends AbstractCompositeCommandFunctionComponent {

        public CompositeCommandFunctionComponent(JvmPlugin plugin, String characterName, String functionName) {
            super(plugin, characterName, functionName, "音乐v2");
        }

        @SubCommand(value = "midi")
        public void midi(CommandSender sender, String firstCode, String... subCodes) {
            if (!checkCosPermission(sender)) {
                return;
            }
            String midiCode = bridgeHelper.merge(firstCode, subCodes);
            String fileName = midiCodeToFileName(midiCode);
            Function<String, InputStream> uncachedPatPatFileProvider = it -> calculateMidiInputStream(midiCode);
            File midiFile = midiCacheableFileHelper.fromCacheOrProvider(fileName, uncachedPatPatFileProvider);

            FunctionReplyReceiver receiver = new FunctionReplyReceiver(sender, log);
            if (midiFile == null) {
                receiver.sendMessage("midiFile is null");
                return;
            } else {
                ExternalResource externalResource = ExternalResource.create(midiFile).toAutoCloseable();
                log.info("externalResource size = " + externalResource.getSize());
                Message voiceOrNotSupportPlaceholder = receiver.uploadVoiceOrNotSupportPlaceholder(externalResource);
                if (voiceOrNotSupportPlaceholder instanceof Audio) {
                    log.info("has real Audio: " + Arrays.toString(((Audio) voiceOrNotSupportPlaceholder).getFileMd5()));
                }
                receiver.sendMessage(voiceOrNotSupportPlaceholder);
            }
        }
            
        @SubCommand(value = "QQ音乐")
        public void searchQQ(CommandSender sender, String firstKeyword, String... keywords) {
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

    private InputStream calculateMidiInputStream(String midiCode) {
        log.info("calculateMidiInputStream by " + midiCode);
        try {
            return midiProduceCore.work(midiCode);
        } catch (Exception e) {
            log.error("midiProduceCore error: ", e);
            return null;
        }
    }


}
