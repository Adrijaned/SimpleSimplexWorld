/*
 * Copyright 2018 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.simplesimplexworld;

import org.terasology.math.ChunkMath;
import org.terasology.math.Region3i;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Rect2i;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.Requires;
import org.terasology.world.generation.WorldRasterizer;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

@Requires({
        @Facet(SurfaceHeightFacet.class),
        @Facet(ChunkTypeFacet.class),
        @Facet(FloraFacet.class)
})
public class FloraRasterizer implements WorldRasterizer {

    private Block[] flowers;

    @Override
    public void initialize() {
        final BlockManager blockManager = CoreRegistry.get(BlockManager.class);
        flowers = new Block[10];
        flowers[0] = blockManager.getBlock("Core:Iris");
        flowers[1] = blockManager.getBlock("Core:Lavender");
        flowers[2] = blockManager.getBlock("Core:RedClover");
        flowers[3] = blockManager.getBlock("Core:Dandelion");
        flowers[4] = blockManager.getBlock("Core:RedFlower");
        flowers[5] = blockManager.getBlock("Core:GlowbellBloom");
        flowers[6] = blockManager.getBlock("Core:Glowbell");
        flowers[7] = blockManager.getBlock("Core:Tulip");
        flowers[8] = blockManager.getBlock("Core:YellowFlower");
        flowers[9] = blockManager.getBlock("Core:Cotton3");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        switch (chunkRegion.getFacet(ChunkTypeFacet.class).getChunkType()) {
            case CLIFF:
            case VOID:
                return;
            case PLAIN:
            case CORRIDOR:
                final SurfaceHeightFacet surfaceHeightFacet = chunkRegion.getFacet(SurfaceHeightFacet.class);
                final FloraFacet floraFacet = chunkRegion.getFacet(FloraFacet.class);
                final Region3i region = chunkRegion.getRegion();
                final Rect2i rect2i = Rect2i.createFromMinAndSize(region.minX(), region.minZ(), region.sizeX(), region.sizeZ());
                for (BaseVector2i pos : rect2i.contents()) {
                    final Vector3i localPos = ChunkMath.calcBlockPos(pos.x(), 0, pos.y());
                    int flowerIndex = Math.round(floraFacet.get(localPos.x, localPos.z));
                    if (flowerIndex-- <= 0) {
                        continue;
                    }
                    chunk.setBlock(ChunkMath.calcBlockPos(pos.x(), Math.round(surfaceHeightFacet.get(localPos.x, localPos.z)) + 2, pos.y()), flowers[flowerIndex]);
                }
                break;
        }
    }
}
