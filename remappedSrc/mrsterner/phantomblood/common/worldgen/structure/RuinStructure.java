package mrsterner.phantomblood.common.worldgen.structure;

import mrsterner.phantomblood.PhantomBlood;
import mrsterner.phantomblood.common.worldgen.generator.RuinGenerator;
import net.minecraft.structure.MarginedStructureStart;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

public class RuinStructure extends StructureFeature<StructurePoolFeatureConfig> {
    public static final Identifier ID = new Identifier(PhantomBlood.MODID,"arrow");

    public RuinStructure() {
        super(StructurePoolFeatureConfig.CODEC);
    }

    @Override
    public StructureStartFactory<StructurePoolFeatureConfig> getStructureStartFactory() {
        return RuinStructure.Start::new;
    }

    public static class Start extends MarginedStructureStart<StructurePoolFeatureConfig> {
        public Start(StructureFeature<StructurePoolFeatureConfig> feature, int chunkX, int chunkZ, BlockBox box, int references, long seed) {
            super(feature, chunkX, chunkZ, box, references, seed);
        }

        @Override
        public void init(DynamicRegistryManager registryManager, ChunkGenerator chunkGenerator, StructureManager structureManager, int x, int z, Biome biome, StructurePoolFeatureConfig config) {
            RuinGenerator.addPieces(registryManager, config, chunkGenerator, structureManager, new BlockPos(x << 4, 1/2, z << 4), children, random);
            this.setBoundingBoxFromChildren();
        }
    }
}