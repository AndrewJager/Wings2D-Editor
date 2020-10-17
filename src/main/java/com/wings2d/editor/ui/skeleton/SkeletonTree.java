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
import com.wings2d.editor.objects.skeleton.Sprite;
import com.wings2d.editor.ui.skeleton.treecontrols.SkeletonTreeControls;

public class SkeletonTree extends SkeletonUIElement{
	private JTree tree;
	private JScrollPane scrollPane;

	public SkeletonTree(final SkeletonEdit edit, final Rectangle bounds) {
		super(edit, bounds);

		panel.setLayout(new BorderLayout());
		
		tree = new JTree();
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));		
		
		scrollPane = new JScrollPane(tree);
		panel.add(scrollPane, BorderLayout.CENTER);
	}
	
	public void setSkeleton(final Skeleton skeleton)
	{
		DefaultTreeModel newModel = new DefaultTreeModel(skeleton);
		tree.setModel(newModel);
	}
	/** 
	 * Gets the currently selected frame, the parent frame of the selected item, or the first frame of the animation,
	 * depending on the item that is selected. Returns null if no selection.
	 * @return {@link SkeletonFrame} to render, or null if no item selected
	 */
	public SkeletonFrame getFrameToRender()
	{
		SkeletonFrame renderFrame = null;
		SkeletonNode selectedNode = (SkeletonNode)tree.getLastSelectedPathComponent();
		if (selectedNode != null)
		{
			if (selectedNode instanceof Skeleton)
			{
				Skeleton skel = (Skeleton)selectedNode;
				renderFrame = skel.getMasterFrame();
			}
			else if (selectedNode instanceof SkeletonAnimation)
			{
				SkeletonAnimation anim = (SkeletonAnimation)selectedNode;
				renderFrame = anim.getFrames().get(0);
			}
			else if (selectedNode instanceof SkeletonFrame)
			{
				renderFrame = (SkeletonFrame)selectedNode;
			}
			else if (selectedNode instanceof SkeletonBone)
			{
				SkeletonBone bone = (SkeletonBone)selectedNode;
				renderFrame = bone.getFrame();
			}
			else if (selectedNode instanceof Sprite)
			{
				Sprite sprite = (Sprite)selectedNode;
				renderFrame = sprite.getBone().getFrame();
			}
		}
		return renderFrame;
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
						treeControls.showSkeletonControls(selectedNode);
					}
					if (selectedNode instanceof SkeletonAnimation)
					{
						treeControls.showAnimationControls(selectedNode);
					}
					else if (selectedNode instanceof SkeletonFrame)
					{
						treeControls.showFrameControls(selectedNode);
					}
					else if (selectedNode instanceof SkeletonBone)
					{
						treeControls.showBoneControls(selectedNode);
						SkeletonBone bone = (SkeletonBone)selectedNode;
						bone.getFrame().deselectAllBones();
						bone.setIsSelected(true);
					}
					else if (selectedNode instanceof Sprite)
					{
						treeControls.showSpriteControls(selectedNode);
						SkeletonBone bone = (SkeletonBone)selectedNode.getParent();
						bone.setSelectedSprite((Sprite)selectedNode);
					}
					skeleton.getDrawingArea().getDrawArea().repaint();
				}
			}
		});
	}

	public JTree getTree()
	{
		return tree;
	}
}
