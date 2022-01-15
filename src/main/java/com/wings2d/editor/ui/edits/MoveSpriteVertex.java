package com.wings2d.editor.ui.edits;

import java.awt.Point;

import com.wings2d.editor.objects.skeleton.Sprite;

public class MoveSpriteVertex extends Edit {
	private Sprite sprite;
	private int vertex;
	private double x, y, oldX, oldY, scale;
	
	public MoveSpriteVertex(final Sprite sprite, final Point point, final double oldX,
			final double oldY, final double scale) {
		this.sprite = sprite;
		this.vertex = sprite.getSelectedVertex();
		this.x = point.getX();
		this.y = point.getY();
		this.oldX = oldX;
		this.oldY = oldY;
		this.scale = scale;
	}

	@Override
	protected void edit() throws ActionNotDoneException {
		sprite.setVertexLocation(x, y, vertex, scale, true);
	}

	@Override
	protected void undo() throws ActionNotDoneException {
		sprite.setVertexLocation(oldX, oldY, vertex, scale, true);
	}

	@Override
	public String getDescription() {
		return "Set position of vertex " + vertex + " of " + sprite.getName() + " from {" + oldX + ", " + oldY 
				+ "} to {" + x + ", " + y + "}";
	}

}
