package com.github.exploder1531.mia.config;

import com.github.exploder1531.mia.Mia;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static net.minecraftforge.common.config.Config.*;

@Config(modid = Mia.MODID, name = "mia/base")
@LangKey("mia.config.base.title")
@Mod.EventBusSubscriber(modid = Mia.MODID)
public class MiaConfig
{
    @Name("Enable music player")
    @Comment("Set to false to completely disable the music player item")
    @LangKey("mia.config.base.music_player")
    @RequiresMcRestart
    public static boolean musicPlayerEnabled = true;
    
    @Name("Music player volume")
    @Comment("Volume of the songs played by music player")
    @LangKey("mia.config.base.music_player_volume")
    @RangeInt(min = 0, max = 100)
    @SlidingOption
    @SideOnly(Side.CLIENT)
    public static int musicPlayerVolume = 30;
    
    @Name("Replaces all raw meat drops with cooked ones")
    @Comment({ "Replaces raw meat dropped by mobs on fire with their cooked version (if possible)",
            "This is done to match modded mobs with vanilla behavior, as not all modded mobs do this" })
    @LangKey("mia.config.base.add_cooked_drops")
    public static boolean addCookedDrops = true;
    
    
    /**
     * Inject the new values and save to the config file when the config has been changed from the GUI.
     *
     * @param event The event
     */
    @SubscribeEvent
    public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(Mia.MODID))
        {
            ConfigManager.sync(Mia.MODID, Type.INSTANCE);
        }
    }
}
