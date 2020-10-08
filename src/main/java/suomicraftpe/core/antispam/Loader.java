package suomicraftpe.core.antispam;

import cn.nukkit.plugin.PluginBase;

import java.util.List;

/**
 * Originally ported from SuomiCraft PE Core
 * ------------------------------
 * SuomiCraft PE Core
 * Created by PetteriM1 for SuomiCraft PE Network
 */
public class Loader extends PluginBase {

    static List<String> blockedMessages;

    public void onEnable() {
        saveDefaultConfig();
        blockedMessages = getConfig().getStringList("blockedMessages");
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getScheduler().scheduleDelayedRepeatingTask(this, new ChatCooldownQueue(), 31, 31);
    }
}
