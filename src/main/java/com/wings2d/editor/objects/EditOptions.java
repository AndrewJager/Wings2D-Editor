package com.wings2d.editor.objects;

import com.wings2d.editor.ui.Editor;

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
	private Editor editor;
	
	/**
	 * Sets cascade changes to true, editing to false, rotating to false, scaling to false
	 * and edit circle size to 12
	 */
	public EditOptions(final Editor editor)
	{
		this.cascadeChanges = true;
		this.editing = false;
		this.rotating = false;
		this.editor = editor;
	}
	/** Should the changes cascade to child frames */
	public Boolean getCascadeChanges() {
		return cascadeChanges;
	}
	/** Set whether the changes should cascade to any child frames */
	public void setCascadeChanges(final Boolean cascadeChanges) {
		this.cascadeChanges = cascadeChanges;
	}
	/** Get if the frames's objects/joints can be edited, meaning that the vertices can be moved */
	public Boolean getEditing() {
		return editing;
	}
	/** Set if the frames's objects/joints can be edited, meaning that the vertices can be moved */
	public void setEditing(final Boolean editing) {
		this.editing = editing;
	}
	/** Get if the frame's objects/joints can be rotated */
	public Boolean getRotating() {
		return rotating;
	}
	/** Set if the frame's objects/joints can be rotated */
	public void setRotating(final Boolean rotating) {
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
	public Editor getEditor()
	{
		return editor;
	}
}