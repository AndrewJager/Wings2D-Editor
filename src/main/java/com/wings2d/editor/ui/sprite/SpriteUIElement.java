package com.wings2d.editor.ui.sprite;

import java.awt.Rectangle;

import com.wings2d.editor.ui.Editor;
import com.wings2d.editor.ui.UIElement;

public abstract class SpriteUIElement extends UIElement{
	protected Editor editor;
	
	public SpriteUIElement(Editor edit, Rectangle bounds) {
		super(bounds);
		
		this.editor = edit;
	}
}
