/*
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
package com.chrisali.javaflightsim.launcher;

import java.util.HashMap;
import java.util.Map;

/**
 * This class has only one static method which allows to return the launch arguments of a
 * main static method into a Map of &lt;key,value&gt;.
 *
 * @since 0.2
 */
public class LauncherUtils {
   private LauncherUtils() {
   }

   /**
    * Return the launch arguments of a main static method into a Map of &lt;key,value&gt;.
    * The arguments can have eone of the following pattern:
    * <pre>
    * key1 prop1 key2 prop2 ...
    * </pre>
    * or
    * <pre>
    * key1=prop1 key2=prop2 ...
    * </pre>
    * it is even possible to mix the two patterns such as:
    * <pre>
    * key1 prop1 key2=prop2 ...
    * </pre>
    *
    * if the value for the key is not set, then a (key, value) will be added with an empty String as value
    * <pre>
    * key2= -key3 -key4=value4
    * </pre>
    *
    * @param args the launch arguments
    * @return the key, value pairs corresponding to the arguments
    */
   public static Map<String, String> getLaunchProperties(String[] args) {
      Map<String, String> props = new HashMap<>();
      if ((args != null) && (args.length != 0)) {
         int i = 0;
         boolean toStore = false;
         String propKey = null;
         while (i < args.length) {
            String arg = args[i];
            if (toStore) {
               if (arg.charAt(0) != '-') {
                  toStore = false;
                  String propValue = arg;
                  props.put(propKey, propValue);
                  propKey = null;
               } else {
                  toStore = false;
                  props.put(propKey, "");
                  propKey = null;
               }
            } else if (arg.indexOf('=') != -1) {
               if (toStore) {
                  toStore = false;
                  props.put(propKey, "");
                  propKey = null;
               }
               String intKey;
               int eqIndex = arg.indexOf('=');
               if (eqIndex == -1) {
                  props.put(arg, "");
               } else {
                  intKey = arg.substring(0, eqIndex);
                  if (arg.indexOf('=') == arg.length() - 1) {
                     props.put(intKey, "");
                  } else {
                     String propValue = arg.substring(eqIndex + 1);
                     props.put(intKey, propValue);
                  }
               }
            } else if (arg.charAt(0) == '-') {
               if (toStore) {
                  toStore = false;
                  props.put(propKey, "");
                  propKey = null;
               }
               props.put(arg, "");
            } else if (arg.charAt(0) != '-') {
               propKey = arg;
               toStore = true;
            }
            i++;
         }
         if (propKey != null) {
            props.put(propKey, "");
         }
      }
      return props;
   }
}
