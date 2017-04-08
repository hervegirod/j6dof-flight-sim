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
package com.chrisali.javaflightsim.otw.renderengine;

import com.chrisali.javaflightsim.otw.interfaces.font.FontType;
import com.chrisali.javaflightsim.otw.interfaces.font.GUIText;
import com.chrisali.javaflightsim.otw.shaders.FontShader;
import java.util.List;
import java.util.Map;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class FontRenderer {

   private FontShader shader;

   public FontRenderer() {
      shader = new FontShader();
   }

   public void render(Map<FontType, List<GUIText>> texts) {
      prepare();

      for (FontType font : texts.keySet()) {
         GL13.glActiveTexture(GL13.GL_TEXTURE0);
         GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTextureAtlas());

         for (GUIText text : texts.get(font)) {
            renderText(text);
         }
      }

      endRendering();
   }

   public void cleanUp() {
      shader.cleanUp();
   }

   private void prepare() {
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      GL11.glDisable(GL11.GL_DEPTH_TEST);
      shader.start();
   }

   private void renderText(GUIText text) {
      GL30.glBindVertexArray(text.getMesh());
      GL20.glEnableVertexAttribArray(0);
      GL20.glEnableVertexAttribArray(1);

      shader.loadColor(text.getColor());
      shader.loadTranslation(text.getPosition());
      GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());

      GL20.glDisableVertexAttribArray(0);
      GL20.glDisableVertexAttribArray(1);
      GL30.glBindVertexArray(0);
   }

   private void endRendering() {
      shader.stop();
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glEnable(GL11.GL_DEPTH_TEST);
   }

}
