/*
 * Copyright (c) 2016, 2017 Chris Ali. All rights reserved.
 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program;
if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA

 If you have any questions about this project, you can visit
 the project website at the project page on http://github.com/chris-ali/j6dof-flight-sim/
 */
package com.chrisali.javaflightsim.otw.terrain;

import com.chrisali.javaflightsim.controllers.Configuration;
import com.chrisali.javaflightsim.otw.entities.Ownship;
import com.chrisali.javaflightsim.otw.renderengine.Loader;
import com.chrisali.javaflightsim.otw.textures.TerrainTexture;
import com.chrisali.javaflightsim.otw.textures.TerrainTexturePack;
import java.io.File;
import java.util.TreeMap;

/**
 * An array of {@link Terrain} objects used to model out the world
 *
 * @author Christopher Ali
 *
 */
public class TerrainCollection {
   private TreeMap<String, Terrain> terrainTree;
   private File terrainDir = null;

   /**
    * Creates a TreeMao of {@link Terrain} objects, with texture blending and height maps. Each key to the tree map consists of
    * the string "xGrid-zGrid", which represents the terrain object's position relative to other terrains in an array fashion
    *
    * @param numTerrains
    * @param loader
    * @param ownship
    */
   public TerrainCollection(int numTerrains, Loader loader, Ownship ownship) {
      terrainTree = new TreeMap<>();
      Configuration conf = Configuration.getInstance();
      terrainDir = conf.getTerrain();

      TerrainTexturePack texturePack = createTexturePack("fields", "town", "forest", "water", loader);
      TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap", terrainDir));

      for (int i = 0; i < numTerrains; i++) {
         for (int j = 0; j < numTerrains; j++) {
            terrainTree.put(i + "-" + j, new Terrain(i, j, "heightMap", terrainDir, loader, texturePack, blendMap, ownship));
         }
      }
   }

   /**
    * Creates a texture blending "pack," which creates a blend map to paint the terrain with 4 texture types
    * assigned to black, red, green and blue colors
    *
    * @param backgroundTextureName
    * @param rTextureName
    * @param gTextureName
    * @param bTextureName
    * @param loader
    * @return
    */
   private TerrainTexturePack createTexturePack(String backgroundTextureName,
      String rTextureName, String gTextureName, String bTextureName, Loader loader) {
      TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture(backgroundTextureName, terrainDir));
      TerrainTexture rTexture = new TerrainTexture(loader.loadTexture(rTextureName, terrainDir));
      TerrainTexture gTexture = new TerrainTexture(loader.loadTexture(gTextureName, terrainDir));
      TerrainTexture bTexture = new TerrainTexture(loader.loadTexture(bTextureName, terrainDir));

      return new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
   }

   public TreeMap<String, Terrain> getTerrainTree() {
      return terrainTree;
   }
}
