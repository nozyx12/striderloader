package dev.nozyx.strider.loader.impl;

import java.util.HashMap;
import java.util.Map;

final class LoaderArgs {
    private final Map<String, String> args = new HashMap<>();

    LoaderArgs(String agentArgs) {
        if (agentArgs == null || agentArgs.trim().isEmpty()) return;

        String[] pairs = agentArgs.split(",");
        for (String pair : pairs) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) args.put(kv[0], kv[1]);
        }
    }

    String get(String key) {
        return args.get(key);
    }

    String getOrDefault(String key, String def) {
        return args.getOrDefault(key, def);
    }

    boolean has(String key) {
        return args.containsKey(key);
    }

    Map<String, String> asMap() {
        return args;
    }
}
