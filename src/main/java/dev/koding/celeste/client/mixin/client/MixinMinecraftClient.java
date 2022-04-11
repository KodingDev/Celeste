package dev.koding.celeste.client.mixin.client;

import dev.koding.celeste.client.event.EventBus;
import dev.koding.celeste.client.event.impl.world.TickEvent;
import dev.koding.celeste.client.utils.Window;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {

    @Inject(method = "updateWindowTitle", at = @At("HEAD"), cancellable = true)
    private void updateWindowTitle(CallbackInfo ci) {
        Window.INSTANCE.setIcon();
        Window.INSTANCE.setTitle();
        ci.cancel();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void preTick(CallbackInfo ci) {
        EventBus.post(new TickEvent(TickEvent.Phase.PRE));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void postTick(CallbackInfo ci) {
        EventBus.post(new TickEvent(TickEvent.Phase.POST));
    }

}
