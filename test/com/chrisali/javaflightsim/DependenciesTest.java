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
package com.chrisali.javaflightsim;

import static org.junit.Assert.assertFalse;
import java.io.File;
import org.jdepend.model.JarDependencies;
import org.jdepend.utils.DependencyPrinter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test jar dependencies.
 *
 * @since 0.2
 */
public class DependenciesTest {
   private static final String CORE = "j6dof-core.jar";
   private static final String LWJGL = "j6dof-lwjgl.jar";
   private static JarDependencies coreDepend = null;
   private static JarDependencies lwjglDepend = null;

   public DependenciesTest() {
   }

   @BeforeClass
   public static void setUpClass() throws Exception {
      // get the path of the dist directory
      File userDir = new File(System.getProperty("user.dir"));
      File dist = new File(userDir, "dist");

      // get the path of the lib directory
      File lib = new File(userDir, "lib");

      // get the path of the JavaFX and Swing framework jar files
      File jarFile = new File(dist, CORE);
      coreDepend = new JarDependencies();
      coreDepend.setFile(jarFile);
      jarFile = new File(dist, LWJGL);
      lwjglDepend = new JarDependencies();
      lwjglDepend.setFile(jarFile);
   }

   @AfterClass
   public static void tearDownClass() {
      coreDepend = null;
      lwjglDepend = null;
   }

   @Before
   public void setUp() {
   }

   @After
   public void tearDown() {
   }

   /**
    * Test that the core jar file don't depend on itext or the pdfWriter.
    */
   @Test
   public void testDependenciesOnLWJGL() {
      System.out.println("DependenciesTest : testDependenciesOnLWJGL");
      boolean isDepending = coreDepend.isDependingOn(lwjglDepend);
      DependencyPrinter.printDependencies(coreDepend, lwjglDepend);
      assertFalse(CORE + " must not depend on " + LWJGL, isDepending);
   }
}
