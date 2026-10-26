package com.combatgods.events;

import com.combatgods.CombatGodsMod;
import com.combatgods.data.PlayerData;
import com.combatgods.item.ModItems;
import com.combatgods.skill.JunSkills;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CombatGodsMod.MODID)
public class ServerEvents {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer p)) return;
        JunSkills.tick(p);
        if (p.tickCount % 10 == 0) JunSkills.sync(p);
    }

    /** Cada golpe de Jun llena la barra de ultimate (mientras la ultimate no esté activa). */
    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        Entity src = event.getSource().getEntity();
        if (src instanceof ServerPlayer p && JunSkills.isJun(p) && !PlayerData.ultActive(p)) {
            PlayerData.addBar(p, com.combatgods.config.CombatConfig.BAR_PER_HIT.get());
        }
    }

    /** Contraataque: si lo activaste justo antes del golpe, el golpe se anula y el atacante muere. */
    @SubscribeEvent
    public static void onAttack(LivingAttackEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer victim) || !JunSkills.isJun(victim)) return;
        long now = victim.level().getGameTime();
        if (PlayerData.counterUntil(victim) <= now || !PlayerData.ultActive(victim)) return;
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) return;
        event.setCanceled(true);
        PlayerData.setCounterUntil(victim, 0);
        JunSkills.counterKill(victim, attacker);
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer p && JunSkills.isJun(p)) JunSkills.onDeath(p);
    }

    /** La daga y la katana no se sueltan al morir. */
    @SubscribeEvent
    public static void onDrops(LivingDropsEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer)) return;
        event.getDrops().removeIf(e -> e.getItem().is(ModItems.KATANA_JUN.get()) || e.getItem().is(ModItems.DAGA_JUN.get()));
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer p) {
            JunSkills.ensureWeapon(p);
            JunSkills.sync(p);
        }
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer p) {
            JunSkills.ensureWeapon(p);
            JunSkills.sync(p);
        }
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer p) JunSkills.cleanup(p);
    }
}
