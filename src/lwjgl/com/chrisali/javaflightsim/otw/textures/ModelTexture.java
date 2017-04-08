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
package com.chrisali.javaflightsim.otw.textures;

public class ModelTexture {
   private int textureID;

   private float shineDamper = 1;
   private float reflectivity = 0;

   private boolean hasTransparency = false;
   private boolean useFakeLighting = false;

   private int numberOfAtlasRows = 1;

   public ModelTexture(int id) {
      this.textureID = id;
   }

   public int getNumberOfAtlasRows() {
      return numberOfAtlasRows;
   }

   public int getTextureID() {
      return textureID;
   }

   public float getShineDamper() {
      return shineDamper;
   }

   public boolean isHasTransparency() {
      return hasTransparency;
   }

   public boolean isUseFakeLighting() {
      return useFakeLighting;
   }

   public void setNumberOfAtlasRows(int numberOfAtlasRows) {
      this.numberOfAtlasRows = numberOfAtlasRows;
   }

   public void setShineDamper(float shineDamper) {
      this.shineDamper = shineDamper;
   }

   public float getReflectivity() {
      return reflectivity;
   }

   public void setReflectivity(float reflectivity) {
      this.reflectivity = reflectivity;
   }

   public void setHasTransparency(boolean hasTransparency) {
      this.hasTransparency = hasTransparency;
   }

   public void setUseFakeLighting(boolean useFakeLighting) {
      this.useFakeLighting = useFakeLighting;
   }
}
