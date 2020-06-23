package editor.ui.skeleton;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class SkeletonTree extends SkeletonUIElement{
	private JTree tree;
	private DefaultMutableTreeNode mainNode;
	private JButton addAnim;

	public SkeletonTree(SkeletonEdit edit, Rectangle bounds) {
		super(edit, bounds);
		panel.setBackground(Color.LIGHT_GRAY);
		
		DefaultMutableTreeNode mainNode = new DefaultMutableTreeNode("Hello");
		tree = new JTree(mainNode);
		panel.add(tree);
		
		addAnim = new JButton("New Animation");
		panel.add(addAnim);
	}

	public void createEvents() {
		
	}

}
