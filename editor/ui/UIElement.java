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
	
	public void resizePanel()
	{
		double xScale = Double.valueOf(editor.getFrame().getWidth()) / editor.frameStartWidth;
		double yScale = Double.valueOf(editor.getFrame().getHeight()) / editor.frameStartHeight;
		Rectangle newBounds = new Rectangle((int)(ogBounds.getX() * xScale), (int)(ogBounds.getY() * yScale),
				(int)(ogBounds.getWidth() * xScale), (int)(ogBounds.getHeight() * yScale));
		panel.setBounds(newBounds);
		panel.revalidate();
	}
	
	public JPanel getPanel()
	{
		return panel;
	}
	
	public abstract void createEvents();
}
