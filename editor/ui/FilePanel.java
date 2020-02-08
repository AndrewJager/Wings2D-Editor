package editor.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
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
	
	public void createEvents(Editor editor)
	{
		AnimationLists ani = editor.getAnimLists();
		selectButton.addActionListener(new ActionListener(){  
	    	public void actionPerformed(ActionEvent e){  
	    		JFileChooser file = new JFileChooser("test");
	    		int result = file.showSaveDialog(editor.getFrame());
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
	    		if (ani.getIsFrameSelected())
	    		{
	    			Frame curFrame = ani.getSelectedFrame();
	    			editor.setShouldReRender(true);
	    			editor.updateRender(ani.getFrameList(), curFrame, editor.getRender().getPanel(), false);
	    		}
	    	}
	    });
	}
	
	public JPanel getPanel()
	{
		return panel;
	}
}
