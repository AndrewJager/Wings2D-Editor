package com.wings2d.editor.ui.edits;

public class ActionNotDoneException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private boolean shouldWarn;

	public ActionNotDoneException(final String msg, final boolean warnUser) {
		super(msg);
		shouldWarn = warnUser;
	}
	
	public boolean getShouldWarn() {
		return shouldWarn;
	}
}
