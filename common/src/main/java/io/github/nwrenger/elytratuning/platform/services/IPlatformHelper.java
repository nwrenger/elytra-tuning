package io.github.nwrenger.elytratuning.platform.services;

import java.nio.file.Path;
import java.util.function.Consumer;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public interface IPlatformHelper {
    /**
     * Gets the path to the configuration directory.
     *
     * @return The path to the configuration directory.
     */
    Path getConfigDir();

    /**
     * Registers a listener for the player join event.
     *
     * @param listener The listener to register.
     */
    void registerPlayerJoin(Consumer<ServerPlayer> listener);

    /**
     * Sends a custom packet payload to a specific client.
     *
     * @param player  The player to send the packet to.
     * @param payload The custom packet payload to send.
     */
    void sendToClient(ServerPlayer player, CustomPacketPayload payload);
}
