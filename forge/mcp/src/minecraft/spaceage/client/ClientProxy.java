package spaceage.client;

import java.util.EnumSet;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.world.World;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import spaceage.client.model.ModelOrganicFemale;
import spaceage.client.model.ModelStarboost;
import spaceage.common.CommonProxy;
import spaceage.common.PlayerTickHandler;
import spaceage.common.SpaceAgeCore;
import spaceage.common.tile.TileGasTank;
import spaceage.common.tile.TileHeatGenerator;
import spaceage.common.tile.TileLiquidTank;
import spaceage.common.tile.TileSolarPanel;
import spaceage.common.tile.render.TileCableRenderer;
import spaceage.common.tile.render.TileGasTankRenderer;
import spaceage.common.tile.render.TileHeatGeneratorRenderer;
import spaceage.common.tile.render.TileLiquidTankRenderer;
import spaceage.planets.aliens.entity.EntityBinary;
import spaceage.planets.aliens.entity.EntityBinaryFemale;
import spaceage.planets.aliens.model.ModelBinaryFemaleTest;
import spaceage.planets.aliens.render.RenderBinary;
import spaceage.planets.aliens.render.RenderBinaryFemale;

/**
 * The client proxy, implementing rendering, of SpaceAgeCore. 
 * @author SkylordJoel
 */

public class ClientProxy extends CommonProxy {
	
	private static int renderIDCable;
	
	//public static int blockMultiRenderType;
	//public static int renderPass;
	
	private static final ModelStarboost advancedSpacesuitChestplate = new ModelStarboost(1.0F);
	private static final ModelStarboost advancedSpacesuitLeggings = new ModelStarboost(0.5F);
	
	private static final ModelOrganicFemale femaleChest = new ModelOrganicFemale(1.0F);
	private static final ModelOrganicFemale femaleLegs = new ModelOrganicFemale(0.5F);

	@Override
	public ModelBiped getStarboostArmorModel(int id){
		switch (id) {
			case 0:
				return advancedSpacesuitChestplate;
			case 1:
				return advancedSpacesuitLeggings;
			default:
				break;
		}
		return advancedSpacesuitChestplate; //default, if whenever you should have passed on a wrong id
	}
	
	@Override
	public ModelBiped getFemaleBinaryArmorModel(int id) {
		switch(id) {
		case 0:
			return femaleChest;
		case 1:
			return femaleLegs;
		default:
			break;
		}
		return femaleChest;
	}
	
	@Override
	public void registerRenderers() {
		float shadowSize = 0.5F;
		
		RenderingRegistry.registerEntityRenderingHandler(EntityBinary.class, new RenderBinary(new ModelBiped(), shadowSize));
		
		RenderingRegistry.registerEntityRenderingHandler(EntityBinaryFemale.class, new RenderBinaryFemale(new ModelBinaryFemaleTest(), shadowSize));
		
		
		//tile entity rendering s***
		ClientRegistry.bindTileEntitySpecialRenderer(TileGasTank.class, new TileGasTankRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileLiquidTank.class, new TileLiquidTankRenderer());
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileHeatGenerator.class, new TileHeatGeneratorRenderer());
		
		this.renderIDCable = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new TileCableRenderer(this.renderIDCable));
	}
	
	/*@Override
	public void setCustomRenderers() {
		//this.blockMultiRenderType = RenderingRegistry.getNextAvailableRenderId();
		//RenderingRegistry.registerBlockHandler(new MultiPassRenderTestRenderer());
	}*/
	
	@Override
	public int getBlockRenderID(int blockID) {
		if(blockID == SpaceAgeCore.cable.blockID) {
			return this.renderIDCable;
		}
		return -1;
	}
	
	@Override
	public void load() {
		TickRegistry.registerTickHandler(new SpaceAgeHUDHandler(), Side.CLIENT);
		//TickRegistry.registerTickHandler(new SpaceAgeTickHandler(EnumSet.of(TickType.CLIENT)), Side.CLIENT);
	}
	
	@Override
	public void registerHandlers() {
		TickRegistry.registerTickHandler(new PlayerTickHandler(), Side.SERVER);
	    TickRegistry.registerTickHandler(new PlayerTickHandler(), Side.CLIENT);
	}
}
