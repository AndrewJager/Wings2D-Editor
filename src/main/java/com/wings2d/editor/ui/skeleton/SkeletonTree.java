package com.wings2d.editor.ui.skeleton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;

import com.wings2d.editor.objects.skeleton.Skeleton;
import com.wings2d.editor.objects.skeleton.SkeletonAnimation;
import com.wings2d.editor.objects.skeleton.SkeletonBone;
import com.wings2d.editor.objects.skeleton.SkeletonFrame;
import com.wings2d.editor.objects.skeleton.SkeletonNode;
import com.wings2d.editor.objects.skeleton.SkeletonPiece;
import com.wings2d.editor.objects.skeleton.Sprite;

public class SkeletonTree extends SkeletonUIElement{
	private JTree tree;
	private JScrollPane scrollPane;

	public SkeletonTree(final SkeletonEdit edit, final Rectangle bounds) {
		super(edit, bounds);

		panel.setLayout(new BorderLayout());
		
		tree = new JTree();
		JTextField textField = new JTextField();
		TreeCellEditor editor = new DefaultCellEditor(textField);
		tree.setCellEditor(editor);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));		
		
		scrollPane = new JScrollPane(tree);
		panel.add(scrollPane, BorderLayout.CENTER);
	}
	
	public void setSkeleton(final Skeleton skeleton)
	{
		DefaultTreeModel newModel = new DefaultTreeModel(skeleton);
		tree.setModel(newModel);
	}

	public void createEvents() {
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e)
			{
				SkeletonTreeControls treeControls = skeleton.getTreeControls();
				SkeletonNode selectedNode = (SkeletonNode)tree.getLastSelectedPathComponent();
				if (selectedNode != null)
				{
					if (selectedNode instanceof Skeleton)
					{
						treeControls.setupControls(SkeletonPiece.SKELETON);	
					}
					if (selectedNode instanceof SkeletonAnimation)
					{
						treeControls.setupControls(SkeletonPiece.ANIMATION);	
					}
					else if (selectedNode instanceof SkeletonFrame)
					{
						treeControls.setupControls(SkeletonPiece.FRAME);
					}
					else if (selectedNode instanceof SkeletonBone)
					{
						treeControls.setupControls(SkeletonPiece.BONE);
						SkeletonBone bone = (SkeletonBone)selectedNode;
						bone.getFrame().deselectAllBones();
						bone.setIsSelected(true);
					}
					else if (selectedNode instanceof Sprite)
					{
						treeControls.setupControls(SkeletonPiece.SPRITE);
						SkeletonBone bone = (SkeletonBone)selectedNode.getParent();
						bone.setSelectedSprite((Sprite)selectedNode);
					}
				}
			}
		});
	}

	public JTree getTree()
	{
		return tree;
	}
}
