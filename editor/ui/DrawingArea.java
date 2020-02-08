package editor.ui;

import java.awt.Color;

import javax.swing.JPanel;

import editor.objects.Frame;

public class DrawingArea {
	private Editor editor;
	private JPanel panel;
	
	public DrawingArea(Editor edit)
	{
		editor = edit;
		panel = new JPanel();
		panel.setBounds(875, 10, 600, 600);
		panel.setBackground(Color.WHITE);
	}
	
	public void createEvents()
	{
		AnimationLists ani = editor.getAnimLists();
		panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
            	if (ani.getIsObjectSelected())
            	{
            		Frame curFrame = ani.getSelectedFrame();
                	curFrame.processMousePress(ani.getObjectList().getSelectedIndex(), evt.getPoint());
            	}
                
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
            	if (ani.getIsObjectSelected())
            	{
            		Frame curFrame = ani.getSelectedFrame();
                	curFrame.processMouseRelease(curFrame.getObject(ani.getObjectList().getSelectedIndex()).getName(), evt.getPoint());
    				editor.updateDrawing(ani);
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
