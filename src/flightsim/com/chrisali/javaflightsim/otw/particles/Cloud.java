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
package com.chrisali.javaflightsim.otw.particles;

import java.util.Random;
import org.lwjgl.util.vector.Vector3f;

public class Cloud extends Particle {

   private int textureIndex;
   private int textureIndices;

   public Cloud(ParticleTexture texture, Vector3f position, Vector3f velocity, float rotation, float scale) {
      super(texture, position, velocity, 0, Float.POSITIVE_INFINITY, rotation, scale);

      Random random = new Random();
      textureIndices = getTexture().getNumberOfAtlasRows() * getTexture().getNumberOfAtlasRows();
      textureIndex = random.nextInt(textureIndices - 1);
   }

   @Override
   protected void updateTextureCoordinateInfo() {
      int index1 = textureIndex;
      int index2 = index1 < textureIndices - 1 ? index1 + 1 : index1;

      this.textureBlend = 1.0f;

      setTextureOffset(getTextureOffset1(), index1);
      setTextureOffset(getTextureOffset2(), index2);
   }
}
