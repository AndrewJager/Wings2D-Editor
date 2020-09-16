package editor.ui.skeleton;

import java.awt.Rectangle;

import editor.ui.Editor;
import editor.ui.UIPanel;

public class SkeletonEdit extends UIPanel{
	public static String CARD_ID = "Skeleton";
	
	private SkeletonTree tree;
	private SkeletonTreeControls treeControls;
	private SkeletonDrawingArea drawingArea;

	public SkeletonEdit(final Editor edit) {
		super(edit);
		
		tree = new SkeletonTree(this, new Rectangle(0, 0, 200, 400));
		drawingArea = new SkeletonDrawingArea(this, new Rectangle(420, 0, 400, 400));		
		treeControls = new SkeletonTreeControls(this, new Rectangle(200, 0, 200, 400));
		
		drawingArea.setControls(treeControls);
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
	public Editor getEditor()
	{
		return editor;
	}
}
