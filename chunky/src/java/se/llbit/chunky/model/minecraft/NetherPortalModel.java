/*
 * Copyright (c) 2023 Chunky contributors
 *
 * This file is part of Chunky.
 *
 * Chunky is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Chunky is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Chunky.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.llbit.chunky.model.minecraft;

import se.llbit.chunky.model.AbstractQuadModel;
import se.llbit.chunky.model.QuadModel;
import se.llbit.chunky.resources.Texture;
import se.llbit.math.Quad;
import se.llbit.math.Vector3;
import se.llbit.math.Vector4;

public class NetherPortalModel extends AbstractQuadModel {
  private final static Quad[] quadNS = {
      new Quad(
          new Vector3(16 / 16.0, 0, 6 / 16.0),
          new Vector3(0, 0, 6 / 16.0),
          new Vector3(16 / 16.0, 16 / 16.0, 6 / 16.0),
          new Vector4(0, 16 / 16.0, 0, 16 / 16.0),
          true
      )
  };

  private final static Quad[] quadEW = {
      new Quad(
          new Vector3(10 / 16.0, 0, 16 / 16.0),
          new Vector3(10 / 16.0, 0, 0),
          new Vector3(10 / 16.0, 16 / 16.0, 16 / 16.0),
          new Vector4(0, 16 / 16.0, 0, 16 / 16.0),
          true
      )
  };

  private final static Texture[] textures = { Texture.portal };

  public NetherPortalModel(String axis) {
    quads = axis.equals("z") ? quadEW : quadNS;
  }

  @Override
  public Quad[] getQuads() {
    return quads;
  }

  @Override
  public Texture[] getTextures() {
    return textures;
  }
}
