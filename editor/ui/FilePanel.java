package editor.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import editor.objects.Frame;
import editor.objects.SpriteSheet;

public class FilePanel {
	private JPanel panel;
	private JButton selectButton;
	private JButton newButton;
	private JButton renderBtn;
	private JLabel fileLabel;
	private JLabel fileName;

	public FilePanel()
	{
		panel = new JPanel();
		panel.setBounds(10, 10, 400, 50);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		fileLabel = new JLabel("File:");
		panel.add(fileLabel);
		fileName = new JLabel("None");
		panel.add(fileName);
		selectButton = new JButton("Select"); 
		panel.add(selectButton);
		newButton = new JButton("New");
	    panel.add(newButton);
	    renderBtn = new JButton("Render");
	    panel.add(renderBtn);
		panel.setLayout(new FlowLayout());
	}
	
	public void createEvents(JList<String> animList, JList<String> frameList, JPanel render, JFrame frame, Editor editor)
	{
		selectButton.addActionListener(new ActionListener(){  
	    	public void actionPerformed(ActionEvent e){  
	    		JFileChooser file = new JFileChooser("test");
	    		int result = file.showSaveDialog(frame);
	    		if (result == JFileChooser.APPROVE_OPTION) 
	    		{
	    			 fileName.setText(file.getSelectedFile().getName());
	    		} else if (result == JFileChooser.CANCEL_OPTION) 
	    		{
	    		    System.out.println("Cancel was selected");
	    		}
	        }  
	    });
	    newButton.addActionListener(new ActionListener(){  
	    	public void actionPerformed(ActionEvent e){ 
	    		editor.setActiveSprite(new SpriteSheet());
	        }  
	    }); 
	    renderBtn.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (animList.getSelectedIndex() != -1 && frameList.getSelectedIndex() != -1)
	    		{
	    			Frame curFrame = editor.getActiveSprite().getAnimation(animList.getSelectedIndex()).getFrame(frameList.getSelectedIndex());
	    			editor.setShouldReRender(true);
	    			editor.updateRender(frameList, curFrame, render, false);
	    		}
	    	}
	    });
	}
	
	public JPanel getPanel()
	{
		return panel;
	}
}
