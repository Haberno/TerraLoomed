package org.haberno.terraloomed.worldgen.biome.modifier;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.StringIdentifiable;
import com.mojang.serialization.Codec;

public enum Order implements StringIdentifiable {
	PREPEND("prepend") {
		
		@Override
		public <T> List<T> add(List<T> list1, List<T> list2) {
			List<T> newList = new ArrayList<>(list1.size() + list2.size());
			newList.addAll(list2);
			newList.addAll(list1);
			return newList;
		}
	},
	APPEND("append") {
		
		@Override
		public <T> List<T> add(List<T> list1, List<T> list2) {
			List<T> newList = new ArrayList<>(list1.size() + list2.size());
			newList.addAll(list1);
			newList.addAll(list2);
			return newList;
		}
	};

	public static final Codec<Order> CODEC = StringIdentifiable.createCodec(Order::values);
	
	private String name;
	
	private Order(String name) {
		this.name = name;
	}
	
	@Override
	public String asString() {
		return this.name;
	}
	
	public abstract <T> List<T> add(List<T> list1, List<T> list2);
}
