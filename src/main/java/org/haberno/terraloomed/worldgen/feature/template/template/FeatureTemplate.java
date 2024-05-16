/*
 * MIT License
 *
 * Copyright (c) 2021 TerraForged
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.haberno.terraloomed.worldgen.feature.template.template;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.*;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.Chunk;
import org.haberno.terraloomed.worldgen.feature.template.StructureUtils;
import org.haberno.terraloomed.worldgen.feature.template.buffer.BufferIterator;
import org.haberno.terraloomed.worldgen.feature.template.buffer.PasteBuffer;
import org.haberno.terraloomed.worldgen.feature.template.buffer.TemplateBuffer;
import org.haberno.terraloomed.worldgen.feature.template.paste.Paste;
import org.haberno.terraloomed.worldgen.feature.template.paste.PasteConfig;
import org.haberno.terraloomed.worldgen.feature.template.paste.PasteType;
import org.haberno.terraloomed.worldgen.feature.template.placement.TemplatePlacement;
import org.haberno.terraloomed.worldgen.feature.util.BlockReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FeatureTemplate {
    public static final PasteType WORLD_GEN = FeatureTemplate::getWorldGenPaste;
    public static final PasteType CHECKED = FeatureTemplate::getCheckedPaste;
    public static final PasteType UNCHECKED = FeatureTemplate::getUnCheckedPaste;

    private static final int PASTE_FLAG = 3 | 16;
    private static final Direction[] DIRECTIONS = Direction.values();
    private static final ThreadLocal<PasteBuffer> PASTE_BUFFER = ThreadLocal.withInitial(PasteBuffer::new);
    private static final ThreadLocal<TemplateBuffer> TEMPLATE_BUFFER = ThreadLocal.withInitial(TemplateBuffer::new);
    private static final ThreadLocal<TemplateRegion> TEMPLATE_REGION = ThreadLocal.withInitial(TemplateRegion::new);

    private final BakedTemplate template;
    private final BakedDimensions dimensions;

    public FeatureTemplate(List<BlockInfo> blocks) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;
        BlockInfo[] blockArray = blocks.toArray(new BlockInfo[0]);

        for (int i = 0; i < blocks.size(); i++) {
            BlockInfo block = blocks.get(i);
            minX = Math.min(minX, block.pos().getX());
            minY = Math.min(minY, block.pos().getY());
            minZ = Math.min(minZ, block.pos().getZ());
            maxX = Math.max(maxX, block.pos().getX());
            maxY = Math.max(maxY, block.pos().getY());
            maxZ = Math.max(maxZ, block.pos().getZ());
            blockArray[i] = block;
        }

        Dimensions dimensions = new Dimensions(new BlockPos(minX, minY, minZ), new BlockPos(maxX, maxY, maxZ));
        this.template = new BakedTemplate(blockArray);
        this.dimensions = new BakedDimensions(dimensions);
    }

    private static <T extends TemplateContext> BlockSetter setter(WorldAccess world, T ctx) {
    	return (pos, state, flags) -> {
        	world.setBlockState(pos, state, flags);
        	ctx.recordState(pos, state);
        };
    }
    
    public <T extends TemplateContext> boolean pasteWorldGen(WorldAccess world, T ctx, BlockPos origin, BlockMirror mirror, BlockRotation rotation, TemplatePlacement<T> placement, PasteConfig config) {
        if (config.checkBounds()) {
            Chunk chunk = world.getChunk(origin);
            if (StructureUtils.hasOvergroundStructure(world.createCommandRegistryWrapper(RegistryKeys.STRUCTURE), chunk)) {
                return this.pasteChecked(world, ctx, origin, mirror, rotation, placement, config);
            }
        }
        return this.pasteUnChecked(world, ctx, origin, mirror, rotation, placement, config);
    }

    private <T extends TemplateContext> boolean pasteUnChecked(WorldAccess world, T ctx, BlockPos origin, BlockMirror mirror, BlockRotation rotation, TemplatePlacement<T> placement, PasteConfig config) {
        boolean placed = false;
        BlockReader reader = new BlockReader();
        PasteBuffer buffer = PASTE_BUFFER.get();
        TemplateRegion region = TEMPLATE_REGION.get().init(origin);
        buffer.setRecording(config.updatePostPaste());

        BlockPos.Mutable pos1 = new BlockPos.Mutable();
        BlockPos.Mutable pos2 = new BlockPos.Mutable();

        BlockSetter setter = setter(world, ctx);
        
        BlockInfo[] blocks = this.template.get(mirror, rotation);
        for (int i = 0; i < blocks.length; i++) {
            BlockInfo block = blocks[i];
            addPos(pos1, origin, block.pos());

            // make sure we don't leak outside the region
            if (!region.containsBlock(world, pos1)) {
                continue;
            }

            // ignore air in the template
            if (!config.pasteAir() && block.state().getBlock() == Blocks.AIR) {
                continue;
            }

            // don't replace existing solids
            if (!config.replaceSolid() && !placement.canReplaceAt(world, pos1)) {
                continue;
            }

            // generate a base going downwards if necessary
            if (block.pos().getY() <= 0 && block.state().isFullCube(reader.setState(block.state()), BlockPos.ORIGIN)) {
            	this.placeBase(world, setter, pos1, pos2, block.state(), config.baseDepth());
            }

            setter.setBlock(pos1, block.state(), 2);
            buffer.record(i);

            placed = true;
        }

        if (config.updatePostPaste()) {
            // once all blocks placed, iterate them and update neighbours if required
            buffer.reset();
            updatePostPlacement(world, setter, buffer, blocks, origin, pos1, pos2);
        }
        return placed;
    }

    private <T extends TemplateContext> boolean pasteChecked(WorldAccess world, T ctx, BlockPos origin, BlockMirror mirror, BlockRotation rotation, TemplatePlacement<T> placement, PasteConfig config) {
        Dimensions dimensions = this.dimensions.get(mirror, rotation);
        TemplateRegion region = TEMPLATE_REGION.get().init(origin);
        TemplateBuffer buffer = TEMPLATE_BUFFER.get().init(world, origin, dimensions.min(), dimensions.max());

        BlockPos.Mutable pos1 = new BlockPos.Mutable();
        BlockPos.Mutable pos2 = new BlockPos.Mutable();
        BlockInfo[] blocks = template.get(mirror, rotation);

        BlockSetter setter = setter(world, ctx);
        
        // record valid BlockInfos into the buffer
        for (int i = 0; i < blocks.length; i++) {
            BlockInfo block = blocks[i];
            addPos(pos1, origin, block.pos());

            // make sure we don't leak outside the region
            if (!region.containsBlock(world, pos1)) {
                continue;
            }

            buffer.record(i, block, pos1, placement, config);
        }

        boolean placed = false;
        BlockReader reader = new BlockReader();
        while (buffer.next()) {
            int i = buffer.nextIndex();
            BlockInfo block = blocks[i];
            addPos(pos1, origin, block.pos());

            if (pos1.getY() <= origin.getY() && block.state().isFullCube(reader.setState(block.state()), BlockPos.ORIGIN)) {
            	this.placeBase(world, setter, pos1, pos2, block.state(), config.baseDepth());
                setter.setBlock(pos1, block.state(), 2);
                placed = true;
            } else if (buffer.test(pos1)) {
                // test uses the placement mask to prevent blocks overriding existing
                // solid blocks
                placed = true;
                setter.setBlock(pos1, block.state(), 2);
            } else {
                // if failed to place mark the pos as invalid
                buffer.exclude(i);
            }
        }

        if (config.updatePostPaste()) {
            // once all blocks placed, iterate them and update neighbours if required
            buffer.reset();
            updatePostPlacement(world, setter, buffer, blocks, origin, pos1, pos2);
        }

        return placed;
    }

    public Dimensions getDimensions(BlockMirror mirror, BlockRotation rotation) {
        return dimensions.get(mirror, rotation);
    }

    private Paste getWorldGenPaste() {
        return new Paste() {

			@Override
			public <T extends TemplateContext> boolean apply(WorldAccess world, T ctx, BlockPos origin, BlockMirror mirror, BlockRotation rotation, TemplatePlacement<T> placement, PasteConfig config) {
				return FeatureTemplate.this.pasteWorldGen(world, ctx, origin, mirror, rotation, placement, config);
			}
        };
    }

    private Paste getCheckedPaste() {
        return new Paste() {

			@Override
			public <T extends TemplateContext> boolean apply(WorldAccess world, T ctx, BlockPos origin, BlockMirror mirror, BlockRotation rotation, TemplatePlacement<T> placement, PasteConfig config) {
				return FeatureTemplate.this.pasteChecked(world, ctx, origin, mirror, rotation, placement, config);
			}
        };
    }

    private Paste getUnCheckedPaste() {
        return new Paste() {

			@Override
			public <T extends TemplateContext> boolean apply(WorldAccess world, T ctx, BlockPos origin, BlockMirror mirror, BlockRotation rotation, TemplatePlacement<T> placement, PasteConfig config) {
				return FeatureTemplate.this.pasteUnChecked(world, ctx, origin, mirror, rotation, placement, config);
			}
        };
    }

    private static void updatePostPlacement(WorldAccess world, BlockSetter setter, BufferIterator iterator, BlockInfo[] blocks, BlockPos origin, BlockPos.Mutable pos1, BlockPos.Mutable pos2) {
        if (!iterator.isEmpty()) {
            while (iterator.next()) {
                int index = iterator.nextIndex();
                if (index < 0 || index >= blocks.length) {
                    continue;
                }

                BlockInfo block = blocks[index];
                addPos(pos1, origin, block.pos());

                for (Direction direction : DIRECTIONS) {
                    updatePostPlacement(world, setter, pos1, pos2, direction);
                }
            }
        }
    }

    private static void updatePostPlacement(WorldAccess world, BlockSetter setter, BlockPos.Mutable pos1, BlockPos.Mutable pos2, Direction direction) {
        pos2.set(pos1).move(direction, 1);

        BlockState state1 = world.getBlockState(pos1);
        BlockState state2 = world.getBlockState(pos2);

        // update state at pos1 - the input position
        BlockState result1 = state1.getStateForNeighborUpdate(direction, state2, world, pos1, pos2);
        if (result1 != state1) {
        	setter.setBlock(pos1, result1, PASTE_FLAG);
        }

        // update state at pos2 - the neighbour
        BlockState result2 = state2.getStateForNeighborUpdate(direction.getOpposite(), result1, world, pos2, pos1);
        if (result2 != state2) {
        	setter.setBlock(pos2, result2, PASTE_FLAG);
        }
    }

    private void placeBase(WorldAccess world, BlockSetter setter, BlockPos pos, BlockPos.Mutable pos2, BlockState state, int depth) {
        for (int dy = 0; dy < depth; dy++) {
            pos2.set(pos).move(Direction.DOWN, dy);
            if (world.getBlockState(pos2).isOpaque()) {
                return;
            }
            setter.setBlock(pos2, state, 2);
        }
    }

    public static BlockPos transform(BlockPos pos, BlockMirror mirror, BlockRotation rotation) {
        return net.minecraft.structure.StructureTemplate.transformAround(pos, mirror, rotation, BlockPos.ORIGIN);
    }

    public static void addPos(BlockPos.Mutable pos, BlockPos a, BlockPos b) {
        pos.setX(a.getX() + b.getX());
        pos.setY(a.getY() + b.getY());
        pos.setZ(a.getZ() + b.getZ());
    }

    public static Optional<FeatureTemplate> load(RegistryWrapper<Block> blockLookup, InputStream data) {
        try {
            NbtCompound root = NbtIo.readCompressed(data, new NbtSizeTracker(10000, 100000));
            if (!root.contains("palette") || !root.contains("blocks")) {
                return Optional.empty();
            }
            BlockState[] palette = readPalette(blockLookup, root.getList("palette", 10));
            BlockInfo[] blockInfos = readBlocks(root.getList("blocks", 10), palette);
            List<BlockInfo> blocks = relativize(blockInfos);
            return Optional.of(new FeatureTemplate(blocks));
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private static BlockState[] readPalette(RegistryWrapper<Block> blockLookup, NbtList list) {
        BlockState[] palette = new BlockState[list.size()];
        for (int i = 0; i < list.size(); i++) {
            try {
                palette[i] = NbtHelper.toBlockState(blockLookup, list.getCompound(i));
            } catch (Throwable t) {
                palette[i] = Blocks.AIR.getDefaultState();
            }
        }
        return palette;
    }

    private static BlockInfo[] readBlocks(NbtList list, BlockState[] palette) {
        BlockInfo[] blocks = new BlockInfo[list.size()];
        for (int i = 0; i < list.size(); i++) {
            NbtCompound compound = list.getCompound(i);
            BlockState state = palette[compound.getInt("state")];
            BlockPos pos = readPos(compound.getList("pos", 3));
            blocks[i] = new BlockInfo(pos, state);
        }
        return blocks;
    }

    private static List<BlockInfo> relativize(BlockInfo[] blocks) {
        // find the lowest, most-central block (the origin)
        BlockPos origin = null;
        int lowestSolid = Integer.MAX_VALUE;

        for (BlockInfo block : blocks) {
            if (!block.state().isOpaque()) {
                continue;
            }

            if (origin == null) {
                origin = block.pos();
                lowestSolid = block.pos().getY();
            } else if (block.pos().getY() < lowestSolid) {
                origin = block.pos();
                lowestSolid = block.pos().getY();
            } else if (block.pos().getY() == lowestSolid) {
                if (block.pos().getX() < origin.getX() && block.pos().getZ() <= origin.getZ()) {
                    origin = block.pos();
                    lowestSolid = block.pos().getY();
                } else if (block.pos().getZ() < origin.getZ() && block.pos().getX() <= origin.getX()) {
                    origin = block.pos();
                    lowestSolid = block.pos().getY();
                }
            }
        }

        if (origin == null) {
            return Arrays.asList(blocks);
        }

        // relativize all blocks to the origin
        List<BlockInfo> list = new ArrayList<>(blocks.length);
        for (BlockInfo in : blocks) {
            BlockPos pos = in.pos().subtract(origin);
            list.add(new BlockInfo(pos, in.state()));
        }

        return list;
    }

    private static BlockPos readPos(NbtList list) {
        int x = list.getInt(0);
        int y = list.getInt(1);
        int z = list.getInt(2);
        return new BlockPos(x, y, z);
    }

    public interface PasteProvider {
        PasteFunction get(FeatureTemplate template);
    }

    public interface PasteFunction {
        <T extends TemplateContext> boolean paste(WorldAccess world, BlockPos origin, BlockMirror mirror, BlockRotation rotation, TemplatePlacement<T> placement, PasteConfig config);
    }
    
    public interface BlockSetter {
    	void setBlock(BlockPos pos, BlockState state, int flags);
    }
}
