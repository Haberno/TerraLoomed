package org.haberno.terraloomed.mixin;

import com.google.common.collect.Lists;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.VanillaDataPackProvider;
import net.minecraft.util.path.SymlinkFinder;
import org.haberno.terraloomed.data.packs.RTFBuiltinPackSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.nio.file.Path;
import java.util.List;

@Mixin(VanillaDataPackProvider.class)
class MixinServerPacksSource {
    
	@Redirect(
		method = "createManager",
		at = @At(
			value = "NEW",
			target = "net/minecraft/resource/ResourcePackManager"
		),
		require = 1
	)
    private static ResourcePackManager createManager(ResourcePackProvider[] repositorySources, Path path, SymlinkFinder directoryValidator) {
		List<ResourcePackProvider> sourceList = Lists.newArrayList(repositorySources);
		sourceList.add(new RTFBuiltinPackSource(directoryValidator));
    	return new ResourcePackManager(sourceList.toArray(ResourcePackProvider[]::new));
    }
}
