package editor.ui.sprite;

import java.awt.Rectangle;

import editor.ui.Editor;
import editor.ui.UIElement;

public abstract class SpriteUIElement extends UIElement{
	protected Editor editor;
	
	public SpriteUIElement(Editor edit, Rectangle bounds) {
		super(bounds);
		
		this.editor = edit;
	}
}
