package editor;

public class Options {
	private Boolean cascadeChanges;
	private Boolean editing;
	private Boolean rotating;
	private Boolean scaling;
	
	public Options()
	{
		cascadeChanges = true;
		editing = false;
		rotating = false;
		scaling = false;
	}

	public Boolean getCascadeChanges() {
		return cascadeChanges;
	}

	public void setCascadeChanges(Boolean cascadeChanges) {
		this.cascadeChanges = cascadeChanges;
	}

	public Boolean getEditing() {
		return editing;
	}

	public void setEditing(Boolean editing) {
		this.editing = editing;
	}

	public Boolean getRotating() {
		return rotating;
	}

	public void setRotating(Boolean rotating) {
		this.rotating = rotating;
	}

	public Boolean getScaling() {
		return scaling;
	}

	public void setScaling(Boolean scaling) {
		this.scaling = scaling;
	}
}
