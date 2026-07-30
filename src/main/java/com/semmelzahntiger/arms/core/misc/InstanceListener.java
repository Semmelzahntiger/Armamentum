package com.semmelzahntiger.arms.core.misc;

import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.CommandEvent;
import net.neoforged.neoforge.event.DifficultyChangeEvent;
import net.neoforged.neoforge.event.GrindstoneEvent;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.ItemStackedOnOtherEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.event.PlayLevelSoundEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.StatAwardEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.VanillaGameEvent;
import net.neoforged.neoforge.event.brewing.PlayerBrewedPotionEvent;
import net.neoforged.neoforge.event.brewing.PotionBrewEvent;
import net.neoforged.neoforge.event.enchanting.EnchantmentLevelSetEvent;
import net.neoforged.neoforge.event.enchanting.GetEnchantmentLevelEvent;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.EntityInvulnerabilityCheckEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.EntityMobGriefingEvent;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.item.ItemExpireEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.neoforged.neoforge.event.level.AlterGroundEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.BlockGrowFeatureEvent;
import net.neoforged.neoforge.event.level.ChunkDataEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.ChunkTicketLevelUpdatedEvent;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.event.level.ExplosionKnockbackEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.level.ModifyCustomSpawnersEvent;
import net.neoforged.neoforge.event.level.NoteBlockEvent;
import net.neoforged.neoforge.event.level.PistonEvent;
import net.neoforged.neoforge.event.level.SleepFinishedTimeEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerLifecycleEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public interface InstanceListener {
    default void onPlayerBrewedPotionEvent(PlayerBrewedPotionEvent event) {}
    default void onPotionBrewEventPre(PotionBrewEvent.Pre event) {}
    default void onPotionBrewEventPost(PotionBrewEvent.Post event) {}
    default void onEnchantmentLevelSetEvent(EnchantmentLevelSetEvent event) {}
    default void onGetEnchantmentLevelEvent(GetEnchantmentLevelEvent event) {}
    default void onEntityEventEnteringSection(EntityEvent.EnteringSection event) {}
    default void onEntityEventSize(EntityEvent.Size event) {}
    default void onEntityConstructing(EntityEvent.EntityConstructing event) {}
    default void onEntityInvulnerabilityCheckEvent(EntityInvulnerabilityCheckEvent event) {}
    default void onEntityJoinLevelEvent(EntityJoinLevelEvent event) {}
    default void onEntityLeaveLevelEvent(EntityLeaveLevelEvent event) {}
    default void onEntityMobGriefingEvent(EntityMobGriefingEvent event) {}
    default void onEntityMountEvent(EntityMountEvent event) {}
    default void onEntityStruckByLightningEvent(EntityStruckByLightningEvent event) {}
    default void onEntityTeleportEvent(EntityTeleportEvent event) {}
    default void onEntityTravelToDimensionEvent(EntityTravelToDimensionEvent event) {}
    default void onProjectileImpactEvent(ProjectileImpactEvent event) {}
    default void onItemExpireEvent(ItemExpireEvent event) {}
    default void onItemTossEvent(ItemTossEvent event) {}
    default void onAnimalTameEvent(AnimalTameEvent event) {}
    default void onArmorHurtEvent(ArmorHurtEvent event) {}
    default void onBabyEntitySpawnEvent(BabyEntitySpawnEvent event) {}
    default void onEffectParticleModificationEvent(EffectParticleModificationEvent event) {}
    default void onEnderManAngerEvent(EnderManAngerEvent event) {}
    default void onFinalizeSpawnEvent(FinalizeSpawnEvent event) {}
    default void onLivingBreatheEvent(LivingBreatheEvent event) {}
    default void onLivingChangeTargetEvent(LivingChangeTargetEvent event) {}
    default void onLivingConversionEventPre(LivingConversionEvent.Pre event) {}
    default void onLivingConversionPostEvent(LivingConversionEvent.Post event) {}
    default void onLivingDamageEventPre(LivingDamageEvent.Pre event) {}
    default void onLivingDamageEventPost(LivingDamageEvent.Post event) {}
    default void onLivingDeathEvent(LivingDeathEvent event) {}
    default void onLivingDestroyBlockEvent(LivingDestroyBlockEvent event) {}
    default void onLivingDropsEvent(LivingDropsEvent event) {}
    default void onLivingDrownEvent(LivingDrownEvent event) {}
    default void onLivingEntityUseItemEvent(LivingEntityUseItemEvent event) {}
    default void onLivingEquipmentChangeEvent(LivingEquipmentChangeEvent event) {}
    default void onLivingEventJumpEvent(LivingEvent.LivingJumpEvent event) {}
    default void onLivingEventVisibilityEvent(LivingEvent.LivingVisibilityEvent event) {}
    default void onLivingExperienceDropEvent(LivingExperienceDropEvent event) {}
    default void onLivingFallEvent(LivingFallEvent event) {}
    default void onLivingGetProjectileEvent(LivingGetProjectileEvent event) {}
    default void onLivingHealEvent(LivingHealEvent event) {}
    default void onLivingIncomingDamageEvent(LivingIncomingDamageEvent event) {}
    default void onLivingKnockBackEvent(LivingKnockBackEvent event) {}
    default void onLivingShieldBlockEvent(LivingShieldBlockEvent event) {}
    default void onLivingSwapItemsEvent(LivingSwapItemsEvent.Hands event) {}
    default void onLivingUseTotemEvent(LivingUseTotemEvent event) {}
    default void onMobDespawnEvent(MobDespawnEvent event) {}
    default void onMobEffectAddedEvent(MobEffectEvent.Added event) {}
    default void onMobEffectApplicableEvent(MobEffectEvent.Applicable event) {}
    default void onMobEffectExpiredEvent(MobEffectEvent.Expired event) {}
    default void onMobEffectRemoveEvent(MobEffectEvent.Remove event) {}
    default void onMobSpawnPositionCheckEvent(MobSpawnEvent.PositionCheck event) {}
    default void onMapSpawnPlacementCheckEvent(MobSpawnEvent.SpawnPlacementCheck event) {}
    default void onMobSplitEvent(MobSplitEvent event) {}
    default void onSpawnClusterSizeEvent(SpawnClusterSizeEvent event) {}
    default void onAlterGroundEvent(AlterGroundEvent event) {}
    default void onBlockDropsEvent(BlockDropsEvent event) {}
    default void onBlockToolModificationEvent(BlockEvent.BlockToolModificationEvent event) {}
    default void onBlockBreakEvent(BlockEvent.BreakEvent event) {}
    default void onEntityMultiPlaceEvent(BlockEvent.EntityMultiPlaceEvent event) {}
    default void onEntityPlaceEvent(BlockEvent.EntityPlaceEvent event) {}
    default void onFarmlandTrampleEvent(BlockEvent.FarmlandTrampleEvent event) {}
    default void onFluidPlaceBlockEvent(BlockEvent.FluidPlaceBlockEvent event) {}
    default void onNeighbourNotifyEvent(BlockEvent.NeighborNotifyEvent event) {}
    default void onPortalSpawnEvent(BlockEvent.PortalSpawnEvent event) {}
    default void onBlockGrowFeatureEvent(BlockGrowFeatureEvent event) {}
    default void onChunkDataSaveEvent(ChunkDataEvent.Save event) {}
    default void onChunkDataLoadEvent(ChunkDataEvent.Load event) {}
    default void onChunkLoadEvent(ChunkEvent.Load event) {}
    default void onChunkUnloadEvent(ChunkEvent.Unload event) {}
    default void onChunkTicketLevelUpdateEvent(ChunkTicketLevelUpdatedEvent event) {}
    default void onChunkWatchEvent(ChunkWatchEvent chunkWatchEvent) {}
    default void onExplosionStartEvent(ExplosionEvent.Start event) {}
    default void onExplosionDetonateEvent(ExplosionEvent.Detonate event) {}
    default void onExplosionKnockbackEvent(ExplosionKnockbackEvent event) {}
    default void onCreateSpawnPositionEvent(LevelEvent.CreateSpawnPosition event) {}
    default void onLevelLoadEvent(LevelEvent.Load event) {}
    default void onLevelUnloadEvent(LevelEvent.Unload event) {}
    default void onPotentialSpawnsEvent(LevelEvent.PotentialSpawns event) {}
    default void onLevelSaveEvent(LevelEvent.Save event) {}
    default void onModifyCustomSpawnersEvent(ModifyCustomSpawnersEvent event) {}
    default void onNoteBlockEvent(NoteBlockEvent.Change event) {}
    default void onNoteBlockPlayEvent(NoteBlockEvent.Play event) {}
    default void onPistonPreEvent(PistonEvent.Pre event) {}
    default void onPistonPostEvent(PistonEvent.Post event) {}
    default void onSleepFinishedTimeEvent(SleepFinishedTimeEvent event) {}
    default void onServerAboutToStartEvent(ServerAboutToStartEvent event) {}
    default void onServerLifecycleEvent(ServerStartedEvent event) {}
    default void onServerStartingEvent(ServerStartingEvent event) {}
    default void onServerStoppingEvent(ServerStoppingEvent event) {}
    default void onServerStoppedEvent(ServerStoppedEvent event) {}
    default void onLevelTickPreEvent(LevelTickEvent.Pre event) {}
    default void onLevelTickPostEvent(LevelTickEvent.Post event) {}
    default void onPlayerTickPreEvent(PlayerTickEvent.Pre event) {}
    default void onPlayerTickPostEvent(PlayerTickEvent.Post event) {}
    default void onServerTickPreEvent(ServerTickEvent.Pre event) {}
    default void onServerTickPostEvent(ServerTickEvent.Post event) {}
    default void onAnvilUpdateEvent(AnvilUpdateEvent event) {}
    default void onCommandEvent(CommandEvent event) {}
    default void onDifficultyChangeEvent(DifficultyChangeEvent event) {}
    default void onGrindstonePlaceItemEvent(GrindstoneEvent.OnPlaceItem event) {}
    default void onGrindstoneTakeItemEvent(GrindstoneEvent.OnTakeItem event) {}
    default void onItemAttributeModifierEvent(ItemAttributeModifierEvent event) {}
    default void onItemStackedOnOtherEvent(ItemStackedOnOtherEvent event) {}
    default void onLootTableLoadEvent(LootTableLoadEvent event) {}
    default void onModifyDefaultComponentsEvent(ModifyDefaultComponentsEvent event) {}
    default void onPlayLevelSoundEvent(PlayLevelSoundEvent event) {}
    default void onRegisterCommandsEvent(RegisterCommandsEvent event) {}
    default void onServerChatEvent(ServerChatEvent event) {}
    default void onStatAwardEvent(StatAwardEvent event) {}
    default void onTagsUpdatedEvent(TagsUpdatedEvent event) {}
    default void onVanillaGameEvent(VanillaGameEvent event) {}
    default void onFurnaceFuelBurnItemEvent(FurnaceFuelBurnTimeEvent event) {}
}
