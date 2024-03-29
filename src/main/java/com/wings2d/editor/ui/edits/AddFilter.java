package com.wings2d.editor.ui.edits;

import com.wings2d.editor.objects.skeleton.path.Sprite;
import com.wings2d.framework.imageFilters.ImageFilter;

public class AddFilter extends Edit{
	private Sprite sprite;
	private ImageFilter filter;
	
	public AddFilter(final Sprite sprite, final ImageFilter filter) {
		super();
		this.sprite = sprite;
		this.filter = filter;
	}

	@Override
	public void edit() throws ActionNotDoneException {
		sprite.addFilter(filter);
	}

	@Override
	public void undo() throws ActionNotDoneException {
		sprite.removeFilter(filter);
	}

	@Override
	public String getDescription() {
		return "Add " + filter.toString() + " filter to " + sprite.getName();
	}
}
