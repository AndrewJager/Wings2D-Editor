package editor.ui.skeleton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import editor.ui.skeleton.SkeletonTreeControls.controlType;

public class SkeletonTree extends SkeletonUIElement{
	private JTree tree;
	private DefaultMutableTreeNode mainNode;
	private JScrollPane scrollPane;

	public SkeletonTree(SkeletonEdit edit, Rectangle bounds) {
		super(edit, bounds);
		
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setLayout(new BorderLayout());
		
		DefaultMutableTreeNode mainNode = new DefaultMutableTreeNode("Hello");
		tree = new JTree(mainNode);
		tree.setEditable(true);
		
		scrollPane = new JScrollPane(tree);
		panel.add(scrollPane, BorderLayout.CENTER);
	}

	public void createEvents() {
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e)
			{
				SkeletonTreeControls treeControls = skeleton.getTreeControls();
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
				if (selectedNode != null)
				{
					switch(selectedNode.getLevel())
					{
					case 0:
						treeControls.setupControls(controlType.PARENT);
						break;
					case 1:
						treeControls.setupControls(controlType.ANIMATION);
						break;
					case 2: 
						treeControls.setupControls(controlType.FRAME);
						break;
					default:
						treeControls.setupControls(controlType.PARENT);
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
