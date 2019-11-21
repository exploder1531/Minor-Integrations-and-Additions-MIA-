package com.github.exploder1531.mia.events;

import com.github.exploder1531.mia.Mia;
import com.github.exploder1531.mia.config.MoCreaturesConfiguration;
import com.pam.harvestcraft.item.ItemRegistry;
import drzhark.mocreatures.entity.MoCEntityAquatic;
import drzhark.mocreatures.entity.ambient.MoCEntityCrab;
import drzhark.mocreatures.entity.aquatic.*;
import drzhark.mocreatures.entity.monster.MoCEntityRat;
import drzhark.mocreatures.entity.monster.MoCEntityWerewolf;
import drzhark.mocreatures.entity.passive.MoCEntityDeer;
import drzhark.mocreatures.entity.passive.MoCEntityDuck;
import drzhark.mocreatures.entity.passive.MoCEntityTurkey;
import drzhark.mocreatures.entity.passive.MoCEntityTurtle;
import drzhark.mocreatures.init.MoCItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

import static com.github.exploder1531.mia.integrations.ModLoadStatus.harvestcraftLoaded;
import static com.github.exploder1531.mia.integrations.ModLoadStatus.moCreaturesLoaded;

@Mod.EventBusSubscriber(modid = Mia.MODID)
public class EntityEvents
{
    @SubscribeEvent
    public static void onEntityHit(LivingHurtEvent event)
    {
        if (moCreaturesLoaded && MoCreaturesConfiguration.buffOtherModSilverWeapons)
        {
            // Make it stronger when Tinker's weapon is holy?
            if (event.getAmount() < 10f && event.getSource() instanceof EntityDamageSource && event.getEntityLiving() instanceof MoCEntityWerewolf && !((MoCEntityWerewolf) event.getEntityLiving()).getIsHumanForm())
            {
                if (event.getSource().damageType.equals("player") && event.getSource().getImmediateSource() instanceof EntityPlayer)
                {
                    EntityPlayer player = ((EntityPlayer) event.getSource().getImmediateSource());
                    
                    ItemStack heldItem = player.getHeldItemMainhand();
                    
                    if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemSword)
                    {
                        ItemSword sword = (ItemSword) heldItem.getItem();
                        
                        if (sword.getToolMaterialName().toLowerCase().contains("silver") || sword.getTranslationKey().toLowerCase().contains("silver"))
                        {
                            event.setAmount(10f);
                        }
                    }
                }
            }
        }
    }
    
    @SuppressWarnings("UnnecessaryReturnStatement")
    @SubscribeEvent
    public static void onEntityDrops(LivingDropsEvent event)
    {
        if (harvestcraftLoaded)
        {
            if (event.getEntityLiving() instanceof EntitySquid)
            {
                dropFewItems(ItemRegistry.calamaricookedItem, ItemRegistry.calamaricookedItem, event);
                return;
            }
            else if (moCreaturesLoaded)
            {
                if (event.getEntityLiving() instanceof MoCEntityAnchovy)
                {
                    replaceItemDrop(event.getDrops(), Items.FISH, ItemRegistry.anchovyrawItem);
                    return;
                }
                else if (event.getEntityLiving() instanceof MoCEntityBass)
                {
                    replaceItemDrop(event.getDrops(), Items.FISH, ItemRegistry.bassrawItem);
                    return;
                }
                // Add new loot
                else if (event.getEntityLiving() instanceof MoCEntityJellyFish)
                {
                    dropFewItems(ItemRegistry.jellyfishrawItem, event);
                    return;
                }
                else if (event.getEntityLiving() instanceof MoCEntityDuck)
                {
                    dropFewItems(ItemRegistry.duckrawItem, event);
                    return;
                }
                else if (event.getEntityLiving() instanceof MoCEntityDeer)
                {
                    dropFewItems(ItemRegistry.venisonrawItem, event);
                    return;
                }
            }
        }
        
        if (moCreaturesLoaded)
        {
            if (MoCreaturesConfiguration.replaceFishDrops)
            {
                if (event.getEntityLiving() instanceof MoCEntityCod)
                {
                    replaceItemDrop(event.getDrops(), Items.FISH, Items.FISH, Items.COOKED_FISH, 1, MoCreaturesConfiguration.addCookedDrops && event.getEntityLiving().isBurning());
                    return;
                }
                else if (event.getEntityLiving() instanceof MoCEntityClownFish)
                {
                    replaceItemDrop(event.getDrops(), Items.FISH, Items.FISH, Items.COOKED_FISH, 2, MoCreaturesConfiguration.addCookedDrops && event.getEntityLiving().isBurning());
                    return;
                }
            }
            
            if (MoCreaturesConfiguration.addCookedDrops && event.getEntityLiving().isBurning())
            {
                if (event.getEntityLiving() instanceof MoCEntityAquatic)
                {
                    replaceItemDrop(event.getDrops(), Items.FISH, Items.COOKED_FISH);
                    return;
                }
                else if (event.getEntityLiving() instanceof MoCEntityDuck)
                {
                    replaceItemDrop(event.getDrops(), MoCItems.crabraw, MoCItems.ostrichcooked);
                    return;
                }
                else if (event.getEntityLiving() instanceof MoCEntityCrab)
                {
                    replaceItemDrop(event.getDrops(), MoCItems.crabraw, MoCItems.crabcooked);
                    return;
                }
                else if (event.getEntityLiving() instanceof MoCEntityRat)
                {
                    replaceItemDrop(event.getDrops(), MoCItems.ratRaw, MoCItems.ratCooked);
                    return;
                }
                else if (event.getEntityLiving() instanceof MoCEntityTurkey)
                {
                    replaceItemDrop(event.getDrops(), MoCItems.rawTurkey, MoCItems.cookedTurkey);
                    return;
                }
                else if (harvestcraftLoaded && event.getEntityLiving() instanceof MoCEntityTurtle)
                {
                    replaceItemDrop(event.getDrops(), MoCItems.turtleraw, ItemRegistry.turtlecookedItem);
                    return;
                }
            }
        }
    }
    
    private static void replaceItemDrop(List<EntityItem> drops, Item itemToReplace, Item targetItem)
    {
        replaceItemDrop(drops, itemToReplace, Items.AIR, targetItem, 0, false);
    }
    
    private static void replaceItemDrop(List<EntityItem> drops, Item itemToReplace, Item targetItem, Item cookedItem, int meta, boolean onFire)
    {
        if (onFire)
            targetItem = cookedItem;
        
        final Item finalTargetItem = targetItem;
        
        drops.forEach((i) ->
        {
            if (i.getItem().getItem() == itemToReplace)
            {
                i.setItem(new ItemStack(finalTargetItem, i.getItem().getCount(), meta));
            }
        });
    }
    
    private static void dropFewItems(Item item, LivingDropsEvent event)
    {
        dropFewItems(item, Items.AIR, false, event.getEntityLiving(), event.getLootingLevel());
    }
    
    private static void dropFewItems(Item item, Item cooked, LivingDropsEvent event)
    {
        dropFewItems(item, cooked, event.getEntityLiving().isBurning(), event.getEntityLiving(), event.getLootingLevel());
    }
    
    private static void dropFewItems(Item item, Item cooked, boolean onFire, EntityLivingBase entity, int lootingLevel)
    {
        int i = entity.world.rand.nextInt(3);
        if (lootingLevel > 0)
        {
            i += entity.world.rand.nextInt(lootingLevel + 1);
        }
        
        if (onFire)
            item = cooked;
        
        for (int j = 0; j < i; ++j)
        {
            entity.dropItem(item, 1);
        }
    }
}