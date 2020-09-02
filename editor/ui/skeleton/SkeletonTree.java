package editor.ui.skeleton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

import editor.objects.skeleton.SkeletonNode;
import editor.objects.skeleton.Skeleton;
import editor.objects.skeleton.SkeletonAnimation;
import editor.objects.skeleton.SkeletonBone;
import editor.objects.skeleton.SkeletonFrame;
import editor.objects.skeleton.SkeletonPiece;

public class SkeletonTree extends SkeletonUIElement{
	private JTree tree;
	private JScrollPane scrollPane;

	public SkeletonTree(SkeletonEdit edit, Rectangle bounds) {
		super(edit, bounds);

		panel.setLayout(new BorderLayout());
		
		Skeleton mainNode = new Skeleton("Skeleton");
		tree = new JTree(mainNode);
		JTextField textField = new JTextField();
		TreeCellEditor editor = new DefaultCellEditor(textField);
		tree.setCellEditor(editor);
		tree.setBackground(Color.LIGHT_GRAY);
		
		
		scrollPane = new JScrollPane(tree);
		panel.add(scrollPane, BorderLayout.CENTER);
	}

	public void createEvents() {
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e)
			{
				SkeletonTreeControls treeControls = skeleton.getTreeControls();
				SkeletonNode selectedNode = (SkeletonNode)tree.getLastSelectedPathComponent();
				if (selectedNode != null)
				{
					if (selectedNode instanceof Skeleton)
					{
						treeControls.setupControls(SkeletonPiece.PARENT);	
					}
					if (selectedNode instanceof SkeletonAnimation)
					{
						treeControls.setupControls(SkeletonPiece.ANIMATION);	
					}
					else if (selectedNode instanceof SkeletonFrame)
					{
						treeControls.setupControls(SkeletonPiece.FRAME);
					}
					else if (selectedNode instanceof SkeletonBone)
					{
						treeControls.setupControls(SkeletonPiece.BONE);
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
