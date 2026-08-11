package com.semmelzahntiger.arms.common.blocks.mountablebases;

public class LargeBase extends AbstractMultiBase {

    public LargeBase(Properties properties) {
        super(properties);
    }

    @Override
    protected int getPlatformSize() {
        return 3;
    }
}
