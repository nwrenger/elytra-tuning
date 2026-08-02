package io.github.nwrenger.elytraspeedcap.network;

import io.github.nwrenger.elytraspeedcap.Constants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public record SyncMaxSpeedPayload(double maxSpeed)
    implements CustomPacketPayload {

    public static final Identifier PAYLOAD_ID = Identifier.fromNamespaceAndPath(
        Constants.MOD_ID,
        "sync_max_speed"
    );
    public static final CustomPacketPayload.@NonNull Type<
        @NonNull SyncMaxSpeedPayload
    > ID = new CustomPacketPayload.Type<>(PAYLOAD_ID);
    public static final StreamCodec<
        RegistryFriendlyByteBuf,
        @NonNull SyncMaxSpeedPayload
    > CODEC = StreamCodec.composite(
        ByteBufCodecs.DOUBLE,
        SyncMaxSpeedPayload::maxSpeed,
        SyncMaxSpeedPayload::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
