package se.llbit.math.primitive;

import se.llbit.chunky.model.Tint;
import se.llbit.chunky.world.Material;
import se.llbit.math.Vector2;
import se.llbit.math.Vector3;

public class TintedTexturedTriangle extends TexturedTriangle{
  private final Tint tint;
  public TintedTexturedTriangle(Vector3 c1, Vector3 c2, Vector3 c3, Vector2 t1, Vector2 t2, Vector2 t3, Material material, Tint tint) {
    super(c1, c2, c3, t1, t2, t3, material);
    this.tint=tint;
  }

  public TintedTexturedTriangle(Vector3 c1, Vector3 c2, Vector3 c3, Vector2 t1, Vector2 t2, Vector2 t3, Material material, Tint tint, boolean doubleSided) {
    super(c1, c2, c3, t1, t2, t3, material, doubleSided);
    this.tint=tint;
  }
  public Tint getTint(){
    return tint;
  }
}
