package alkalus.main.mixins.late.witchery;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.common.GenericEvents;
import com.emoniph.witchery.item.ItemWitchesClothes;
import com.llamalad7.mixinextras.sugar.Local;

import baubles.api.BaublesApi;

@SuppressWarnings("UnusedMixin")
@Mixin(GenericEvents.class)
public abstract class GenericEventsMixin_Bauble {

    @Redirect(
            method = "onLivingHurt",
            remap = false,
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/entity/player/InventoryPlayer;armorItemInSlot(I)Lnet/minecraft/item/ItemStack;"))
    private ItemStack witcheryExtras$searchForBitingBeltInBaubles(InventoryPlayer inventory, ItemStack original) {
        return witcheryExtras$searchForBeltInBaubles(inventory.player, original, Witchery.Items.BITING_BELT);
    }

    @ModifyVariable(
            method = "onLivingHurt",
            remap = false,
            name = "belt",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/entity/player/EntityPlayer;getEquipmentInSlot(I)Lnet/minecraft/item/ItemStack;"))
    private ItemStack witcheryExtras$searchForBarkBeltInBaubles(ItemStack original,
            @Local(name = "player") EntityPlayer player) {
        return witcheryExtras$searchForBeltInBaubles(player, original, Witchery.Items.BARK_BELT);
    }

    @Unique
    private ItemStack witcheryExtras$searchForBeltInBaubles(final EntityPlayer player, final ItemStack armorStack,
            final ItemWitchesClothes belt) {
        if (armorStack != null && armorStack.getItem() == belt) return armorStack;
        IInventory baubles = BaublesApi.getBaubles(player);
        if (baubles == null) return armorStack;
        for (int slot = 0; slot < baubles.getSizeInventory(); slot++) {
            ItemStack bauble = baubles.getStackInSlot(slot);
            if (bauble.getItem() == belt) return bauble;
        }
        return armorStack;
    }
}
