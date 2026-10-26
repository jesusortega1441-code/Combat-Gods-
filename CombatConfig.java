package com.combatgods.config;

import com.combatgods.skill.Skill;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.EnumMap;
import java.util.Map;

/**
 * Opciones del mod. Se editan en  config/combatgods-common.toml
 * (más adelante tendrán su pantalla propia dentro del juego).
 */
public final class CombatConfig {
    public static final ForgeConfigSpec SPEC;

    // general
    public static final ForgeConfigSpec.IntValue ULTIMATE_SECONDS;
    public static final ForgeConfigSpec.DoubleValue BAR_PER_HIT;
    public static final ForgeConfigSpec.DoubleValue BAR_PER_SKILL;
    public static final ForgeConfigSpec.DoubleValue CLOSE_RANGE;
    public static final ForgeConfigSpec.DoubleValue COUNTER_WINDOW_SECONDS;

    // cooldowns (segundos)
    private static final Map<Skill, ForgeConfigSpec.IntValue> COOLDOWNS = new EnumMap<>(Skill.class);

    // daño
    public static final ForgeConfigSpec.DoubleValue NORMAL_1_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue NORMAL_2_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue NORMAL_3_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue DAGGER_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue KATANA_CUT_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue ULT_1_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue ULT_2_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue ULT_3_EXPLOSION_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue ULT_3_EXPLOSION_RADIUS;

    // katana
    public static final ForgeConfigSpec.IntValue KATANA_RUN_SECONDS;
    public static final ForgeConfigSpec.DoubleValue KATANA_SPEED_BONUS;
    public static final ForgeConfigSpec.DoubleValue KATANA_TARGET_RANGE;
    public static final ForgeConfigSpec.IntValue KATANA_CUTS;

    // clones
    public static final ForgeConfigSpec.IntValue CLONES_NORMAL;
    public static final ForgeConfigSpec.IntValue CLONES_ULTIMATE;
    public static final ForgeConfigSpec.DoubleValue CLONE_HEALTH;
    public static final ForgeConfigSpec.DoubleValue CLONE_EXTRA_HEALTH_ULTIMATE;
    public static final ForgeConfigSpec.DoubleValue CLONE_DAMAGE;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

        b.push("general");
        ULTIMATE_SECONDS = b.comment("Duración de la ultimate (segundos)").defineInRange("ultimateSeconds", 120, 5, 3600);
        BAR_PER_HIT = b.comment("Cuánto sube la barra de ultimate (0-100) con cada golpe").defineInRange("barPerHit", 3.0, 0.0, 100.0);
        BAR_PER_SKILL = b.comment("Cuánto sube la barra de ultimate al usar una habilidad").defineInRange("barPerSkill", 10.0, 0.0, 100.0);
        CLOSE_RANGE = b.comment("Distancia (bloques) a la que debe estar el objetivo para las habilidades normales").defineInRange("closeRange", 1.0, 0.5, 8.0);
        COUNTER_WINDOW_SECONDS = b.comment("Tiempo (s) antes del golpe en que vale el contraataque").defineInRange("counterWindowSeconds", 1.0, 0.1, 10.0);
        b.pop();

        b.push("cooldowns");
        for (Skill s : Skill.values()) {
            COOLDOWNS.put(s, b.comment("Cooldown en segundos: " + s.id())
                    .defineInRange(s.id(), s.defaultCooldownSeconds(), 0, 600));
        }
        b.pop();

        b.push("damage");
        NORMAL_1_DAMAGE = b.defineInRange("normal1", 8.0, 0.0, 1000.0);
        NORMAL_2_DAMAGE = b.defineInRange("normal2", 10.0, 0.0, 1000.0);
        NORMAL_3_DAMAGE = b.defineInRange("normal3", 12.0, 0.0, 1000.0);
        DAGGER_DAMAGE = b.comment("Daño total de la habilidad de la daga").defineInRange("dagger", 12.0, 0.0, 1000.0);
        KATANA_CUT_DAMAGE = b.comment("Daño de cada corte de la katana").defineInRange("katanaCut", 4.0, 0.0, 1000.0);
        ULT_1_DAMAGE = b.comment("Daño total de la ultimate 1").defineInRange("ult1", 30.0, 0.0, 1000.0);
        ULT_2_DAMAGE = b.comment("Daño total de la ultimate 2").defineInRange("ult2", 40.0, 0.0, 1000.0);
        ULT_3_EXPLOSION_DAMAGE = b.comment("Daño de la explosión de la ultimate 3 (no afecta a quien golpea)").defineInRange("ult3Explosion", 12.0, 0.0, 1000.0);
        ULT_3_EXPLOSION_RADIUS = b.defineInRange("ult3ExplosionRadius", 4.0, 1.0, 32.0);
        b.pop();

        b.push("katana");
        KATANA_RUN_SECONDS = b.comment("Tiempo máximo de carrera (s)").defineInRange("runSeconds", 10, 1, 120);
        KATANA_SPEED_BONUS = b.comment("Velocidad extra al correr (0.5 = +50%)").defineInRange("speedBonus", 0.5, 0.0, 5.0);
        KATANA_TARGET_RANGE = b.comment("Alcance (bloques) para buscar objetivo al disparar el ataque").defineInRange("targetRange", 24.0, 2.0, 128.0);
        KATANA_CUTS = b.comment("Cantidad de cortes de la serie final").defineInRange("cuts", 5, 1, 30);
        b.pop();

        b.push("clones");
        CLONES_NORMAL = b.defineInRange("count", 4, 1, 32);
        CLONES_ULTIMATE = b.comment("Cantidad con la ultimate activa").defineInRange("countUltimate", 8, 1, 64);
        CLONE_HEALTH = b.comment("Vida de cada clon (2 = 1 corazón)").defineInRange("health", 20.0, 1.0, 1000.0);
        CLONE_EXTRA_HEALTH_ULTIMATE = b.comment("Vida extra con la ultimate (10 = 5 corazones)").defineInRange("extraHealthUltimate", 10.0, 0.0, 1000.0);
        CLONE_DAMAGE = b.defineInRange("damage", 4.0, 0.0, 1000.0);
        b.pop();

        SPEC = b.build();
    }

    private CombatConfig() {}

    public static int cooldownTicks(Skill s) {
        return COOLDOWNS.get(s).get() * 20;
    }
}
