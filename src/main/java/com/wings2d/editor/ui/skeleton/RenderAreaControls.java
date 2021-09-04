package com.wings2d.editor.ui.skeleton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.wings2d.editor.objects.skeleton.SkeletonFrame;
import com.wings2d.editor.ui.UIElement;

public class RenderAreaControls extends UIElement<SkeletonEdit>{
	private SkeletonFrame frame;

	private JButton render, previous, next;
	private JLabel curFrame;
	private JPanel idxPanel;

	public RenderAreaControls(SkeletonEdit edit) {
		super(edit);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		render = new JButton("Render");
		panel.add(render);
		
		idxPanel = new JPanel();
		idxPanel.setLayout(new BorderLayout());
		
		previous = new JButton("<");
		idxPanel.add(previous, BorderLayout.WEST);
		curFrame = new JLabel("None");
		curFrame.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
		idxPanel.add(curFrame, BorderLayout.CENTER);
		next = new JButton(">");
		idxPanel.add(next, BorderLayout.EAST);
		panel.add(idxPanel);
	}
	
	private void renderFrame() {
		getEditPanel().getSkeleton().generateRender(getEditPanel().getEditor().getUIScale());
		getEditPanel().getRenderArea().getRenderPanel().getDrawArea().setDrawItem(getEditPanel().getSkeletonTree().getFrameToRender());
		getEditPanel().getRenderArea().getRenderPanel().getDrawArea().repaint();
	}

	@Override
	public void createEvents() {
		render.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				renderFrame();
			}
		});
		previous.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
	}
}
