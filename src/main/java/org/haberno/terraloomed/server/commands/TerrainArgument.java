package org.haberno.terraloomed.server.commands;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import raccoonman.reterraforged.client.data.RTFTranslationKeys;
import raccoonman.reterraforged.world.worldgen.terrain.Terrain;
import raccoonman.reterraforged.world.worldgen.terrain.TerrainType;

public class TerrainArgument implements ArgumentType<Terrain> {
    public static final DynamicCommandExceptionType ERROR_INVALID_VALUE = new DynamicCommandExceptionType(input -> Text.translatable(RTFTranslationKeys.TERRAIN_ARGUMENT_INVALID, input));

    private TerrainArgument() {    	
    }
    
    public static TerrainArgument terrain() {
    	return new TerrainArgument();
    }
    
	@Override
	public Terrain parse(StringReader reader) throws CommandSyntaxException {
        String terrainName = reader.readUnquotedString();
        Terrain terrain = TerrainType.get(terrainName);
        if (terrain == null) {
            throw ERROR_INVALID_VALUE.create(terrainName);
        }
        return terrain;
	}

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContext, SuggestionsBuilder suggestionsBuilder) {
        return CommandSource.suggestMatching(getTerrainTypeNames(), suggestionsBuilder);
    }

    @Override
    public Collection<String> getExamples() {
        return getTerrainTypeNames().toList();
    }
    
    private static final List<Terrain> BLACKLIST = ImmutableList.of(TerrainType.NONE, TerrainType.VOLCANO_PIPE);
    
    private static Stream<String> getTerrainTypeNames() {
    	return TerrainType.REGISTRY.stream().filter((type) -> !BLACKLIST.contains(type)).map(Terrain::getName);
    }
    
    public static class Info implements ArgumentSerializer<TerrainArgument, Info.Template> {

		@Override
		public void serializeToNetwork(Template template, PacketByteBuf byteBuf) {
		}

		@Override
		public Template fromPacket(PacketByteBuf byteBuf) {
			return new Template();
		}

		@Override
		public void serializeToJson(Template template, JsonObject json) {
		}

		@Override
		public Template unpack(TerrainArgument argument) {
			return new Template();
		}

        public class Template implements ArgumentTypeProperties<TerrainArgument> {

        	@Override
            public TerrainArgument createType(CommandRegistryAccess commandBuildContext) {
                return TerrainArgument.terrain();
            }

            @Override
            public ArgumentSerializer<TerrainArgument, ?> getSerializer() {
                return Info.this;
            }
        }
    }
}
