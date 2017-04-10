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

import com.chrisali.javaflightsim.conf.Configuration;
import com.chrisali.javaflightsim.utilities.Vector3D;
import java.io.File;
import java.util.TreeMap;

/**
 * An array of {@link DefaultTerrain} objects used to model out the world.
 *
 * @author Herve Girod
 */
public class DefaultTerrainCollection {
   private TreeMap<String, DefaultTerrain> terrainTree;
   private File terrainDir = null;

   /**
    * Creates a TreeMap of {@link DefaultTerrain} objects, with texture blending and height maps. Each key to the tree map consists of
    * the string "xGrid-zGrid", which represents the terrain object's position relative to other terrains in an array fashion.
    */
   public DefaultTerrainCollection() {
      terrainTree = new TreeMap<>();
      Configuration conf = Configuration.getInstance();
      terrainDir = conf.getTerrain();
   }

   /**
    * Setup the DefaultTerrainCollection.
    *
    * @param numTerrains the number of terrains
    * @param ownship the aircraft
    */
   public void setup(int numTerrains, Vector3D ownship) {
      for (int i = 0; i < numTerrains; i++) {
         for (int j = 0; j < numTerrains; j++) {
            DefaultTerrain terrain = new DefaultTerrain(i, j, "heightMap", terrainDir, ownship);
            terrainTree.put(i + "-" + j, terrain);
         }
      }
   }

   public TreeMap<String, DefaultTerrain> getTerrainTree() {
      return terrainTree;
   }
}
