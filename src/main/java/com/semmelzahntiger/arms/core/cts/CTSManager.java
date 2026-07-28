package com.semmelzahntiger.arms.core.cts;

import io.netty.util.internal.SuppressJava6Requirement;
import net.minecraft.world.entity.Entity;

import java.util.IdentityHashMap;

/**
 * Fuck ass system whose purpose is to sync server side values with the next client tick.
 * Useful for Interpolation Values.
 */
public class CTSManager {

    public static final IdentityHashMap<CTSEntity<?,?>, CTSData<?>> CLIENT_SYNCED_ENTITIES = new IdentityHashMap<>();

    public static void clientTickUpdate() {
        CLIENT_SYNCED_ENTITIES.values().forEach(CTSData::updateData);
    }

    public static <E extends Entity, D extends CTSData<E>> void registerEntity(CTSEntity<E,D> entity) {
        CLIENT_SYNCED_ENTITIES.put(entity, entity.getDataInstance().get());
    }
    @SuppressWarnings("unchecked")
    public static <E extends Entity & CTSEntity<E,D>,D extends CTSData<E>> D getData(CTSEntity<?,?> entity, Class<D> dataClass) {
        return (D) CLIENT_SYNCED_ENTITIES.get(entity);
    }

    public static void removeEntity(CTSEntity<?,?> entity) {
        CLIENT_SYNCED_ENTITIES.remove(entity);
    }

}
