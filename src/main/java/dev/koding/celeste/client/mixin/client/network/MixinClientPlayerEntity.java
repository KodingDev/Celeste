package dev.koding.celeste.client.mixin.client.network;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.koding.celeste.client.Client;
import dev.koding.celeste.client.systems.command.CommandSystem;
import dev.koding.celeste.client.utils.Chat;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity {

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void sendChatMessage(String message, CallbackInfo ci) {
        if (!message.startsWith(Client.INSTANCE.getConfig().getCommandPrefix())) return;

        try {
            CommandSystem.dispatch(message.substring(Client.INSTANCE.getConfig().getCommandPrefix().length()));
        } catch (CommandSyntaxException e) {
            Chat.error(e.getMessage());
        }

        ci.cancel();
    }

}
