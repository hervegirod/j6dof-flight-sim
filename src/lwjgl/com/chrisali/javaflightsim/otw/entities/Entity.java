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
package com.chrisali.javaflightsim.otw.entities;

import com.chrisali.javaflightsim.otw.models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

public class Entity {
   private TexturedModel model;
   private Vector3f position;
   private float rotX, rotY, rotZ;
   private float scale;

   private int textureIndex = 0;

   public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
      this.model = model;
      this.position = position;
      this.rotX = rotX;
      this.rotY = rotY;
      this.rotZ = rotZ;
      this.scale = scale;
   }

   public Entity(TexturedModel model, int index, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
      this(model, position, rotX, rotY, rotZ, scale);
      this.textureIndex = index;
   }

   public void increasePosition(float dx, float dy, float dz) {
      this.position.x += dx;
      this.position.y += dy;
      this.position.z += dz;
   }

   public void increaseRotation(float dx, float dy, float dz) {
      this.rotX += dx;
      this.rotY += dy;
      this.rotZ += dz;
   }

   public float getTextureXOffset() {
      int column = textureIndex % model.getTexture().getNumberOfAtlasRows();
      return (float) column / (float) model.getTexture().getNumberOfAtlasRows();
   }

   public float getTextureYOffset() {
      int row = textureIndex / model.getTexture().getNumberOfAtlasRows();
      return (float) row / (float) model.getTexture().getNumberOfAtlasRows();
   }

   public TexturedModel getModel() {
      return model;
   }

   public void setModel(TexturedModel model) {
      this.model = model;
   }

   public Vector3f getPosition() {
      return position;
   }

   public void setPosition(Vector3f position) {
      this.position = position;
   }

   public float getRotX() {
      return rotX;
   }

   public void setRotX(float rotX) {
      this.rotX = rotX;
   }

   public float getRotY() {
      return rotY;
   }

   public void setRotY(float rotY) {
      this.rotY = rotY;
   }

   public float getRotZ() {
      return rotZ;
   }

   public void setRotZ(float rotZ) {
      this.rotZ = rotZ;
   }

   public float getScale() {
      return scale;
   }

   public void setScale(float scale) {
      this.scale = scale;
   }
}
