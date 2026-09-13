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
    public static final ModelLayerLocation KUUGA_SUIT_WIDE = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(KamenRiderMod.MODID, "kuuga_suit"), "wide"
    );
    public static final ModelLayerLocation KUUGA_SUIT_SLIM = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(KamenRiderMod.MODID, "kuuga_suit"), "slim"
    );
    public static final ModelLayerLocation DECADE_SUIT_WIDE = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(KamenRiderMod.MODID, "decade_suit"), "wide"
    );
    public static final ModelLayerLocation DECADE_SUIT_SLIM = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(KamenRiderMod.MODID, "decade_suit"), "slim"
    );
    public static final ModelLayerLocation DOUBLE_SUIT_WIDE = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(KamenRiderMod.MODID, "double_suit"), "wide"
    );
    public static final ModelLayerLocation DOUBLE_SUIT_SLIM = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(KamenRiderMod.MODID, "double_suit"), "slim"
    );

    public KamenRiderClient(IEventBus modBus) {
        modBus.addListener(KamenRiderClient::registerLayerDefinitions);
        modBus.addListener(KamenRiderClient::addPlayerLayers);
    }

    private static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(
                RIDER_SUIT_WIDE,
                () -> LayerDefinition.create(PlayerModel.createMesh(new CubeDeformation(0.08F), false), 64, 64)
        );
        event.registerLayerDefinition(
                RIDER_SUIT_SLIM,
                () -> LayerDefinition.create(PlayerModel.createMesh(new CubeDeformation(0.08F), true), 64, 64)
        );
        event.registerLayerDefinition(KUUGA_SUIT_WIDE, () -> KuugaSuitModel.createBodyLayer(false));
        event.registerLayerDefinition(KUUGA_SUIT_SLIM, () -> KuugaSuitModel.createBodyLayer(true));
        event.registerLayerDefinition(DECADE_SUIT_WIDE, () -> DecadeSuitModel.createBodyLayer(false));
        event.registerLayerDefinition(DECADE_SUIT_SLIM, () -> DecadeSuitModel.createBodyLayer(true));
        event.registerLayerDefinition(DOUBLE_SUIT_WIDE, () -> DoubleSuitModel.createBodyLayer(false));
        event.registerLayerDefinition(DOUBLE_SUIT_SLIM, () -> DoubleSuitModel.createBodyLayer(true));
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
            renderer.addLayer(new KuugaSuitLayer(
                    renderer,
                    event.getContext().bakeLayer(slim ? KUUGA_SUIT_SLIM : KUUGA_SUIT_WIDE),
                    slim
            ));
            renderer.addLayer(new DecadeSuitLayer(
                    renderer,
                    event.getContext().bakeLayer(slim ? DECADE_SUIT_SLIM : DECADE_SUIT_WIDE),
                    slim
            ));
            renderer.addLayer(new DoubleSuitLayer(
                    renderer,
                    event.getContext().bakeLayer(slim ? DOUBLE_SUIT_SLIM : DOUBLE_SUIT_WIDE),
                    slim
            ));
        }
    }
}
