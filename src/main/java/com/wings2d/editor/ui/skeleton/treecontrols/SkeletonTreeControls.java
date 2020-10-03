package com.wings2d.editor.ui.skeleton.treecontrols;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import com.wings2d.editor.objects.skeleton.Skeleton;
import com.wings2d.editor.objects.skeleton.SkeletonAnimation;
import com.wings2d.editor.objects.skeleton.SkeletonBone;
import com.wings2d.editor.objects.skeleton.SkeletonFrame;
import com.wings2d.editor.objects.skeleton.SkeletonNode;
import com.wings2d.editor.objects.skeleton.SkeletonPiece;
import com.wings2d.editor.objects.skeleton.Sprite;
import com.wings2d.editor.ui.skeleton.SkeletonDrawingArea;
import com.wings2d.editor.ui.skeleton.SkeletonEdit;
import com.wings2d.editor.ui.skeleton.SkeletonUIElement;

public class SkeletonTreeControls extends SkeletonUIElement{
	private CardLayout cards;
	private SkeletonControls skeletonControls;
	private AnimationControls animationControls;
	private FrameControls frameControls;
	private BoneControls boneControls;
	private SpriteControls spriteControls;
	private SkeletonTreeControlsUIElement currentCard;
	private JTree tree;
	private SkeletonDrawingArea drawingArea;
	

	public SkeletonTreeControls(final SkeletonEdit edit, final Rectangle bounds) {
		super(edit, bounds);
		cards = new CardLayout();
		panel.setLayout(cards);
		
		skeletonControls = new SkeletonControls(this);
		animationControls = new AnimationControls(this);
		frameControls = new FrameControls(this);
		boneControls = new BoneControls(this);
		spriteControls = new SpriteControls(this);
		
		panel.add(skeletonControls.getPanel(), SkeletonControls.CARD_ID);
		panel.add(animationControls.getPanel(), AnimationControls.CARD_ID);
		panel.add(frameControls.getPanel(), FrameControls.CARD_ID);
		panel.add(boneControls.getPanel(), BoneControls.CARD_ID);
		panel.add(spriteControls.getPanel(), SpriteControls.CARD_ID);
		
		this.tree = edit.getSkeletonTree().getTree();
		this.drawingArea = edit.getDrawingArea();
		
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		showSkeletonControls(null);
	}
	
	public void showSkeletonControls(final SkeletonNode node)
	{
		cards.show(panel, SkeletonControls.CARD_ID);
		skeletonControls.updateInfo(node);
		currentCard = skeletonControls;
	}
	public void showAnimationControls(final SkeletonNode node)
	{
		cards.show(panel, AnimationControls.CARD_ID);
		animationControls.updateInfo(node);
		currentCard = animationControls;
	}
	public void showFrameControls(final SkeletonNode node)
	{
		cards.show(panel, FrameControls.CARD_ID);
		frameControls.updateInfo(node);
		currentCard = frameControls;
	}
	public void showBoneControls(final SkeletonNode node)
	{
		cards.show(panel, BoneControls.CARD_ID);
		boneControls.updateInfo(node);
		currentCard = boneControls;
	}
	public void showSpriteControls(final SkeletonNode node)
	{
		cards.show(panel, SpriteControls.CARD_ID);
		spriteControls.updateInfo(node);
		currentCard = spriteControls;
	}

	public JTree getTree()
	{
		return skeleton.getSkeletonTree().getTree();
	}
	public SkeletonDrawingArea getDrawingArea()
	{
		return drawingArea;
	}


	public void updateNodeInfo(final SkeletonNode node)
	{
		currentCard.updateVisibleInfo(node);
	}
	
	public void createEvents()
	{
		skeletonControls.createEvents();
		animationControls.createEvents();
		frameControls.createEvents();
		boneControls.createEvents();
		spriteControls.createEvents();
	}
}
