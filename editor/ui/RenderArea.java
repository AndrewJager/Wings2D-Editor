package editor.ui;

import java.awt.Color;

import javax.swing.JPanel;

public class RenderArea {
	private JPanel panel;
	
	public RenderArea()
	{
		panel = new JPanel();
		panel.setBounds(700, 10, 150, 150);
		panel.setBackground(Color.WHITE);
	}

	public JPanel getPanel()
	{
		return panel;
	}
}
