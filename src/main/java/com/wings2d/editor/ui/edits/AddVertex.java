package com.wings2d.editor.ui.edits;

import java.awt.geom.Point2D;

import com.wings2d.editor.objects.skeleton.Sprite;

public class AddVertex extends Edit{
	private Sprite sprite;
	private Point2D point;
	
	public AddVertex(final Sprite sprite, final Point2D point) {
		this.sprite = sprite;
		this.point = point;
	}

	@Override
	public void edit() throws ActionNotDoneException {
		sprite.addVertex(point);
	}

	@Override
	public void undo() throws ActionNotDoneException {
		sprite.remove(sprite.getChildCount() - 1);
	}

	@Override
	public String getDescription() {
		return null;
	}
}
