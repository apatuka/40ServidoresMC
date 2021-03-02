package com.cadiducho.cservidoresmc;

import java.util.HashMap;

public class Cooldown {

    private final int time;
    private final HashMap<String, Long> cooldowns;

    public Cooldown(int time) {
        this.time = time;
        this.cooldowns = new HashMap<>();
    }

    public int getTime() {
        return time;
    }

    private HashMap<String, Long> getCooldowns() {
        return cooldowns;
    }

    public int getTimeLeft(String player) {
        if (!isCoolingDown(player)) {
            return 0;
        }
        return (int) (((getCooldowns().get(player) - (System.currentTimeMillis() - (getTime() * 1000))) / 1000) + 1);
    }

    public void setOnCooldown(String player) {
        getCooldowns().put(player, System.currentTimeMillis());
    }

    public boolean isCoolingDown(String player) {
        return getCooldowns().containsKey(player) && getCooldowns().get(player) >= (System.currentTimeMillis() - (getTime() * 1000));
    }
}