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

package se.llbit.chunky.model;

import se.llbit.chunky.block.jsonmodels.TintedQuad;
import se.llbit.chunky.plugin.PluginApi;
import se.llbit.chunky.renderer.scene.Scene;
import se.llbit.chunky.resources.Texture;
import se.llbit.math.Quad;
import se.llbit.math.Ray;
import se.llbit.math.Vector3;
import se.llbit.math.Vector4;

import java.util.Random;

/**
 * A block model that is made out of textured quads.
 */
@PluginApi
public interface QuadModel extends BlockModel {

  // For some visualizations, see this PR: https://github.com/chunky-dev/chunky/pull/1603
  public static final Quad FULL_BLOCK_NORTH_SIDE = new Quad(
    new Vector3(1, 0, 0),
    new Vector3(0, 0, 0),
    new Vector3(1, 1, 0),
    new Vector4(0, 1, 0, 1));
  public static final Quad FULL_BLOCK_SOUTH_SIDE = new Quad(
    new Vector3(0, 0, 1),
    new Vector3(1, 0, 1),
    new Vector3(0, 1, 1),
    new Vector4(0, 1, 0, 1));
  public static final Quad FULL_BLOCK_WEST_SIDE = new Quad(
    new Vector3(0, 0, 0),
    new Vector3(0, 0, 1),
    new Vector3(0, 1, 0),
    new Vector4(0, 1, 0, 1));
  public static final Quad FULL_BLOCK_EAST_SIDE = new Quad(
    new Vector3(1, 0, 1),
    new Vector3(1, 0, 0),
    new Vector3(1, 1, 1),
    new Vector4(0, 1, 0, 1));
  public static final Quad FULL_BLOCK_TOP_SIDE = new Quad(
    new Vector3(1, 1, 0),
    new Vector3(0, 1, 0),
    new Vector3(1, 1, 1),
    new Vector4(1, 0, 1, 0));
  public static final Quad FULL_BLOCK_BOTTOM_SIDE = new Quad(
    new Vector3(0, 0, 0),
    new Vector3(1, 0, 0),
    new Vector3(0, 0, 1),
    new Vector4(0, 1, 0, 1));

  public static final Quad[] FULL_BLOCK_QUADS = {
    FULL_BLOCK_NORTH_SIDE, FULL_BLOCK_SOUTH_SIDE,
    FULL_BLOCK_WEST_SIDE, FULL_BLOCK_EAST_SIDE,
    FULL_BLOCK_TOP_SIDE, FULL_BLOCK_BOTTOM_SIDE
  };

  // Epsilons to clip ray intersections to the current block.
  static final double E0 = -Ray.EPSILON;
  static final double E1 = 1 + Ray.EPSILON;

  @PluginApi
  Quad[] getQuads();

  @PluginApi
  Texture[] getTextures();

  @PluginApi
  default Tint[] getTints() {
    return null;
  }

  @Override
  default int faceCount() {
    return getQuads().length;
  }

  @Override
  default void sample(int face, Vector3 loc, Random rand) {
    getQuads()[face % faceCount()].sample(loc, rand);
  }

  @Override
  default double faceSurfaceArea(int face) {
    return getQuads()[face % faceCount()].surfaceArea();
  }

  @Override
  default boolean intersect(Ray ray, Scene scene) {
    boolean hit = false;
    ray.t = Double.POSITIVE_INFINITY;

    Quad[] quads = getQuads();
    Texture[] textures = getTextures();
    Tint[] tintedQuads = getTints();
    if(textures==null||quads==null) return false;

    float[] color = null;
    Tint tint = Tint.NONE;
    Quad hitQuad;
    for (int i = 0; i < quads.length; ++i) {
      Quad quad = quads[i];
      if (quad.intersect(ray)) {
        Texture texture = textures[i];
        if (texture == null) continue;
        float[] c = texture.getColor(ray.u, ray.v);
        if (c[3] > Ray.EPSILON) {
          tint = tintedQuads == null ? Tint.NONE : tintedQuads[i];
          if(quad instanceof TintedQuad){
            tint = ((TintedQuad)quad).tint;
          }
          color = c;
          ray.t = ray.tNext;
          if (quad.doubleSided) {
            ray.orientNormal(quad.n);
          } else {
            ray.setNormal(quad.n);
          }
          hit = true;
        }
      }
    }

    if (hit) {
      final double E0 = -Ray.EPSILON;
      final double E1 = 1 + Ray.EPSILON;

      double px = ray.o.x - Math.floor(ray.o.x + ray.d.x * Ray.OFFSET) + ray.d.x * ray.tNext;
      double py = ray.o.y - Math.floor(ray.o.y + ray.d.y * Ray.OFFSET) + ray.d.y * ray.tNext;
      double pz = ray.o.z - Math.floor(ray.o.z + ray.d.z * Ray.OFFSET) + ray.d.z * ray.tNext;
      if (px < E0 || px > E1 || py < E0 || py > E1 || pz < E0 || pz > E1) {
        // TODO this check is only really needed for wall torches
        return false;
      }

      ray.color.set(color);
      tint.tint(ray.color, ray, scene);
      ray.distance += ray.t;
      ray.o.scaleAdd(ray.t, ray.d);
    }
    return hit;
  }
}
