package org.haberno.terraloomed.data.packs;

import net.minecraft.resource.*;
import net.minecraft.resource.ResourcePackProfile.PackFactory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.path.SymlinkFinder;
import org.haberno.terraloomed.RTFCommon;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.util.Collections;

public class RTFBuiltinPackSource extends VanillaResourcePackProvider {
	private static final Identifier PACKS_DIR = RTFCommon.location("datapacks");
	
	public RTFBuiltinPackSource(SymlinkFinder directoryValidator) {
		super(ResourceType.SERVER_DATA, createRTFPackSource(), PACKS_DIR, directoryValidator);
	}

	@Nullable
	@Override
	protected ResourcePackProfile createDefault(ResourcePack packResources) {
		return null;
	}

	@Override
	protected Text getProfileName(String title) {
		return Text.literal(title);
	}

	@Override
	protected ResourcePackProfile create(String title, PackFactory resourceSupplier, Text description) {
        return ResourcePackProfile.create(title, description, false, resourceSupplier, ResourceType.SERVER_DATA, ResourcePackProfile.InsertionPosition.TOP, ResourcePackSource.FEATURE);
	}

	private static DefaultResourcePack createRTFPackSource() {
		DefaultResourcePackBuilder builder = new DefaultResourcePackBuilder().withNamespaces(RTFCommon.MOD_ID);
		ResourceType packType = ResourceType.SERVER_DATA;
		String root = "/" + packType.getDirectory() + "/";
		URL uRL = RTFCommon.class.getResource(root);
		if (uRL == null) {
			RTFCommon.LOGGER.error("File {} does not exist in classpath", root);
		} else {
			try {
				URI uRI = uRL.toURI();
				String uriSchema = uRI.getScheme();
				if (!"jar".equals(uriSchema) && !"file".equals(uriSchema)) {
					RTFCommon.LOGGER.warn("Assets URL '{}' uses unexpected schema", uRI);
				}
				Path path = safeGetPath(uRI);
				builder.withPath(packType, path);
			} catch (Exception exception) {
				RTFCommon.LOGGER.error("Couldn't resolve path to assets", exception);
			}	
		}
        return builder.runCallback().build();
	}

    private static Path safeGetPath(URI uRI) throws IOException {
        try {
            return Paths.get(uRI);
        } catch (FileSystemNotFoundException fileSystemNotFoundException) {
        } catch (Throwable throwable) {
        	RTFCommon.LOGGER.warn("Unable to get path for: {}", uRI, throwable);
        }
        try {
            FileSystems.newFileSystem(uRI, Collections.emptyMap());
        } catch (FileSystemAlreadyExistsException fileSystemAlreadyExistsException) {
            // empty catch block
        }
        return Paths.get(uRI);
    }
}
