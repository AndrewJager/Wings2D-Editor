package com.wings2d.editor.ui.skeleton.treecontrols;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.tree.DefaultTreeModel;

import com.wings2d.editor.objects.skeleton.SkeletonBone;
import com.wings2d.editor.objects.skeleton.SkeletonNode;
import com.wings2d.editor.objects.skeleton.Sprite;
import com.wings2d.editor.ui.edits.AddToTree;
import com.wings2d.editor.ui.edits.SetBoneLocation;
import com.wings2d.editor.ui.edits.SetBoneRotation;
import com.wings2d.editor.ui.edits.SetParentBone;
import com.wings2d.editor.ui.setters.ComboEditSetter;
import com.wings2d.editor.ui.setters.DoubleEditSetter;
import com.wings2d.editor.ui.skeleton.SkeletonEdit;

public class BoneControls extends SkeletonTreeControlsUIElement{
	public static final String CARD_ID = "Bone";
	private SkeletonBone bone;
	
	private JPanel namePanel, spritesPanel;
	
	private ComboEditSetter<SkeletonEdit, SkeletonBone> parentBone;
	private DoubleEditSetter<SkeletonEdit> xPos, yPos, rot;
	private JButton addSprite, syncBone;

	public BoneControls(final SkeletonTreeControls controls, final Connection con) {
		super(controls, con);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		namePanel = new JPanel(); 
		namePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)new JLabel().getPreferredSize().getHeight()));
		
		controlsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)rename.getPreferredSize().getHeight()));
		
		parentBone = new ComboEditSetter<SkeletonEdit, SkeletonBone>(controls.getEditPanel(), "Parent Bone:", 
				new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (bone.getParentBone() != parentBone.getValue()) {
						controls.getEditPanel().getEditor().getEditsManager().edit(new SetParentBone(bone, parentBone.getValue()));
					}
				}
			});
		panel.add(parentBone.getPanel());
		xPos = new DoubleEditSetter<SkeletonEdit>(controls.getEditPanel(),
				"X:",
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						controls.getEditPanel().getEditor().getEditsManager().edit(new SetBoneLocation(
								bone, xPos.getAmt(), bone.getY(), true));
					}
				});
		panel.add(xPos.getPanel());
		yPos = new DoubleEditSetter<SkeletonEdit>(controls.getEditPanel(),
				"Y:",
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						controls.getEditPanel().getEditor().getEditsManager().edit(new SetBoneLocation(
								bone, bone.getX(), yPos.getAmt(), true));
					}
				});
		panel.add(xPos.getPanel());
		rot = new DoubleEditSetter<SkeletonEdit>(controls.getEditPanel(),
				"Rotation:",
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						controls.getEditPanel().getEditor().getEditsManager().edit(
								new SetBoneRotation(bone, rot.getAmt()));
					}
				});
		panel.add(xPos.getPanel());
		
		syncBone = new JButton("Sync");
		
		spritesPanel = new JPanel();
		spritesPanel.add(syncBone);
		addSprite = new JButton("New Sprite");
		spritesPanel.add(addSprite);
		spritesPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)addSprite.getPreferredSize().getHeight()));
	}

	@Override
	protected void updatePanelInfo(final SkeletonNode node) {
		super.updatePanelInfo(node);
		bone = (SkeletonBone)node;
		panel.add(namePanel);
		namePanel.removeAll();
		namePanel.add(new JLabel(node.toString()));
		if (bone.getParentSyncedBone() == null)
		{
			JLabel unsynced = new JLabel("(Unsynced)");
			unsynced.setFont(unsynced.getFont().deriveFont(Font.ITALIC));
			namePanel.add(unsynced);
		}
		panel.add(new JSeparator());
		
		rename.setText("Rename Bone");
		panel.add(controlsPanel);

		panel.add(parentBone.getPanel());
		DefaultComboBoxModel<SkeletonBone> model = new DefaultComboBoxModel<SkeletonBone>(bone.getFrame().getArrayOfBonesExcept(bone));
		parentBone.setModel(model);
		parentBone.setValue(bone.getParentBone());
		if(bone.getParentSyncedBone() != null)
		{
			rename.setEnabled(false);
			parentBone.getPanel().setEnabled(false);
		}
		else {
			rename.setEnabled(true);
			parentBone.getPanel().setEnabled(true);
		}
		panel.add(new JSeparator());

		panel.add(xPos.getPanel());
		xPos.setAmt(bone.getX());
		panel.add(yPos.getPanel());
		yPos.setAmt(bone.getY());
		panel.add(rot.getPanel());
		rot.setAmt(Math.round(bone.getRotation()));
		panel.add(new JSeparator());
		
		panel.add(spritesPanel);
//		panel.add(Box.createRigidArea(new Dimension(0,100)));
		
		setSelectedBone(bone);		
	}

	@Override
	protected void createOtherEvents() {
		addSprite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SkeletonNode selectedNode = (SkeletonNode)controls.getTree().getLastSelectedPathComponent();
				if (selectedNode != null)
				{
					DefaultTreeModel model = (DefaultTreeModel)controls.getTree().getModel();
					try {
						SkeletonBone bone = (SkeletonBone)selectedNode;
						Sprite newSprite = Sprite.insert("Sprite", bone, controls.getEditPanel().getEditor().getConnection());

						controls.getEditPanel().getEditor().getEditsManager().edit(new AddToTree(model, newSprite, selectedNode));
						controls.getTree().setSelectionPath(controls.getTree().getSelectionPath().pathByAddingChild(
								selectedNode.getChildAt(selectedNode.getChildCount() - 1)));
						
					}
					catch (IllegalArgumentException exception) {
						JOptionPane.showMessageDialog(panel, exception.getMessage(), "Insert Failed!", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		syncBone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
	}

	@Override 
	protected void updateVisibleInfo(final SkeletonNode node)
	{
		SkeletonBone bone = (SkeletonBone)node;
		if (bone != null)
		{
			xPos.setAmt(bone.getX());
			yPos.setAmt(bone.getY());
			rot.setAmt(Math.round(bone.getRotation()));
		}
	}
}
