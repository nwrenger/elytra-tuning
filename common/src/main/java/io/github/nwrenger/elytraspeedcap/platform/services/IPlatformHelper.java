package io.github.nwrenger.elytraspeedcap.platform.services;

import java.nio.file.Path;
import java.util.function.Consumer;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public interface IPlatformHelper {
    Path getConfigDir();

    void registerEndServerTick(Consumer<MinecraftServer> listener);

    void registerPlayerJoin(Consumer<ServerPlayer> listener);

    void sendToClient(ServerPlayer player, CustomPacketPayload payload);
}
