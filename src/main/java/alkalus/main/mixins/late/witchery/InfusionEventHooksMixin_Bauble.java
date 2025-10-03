package alkalus.main.mixins.late.witchery;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.infusion.Infusion;

import baubles.api.BaublesApi;

@SuppressWarnings("UnusedMixin")
@Mixin(Infusion.EventHooks.class)
public abstract class InfusionEventHooksMixin_Bauble {

    @Redirect(
            method = "onLivingUpdate",
            remap = false,
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/entity/EntityLivingBase;getEquipmentInSlot(I)Lnet/minecraft/item/ItemStack;"))
    private ItemStack witcheryExtras$searchForBarkBeltInBaubles(EntityLivingBase entity, ItemStack original) {
        if (original != null && original.getItem() == Witchery.Items.BARK_BELT) return original;
        if (!(entity instanceof EntityPlayer player)) return original;
        IInventory baubles = BaublesApi.getBaubles(player);
        if (baubles == null) return original;
        for (int slot = 0; slot < baubles.getSizeInventory(); slot++) {
            ItemStack bauble = baubles.getStackInSlot(slot);
            if (bauble.getItem() == Witchery.Items.BARK_BELT) return bauble;
        }
        return original;
    }
}
