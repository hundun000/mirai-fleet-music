package hundun.miraifleet.music.share.function.music;

import hundun.miraifleet.framework.core.botlogic.BaseBotLogic;
import hundun.miraifleet.framework.core.function.BaseFunction;
import hundun.miraifleet.framework.core.function.FunctionReplyReceiver;
import hundun.miraifleet.framework.core.helper.Utils;
import hundun.miraifleet.framework.core.helper.file.CacheableFileHelper;
import hundun.miraifleet.music.share.function.music.search.MusicBridgeHelper;
import hundun.miraifleet.music.share.function.music.search.khjxiaogu.MiraiSongLogic.CardStyle;
import hundun.miraifleet.music.share.function.music.search.khjxiaogu.MiraiSongLogic.MusicSourceId;
import lombok.Getter;
import net.mamoe.mirai.console.command.AbstractCommand;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;
import net.mamoe.mirai.message.data.Audio;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.utils.ExternalResource;
import whiter.music.mider.MidiFile;
import whiter.music.mider.code.MiderCodeParserConfiguration;
import whiter.music.mider.code.MidiProduceCoreKt;
import whiter.music.mider.dsl.FuncKt;
import whiter.music.mider.dsl.MiderDSL;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.function.Function;

/**
 * @author hundun
 * Created on 2022/02/09
 */
public class MusicMidiFunction extends BaseFunction<Void> {

    private CacheableFileHelper midiCacheableFileHelper;
    private MiderCodeParserConfiguration miderCodeParserConfiguration = new MiderCodeParserConfiguration();
    @Getter
    private final CompositeCommandFunctionComponent commandComponent;

    public MusicMidiFunction(
            BaseBotLogic botLogic,
            JvmPlugin plugin, 
            String characterName
            ) {
        super(
                botLogic,
                plugin, 
                characterName, 
                "MusicMidiFunction",
                null
                );
        this.commandComponent = new CompositeCommandFunctionComponent(plugin, characterName, functionName);
        this.midiCacheableFileHelper = new CacheableFileHelper(resolveFunctionCacheFileFolder(), "midi", plugin.getLogger());
    }

    private String midiCodeToFileName(String midiCode) {
        return  "midi_" + MD5(midiCode);
    }

    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
    
    @Override
    public AbstractCommand provideCommand() {
        return commandComponent;
    }

    public class CompositeCommandFunctionComponent extends AbstractSimpleCommandFunctionComponent {

        public CompositeCommandFunctionComponent(JvmPlugin plugin, String characterName, String functionName) {
            super(plugin, characterName, functionName, "midi");
        }

        @Handler
        public void midi(CommandSender sender, String firstCode, String... subCodes) {
            if (!checkCosPermission(sender)) {
                return;
            }
            String midiCode = MusicBridgeHelper.merge(firstCode, subCodes);
            String fileName = midiCodeToFileName(midiCode);
            Function<String, InputStream> uncachedPatPatFileProvider = it -> calculateSilkOrRawMidiInputStream(midiCode);
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
    }

    private InputStream calculateSilkOrRawMidiInputStream(String midiCode) {
        log.info("calculateMidiInputStream by " + midiCode);
        try {
            MiderDSL dsl = MidiProduceCoreKt.miderCodeToMiderDSL(midiCode, miderCodeParserConfiguration);
            MidiFile midiFile = FuncKt.fromDslInstance(dsl);
            InputStream midiFileStream = midiFile.inStream();
            return midiFileStream;
        } catch (Exception e) {
            log.error("midiProduceCore error: ", e);
            return null;
        }
    }


}
