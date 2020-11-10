package com.wings2d.editor.ui.skeleton.filterEdits;

import java.awt.Frame;

import javax.swing.JSpinner;

import com.wings2d.framework.imageFilters.BasicVariance;

public class BasicVarianceEdit extends FilterEdit{		
	private JSpinner spinner;
	
	public BasicVarianceEdit(final Frame owner)
	{
		super(owner);
		
		spinner = new JSpinner();
		spinner.setValue(25);
		this.add(spinner);
		
		addButtons();
	}

	@Override
	public BasicVariance getFilter() {
		return new BasicVariance((int)spinner.getValue());
	}
}