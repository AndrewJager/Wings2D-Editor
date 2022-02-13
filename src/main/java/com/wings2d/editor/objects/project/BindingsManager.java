package com.wings2d.editor.objects.project;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.wings2d.framework.misc.KeyBind;

public class BindingsManager {
	@SuppressWarnings("serial")
	class KeyUp extends AbstractAction 
	{
	    private Integer key;
	    private List<Integer> pressedKeys;

	    public KeyUp(final Integer key, final List<Integer> keys)
	    {
	        this.key = key;
	        this.pressedKeys = keys;
	    }

	    @Override
	    public void actionPerformed(ActionEvent e) {
	        pressedKeys.remove(key);
	    }
	};
	@SuppressWarnings("serial")
	class KeyDown extends AbstractAction 
	{
	    private Integer key;
	    private List<Integer> pressedKeys;

	    public KeyDown(final Integer key, final List<Integer> keys)
	    {
	        this.key = key;
	        this.pressedKeys = keys;
	    }

	    @Override
	    public void actionPerformed(ActionEvent e) {
	    	pressedKeys.add(key);
	    	parseEvents();
	    }
	};
	
	private List<Integer> mappedKeys;
	private List<Integer> pressedKeys;
	private HashMap<String, EditorKeyBind> keyBinds;
	private HashMap<String, Runnable> actions;
	
	public BindingsManager(final HashMap<String, EditorKeyBind> keyBinds, final HashMap<String, Runnable> actions,
			final JPanel mainPanel) {
		mappedKeys = new ArrayList<Integer>();
		pressedKeys = new ArrayList<Integer>();
		this.keyBinds = keyBinds;
		this.actions = actions;
		
		for (EditorKeyBind keyBind : keyBinds.values()) {
			String[] keys = keyBind.getKeys().split(KeyBind.DELIMITER);
			for (int i = 0; i < keys.length; i++) {
				Integer keyCode = Integer.parseInt(keys[i]);
				if (!mappedKeys.contains(keyCode)) {
					mappedKeys.add(keyCode);
				}
			}
		}
		actions.get("Undo").run();
		
		for (int i = 0; i < mappedKeys.size(); i++) {
			Integer keyCode = mappedKeys.get(i);
			mainPanel.getInputMap().put(KeyStroke.getKeyStroke(keyCode, InputEvent.CTRL_DOWN_MASK
					, false), keyCode.toString() + "DOWN");
			mainPanel.getActionMap().put(keyCode.toString() + "DOWN", new KeyDown(keyCode, pressedKeys));
			mainPanel.getInputMap().put(KeyStroke.getKeyStroke(keyCode, 0, true), keyCode.toString() + "UP");
			mainPanel.getActionMap().put(keyCode.toString() + "UP", new KeyUp(keyCode, pressedKeys));
		}
	}
	
	public void parseEvents() {
		for (EditorKeyBind keyBind : keyBinds.values()) {
			boolean fireEvent = true;
			String[] keys = keyBind.getKeys().split(KeyBind.DELIMITER);
			for (int i = 0; i < keys.length; i++) {
				if (!pressedKeys.contains(Integer.parseInt(keys[i]))) {
					fireEvent = false;
					break;
				}
			}
			if (fireEvent) {
				actions.get(keyBind.getName()).run();
			}
		}
	}
}
