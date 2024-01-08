package frc.thunder.command.core;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;


/**
 * Command to run a command for a set time.
 */
public class TimedCommand extends Command {

	protected Command command;
	protected Timer timer;
	protected double timeout;

	/**
	 * A command that runs with a timeout.
	 * 
	 * @param command the command to be run
	 * @param timeout the timeout in seconds
	 */
	public TimedCommand(Command command, double timeout) {
		timer = new Timer();
		this.command = command;
		this.timeout = timeout;
	}

	@Override
	public void initialize() {
		timer.reset();
		timer.start();
		command.initialize();
	}

	@Override
	public void execute() {
		command.execute();
	}

	@Override
	public void end(boolean interrupted) {
		command.end(interrupted);
	}

	@Override
	public boolean isFinished() {
		return timer.hasElapsed(timeout) || command.isFinished();
	}

}
