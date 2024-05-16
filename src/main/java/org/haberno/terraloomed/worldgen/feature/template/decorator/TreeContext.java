package org.haberno.terraloomed.worldgen.feature.template.decorator;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockState;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import org.haberno.terraloomed.worldgen.feature.template.template.TemplateContext;

import java.util.HashSet;
import java.util.Set;

public class TreeContext implements TemplateContext {
	private Set<BlockPos> logs;
	private Set<BlockPos> leaves;

	public Set<BlockPos> logs() {
		if (this.logs != null) {
			return this.logs;
		}
		return ImmutableSet.of();
	}

	public Set<BlockPos> leaves() {
		if (this.leaves != null) {
			return this.leaves;
		}
		return ImmutableSet.of();
	}

	@Override
	public void recordState(BlockPos pos, BlockState state) {
		if (state.isIn(BlockTags.LEAVES)) {
			this.leaves = safeAdd(this.leaves, pos);
			return;
		}

		if (state.isIn(BlockTags.LOGS)) {
			this.logs = safeAdd(this.logs, pos);
		}
	}

	private static Set<BlockPos> safeAdd(Set<BlockPos> list, BlockPos pos) {
		if (list == null) {
			list = new HashSet<>();
		}
		list.add(pos.toImmutable());
		return list;
	}
}