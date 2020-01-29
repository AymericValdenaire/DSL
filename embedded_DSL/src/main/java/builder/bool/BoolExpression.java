package builder.bool;

import builder.BrickSetterBuilder;
import builder.StateBuilder;
import kernel.model.Brick;
import lombok.AccessLevel;
import lombok.Setter;
import org.graalvm.compiler.asm.sparc.SPARCAssembler.Br;

/**
 * Permet de créer des expressions booléennes
 */
public class BoolExpression {

  StateBuilder stateBuilder;
  String brickName;
  String value;

  @Setter(AccessLevel.PROTECTED)
  BrickSetterBuilder action;

  public BoolExpression(StateBuilder stateBuilder) {
    this.stateBuilder = stateBuilder;
  }

  /**
   * Permet de définir la condition
   * @param brickName String
   * @param value int
   * @return BoolSetterBuilder
   */
  public BoolSetterBuilder ifIsEqual(String brickName, String value) {
    this.brickName = brickName;
    this.value = value;
    return new BoolSetterBuilder(this);
  }
}
