package com.wings2d.editor.ui.skeleton.filterEdits;

import java.awt.Frame;

import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import com.wings2d.framework.imageFilters.DarkenFrom;
import com.wings2d.framework.imageFilters.ImageFilter;
import com.wings2d.framework.imageFilters.LightenFrom;
import com.wings2d.framework.imageFilters.ShadeDir;


public abstract class ShadeFromEdit extends FilterEdit{
	public enum ShadeType{
		LIGHTEN,
		DARKEN
	}
	
	private ShadeType type;
	private ShadeDir dir;
	private double amt;
	
	private JComboBox<ShadeDir> directionSelect;
	private SpinnerModel spinModel;
	private JSpinner filterAmt;
	
	public ShadeFromEdit(final Frame owner, final ShadeType type)
	{
		super(owner);
		this.type = type;
		
		ShadeDir[] directions = new ShadeDir[4];
		for (int i = 0; i < ShadeDir.values().length; i++) 
		{
			directions[i] = ShadeDir.values()[i];
		}
		
		
		directionSelect = new JComboBox<ShadeDir>(directions);
		this.add(directionSelect);
		
		spinModel = new SpinnerNumberModel(1, 0.001, 100, 0.05);
		filterAmt = new JSpinner(spinModel);
		this.add(filterAmt);
		

	    addButtons();
	}

	public DarkenFrom getDarken()
	{
		dir = (ShadeDir) directionSelect.getSelectedItem();
		amt = (double) filterAmt.getValue();
		return new DarkenFrom(dir, amt);
	}
	public LightenFrom getLighten()
	{
		dir = (ShadeDir) directionSelect.getSelectedItem();
		amt = (double) filterAmt.getValue();
		return new LightenFrom(dir, amt);
	}
	
	@Override
	public ImageFilter getFilter() {
		switch(type)
		{
		case LIGHTEN:
			return getLighten();
		case DARKEN:
			return getDarken();
		default:
			return null;
		}
	}
}
