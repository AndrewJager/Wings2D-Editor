package com.wings2d.editor.objects.skeleton;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.wings2d.framework.CharImageCreator;

public class SkeletonTreeCellRenderer extends DefaultTreeCellRenderer{
	private static final long serialVersionUID = 1L;
	private ImageIcon skeletonIcon, masterFrameIcon, animationIcon, frameIcon, boneIcon, spriteIcon;
	
	public SkeletonTreeCellRenderer()
	{
		super();
		int iconSize = 10;
		skeletonIcon = new ImageIcon(CharImageCreator.CreateImage('\u2444', iconSize, 0, true));
		masterFrameIcon = new ImageIcon(CharImageCreator.CreateImage('\u2B1A', iconSize, 0, true));
		animationIcon = new ImageIcon(CharImageCreator.CreateImage('\u256C', iconSize, 0, true));
		frameIcon = new ImageIcon(CharImageCreator.CreateImage('\u25F0', iconSize, 0, true));
		boneIcon = new ImageIcon(CharImageCreator.CreateImage('\u2425', iconSize, 0, true));
		spriteIcon = new ImageIcon(CharImageCreator.CreateImage('\u2B53', iconSize, 0, true));
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, selected,expanded, leaf, row, hasFocus);
		if (value instanceof Skeleton)
		{
			setIcon(skeletonIcon);
		}
		else if (value instanceof SkeletonMasterFrame)
		{
			setIcon(masterFrameIcon);
		}
		else if (value instanceof SkeletonAnimation)
		{
			setIcon(animationIcon);
		}
		else if (value instanceof SkeletonFrame)
		{
			setIcon(frameIcon);
		}
		else if (value instanceof SkeletonBone)
		{
			setIcon(boneIcon);
		}
		else if (value instanceof Sprite)
		{
			setIcon(spriteIcon);
		}
		return this;
	}
}
