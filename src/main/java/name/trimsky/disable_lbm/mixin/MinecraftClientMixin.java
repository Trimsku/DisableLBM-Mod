package name.trimsky.disable_lbm.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Inject(method = "doAttack", at=@At(value = "HEAD"), cancellable = true)
    private void breakIfModActivated(CallbackInfoReturnable<Boolean> cir) {
        File file = new File(MinecraftClient.getInstance().runDirectory, "lbm_disabled.lock");
        if(file.exists()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "handleBlockBreaking(Z)V", at=@At(value = "HEAD"), cancellable = true)
    private void breakIfModActivated(boolean breaking, CallbackInfo ci) {
        File file = new File(MinecraftClient.getInstance().runDirectory, "lbm_disabled.lock");
        if(file.exists()) {
            ci.cancel();
        }
    }
}
