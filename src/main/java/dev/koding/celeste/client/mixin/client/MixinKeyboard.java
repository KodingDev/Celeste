package dev.koding.celeste.client.mixin.client;

import dev.koding.celeste.client.event.EventBus;
import dev.koding.celeste.client.event.impl.client.KeyboardEvent;
import dev.koding.celeste.client.utils.Input;
import net.minecraft.client.Keyboard;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class MixinKeyboard {

    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (key == GLFW.GLFW_KEY_UNKNOWN) return;
        Input.getKeyboard().set(key, action != GLFW.GLFW_RELEASE);

        if (EventBus.postBlocking(new KeyboardEvent(key, modifiers, KeyboardEvent.Action.parse(action))).isCancelled()) {
            ci.cancel();
        }
    }

}
