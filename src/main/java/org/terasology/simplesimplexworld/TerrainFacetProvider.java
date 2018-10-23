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

import org.terasology.math.geom.Vector3f;
import org.terasology.math.geom.Vector3i;
import org.terasology.utilities.procedural.Noise;
import org.terasology.utilities.procedural.SimplexNoise;
import org.terasology.utilities.procedural.SubSampledNoise;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;

@Produces(TerrainFacet.class)
public class TerrainFacetProvider implements FacetProvider {

    private Noise noise;

    @Override
    public void setSeed(long seed) {
        noise = new SubSampledNoise(new SimplexNoise(seed + 14354), new Vector3f(0.05f, 0.05f, 0.05f), 1);
    }

    @Override
    public void process(GeneratingRegion region) {
        final Border3D border = region.getBorderForFacet(TerrainFacet.class);
        final TerrainFacet facet = new TerrainFacet(region.getRegion(), border);
        for (Vector3i position : facet.getWorldRegion()) {
            facet.setWorld(position, noise.noise(position.x, position.y, position.z));
        }
        region.setRegionFacet(TerrainFacet.class, facet);
    }
}
