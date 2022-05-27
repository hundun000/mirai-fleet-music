package hundun.miraifleet.music.share.midi


import bot.music.whiter.midi2mp3Stream
import bot.music.whiter.toMiderNoteList
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.utils.ExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.info
import whiter.music.mider.dsl.MiderDSL
import java.io.InputStream

object MidiProduceCore {

    private var handler: (String) -> InputStream = { msg: String ->
        var arrowCount = 0
        var availCount = 0
        var defaultBmp = 80
        var defaultPitch = 4
        var mode = ""

        msg.forEach {
            if (arrowCount >= 2) return@forEach
            if (it == '>') arrowCount ++
            availCount ++
        }

        val noteList = msg.substring(availCount, msg.length)//.replace(Regex("\\s*"), "")
        val configPart = msg.substring(0, availCount).replace(">", "").split(";")

        configPart.forEach {
            if (it.matches(Regex("\\d+b"))) {
                defaultBmp = it.replace("b", "").toInt()
            } else if (it.matches(Regex("[-+b#]?[A-G](min|maj|major|minor)?"))) {
                mode = it
            } else if (it.matches(Regex("\\d"))) {
                defaultPitch = it.toInt()
            }
        }

        val stream = midi2mp3Stream(GOOD_QUALITY_BITRATE = this.quality) {
            bpm = defaultBmp

            if (noteList.matches(Regex("[0-9.\\s-+*/|↑↓i!#b&]+"))) {
                if (noteList.trim().matches(Regex("b+"))) {
                    ifUseMode(mode) { !toMiderNoteList(noteList, defaultPitch) }
                } else {
                    if (defaultPitch != 4) pitch = defaultPitch.toByte()
                    ifUseMode(mode) { parseInt(noteList.replace(Regex("( {2}| \\| )"), "0")) }
                }
            } else {
                ifUseMode(mode) { !toMiderNoteList(noteList, defaultPitch) }
            }
        }

        stream
    }





    fun work(userMsg: String): InputStream? {

        if (userMsg.matches(Regex(">((g|\\d+b)(;([-+b#]?[A-G](min|maj|major|minor)?))?(;\\d)?)>[^>]+"))) {
            return handler.invoke(userMsg);
        }
        return null
    }

    private fun MiderDSL.ifUseMode(mode: String, block: MiderDSL.()-> Unit) {
        if (mode.isNotBlank()) {
            useMode(mode) {
                block()
            }
        } else block()
    }

    @JvmField
    val quality = 64
    @JvmField
    val uploadSize = 1153433L
    @JvmField
    val help = """
命令格式:
>bpm;mode;pitch>音名序列|简谱序列
bpm: 速度, 必选, 格式是: 数字+b, 如 120b, 默认可以用 g 代替
mode: 调式, 可选, 格式是(b/#)调式名, 如Cminor, -Emaj
pitch: 音域(音高), 可选, 默认为 4

示例: 
>g>1155665  4433221  5544332  5544332
等同于
>g>ccggaag+ffeeddc+ggffeed+ggffeed
如命令后的字符仅由数字和+-*/.|空格回车这8种符号组成则判定为简谱序列
如果是音名序列则以下规则生效
a~g: A4~G4
A~G: A5~G5
0-9: 手动修改音域
 # : 升一个半音
 ${'$'} : 降一个半音
 + : 时值变为原来的两倍
 - : 时值变为原来的一半
 . : 时值变为原来的一点五倍
 : : 两个以上音符组成一个和弦 (目前有bug, 尽量不要使用)
 ~ : 克隆上一个音符
 ^ : 克隆上一个音符, 并升高1度
 v : 克隆上一个音符, 并降低1度
+-.这三个符号对简谱序列也生效, 简谱序列暂未支持b和#
类似的用法还有m-w, n-u, i-!, q-p, s-z,升高或降低度数在^-v的基础上逐步递增或递减
"""
}
