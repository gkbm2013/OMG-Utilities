package omg_utilities.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import omg_utilities.inventory.ContainerSpiralChest;
import omg_utilities.main.Main;
import omg_utilities.tileentity.SpiralChestTileEntity;

public class SpiralChestGui extends GuiContainer{

	private static final ResourceLocation SCGuiTextures = new ResourceLocation(Main.MODID, "textures/gui/sprialtext.png");
	public World world;
	private SpiralChestTileEntity tileSC;
	
	public SpiralChestGui(InventoryPlayer invPlayer, SpiralChestTileEntity tile) {
		super(new ContainerSpiralChest(invPlayer, tile));
		this.tileSC = tile;
		this.xSize = 176;
		this.ySize = 246;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int cornerX = (width - xSize) / 2;
	    int cornerY = (height - ySize) / 2;
		
		String string = this.tileSC.hasCustomName() ? this.tileSC.getName() : I18n.format(this.tileSC.getName(), new Object[0]);
		
		this.fontRendererObj.drawString(string, (this.xSize - this.fontRendererObj.getStringWidth(string))/2, 6, 4210752);
		
		String storageAmount = I18n.format("omg.gui.sc.stack", new Object[0]) + " : " + tileSC.getStorageAmount() + "";
		this.fontRendererObj.drawString(storageAmount, 5, 120, 4210752);
		
		String prec = I18n.format("omg.gui.sc.prec", new Object[0]) + " : " + tileSC.getPrec() + " %";
		this.fontRendererObj.drawString(prec, 5, 132, 4210752);
		
		String teir = I18n.format("omg.gui.sc.tier", new Object[0]) + " : " + tileSC.getTier() + "";
		this.fontRendererObj.drawString(teir, 5, 144, 4210752);
		
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		int cornerX = (width - xSize) / 2;
	    int cornerY = (height - ySize) / 2;
		
		 GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        this.mc.getTextureManager().bindTexture(SCGuiTextures);
	        this.drawTexturedModalRect(cornerX, cornerY, 0, 0, this.xSize, this.ySize);
	}

}
