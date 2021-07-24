package com.wings2d.editor.ui.skeleton.treecontrols;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.tree.DefaultTreeModel;

import com.wings2d.editor.objects.skeleton.SkeletonBone;
import com.wings2d.editor.objects.skeleton.SkeletonFrame;
import com.wings2d.editor.objects.skeleton.SkeletonNode;
import com.wings2d.editor.ui.edits.AddToTree;

public class FrameControls extends SkeletonTreeControlsUIElement{
	public static final String CARD_ID = "Frame";
	
	private JPanel namePanel, syncPanel, bonePanel;
	private JButton addBone;
	private JLabel syncLabel;

	public FrameControls(final SkeletonTreeControls controls) {
		super(controls);
		
		namePanel = new JPanel();
		
		syncPanel = new JPanel();
		syncLabel = new JLabel();
		syncPanel.add(syncLabel);
		
		bonePanel = new JPanel();
		addBone = new JButton("New Bone");
		bonePanel.add(addBone);
	}

	@Override
	protected void updatePanelInfo(final SkeletonNode node) {
		super.updatePanelInfo(node);
		if (node != null)
		{
			panel.add(namePanel);
			namePanel.removeAll();
			namePanel.add(new JLabel(node.toString()));
		}
		panel.add(new JSeparator());
		
		panel.add(controlsPanel);
		
		panel.add(syncPanel);
		SkeletonFrame frame = (SkeletonFrame)node;
		if (frame.getParentSyncedFrame() != null)
		{
			syncLabel.setText("Sync: " + frame.getParentSyncedFrame().toString());
		}
		else
		{
			syncLabel.setText("No parent sync frame");
		}
		
		JPanel syncPanel = new JPanel();
		JButton syncBones = new JButton("Sync Bone Locations");
		syncBones.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.cautiousSyncBonePositions();
				controls.getDrawingArea().getDrawArea().repaint();
			}
		});
		syncPanel.add(syncBones);
		panel.add(syncPanel);
		
		createList(frame.getSyncedFrameNames());
		panel.add(new JSeparator());
		
		panel.add(bonePanel);
		
		controls.getDrawingArea().setSelectedFrame(frame);
		controls.getDrawingArea().getDrawArea().setDrawItem(frame);
	}

	@Override
	protected void createOtherEvents() {
		addBone.addActionListener(new ActionListener() {
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
							SkeletonNode node = new SkeletonBone(boneName, (SkeletonFrame)selectedNode);


							controls.getSkeleton().getEditor().getEditsManager().edit(new AddToTree(model, node, (SkeletonNode)node.getParent()));
						}
						catch (IllegalArgumentException exception) {
							JOptionPane.showMessageDialog(panel, exception.getMessage(), "Insert Failed!", JOptionPane.ERROR_MESSAGE);
						}
					}
				}			
			}
		});
	}
}
