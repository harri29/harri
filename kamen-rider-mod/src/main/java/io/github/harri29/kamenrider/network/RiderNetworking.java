package io.github.harri29.kamenrider.network;

import io.github.harri29.kamenrider.RiderForm;
import io.github.harri29.kamenrider.RiderTransformation;
import io.github.harri29.kamenrider.client.RiderClientState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/** Networking bridge used by Rider rendering and first-person presentation FX. */
public final class RiderNetworking {
    private RiderNetworking() {
    }

    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("2");

        if (FMLEnvironment.dist == Dist.CLIENT) {
            registrar.playToClient(
                    RiderStatePayload.TYPE,
                    RiderStatePayload.STREAM_CODEC,
                    (payload, context) -> context.enqueueWork(() -> RiderClientState.accept(payload))
            );
            registrar.playToClient(
                    RiderPresentationPayload.TYPE,
                    RiderPresentationPayload.STREAM_CODEC,
                    (payload, context) -> context.enqueueWork(() -> RiderClientState.accept(payload))
            );
        } else {
            registrar.playToClient(
                    RiderStatePayload.TYPE,
                    RiderStatePayload.STREAM_CODEC,
                    (payload, context) -> { }
            );
            registrar.playToClient(
                    RiderPresentationPayload.TYPE,
                    RiderPresentationPayload.STREAM_CODEC,
                    (payload, context) -> { }
            );
        }
    }

    /** Real gameplay change: clients should play the Henshin/form-change presentation. */
    public static void sync(Player player, RiderForm form) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        String formId = form == null ? "" : form.id();
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(
                serverPlayer,
                new RiderStatePayload(serverPlayer.getId(), formId, true)
        );
    }

    /** Short cinematic cue used by first-person camera/hand presentation. */
    public static void presentation(Player player, String effectId) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(
                serverPlayer,
                new RiderPresentationPayload(serverPlayer.getId(), effectId)
        );
    }

    /** Snapshot for a newly tracking client: show the current suit immediately, with no fake Henshin replay. */
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (!(event.getEntity() instanceof ServerPlayer watcher)) {
            return;
        }
        if (!(event.getTarget() instanceof Player target)) {
            return;
        }

        String formId = RiderTransformation.currentForm(target)
                .map(RiderForm::id)
                .orElse("");
        PacketDistributor.sendToPlayer(watcher, new RiderStatePayload(target.getId(), formId, false));
    }
}
