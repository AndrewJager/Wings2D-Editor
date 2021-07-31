package com.wings2d.editor.ui.settings;

import javax.swing.BoxLayout;

import com.wings2d.editor.objects.EditorSettings;
import com.wings2d.editor.ui.UIElement;

public class DrawingSettings extends UIElement<SettingsEdit>{

	public DrawingSettings(final SettingsEdit edit, final EditorSettings settings) {
		super(edit);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		IntSetter handleSize = new IntSetter(edit, "Handle Size:",
				settings::getHandleSize, settings::setHandleSize);
		panel.add(handleSize.getPanel());
		
		IntSetter posHandleOffset = new IntSetter(edit, "Position Edit Handle Offset:          ", // Temp hack to get space between label and control
				settings::getPosHandleOffset, settings::setPosHandleOffset);
		panel.add(posHandleOffset.getPanel());
		
		IntSetter rotHandleOffset = new IntSetter(edit, "Rotation Edit Handle Offset:",
				settings::getRotHandleOffset, settings::setRotHandleOffset);
		panel.add(rotHandleOffset.getPanel());
		
		ColorSetter selectedHandleSettings = new ColorSetter(edit, "Selected Handle Color:", 
				settings::getSelectedHandleColor, settings::setSelectedHandleColor);
		panel.add(selectedHandleSettings.getPanel());
		ColorSetter unselectedHandleSettings = new ColorSetter(edit, "Unselected Handle Color:", 
				settings::getUnselectedHandleColor, settings::setUnselectedHandleColor);
		panel.add(unselectedHandleSettings.getPanel());
	}

	@Override
	public void createEvents() {

	}
}
