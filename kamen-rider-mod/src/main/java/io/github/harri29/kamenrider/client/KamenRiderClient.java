package io.github.harri29.kamenrider.client;

import io.github.harri29.kamenrider.KamenRiderMod;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@Mod(value = KamenRiderMod.MODID, dist = Dist.CLIENT)
public final class KamenRiderClient {
    public static final ModelLayerLocation RIDER_SUIT_WIDE = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(KamenRiderMod.MODID, "rider_suit"), "wide"
    );
    public static final ModelLayerLocation RIDER_SUIT_SLIM = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(KamenRiderMod.MODID, "rider_suit"), "slim"
    );

    public KamenRiderClient(IEventBus modBus) {
        modBus.addListener(KamenRiderClient::registerLayerDefinitions);
        modBus.addListener(KamenRiderClient::addPlayerLayers);
    }

    private static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(
                RIDER_SUIT_WIDE,
                () -> LayerDefinition.create(PlayerModel.createMesh(new CubeDeformation(0.14F), false), 64, 64)
        );
        event.registerLayerDefinition(
                RIDER_SUIT_SLIM,
                () -> LayerDefinition.create(PlayerModel.createMesh(new CubeDeformation(0.14F), true), 64, 64)
        );
    }

    private static void addPlayerLayers(EntityRenderersEvent.AddLayers event) {
        for (PlayerSkin.Model skin : event.getSkins()) {
            PlayerRenderer renderer = event.getSkin(skin);
            if (renderer == null) {
                continue;
            }
            boolean slim = skin == PlayerSkin.Model.SLIM;
            renderer.addLayer(new RiderSuitLayer(
                    renderer,
                    event.getContext().bakeLayer(slim ? RIDER_SUIT_SLIM : RIDER_SUIT_WIDE),
                    slim
            ));
        }
    }
}
