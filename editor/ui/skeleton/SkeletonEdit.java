package editor.ui.skeleton;

import java.awt.Rectangle;

import editor.ui.Editor;
import editor.ui.UIPanel;

public class SkeletonEdit extends UIPanel{
	private SkeletonTree tree;
	private SkeletonTreeControls treeControls;

	public SkeletonEdit(Editor edit) {
		super(edit);
		
		tree = new SkeletonTree(this, new Rectangle(0, 0, 200, 400));
		elements.add(tree);
		treeControls = new SkeletonTreeControls(this, new Rectangle(200, 0, 200, 400), tree.getTree());
		elements.add(treeControls);
	}
	
	public SkeletonTreeControls getTreeControls()
	{
		return treeControls;
	}

}
