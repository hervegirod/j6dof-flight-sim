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
package com.chrisali.javaflightsim.controls.hidcontrollers;

import com.chrisali.javaflightsim.simulation.aero.Aerodynamics;
import com.chrisali.javaflightsim.simulation.controls.AbstractController;
import com.chrisali.javaflightsim.simulation.integration.Integrate6DOFEquations;
import java.util.ArrayList;
import net.java.games.input.Controller;

/**
 * Contains methods implemented {@link Joystick}, {@link Keyboard} and {@link Mouse} to take controller inputs
 * given by JInput, and convert them to actual control deflections used by the simulation in {@link Integrate6DOFEquations}
 * and {@link Aerodynamics}.
 *
 * @author Herve Girod
 * @version 0.8
 */
public abstract class AbstractPhysicalController extends AbstractController {
   protected ArrayList<Controller> controllerList;

   protected abstract void searchForControllers();
}