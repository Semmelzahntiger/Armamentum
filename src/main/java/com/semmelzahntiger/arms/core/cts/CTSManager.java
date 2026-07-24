package com.semmelzahntiger.arms.core.cts;

import net.minecraft.world.entity.Entity;

import java.util.IdentityHashMap;

public class CTSManager {

    public static final IdentityHashMap<CTSEntity<?,?>, CTSData<?>> CLIENT_SYNCED_ENTITIES = new IdentityHashMap<>();

    public static void clientTickUpdate() {
        CLIENT_SYNCED_ENTITIES.values().forEach(CTSData::updateData);
    }

    public static <E extends Entity, D extends CTSData<E>> void registerEntity(CTSEntity<E,D> entity) {
        CLIENT_SYNCED_ENTITIES.put(entity, entity.getDataInstance().get());
    }

    public static void removeEntity(CTSEntity<?,?> entity) {
        CLIENT_SYNCED_ENTITIES.remove(entity);
    }

}
