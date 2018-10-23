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
        @Facet(TerrainFacet.class),
        @Facet(SurfaceHeightFacet.class)
})
public class DirtRasterizer implements WorldRasterizer {

    private Block dirt;

    @Override
    public void initialize() {
        dirt = CoreRegistry.get(BlockManager.class).getBlock("Core:Dirt");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        if (chunk.getPosition().z() > 30) {
            return; //dirt should be found no higher than 20, putting in 30 just to make sure.
        }
        for (Vector3i position : chunkRegion.getRegion()) {
            final SurfaceHeightFacet surfaceHeightFacet = chunkRegion.getFacet(SurfaceHeightFacet.class);
            if (position.y <= surfaceHeightFacet.getWorld(position.x, position.z)) {
                final TerrainFacet terrainFacet = chunkRegion.getFacet(TerrainFacet.class);
                if (terrainFacet.getWorld(position) > -0.8f)
                chunk.setBlock(ChunkMath.calcBlockPos(position), dirt);
            }
        }
    }
}
