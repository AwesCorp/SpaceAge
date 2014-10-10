package cr0s.WarpDrive;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cr0s.WarpDrive.block.BlockAir;
import cr0s.WarpDrive.block.BlockAirDistributor;
import cr0s.WarpDrive.block.BlockCamera;
import cr0s.WarpDrive.block.BlockCloakingCoil;
import cr0s.WarpDrive.block.BlockCloakingDeviceCore;
import cr0s.WarpDrive.block.BlockGas;
import cr0s.WarpDrive.block.BlockIridium;
import cr0s.WarpDrive.block.BlockLaser;
import cr0s.WarpDrive.block.BlockLaserCam;
import cr0s.WarpDrive.block.BlockLift;
import cr0s.WarpDrive.block.BlockMiningLaser;
import cr0s.WarpDrive.block.BlockMonitor;
import cr0s.WarpDrive.block.BlockParticleBooster;
import cr0s.WarpDrive.block.BlockProtocol;
import cr0s.WarpDrive.block.BlockRadar;
import cr0s.WarpDrive.block.BlockReactor;
import cr0s.WarpDrive.block.BlockShipScanner;
import cr0s.WarpDrive.block.BlockWarpIsolation;
import cr0s.WarpDrive.client.gui.WDGUI;
import cr0s.WarpDrive.command.GenerateCommand;
import cr0s.WarpDrive.command.InvisibleCommand;
import cr0s.WarpDrive.common.CommonProxy;
import cr0s.WarpDrive.handler.PacketHandler;
import cr0s.WarpDrive.handler.SoundHandler;
import cr0s.WarpDrive.hyperspace.HyperSpaceProvider;
import cr0s.WarpDrive.hyperspace.HyperSpaceWorldGenerator;
import cr0s.WarpDrive.jumpgate.JumpGatesRegistry;
import cr0s.WarpDrive.manager.CloakManager;
import cr0s.WarpDrive.registry.CamRegistry;
import cr0s.WarpDrive.registry.WarpCoresRegistry;
import cr0s.WarpDrive.space.BiomeSpace;
import cr0s.WarpDrive.space.SpaceEventHandler;
import cr0s.WarpDrive.space.SpaceProvider;
import cr0s.WarpDrive.space.SpaceTpCommand;
import cr0s.WarpDrive.space.SpaceWorldGenerator;
import cr0s.WarpDrive.tile.TileEntityAirDistributor;
import cr0s.WarpDrive.tile.TileEntityCamera;
import cr0s.WarpDrive.tile.TileEntityCloakingDeviceCore;
import cr0s.WarpDrive.tile.TileEntityLaser;
import cr0s.WarpDrive.tile.TileEntityLift;
import cr0s.WarpDrive.tile.TileEntityMiningLaser;
import cr0s.WarpDrive.tile.TileEntityMonitor;
import cr0s.WarpDrive.tile.TileEntityParticleBooster;
import cr0s.WarpDrive.tile.TileEntityProtocol;
import cr0s.WarpDrive.tile.TileEntityRadar;
import cr0s.WarpDrive.tile.TileEntityReactor;
import cr0s.WarpDrive.tile.TileEntityShipScanner;
import cr0s.WarpDrive.utils.CameraOverlay;
import cr0s.WarpDrive.utils.CloakChunkWatcher;

import java.util.Arrays;
import java.util.List;

import spaceage.client.gui.SPGUI;
import spaceage.common.SpaceAgeCore;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

@Mod(modid = WarpDrive.modid, name = "WarpDrive - Universal Electricity", version = "1.2.0_ZLO", dependencies = "required-after:UniversalElectricity; required-after:ComputerCraft@[1.58]; required-after:SpaceAge; after:CCTurtle; after:AtomicScience; after:ICBM|Explosion; after:MFFS")
@NetworkMod(clientSideRequired = true, serverSideRequired = true, channels = {
		"WarpDriveBeam", 
		"WarpDriveFreq", 
		"WarpDriveLaserT",
		"WarpDriveCloaks",
		"WarpDrive_Protocol_Distance",
		"WarpDrive_Protocol_Front",
		"WarpDrive_Protocol_Back",
		"WarpDrive_Protocol_Right",
		"WarpDrive_Protocol_Left",
		"WarpDrive_Protocol_Up",
		"WarpDrive_Protocol_Down",
		"WarpDrive_Protocol_Direction",
		"WarpDrive_Protocol_Mode",
		"WarpDrive_ShipName",
		"WarpDrive_CD_Frequency",
		"WarpDrive_CD_Tier",
		"WarpDrive_Radar_Pixel"}, packetHandler = PacketHandler.class)
/**
 * @author Cr0s, SkylordJoel
 */
public class WarpDrive implements LoadingCallback {
	
	public static CreativeTabs tabWD = new CreativeTabs("tabWD") {
		public ItemStack getIconItemstack() {
			return new ItemStack(warpCore);
		}
	};
	
	// World limits
	public final static int WORLD_LIMIT_BLOCKS = 100000;
	
	public static final String modid = "WarpDrive";

	public static Block warpCore;
	public static Block protocolBlock;
	public static Block radarBlock;
	public static Block isolationBlock;
	public static Block airgenBlock;
	public static Block laserBlock;
	public static Block laserCamBlock;
	public static Block cameraBlock;
	public static Block monitorBlock;
	public static Block boosterBlock;
	public static Block miningLaserBlock;
	public static Block liftBlock;
	public static Block scannerBlock;
	public static Block cloakBlock;
	public static Block cloakCoilBlock;
	
	public static Block airBlock;
	public static Block gasBlock;

	//public static Block iridiumBlock;

	public static BiomeGenBase spaceBiome;
	
	public World space;
	private int spaceProviderID;
	public int spaceDimID;
	public SpaceWorldGenerator spaceWorldGenerator;
	
	public HyperSpaceWorldGenerator hyperSpaceWorldGenerator;
	public World hyperSpace;
	private int hyperSpaceProviderID;
	public int hyperSpaceDimID;
	
    public static WDGUI guiHandler = new WDGUI();

	@Instance("WarpDrive")
	public static WarpDrive instance;
	
    @Mod.Metadata(WarpDrive.modid)
    public static ModMetadata metadata;
	
	@SidedProxy(clientSide = "cr0s.WarpDrive.ClientProxy", serverSide = "cr0s.WarpDrive.CommonProxy")
	public static CommonProxy proxy;

	public WarpCoresRegistry registry;
	public JumpGatesRegistry jumpGates;
	
	public CloakManager cloaks;

	public CamRegistry cams;
	public boolean isOverlayEnabled = false;
	public int overlayType = 0;

	@EventHandler
	// @PreInit
	public void preInit(FMLPreInitializationEvent event) {
		WarpDriveConfig.Init(new Configuration(event
				.getSuggestedConfigurationFile()));
		
	       metadata.modId = this.modid;
	       metadata.name = "WarpDrive - Universal Electricity";
	       metadata.description = "Build and pilot your own custom multiblock ship through dangerous space!";
	       metadata.url = "Not released yet";
	       metadata.logoFile = "assets/warpdrive/logo.png";
	       metadata.version = "Alpha";
	       metadata.authorList = Arrays.asList(new String[] { 
	    		   "cr0s", "SkylordJoel"
	    		   });
	       metadata.credits = "SkylordJoel for rewriting the mod to be non-dependant on IC2, and adding GUIs for many devices";
	       metadata.autogenerated = false;

		if (FMLCommonHandler.instance().getSide().isClient()) {
			System.out.println("[WarpDrive] Registering sounds event handler...");
			MinecraftForge.EVENT_BUS.register(new SoundHandler());
		}
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {
		WarpDriveConfig.i.Init2();
		
		OxygenHelper.OXYGEN = new Fluid("oxygen");
		OxygenHelper.OXYGEN.setGaseous(true);
		FluidRegistry.registerFluid(OxygenHelper.OXYGEN);
		OxygenHelper.fluidstack_OXYGEN = new FluidStack(OxygenHelper.OXYGEN, 0);

		// CORE CONTROLLER
		this.protocolBlock = new BlockProtocol(WarpDriveConfig.i.controllerID,
				0, Material.rock).setHardness(0.5F)
				.setStepSound(Block.soundMetalFootstep)
				.setCreativeTab(WarpDrive.tabWD)
				.setUnlocalizedName("warpInterface");
		
		//LanguageRegistry.addName(protocolBlock, "Warp Controller");
		GameRegistry.registerBlock(protocolBlock, "protocolBlock");
		GameRegistry.registerTileEntity(TileEntityProtocol.class,
				"protocolBlock");
		
		// WARP CORE
		this.warpCore = new BlockReactor(WarpDriveConfig.i.coreID, 0, Material.rock).setHardness(0.5F).setStepSound(Block.soundMetalFootstep).setCreativeTab(WarpDrive.tabWD).setUnlocalizedName("warpCore");
		
		//LanguageRegistry.addName(warpCore, "Warp Core");
		GameRegistry.registerBlock(warpCore, "warpCore");
		GameRegistry.registerTileEntity(TileEntityReactor.class, "warpCore");		
		
		// WARP RADAR
		this.radarBlock = new BlockRadar(WarpDriveConfig.i.radarID, 0,
				Material.rock).setHardness(0.5F)
				.setStepSound(Block.soundMetalFootstep)
				.setCreativeTab(WarpDrive.tabWD)
				.setUnlocalizedName("wRadar");
		
		//LanguageRegistry.addName(radarBlock, "W-Radar");
		GameRegistry.registerBlock(radarBlock, "radarBlock");
		GameRegistry.registerTileEntity(TileEntityRadar.class, "radarBlock");
		
		// WARP ISOLATION
		this.isolationBlock = new BlockWarpIsolation(
				WarpDriveConfig.i.isolationID, 0, Material.rock)
		.setHardness(0.5F).setStepSound(Block.soundMetalFootstep)
		.setCreativeTab(WarpDrive.tabWD)
		.setUnlocalizedName("warpIsolation");
		
		//LanguageRegistry.addName(isolationBlock, "Warp-Field Isolation Block");
		GameRegistry.registerBlock(isolationBlock, "isolationBlock");
		
		// AIR GENERATOR
		this.airgenBlock = new BlockAirDistributor(WarpDriveConfig.i.airgenID, 0,
				Material.rock).setHardness(0.5F)
				.setStepSound(Block.soundMetalFootstep)
				.setCreativeTab(WarpDrive.tabWD)
				.setUnlocalizedName("airGen");
		//LanguageRegistry.addName(airgenBlock, "Air Generator");
		GameRegistry.registerBlock(airgenBlock, "airgenBlock");
		GameRegistry.registerTileEntity(TileEntityAirDistributor.class,
				"airgenBlock");
		
		// AIR BLOCK
		this.airBlock = (new BlockAir(WarpDriveConfig.i.airID)).setHardness(
				0.0F).setUnlocalizedName("wdAir");
		//LanguageRegistry.addName(airBlock, "Air block");
		GameRegistry.registerBlock(airBlock, "airBlock");
		
		// GAS BLOCK
		this.gasBlock = (new BlockGas(WarpDriveConfig.i.gasID)).setHardness(
				0.0F).setUnlocalizedName("wdGas");
		//LanguageRegistry.addName(gasBlock, "Gas block");
		GameRegistry.registerBlock(gasBlock, "gasBlock");
		
		// LASER EMITTER
		this.laserBlock = new BlockLaser(WarpDriveConfig.i.laserID, 0,
				Material.rock).setHardness(0.5F)
				.setStepSound(Block.soundMetalFootstep)
				.setCreativeTab(WarpDrive.tabWD)
				.setUnlocalizedName("laserEmitter");
		//LanguageRegistry.addName(laserBlock, "Laser Emitter");
		GameRegistry.registerBlock(laserBlock, "laserBlock");
		GameRegistry.registerTileEntity(TileEntityLaser.class, "laserBlock");
		
		// LASER EMITTER WITH CAMERA
		this.laserCamBlock = new BlockLaserCam(WarpDriveConfig.i.laserCamID, 0,
				Material.rock).setHardness(0.5F)
				.setStepSound(Block.soundMetalFootstep)
				.setCreativeTab(WarpDrive.tabWD)
				.setUnlocalizedName("laserEmitterCam");
		//LanguageRegistry.addName(laserCamBlock, "Laser Emitter + Camera");
		GameRegistry.registerBlock(laserCamBlock, "laserCamBlock");
		
		// CAMERA
		this.cameraBlock = new BlockCamera(WarpDriveConfig.i.camID, 0,
				Material.rock).setHardness(0.5F)
				.setStepSound(Block.soundMetalFootstep)
				.setCreativeTab(WarpDrive.tabWD)
				.setUnlocalizedName("camera");
		//LanguageRegistry.addName(cameraBlock, "Camera");
		GameRegistry.registerBlock(cameraBlock, "cameraBlock");
		GameRegistry.registerTileEntity(TileEntityCamera.class, "cameraBlock");
		
		// MONITOR
		this.monitorBlock = new BlockMonitor(WarpDriveConfig.i.monitorID)
		.setHardness(0.5F).setStepSound(Block.soundMetalFootstep)
		.setCreativeTab(WarpDrive.tabWD)
		.setUnlocalizedName("monitor");
		//LanguageRegistry.addName(monitorBlock, "Monitor");
		GameRegistry.registerBlock(monitorBlock, "monitorBlock");
		GameRegistry
		.registerTileEntity(TileEntityMonitor.class, "monitorBlock");
		
		// MINING LASER
		this.miningLaserBlock = new BlockMiningLaser(
				WarpDriveConfig.i.miningLaserID, 0, Material.rock)
		.setHardness(0.5F).setStepSound(Block.soundMetalFootstep)
		.setCreativeTab(WarpDrive.tabWD)
		.setUnlocalizedName("miningLaser");
		//LanguageRegistry.addName(miningLaserBlock, "Mining Laser");
		GameRegistry.registerBlock(miningLaserBlock, "miningLaserBlock");
		GameRegistry.registerTileEntity(TileEntityMiningLaser.class,
				"miningLaserBlock");
		
		// PARTICLE BOOSTER
		this.boosterBlock = new BlockParticleBooster(
				WarpDriveConfig.i.particleBoosterID, 0, Material.rock)
		.setHardness(0.5F).setStepSound(Block.soundMetalFootstep)
		.setCreativeTab(WarpDrive.tabWD)
		.setUnlocalizedName("particleBooster");
		//LanguageRegistry.addName(boosterBlock, "Particle Booster");
		GameRegistry.registerBlock(boosterBlock, "boosterBlock");
		GameRegistry.registerTileEntity(TileEntityParticleBooster.class,
				"boosterBlock");
		
		// LASER LIFT
		this.liftBlock = new BlockLift(WarpDriveConfig.i.liftID, 0,
				Material.rock).setHardness(0.5F)
				.setStepSound(Block.soundMetalFootstep)
				.setCreativeTab(WarpDrive.tabWD)
				.setUnlocalizedName("laserLift");
		//LanguageRegistry.addName(liftBlock, "Laser lift");
		GameRegistry.registerBlock(liftBlock, "liftBlock");
		GameRegistry.registerTileEntity(TileEntityLift.class, "liftBlock");
		
		/*// IRIDIUM BLOCK
		this.iridiumBlock = new BlockIridium(WarpDriveConfig.i.iridiumID)
		.setHardness(0.8F).setResistance(150 * 4)
		.setStepSound(Block.soundMetalFootstep)
		.setCreativeTab(WarpDrive.tabWD)
		.setUnlocalizedName("Block of Iridium");
		
		LanguageRegistry.addName(iridiumBlock, "Block of Iridium");
		GameRegistry.registerBlock(iridiumBlock, "iridiumBlock");*/
		
        // SHIP SCANNER
        this.scannerBlock = new BlockShipScanner(WarpDriveConfig.i.shipScannerID, 0, Material.rock)
        .setHardness(0.5F).setStepSound(Block.soundMetalFootstep)
        .setCreativeTab(WarpDrive.tabWD).setUnlocalizedName("shipScanner");
        
        //LanguageRegistry.addName(scannerBlock, "Ship Scanner");
        GameRegistry.registerBlock(scannerBlock, "scannerBlock");
        GameRegistry.registerTileEntity(TileEntityShipScanner.class, "scannerBlock");		

        // CLOAKING DEVICE CORE
        this.cloakBlock = new BlockCloakingDeviceCore(WarpDriveConfig.i.cloakCoreID, 0, Material.rock)
        .setHardness(0.5F).setStepSound(Block.soundMetalFootstep)
        .setCreativeTab(WarpDrive.tabWD).setUnlocalizedName("cloakingDeviceCore");
        
        //LanguageRegistry.addName(cloakBlock, "Cloaking Device Core");
        GameRegistry.registerBlock(cloakBlock, "cloakBlock");
        GameRegistry.registerTileEntity(TileEntityCloakingDeviceCore.class, "cloakBlock");        
        
        // CLOAKING DEVICE COIL
		this.cloakCoilBlock = new BlockCloakingCoil(WarpDriveConfig.i.cloakCoilID, 0, Material.rock)
		.setHardness(0.5F)
		.setStepSound(Block.soundMetalFootstep)
		.setCreativeTab(WarpDrive.tabWD)
		.setUnlocalizedName("cloakingCoil");
		
		//LanguageRegistry.addName(cloakCoilBlock, "Cloaking Device Coil");
		GameRegistry.registerBlock(cloakCoilBlock, "cloakCoilBlock");       
		
	    NetworkRegistry.instance().registerGuiHandler(WarpDrive.instance, WarpDrive.guiHandler);
        
		proxy.registerEntities();
		ForgeChunkManager.setForcedChunkLoadingCallback(instance, instance);
		spaceWorldGenerator = new SpaceWorldGenerator();
		GameRegistry.registerWorldGenerator(spaceWorldGenerator);
		hyperSpaceWorldGenerator = new HyperSpaceWorldGenerator();
		GameRegistry.registerWorldGenerator(hyperSpaceWorldGenerator);
		registerSpaceDimension();
		registerHyperSpaceDimension();
		MinecraftForge.EVENT_BUS.register(new SpaceEventHandler());

		if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
			MinecraftForge.EVENT_BUS.register(new CameraOverlay(Minecraft
					.getMinecraft()));
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		space = DimensionManager.getWorld(spaceDimID);
		hyperSpace = DimensionManager.getWorld(hyperSpaceDimID);
		
		/*GameRegistry.addRecipe(new ItemStack(warpCore), "ici", "cmc", "ici", 		//TODO Recipes 
				'i', WarpDriveConfig.i.getIC2Item("iridiumPlate"), 'm',
				WarpDriveConfig.i.getIC2Item("advancedMachine"), 'c',
				WarpDriveConfig.i.getIC2Item("advancedCircuit"));
		
		GameRegistry.addRecipe(new ItemStack(protocolBlock), "iic", "imi",
				"cii", 'i', WarpDriveConfig.i.getIC2Item("iridiumPlate"), 'm',
				WarpDriveConfig.i.getIC2Item("advancedMachine"), 'c',
				WarpDriveConfig.i.getIC2Item("advancedCircuit"));
		
		GameRegistry.addRecipe(new ItemStack(radarBlock), "ifi", "imi", "imi",
				'i', WarpDriveConfig.i.getIC2Item("iridiumPlate"), 'm',
				WarpDriveConfig.i.getIC2Item("advancedMachine"), 'f',
				WarpDriveConfig.i.getIC2Item("frequencyTransmitter"));
		
		GameRegistry.addRecipe(new ItemStack(isolationBlock), "iii", "idi",
				"iii", 'i', WarpDriveConfig.i.getIC2Item("iridiumPlate"), 'm',
				WarpDriveConfig.i.getIC2Item("advancedMachine"), 'd',
				Block.blockDiamond);
		
		GameRegistry.addRecipe(new ItemStack(airgenBlock), "lcl", "lml", "lll",
				'l', Block.leaves, 'm',
				WarpDriveConfig.i.getIC2Item("advancedMachine"), 'c',
				WarpDriveConfig.i.getIC2Item("advancedCircuit"));
		
		GameRegistry.addRecipe(new ItemStack(laserBlock), "sss", "ama", "aaa",
				'm', WarpDriveConfig.i.getIC2Item("advancedMachine"), 'a',
				WarpDriveConfig.i.getIC2Item("advancedAlloy"), 's',
				WarpDriveConfig.i.getIC2Item("advancedCircuit"));
		
		GameRegistry.addRecipe(new ItemStack(miningLaserBlock), "aaa", "ama",
				"ccc", 'c', WarpDriveConfig.i.getIC2Item("advancedCircuit"),
				'a', WarpDriveConfig.i.getIC2Item("advancedAlloy"), 'm',
				WarpDriveConfig.i.getIC2Item("miner"));
		
		GameRegistry.addRecipe(new ItemStack(boosterBlock), "afc", "ama",
				"cfa", 'c', WarpDriveConfig.i.getIC2Item("advancedCircuit"),
				'a', WarpDriveConfig.i.getIC2Item("advancedAlloy"), 'f',
				WarpDriveConfig.i.getIC2Item("glassFiberCableItem"), 'm',
				WarpDriveConfig.i.getIC2Item("mfeUnit"));
		
		GameRegistry.addRecipe(new ItemStack(liftBlock), "aca", "ama", "a#a",
				'c', WarpDriveConfig.i.getIC2Item("advancedCircuit"), 'a',
				WarpDriveConfig.i.getIC2Item("advancedAlloy"), 'm',
				WarpDriveConfig.i.getIC2Item("magnetizer"));

		GameRegistry.addRecipe(new ItemStack(iridiumBlock), "iii", "iii",
				"iii", 'i', WarpDriveConfig.i.getIC2Item("iridiumPlate"));
		
		GameRegistry.addShapelessRecipe(new ItemStack(WarpDriveConfig.i
				.getIC2Item("iridiumPlate").getItem(), 9), new ItemStack(
						iridiumBlock));
		
		GameRegistry.addRecipe(new ItemStack(laserCamBlock), "imi", "cec",
				"#k#", 'i', WarpDriveConfig.i.getIC2Item("iridiumPlate"), 'm',
				WarpDriveConfig.i.getIC2Item("advancedMachine"), 'c',
				WarpDriveConfig.i.getIC2Item("advancedCircuit"), 'e',
				laserBlock, 'k', cameraBlock);
		
		GameRegistry.addRecipe(new ItemStack(cameraBlock), "cgc", "gmg", "cgc",
				'm', WarpDriveConfig.i.getIC2Item("advancedMachine"), 'c',
				WarpDriveConfig.i.getIC2Item("advancedCircuit"), 'g',
				Block.glass);
		
		GameRegistry.addRecipe(new ItemStack(monitorBlock), "gcg", "gmg",
				"ggg", 'm', WarpDriveConfig.i.getIC2Item("advancedMachine"),
				'c', WarpDriveConfig.i.getIC2Item("advancedCircuit"), 'g',
				Block.glass);
		
		GameRegistry.addRecipe(new ItemStack(scannerBlock), "sgs", "mma", "amm",
				'm', WarpDriveConfig.i.getIC2Item("advancedMachine"), 'a',
				WarpDriveConfig.i.getIC2Item("advancedAlloy"), 's',
				WarpDriveConfig.i.getIC2Item("advancedCircuit"), 'g', Block.glass);	
		
		GameRegistry.addRecipe(new ItemStack(cloakBlock), 
				"imi",
				"mcm",
				"imi", 
			'i', iridiumBlock, 'c', cloakCoilBlock, 'm', WarpDriveConfig.i.getIC2Item("advancedMachine"));
		
		GameRegistry.addRecipe(new ItemStack(cloakCoilBlock), 
				"iai",
				"aca",
				"iai", 
			'i', WarpDriveConfig.i.getIC2Item("iridiumPlate"), 'c', WarpDriveConfig.i.getIC2Item("advancedCircuit"), 'a', WarpDriveConfig.i.getIC2Item("advancedAlloy"));		*/
		
		registry = new WarpCoresRegistry();

		if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
			jumpGates = new JumpGatesRegistry();
		} else {
			cams = new CamRegistry();
		}
	}

	private void registerSpaceDimension() {
		spaceBiome = (new BiomeSpace(23)).setColor(0).setDisableRain()
				.setBiomeName("Space");
		this.spaceProviderID = 14;
		DimensionManager.registerProviderType(this.spaceProviderID,
				SpaceProvider.class, true);
		this.spaceDimID = DimensionManager.getNextFreeDimId();
		DimensionManager.registerDimension(this.spaceDimID,
				this.spaceProviderID);
	}

	private void registerHyperSpaceDimension() {
		this.hyperSpaceProviderID = 15;
		DimensionManager.registerProviderType(this.hyperSpaceProviderID,
				HyperSpaceProvider.class, true);
		this.hyperSpaceDimID = DimensionManager.getNextFreeDimId();
		DimensionManager.registerDimension(this.hyperSpaceDimID,
				this.hyperSpaceProviderID);
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		cloaks = new CloakManager();
		MinecraftForge.EVENT_BUS.register(new CloakChunkWatcher());
		
		event.registerServerCommand(new GenerateCommand());
		event.registerServerCommand(new SpaceTpCommand());
		event.registerServerCommand(new InvisibleCommand());
	}

	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world) {
		for (Ticket ticket : tickets) {
			ForgeChunkManager.releaseTicket(ticket);
		}
	}
}