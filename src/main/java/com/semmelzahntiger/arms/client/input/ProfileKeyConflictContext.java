package com.semmelzahntiger.arms.client.input;

import net.neoforged.neoforge.client.settings.IKeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

public class ProfileKeyConflictContext implements IKeyConflictContext {
    private final InputPriorityProfile profile;

    public ProfileKeyConflictContext(InputPriorityProfile profile) {
        this.profile = profile;
    }

    public InputPriorityProfile getProfile() {
        return profile;
    }

    @Override
    public boolean isActive() {
        return profile.isActive() && KeyConflictContext.IN_GAME.isActive();
    }

    @Override
    public boolean conflicts(IKeyConflictContext other) {
        return other == this;
    }
}
