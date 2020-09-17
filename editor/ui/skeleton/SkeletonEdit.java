package editor.ui.skeleton;

import java.awt.Rectangle;

import editor.objects.skeleton.Skeleton;
import editor.objects.skeleton.SkeletonPiece;
import editor.ui.Editor;
import editor.ui.UIPanel;

public class SkeletonEdit extends UIPanel{
	public static String CARD_ID = "Skeleton";
	
	private Skeleton skeleton;
	
	private SkeletonTopBar bar;
	private SkeletonTree tree;
	private SkeletonTreeControls treeControls;
	private SkeletonDrawingArea drawingArea;

	public SkeletonEdit(final Editor edit) {
		super(edit);
		
		bar = new SkeletonTopBar(this, new Rectangle(0, 0, edit.frameStartWidth, 50));
		tree = new SkeletonTree(this, new Rectangle(0, 50, 200, 400));
		drawingArea = new SkeletonDrawingArea(this, new Rectangle(420, 50, 400, 400));		
		treeControls = new SkeletonTreeControls(this, new Rectangle(200, 50, 200, 400));
		
		drawingArea.setControls(treeControls);
	}
	
	public void setCurrentSkeleton(final Skeleton s)
	{
		this.skeleton = s;
		tree.setSkeleton(s);
		treeControls.setupControls(SkeletonPiece.SKELETON);
	}
	
	public Skeleton getSkeleton()
	{
		return skeleton;
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
}
