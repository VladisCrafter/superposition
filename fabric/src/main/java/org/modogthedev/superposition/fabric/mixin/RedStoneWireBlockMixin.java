package org.modogthedev.superposition.fabric.mixin;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.modogthedev.superposition.fabric.util.ConnectableRedstoneBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RedStoneWireBlock.class)
public class RedStoneWireBlockMixin {
    @Inject(
            method = "shouldConnectTo(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void port_lib$shouldConnectTo(BlockState state, Direction side, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() instanceof ConnectableRedstoneBlock connectable) {
            // Passing null for world and pos here just for extra upstream compat, not properly implementing it because
            // 1. world and pos are never used in Create
            // 2. extra work :help_me:
            cir.setReturnValue(connectable.canConnectRedstone(state, null, null, side));
        }
    }
}
