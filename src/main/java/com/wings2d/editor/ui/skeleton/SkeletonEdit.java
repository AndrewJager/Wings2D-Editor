package com.wings2d.editor.ui.skeleton;

import java.awt.BorderLayout;
import java.sql.Connection;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

import com.wings2d.editor.objects.skeleton.DrawMode;
import com.wings2d.editor.objects.skeleton.Skeleton;
import com.wings2d.editor.ui.Editor;
import com.wings2d.editor.ui.UIPanel;
import com.wings2d.editor.ui.skeleton.treecontrols.SkeletonTreeControls;

public class SkeletonEdit extends UIPanel<SkeletonEdit>{
	public static String CARD_ID = "Skeleton";
	private JPanel centerPanel;
	
	private Skeleton skeleton;
	private DrawMode drawMode;
	private DrawMode lastBoneDrawMode;
	private DrawMode lastSpriteDrawMode;
	
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

		panel.setLayout(new BorderLayout());
		
		centerPanel = new JPanel();
		centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		bar = new SkeletonToolBar(this);
		panel.add(bar.getToolbar(), BorderLayout.NORTH);
		
		tree = new SkeletonTree(this);
		
		drawingArea = new SkeletonDrawing(this);	
		treeControls = new SkeletonTreeControls(this, con);
		renderArea = new RenderArea(this);
		
		JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tree.getPanel(), treeControls.getPanel());
		JSplitPane pane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pane, drawingArea.getPanel());
		JSplitPane pane3 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pane2, renderArea.getPanel());
		centerPanel.add(pane3);
		
		panel.add(centerPanel, BorderLayout.WEST);
		
		drawingArea.setTreeControls(treeControls);
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
//		drawingControls.setControls(mode);
//		drawingArea.getDrawArea().repaint();
//		if (mode == DrawMode.BONE_MOVE || mode == DrawMode.BONE_ROTATE)
//		{
//			lastBoneDrawMode = mode;
//		}
//		else if (mode == DrawMode.SPRITE_MOVE || mode == DrawMode.SPRITE_EDIT)
//		{
//			lastSpriteDrawMode = mode;
//		}
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
}
