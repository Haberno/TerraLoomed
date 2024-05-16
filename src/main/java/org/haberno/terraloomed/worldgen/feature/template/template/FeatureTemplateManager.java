package org.haberno.terraloomed.worldgen.feature.template.template;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.ImmutableList;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

public class FeatureTemplateManager {
	private MinecraftServer server;
	private ResourceManager resourceManager;
	private Map<Identifier, FeatureTemplate> cache;
	
	public FeatureTemplateManager(MinecraftServer server, ResourceManager resourceManager) {
		this.server = server;
		this.resourceManager = resourceManager;
		this.cache = new ConcurrentHashMap<>();
	}
	
	public void onReload(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
		this.cache.clear();
	}
	
	public FeatureTemplate load(Identifier location) {
		return this.cache.computeIfAbsent(location, this::read);
	}
	
	private FeatureTemplate read(Identifier location) {
		return this.resourceManager.getResource(location).flatMap((resource) -> {
			try(InputStream stream = resource.getInputStream()) {
				return FeatureTemplate.load(this.server.getRegistryManager().getWrapperOrThrow(RegistryKeys.BLOCK).withFeatureFilter(this.server.getSaveProperties().getEnabledFeatures()), stream);
			} catch (IOException e) {
				e.printStackTrace();
				return Optional.empty();
			}
		}).orElse(new FeatureTemplate(ImmutableList.of()));
	}
}
