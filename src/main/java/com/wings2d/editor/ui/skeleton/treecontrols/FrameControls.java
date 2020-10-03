package com.wings2d.editor.ui.skeleton.treecontrols;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import com.wings2d.editor.objects.skeleton.SkeletonBone;
import com.wings2d.editor.objects.skeleton.SkeletonFrame;
import com.wings2d.editor.objects.skeleton.SkeletonNode;

public class FrameControls extends SkeletonTreeControlsUIElement{
	public static final String CARD_ID = "Frame";
	
	private JButton addBone;

	public FrameControls(final SkeletonTreeControls controls) {
		super(controls);
		addBone = new JButton("New Bone");
	}

	@Override
	protected void updatePanelInfo(final SkeletonNode node) {
		if (node != null)
		{
			addLabel(node.toString());
			addNameLine();
		}
		panel.add(rename);
		rename.setText("Rename Frame");
		panel.add(delete);
		delete.setText("Delete Frame");
		SkeletonFrame frame = (SkeletonFrame)node;
		if (frame.getParentSyncedFrame() != null)
		{
			addLabel(frame.getParentSyncedFrame().toString());
		}
		else
		{
			addLabel("No parent sync frame");
		}
		JButton syncBones = new JButton("Sync Bone Locations");
		syncBones.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.cautiousSyncBonePositions();
				controls.getDrawingArea().getDrawArea().repaint();
			}
		});
		panel.add(syncBones);
		createList(frame.getSyncedFrameNames());
		JSeparator line = new JSeparator();
		line.setPreferredSize(new Dimension(panel.getWidth(), SEPARATOR_WIDTH));
		panel.add(line);
		panel.add(addBone);
		
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
					TreePath path = controls.getTree().getSelectionPath();
					String boneName = (String)JOptionPane.showInputDialog(panel, "","Bone Name",
		    				JOptionPane.PLAIN_MESSAGE, null, null, "Bone");
					if (boneName != null)
					{
						DefaultTreeModel model = (DefaultTreeModel) controls.getTree().getModel();
						try {
							model.insertNodeInto((MutableTreeNode)new SkeletonBone(boneName, (SkeletonFrame)selectedNode),
									selectedNode, selectedNode.getChildCount());
							model.reload();
											
							controls.getTree().expandPath(path);
							controls.getTree().setSelectionPath(path.pathByAddingChild(selectedNode.getChildAt(selectedNode.getChildCount() - 1)));
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
