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
import com.wings2d.editor.objects.skeleton.SkeletonNode;
import com.wings2d.editor.ui.edits.AddToTree;

public class SkeletonControls extends SkeletonTreeControlsUIElement{
	public static final String CARD_ID = "Skeleton";
	
	private JPanel namePanel, addAnimPanel;
	private JButton addAnim;
	
	public SkeletonControls(SkeletonTreeControls controls) {
		super(controls);
		
		namePanel = new JPanel();
		
		addAnimPanel = new JPanel();
		addAnim = new JButton("New Animation");
		addAnimPanel.add(addAnim);
	}

	@Override
	public void updatePanelInfo(SkeletonNode node) {
		super.updatePanelInfo(node);
		if (node != null)
		{
			panel.add(namePanel);
			namePanel.removeAll();
			namePanel.add(new JLabel(node.toString()));
			panel.add(new JSeparator());
		}
		panel.add(addAnimPanel);
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
						controls.getSkeleton().getEditor().getEditsManager().edit(new AddToTree(model, newAnim));
						controls.getSkeleton().getSkeletonTree().reloadModel();
					}
					catch (IllegalArgumentException exception){
						JOptionPane.showMessageDialog(panel, exception.getMessage(), "Insert Failed!", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
	}
}
