package editor.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public abstract class UIPanel {
	protected JPanel panel;
	protected Editor editor;
	protected List<UIElement> elements;
	
	public UIPanel(final Editor edit)
	{
		this.editor = edit;
		panel = new JPanel();
		panel.setLayout(null);
		edit.getPanels().add(this);
		elements = new ArrayList<UIElement>();
	}
	
	public void resize(final JPanel parent, final double scale)
	{
		panel.setLocation(0, 0);
		panel.setSize(parent.getWidth(), parent.getHeight());
		for (int i = 0; i < elements.size(); i++)
		{
			elements.get(i).resize(scale);
		}
	}
	
	public void initElements()
	{
		for (int i = 0; i < elements.size(); i++)
		{
			elements.get(i).createEvents();
			panel.add(elements.get(i).getPanel());
		}
	}
	
	public JPanel getPanel()
	{
		return panel;
	}
	
	public List<UIElement> getElements()
	{
		return elements;
	}
}
