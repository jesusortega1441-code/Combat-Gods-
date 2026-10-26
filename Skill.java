package com.combatgods.skill;

/** Todas las habilidades de Jun. Cada una tiene su tecla (asignable) y su cooldown (configurable). */
public enum Skill {
    NORMAL_1("normal_1", 20),
    NORMAL_2("normal_2", 20),
    NORMAL_3("normal_3", 20),
    CLONES("clones", 20),
    WEAPON("weapon", 20),     // daga o katana, según cuál tengas en la mano
    ULT_1("ult_1", 20),
    ULT_2("ult_2", 20),
    ULT_3("ult_3", 20),
    COUNTER("counter", 20),
    ULTIMATE("ultimate", 0);

    private final String id;
    private final int defaultCooldownSeconds;

    Skill(String id, int defaultCooldownSeconds) {
        this.id = id;
        this.defaultCooldownSeconds = defaultCooldownSeconds;
    }

    public String id() { return id; }
    public int defaultCooldownSeconds() { return defaultCooldownSeconds; }

    public static Skill byOrdinal(int i) {
        Skill[] all = values();
        return (i >= 0 && i < all.length) ? all[i] : null;
    }
}
