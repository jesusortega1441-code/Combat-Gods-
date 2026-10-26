package com.combatgods.client;

import com.combatgods.CombatGodsMod;
import com.combatgods.entity.ModEntities;
import com.combatgods.network.ModNetwork;
import com.combatgods.network.SkillPacket;
import com.combatgods.skill.Skill;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.EnumMap;
import java.util.Map;

public class ClientEvents {
    private static final String CATEGORY = "key.categories.combatgods";

    /** Menú para elegir personaje. */
    public static final KeyMapping OPEN_SELECT = new KeyMapping(
            "key.combatgods.select_character", GLFW.GLFW_KEY_H, CATEGORY);

    /** Una tecla (asignable en Controles) por cada habilidad. */
    public static final Map<Skill, KeyMapping> SKILL_KEYS = new EnumMap<>(Skill.class);

    static {
        key(Skill.NORMAL_1, GLFW.GLFW_KEY_Z);
        key(Skill.NORMAL_2, GLFW.GLFW_KEY_X);
        key(Skill.NORMAL_3, GLFW.GLFW_KEY_C);
        key(Skill.CLONES, GLFW.GLFW_KEY_Y);
        key(Skill.WEAPON, GLFW.GLFW_KEY_V);
        key(Skill.ULT_1, GLFW.GLFW_KEY_B);
        key(Skill.ULT_2, GLFW.GLFW_KEY_N);
        key(Skill.ULT_3, GLFW.GLFW_KEY_M);
        key(Skill.COUNTER, GLFW.GLFW_KEY_R);
        key(Skill.ULTIMATE, GLFW.GLFW_KEY_G);
    }

    private static void key(Skill s, int defaultKey) {
        SKILL_KEYS.put(s, new KeyMapping("key.combatgods.skill." + s.id(), defaultKey, CATEGORY));
    }

    @Mod.EventBusSubscriber(modid = CombatGodsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModBus {
        @SubscribeEvent
        public static void registerKeys(RegisterKeyMappingsEvent event) {
            event.register(OPEN_SELECT);
            SKILL_KEYS.values().forEach(event::register);
        }

        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.SHADOW_CLONE.get(), ShadowCloneRenderer::new);
        }

        @SubscribeEvent
        public static void registerOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll("combatgods_hud", HudOverlay::render);
        }
    }

    @Mod.EventBusSubscriber(modid = CombatGodsMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ForgeBus {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase != TickEvent.Phase.END) return;
            Minecraft mc = Minecraft.getInstance();
            ClientState.tick();

            while (OPEN_SELECT.consumeClick()) {
                if (mc.player != null && mc.screen == null) mc.setScreen(new CharacterSelectScreen());
            }
            for (Map.Entry<Skill, KeyMapping> e : SKILL_KEYS.entrySet()) {
                while (e.getValue().consumeClick()) {
                    if (mc.player != null && mc.screen == null) {
                        ModNetwork.CHANNEL.sendToServer(new SkillPacket(e.getKey().ordinal()));
                    }
                }
            }
        }
    }
}
