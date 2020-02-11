package editor.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import editor.objects.SpriteSheet;

public class FilePanel {
	private Editor editor;
	private JPanel panel;
	private JButton selectButton;
	private JButton newButton;
	private JButton renderBtn;
	private JButton saveBtn;
	private JLabel fileLabel;
	private JLabel fileName;

	public FilePanel(Editor edit)
	{
		editor = edit;
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
	    saveBtn = new JButton("Save");
	    panel.add(saveBtn);
		panel.setLayout(new FlowLayout());
	}
	
	public void createEvents()
	{
		AnimationLists ani = editor.getAnimLists();
		selectButton.addActionListener(new ActionListener(){  
	    	public void actionPerformed(ActionEvent e){  
	    		JFileChooser file = new JFileChooser("test");
	    		int result = file.showSaveDialog(editor.getFrame());
	    		if (result == JFileChooser.APPROVE_OPTION) 
	    		{
	    			fileName.setText(file.getSelectedFile().getName());
	    			try {
	    				Scanner in = new Scanner(new FileReader(file.getSelectedFile()));
	    				String spriteName = file.getSelectedFile().getName().substring(0, file.getSelectedFile().getName().length() - 1);
	    				editor.setActiveSprite(new SpriteSheet(spriteName, in));
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
	    		} else if (result == JFileChooser.CANCEL_OPTION) 
	    		{
	    		    System.out.println("Cancel was selected");
	    		}
	        }  
	    });
	    newButton.addActionListener(new ActionListener(){  
	    	public void actionPerformed(ActionEvent e){ 
	    		String spriteName = (String)JOptionPane.showInputDialog(editor.getFrame(), "","Name sprite",
	    				JOptionPane.PLAIN_MESSAGE, null, null, "Object");
	    		editor.setActiveSprite(new SpriteSheet(spriteName));
	        }  
	    }); 
	    renderBtn.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (ani.getIsFrameSelected())
	    		{
	    			editor.setShouldReRender(true);
	    			editor.getRender().updateRender(false);
	    		}
	    	}
	    });
	    saveBtn.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		editor.getActiveSprite().saveToFile();
	    	}
	    });
	}
	
	public JPanel getPanel()
	{
		return panel;
	}
}
