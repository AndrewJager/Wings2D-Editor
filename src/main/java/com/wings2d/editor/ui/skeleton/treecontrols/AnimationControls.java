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

import com.wings2d.editor.objects.skeleton.Skeleton;
import com.wings2d.editor.objects.skeleton.SkeletonAnimation;
import com.wings2d.editor.objects.skeleton.SkeletonFrame;
import com.wings2d.editor.objects.skeleton.SkeletonNode;

public class AnimationControls extends SkeletonTreeControlsUIElement{
	public static final String CARD_ID = "Animation";
	
	private JButton addFrame;

	public AnimationControls(final SkeletonTreeControls controls) {
		super(controls);
		addFrame = new JButton("New Frame");;
	}


	@Override
	protected void updatePanelInfo(final SkeletonNode node) {
		JSeparator line = new JSeparator();
		line.setPreferredSize(new Dimension(panel.getWidth(), SEPARATOR_WIDTH));
		panel.add(line);
		if (node != null)
		{
			addLabel(node.toString());
			addNameLine();
		}
		panel.add(rename);
		rename.setText("Rename Animation");
		panel.add(delete);
		delete.setText("Delete Animation");
		line = new JSeparator();
		line.setPreferredSize(new Dimension(panel.getWidth(), SEPARATOR_WIDTH));
		panel.add(line);
		panel.add(addFrame);	
	}
	
	@Override
	public void createOtherEvents() {
		addFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SkeletonNode selectedNode = (SkeletonNode)controls.getTree().getLastSelectedPathComponent();
				if (selectedNode != null)
				{
					TreePath path = controls.getTree().getSelectionPath();
					String frameName = (String)JOptionPane.showInputDialog(panel, "","Frame Name",
		    				JOptionPane.PLAIN_MESSAGE, null, null, "Frame");
					if (frameName != null)
					{
						DefaultTreeModel model = (DefaultTreeModel) controls.getTree().getModel();
						SkeletonAnimation anim = (SkeletonAnimation)selectedNode;
						try {
							SkeletonFrame newFrame = new SkeletonFrame(frameName, anim);
							if (anim.getChildCount() == 0)
							{
								Skeleton skeleton = (Skeleton)model.getRoot();
								newFrame.setParentSyncedFrame(skeleton.getMasterFrame());
							}
							else
							{
								newFrame.setParentSyncedFrame((SkeletonFrame)anim.getChildAt(anim.getChildCount() - 1));
							}
							model.insertNodeInto((MutableTreeNode)newFrame,
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
