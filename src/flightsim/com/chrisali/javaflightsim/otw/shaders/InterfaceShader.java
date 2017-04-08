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
package com.chrisali.javaflightsim.otw.shaders;

import org.lwjgl.util.vector.Matrix4f;

public class InterfaceShader extends ShaderProgram {

   private static final String VERTEX_FILE = SHADER_ROOT_PATH + "interfaceVertexShader.txt";
   private static final String FRAGMENT_FILE = SHADER_ROOT_PATH + "interfaceFragmentShader.txt";

   private int location_transformationMatrix;

   public InterfaceShader() {
      super(VERTEX_FILE, FRAGMENT_FILE);
   }

   public void loadTransformation(Matrix4f matrix) {
      super.loadMatrix(location_transformationMatrix, matrix);
   }

   @Override
   protected void getAllUniformLocations() {
      location_transformationMatrix = super.getUniformLocation("transformationMatrix");
   }

   @Override
   protected void bindAttributes() {
      super.bindAttribute(0, "position");
   }
}
