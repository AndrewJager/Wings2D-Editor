package editor.ui;

import java.awt.Rectangle;

import javax.swing.JPanel;

public abstract class UIElement {
	protected JPanel panel;
	protected Rectangle ogBounds;
	protected Editor editor;
	
	public UIElement(Editor edit, Rectangle bounds)
	{
		this.editor = edit;
		this.ogBounds = bounds;
		panel = new JPanel();
		panel.setBounds(bounds);
	}
	
	public void resize(double scale)
	{
		Rectangle newBounds = new Rectangle((int)(ogBounds.getX() * scale), (int)(ogBounds.getY() * scale),
				(int)(ogBounds.getWidth() * scale), (int)(ogBounds.getHeight() * scale));
		panel.setBounds(newBounds);
		panel.revalidate();
	}
	
	public JPanel getPanel()
	{
		return panel;
	}
	
	public abstract void createEvents();
}
