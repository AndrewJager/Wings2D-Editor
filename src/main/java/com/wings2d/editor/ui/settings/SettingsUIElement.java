package com.wings2d.editor.ui.settings;

import com.wings2d.editor.ui.UIElement;

public abstract class SettingsUIElement extends UIElement{
	protected SettingsEdit settingsEdit;

	public SettingsUIElement(final SettingsEdit edit) {
		super(edit);
		this.settingsEdit = edit;
	}
}
