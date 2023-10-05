package se.llbit.chunky.block.jsonmodels;

import se.llbit.chunky.model.Tint;
import se.llbit.chunky.world.Material;
import se.llbit.math.Quad;
import se.llbit.math.Transform;
import se.llbit.math.Vector2;
import se.llbit.math.Vector3;
import se.llbit.math.primitive.Primitive;
import se.llbit.math.primitive.TintedTexturedTriangle;

import java.util.Collection;

public class TintedQuad extends Quad {

  public final Tint tint;

  public TintedQuad(Quad quad, Tint tint) {
    super(quad, Transform.NONE);
    this.tint = tint;
  }

  @Override
  public void addTriangles(Collection<Primitive> primitives, Material material, Transform transform) {
    Vector3 c0 = new Vector3(o);
    Vector3 c1 = new Vector3();
    Vector3 c2 = new Vector3();
    Vector3 c3 = new Vector3();
    c1.add(o, xv);
    c2.add(o, yv);
    c3.add(c1, yv);
    transform.apply(c0);
    transform.apply(c1);
    transform.apply(c2);
    transform.apply(c3);
    double u0 = uv.x;
    double u1 = uv.x + uv.y;
    double v0 = uv.z;
    double v1 = uv.z + uv.w;

    if (textureRotation == 90) {
      primitives.add(
        new TintedTexturedTriangle(
          c0, c2, c1, new Vector2(u1, v0), new Vector2(u0, v0), new Vector2(u1, v1), material, tint, doubleSided));
      primitives.add(
        new TintedTexturedTriangle(
          c1, c2, c3, new Vector2(u1, v1), new Vector2(u0, v0), new Vector2(u0, v1), material, tint, doubleSided));
    } else if (textureRotation == 180) {
      primitives.add(
        new TintedTexturedTriangle(
          c0, c2, c1, new Vector2(u1, v1), new Vector2(u1, v0), new Vector2(u0, v1), material, tint, doubleSided));
      primitives.add(
        new TintedTexturedTriangle(
          c1, c2, c3, new Vector2(u0, v1), new Vector2(u1, v0), new Vector2(u0, v0), material, tint, doubleSided));
    } else if (textureRotation == 270) {
      primitives.add(
        new TintedTexturedTriangle(
          c0, c2, c1, new Vector2(u0, v1), new Vector2(u1, v1), new Vector2(u0, v0), material, tint, doubleSided));
      primitives.add(
        new TintedTexturedTriangle(
          c1, c2, c3, new Vector2(u0, v0), new Vector2(u1, v1), new Vector2(u1, v0), material, tint, doubleSided));
    } else {
      primitives.add(
        new TintedTexturedTriangle(
          c0, c2, c1, new Vector2(u0, v0), new Vector2(u0, v1), new Vector2(u1, v0), material, tint, doubleSided));
      primitives.add(
        new TintedTexturedTriangle(
          c1, c2, c3, new Vector2(u1, v0), new Vector2(u0, v1), new Vector2(u1, v1), material, tint, doubleSided));
    }
  }
}
