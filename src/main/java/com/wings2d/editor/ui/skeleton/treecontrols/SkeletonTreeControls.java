package com.wings2d.editor.ui.skeleton.treecontrols;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.Connection;

import javax.swing.BorderFactory;
import javax.swing.JTree;

import com.wings2d.editor.objects.skeleton.SkeletonNode;
import com.wings2d.editor.ui.UIElement;
import com.wings2d.editor.ui.skeleton.SkeletonDrawingPanel;
import com.wings2d.editor.ui.skeleton.SkeletonEdit;

public class SkeletonTreeControls extends UIElement<SkeletonEdit>{
	private CardLayout cards;
	private SkeletonControls skeletonControls;
	private AnimationControls animationControls;
	private FrameControls frameControls;
	private BoneControls boneControls;
	private SpriteControls spriteControls;
	private PointControls pointControls;
	private SkeletonTreeControlsUIElement currentCard;
	private SkeletonDrawingPanel drawingArea;

	public SkeletonTreeControls(final SkeletonEdit edit, final Connection con) {
		super(edit);
		cards = new CardLayout();
		panel.setLayout(cards);
		panel.setPreferredSize(new Dimension(200, 400));
		
		skeletonControls = new SkeletonControls(this, con);
		animationControls = new AnimationControls(this, con);
		frameControls = new FrameControls(this, con);
		boneControls = new BoneControls(this, con);
		spriteControls = new SpriteControls(this, con);
		pointControls = new PointControls(this, con);
		
		panel.add(skeletonControls.getPanel(), SkeletonControls.CARD_ID);
		panel.add(animationControls.getPanel(), AnimationControls.CARD_ID);
		panel.add(frameControls.getPanel(), FrameControls.CARD_ID);
		panel.add(boneControls.getPanel(), BoneControls.CARD_ID);
		panel.add(spriteControls.getPanel(), SpriteControls.CARD_ID);
		panel.add(pointControls.getPanel(), PointControls.CARD_ID);
		
		this.drawingArea = edit.getDrawingArea().getDrawingPanel();
		
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
	public void showPointControls(final SkeletonNode node)
	{
		cards.show(panel, PointControls.CARD_ID);
		pointControls.updateInfo(node);
		currentCard = pointControls;
	}

	public JTree getTree()
	{
		return getEditPanel().getSkeletonTree().getTree();
	}
	public SkeletonDrawingPanel getDrawingArea()
	{
		return drawingArea;
	}


	public void updateNodeInfo(final SkeletonNode node)
	{
		currentCard.updateVisibleInfo(node);
	}
	
	@Override
	public void createEvents()
	{
		skeletonControls.createEvents();
		animationControls.createEvents();
		frameControls.createEvents();
		boneControls.createEvents();
		spriteControls.createEvents();
		pointControls.createEvents();
	}
}
