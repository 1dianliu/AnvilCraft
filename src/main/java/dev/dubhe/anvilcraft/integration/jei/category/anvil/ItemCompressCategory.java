package dev.dubhe.anvilcraft.integration.jei.category.anvil;

import dev.dubhe.anvilcraft.init.ModBlocks;
import dev.dubhe.anvilcraft.init.ModRecipeTypes;
import dev.dubhe.anvilcraft.integration.jei.AnvilCraftJeiPlugin;
import dev.dubhe.anvilcraft.integration.jei.drawable.DrawableBlockStateIcon;
import dev.dubhe.anvilcraft.integration.jei.util.JeiRecipeUtil;
import dev.dubhe.anvilcraft.integration.jei.util.JeiRenderHelper;
import dev.dubhe.anvilcraft.integration.jei.util.JeiSlotUtil;
import dev.dubhe.anvilcraft.integration.jei.util.TextureConstants;
import dev.dubhe.anvilcraft.recipe.anvil.ItemCompressRecipe;
import dev.dubhe.anvilcraft.util.RenderHelper;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ItemCompressCategory implements IRecipeCategory<RecipeHolder<ItemCompressRecipe>> {

    public static final int WIDTH = 162;
    public static final int HEIGHT = 64;

    private final IDrawable icon;
    private final IDrawable slot;
    private final Component title;
    private final ITickTimer timer;

    private final IDrawable arrowIn;
    private final IDrawable arrowOut;

    public ItemCompressCategory(IGuiHelper helper) {
        icon = new DrawableBlockStateIcon(Blocks.ANVIL.defaultBlockState(), Blocks.CAULDRON.defaultBlockState());
        slot = helper.getSlotDrawable();
        title = Component.translatable("gui.anvilcraft.category.item_compress");
        timer = helper.createTickTimer(30, 60, true);

        arrowIn = helper.createDrawable(TextureConstants.ANVIL_CRAFT_SPRITES, 0, 31, 16, 8);
        arrowOut = helper.createDrawable(TextureConstants.ANVIL_CRAFT_SPRITES, 0, 40, 16, 10);
    }

    @Override
    public RecipeType<RecipeHolder<ItemCompressRecipe>> getRecipeType() {
        return AnvilCraftJeiPlugin.ITEM_COMPRESS;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(
        IRecipeLayoutBuilder builder, RecipeHolder<ItemCompressRecipe> recipeHolder, IFocusGroup focuses) {
        ItemCompressRecipe recipe = recipeHolder.value();
        JeiSlotUtil.addInputSlots(builder, recipe.mergedIngredients);
        JeiSlotUtil.addOutputSlots(builder, recipe.results);
    }

    @Override
    public void draw(
        RecipeHolder<ItemCompressRecipe> recipeHolder,
        IRecipeSlotsView recipeSlotsView,
        GuiGraphics guiGraphics,
        double mouseX,
        double mouseY) {
        ItemCompressRecipe recipe = recipeHolder.value();
        float anvilYOffset = JeiRenderHelper.getAnvilAnimationOffset(timer);
        RenderHelper.renderBlock(
            guiGraphics,
            Blocks.ANVIL.defaultBlockState(),
            81,
            22 + anvilYOffset,
            20,
            12,
            RenderHelper.SINGLE_BLOCK);
        RenderHelper.renderBlock(
            guiGraphics, Blocks.CAULDRON.defaultBlockState(), 81, 40, 10, 12, RenderHelper.SINGLE_BLOCK);

        arrowIn.draw(guiGraphics, 54, 32);
        arrowOut.draw(guiGraphics, 92, 31);

        int inputSize = recipe.mergedIngredients.size();
        JeiSlotUtil.drawInputSlots(guiGraphics, slot, inputSize);
        int outputSize = recipe.results.size();
        JeiSlotUtil.drawOutputSlots(guiGraphics, slot, outputSize);
    }

    public static void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(
            AnvilCraftJeiPlugin.ITEM_COMPRESS,
            JeiRecipeUtil.getRecipeHoldersFromType(ModRecipeTypes.ITEM_COMPRESS_TYPE.get()));
    }

    public static void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(Items.ANVIL), AnvilCraftJeiPlugin.ITEM_COMPRESS);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ROYAL_ANVIL), AnvilCraftJeiPlugin.ITEM_COMPRESS);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.EMBER_ANVIL), AnvilCraftJeiPlugin.ITEM_COMPRESS);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.GIANT_ANVIL), AnvilCraftJeiPlugin.ITEM_COMPRESS);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SPECTRAL_ANVIL), AnvilCraftJeiPlugin.ITEM_COMPRESS);
        registration.addRecipeCatalyst(new ItemStack(Items.CAULDRON), AnvilCraftJeiPlugin.ITEM_COMPRESS);
    }
}
