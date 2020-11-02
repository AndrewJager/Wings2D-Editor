package com.wings2d.editor.ui.skeleton.filterEdits;

import java.awt.Frame;

import javax.swing.JSpinner;

import com.wings2d.framework.imageFilters.BasicVariance;
import com.wings2d.framework.imageFilters.ImageFilter;

public class BasicVarianceEdit extends FilterEdit<BasicVariance>{
	public Class<? extends ImageFilter> FILTER_CLASS = BasicVariance.class;	
			
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