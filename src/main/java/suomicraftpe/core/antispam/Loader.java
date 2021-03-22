package suomicraftpe.core.antispam;

import cn.nukkit.plugin.PluginBase;

/**
 * Ported from SuomiCraft PE Core
 * ------------------------------
 * SuomiCraft PE Core
 * Created by PetteriM1 for SuomiCraft PE Network
 */
public class Loader extends PluginBase {

    public void onEnable() {
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getScheduler().scheduleDelayedRepeatingTask(this, new ChatCooldownQueue(), 40, 40);
    }
}
