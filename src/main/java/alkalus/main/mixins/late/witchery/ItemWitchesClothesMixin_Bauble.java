package alkalus.main.mixins.late.witchery;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.item.ItemWitchesClothes;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.expanded.BaubleExpandedSlots;
import baubles.api.expanded.BaubleItemHelper;
import baubles.api.expanded.IBaubleExpanded;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SuppressWarnings("UnusedMixin")
@Mixin(ItemWitchesClothes.class)
public abstract class ItemWitchesClothesMixin_Bauble implements IBaubleExpanded {

    @Unique
    private static final String[] witcheryExtras$beltBaubleTypes = { BaubleExpandedSlots.beltType };

    @SideOnly(Side.CLIENT)
    @WrapMethod(method = "addInformation")
    public void witcheryExtras$addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip,
            boolean expanded, Operation<Void> original) {
        original.call(stack, player, tooltip, expanded);
        if (witcheryExtras$isBeltBauble(stack)) {
            BaubleItemHelper.addSlotInformation(tooltip, witcheryExtras$beltBaubleTypes);
        }
    }

    @WrapMethod(method = "isBeltWorn", remap = false)
    public boolean witcheryExtras$isBeltWorn(EntityPlayer player, Operation<Boolean> original) {
        if (original.call(player)) return true;
        IInventory baubles = BaublesApi.getBaubles(player);
        if (baubles == null) return false;
        ItemWitchesClothes castThis = (ItemWitchesClothes) (Object) this;
        for (int slot = 0; slot < baubles.getSizeInventory(); slot++) {
            ItemStack bauble = baubles.getStackInSlot(slot);
            if (bauble != null && bauble.getItem() == castThis) return true;
        }
        return false;
    }

    @Unique
    private static boolean witcheryExtras$isBeltBauble(ItemStack stack) {
        return stack != null
                && (stack.getItem() == Witchery.Items.BARK_BELT || stack.getItem() == Witchery.Items.BITING_BELT);
    }

    // Baubles interface methods
    @Unique
    @Override
    public String[] getBaubleTypes(ItemStack itemstack) {
        return witcheryExtras$beltBaubleTypes;
    }

    @Unique
    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return null;
    }

    @Unique
    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {}

    @Unique
    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {}

    @Unique
    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {}

    @Unique
    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        return witcheryExtras$isBeltBauble(itemstack);
    }

    @Unique
    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }
}
