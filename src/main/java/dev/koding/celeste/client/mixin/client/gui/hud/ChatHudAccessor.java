package dev.koding.celeste.client.mixin.client.gui.hud;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChatHud.class)
public interface ChatHudAccessor {
    @Invoker("addMessage")
    void add(Text message, int messageId);
}
