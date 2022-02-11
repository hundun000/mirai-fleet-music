package hundun.miraifleet.music.share.function.music;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map.Entry;

import hundun.miraifleet.framework.core.botlogic.BaseBotLogic;
import hundun.miraifleet.framework.core.function.FunctionReplyReceiver;
import hundun.miraifleet.music.share.function.music.khjxiaogu.MiraiSongLogic;
import hundun.miraifleet.music.share.function.music.khjxiaogu.MusicCardProvider;
import hundun.miraifleet.music.share.function.music.khjxiaogu.MusicInfo;
import hundun.miraifleet.music.share.function.music.khjxiaogu.MusicSource;
import hundun.miraifleet.music.share.function.music.khjxiaogu.MiraiSongLogic.CardStyle;
import hundun.miraifleet.music.share.function.music.khjxiaogu.MiraiSongLogic.MusicMainCommand;
import hundun.miraifleet.music.share.function.music.khjxiaogu.MiraiSongLogic.MusicSourceId;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;

/**
 * @author hundun
 * Created on 2022/02/10
 */
public class MusicBridgeHelper {
    
    private static MusicBridgeHelper INSTANCE; 
    
    MiraiSongLogic miraiSongLogic;
    JvmPlugin plugin;
    
    public static MusicBridgeHelper getInstance(JvmPlugin plugin) {
        if (INSTANCE == null) {
            INSTANCE = new MusicBridgeHelper(plugin);
        }
        return INSTANCE;
    }
    
    private MusicBridgeHelper(JvmPlugin plugin) {
        this.plugin = plugin;
        miraiSongLogic = new MiraiSongLogic(plugin);
        miraiSongLogic.onEnable(false);
    }

    public String merge(String firstKeyword, String... keywords) {
        return firstKeyword + (keywords != null ? (" " + String.join(" ", keywords)) : "");
    }

    public void musicSearch(FunctionReplyReceiver receiver, String keyword, CardStyle cardStyle, MusicSourceId targetSourceId) {
        MusicCardProvider cardProvider = miraiSongLogic.cardStyleToProviderMap.get(cardStyle);
        if (cardProvider == null) {
            throw new IllegalArgumentException("card template not exists");
        }
        

        String sn;
        try {
            sn = URLEncoder.encode(keyword, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {
            return;
        }
        miraiSongLogic.exec.execute(() -> {
            for (Entry<MusicSourceId, MusicSource> entry : miraiSongLogic.sources.entrySet()) {
                if (targetSourceId != null && entry.getKey() != targetSourceId) {
                    continue;
                }
                MusicSource mc = entry.getValue();
                if (!mc.isVisible()) {
                    continue;
                }
                MusicInfo mi;
                try {
                    mi = mc.get(sn);
                } catch (Throwable t) {
                    plugin.getLogger().debug(t);
                    continue;
                }
                try {
                    receiver.sendMessage(cardProvider.process(mi, receiver));
                } catch (Throwable t) {
                    plugin.getLogger().debug(t);
                    receiver.sendMessage(miraiSongLogic.unavailableShare);
                }
                return;
            }
            receiver.sendMessage(miraiSongLogic.unfoundSong);
        });

        
    }
    
}
