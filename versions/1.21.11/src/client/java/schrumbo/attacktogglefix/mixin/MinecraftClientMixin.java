package schrumbo.attacktogglefix.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow @Final public GameOptions options;
    @Shadow public ClientPlayerEntity player;

    @Unique
    private int attackToggleFix$previousSlot = -1;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (player == null) {
            attackToggleFix$previousSlot = -1;
            return;
        }

        int currentSlot = player.getInventory().getSelectedSlot();

        if (attackToggleFix$previousSlot != -1
                && currentSlot != attackToggleFix$previousSlot
                && Boolean.TRUE.equals(options.getAttackToggled().getValue())
                && options.attackKey.isPressed()) {
            ((KeyBindingAccessor) options.attackKey).attackToggleFix$setPressed(false);
        }

        attackToggleFix$previousSlot = currentSlot;
    }
}
