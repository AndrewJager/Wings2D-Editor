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
		double[] imgScales = new double[] {1.0, 1.25, 1.50, 1.75, 2.0};
		int padding = 2;
		
		skeletonIcon = new ImageIcon(CharImageCreator.CreateMultiImage('\u2444', imgScales, iconSize, padding));
		masterFrameIcon = new ImageIcon(CharImageCreator.CreateMultiImage('\u2B1A', imgScales, iconSize, padding));
		animationIcon = new ImageIcon(CharImageCreator.CreateMultiImage('\u256C', imgScales, iconSize, padding));
		frameIcon = new ImageIcon(CharImageCreator.CreateMultiImage('\u25F0', imgScales, iconSize, padding));
		boneIcon = new ImageIcon(CharImageCreator.CreateMultiImage('\u2425', imgScales, iconSize, padding));
		spriteIcon = new ImageIcon(CharImageCreator.CreateMultiImage('\u2B53', imgScales, iconSize, padding));
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
