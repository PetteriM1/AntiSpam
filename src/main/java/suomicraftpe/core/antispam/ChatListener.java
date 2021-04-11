package suomicraftpe.core.antispam;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
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

    private final Map<Long, String> lastMessage = new HashMap<>(120);
    private final Map<Long, String> lastMessage2 = new HashMap<>(120);

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChat(PlayerChatEvent e) {
        Player p = e.getPlayer();
        long id = p.getId();
        long time = System.currentTimeMillis();

        if (ChatCooldownQueue.list.containsKey(id)) {
            p.sendMessage("§cYou are chatting too fast");
            e.setCancelled(true);
            ChatCooldownQueue.list.put(id, time);
            return;
        }

        String message = TextFormat.clean(e.getMessage().toLowerCase());

        if (message.length() > 150) {
            p.sendMessage("§cYour message is too long");
            e.setCancelled(true);
            ChatCooldownQueue.list.put(id, time);
            return;
        }

        if (message.startsWith("horion - the best minecraft bedrock utility mod - ")) {
            e.setCancelled(true);
            ChatCooldownQueue.list.put(id, time);
            return;
        }

        String last = lastMessage.getOrDefault(id, "");
        String last2 = lastMessage2.getOrDefault(id, "");

        if (tooSimilar(message, last, last2)) {
            p.sendMessage("§cDuplicated message");
            e.setCancelled(true);
            ChatCooldownQueue.list.put(id, time);
            return;
        }

        lastMessage.put(id, message);
        lastMessage2.put(id, last);

        ChatCooldownQueue.list.put(id, time);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        lastMessage.remove(e.getPlayer().getId());
        lastMessage2.remove(e.getPlayer().getId());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPreCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        long id = p.getId();
        long time = System.currentTimeMillis();

        if (ChatCooldownQueue.list.containsKey(id)) {
            p.sendMessage("§cYou are chatting too fast");
            e.setCancelled(true);
        }

        ChatCooldownQueue.list.put(id, time);
    }

    private boolean tooSimilar(String message, String last, String last2) {
        return message.equals(last2) || message.equals(last) || StringUtils.getJaroWinklerDistance(message, last) > 0.9;
    }
}
