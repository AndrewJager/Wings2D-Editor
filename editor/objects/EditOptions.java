package editor.objects;

/** Options used by editor. */
public class EditOptions {
	/** Should the changes cascade to child frames */
	private Boolean cascadeChanges;
	/** Should the object/joint have the edit vertex handles enabled */
	private Boolean editing;
	/** Should the object/joint have the rotation handle enabled */
	private Boolean rotating;
	/** Should the object/joint have the scale handles enabled */
	private Boolean scaling;
	/** Size of the circles for the edit handles */
	private int editHandleSize;
	
	/**
	 * Sets cascade changes to true, editing to false, rotating to false, scaling to false
	 * and edit circle size to 12
	 */
	public EditOptions()
	{
		cascadeChanges = true;
		editing = false;
		rotating = false;
		scaling = false;
		editHandleSize = 12;
	}
	/** Should the changes cascade to child frames */
	public Boolean getCascadeChanges() {
		return cascadeChanges;
	}
	/** Set whether the changes should cascade to any child frames */
	public void setCascadeChanges(Boolean cascadeChanges) {
		this.cascadeChanges = cascadeChanges;
	}
	/** Get if the frames's objects/joints can be edited, meaning that the vertices can be moved */
	public Boolean getEditing() {
		return editing;
	}
	/** Set if the frames's objects/joints can be edited, meaning that the vertices can be moved */
	public void setEditing(Boolean editing) {
		this.editing = editing;
	}
	/** Get if the frame's objects/joints can be rotated */
	public Boolean getRotating() {
		return rotating;
	}
	/** Set if the frame's objects/joints can be rotated */
	public void setRotating(Boolean rotating) {
		this.rotating = rotating;
	}
	/** Get if the frame's objects/joints can be scaled */
	public Boolean getScaling() {
		return scaling;
	}
	/** Set if the frame's objects/joints can be scaled */
	public void setScaling(Boolean scaling) {
		this.scaling = scaling;
	}
	/** Get the size of the edit handles */
	public int getEditHandleSize()
	{
		return editHandleSize;
	}
	/**
	 * Set the size of the edit handles
	 * @param size Size in graphics units of the edit handles
	 */
	public void setEditHandleSize(int size)
	{
		this.editHandleSize = size;
	}
}