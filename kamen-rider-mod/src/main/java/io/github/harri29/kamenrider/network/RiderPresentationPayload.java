package io.github.harri29.kamenrider.network;

import io.github.harri29.kamenrider.KamenRiderMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** Lightweight server -> client cue for first-person Rider presentation FX. */
public record RiderPresentationPayload(int entityId, String effectId) implements CustomPacketPayload {
    public static final Type<RiderPresentationPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(KamenRiderMod.MODID, "rider_presentation")
    );

    public static final StreamCodec<FriendlyByteBuf, RiderPresentationPayload> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public RiderPresentationPayload decode(FriendlyByteBuf buffer) {
            return new RiderPresentationPayload(buffer.readVarInt(), buffer.readUtf(48));
        }

        @Override
        public void encode(FriendlyByteBuf buffer, RiderPresentationPayload payload) {
            buffer.writeVarInt(payload.entityId());
            buffer.writeUtf(payload.effectId(), 48);
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
