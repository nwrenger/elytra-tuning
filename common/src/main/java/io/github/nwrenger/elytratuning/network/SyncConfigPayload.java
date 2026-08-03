package io.github.nwrenger.elytratuning.network;

import io.github.nwrenger.elytratuning.Constants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public record SyncConfigPayload(
    String configJson
) implements CustomPacketPayload {
    public static final Identifier PAYLOAD_ID = Identifier.fromNamespaceAndPath(
        Constants.MOD_ID,
        "sync_config"
    );
    public static final CustomPacketPayload.@NonNull Type<@NonNull SyncConfigPayload> ID =
        new CustomPacketPayload.Type<>(PAYLOAD_ID);
    public static final StreamCodec<
        RegistryFriendlyByteBuf,
        @NonNull SyncConfigPayload
    > CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8,
        SyncConfigPayload::configJson,
        SyncConfigPayload::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
