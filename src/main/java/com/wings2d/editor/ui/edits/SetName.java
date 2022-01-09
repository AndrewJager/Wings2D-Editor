package com.wings2d.editor.ui.edits;

import com.wings2d.editor.objects.skeleton.DBObject;

/**
 * Calls {@link DBObject#setName} with the given node
 */
public class SetName extends Edit{
	private DBObject node;
	private String oldName, newName;

	/**
	 * Rename the node to the new name
	 * @param node The node to be renamed
	 * @param newName Name to change to
	 * @param oldName Name being changed from. Used when undoing the edit
	 */
	public SetName(final DBObject node, final String newName, final String oldName) {
		super();
		this.node = node;
		this.newName = newName;
		this.oldName = oldName;
	}
	/**
	 * Rename the node to the new name. Uses the getName() function of the node for the old name.
	 * @param node The node to be renamed
	 * @param newName Name to change to
	 */
	public SetName(final DBObject node, final String newName) {
		this(node, newName, node.getName());
	}
	
	@Override
	public void edit() throws ActionNotDoneException {
		node.setName(newName);
	}

	@Override
	public void undo() throws ActionNotDoneException {
		node.setName(oldName);
	}

	@Override
	public String getDescription() {
		return "Change name of " + oldName + " to " + newName;
	}

}
