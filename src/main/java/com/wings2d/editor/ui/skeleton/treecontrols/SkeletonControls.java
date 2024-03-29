package com.wings2d.editor.ui.skeleton.treecontrols;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.tree.DefaultTreeModel;

import com.wings2d.editor.objects.skeleton.Skeleton;
import com.wings2d.editor.objects.skeleton.SkeletonAnimation;
import com.wings2d.editor.objects.skeleton.SkeletonNode;
import com.wings2d.editor.ui.edits.AddToTree;

public class SkeletonControls extends SkeletonTreeControlsUIElement{
	public static final String CARD_ID = "Skeleton";
	
	private JPanel namePanel, addAnimPanel;
	private JButton addAnim;
	
	private Connection con;
	
	public SkeletonControls(final SkeletonTreeControls controls, final Connection con) {
		super(controls, con);
		this.con = con;
		panel.setLayout(new BorderLayout());
		
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
			panel.add(namePanel, BorderLayout.NORTH);
			namePanel.removeAll();
			namePanel.add(new JLabel(node.toString()));
		}
		panel.add(addAnimPanel, BorderLayout.SOUTH);
	}

	@Override
	public void createOtherEvents() {
		addAnim.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String animName = (String)JOptionPane.showInputDialog(panel, "","Animation Name",
	    				JOptionPane.PLAIN_MESSAGE, null, null, "Animation");
				if (animName != null)
				{
					DefaultTreeModel model = (DefaultTreeModel) controls.getTree().getModel();
					Skeleton root = (Skeleton) model.getRoot();
					try {
						SkeletonAnimation newAnim = SkeletonAnimation.insert(animName, root.getID(), root, con);	
						controls.getEditPanel().getEditor().getEditsManager().edit(new AddToTree(model, newAnim, root));
						controls.getTree().setSelectionPath(controls.getTree().getSelectionPath().pathByAddingChild(
								root.getChildAt(root.getChildCount() - 1)));
					}
					catch (IllegalArgumentException exception){
						JOptionPane.showMessageDialog(panel, exception.getMessage(), "Insert Failed!", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
	}
}
