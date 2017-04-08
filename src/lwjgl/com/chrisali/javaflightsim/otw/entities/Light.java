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

import org.lwjgl.util.vector.Vector3f;

public class Light {
   private Vector3f position;
   private Vector3f color;
   private Vector3f attenuation = new Vector3f(1, 0, 0);

   public Light(Vector3f position, Vector3f color) {
      this.position = position;
      this.color = color;
   }

   public Light(Vector3f position, Vector3f color, Vector3f attenuation) {
      this(position, color);
      this.attenuation = attenuation;
   }

   public Vector3f getAttenuation() {
      return attenuation;
   }

   public Vector3f getPosition() {
      return position;
   }

   public void setPosition(Vector3f position) {
      this.position = position;
   }

   public Vector3f getColor() {
      return color;
   }

   public void setColor(Vector3f color) {
      this.color = color;
   }
}
