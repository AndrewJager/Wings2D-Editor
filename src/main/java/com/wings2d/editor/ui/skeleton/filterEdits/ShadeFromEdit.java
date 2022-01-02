package com.wings2d.editor.ui.skeleton.filterEdits;

import java.awt.Frame;

import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import com.wings2d.framework.imageFilters.DarkenFrom;
import com.wings2d.framework.imageFilters.ImageFilter;
import com.wings2d.framework.imageFilters.LightenFrom;
import com.wings2d.framework.misc.CardinalDir;


public abstract class ShadeFromEdit extends FilterEdit{
	private static final long serialVersionUID = 1L;

	public enum ShadeType{
		LIGHTEN,
		DARKEN
	}
	
	private ShadeType type;
	private CardinalDir dir;
	private double amt;
	
	private JComboBox<CardinalDir> directionSelect;
	private SpinnerModel spinModel;
	private JSpinner filterAmt;
	
	public ShadeFromEdit(final Frame owner, final ShadeType type)
	{
		super(owner);
		this.type = type;
		
		CardinalDir[] directions = new CardinalDir[4];
		for (int i = 0; i < CardinalDir.values().length; i++) 
		{
			directions[i] = CardinalDir.values()[i];
		}
		
		
		directionSelect = new JComboBox<CardinalDir>(directions);
		this.add(directionSelect);
		
		spinModel = new SpinnerNumberModel(1, 0.001, 100, 0.05);
		filterAmt = new JSpinner(spinModel);
		this.add(filterAmt);
		

	    addButtons();
	}
	
	public ShadeFromEdit(final ImageFilter filter, final Frame owner, final ShadeType type) {
		this(owner, type);
		if (filter instanceof DarkenFrom) {
			DarkenFrom darken = (DarkenFrom)filter;
			directionSelect.setSelectedItem(darken.getDirection());
			filterAmt.setValue(darken.getAmt());
			
		}
		else if (filter instanceof LightenFrom) {
			LightenFrom lighten = (LightenFrom)filter;
			directionSelect.setSelectedItem(lighten.getDirection());
			filterAmt.setValue(lighten.getAmt());
		}
	}

	public DarkenFrom getDarken()
	{
		dir = (CardinalDir) directionSelect.getSelectedItem();
		amt = (double) filterAmt.getValue();
		return new DarkenFrom(dir, amt);
	}
	public LightenFrom getLighten()
	{
		dir = (CardinalDir) directionSelect.getSelectedItem();
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
