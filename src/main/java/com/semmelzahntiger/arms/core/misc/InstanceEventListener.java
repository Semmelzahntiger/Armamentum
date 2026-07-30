package com.semmelzahntiger.arms.core.misc;

import net.neoforged.bus.api.Event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a type as an {@link InstanceListener} that wants the listed events dispatched to it.
 *
 * <p>Every event listed here across the whole mod gets a handler generated into
 * {@code InstanceEventDistributor}. Listing an event only opts it <em>into existence</em>: once any
 * type declares it, every registered listener that overrides the matching {@link InstanceListener}
 * method receives it. The inverse is the one to watch for — overriding an {@link InstanceListener}
 * method for an event that nobody declares here is a silent no-op.
 *
 * <p>Retention is deliberately {@link RetentionPolicy#CLASS}: {@code SOURCE} would break the
 * annotation processor's contract with Gradle's incremental compilation, which needs to see the
 * annotation in the compiled class files.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface InstanceEventListener {
    Class<? extends Event>[] events();
}
