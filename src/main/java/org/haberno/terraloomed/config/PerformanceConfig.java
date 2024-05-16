package org.haberno.terraloomed.config;

import com.mojang.serialization.DataResult;
import org.haberno.terraloomed.concurrent.ThreadPools;
import org.haberno.terraloomed.platform.ConfigUtil;

import java.nio.file.Path;

public record PerformanceConfig(int tileSize, int batchCount, int threadCount) {
	public static final Path DEFAULT_FILE_PATH = ConfigUtil.rtf("performance_internal.conf");
	
    public static final int MAX_TILE_SIZE = 8;
    public static final int MAX_BATCH_COUNT = 20;
    public static final int MAX_THREAD_COUNT = Runtime.getRuntime().availableProcessors() * 2;

    //TODO
    public static DataResult<PerformanceConfig> read(Path path) {
    	return DataResult.success(makeDefault());
    }
    
    public static PerformanceConfig makeDefault() {
    	int tileSize = 3;
    	int batchCount = 6;
    	int threadCount = ThreadPools.availableProcessors();
    	return new PerformanceConfig(tileSize, batchCount, threadCount);
    }
}
