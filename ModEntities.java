package com.combatgods.entity;

import com.combatgods.CombatGodsMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CombatGodsMod.MODID);

    public static final RegistryObject<EntityType<ShadowCloneEntity>> SHADOW_CLONE = ENTITIES.register("shadow_clone",
            () -> EntityType.Builder.<ShadowCloneEntity>of(ShadowCloneEntity::new, MobCategory.MISC)
                    .sized(0.6F, 1.8F)
                    .clientTrackingRange(10)
                    .build("shadow_clone"));

    private ModEntities() {}

    public static void register(IEventBus bus) {
        ENTITIES.register(bus);
    }

    @Mod.EventBusSubscriber(modid = CombatGodsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Attributes {
        @SubscribeEvent
        public static void onAttributes(EntityAttributeCreationEvent event) {
            // valores base; la vida y el daño de la config se aplican cuando aparece cada clon
            event.put(SHADOW_CLONE.get(), ShadowCloneEntity.createAttributes(20.0, 4.0).build());
        }
    }
}
