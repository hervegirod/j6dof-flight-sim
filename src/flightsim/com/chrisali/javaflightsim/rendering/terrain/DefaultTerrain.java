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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.TreeMap;
import javax.imageio.ImageIO;

/**
 * DefaultTerrain object that contains one ground tile that makes up the world of JavaFlightSimulator.
 * This terrain is only used to get the altitude at an aircraft (x, y) position.
 *
 * @author Herve Girod
 */
public class DefaultTerrain implements Comparable<DefaultTerrain> {
   private static final float SIZE = 1600;
   private static final float MAX_HEIGHT = 20;
   private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;
   private float x, z;
   private Vector3D ownship;
   private float[][] heightArray;

   /**
    * <p>
    * Constructor for Terrain object; uses {@link TerrainTexturePack} and {@link TerrainTexture} to
    * generate a terrain texture blend map </p>
    *
    * <p>
    * gridX and gridZ correspond to indices in the terrain array that this object resides</p>
    *
    * <p>
    * fileName and Directory point to a height map .png file to give the terrain vertical modeling</p>
    *
    * <p>
    * Uses {@link EntityCollections#createAutogenImageEntities(com.chrisali.javaflightsim.otw.terrain.Terrain, java.lang.String, java.lang.String)} to generate populate this object's lists of entities
    * using an autogen image file in ./Resources/Terrain/</p>
    *
    * <p>
    * Uses a reference to {@link Ownship} to calculate the distance the midpoint of this terrain instance is from
    * the ownship; this is used to compare to other Terrain objects in compareTo()</p>
    *
    * @param gridX
    * @param gridZ
    * @param fileName
    * @param directory
    * @param ownship the aircraft
    */
   public DefaultTerrain(int gridX, int gridZ, String fileName, String directory, Vector3D ownship) {
      this.x = gridX * SIZE;
      this.z = gridZ * SIZE;
      this.ownship = ownship;
      generateTerrain(fileName, directory);
   }

   /**
    * Gererates a terrain model using a BufferedImage height map
    *
    * @param fileName
    * @param directory (usually "DefaultTerrain" or can specify sub-directories such as "DefaultTerrain\\1-1")
    * @param loader
    * @return terrain model
    */
   private void generateTerrain(String fileName, String directory) {
      BufferedImage image = null;

      try {
         image = ImageIO.read(new File("Resources\\" + directory + "\\" + fileName + ".png"));
      } catch (IOException e) {
         System.err.println("Could not load height map: " + fileName + ".png");
      }

      int vertexCount = image.getHeight();
      heightArray = new float[vertexCount][vertexCount];
      for (int i = 0; i < vertexCount; i++) {
         for (int j = 0; j < vertexCount; j++) {
            float height = getHeightFromImage(j, i, image);
            heightArray[j][i] = height;
         }
      }
   }

   /**
    * Calculates normal of a terrain vertex for use with lighting or specular calculations
    *
    * @param x
    * @param z
    * @param image
    * @return normal vector
    */
   private Vector3D.Float calculateNormal(int x, int z, BufferedImage image) {
      float heightL = getHeightFromImage(x - 1, z, image);
      float heightR = getHeightFromImage(x + 1, z, image);
      float heightD = getHeightFromImage(x, z - 1, image);
      float heightU = getHeightFromImage(x, z + 1, image);

      Vector3D.Float normal = new Vector3D.Float(heightL - heightR, 2f, heightD - heightU);
      normal.normalize();

      return normal;
   }

   /**
    * Calculates the height of a terrain vertex by reading the RGB value pixel of a
    * buffered image and converting it to a height value
    *
    * @param x
    * @param z
    * @param image
    * @return height of terrain vertex
    */
   private float getHeightFromImage(int x, int z, BufferedImage image) {
      // If out of terrain bounds, return 0
      if (x < 0 || x >= image.getHeight() || z < 0 || z >= image.getWidth()) {
         return 0;
      }

      // Get RGB value and convert from white/black to +/-MAX_HEIGHT
      float height = image.getRGB(x, z);
      height += MAX_PIXEL_COLOR / 2f;
      height /= MAX_PIXEL_COLOR / 2f;
      height *= MAX_HEIGHT;

      return height;
   }

   /**
    * Uses Barycentric interpolation to calculate the height of terrain for a given X and Z position
    *
    * @param worldX
    * @param worldZ
    * @return terrain height
    */
   public float getTerrainHeight(float worldX, float worldZ) {
      // Convert absolute world position to position relative to terrain square
      float terrainX = worldX - this.x;
      float terrainZ = worldZ - this.z;

      // Size of each grid square
      float gridSquareSize = SIZE / ((float) heightArray.length - 1);

      // Grid square that the player is located in
      int gridX = (int) Math.floor(terrainX / gridSquareSize);
      int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

      // If outside terrain bounds return zero
      if (gridX >= (heightArray.length - 1) || gridZ >= (heightArray.length - 1) || gridX < 0 || gridZ < 0) {
         return 0;
      }

      // Location of player on a grid square
      float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
      float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;

      // Get terrain height by using barycentric coordinates
      float terrainHeight;
      if (xCoord <= (1 - zCoord)) {
         terrainHeight = TerrainUtilities.barycentric(new Vector3D.Float(0, heightArray[gridX][gridZ], 0),
            new Vector3D.Float(1, heightArray[gridX + 1][gridZ], 0),
            new Vector3D.Float(0, heightArray[gridX][gridZ + 1], 1),
            new Vector3D.Float(xCoord, zCoord));
      } else {
         terrainHeight = TerrainUtilities.barycentric(new Vector3D.Float(1, heightArray[gridX + 1][gridZ], 0),
            new Vector3D.Float(1, heightArray[gridX + 1][gridZ + 1], 1),
            new Vector3D.Float(0, heightArray[gridX][gridZ + 1], 1),
            new Vector3D.Float(xCoord, zCoord));
      }

      return terrainHeight;
   }

   /**
    * Returns the DefaultTerrain object from a DefaultTerrain map that the player is currently standing on
    *
    * @param terrainTree
    * @param worldX
    * @param worldZ
    * @return terrain object that the player is standing on
    */
   public static DefaultTerrain getCurrentTerrain(TreeMap<String, DefaultTerrain> terrainTree, float worldX, float worldZ) {
      // Floor divide player's absolute (world) x and z coordinates to get the grid indices that this terrain object lies in
      int xGrid = Math.floorDiv((int) worldX, (int) DefaultTerrain.SIZE);
      int zGrid = Math.floorDiv((int) worldZ, (int) DefaultTerrain.SIZE);

      // "xGrid-zGrid" comprises each key to the map of terrains
      return terrainTree.get(xGrid + "-" + zGrid);
   }

   public float getX() {
      return x;
   }

   public float getZ() {
      return z;
   }

   public static float getSize() {
      return SIZE;
   }

   public static float getMaxHeight() {
      return MAX_HEIGHT;
   }

   /**
    * @return the magnitude of the distance from the center of the {@link DefaultTerrain} object's absolute postion
    * to the {@link Ownship} objects absolute postion
    */
   public float getDistanceFromAircraft() {
      float terrainMidpointX = x + (MAX_HEIGHT / 2);
      float terrainMidpointZ = z + (MAX_HEIGHT / 2);

      return (float) Math.sqrt(Math.pow((ownship.getX() - terrainMidpointX), 2)
         + Math.pow((ownship.getZ() - terrainMidpointZ), 2));
   }

   /**
    * Compares using absolute distance between this {@link DefaultTerrain} and the {@link Ownship} versus another {@link DefaultTerrain}
    *
    * @param terrain
    * @return 1 if this is further away, -1 if this is closer, 0 if they are equal
    */
   @Override
   public int compareTo(DefaultTerrain terrain) {
      return this.getDistanceFromAircraft() > terrain.getDistanceFromAircraft() ? 1
         : this.getDistanceFromAircraft() < terrain.getDistanceFromAircraft() ? -1 : 0;
   }

}
