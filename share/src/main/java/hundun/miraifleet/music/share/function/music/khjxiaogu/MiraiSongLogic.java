/**
 * Mirai Song Plugin
 * Copyright (C) 2021  khjxiaogu
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package hundun.miraifleet.music.share.function.music.khjxiaogu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import hundun.miraifleet.music.share.function.music.khjxiaogu.cardprovider.AmrVoiceProvider;
import hundun.miraifleet.music.share.function.music.khjxiaogu.cardprovider.LightAppXCardProvider;
import hundun.miraifleet.music.share.function.music.khjxiaogu.cardprovider.MiraiMusicCard;
import hundun.miraifleet.music.share.function.music.khjxiaogu.cardprovider.PlainMusicInfoProvider;
import hundun.miraifleet.music.share.function.music.khjxiaogu.cardprovider.ShareCardProvider;
import hundun.miraifleet.music.share.function.music.khjxiaogu.cardprovider.SilkVoiceProvider;
import hundun.miraifleet.music.share.function.music.khjxiaogu.cardprovider.XMLCardProvider;
import hundun.miraifleet.music.share.function.music.khjxiaogu.musicsource.BaiduMusicSource;
import hundun.miraifleet.music.share.function.music.khjxiaogu.musicsource.BiliBiliMusicSource;
import hundun.miraifleet.music.share.function.music.khjxiaogu.musicsource.KugouMusicSource;
import hundun.miraifleet.music.share.function.music.khjxiaogu.musicsource.LocalFileSource;
import hundun.miraifleet.music.share.function.music.khjxiaogu.musicsource.NetEaseAdvancedRadio;
import hundun.miraifleet.music.share.function.music.khjxiaogu.musicsource.NetEaseHQMusicSource;
import hundun.miraifleet.music.share.function.music.khjxiaogu.musicsource.NetEaseMusicSource;
import hundun.miraifleet.music.share.function.music.khjxiaogu.musicsource.NetEaseRadioSource;
import hundun.miraifleet.music.share.function.music.khjxiaogu.musicsource.QQMusicHQSource;
import hundun.miraifleet.music.share.function.music.khjxiaogu.musicsource.QQMusicSource;
import hundun.miraifleet.music.share.function.music.khjxiaogu.musicsource.XimalayaSource;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.event.events.StrangerMessageEvent;
import net.mamoe.mirai.utils.MiraiLogger;


// TODO: Auto-generated Javadoc
/**
 * 插件主类
 * 
 * @author khjxiaogu
 *         file: MiraiSongPlugin.java
 *         time: 2020年8月26日
 */
public class MiraiSongLogic {

	public static final String spliter = " ";

	// 请求音乐的线程池。
	public Executor exec = Executors.newFixedThreadPool(8);

	/** 命令列表. */
//	public static final Map<MusicMainCommand, BiConsumer<MessageEvent, String[]>> commands = new ConcurrentHashMap<>();

	/** 音乐来源. */
	public Map<MusicSourceId, MusicSource> sources = Collections.synchronizedMap(new LinkedHashMap<>());

	/** 外观来源 */
	public Map<CardStyle, MusicCardProvider> cardStyleToProviderMap = new ConcurrentHashMap<>();
	//public static Map<String, IHundunMusicCardProvider> hundunCardProviders = new ConcurrentHashMap<>();
	
	public enum MusicSourceId {
	    /**
	     * QQ音乐
	     */
	    QQ,
	    /**
	     * QQ音乐HQ
	     */
	    QQ_HQ,
	    /**
	     * 网易
	     */
	    NetEase,
	    ;
	}
	
	private void init() {
		// 注册音乐来源
		sources.put(MusicSourceId.QQ, new QQMusicSource());
		sources.put(MusicSourceId.QQ_HQ, new QQMusicHQSource());
		sources.put(MusicSourceId.NetEase, new NetEaseMusicSource());
//		sources.put("网易电台节目", new NetEaseAdvancedRadio());
//		sources.put("网易电台", new NetEaseRadioSource());
//		sources.put("网易HQ", new NetEaseHQMusicSource());
//		sources.put("酷狗", new KugouMusicSource());
//		sources.put("千千", new BaiduMusicSource());
//		sources.put("Bilibili", new BiliBiliMusicSource());
//		sources.put("喜马拉雅", new XimalayaSource());
//		sources.put("本地", new LocalFileSource());
		// 注册外观
		// cards.put("LightApp", new LightAppCardProvider());
		cardStyleToProviderMap.put(CardStyle.LIGHT_APP, new MiraiMusicCard());
		cardStyleToProviderMap.put(CardStyle.LIGHT_APP_X, new LightAppXCardProvider());
		cardStyleToProviderMap.put(CardStyle.XML, new XMLCardProvider());
		cardStyleToProviderMap.put(CardStyle.SILK, new SilkVoiceProvider());
		cardStyleToProviderMap.put(CardStyle.AMR, new AmrVoiceProvider());
		cardStyleToProviderMap.put(CardStyle.SHARE, new ShareCardProvider());
		cardStyleToProviderMap.put(CardStyle.MESSAGE, new PlainMusicInfoProvider());
		cardStyleToProviderMap.put(CardStyle.MIRAI, new MiraiMusicCard());
		
	}
	
	public enum CardStyle {
	    LIGHT_APP,
	    LIGHT_APP_X,
	    XML,
	    SILK,
	    AMR,
	    SHARE,
	    MESSAGE,
	    MIRAI,
	    ;
	    
	}
	
	static {
		HttpURLConnection.setFollowRedirects(true);
	}
	
	JvmPlugin plugin;
	public MiraiSongLogic(JvmPlugin plugin) {
        this.plugin = plugin;
        init();
    }


//	GlobalMatcher matcher = new GlobalMatcher();
	public String unfoundSong;
	public String unavailableShare;
	String templateNotFound;
	String sourceNotFound;

//	/**
//	 * 使用现有的来源和外观制作指令执行器
//	 * 
//	 * @param source 音乐来源名称
//	 * @param card   音乐外观名称
//	 * @return return 返回一个指令执行器，可以注册到命令列表里面
//	 */
//	private BiConsumer<MessageEvent, String[]> makeTemplate(String source, String card) {
//		if (source.equals("all"))
//			return makeSearchesTemplate(card);
//		MusicCardProvider cb = cards.get(card);
//		if (cb == null)
//			throw new IllegalArgumentException("card template not exists");
//		MusicSource mc = sources.get(source);
//		if (mc == null)
//			throw new IllegalArgumentException("music source not exists");
//		return (event, args) -> {
//			String sn;
//			try {
//				sn = URLEncoder.encode(String.join(spliter, Arrays.copyOfRange(args, 1, args.length)), "UTF-8");
//			} catch (UnsupportedEncodingException ignored) {
//				return;
//			}
//			exec.execute(() -> {
//				MusicInfo mi;
//				try {
//					mi = mc.get(sn);
//				} catch (Throwable t) {
//					plugin.getLogger().debug(t);
//					Utils.getRealSender(event).sendMessage(unfoundSong);
//					return;
//				}
//				try {
//					Utils.getRealSender(event).sendMessage(cb.process(mi, Utils.getRealSender(event)));
//				} catch (Throwable t) {
//					plugin.getLogger().debug(t);
//					// plugin.getLogger().
//					Utils.getRealSender(event).sendMessage(unavailableShare);
//					return;
//				}
//			});
//		};
//	}
//
//	/**
//	 * 自动搜索所有源并且以指定外观返回
//	 * 
//	 * @param card 音乐外观名称
//	 * @return return 返回一个指令执行器，可以注册到命令列表里面
//	 */
//	private BiConsumer<MessageEvent, String[]> makeSearchesTemplate(String card) {
//		MusicCardProvider cb = cards.get(card);
//		if (cb == null)
//			throw new IllegalArgumentException("card template not exists");
//		return (event, args) -> {
//			String sn;
//			try {
//				sn = URLEncoder.encode(String.join(spliter, Arrays.copyOfRange(args, 1, args.length)), "UTF-8");
//			} catch (UnsupportedEncodingException ignored) {
//				return;
//			}
//			exec.execute(() -> {
//				for (MusicSource mc : sources.values()) {
//					if (!mc.isVisible())
//						continue;
//					MusicInfo mi;
//					try {
//						mi = mc.get(sn);
//					} catch (Throwable t) {
//						plugin.getLogger().debug(t);
//						continue;
//					}
//					try {
//						Utils.getRealSender(event).sendMessage(cb.process(mi, Utils.getRealSender(event)));
//					} catch (Throwable t) {
//						plugin.getLogger().debug(t);
//						Utils.getRealSender(event).sendMessage(unavailableShare);
//					}
//					return;
//				}
//				Utils.getRealSender(event).sendMessage(unfoundSong);
//			});
//
//		};
//	}

	@SuppressWarnings("resource")
	public void onEnable(boolean withRegister) {
	    staticLogger = plugin.getLogger();
//		if (!new File(plugin.getDataFolder(), "global.permission").exists()) {
//			try (FileOutputStream fos = new FileOutputStream(new File(plugin.getDataFolder(), "global.permission"))) {
//				fos.write("#global fallback permission file".getBytes());
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		if (!new File(plugin.getDataFolder(), "config.yml").exists()) {
//			try {
//				new FileOutputStream(new File(plugin.getDataFolder(), "config.yml"))
//						.write(Utils.readAll(plugin.getResourceAsStream("config.yml")));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}

//		File local = new File("SongPluginLocal.json");
//		if (!local.exists()) {
//			try {
//				local.createNewFile();
//				JsonArray datas = new JsonArray();
//				JsonObject obj = new JsonObject();
//				obj.addProperty("title", "标题");
//				obj.addProperty("desc", "副标题");
//				obj.addProperty("previewUrl", "专辑图片url");
//				obj.addProperty("musicUrl", "音乐播放url");
//				obj.addProperty("jumpUrl", "点击跳转url");
//				obj.addProperty("source", "本地");
//				datas.add(obj);
//				FileWriter fw = new FileWriter(local);
//				fw.write(datas.toString());
//				fw.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//		}
		reload();
		if (withRegister) {
		    //register();
		}
		plugin.getLogger().info("MiraiSongPluginLogic加载完毕!");
	}
	
//	private void register() {
//	    GlobalEventChannel.INSTANCE.registerListenerHost(new SimpleListenerHost(plugin.getCoroutineContext()) {
//            @EventHandler
//            public void onGroup(GroupMessageEvent event) {
//                String[] args = Utils.getPlainText(event.getMessage()).split(spliter);
//                BiConsumer<MessageEvent, String[]> exec = commands.get(MusicMainCommand.fromCommandTextOrNone(args[0]));
//                if (exec != null)
//                    if (matcher.match(event.getSender()).isAllowed())
//                        exec.accept(event, args);
//
//            }
//
//            @EventHandler
//            public void onFriend(FriendMessageEvent event) {
//                String[] args = Utils.getPlainText(event.getMessage()).split(spliter);
//                BiConsumer<MessageEvent, String[]> exec = commands.get(MusicMainCommand.fromCommandTextOrNone(args[0]));
//                if (exec != null)
//                    if (matcher.match(event.getSender(),false).isAllowed())
//                        exec.accept(event, args);
//
//            }
//
//            @EventHandler
//            public void onTemp(StrangerMessageEvent event) {
//                String[] args = Utils.getPlainText(event.getMessage()).split(spliter);
//                BiConsumer<MessageEvent, String[]> exec = commands.get(MusicMainCommand.fromCommandTextOrNone(args[0]));
//                if (exec != null)
//                    if (matcher.match(event.getSender(), true).isAllowed())
//                        exec.accept(event, args);
//
//            }
//        });
//    }
	
	
	public enum MusicMainCommand {
	    SIMPLE_MUSIC("#音乐"), 
	    NONE("none")
	    ;
	    private final String text;
	    private MusicMainCommand(String text) {
            this.text = text;
        }
	    public static MusicMainCommand fromCommandTextOrNone(String string) {
            for (MusicMainCommand value : values()) {
                if (value.text.equals(string)) {
                    return value;
                }
            }
	        return NONE;
        }
        public String getText() {
            return text;
        }
	}

	public void reload() {
//		YamlMap cfg = Yaml.Default.decodeYamlMapFromString(
//				new String(Utils.readAll(new File(plugin.getDataFolder(), "config.yml")), StandardCharsets.UTF_8));
//		matcher.load(plugin.getDataFolder());
//		YamlMap excs = (YamlMap) cfg.get(new YamlLiteral("extracommands"));
//		String addDefault = cfg.getStringOrNull("adddefault");


//		commands.clear();
//		if (true) {
//			commands.put(MusicMainCommand.SIMPLE_MUSIC, makeSearchesTemplate("Mirai"));
//			commands.put("#外链", makeSearchesTemplate("Message"));
//			commands.put("#语音", makeSearchesTemplate("AMR"));
//			commands.put("#QQ", makeTemplate("QQ音乐", "Mirai"));// 标准样板
//			commands.put("#网易", makeTemplate("网易", "Mirai"));
//			commands.put("#网易电台", makeTemplate("网易电台节目", "Mirai"));
//			commands.put("#酷狗", makeTemplate("酷狗", "Mirai"));
//			commands.put("#千千", makeTemplate("千千", "XML"));
//			commands.put("#点歌", (event, args) -> {
//				String sn;
//				try {
//					sn = URLEncoder.encode(String.join(spliter, Arrays.copyOfRange(args, 3, args.length)), "UTF-8");
//				} catch (UnsupportedEncodingException ignored) {
//					return;
//				}
//				exec.execute(() -> {
//					try {
//						MusicSource ms = sources.get(args[1]);
//						if (ms == null) {
//							Utils.getRealSender(event).sendMessage("无法找到源");
//							return;
//						}
//						MusicCardProvider mcp = cards.get(args[2]);
//						if (mcp == null) {
//							Utils.getRealSender(event).sendMessage("无法找到模板");
//							return;
//						}
//						MusicInfo mi;
//						try {
//							mi = ms.get(sn);
//						} catch (Throwable t) {
//							plugin.getLogger().debug(t);
//							Utils.getRealSender(event).sendMessage(unfoundSong);
//							return;
//						}
//						try {
//							Utils.getRealSender(event).sendMessage(mcp.process(mi, Utils.getRealSender(event)));
//						} catch (Throwable t) {
//							plugin.getLogger().debug(t);
//							Utils.getRealSender(event).sendMessage(unavailableShare);
//							return;
//						}
//					} catch (Throwable e) {
//						e.printStackTrace();
//						Utils.getRealSender(event).sendMessage(unfoundSong);
//					}
//				});
//			});
//		}
//		if (excs != null)
//			for (YamlElement cmd : excs.getKeys()) {
//				commands.put(CommandStart.valueOfOrNone(cmd.toString()), makeTemplate(((YamlMap) excs.get(cmd)).getString("source"),
//						((YamlMap) excs.get(cmd)).getString("card")));
//			}

		
		//initFfmpeg(cfg);
		plugin.getLogger().info("跳过initFfmpeg");
	}
	
//	@SuppressWarnings("unused")
//    private void initFfmpeg(YamlMap cfg) {
//	    AmrVoiceProvider.ffmpeg = SilkVoiceProvider.ffmpeg = cfg.getString("ffmpeg_path");
//        String amras = cfg.getStringOrNull("amrqualityshift");
//        String amrwb = cfg.getStringOrNull("amrwb");
//        String usecc = cfg.getStringOrNull("use_custom_ffmpeg_command");
//        String ulocal = cfg.getStringOrNull("enable_local");
//        String vb = cfg.getStringOrNull("verbose");
//        unfoundSong = cfg.getStringOrNull("hintsongnotfound");
//        if (unfoundSong == null)
//            unfoundSong = "无法找到歌曲。";
//        unavailableShare = cfg.getStringOrNull("hintcarderror");
//        if (unavailableShare == null)
//            unavailableShare = "分享歌曲失败。";
//        templateNotFound = cfg.getStringOrNull("hintnotemplate");
//        if (templateNotFound == null)
//            templateNotFound = "无法找到卡片。";
//        sourceNotFound = cfg.getStringOrNull("hintsourcenotfound");
//        if (sourceNotFound == null)
//            sourceNotFound = "无法找到来源。";
//        LocalFileSource.autoLocal = ulocal != null && ulocal.equals("true");
//        AmrVoiceProvider.autoSize = amras != null && amras.equals("true");
//        AmrVoiceProvider.wideBrand = amrwb == null || amrwb.equals("true");
//        AmrVoiceProvider.customCommand = (usecc != null && usecc.equals("true"))
//                ? cfg.getStringOrNull("custom_ffmpeg_command")
//                : null;
//        Utils.verbose = vb == null || vb.equals("true");
//        SilkVoiceProvider.silk = cfg.getString("silkenc_path");
//        if (AmrVoiceProvider.customCommand == null) {
//            try {
//                Utils.exeCmd(AmrVoiceProvider.ffmpeg, "-version");
//            } catch (RuntimeException ex) {
//                ex.printStackTrace();
//                plugin.getLogger().warning("ffmpeg启动失败，语音功能失效！");
//            }
//            plugin.getLogger().info("当前配置项：宽域AMR:" + AmrVoiceProvider.wideBrand + " AMR自动大小:" + AmrVoiceProvider.autoSize);
//        } else
//            plugin.getLogger().info("当前配置项：自定义指令:" + AmrVoiceProvider.customCommand);
//	}

	static MiraiLogger staticLogger;
    
    public static MiraiLogger getMLogger() {
        return staticLogger;
    }
}
