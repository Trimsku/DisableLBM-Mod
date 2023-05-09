package name.trimsky.disable_lbm.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.IOException;

@Environment(net.fabricmc.api.EnvType.CLIENT)
public class DisableLBMClient implements ClientModInitializer {
    private static KeyBinding leftMouseButton = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.disable_lbm.disable_lbm", // The translation key of the keybinding's name
            InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_M, // The keycode of the key
            "category.disable_lbm.disable_lbm" // The translation key of the keybinding's category.
    ));

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (leftMouseButton.wasPressed()) {
                File file = new File(MinecraftClient.getInstance().runDirectory, "lbm_disabled.lock");
                if(!file.exists()) {
                    try {
                        if(file.createNewFile()) {
                            client.player.sendMessage(Text.literal("Левая кнопка мыши отключена!"), true);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    if(file.delete()) {
                        client.player.sendMessage(Text.literal("Левая кнопка мыши включена!"), true);
                    }
                }
            }
        });
    }
}
