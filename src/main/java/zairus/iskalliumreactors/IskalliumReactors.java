package zairus.iskalliumreactors;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zairus.iskalliumreactors.block.IRBlocks;
import zairus.iskalliumreactors.handlres.IRCraftingHandler;
import zairus.iskalliumreactors.handlres.IRGuiHandler;
import zairus.iskalliumreactors.networking.MessageTile;
import zairus.iskalliumreactors.proxy.CommonProxy;
import zairus.iskalliumreactors.world.gen.feature.WorldGenIskalliumOre;

@Mod(modid = IRConstants.MOD_ID, name = IRConstants.MOD_NAME, version = IRConstants.MOD_VERSION)
public class IskalliumReactors
{
	@Mod.Instance(IRConstants.MOD_ID)
	public static IskalliumReactors instance;
	
	@SidedProxy(clientSide = IRConstants.CLIENT_PROXY, serverSide = IRConstants.COMMON_PROXY)
	public static CommonProxy proxy;
	
	public static Logger logger;
	public static SimpleNetworkWrapper networkWrapper;
	public static final IRGuiHandler guiHandler = new IRGuiHandler();

	public static CreativeTabs creativeTab = new CreativeTabs("iskalliumReactors") {
		@Override
		public ItemStack getTabIconItem()
		{
			return new ItemStack(IRBlocks.ISKALLIUM);
		}
	};
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger = event.getModLog();
		IRConfig.init(event.getSuggestedConfigurationFile());

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, guiHandler);

        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(IRConstants.MOD_ID);
		networkWrapper.registerMessage(MessageTile.MessageTileHandler.class, MessageTile.class, 0, Side.CLIENT);

		IskalliumReactors.proxy.preInit(event);
	}
	
	@Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
		IskalliumReactors.proxy.init(event);
		IskalliumReactors.proxy.initBuiltinShapes();
		
		GameRegistry.registerWorldGenerator(new WorldGenIskalliumOre(), IRConfig.iskalliumGenerationWeight);
		
		IRCraftingHandler.addRecipes();
    }
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		IskalliumReactors.proxy.postInit(event);
	}
}
