package org.haberno.terraloomed.mixin;

import java.nio.file.Path;
import java.util.List;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.VanillaDataPackProvider;
import net.minecraft.util.path.SymlinkFinder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.google.common.collect.Lists;
import raccoonman.reterraforged.data.packs.RTFBuiltinPackSource;

@Mixin(VanillaDataPackProvider.class)
class MixinServerPacksSource {
    
	@Redirect(
		method = "createPackRepository",
		at = @At(
			value = "NEW",
			target = "Lnet/minecraft/server/packs/repository/PackRepository;"
		),
		require = 1
	)
    private static ResourcePackManager createPackRepository(ResourcePackProvider[] repositorySources, Path path, SymlinkFinder directoryValidator) {
		List<ResourcePackProvider> sourceList = Lists.newArrayList(repositorySources);
		sourceList.add(new RTFBuiltinPackSource(directoryValidator));
    	return new ResourcePackManager(sourceList.toArray(ResourcePackProvider[]::new));
    }
}
