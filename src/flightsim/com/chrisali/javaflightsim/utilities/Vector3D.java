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
 * A Vector3D class which does not depend on LWJGL.
 *
 * @author Herve Girod
 * @since 0.2
 */
public abstract class Vector3D {

   /**
    * Constructor.
    */
   public Vector3D() {
   }

   /**
    * Set the position.
    *
    * @param x the X coordinate
    * @param y the Y coordinate
    * @param z the Z coordinate
    */
   public void setLocation(float x, float y, float z) {
      Vector3D.this.setLocation((double) x, (double) y, (double) z);
   }

   /**
    * Set the position.
    *
    * @param x the X coordinate
    * @param y the Y coordinate
    * @param z the Z coordinate
    */
   public abstract void setLocation(double x, double y, double z);

   /**
    * Return the x coordinate.
    *
    * @return the x coordinate
    */
   public abstract double getX();

   /**
    * Return the y coordinate.
    *
    * @return the y coordinate
    */
   public abstract double getY();

   /**
    * Return the z coordinate.
    *
    * @return the z coordinate
    */
   public abstract double getZ();

   /**
    * Return the x coordinate.
    *
    * @return the x coordinate
    */
   public float getXasFloat() {
      return (float) getX();
   }

   /**
    * Return the y coordinate.
    *
    * @return the y coordinate
    */
   public float getYasFloat() {
      return (float) getY();
   }

   /**
    * Return the z coordinate.
    *
    * @return the z coordinate
    */
   public float getZasFloat() {
      return (float) getZ();
   }

   /**
    * Normalise this Vector and place the result in another Vector.
    *
    * @param dest The destination Vector, or null if a new Vector is to be created
    * @return the normalised Vector
    */
   public abstract Vector3D normalize(Vector3D dest);

   /**
    * Normalise this Vector and place the result in another Vector.
    *
    * @return the normalised Vector
    */
   public Vector3D normalize() {
      return normalize(this);
   }

   public static class Double extends Vector3D {
      public double x = 0;
      public double y = 0;
      public double z = 0;

      /**
       * Constructor.
       */
      public Double() {
      }

      /**
       * Constructor.
       *
       * @param x the X coordinate
       * @param y the Y coordinate
       */
      public Double(double x, double y) {
         this.x = x;
         this.y = y;
      }

      /**
       * Constructor.
       *
       * @param x the X coordinate
       * @param y the Y coordinate
       * @param z the Z coordinate
       */
      public Double(double x, double y, double z) {
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
      @Override
      public void setLocation(double x, double y, double z) {
         this.x = x;
         this.y = y;
         this.z = z;
      }

      /**
       * Return the x coordinate.
       *
       * @return the x coordinate
       */
      @Override
      public double getX() {
         return x;
      }

      /**
       * Return the y coordinate.
       *
       * @return the y coordinate
       */
      @Override
      public double getY() {
         return y;
      }

      /**
       * Return the z coordinate.
       *
       * @return the z coordinate
       */
      @Override
      public double getZ() {
         return z;
      }

      /**
       * Normalise this Vector and place the result in another Vector.
       *
       * @param dest The destination Vector, or null if a new Vector is to be created
       * @return the normalised Vector
       */
      @Override
      public Vector3D normalize(Vector3D dest) {
         double l = x * x + y * y + z * z;
         l = (double) Math.sqrt(l);

         if (dest == null) {
            dest = new Vector3D.Double(x / l, y / l, z / l);
         } else {
            dest.setLocation(x / l, y / l, z / l);
         }

         return dest;
      }
   }

   public static class Float extends Vector3D {
      public float x = 0;
      public float y = 0;
      public float z = 0;

      /**
       * Constructor.
       */
      public Float() {
      }

      /**
       * Constructor.
       *
       * @param x the X coordinate
       * @param y the Y coordinate
       */
      public Float(float x, float y) {
         this.x = x;
         this.y = y;
      }

      /**
       * Constructor.
       *
       * @param x the X coordinate
       * @param y the Y coordinate
       * @param z the Z coordinate
       */
      public Float(float x, float y, float z) {
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
      @Override
      public void setLocation(float x, float y, float z) {
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
      @Override
      public void setLocation(double x, double y, double z) {
         this.x = (float) x;
         this.y = (float) y;
         this.z = (float) z;
      }

      /**
       * Return the x coordinate.
       *
       * @return the x coordinate
       */
      @Override
      public double getX() {
         return x;
      }

      /**
       * Return the y coordinate.
       *
       * @return the y coordinate
       */
      @Override
      public double getY() {
         return y;
      }

      /**
       * Return the z coordinate.
       *
       * @return the z coordinate
       */
      @Override
      public double getZ() {
         return z;
      }

      /**
       * Return the x coordinate.
       *
       * @return the x coordinate
       */
      @Override
      public float getXasFloat() {
         return x;
      }

      /**
       * Return the y coordinate.
       *
       * @return the y coordinate
       */
      @Override
      public float getYasFloat() {
         return y;
      }

      /**
       * Return the z coordinate.
       *
       * @return the z coordinate
       */
      @Override
      public float getZasFloat() {
         return z;
      }

      /**
       * Normalise this Vector and place the result in another Vector.
       *
       * @param dest The destination Vector, or null if a new Vector is to be created
       * @return the normalised Vector
       */
      @Override
      public Vector3D normalize(Vector3D dest) {
         float l = x * x + y * y + z * z;
         l = (float) Math.sqrt(l);

         if (dest == null) {
            dest = new Vector3D.Float(x / l, y / l, z / l);
         } else {
            dest.setLocation(x / l, y / l, z / l);
         }

         return dest;
      }
   }
}
