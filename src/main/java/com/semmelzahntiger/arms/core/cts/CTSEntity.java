package com.semmelzahntiger.arms.core.cts;

import net.minecraft.data.DataProvider;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;


public interface CTSEntity<T extends Entity, D extends CTSData<T>> {
    @NotNull Supplier<D> getDataInstance();
    @NotNull T getEntity();
}
