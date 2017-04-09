/*
 * Copyright (c) 2016, 2017 Chris Ali. All rights reserved.
   Copyright (c) 2017 Herve Girod. All rights reserved.
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
package com.chrisali.javaflightsim.rendering.terrain;

import com.chrisali.javaflightsim.utilities.Vector3D;
import java.util.TreeMap;

/**
 * An array of {@link DefaultTerrain} objects used to model out the world.
 *
 * @author Herve Girod
 */
public class DefaultTerrainCollection {
   private TreeMap<String, DefaultTerrain> terrainTree;

   /**
    * Creates a TreeMao of {@link DefaultTerrain} objects, with texture blending and height maps. Each key to the tree map consists of
    * the string "xGrid-zGrid", which represents the terrain object's position relative to other terrains in an array fashion
    *
    * @param numTerrains
    * @param loader
    * @param ownship
    */
   public DefaultTerrainCollection(int numTerrains, Vector3D ownship) {
      terrainTree = new TreeMap<>();

      for (int i = 0; i < numTerrains; i++) {
         for (int j = 0; j < numTerrains; j++) {
            terrainTree.put(i + "-" + j, new DefaultTerrain(i, j, "heightMap", "Terrain", ownship));
         }
      }
   }

   public TreeMap<String, DefaultTerrain> getTerrainTree() {
      return terrainTree;
   }
}
