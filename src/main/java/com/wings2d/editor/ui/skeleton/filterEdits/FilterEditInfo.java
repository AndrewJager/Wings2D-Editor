package com.wings2d.editor.ui.skeleton.filterEdits;

import com.wings2d.framework.imageFilters.ImageFilter;

public interface FilterEditInfo {
	public abstract Class<? extends ImageFilter> getFilterClass();
}
