package org.haberno.terraloomed.worldgen.surface;

import net.minecraft.world.ChunkRegion;

public class SurfaceRegion {
	private static final ThreadLocal<ChunkRegion> THREAD_LOCAL = new ThreadLocal<>();
	
	public static void set(ChunkRegion region) {
		THREAD_LOCAL.set(region);
	}
	
	public static ChunkRegion get() {
		return THREAD_LOCAL.get();
	}
}
