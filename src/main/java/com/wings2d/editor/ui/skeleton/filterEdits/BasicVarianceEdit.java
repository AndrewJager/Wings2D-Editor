package com.wings2d.editor.ui.skeleton.filterEdits;

import java.awt.Frame;

import javax.swing.JSpinner;

import com.wings2d.framework.imageFilters.BasicVariance;

public class BasicVarianceEdit extends FilterEdit{		
	private static final long serialVersionUID = 1L;
	
	private JSpinner spinner;
	
	public BasicVarianceEdit(final Frame owner)
	{
		super(owner);
		
		spinner = new JSpinner();
		spinner.setValue(25);
		this.add(spinner);
		
		addButtons();
	}
	
	public BasicVarianceEdit(final BasicVariance filter, final Frame owner) {
		this(owner);
		
		spinner.setValue(filter.getVarAmt());
	}

	@Override
	public BasicVariance getFilter() {
		return new BasicVariance((int)spinner.getValue());
	}
}