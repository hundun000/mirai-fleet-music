package hundun.miraifleet.music;


import hundun.miraifleet.music.MusicPlugin;
import net.mamoe.mirai.console.plugin.PluginManager;
import net.mamoe.mirai.console.terminal.MiraiConsoleImplementationTerminal;
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader;
import org.junit.Test;

/**
 * @author hundun
 * Created on 2021/06/03
 */
public class OnlyUseJarPluginTest {
    @Test
    public void test() throws InterruptedException {
        MiraiConsoleTerminalLoader.INSTANCE.startAsDaemon(new MiraiConsoleImplementationTerminal());
        // use jar plugin in "./plugins"
    }
}
