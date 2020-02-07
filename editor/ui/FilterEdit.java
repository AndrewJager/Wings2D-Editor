package editor.ui;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class FilterEdit {
	private JPanel panel;
	
	public FilterEdit()
	{
		panel = new JPanel();
		panel.setBounds(500, 540, 350, 210);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panel.setLayout(new FlowLayout());
	}
	
	public JPanel getPanel()
	{
		return panel;
	}
}
