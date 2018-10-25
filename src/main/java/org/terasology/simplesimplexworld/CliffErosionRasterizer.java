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
        @Facet(ChunkTypeFacet.class),
        @Facet(SurfaceHeightFacet.class),
        @Facet(CliffErosionFacet.class)
})
public class CliffErosionRasterizer implements WorldRasterizer {

    private Block air;
    private Block grass;

    @Override
    public void initialize() {
        final BlockManager blockManager = CoreRegistry.get(BlockManager.class);
        air = blockManager.getBlock("Engine:air");
        grass = blockManager.getBlock("Core:Grass");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        final ChunkTypeFacet chunkTypeFacet = chunkRegion.getFacet(ChunkTypeFacet.class);
        if (chunkTypeFacet.getChunkType() != ChunkType.CLIFF) {
            return;
        }
        final SurfaceHeightFacet surfaceHeightFacet = chunkRegion.getFacet(SurfaceHeightFacet.class);
        final CliffErosionFacet cliffErosionFacet = chunkRegion.getFacet(CliffErosionFacet.class);
        if (chunkTypeFacet.getTile().isOpenUp) { //up left - position goes down
            final Region3i region = chunkRegion.getRegion();
            for (BaseVector2i pos : Rect2i.createFromMinAndMax(region.minX(), region.minY(), region.maxX(), region.maxY()).contents()) {
                Block replacementBlock = air;
                if (surfaceHeightFacet.get(ChunkMath.calcBlockPosX(pos.x()), ChunkMath.calcBlockPosZ(region.minZ())) + 2 >= pos.y()) {
                    replacementBlock = grass;
                }
                if (cliffErosionFacet.getWorld(pos.x(), pos.y(), region.minZ()) < 0.33) {
                    chunk.setBlock(ChunkMath.calcBlockPos(pos.x(), pos.y(), region.minZ()), replacementBlock);
                }
                if (cliffErosionFacet.getWorld(pos.x(), pos.y(), region.minZ()) < -0.33) {
                    chunk.setBlock(ChunkMath.calcBlockPos(pos.x(), pos.y(), region.minZ() + 1), replacementBlock);
                }
            }
        }
        if (chunkTypeFacet.getTile().isOpenDown) {
            final Region3i region = chunkRegion.getRegion();
            for (BaseVector2i pos : Rect2i.createFromMinAndMax(region.minX(), region.minY(), region.maxX(), region.maxY()).contents()) {
                Block replacementBlock = air;
                if (surfaceHeightFacet.get(ChunkMath.calcBlockPosX(pos.x()), ChunkMath.calcBlockPosZ(region.maxZ())) + 2 >= pos.y()) {
                    replacementBlock = grass;
                }
                if (cliffErosionFacet.getWorld(pos.x(), pos.y(), region.maxZ()) < 0.33) {
                    chunk.setBlock(ChunkMath.calcBlockPos(pos.x(), pos.y(), region.maxZ()), replacementBlock);
                }
                if (cliffErosionFacet.getWorld(pos.x(), pos.y(), region.maxZ()) < -0.33) {
                    chunk.setBlock(ChunkMath.calcBlockPos(pos.x(), pos.y(), region.maxZ() - 1), replacementBlock);
                }
            }
        }
        if (chunkTypeFacet.getTile().isOpenLeft) {
            final Region3i region = chunkRegion.getRegion();
            for (BaseVector2i pos : Rect2i.createFromMinAndMax(region.minZ(), region.minY(), region.maxZ(), region.maxY()).contents()) {
                Block replacementBlock = air;
                if (surfaceHeightFacet.get(ChunkMath.calcBlockPosX(region.minX()), ChunkMath.calcBlockPosZ(pos.x())) + 2 >= pos.y()) {
                    replacementBlock = grass;
                }
                if (cliffErosionFacet.getWorld(region.minX(), pos.y(), pos.x()) < 0.33) {
                    chunk.setBlock(ChunkMath.calcBlockPos(region.minX(), pos.y(), pos.x()), replacementBlock);
                }
                if (cliffErosionFacet.getWorld(region.minX(), pos.y(), pos.x()) < -0.33) {
                    chunk.setBlock(ChunkMath.calcBlockPos(region.minX() + 1, pos.y(), pos.x()), replacementBlock);
                }
            }
        }
        if (chunkTypeFacet.getTile().isOpenRight) {
            final Region3i region = chunkRegion.getRegion();
            for (BaseVector2i pos : Rect2i.createFromMinAndMax(region.minZ(), region.minY(), region.maxZ(), region.maxY()).contents()) {
                Block replacementBlock = air;
                if (surfaceHeightFacet.get(ChunkMath.calcBlockPosX(region.maxX()), ChunkMath.calcBlockPosZ(pos.x())) + 2 >= pos.y()) {
                    replacementBlock = grass;
                }
                if (cliffErosionFacet.getWorld(region.maxX(), pos.y(), pos.x()) < 0.33) {
                    chunk.setBlock(ChunkMath.calcBlockPos(region.maxX(), pos.y(), pos.x()), replacementBlock);
                }
                if (cliffErosionFacet.getWorld(region.maxX(), pos.y(), pos.x()) < -0.33) {
                    chunk.setBlock(ChunkMath.calcBlockPos(region.maxX() - 1, pos.y(), pos.x()), replacementBlock);
                }
            }
        }
    }
}
