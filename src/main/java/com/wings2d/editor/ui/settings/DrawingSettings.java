package com.wings2d.editor.ui.settings;

import javax.swing.BoxLayout;

import com.wings2d.editor.objects.EditorSettings;
import com.wings2d.editor.ui.UIElement;
import com.wings2d.editor.ui.setters.ColorSetter;
import com.wings2d.editor.ui.setters.IntSetter;

public class DrawingSettings extends UIElement<SettingsEdit>{

	public DrawingSettings(final SettingsEdit edit, final EditorSettings settings) {
		super(edit);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		IntSetter<SettingsEdit> handleSize = new IntSetter<SettingsEdit>(edit, "Handle Size:",
				settings::getHandleSize, settings::setHandleSize);
		panel.add(handleSize.getPanel());
		
		IntSetter<SettingsEdit> posHandleOffset = new IntSetter<SettingsEdit>(edit, 
				"Position Edit Handle Offset:          ", // Temp hack to get space between label and control
				settings::getPosHandleOffset, settings::setPosHandleOffset);
		panel.add(posHandleOffset.getPanel());
		
		IntSetter<SettingsEdit> rotHandleOffset = new IntSetter<SettingsEdit>(edit, "Rotation Edit Handle Offset:",
				settings::getRotHandleOffset, settings::setRotHandleOffset);
		panel.add(rotHandleOffset.getPanel());
		IntSetter<SettingsEdit> defaultTime = new IntSetter<SettingsEdit>(edit, "Default Frame Time:",
				settings::getDefaultTime, settings::setDefaultTime, 10);
		panel.add(defaultTime.getPanel());
		
		ColorSetter selectedHandleSettings = new ColorSetter(edit, "Selected Handle Color:", 
				settings::getSelectedHandleColor, settings::setSelectedHandleColor);
		panel.add(selectedHandleSettings.getPanel());
		ColorSetter unselectedHandleSettings = new ColorSetter(edit, "Unselected Handle Color:", 
				settings::getUnselectedHandleColor, settings::setUnselectedHandleColor);
		panel.add(unselectedHandleSettings.getPanel());

		for (Hotkeys key: Hotkeys.values()) { 
			panel.add(new BindingEdit(key.name(), settings.getKeyBinds().get(key.name())));
		}
	}

	@Override
	public void createEvents() {

	}
}
