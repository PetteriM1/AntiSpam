package suomicraftpe.core.antispam;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;

/**
 * Ported from SuomiCraft PE Core
 * ------------------------------
 * SuomiCraft PE Core
 * Created by PetteriM1 for SuomiCraft PE Network
 */
public class ChatListener implements Listener {

    private String lastMsg;
    private String lastSender;
    private long lastTime;

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChat(PlayerChatEvent e) {
        Player p = e.getPlayer();
        String name = p.getName();

        if (ChatCooldownQueue.list.containsKey(name)) {
            p.sendMessage("§cYou are chatting too fast");
            e.setCancelled(true);
            return;
        }

        String message = e.getMessage();

        if (message.length() > 150) {
            p.sendMessage("§cYour message is too long");
            e.setCancelled(true);
            return;
        }

        if (message.startsWith("Horion - the best minecraft bedrock utility mod - ")) {
            e.setCancelled(true);
            return;
        }

        if (name.equals(lastSender) && message.equals(lastMsg) && System.currentTimeMillis() - lastTime < 10000) {
            p.sendMessage("§cDuplicated message");
            e.setCancelled(true);
            return;
        }

        lastMsg = message;
        lastSender = name;
        lastTime = System.currentTimeMillis();

        ChatCooldownQueue.list.put(name, lastTime);
    }
}
