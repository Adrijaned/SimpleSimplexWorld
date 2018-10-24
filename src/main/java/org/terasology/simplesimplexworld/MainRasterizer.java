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
        @Facet(ChunkTypeFacet.class)
})
public class MainRasterizer implements WorldRasterizer {

    private Block grass;
    private Block stone;

    @Override
    public void initialize() {
        final BlockManager blockManager = CoreRegistry.get(BlockManager.class);
        grass = blockManager.getBlock("Core:Grass");
        stone = blockManager.getBlock("Core:Stone");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        final ChunkTypeFacet facet = chunkRegion.getFacet(ChunkTypeFacet.class);
        final SurfaceHeightFacet surfaceHeightFacet = chunkRegion.getFacet(SurfaceHeightFacet.class);
        switch (facet.getChunkType()) {
            case VOID:
                return;
            case CLIFF:
                for (Vector3i pos : chunkRegion.getRegion()) {
                    final Vector3i localPos = ChunkMath.calcBlockPos(pos);
                    if (localPos.y < 62 + surfaceHeightFacet.get(localPos.x, localPos.z) * 2) {
                        chunk.setBlock(localPos, stone);
                    }
                }
                break;
            case CORRIDOR:
            case PLAIN:
                final Region3i region = chunkRegion.getRegion();
                final Rect2i rect2i = Rect2i.createFromMinAndSize(region.minX(), region.minZ(), region.sizeX(), region.sizeZ());
                for (BaseVector2i pos : rect2i.contents()) {
                    final Vector3i localPos = ChunkMath.calcBlockPos(pos.x(), 0, pos.y());
                    chunk.setBlock(ChunkMath.calcBlockPos(pos.x(), Math.round(surfaceHeightFacet.get(localPos.x, localPos.z)) + 1, pos.y()), grass);
                }
                break;
        }
    }
}
