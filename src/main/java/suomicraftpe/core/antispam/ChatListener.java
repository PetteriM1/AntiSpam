package suomicraftpe.core.antispam;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.utils.TextFormat;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Ported from SuomiCraft PE Core
 * ------------------------------
 * SuomiCraft PE Core
 * Created by PetteriM1 for SuomiCraft PE Network
 */
public class ChatListener implements Listener {

    private Map<String, String> lastMsg = new HashMap<>();

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChat(PlayerChatEvent e) {
        Player p = e.getPlayer();
        String name = p.getName();
        long time = System.currentTimeMillis();

        if (ChatCooldownQueue.list.containsKey(name)) {
            p.sendMessage("§cYou are chatting too fast");
            e.setCancelled(true);
            ChatCooldownQueue.list.put(name, time);
            return;
        }

        String message = TextFormat.clean(e.getMessage());

        if (message.length() > 150) {
            p.sendMessage("§cYour message is too long");
            e.setCancelled(true);
            ChatCooldownQueue.list.put(name, time);
            return;
        }

        if (message.startsWith("Horion - the best minecraft bedrock utility mod - ")) {
            e.setCancelled(true);
            ChatCooldownQueue.list.put(name, time);
            return;
        }

        if (tooSimilar(name, message)) {
            p.sendMessage("§cDuplicated message");
            e.setCancelled(true);
            ChatCooldownQueue.list.put(name, time);
            return;
        }

        lastMsg.put(name, message);

        ChatCooldownQueue.list.put(name, time);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        lastMsg.remove(e.getPlayer().getName());
    }

    private boolean tooSimilar(String player, String message) {
        String last = lastMsg.getOrDefault(player, "");
        return message.equals(last) || StringUtils.getJaroWinklerDistance(message, last) > 0.9;
    }
}
