package com.wings2d.editor.ui.skeleton;

import java.awt.Rectangle;

import com.wings2d.editor.objects.skeleton.DrawMode;
import com.wings2d.editor.objects.skeleton.Skeleton;
import com.wings2d.editor.objects.skeleton.SkeletonPiece;
import com.wings2d.editor.ui.Editor;
import com.wings2d.editor.ui.UIPanel;
import com.wings2d.editor.ui.skeleton.treecontrols.SkeletonTreeControls;

public class SkeletonEdit extends UIPanel{
	public static String CARD_ID = "Skeleton";
	
	private Skeleton skeleton;
	private DrawMode drawMode;
	private DrawMode lastBoneDrawMode;
	
	private SkeletonTopBar bar;
	private SkeletonTree tree;
	private SkeletonTreeControls treeControls;
	private SkeletonDrawingArea drawingArea;
	private SkeletonDrawingControls drawingControls;

	public SkeletonEdit(final Editor edit) {
		super(edit);
		drawMode = DrawMode.BONE_MOVE;
		lastBoneDrawMode = DrawMode.BONE_MOVE;
		
		bar = new SkeletonTopBar(this, new Rectangle(0, 0, edit.frameStartWidth, 50));
		tree = new SkeletonTree(this, new Rectangle(0, 50, 200, 400));
		drawingArea = new SkeletonDrawingArea(this, new Rectangle(420, 50, 400, 400));		
		treeControls = new SkeletonTreeControls(this, new Rectangle(200, 50, 200, 400));
		drawingControls = new SkeletonDrawingControls(this, new Rectangle(420, 450, 400, 50));
		
		drawingArea.setControls(treeControls);
	}
	
	public void setCurrentSkeleton(final Skeleton s)
	{
		this.skeleton = s;
		tree.setSkeleton(s);
		treeControls.showSkeletonControls(s);
	}
	
	public Skeleton getSkeleton()
	{
		return skeleton;
	}
	public void setDrawMode(final DrawMode mode)
	{
		drawMode = mode;
		drawingControls.setControls(mode);
		drawingArea.getDrawArea().repaint();
		if (mode == DrawMode.BONE_MOVE || mode == DrawMode.BONE_ROTATE)
		{
			lastBoneDrawMode = mode;
		}
	}
	public DrawMode getDrawMode()
	{
		return drawMode;
	}
	public DrawMode getLastBoneDrawMode()
	{
		return lastBoneDrawMode;
	}
	public SkeletonTopBar getTopBar()
	{
		return bar;
	}
	public SkeletonTree getSkeletonTree()
	{
		return tree;
	}
	public SkeletonTreeControls getTreeControls()
	{
		return treeControls;
	}
	public SkeletonDrawingArea getDrawingArea()
	{
		return drawingArea;
	}
	public SkeletonDrawingControls getDrawingControls()
	{
		return drawingControls;
	}
}
