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
import com.wings2d.editor.objects.skeleton.SkeletonNode;

public class SkeletonControls extends SkeletonTreeControlsUIElement{
	public static final String CARD_ID = "Skeleton";
	
	private JButton addAnim;
	
	public SkeletonControls(SkeletonTreeControls controls) {
		super(controls);
		addAnim = new JButton("New Animation");
	}

	@Override
	public void updatePanelInfo(SkeletonNode node) {
		if (node != null)
		{
			addLabel(node.toString());
			addNameLine();
		}
		panel.add(addAnim);
		JSeparator line = new JSeparator();
		line.setPreferredSize(new Dimension(panel.getWidth(), SEPARATOR_WIDTH));
		panel.add(line);
	}

	@Override
	public void createOtherEvents() {
		addAnim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String animName = (String)JOptionPane.showInputDialog(panel, "","Animation Name",
	    				JOptionPane.PLAIN_MESSAGE, null, null, "Animation");
				if (animName != null)
				{
					DefaultTreeModel model = (DefaultTreeModel) controls.getTree().getModel();
					Skeleton root = (Skeleton) model.getRoot();
					try {
						SkeletonAnimation newAnim = new SkeletonAnimation(animName, root);	
						model.insertNodeInto((MutableTreeNode)newAnim, root, root.getChildCount());
						model.reload();
						TreePath path = new TreePath(root).pathByAddingChild(newAnim);
						controls.getTree().setSelectionPath(path);
					}
					catch (IllegalArgumentException exception){
						JOptionPane.showMessageDialog(panel, exception.getMessage(), "Insert Failed!", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
	}
}
