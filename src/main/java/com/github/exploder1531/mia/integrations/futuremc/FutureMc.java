package com.github.exploder1531.mia.integrations.futuremc;

import com.github.exploder1531.mia.Mia;
import com.github.exploder1531.mia.integrations.ModIds;
import com.github.exploder1531.mia.integrations.base.IBaseMod;
import com.github.exploder1531.mia.integrations.base.IModIntegration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;
import thedarkcolour.futuremc.init.Init;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

import static com.github.exploder1531.mia.integrations.ModIds.*;

public class FutureMc implements IBaseMod
{
    private final List<IFutureMcIntegration> modIntegrations = new LinkedList<>();
    
    @Override
    public void register(BiConsumer<ModIds, IModIntegration> modIntegration)
    {
        if (JER.isLoaded)
            modIntegration.accept(JER, new JerFutureMcIntegration());
        if (QUARK.isLoaded)
            modIntegration.accept(QUARK, new QuarkFutureMcIntegration());
        if (THERMAL_EXPANSION.isLoaded)
            modIntegration.accept(THERMAL_EXPANSION, new ThermalExpansionFutureMcIntegration());
    }
    
    @Override
    public void addIntegration(IModIntegration integration)
    {
        // TODO: check if integrations are enabled
        
        if (integration instanceof IFutureMcIntegration)
            modIntegrations.add((IFutureMcIntegration)integration);
        else
            Mia.LOGGER.warn("Incorrect FutureMC integration with id of " + integration.getModId() + ": " + integration.toString());
    }
    
    @Override
    public void init(FMLInitializationEvent event)
    {
        OreDictionary.registerOre("blockSlime", Init.HONEY_BLOCK);
        OreDictionary.registerOre("honeycomb", Init.HONEY_COMB);
        OreDictionary.registerOre("listAllsugar", Init.HONEY_BOTTLE);
        OreDictionary.registerOre("dropHoney", Init.HONEY_BOTTLE);
        OreDictionary.registerOre("foodHoneydrop", Init.HONEY_BOTTLE);
    }
}
