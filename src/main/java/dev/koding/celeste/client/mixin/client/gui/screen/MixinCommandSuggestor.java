package dev.koding.celeste.client.mixin.client.gui.screen;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import dev.koding.celeste.client.Client;
import dev.koding.celeste.client.systems.command.CommandSystem;
import net.minecraft.client.gui.screen.CommandSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.command.CommandSource;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.concurrent.CompletableFuture;

@Mixin(CommandSuggestor.class)
public abstract class MixinCommandSuggestor {

    @Shadow
    private @Nullable ParseResults<CommandSource> parse;

    @Shadow
    @Final
    TextFieldWidget textField;

    @Shadow
    @Nullable CommandSuggestor.SuggestionWindow window;

    @Shadow
    boolean completingSuggestions;

    @Shadow
    private @Nullable CompletableFuture<Suggestions> pendingSuggestions;

    @Shadow
    protected abstract void show();

    @Inject(method = "refresh", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/StringReader;canRead()Z", remap = false), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void refresh(CallbackInfo ci, String string, StringReader reader) {
        String prefix = Client.INSTANCE.getConfig().getCommandPrefix();
        int length = prefix.length();

        if (reader.canRead(length) && reader.getString().startsWith(prefix, reader.getCursor())) {
            reader.setCursor(reader.getCursor() + length);

            CommandDispatcher<CommandSource> dispatcher = CommandSystem.getDispatcher();
            if (this.parse == null) {
                this.parse = dispatcher.parse(reader, CommandSystem.getCommandSource());
            }

            int cursor = textField.getCursor();
            if (cursor >= 1 && (this.window == null || !completingSuggestions)) {
                this.pendingSuggestions = dispatcher.getCompletionSuggestions(this.parse, cursor);
                this.pendingSuggestions.thenRun(() -> {
                    if (this.pendingSuggestions.isDone()) {
                        this.show();
                    }
                });
            }

            ci.cancel();
        }
    }

}
