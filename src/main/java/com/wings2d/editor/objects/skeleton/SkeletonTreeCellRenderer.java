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
		CharImageCreator.ImageOptions options = new CharImageCreator.ImageOptions(10, new double[] {1.0, 1.25, 1.50, 1.75, 2.0}, 2);
		
		skeletonIcon = new ImageIcon(CharImageCreator.CreateMultiImage('\u2444', options));
		masterFrameIcon = new ImageIcon(CharImageCreator.CreateMultiImage('\u2B1A', options));
		animationIcon = new ImageIcon(CharImageCreator.CreateMultiImage('\u256C', options));
		frameIcon = new ImageIcon(CharImageCreator.CreateMultiImage('\u25F0', options));
		boneIcon = new ImageIcon(CharImageCreator.CreateMultiImage('\u2425', options));
		spriteIcon = new ImageIcon(CharImageCreator.CreateMultiImage('\u2B53', options));
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
