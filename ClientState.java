package com.combatgods.client;

import com.combatgods.character.CombatCharacter;
import com.combatgods.skill.Skill;

public final class ClientState {
    /** Personaje elegido (resalta el botón del menú y decide si se dibuja el HUD). */
    public static CombatCharacter selected = null;

    public static float bar = 0;
    public static int ultimateTicksLeft = 0;
    public static int[] cooldownTicks = new int[Skill.values().length];

    private ClientState() {}

    /** El servidor manda el estado cada medio segundo; entre paquetes el cliente lo va bajando solo. */
    public static void update(String character, float newBar, int ultLeft, int[] cooldowns) {
        selected = CombatCharacter.byId(character);
        bar = newBar;
        ultimateTicksLeft = ultLeft;
        cooldownTicks = cooldowns.length == Skill.values().length ? cooldowns : new int[Skill.values().length];
    }

    public static void tick() {
        if (ultimateTicksLeft > 0) ultimateTicksLeft--;
        for (int i = 0; i < cooldownTicks.length; i++) {
            if (cooldownTicks[i] > 0) cooldownTicks[i]--;
        }
    }
}
