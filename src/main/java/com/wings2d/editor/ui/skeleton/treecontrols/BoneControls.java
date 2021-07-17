package com.wings2d.editor.ui.skeleton.treecontrols;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import com.wings2d.editor.objects.skeleton.SkeletonBone;
import com.wings2d.editor.objects.skeleton.SkeletonNode;
import com.wings2d.editor.objects.skeleton.Sprite;

public class BoneControls extends SkeletonTreeControlsUIElement{
	public static final String CARD_ID = "Bone";
	private SkeletonBone bone;
	
	
	private JPanel namePanel, parentBone, xPosPanel, yPosPanel, rotPanel, spritesPanel;
	private JComboBox<SkeletonBone> otherBones;
	private JFormattedTextField xPos, yPos, rotation;
	private JButton addSprite;
	

	public BoneControls(final SkeletonTreeControls controls) {
		super(controls);
		namePanel = new JPanel();
		
		parentBone = new JPanel();
		parentBone.add(new JLabel("Parent Bone: "));
		otherBones = new JComboBox<SkeletonBone>();
		otherBones.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bone.setParentBone((SkeletonBone)otherBones.getSelectedItem());
			}
		});
		parentBone.add(otherBones);
		
		xPosPanel = new JPanel();
		xPosPanel.add(new JLabel("X:"));
		xPos = new JFormattedTextField(new DecimalFormat());
		xPosPanel.add(xPos);
		
		yPosPanel = new JPanel();
		yPosPanel.add(new JLabel("Y:"));
		yPos = new JFormattedTextField(new DecimalFormat());
		yPosPanel.add(yPos);
		
		rotPanel = new JPanel();
		rotPanel.add(new JLabel("Rotation:"));
		rotation = new JFormattedTextField(new DecimalFormat());
		rotPanel.add(rotation);
		
		spritesPanel = new JPanel();
		addSprite = new JButton("New Sprite");
		spritesPanel.add(addSprite);
	}

	@Override
	protected void updatePanelInfo(final SkeletonNode node) {
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

		panel.add(parentBone);
		DefaultComboBoxModel<SkeletonBone> model = new DefaultComboBoxModel<SkeletonBone>(bone.getFrame().getArrayOfBonesExcept(bone));
		otherBones.setModel(model);
		otherBones.setSelectedItem(bone.getParentBone());
		if(bone.getParentSyncedBone() != null)
		{
			rename.setEnabled(false);
			otherBones.setEnabled(false);
		}
		panel.add(new JSeparator());

		panel.add(xPosPanel);
		xPos.setValue(bone.getX());
		panel.add(yPosPanel);
		yPos.setValue(bone.getY());
		panel.add(rotPanel);
		rotation.setValue(Math.round(bone.getRotation()));
		panel.add(new JSeparator());
		
		panel.add(spritesPanel);
		panel.add(Box.createRigidArea(new Dimension(0,100)));
		
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
					TreePath path = controls.getTree().getSelectionPath();
					try {
						SkeletonBone bone = (SkeletonBone)selectedNode;
						Sprite newSprite = new Sprite("Sprite", bone);
						model.insertNodeInto((MutableTreeNode)newSprite,
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
		});	
		xPos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bone.setLocation(Double.parseDouble(xPos.getText()), bone.getY(), true);
				controls.getSkeleton().getDrawingArea().getDrawArea().repaint();
			}
		});
		yPos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bone.setLocation(bone.getX(), Double.parseDouble(yPos.getText()), true);
				controls.getSkeleton().getDrawingArea().getDrawArea().repaint();
			}
		});
		rotation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bone.rotate(Double.parseDouble(rotation.getText()));
				controls.getSkeleton().getDrawingArea().getDrawArea().repaint();
			}
		});
	}

	@Override 
	protected void updateVisibleInfo(final SkeletonNode node)
	{
		SkeletonBone bone = (SkeletonBone)node;
		if (bone != null)
		{
			xPos.setValue(bone.getX());
			yPos.setValue(bone.getY());
			rotation.setValue(Math.round(bone.getRotation()));
		}
	}
}
