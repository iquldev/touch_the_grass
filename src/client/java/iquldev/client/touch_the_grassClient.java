package iquldev.client;

import iquldev.touch_the_grassConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class touch_the_grassClient implements ClientModInitializer {
    private static int ticksLeft = 0;
    private static boolean timerActive = false;

    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            ticksLeft = touch_the_grassConfig.timerMinutes * 60 * 20;
            timerActive = true;
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!timerActive || client.world == null) return;

            if (!client.isPaused()) {
                if (ticksLeft > 0) {
                    ticksLeft--;

                    if (ticksLeft % 20 == 0) {
                        int secondsLeft = ticksLeft / 20;
                        int hours = secondsLeft / 3600;
                        int minutes = (secondsLeft % 3600) / 60;
                        int seconds = secondsLeft % 60;

                        String timeStr = (hours > 0)
                            ? String.format("%02d:%02d:%02d", hours, minutes, seconds)
                            : String.format("%02d:%02d", minutes, seconds);

                        client.execute(() -> {
                            if (client.player != null) {
                                Text message;
                                if (secondsLeft <= 1) {
                                    message = Text.literal("TIME TO TOUCH THE GRASS!!!")
                                        .formatted(Formatting.GREEN, Formatting.BOLD);
                                } else if (secondsLeft <= 60) {
                                    message = Text.literal("[")
                                        .formatted(Formatting.RED, Formatting.BOLD)
                                        .append(Text.literal(timeStr).formatted(Formatting.RED, Formatting.BOLD))
                                        .append(Text.literal("]").formatted(Formatting.RED, Formatting.BOLD));
                                } else {
                                    message = Text.literal("[")
                                        .formatted(Formatting.GREEN)
                                        .append(Text.literal(timeStr).formatted(Formatting.GREEN))
                                        .append(Text.literal("]").formatted(Formatting.GREEN));
                                }
                                client.player.sendMessage(message, true);
                            }
                        });
                    }
                } else {
                    timerActive = false;
                    client.execute(() -> MinecraftClient.getInstance().scheduleStop());
                }
            }
        });
    }
}