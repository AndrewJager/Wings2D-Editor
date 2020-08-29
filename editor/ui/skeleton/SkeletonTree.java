package editor.ui.skeleton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import editor.objects.ISkeleton;
import editor.objects.Skeleton;
import editor.objects.SkeletonPiece;

public class SkeletonTree extends SkeletonUIElement{
	private JTree tree;
	private JScrollPane scrollPane;

	public SkeletonTree(SkeletonEdit edit, Rectangle bounds) {
		super(edit, bounds);
		
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setLayout(new BorderLayout());
		
		Skeleton mainNode = new Skeleton("Skeleton");

		tree = new JTree(mainNode);
		tree.setEditable(true);
		tree.setRootVisible(false);
		
		scrollPane = new JScrollPane(tree);
		panel.add(scrollPane, BorderLayout.CENTER);
	}

	public void createEvents() {
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e)
			{
				SkeletonTreeControls treeControls = skeleton.getTreeControls();
				ISkeleton selectedNode = (ISkeleton)tree.getLastSelectedPathComponent();
				if (selectedNode != null)
				{
					switch(selectedNode.getTreeLevel())
					{
					case 1:
						treeControls.setupControls(SkeletonPiece.ANIMATION);
						break;
					case 2: 
						treeControls.setupControls(SkeletonPiece.FRAME);
						break;
					default:
						treeControls.setupControls(SkeletonPiece.ANIMATION);
					}
				}
			}
		});
	}

	public JTree getTree()
	{
		return tree;
	}
}
