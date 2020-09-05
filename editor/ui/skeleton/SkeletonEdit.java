package editor.ui.skeleton;

import java.awt.Rectangle;

import editor.ui.Editor;
import editor.ui.UIPanel;

public class SkeletonEdit extends UIPanel{
	private SkeletonTree tree;
	private SkeletonTreeControls treeControls;
	private SkeletonDrawingArea drawingArea;

	public SkeletonEdit(Editor edit) {
		super(edit);
		
		tree = new SkeletonTree(this, new Rectangle(0, 0, 200, 400));
		elements.add(tree);
		drawingArea = new SkeletonDrawingArea(this, new Rectangle(420, 0, 400, 400));
		elements.add(drawingArea);
		
		treeControls = new SkeletonTreeControls(this, new Rectangle(200, 0, 200, 400));
		elements.add(treeControls);
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
}
