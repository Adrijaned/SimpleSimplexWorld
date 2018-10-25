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
import org.terasology.math.geom.Vector3i;
import org.terasology.mazeAPI.Maze;
import org.terasology.mazeAPI.Util;
import org.terasology.mazeAPI.config.MazeConfig;
import org.terasology.utilities.random.FastRandom;
import org.terasology.utilities.random.Random;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;

@Produces(ChunkTypeFacet.class)
public class ChunkTypeFacetProvider implements FacetProvider {
    private Maze maze;
    private final MazeConfig mazeConfig;

    public ChunkTypeFacetProvider() {
        mazeConfig = new MazeConfig();

    }

    public Maze getMaze() {
        return maze;
    }

    @Override
    public void setSeed(long seed) {
        mazeConfig.util = new Util() {
            private Random random = new FastRandom(seed); // MazeAPIs Util uses java random for compatibility purposes

            @Override
            public int randomInt() {
                return Math.abs(random.nextInt());
            }
        };
        maze = new Maze(mazeConfig);
    }

    @Override
    public void process(GeneratingRegion region) {
        final Border3D border = region.getBorderForFacet(ChunkTypeFacet.class);
        ChunkTypeFacet facet = new ChunkTypeFacet(region.getRegion(), border);
        Region3i regionPositions = facet.getWorldRegion();
        final Vector3i[] chunkPositions = ChunkMath.calcChunkPos(regionPositions);
        if (chunkPositions.length != 1) {
            throw new IllegalArgumentException("Not being generated one chunk at time");
        }
        final Vector3i chunkPos = chunkPositions[0].add(mazeConfig.width / 2, 0, mazeConfig.height / 2);
        if (chunkPos.y != 0
                || chunkPos.x < 0 || chunkPos.x >= mazeConfig.width
                || chunkPos.z < 0 || chunkPos.z >= mazeConfig.height) {
            facet.setChunkType(ChunkType.VOID);
        } else {
            facet.setTile(maze.level.getTile(chunkPos.x, chunkPos.z));
            switch (maze.level.getTile(chunkPos.x, chunkPos.z).tileType) {
                case WALL:
                    facet.setChunkType(ChunkType.CLIFF);
                    break;
                case CORRIDOR:
                case POTENTIAL_DOOR:
                case DOOR:
                    facet.setChunkType(ChunkType.CORRIDOR);
                    break;
                case ROOM:
                    facet.setChunkType(ChunkType.PLAIN);
            }
        }
        region.setRegionFacet(ChunkTypeFacet.class, facet);
    }
}
