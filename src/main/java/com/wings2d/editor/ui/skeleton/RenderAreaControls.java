package com.wings2d.editor.ui.skeleton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.wings2d.editor.objects.skeleton.SkeletonFrame;
import com.wings2d.editor.ui.UIElement;

public class RenderAreaControls extends UIElement<SkeletonEdit>{
	private JButton render, previous, next;
	private JLabel curFrame;
	private JPanel idxPanel;
	
	private SkeletonFrame renderFrame;

	public RenderAreaControls(SkeletonEdit edit) {
		super(edit);
		int PADDING = 5;
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), 
				new EmptyBorder(PADDING, PADDING, PADDING, PADDING)));
		panel.setLayout(new BorderLayout());
		
		render = new JButton("Render");
		panel.add(render, BorderLayout.WEST);
		
		idxPanel = new JPanel();
		idxPanel.setLayout(new BorderLayout());
		
		previous = new JButton("<");
		idxPanel.add(previous, BorderLayout.WEST);
		curFrame = new JLabel("None");
		curFrame.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
		idxPanel.add(curFrame, BorderLayout.CENTER);
		next = new JButton(">");
		idxPanel.add(next, BorderLayout.EAST);
		panel.add(idxPanel, BorderLayout.EAST);
	}
	
	private void renderFrame() {
		getEditPanel().getSkeleton().generateRender(getEditPanel().getEditor().getUIScale());
		renderFrame = getEditPanel().getSkeletonTree().getFrameToRender();
		drawRender();
	}
	
	private void drawRender() {
		if (renderFrame != null) { 
			curFrame.setText(renderFrame.getName());
			getEditPanel().getRenderArea().getRenderPanel().getDrawArea().setDrawItem(renderFrame);
			getEditPanel().getRenderArea().getRenderPanel().getDrawArea().repaint();
		}
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
				renderFrame = getEditPanel().getSkeleton().getPreviousFrame(renderFrame);
				drawRender();
			}
		});
		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				renderFrame = getEditPanel().getSkeleton().getNextFrame(renderFrame);
				drawRender();
			}
		});
	}
}
