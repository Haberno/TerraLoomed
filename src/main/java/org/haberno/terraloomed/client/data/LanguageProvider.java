/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

// copy of net/minecraftforge/common/data/LanguageProvider
package org.haberno.terraloomed.client.data;

import com.google.gson.JsonObject;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import net.minecraft.block.Block;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class LanguageProvider implements DataProvider {
    private final Map<String, String> data = new TreeMap<>();
    private final DataOutput output;
    private final String modid;
    private final String locale;

    public LanguageProvider(DataOutput output, String modid, String locale) {
        this.output = output;
        this.modid = modid;
        this.locale = locale;
    }

    protected abstract void addTranslations();

    @Override
    public CompletableFuture<?> run(DataWriter cache) {
      	this.addTranslations();

        if (!this.data.isEmpty())
            return this.save(cache, this.output.resolvePath(DataOutput.OutputType.RESOURCE_PACK).resolve(this.modid).resolve("lang").resolve(this.locale + ".json"));
        
        return CompletableFuture.allOf();
    }

    @Override
    public String getName() {
        return "Languages: " + locale;
    }

    private CompletableFuture<?> save(DataWriter cache, Path target) {
        JsonObject json = new JsonObject();
        this.data.forEach(json::addProperty);
        return DataProvider.writeToPath(cache, json, target);
    }

    public void addBlock(Supplier<? extends Block> key, String name) {
        add(key.get(), name);
    }

    public void add(Block key, String name) {
        add(key.getTranslationKey(), name);
    }

    public void addItem(Supplier<? extends Item> key, String name) {
        add(key.get(), name);
    }

    public void add(Item key, String name) {
        add(key.getTranslationKey(), name);
    }

    public void addItemStack(Supplier<ItemStack> key, String name) {
        add(key.get(), name);
    }

    public void add(ItemStack key, String name) {
        add(key.getTranslationKey(), name);
    }

    public void addEnchantment(Supplier<? extends Enchantment> key, String name) {
        add(key.get(), name);
    }

    public void add(Enchantment key, String name) {
        add(key.getTranslationKey(), name);
    }

    /*
    public void addBiome(Supplier<? extends Biome> key, String name) {
        add(key.get(), name);
    }

    public void add(Biome key, String name) {
        add(key.getTranslationKey(), name);
    }
    */

    public void addEffect(Supplier<? extends StatusEffect> key, String name) {
        add(key.get(), name);
    }

    public void add(StatusEffect key, String name) {
        add(key.getTranslationKey(), name);
    }

    public void addEntityType(Supplier<? extends EntityType<?>> key, String name) {
        add(key.get(), name);
    }

    public void add(EntityType<?> key, String name) {
        add(key.getTranslationKey(), name);
    }

    public void add(String key, String value) {
        if (data.put(key, value) != null)
            throw new IllegalStateException("Duplicate translation key " + key);
    }
}
