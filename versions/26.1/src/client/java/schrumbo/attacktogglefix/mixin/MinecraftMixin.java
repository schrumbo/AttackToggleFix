package schrumbo.attacktogglefix.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow @Final public Options options;
    @Shadow public LocalPlayer player;

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
                && Boolean.TRUE.equals(options.toggleAttack().get())
                && options.keyAttack.isDown()) {
            ((KeyMappingAccessor) options.keyAttack).attackToggleFix$setIsDown(false);
        }

        attackToggleFix$previousSlot = currentSlot;
    }
}
