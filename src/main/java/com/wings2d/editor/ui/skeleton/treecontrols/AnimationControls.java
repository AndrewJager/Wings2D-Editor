package com.wings2d.editor.ui.skeleton.treecontrols;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.tree.DefaultTreeModel;

import com.wings2d.editor.objects.skeleton.Skeleton;
import com.wings2d.editor.objects.skeleton.SkeletonAnimation;
import com.wings2d.editor.objects.skeleton.SkeletonFrame;
import com.wings2d.editor.objects.skeleton.SkeletonNode;
import com.wings2d.editor.ui.edits.AddToTree;

public class AnimationControls extends SkeletonTreeControlsUIElement{
	public static final String CARD_ID = "Animation";
	
	private JPanel namePanel, framePanel;
	private JButton addFrame;

	public AnimationControls(final SkeletonTreeControls controls) {
		super(controls);
		
		namePanel = new JPanel();
		
		framePanel = new JPanel();
		addFrame = new JButton("New Frame");
		framePanel.add(addFrame);
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
		panel.add(new JSeparator());

		panel.add(framePanel);	
	}
	
	@Override
	public void createOtherEvents() {
		addFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SkeletonNode selectedNode = (SkeletonNode)controls.getTree().getLastSelectedPathComponent();
				if (selectedNode != null)
				{
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
							controls.getSkeleton().getEditor().getEditsManager().edit(new AddToTree(model, newFrame,
									(SkeletonNode)selectedNode));
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
	}
}
