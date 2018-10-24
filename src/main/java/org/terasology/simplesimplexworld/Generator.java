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

import org.terasology.engine.SimpleUri;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.math.geom.Vector3f;
import org.terasology.mazeAPI.mazeItems.Tile;
import org.terasology.registry.In;
import org.terasology.world.generation.BaseFacetedWorldGenerator;
import org.terasology.world.generation.WorldBuilder;
import org.terasology.world.generator.RegisterWorldGenerator;
import org.terasology.world.generator.plugin.WorldGeneratorPluginLibrary;

@RegisterWorldGenerator(id = "simpleSimplexWorld", displayName = "Simple Simplex World")
public class Generator extends BaseFacetedWorldGenerator {
    @In
    private WorldGeneratorPluginLibrary worldGeneratorPluginLibrary;
    private ChunkTypeFacetProvider chunkTypeFacetProvider;

    public Generator(SimpleUri uri) {
        super(uri);
    }

    @Override
    protected WorldBuilder createWorld() {
        chunkTypeFacetProvider = new ChunkTypeFacetProvider();
        return new WorldBuilder(worldGeneratorPluginLibrary)
                .addProvider(new HeightFacetProvider())
                .addProvider(chunkTypeFacetProvider)
                .addRasterizer(new MainRasterizer());
    }

    @Override
    public Vector3f getSpawnPosition(EntityRef entity) { //TODO this is dirty hack
        for (Tile tile : chunkTypeFacetProvider.getMaze().level) {
            if (tile.isPassable) {
                return new Vector3f(
                        (tile.position.x - (chunkTypeFacetProvider.getMaze().config.width >> 1)) * 32f,
                        4,
                        (tile.position.y - (chunkTypeFacetProvider.getMaze().config.height >> 1)) * 32f
                );
            }
        }
        return null;
    }
}
