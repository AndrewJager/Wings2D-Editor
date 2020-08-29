package editor.ui.skeleton;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import editor.objects.ISkeleton;
import editor.objects.Skeleton;
import editor.objects.SkeletonAnimation;
import editor.objects.SkeletonFrame;
import editor.objects.SkeletonPiece;

public class SkeletonTreeControls extends SkeletonUIElement{
	private JButton addAnim, deleteAnim, renameAnim, addFrame;
	private JTree tree;
	
	public SkeletonTreeControls(SkeletonEdit edit, Rectangle bounds, JTree skeletonTree) {
		super(edit, bounds);
		this.tree = skeletonTree;
		
		panel.setBackground(Color.DARK_GRAY);	
	
		// Animation controls
		addAnim = new JButton("New Animation");
		deleteAnim = new JButton("Delete Animation");
		renameAnim = new JButton("Rename Animation");
		addFrame = new JButton("New Frame");
		
		// Frame controls 
		
		setupControls(SkeletonPiece.ANIMATION);
	}
	
	public void setupControls(SkeletonPiece type)
	{
		panel.removeAll();
		switch (type)
		{
		case ANIMATION:
			panel.add(addAnim);
			panel.add(deleteAnim);
			panel.add(renameAnim);
			panel.add(addFrame);
			break;
		case FRAME:
			break;
		default:
			break;
		}
		panel.revalidate();
		panel.repaint();
	}
	
	public void createEvents()
	{
		addAnim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String animName = (String)JOptionPane.showInputDialog(panel, "","Animation Name",
	    				JOptionPane.PLAIN_MESSAGE, null, null, "Animation");
				DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
				Skeleton root = (Skeleton) model.getRoot();
				root.insert((MutableTreeNode)new SkeletonAnimation(animName, root), root.getChildCount());
				model.reload();
			}
		});
		renameAnim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ISkeleton selectedNode = (ISkeleton)tree.getLastSelectedPathComponent();
				if (selectedNode != null)
				{
					String animName = (String)JOptionPane.showInputDialog(panel, "","Animation Name",
		    				JOptionPane.PLAIN_MESSAGE, null, null, selectedNode.toString());
					selectedNode.setName(animName);
					DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
					model.reload();
				}
			}
		});
		addFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ISkeleton selectedNode = (ISkeleton)tree.getLastSelectedPathComponent();
				if (selectedNode != null)
				{
					TreePath path = tree.getSelectionPath();
					String frameName = (String)JOptionPane.showInputDialog(panel, "","Frame Name",
		    				JOptionPane.PLAIN_MESSAGE, null, null, "Frame");
					selectedNode.insert((MutableTreeNode)new SkeletonFrame(frameName, (SkeletonAnimation)selectedNode), selectedNode.getChildCount());
					DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
					model.reload();
									
					tree.expandPath(path);
					tree.setSelectionPath(path);
				}			
			}
		});
	}
}
