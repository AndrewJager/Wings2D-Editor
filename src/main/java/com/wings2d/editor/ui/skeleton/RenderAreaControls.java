package com.wings2d.editor.ui.skeleton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import com.wings2d.editor.objects.skeleton.SkeletonFrame;
import com.wings2d.editor.ui.UIElement;

public class RenderAreaControls extends UIElement<SkeletonEdit>{
	private JButton render, previous, next;
	private JToggleButton play;
	private JLabel curFrame;
	private JPanel idxPanel;
	private Timer timer;
	
	private int curFrameTime, timeSinceLast;
	private int TICK = 10;
	
	private String PLAY_CHAR = Character.toString('\u25B6');
	private String STOP_CHAR = Character.toString('\u23F9');
	
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
		curFrame = new JLabel("None", JLabel.CENTER);
		idxPanel.add(curFrame, BorderLayout.CENTER);
		next = new JButton(">");
		idxPanel.add(next, BorderLayout.EAST);
		idxPanel.setBorder(new EmptyBorder(0, 80, 0, 80));
		panel.add(idxPanel, BorderLayout.CENTER);
		
		play = new JToggleButton(PLAY_CHAR);
		JPanel playPanel = new JPanel();
		playPanel.add(play);
		panel.add(playPanel, BorderLayout.EAST);
		
		
		timer  = new Timer(TICK, 
			new ActionListener() {
				@Override
			    public void actionPerformed(ActionEvent evt) {
					updateTime();
			    }
			}
		);
		
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
	
	private void updateTime() {
		timeSinceLast += TICK;
		if (timeSinceLast >= curFrameTime) {
			if (renderFrame != null) {
				renderFrame = getEditPanel().getSkeleton().getNextFrame(renderFrame);
				drawRender();
				timeSinceLast = 0;
				curFrameTime = getFrameTime();
			}
		}
	}
	private int getFrameTime() {
		if (renderFrame != null) {
			if (renderFrame.getTime() == 0) {
				return getEditPanel().getEditor().getSettings().getDefaultTime();
			}
			else {
				return renderFrame.getTime();
			}
		}
		else {
			return 0;
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
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (play.isSelected()) {
					play.setText(STOP_CHAR);
					timer.restart();
					curFrameTime = getFrameTime();
					timeSinceLast = 0;
				}
				else {
					play.setText(PLAY_CHAR);
					timer.stop();
				}
			}	
		});
	}
}
