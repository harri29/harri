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

/** Networking bridge used by client suit rendering. */
public final class RiderNetworking {
    private RiderNetworking() {
    }

    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        if (FMLEnvironment.dist == Dist.CLIENT) {
            registrar.playToClient(
                    RiderStatePayload.TYPE,
                    RiderStatePayload.STREAM_CODEC,
                    (payload, context) -> context.enqueueWork(() -> RiderClientState.accept(payload))
            );
        } else {
            // Dedicated servers still declare the clientbound payload type.
            registrar.playToClient(
                    RiderStatePayload.TYPE,
                    RiderStatePayload.STREAM_CODEC,
                    (payload, context) -> { }
            );
        }
    }

    public static void sync(Player player, RiderForm form) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        String formId = form == null ? "" : form.id();
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(
                serverPlayer,
                new RiderStatePayload(serverPlayer.getId(), formId)
        );
    }

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
        PacketDistributor.sendToPlayer(watcher, new RiderStatePayload(target.getId(), formId));
    }
}
