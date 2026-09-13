package io.github.harri29.kamenrider.network;

import io.github.harri29.kamenrider.KamenRiderMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** Server -> client snapshot used by the suit renderer. */
public record RiderStatePayload(int entityId, String formId, boolean animate) implements CustomPacketPayload {
    public static final Type<RiderStatePayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(KamenRiderMod.MODID, "rider_state")
    );

    public static final StreamCodec<FriendlyByteBuf, RiderStatePayload> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public RiderStatePayload decode(FriendlyByteBuf buffer) {
            return new RiderStatePayload(buffer.readVarInt(), buffer.readUtf(64), buffer.readBoolean());
        }

        @Override
        public void encode(FriendlyByteBuf buffer, RiderStatePayload payload) {
            buffer.writeVarInt(payload.entityId());
            buffer.writeUtf(payload.formId(), 64);
            buffer.writeBoolean(payload.animate());
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
