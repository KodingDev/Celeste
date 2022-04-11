package dev.koding.celeste.client.mixin.client.gui.hud;

import dev.koding.celeste.client.event.EventBus;
import dev.koding.celeste.client.event.impl.client.Render2DEvent;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHud {
    @Inject(method = "render", at = @At("TAIL"))
    private void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        EventBus.post(new Render2DEvent(tickDelta, matrices));
    }
}
