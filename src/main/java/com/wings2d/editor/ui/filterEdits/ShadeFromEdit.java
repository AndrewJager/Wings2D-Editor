package com.wings2d.editor.ui.filterEdits;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.wings2d.framework.imageFilters.DarkenFrom;
import com.wings2d.framework.imageFilters.LightenFrom;
import com.wings2d.framework.imageFilters.ShadeDir;
import com.wings2d.framework.imageFilters.ShadeFrom;


public class ShadeFromEdit {
	private ShadeDir dir;
	private double amt;
	
	private JDialog dialog;
	private JButton okBtn;
	private JComboBox<ShadeDir> directionSelect;
	private SpinnerModel spinModel;
	private JSpinner filterAmt;
	
	public ShadeFromEdit(ShadeFrom initShade)
	{
		dir = initShade.getDirection();
		amt = initShade.getAmt();
		ShadeDir[] directions = new ShadeDir[4];
		for (int i = 0; i < ShadeDir.values().length; i++) 
		{
			directions[i] = ShadeDir.values()[i];
		}
		
		dialog = new JDialog();
		dialog.setSize(300, 200);
		dialog.setLocationRelativeTo(null);
		
		directionSelect = new JComboBox<ShadeDir>(directions);
		directionSelect.setSelectedItem(dir);
		dialog.add(directionSelect);
		
		spinModel = new SpinnerNumberModel(amt, 0.001, 100, 0.05);
		filterAmt = new JSpinner(spinModel);
		filterAmt.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				amt = (double)filterAmt.getValue();
			}
		});
		dialog.add(filterAmt);
		
		okBtn = new JButton("Ok");
	    okBtn.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		dir = (ShadeDir) directionSelect.getSelectedItem();
	    		
	    		dialog.setVisible(false);
	    		dialog.dispose();
	    	}
	    });
		dialog.add(okBtn);
		
		dialog.setLayout(new FlowLayout());
		dialog.setModal(true);
		dialog.setVisible(true);
	}

	public DarkenFrom getDarken()
	{
		return new DarkenFrom(dir, amt);
	}
	public LightenFrom getLighten()
	{
		return new LightenFrom(dir, amt);
	}
}
