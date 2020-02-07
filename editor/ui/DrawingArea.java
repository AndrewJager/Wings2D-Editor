package editor.ui;

import java.awt.Color;

import javax.swing.JList;
import javax.swing.JPanel;

import editor.objects.Frame;

public class DrawingArea {
	private JPanel panel;
	
	public DrawingArea()
	{
		panel = new JPanel();
		panel.setBounds(875, 10, 600, 600);
		panel.setBackground(Color.WHITE);
	}
	
	public void createEvents(JList<String> animList, JList<String> frameList, JList<String> objectList, Editor editor)
	{
		panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
            	if (frameList.getSelectedIndex() != -1 && animList.getSelectedIndex() != -1 && objectList.getSelectedIndex() != -1)
            	{
            		Frame curFrame = editor.getActiveSprite().getAnimation(animList.getSelectedIndex()).getFrame(frameList.getSelectedIndex());
                	curFrame.processMousePress(objectList.getSelectedIndex(), evt.getPoint());
            	}
                
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
            	if (animList.getSelectedIndex() != -1 && frameList.getSelectedIndex() != -1 && objectList.getSelectedIndex() != -1)
            	{
            		Frame curFrame = editor.getActiveSprite().getAnimation(animList.getSelectedIndex()).getFrame(frameList.getSelectedIndex());
                	curFrame.processMouseRelease(curFrame.getObject(objectList.getSelectedIndex()).getName(), evt.getPoint());
    				editor.updateDrawing(editor.getActiveSprite().getAnimation(animList.getSelectedIndex()).getFrame(frameList.getSelectedIndex()), panel,
    						objectList.getSelectedIndex());
    				editor.setShouldReRender(true);
            	}
            }
        });
	}
	
	public JPanel getPanel()
	{
		return panel;
	}
}
