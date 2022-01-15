package com.wings2d.editor.ui.edits;

import java.awt.Point;

import com.wings2d.editor.objects.skeleton.Sprite;

public class MoveSprite extends Edit {
	private Sprite sprite;
	private double x, y, oldX, oldY, scale;
	
	public MoveSprite(final Sprite sprite, final Point point, final double scale) {
		super();
		this.sprite = sprite;
		this.x = point.getX();
		this.y = point.getY();
		this.oldX = sprite.getPath().getBounds2D().getCenterX();
		this.oldY = sprite.getPath().getBounds2D().getCenterY();
		this.scale = scale;
	}
	public MoveSprite(final Sprite sprite, final Point point, final double oldX, final double oldY, final double scale) {
		super();
		this.sprite = sprite;
		this.x = point.getX();
		this.y = point.getY();
		this.oldX = oldX;
		this.oldY = oldY;
		this.scale = scale;
	}

	@Override
	protected void edit() throws ActionNotDoneException {
		sprite.setLocation(x, y, scale);
	}

	@Override
	protected void undo() throws ActionNotDoneException {
		sprite.setLocation(oldX, oldY, 1.0); // Original coords are given unscaled
	}

	@Override
	public String getDescription() {
		return "Move Sprite " + sprite.getName() + " from {" + oldX + ", " + oldY + "} to {" 
				+ x + ", " + y +"}";
	}

}
