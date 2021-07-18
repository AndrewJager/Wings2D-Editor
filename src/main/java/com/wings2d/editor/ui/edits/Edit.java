package com.wings2d.editor.ui.edits;

import javax.swing.JOptionPane;

public abstract class Edit {
	private boolean actionDone;
	
	public Edit() {
		actionDone = false;
	}
	
	public abstract void edit() throws ActionNotDoneException;
	public abstract void undo() throws ActionNotDoneException;
	
	public boolean doEdit() {
		try {
			edit();
			actionDone = true;
		}
		catch (ActionNotDoneException e) {
			if (e.getShouldWarn()) {
				JOptionPane.showMessageDialog (null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		return actionDone;
	}
	public boolean undoEdit() {
		if (actionDone) {
			try {
				undo();
				actionDone = true;
			}
			catch (ActionNotDoneException e) {
				if (e.getShouldWarn()) {
					JOptionPane.showMessageDialog (null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		return actionDone;
	}
}
