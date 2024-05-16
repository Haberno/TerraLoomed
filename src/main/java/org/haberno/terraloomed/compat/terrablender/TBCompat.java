package org.haberno.terraloomed.compat.terrablender;


import org.haberno.terraloomed.platform.ModLoaderUtil;
import terrablender.core.TerraBlender;

public class TBCompat {

	public static void bootstrap() {
		TBSurfaceRules.bootstrap();
	}
	
	public static boolean isEnabled() {
		return ModLoaderUtil.isLoaded(TerraBlender.MOD_ID);
	}
}
