package org.haberno.terraloomed.server.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.haberno.terraloomed.client.data.RTFTranslationKeys;
import org.haberno.terraloomed.worldgen.GeneratorContext;
import org.haberno.terraloomed.worldgen.RTFRandomState;
import org.haberno.terraloomed.worldgen.cell.Cell;
import org.haberno.terraloomed.worldgen.heightmap.WorldLookup;
import org.haberno.terraloomed.worldgen.terrain.Terrain;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

public class LocateTerrainCommand {
    private static final DynamicCommandExceptionType ERROR_TERRAIN_NOT_FOUND = new DynamicCommandExceptionType(terrain -> Text.translatable(RTFTranslationKeys.TERRAIN_NOT_FOUND, terrain));

    public static void register(CommandDispatcher<ServerCommandSource> commandDispatcher, CommandRegistryAccess commandBuildContext) {
    	commandDispatcher.register(
    		CommandManager.literal("rtf").requires((stack) -> stack.hasPermissionLevel(2)).then(
    			CommandManager.literal("locate").then(
    				CommandManager.argument("terrain", TerrainArgument.terrain()).executes((ctx) -> {
	    				ServerCommandSource stack = ctx.getSource();
	    				Terrain terrain = ctx.getArgument("terrain", Terrain.class);
	    				String terrainName = terrain.getName();
	    				BlockPos origin = BlockPos.ofFloored(stack.getPosition());
	    				@Nullable
	    				BlockPos result = locate(stack, terrain, 256, 256, 24000, 30L);
	    				if(result != null) {
	    			        int distance = MathHelper.floor(dist(origin.getX(), origin.getZ(), result.getX(), result.getZ()));
		    			    stack.sendFeedback(() -> Text.translatable(RTFTranslationKeys.TERRAIN_FOUND, terrainName, createTeleportMessage(result), distance), false);
		    			    return Command.SINGLE_SUCCESS;
	    				}
	    	            throw ERROR_TERRAIN_NOT_FOUND.create(terrainName);
    				})
    			)
	    	)
    	);
    }
    
    private static Text createTeleportMessage(BlockPos pos) {
        return Texts.bracketed(Text.translatable("chat.coordinates", pos.getX(), pos.getY(), pos.getZ())).styled(s -> s
        	.withColor(Formatting.GREEN)
        	.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp @s " + pos.getX() + " " + pos.getY() + " " + pos.getZ()))
        	.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("chat.coordinates.tooltip")))
        );
    }

    @Nullable
    private static BlockPos locate(ServerCommandSource commandSourceStack, Terrain target, int step, int minRadius, int maxRadius, long timeout) {
    	ServerWorld level = commandSourceStack.getWorld();

    	@Nullable
        GeneratorContext generatorContext;
    	if((Object) level.getChunkManager().getNoiseConfig() instanceof RTFRandomState rtfRandomState && (generatorContext = rtfRandomState.generatorContext()) != null) {
    		return search(commandSourceStack, generatorContext.lookup, target, step, minRadius, maxRadius, timeout);
    	}
    	
    	return null;
    }

    @Nullable
    private static BlockPos search(ServerCommandSource commandSourceStack, WorldLookup lookup, Terrain target, int step, int minRadius, int maxRadius, long timeout) {
        int radius = maxRadius;
        double minRadiusSq = minRadius * minRadius;
        Vec3d origin = commandSourceStack.getPosition();
        int x = 0;
        int z = 0;
        int dx = 0;
        int dz = -1;
        int size = radius + 1 + radius;
        long max = (long) size * (long) size;
        long timeOut = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(timeout);
        BlockPos.Mutable pos = new BlockPos.Mutable();

        Cell cell = new Cell();
        
        for (long i = 0; i < max; i++) {
            if (System.currentTimeMillis() > timeOut) {
                break;
            }

            if ((-radius <= x) && (x <= radius) && (-radius <= z) && (z <= radius)) {
                pos.set(origin.getX() + (x * step), origin.getY(), origin.getZ() + (z * step));
                if (minRadiusSq == 0 || origin.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ()) >= minRadiusSq) {
                	if(test(lookup, cell, pos, target)) {
                		return pos;
                	}
                }
            }

            if ((x == z) || ((x < 0) && (x == -z)) || ((x > 0) && (x == 1 - z))) {
                size = dx;
                dx = -dz;
                dz = size;
            }

            x += dx;
            z += dz;
        }

        return null;
    }
    
    @Nullable
    private static boolean test(WorldLookup lookup, Cell cell, BlockPos pos, Terrain target) {
        lookup.apply(cell, pos.getX(), pos.getZ());
        return cell.terrain.equals(target);
    }

    private static float dist(int x0, int z0, int x1, int z1) {
        int deltaX = x1 - x0;
        int deltaZ = z1 - z0;
        return MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ);
    }
}

