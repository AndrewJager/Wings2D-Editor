package com.wings2d.editor.ui.skeleton.treecontrols;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultTreeModel;

import com.wings2d.editor.objects.skeleton.SkeletonBone;
import com.wings2d.editor.objects.skeleton.SkeletonFrame;
import com.wings2d.editor.objects.skeleton.SkeletonNode;
import com.wings2d.editor.ui.edits.AddToTree;
import com.wings2d.editor.ui.edits.SyncBones;

public class FrameControls extends SkeletonTreeControlsUIElement{
	public static final String CARD_ID = "Frame";
	
	private SkeletonFrame frame;
	private JPanel syncPanel, bonePanel, bottomPanel, timePanel;
	private JButton addBone, cautiousSync, forceSync, resetTime;
	private JLabel syncLabel, defaultTime;
	private JSpinner time;

	public FrameControls(final SkeletonTreeControls controls, final Connection con) {
		super(controls, con);
		panel.setLayout(new BorderLayout());
		
		syncPanel = new JPanel();
		syncPanel.setLayout(new BorderLayout());
		syncLabel = new JLabel("", SwingConstants.CENTER);
		syncPanel.add(syncLabel, BorderLayout.NORTH);
		
		JPanel syncButtonsPanel = new JPanel(new BorderLayout());
		cautiousSync = new JButton("Cautious Sync");
		syncButtonsPanel.add(cautiousSync, BorderLayout.WEST);
		forceSync = new JButton("Force Sync");
		syncButtonsPanel.add(forceSync, BorderLayout.EAST);
		syncPanel.add(syncButtonsPanel, BorderLayout.SOUTH);
		
		bottomPanel = new JPanel(new BorderLayout());

		timePanel = new JPanel(new BorderLayout());
		timePanel.add(new JLabel("Time:"), BorderLayout.WEST);
		JPanel resetTimePanel = new JPanel();
		resetTime = new JButton(Character.toString('\u238C')); 
		resetTimePanel.add(resetTime);
		timePanel.add(resetTimePanel, BorderLayout.CENTER);
		time = new JSpinner(new SpinnerNumberModel(0, 0, 50000, 10));
		timePanel.add(time, BorderLayout.EAST);
		defaultTime = new JLabel("Default:", SwingConstants.CENTER);
		timePanel.add(defaultTime, BorderLayout.SOUTH);
		
		bottomPanel.add(timePanel, BorderLayout.NORTH);
		bonePanel = new JPanel();
		addBone = new JButton("New Bone");
		bonePanel.add(addBone);
		bottomPanel.add(bonePanel, BorderLayout.SOUTH);
		bottomPanel.setBorder(new EmptyBorder(10, 2, 0, 2));
		
	}

	@Override
	protected void updatePanelInfo(final SkeletonNode node) {
		super.updatePanelInfo(node);
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		if (node != null)
		{
			topPanel.add(new JLabel(node.toString(), SwingConstants.CENTER), BorderLayout.NORTH);
		}
		
		topPanel.add(controlsPanel, BorderLayout.CENTER);
		panel.add(topPanel, BorderLayout.NORTH);
		
		panel.add(syncPanel, BorderLayout.CENTER);
		frame = (SkeletonFrame)node;
		if (frame.getParentSyncedFrame() != null)
		{
			syncLabel.setText("Sync: " + frame.getParentSyncedFrame().toString());
		}
		else
		{
			syncLabel.setText("No parent sync frame");
		}
		
		panel.add(bottomPanel, BorderLayout.SOUTH);
		
		// Disable sync buttons if no sync frame
		cautiousSync.setEnabled(frame.getParentSyncedFrame() != null);
		forceSync.setEnabled(frame.getParentSyncedFrame() != null);
		
		time.getModel().setValue(frame.getTime());
		time.setEnabled(!frame.getIsMaster());
		resetTime.setEnabled(!frame.getIsMaster());
		defaultTime.setText("Default: " + controls.getEditPanel().getEditor().getSettings().getDefaultTime());
	
//		createList(frame.getSyncedFrameNames());
		
		controls.getDrawingArea().setSelectedFrame(frame);
		controls.getDrawingArea().getDrawArea().setDrawItem(frame);
	}

	@Override
	protected void createOtherEvents() {
		addBone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SkeletonNode selectedNode = (SkeletonNode)controls.getTree().getLastSelectedPathComponent();
				if (selectedNode != null)
				{
					String boneName = (String)JOptionPane.showInputDialog(panel, "","Bone Name",
		    				JOptionPane.PLAIN_MESSAGE, null, null, "Bone");
					if (boneName != null)
					{
						DefaultTreeModel model = (DefaultTreeModel) controls.getTree().getModel();
						try {
							SkeletonNode node = SkeletonBone.insert(boneName, (SkeletonFrame)selectedNode, con);

							controls.getEditPanel().getEditor().getEditsManager().edit(new AddToTree(model, node, (SkeletonNode)node.getParent()));
							controls.getTree().setSelectionPath(controls.getTree().getSelectionPath().pathByAddingChild(
									selectedNode.getChildAt(selectedNode.getChildCount() - 1)));
						}
						catch (IllegalArgumentException exception) {
							JOptionPane.showMessageDialog(panel, exception.getMessage(), "Insert Failed!", JOptionPane.ERROR_MESSAGE);
						}
					}
				}			
			}
		});
		cautiousSync.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controls.getEditPanel().getEditor().getEditsManager().edit(new SyncBones(frame, true));
				controls.getDrawingArea().getDrawArea().repaint();
			}
		});
		forceSync.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controls.getEditPanel().getEditor().getEditsManager().edit(new SyncBones(frame, false));
				controls.getDrawingArea().getDrawArea().repaint();
			}
		});
		time.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				frame.setTime((int)time.getModel().getValue());	
			}
		});
		resetTime.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setTime(0);
				time.getModel().setValue(0);
			}
		});
	}
}
