package dev.koding.celeste.client.mixin.client;

import dev.koding.celeste.client.event.EventBus;
import dev.koding.celeste.client.event.impl.world.TickEvent;
import dev.koding.celeste.client.utils.Window;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {

    @Shadow
    public abstract Profiler getProfiler();

    @Inject(method = "updateWindowTitle", at = @At("HEAD"), cancellable = true)
    private void updateWindowTitle(CallbackInfo ci) {
        Window.INSTANCE.setIcon();
        Window.INSTANCE.setTitle();
        ci.cancel();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void preTick(CallbackInfo ci) {
        getProfiler().push("celeste_pre_tick");
        EventBus.postBlocking(new TickEvent(TickEvent.Phase.PRE));
        getProfiler().pop();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void postTick(CallbackInfo ci) {
        getProfiler().push("celeste_post_tick");
        EventBus.postBlocking(new TickEvent(TickEvent.Phase.POST));
        getProfiler().pop();
    }

}
