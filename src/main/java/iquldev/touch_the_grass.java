package iquldev;

import net.fabricmc.api.ModInitializer;
import eu.midnightdust.lib.config.MidnightConfig;

public class touch_the_grass implements ModInitializer {

    @Override
    public void onInitialize() {
        MidnightConfig.init("touch_the_grass", touch_the_grassConfig.class);
    }
}
