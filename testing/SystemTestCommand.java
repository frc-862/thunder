// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class SystemTestCommand extends Command {

  private Command test;

  public SystemTestCommand(Command test) {
    this.test = test;

    addRequirements(test.getRequirements().toArray(new Subsystem[0]));
  }

  private boolean canRun() {
    return DriverStation.isTest();
  }

  @Override
  public void initialize() {
    if (canRun()) {
      test.initialize();
    }
  }

  @Override
  public void execute() {
    if (canRun() && !test.isFinished()) {
      test.execute();
    }
  }

  @Override
  public void end(boolean interrupted) {
    if (canRun() && !test.isFinished()) {
      test.end(interrupted);
    }
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
