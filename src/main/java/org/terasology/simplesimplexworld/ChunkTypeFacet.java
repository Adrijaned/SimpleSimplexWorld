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

import org.terasology.math.Region3i;
import org.terasology.mazeapi.mazeItems.Tile;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.WorldFacet3D;

public class ChunkTypeFacet implements WorldFacet3D {
    private final Region3i worldRegion;
    private final Region3i relativeRegion;
    private ChunkType chunkType;
    private Tile tile;

    public ChunkTypeFacet(Region3i targetRegion, Border3D border) {
        worldRegion = border.expandTo3D(targetRegion);
        relativeRegion = border.expandTo3D(targetRegion.size());
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public void setChunkType(ChunkType chunkType) {
        this.chunkType = chunkType;
    }

    public ChunkType getChunkType() {
        return chunkType;
    }

    @Override
    public Region3i getWorldRegion() {
        return worldRegion;
    }

    @Override
    public Region3i getRelativeRegion() {
        return relativeRegion;
    }
}
