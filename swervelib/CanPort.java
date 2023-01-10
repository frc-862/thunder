package frc.thunder.swervelib;

import java.util.Objects;

/** Add your docs here. */
public class CanPort {
	public final int id;
	public final String busName;

	public CanPort(int id, String busName) {
		super();
		this.id = id;
		this.busName = busName;
	}

	public CanPort(int id) {
		this(id, "");
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		CanPort otherPort = (CanPort) obj;
		return this.busName.equals(otherPort.busName) && this.id == otherPort.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.busName, this.id);
	}

	@Override
	public String toString() {
		return "Canbus:" + busName + ", CAN ID:" + id;
	}
}
