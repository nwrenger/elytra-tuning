package io.github.nwrenger.elytraspeedcap.platform;

import io.github.nwrenger.elytraspeedcap.platform.services.IPlatformHelper;
import java.nio.file.Path;
import java.util.function.Consumer;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.registration.NetworkRegistry;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public void registerEndServerTick(Consumer<MinecraftServer> listener) {
        NeoForge.EVENT_BUS.addListener((ServerTickEvent.Post event) ->
            listener.accept(event.getServer())
        );
    }

    @Override
    public void registerPlayerJoin(Consumer<ServerPlayer> listener) {
        NeoForge.EVENT_BUS.addListener((PlayerEvent.PlayerLoggedInEvent event) -> {
            if (event.getEntity() instanceof ServerPlayer player) {
                listener.accept(player);
            }
        });
    }

    @Override
    public void sendToClient(
        ServerPlayer player,
        CustomPacketPayload payload
    ) {
        if (NetworkRegistry.hasChannel(player.connection, payload.type().id())) {
            PacketDistributor.sendToPlayer(player, payload);
        }
    }
}
