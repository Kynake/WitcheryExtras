package alkalus.main.mixins.late.witchery;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.item.ItemGoblinClothes;
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
@Mixin(ItemGoblinClothes.class)
public abstract class ItemGoblinClothesMixin implements IBaubleExpanded {

    @Unique
    private static final String[] witcheryExtras$mogsQuiverBaubleTypes = { BaubleExpandedSlots.quiverType };

    @Unique
    private static final String[] witcheryExtras$gulgsGurdleBaubleTypes = { BaubleExpandedSlots.beltType };

    @Shadow
    public abstract void onArmorTick(World world, EntityPlayer player, ItemStack itemStack);

    @SideOnly(Side.CLIENT)
    @WrapMethod(method = "addInformation")
    public void witcheryExtras$addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip,
            boolean expanded, Operation<Void> original) {
        original.call(stack, player, tooltip, expanded);
        if (stack == null) return;

        if (stack.getItem() == Witchery.Items.MOGS_QUIVER) {
            BaubleItemHelper.addSlotInformation(tooltip, witcheryExtras$mogsQuiverBaubleTypes);
        } else if (stack.getItem() == Witchery.Items.GULGS_GURDLE) {
            BaubleItemHelper.addSlotInformation(tooltip, witcheryExtras$gulgsGurdleBaubleTypes);
        }
    }

    @WrapMethod(method = "isQuiverWorn", remap = false)
    private static boolean witcheryExtras$isQuiverWorn(EntityPlayer player, Operation<Boolean> original) {
        if (original.call(player)) return true;
        IInventory baubles = BaublesApi.getBaubles(player);
        if (baubles == null) return false;
        for (int i = 0; i < baubles.getSizeInventory(); i++) {
            ItemStack stack = baubles.getStackInSlot(i);
            if (stack != null && stack.getItem() == Witchery.Items.MOGS_QUIVER) return true;
        }
        return false;
    }

    @WrapMethod(method = "isBeltWorn", remap = false)
    private static boolean witcheryExtras$isBeltWorn(EntityPlayer player, Operation<Boolean> original) {
        if (original.call(player)) return true;
        IInventory baubles = BaublesApi.getBaubles(player);
        if (baubles == null) return false;
        for (int i = 0; i < baubles.getSizeInventory(); i++) {
            ItemStack stack = baubles.getStackInSlot(i);
            if (stack != null && stack.getItem() == Witchery.Items.GULGS_GURDLE) return true;
        }
        return false;
    }

    // Baubles interface methods
    @Unique
    @Override
    public String[] getBaubleTypes(ItemStack stack) {
        if (stack.getItem() == Witchery.Items.GULGS_GURDLE) {
            return witcheryExtras$gulgsGurdleBaubleTypes;
        }

        if (stack.getItem() == Witchery.Items.MOGS_QUIVER) {
            return witcheryExtras$mogsQuiverBaubleTypes;
        }

        return null;
    }

    @Unique
    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return null;
    }

    @Unique
    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase entity) {
        if (!(entity instanceof EntityPlayer player)) return;
        onArmorTick(entity.worldObj, player, itemstack);
    }

    @Unique
    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase player) {}

    @Unique
    @Override
    public void onUnequipped(ItemStack stack, EntityLivingBase player) {}

    @Unique
    @Override
    public boolean canEquip(ItemStack stack, EntityLivingBase player) {
        return stack.getItem() == Witchery.Items.GULGS_GURDLE || stack.getItem() == Witchery.Items.MOGS_QUIVER;
    }

    @Unique
    @Override
    public boolean canUnequip(ItemStack stack, EntityLivingBase player) {
        return true;
    }
}
