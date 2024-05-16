package org.haberno.terraloomed.worldgen.feature.template.decorator;

import java.util.Set;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

record TreeDecorator(net.minecraft.world.gen.treedecorator.TreeDecorator decorator, net.minecraft.world.gen.treedecorator.TreeDecorator modifiedDecorator) implements TemplateDecorator<TreeContext> {
	public static final Codec<TreeDecorator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		net.minecraft.world.gen.treedecorator.TreeDecorator.TYPE_CODEC.fieldOf("decorator").forGetter(TreeDecorator::decorator),
		net.minecraft.world.gen.treedecorator.TreeDecorator.TYPE_CODEC.fieldOf("modified_decorator").forGetter(TreeDecorator::modifiedDecorator)
	).apply(instance, TreeDecorator::new));
	
    public net.minecraft.world.gen.treedecorator.TreeDecorator getDecorator(boolean modified) {
        return modified ? this.modifiedDecorator : this.decorator;
    }

    @Override
    public void apply(WorldAccess level, TreeContext buffer, Random random, boolean modified) {
    	Set<BlockPos> logs = buffer.logs();
    	Set<BlockPos> leaves = buffer.leaves();
    	
        if (logs.isEmpty() || leaves.isEmpty()) {
            return;
        }
        
        net.minecraft.world.gen.treedecorator.TreeDecorator.Generator ctx = new net.minecraft.world.gen.treedecorator.TreeDecorator.Generator(
        	level,
        	(pos, state) -> level.setBlockState(pos, state, 19), 
        	random,
        	logs,
        	leaves,
        	ImmutableSet.of()
        );

        this.getDecorator(modified).generate(ctx);
    }

	@Override
	public Codec<TreeDecorator> codec() {
		return CODEC;
	}
}
