package com.wings2d.editor.ui.skeleton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.geom.PathIterator;
import java.sql.Connection;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.wings2d.editor.objects.skeleton.DrawMode;
import com.wings2d.editor.objects.skeleton.Skeleton;
import com.wings2d.editor.ui.Editor;
import com.wings2d.editor.ui.FixedGrid;
import com.wings2d.editor.ui.UIPanel;
import com.wings2d.editor.ui.skeleton.treecontrols.SkeletonTreeControls;

public class SkeletonEdit extends UIPanel<SkeletonEdit>{
	public static String CARD_ID = "Skeleton";
	private FixedGrid centerPanel;
	
	private Skeleton skeleton;
	private DrawMode drawMode;
	private DrawMode lastBoneDrawMode;
	private DrawMode lastSpriteDrawMode;
	private int pathAddMode;
	
	private SkeletonToolBar bar;
	private SkeletonTree tree;
	private SkeletonTreeControls treeControls;
	private SkeletonDrawing drawingArea;
	private RenderArea renderArea;

	public SkeletonEdit(final Editor edit, final Connection con) {
		super(edit);
		drawMode = DrawMode.BONE_MOVE;
		lastBoneDrawMode = DrawMode.BONE_MOVE;
		lastSpriteDrawMode = DrawMode.SPRITE_MOVE;
		pathAddMode = PathIterator.SEG_LINETO;

		panel.setLayout(new BorderLayout());
		
		centerPanel = new FixedGrid(6, 4);
		centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		bar = new SkeletonToolBar(this);
		panel.add(bar.getToolbar(), BorderLayout.NORTH);
		
		tree = new SkeletonTree(this);
		drawingArea = new SkeletonDrawing(this);	
		treeControls = new SkeletonTreeControls(this, con);
		renderArea = new RenderArea(this);
		
		centerPanel.addChild(tree.getPanel(), 0, 0, 1, 4);
		centerPanel.addChild(treeControls.getPanel(), 1, 0, 1, 2);
		centerPanel.addChild(drawingArea.getPanel(), 2, 0, 2, 2);
		centerPanel.addChild(renderArea.getPanel(), 4, 0, 2, 2);
		
		panel.add(centerPanel, BorderLayout.CENTER);
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
		drawingArea.getControls().setControls(mode);
		drawingArea.getDrawingPanel().getPanel().repaint();
		if (mode == DrawMode.BONE_MOVE || mode == DrawMode.BONE_ROTATE)
		{
			lastBoneDrawMode = mode;
		}
		else if (mode == DrawMode.SPRITE_MOVE || mode == DrawMode.SPRITE_EDIT)
		{
			lastSpriteDrawMode = mode;
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
	public DrawMode getLastSpriteDrawMode()
	{
		return lastSpriteDrawMode;
	}
	public SkeletonToolBar getTopBar()
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
	public SkeletonDrawing getDrawingArea()
	{
		return drawingArea;
	}
	public RenderArea getRenderArea()
	{
		return renderArea;
	}
	public int getPathAddMode() {
		return pathAddMode;
	}
	public void setPathAddMode(final int mode) {
		this.pathAddMode = mode;
	}
}
