package kernel.logic.statements.action;

import kernel.logic.statements.Statement;

public abstract class Action extends Statement {
  // no setup code for an action
  @Override
  public String generateSetupCode() {
    return "";
  }
}
