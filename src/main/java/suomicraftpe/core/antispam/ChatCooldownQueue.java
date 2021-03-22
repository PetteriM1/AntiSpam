package suomicraftpe.core.antispam;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Ported from SuomiCraft PE Core
 * ------------------------------
 * SuomiCraft PE Core
 * Created by PetteriM1 for SuomiCraft PE Network
 */
public class ChatCooldownQueue implements Runnable {

    public static ConcurrentMap<Long, Long> list = new ConcurrentHashMap<>();

    public void run() {
        long time = System.currentTimeMillis();

        for (Long id : list.keySet()) {
            if (list.get(id) - time < 2000) {
                list.remove(id);
            }
        }
    }
}
