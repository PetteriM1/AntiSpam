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

    public static ConcurrentMap<String, Long> list = new ConcurrentHashMap<>();

    public void run() {
        long time = System.currentTimeMillis();

        for (String s : list.keySet()) {
            if (list.get(s) - time < 2000) {
                list.remove(s);
            }
        }
    }
}
