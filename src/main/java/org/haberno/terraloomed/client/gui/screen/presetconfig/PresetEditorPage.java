package org.haberno.terraloomed.client.gui.screen.presetconfig;

import java.awt.Color;
import java.io.IOException;
import java.util.Optional;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import raccoonman.reterraforged.RTFCommon;
import raccoonman.reterraforged.client.data.RTFTranslationKeys;
import raccoonman.reterraforged.client.gui.screen.page.BisectedPage;
import raccoonman.reterraforged.client.gui.screen.presetconfig.PresetListPage.PresetEntry;
import raccoonman.reterraforged.client.gui.widget.Slider;
import raccoonman.reterraforged.client.gui.widget.ValueButton;
import raccoonman.reterraforged.concurrent.cache.CacheManager;
import raccoonman.reterraforged.config.PerformanceConfig;
import raccoonman.reterraforged.data.preset.PresetData;
import raccoonman.reterraforged.data.preset.settings.Preset;
import raccoonman.reterraforged.data.preset.settings.WorldSettings;
import raccoonman.reterraforged.registries.RTFRegistries;
import raccoonman.reterraforged.world.worldgen.GeneratorContext;
import raccoonman.reterraforged.world.worldgen.biome.spawn.SpawnType;
import raccoonman.reterraforged.world.worldgen.cell.Cell;
import raccoonman.reterraforged.world.worldgen.heightmap.Levels;
import raccoonman.reterraforged.world.worldgen.noise.NoiseUtil;
import raccoonman.reterraforged.world.worldgen.tile.Tile;
import raccoonman.reterraforged.world.worldgen.util.PosUtil;

public abstract class PresetEditorPage extends BisectedPage<PresetConfigScreen, ClickableWidget, ClickableWidget> {
	private Slider zoom;
	private CyclingButtonWidget<RenderMode> renderMode;
	private ValueButton<Integer> seed;
	private Preview preview;
	protected PresetEntry preset;
	
	public PresetEditorPage(PresetConfigScreen screen, PresetEntry preset) {
		super(screen);
		
		this.preset = preset;
	}
	
	protected void regenerate() {
		this.preview.regenerate();
	}
	
	@Override
	public void init() {
		super.init();

		if(this.preview != null) {
			try {
				this.preview.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		this.zoom = PresetWidgets.createIntSlider(Optional.ofNullable(this.zoom).map(Slider::getLerpedValue).orElse(680.0D).intValue(), 1, 1000, RTFTranslationKeys.GUI_SLIDER_ZOOM, (slider, value) -> {
			this.regenerate();
			return value;
		});
		this.renderMode = PresetWidgets.createCycle(ImmutableList.copyOf(RenderMode.values()), this.renderMode != null ? this.renderMode.getValue() : RenderMode.BIOME_TYPE, Optional.empty(), (button, value) -> {
			this.regenerate();
		}, RenderMode::name);
		this.seed = PresetWidgets.createRandomButton(RTFTranslationKeys.GUI_BUTTON_SEED, (int) this.screen.getSettings().options().seed(), (i) -> {
			this.screen.setSeed(i);
			this.regenerate();
		});

		this.preview = new Preview();
		this.preview.regenerate();

		this.right.addWidget(this.zoom);
		this.right.addWidget(this.renderMode);
		this.right.addWidget(this.seed);
		this.right.addWidget(this.preview);
	}
	
	@Override
	public void onClose() {
		super.onClose();
	
		try {
			this.preset.save();
			this.preview.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onDone() {
		super.onDone();
		
		try {
			this.screen.applyPreset(this.preset);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public class Preview extends ButtonWidget {
	    private static final int FACTOR = 4;
	    public static final int SIZE = (1 << 4) << FACTOR;
	    private static final float[] LEGEND_SCALES = { 1, 0.9F, 0.75F, 0.6F };
	    private NativeImageBackedTexture texture = new NativeImageBackedTexture(new NativeImage(SIZE, SIZE, false));
	    private Identifier textureId = MinecraftClient.getInstance().getTextureManager().registerTexture(RTFCommon.MOD_ID + "-preview-framebuffer", this.texture); 
	    private Tile tile;
	    private int centerX, centerZ;
	    
	    private String hoveredCoords = "";
	    //TODO maybe make this a map or something instead?
	    private String[] legendValues = {"", "", "", ""};
	    private Text[] legendLabels = { Text.translatable(RTFTranslationKeys.GUI_LABEL_PREVIEW_AREA), Text.translatable(RTFTranslationKeys.GUI_LABEL_PREVIEW_TERRAIN), Text.translatable(RTFTranslationKeys.GUI_LABEL_PREVIEW_BIOME), Text.translatable(RTFTranslationKeys.GUI_LABEL_PREVIEW_NOISE_VALUE) };
	    
	    private int offsetX, offsetZ;

	    public Preview() {
	        super(-1, -1, -1, -1, ScreenTexts.EMPTY, (b) -> {
	        	MinecraftClient mc = MinecraftClient.getInstance();
	        	Mouse mouse = mc.mouse;
	        	if(b instanceof Preview self) {
			        if (self.updateLegend((int) mouse.getX(), (int) mouse.getY()) && !self.hoveredCoords.isEmpty()) {
			            self.playDownSound(MinecraftClient.getInstance().getSoundManager());
			            PresetEditorPage.this.screen.minecraft.keyboardHandler.setClipboard(self.hoveredCoords);
			        }
	        	}
	        }, DEFAULT_NARRATION_SUPPLIER);
	    }

	    public void regenerate() {
			GeneratorOptionsHolder settings = PresetEditorPage.this.screen.getSettings();
	        DynamicRegistryManager.Immutable registries = settings.getCombinedRegistryManager();
	        RegistryWrapper.WrapperLookup provider = PresetEditorPage.this.preset.getPreset().buildPatch(registries);
	        RegistryEntryLookup<Preset> presets = provider.getWrapperOrThrow(RTFRegistries.PRESET);
	        Preset preset = presets.getOrThrow(PresetData.PRESET).value();
	        WorldSettings world = preset.world();
	        WorldSettings.Properties properties = world.properties;
	        
	        CacheManager.clear();
	        
			PerformanceConfig config = PerformanceConfig.read(PerformanceConfig.DEFAULT_FILE_PATH)
				.resultOrPartial(RTFCommon.LOGGER::error)
				.orElseGet(PerformanceConfig::makeDefault);
	        GeneratorContext generatorContext = GeneratorContext.makeUncached(preset, (int) settings.generatorOptions().getSeed(), FACTOR, 0, config.batchCount());
	        
	        this.centerX = 0;
	        this.centerZ = 0;
	        
	        if(preset.world().properties.spawnType == SpawnType.CONTINENT_CENTER) {
	        	long nearestContinentCenter = generatorContext.localHeightmap.get().continent().getNearestCenter(this.offsetX, this.offsetZ);
	        	this.centerX = PosUtil.unpackLeft(nearestContinentCenter);
	        	this.centerZ = PosUtil.unpackRight(nearestContinentCenter);
	        } else {
	        	this.centerX = 0;
	        	this.centerZ = 0;
	        }

	        this.tile = generatorContext.generator.generateZoomed(this.centerX, this.centerZ, this.getZoom(), false).join();
	        RenderMode renderMode = PresetEditorPage.this.renderMode.getValue();
	        Levels levels = new Levels(properties.terrainScaler(), properties.seaLevel);

	        int stroke = 2;
	        int width = this.tile.getBlockSize().size();

	        NativeImage pixels = this.texture.getImage();
	        this.tile.iterate((cell, x, z) -> {
	            if (x < stroke || z < stroke || x >= width - stroke || z >= width - stroke) {
	                pixels.setPixelRGBA(x, z, Color.BLACK.getRGB());
	            } else {
	                pixels.setPixelRGBA(x, z, renderMode.getColor(cell, levels));
	            }
	        });
	        this.texture.upload();
	    }
	    
	    public void close() throws Exception {
	    	this.texture.close();

	    	CacheManager.clear();
	    }

	    @Override
	    public void render(DrawContext guiGraphics, int mx, int my, float partialTicks) {
	    	int x = this.getX();
	    	int y = this.getY();
	    	
	    	this.height = this.getWidth();
	        RenderSystem.enableBlend();
	        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
	        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
	    	guiGraphics.drawTexture(this.textureId, x, y, 0, 0, this.width, this.height, this.width, this.height);

	    	this.updateLegend(mx, my);

	    	this.renderLegend(guiGraphics, mx, my, this.legendLabels, this.legendValues, x, y + this.width, 10, 0xFFFFFF);
	    }

	    private boolean updateLegend(int mx, int my) {
	        if (this.tile != null) {
	            int left = this.getX();
	            int top = this.getY();
	            float size = this.width;
	
	            int zoom = this.getZoom();
	            int width = Math.max(1, this.tile.getBlockSize().size() * zoom);
	            int height = Math.max(1, this.tile.getBlockSize().size() * zoom);
	            this.legendValues[0] = width + "x" + height;
	            if (mx >= left && mx <= left + size && my >= top && my <= top + size) {
	                float fx = (mx - left) / size;
	                float fz = (my - top) / size;
	                int ix = NoiseUtil.round(fx * this.tile.getBlockSize().size());
	                int iz = NoiseUtil.round(fz * this.tile.getBlockSize().size());
	                Cell cell = this.tile.lookup(ix, iz);
	                this.legendValues[1] = getTerrainName(cell);
	                this.legendValues[2] = getBiomeName(cell);
	                this.legendValues[3] = String.valueOf(PresetEditorPage.this.renderMode.getValue().getNoiseValue(cell));
	
	                int dx = (ix - (this.tile.getBlockSize().size() / 2)) * zoom;
	                int dz = (iz - (this.tile.getBlockSize().size() / 2)) * zoom;
	
	                this.hoveredCoords = (this.centerX + dx) + ":" + (this.centerZ + dz);
	                return true;
	            } else {
	            	this.hoveredCoords = "";
	            }
	        }
	        return false;
	    }

	    private float getLegendScale() {
	        int index = PresetEditorPage.this.screen.minecraft.options.guiScale().get() - 1;
	        if (index < 0 || index >= LEGEND_SCALES.length) {
	            // index=-1 == GuiScale(AUTO) which is the same as GuiScale(4)
	            // values above 4 don't exist but who knows what mods might try set it to
	            // in both cases use the smallest acceptable scale
	            index = LEGEND_SCALES.length - 1;
	        }
	        return LEGEND_SCALES[index];
	    }

	    private void renderLegend(DrawContext guiGraphics, int mx, int my, Text[] labels, String[] values, int left, int top, int lineHeight, int color) {
	        float scale = this.getLegendScale();
	        MatrixStack pose = guiGraphics.getMatrices();
	        	
	        pose.push();
	        pose.translate(left + 3.75F * scale, top - lineHeight * (3.2F * scale), 0);
	        pose.scale(scale, scale, 1);
	
	        MinecraftClient mc = MinecraftClient.getInstance();
	        TextRenderer renderer = mc.textRenderer;
	        int spacing = 0;
	        for (Text s : labels) {
	            spacing = Math.max(spacing, renderer.getWidth(s));
	        }
	
	        float maxWidth = (this.width - 4) / scale;
	        for (int i = 0; i < labels.length && i < values.length; i++) {
	        	Text label = labels[i];
	            String value = values[i];
	
	            while (value.length() > 0 && spacing + renderer.getWidth(value) > maxWidth) {
	                value = value.substring(0, value.length() - 1);
	            }
	
	            guiGraphics.drawTextWithShadow(renderer, label, 0, i * lineHeight, color);
	            guiGraphics.drawTextWithShadow(renderer, value, spacing, i * lineHeight, color);
	        }
	
	        pose.pop();
	
	        if (!this.hoveredCoords.isEmpty()) {
	        	guiGraphics.drawCenteredTextWithShadow(renderer, this.hoveredCoords, mx, my - 10, 0xFFFFFF);
	        }
	    }
	
	    private int getZoom() {
	        return NoiseUtil.round(1.5F * (1001 - (float) PresetEditorPage.this.zoom.getLerpedValue()));
	    }
	
	    private static String getTerrainName(Cell cell) {
	        if (cell.terrain.isRiver()) {
	            return "river";
	        }
	        return cell.terrain.getName().toLowerCase();
	    }
	
	    private static String getBiomeName(Cell cell) {
	        String terrain = cell.terrain.getName().toLowerCase();
	        if (terrain.contains("ocean")) {
	            if (cell.temperature < 0.3F) {
	                return "cold_" + terrain;
	            }
	            if (cell.temperature > 0.6F) {
	                return "warm_" + terrain;
	            }
	            return terrain;
	        }
	        if (terrain.contains("river")) {
	            return "river";
	        }
	        return cell.biomeType.name().toLowerCase();
	    }
	}
}
