package editor.ui.skeleton;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class SkeletonTreeControls extends SkeletonUIElement{
	public enum controlType {
		PARENT,
		ANIMATION,
		FRAME,
	}
	private JButton addAnim, deleteAnim, renameAnim;
	private JTree tree;
	
	public SkeletonTreeControls(SkeletonEdit edit, Rectangle bounds, JTree skeletonTree) {
		super(edit, bounds);
		this.tree = skeletonTree;
		
		panel.setBackground(Color.DARK_GRAY);
		
		// Top-level controls
		addAnim = new JButton("New Animation");
	
		// Animation controls
		deleteAnim = new JButton("Delete Animation");
		renameAnim = new JButton("Rename Animation");
		
		// Frame controls 
		
		setupControls(controlType.PARENT);
	}
	
	public void setupControls(controlType type)
	{
		panel.removeAll();
		switch (type)
		{
		case PARENT:
			panel.add(addAnim);
			break;
		case ANIMATION:
			panel.add(deleteAnim);
			panel.add(renameAnim);
			break;
		case FRAME:
			break;
		}
		panel.revalidate();
		panel.repaint();
	}
	
	public void createEvents()
	{
		addAnim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
				DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
				root.add(new DefaultMutableTreeNode("child"));
				model.reload();
			}
		});
	}
}
