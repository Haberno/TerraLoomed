package org.haberno.terraloomed.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class ModLoaderUtil {
	
	@ExpectPlatform
	public static boolean isLoaded(String modId) {
		throw new IllegalStateException();
	}
}
