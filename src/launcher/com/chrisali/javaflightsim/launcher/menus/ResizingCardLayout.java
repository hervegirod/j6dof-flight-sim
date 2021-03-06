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
package com.chrisali.javaflightsim.launcher.menus;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;

public class ResizingCardLayout extends CardLayout {

   private static final long serialVersionUID = 4014980659323192230L;

   @Override
   public Dimension preferredLayoutSize(Container parent) {
      Component current = findCurrentComponent(parent);

      if (current != null) {
         Insets insets = parent.getInsets();
         Dimension preferred = current.getPreferredSize();

         preferred.width += insets.left + insets.right;
         preferred.height += insets.top + insets.bottom;

         return preferred;
      }

      return super.preferredLayoutSize(parent);
   }

   public Component findCurrentComponent(Container parent) {
      for (Component comp : parent.getComponents()) {
         if (comp.isVisible()) {
            return comp;
         }
      }
      return null;
   }

}
