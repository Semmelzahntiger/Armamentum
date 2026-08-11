package com.semmelzahntiger.arms.common.blocks.mountablebases;

public class MediumBase extends AbstractMultiBase {
    public MediumBase(Properties properties) {
        super(properties);
    }

    @Override
    protected int getPlatformSize() {
        return 2;
    }
}
