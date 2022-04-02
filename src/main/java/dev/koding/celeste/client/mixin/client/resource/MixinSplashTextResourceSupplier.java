package dev.koding.celeste.client.mixin.client.resource;

import dev.koding.celeste.client.utils.Window;
import net.minecraft.client.resource.SplashTextResourceSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SplashTextResourceSupplier.class)
public class MixinSplashTextResourceSupplier {

    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
    private void get(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(Window.INSTANCE.getColoredSplash());
    }

}
