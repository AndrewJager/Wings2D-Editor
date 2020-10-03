package com.wings2d.editor.ui.skeleton.treecontrols;

import com.wings2d.editor.objects.skeleton.SkeletonNode;
import com.wings2d.editor.objects.skeleton.Sprite;

public class SpriteControls extends SkeletonTreeControlsUIElement{
	public static final String CARD_ID = "Sprite";

	public SpriteControls(final SkeletonTreeControls controls) {
		super(controls);
	}

	@Override
	protected void updatePanelInfo(final SkeletonNode node) {
		Sprite sprite = (Sprite)node;
		addLabel(sprite.toString());
	}

	@Override
	protected void createOtherEvents() {
		
	}

}
