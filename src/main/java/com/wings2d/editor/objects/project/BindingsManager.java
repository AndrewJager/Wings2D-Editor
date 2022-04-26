package com.wings2d.editor.objects.project;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class BindingsManager {
	private record KeyBind(int keycode, int modifier, String name) {}
	
	@SuppressWarnings("serial")
	class KeyAction extends AbstractAction 
	{
	    private Runnable r;

	    public KeyAction(final Runnable r)
	    {
	        this.r = r;
	    }

	    @Override
	    public void actionPerformed(ActionEvent e) {
	    	r.run();
	    }
	};
	
	
	public BindingsManager(final HashMap<String, EditorKeyBind> keyBinds, final HashMap<String, Runnable> actions,
			final JPanel mainPanel) {
		bindHotkeys(keyBinds, actions, mainPanel);
	}
	
	public void bindHotkeys(final HashMap<String, EditorKeyBind> keyBinds, final HashMap<String, Runnable> actions,
			final JPanel mainPanel) {
		mainPanel.getInputMap().clear();
		mainPanel.getActionMap().clear();
		
		List<KeyBind> mappedKeys = new ArrayList<KeyBind>();
		
		for (EditorKeyBind keyBind : keyBinds.values()) {
			if (keyBind.getCtrl()) {
				mappedKeys.add(new KeyBind(keyBind.getKeyCode(), InputEvent.CTRL_DOWN_MASK, keyBind.getName()));
			}
			else if (keyBind.getShift()) {
				mappedKeys.add(new KeyBind(keyBind.getKeyCode(), InputEvent.SHIFT_DOWN_MASK, keyBind.getName()));
			}
			else if (keyBind.getAlt()) {
				mappedKeys.add(new KeyBind(keyBind.getKeyCode(), InputEvent.ALT_DOWN_MASK, keyBind.getName()));
			}
			else {
				mappedKeys.add(new KeyBind(keyBind.getKeyCode(), 0, keyBind.getName()));
			}
			
		}
		
		for (int i = 0; i < mappedKeys.size(); i++) {
			KeyBind k = mappedKeys.get(i);
			mainPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(k.keycode, k.modifier), k.name);
			mainPanel.getActionMap().put(k.name, new KeyAction(actions.get(k.name)));
		}
	}
}
