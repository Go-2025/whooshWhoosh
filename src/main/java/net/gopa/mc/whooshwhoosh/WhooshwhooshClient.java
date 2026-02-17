package net.gopa.mc.whooshwhoosh;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public class WhooshwhooshClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayerEntity self = MinecraftClient.getInstance().player;
    }
}
