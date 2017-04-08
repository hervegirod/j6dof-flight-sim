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
package com.chrisali.javaflightsim.utilities;

/**
 * A Point3D class which does not depend on LWJGL.
 *
 * @author Herve Girod
 * @since 0.2
 */
public class Point3D {
   private double x = 0;
   private double y = 0;
   private double z = 0;

   /**
    * Constructor.
    */
   public Point3D() {
   }

   /**
    * Constructor.
    *
    * @param x the X coordinate
    * @param y the Y coordinate
    * @param z the Z coordinate
    */
   public Point3D(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   /**
    * Set the position.
    *
    * @param x the X coordinate
    * @param y the Y coordinate
    * @param z the Z coordinate
    */
   public void setPosition(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   /**
    * Return the x coordinate.
    *
    * @return the x coordinate
    */
   public double getX() {
      return x;
   }

   /**
    * Return the y coordinate.
    *
    * @return the y coordinate
    */
   public double getY() {
      return y;
   }

   /**
    * Return the z coordinate.
    *
    * @return the z coordinate
    */
   public double getZ() {
      return z;
   }
}
