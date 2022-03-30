package com.wings2d.editor.ui.edits;

import java.awt.Color;

import com.wings2d.editor.objects.skeleton.path.Sprite;

public class SetSpriteColor extends Edit{
	private Sprite sprite;
	private Color oldColor, newColor;
	
	public SetSpriteColor(final Sprite sprite, final Color color) {
		super();
		this.sprite = sprite;
		this.oldColor = sprite.getColor();
		this.newColor = color;
	}

	@Override
	public void edit() throws ActionNotDoneException {
		sprite.setColor(newColor);
	}

	@Override
	public void undo() throws ActionNotDoneException {
		sprite.setColor(oldColor);
	}

	@Override
	public String getDescription() {
		return "Change color of " + sprite.getName() + " from " + oldColor.toString() + " to " + newColor.toString();
	}
}
