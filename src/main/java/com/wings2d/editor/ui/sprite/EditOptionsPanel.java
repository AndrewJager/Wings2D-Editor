package com.wings2d.editor.ui.sprite;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.wings2d.editor.ui.Editor;

public class EditOptionsPanel extends SpriteUIElement{
	private JCheckBox cascade;
	private SpinnerModel spinModel;
	private JSpinner imgScale;
	private JCheckBox editing;
	private JCheckBox rotate;
	private JCheckBox scale;

	public EditOptionsPanel(Editor edit, Rectangle bounds)
	{
		super(edit, bounds);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		cascade = new JCheckBox("Cascade transforms");
		cascade.setSelected(editor.getOptions().getCascadeChanges());
		panel.add(cascade);
		spinModel = new SpinnerNumberModel(0.25, 0.001, 2, 0.05);
		imgScale = new JSpinner(spinModel);
		editor.getManager().setScale((double) imgScale.getValue());
		editing = new JCheckBox("Editing");
		editing.setSelected(editor.getOptions().getEditing());
		panel.add(editing);
		rotate = new JCheckBox("Rotation Handle");
		rotate.setSelected(editor.getOptions().getRotating());
		panel.add(rotate);
		scale = new JCheckBox("Scale Handles");
		scale.setSelected(editor.getOptions().getScaling());
		panel.add(scale);
		panel.add(imgScale);
		panel.setLayout(new GridLayout(3, 2));
	}
	
	public void createEvents()
	{
		AnimationLists ani = editor.getSpriteEdit().getAnimLists();
		editing.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (ani.getIsObjectSelected())
	    		{
	    			editor.getOptions().setEditing(editing.isSelected());
	    			editor.getSpriteEdit().getDrawing().setShouldRedraw(true);
	    		}
	    	}
	    });
		imgScale.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
            	editor.getManager().setScale((double) imgScale.getValue());
            }
        });
		cascade.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		editor.getOptions().setCascadeChanges(cascade.isSelected());
	    	}
	    });
	    rotate.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (ani.getIsObjectSelected())
	    		{
	    			editor.getOptions().setRotating(rotate.isSelected());
	    			editor.getSpriteEdit().getDrawing().setShouldRedraw(true);
	    		}
	    	}
	    });
	    scale.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (ani.getIsObjectSelected())
	    		{
	    			editor.getOptions().setScaling(scale.isSelected());
	    			editor.getSpriteEdit().getDrawing().setShouldRedraw(true);
	    		}
	    	}
	    });
	}
	
	public JCheckBox getEditing()
	{
		return editing;
	}
	public JCheckBox getCascade()
	{
		return cascade;
	}
	public JCheckBox getRotate()
	{
		return rotate;
	}
	public JCheckBox getScale()
	{
		return scale;
	}
}