package com.combatgods.client;

import com.combatgods.entity.ShadowCloneEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

/** Dibuja al clon con el modelo de jugador y la skin de su dueño. */
public class ShadowCloneRenderer extends MobRenderer<ShadowCloneEntity, PlayerModel<ShadowCloneEntity>> {

    public ShadowCloneRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(ShadowCloneEntity entity) {
        UUID owner = entity.getOwnerUUID().orElse(null);
        ClientPacketListener conn = Minecraft.getInstance().getConnection();
        if (owner != null && conn != null) {
            PlayerInfo info = conn.getPlayerInfo(owner);
            if (info != null) return info.getSkinLocation();
        }
        return DefaultPlayerSkin.getDefaultSkin(owner != null ? owner : entity.getUUID());
    }
}
