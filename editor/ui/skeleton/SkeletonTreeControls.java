package editor.ui.skeleton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import editor.objects.skeleton.SkeletonNode;
import editor.objects.skeleton.Skeleton;
import editor.objects.skeleton.SkeletonAnimation;
import editor.objects.skeleton.SkeletonBone;
import editor.objects.skeleton.SkeletonFrame;
import editor.objects.skeleton.SkeletonPiece;

public class SkeletonTreeControls extends SkeletonUIElement{
	private JButton addAnim, delete, addFrame, rename, addBone;
	private JLabel xPos, yPos, rotation;
	private JTree tree;
	private SkeletonDrawingArea drawingArea;
	private JSeparator line;
	private int SEPARATOR_WIDTH = 5;

	public SkeletonTreeControls(SkeletonEdit edit, Rectangle bounds) {
		super(edit, bounds);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.tree = edit.getSkeletonTree().getTree();
		this.drawingArea = edit.getDrawingArea();
		
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		addAnim = new JButton("New Animation");
		delete = new JButton("Delete Animation");
		rename = new JButton("Rename Animation");
		addFrame = new JButton("New Frame");
		addBone = new JButton("New Bone");
		
		xPos = new JLabel("X: ", JLabel.CENTER);
		yPos = new JLabel("Y: ", JLabel.CENTER);
		rotation = new JLabel("Rotation: ", JLabel.CENTER);
		
		setupControls(SkeletonPiece.SKELETON);
	}
	
	public void setupControls(SkeletonPiece type)
	{
		SkeletonNode selectedNode = (SkeletonNode)tree.getLastSelectedPathComponent();
		panel.removeAll();
		switch (type)
		{
		case SKELETON:
			if (selectedNode != null)
			{
				addLabel(selectedNode.toString());
				addNameLine();
			}
			panel.add(addAnim);
			line = new JSeparator();
			line.setPreferredSize(new Dimension(panel.getWidth(), SEPARATOR_WIDTH));
			panel.add(line);
			break;
		case ANIMATION:
			panel.add(addAnim);
			line = new JSeparator();
			line.setPreferredSize(new Dimension(panel.getWidth(), SEPARATOR_WIDTH));
			panel.add(line);
			if (selectedNode != null)
			{
				addLabel(selectedNode.toString());
				addNameLine();
			}
			panel.add(rename);
			rename.setText("Rename Animation");
			panel.add(delete);
			delete.setText("Delete Animation");
			line = new JSeparator();
			line.setPreferredSize(new Dimension(panel.getWidth(), SEPARATOR_WIDTH));
			panel.add(line);
			panel.add(addFrame);
			break;
		case FRAME:
			if (selectedNode != null)
			{
				addLabel(selectedNode.toString());
				addNameLine();
			}
			panel.add(rename);
			rename.setText("Rename Frame");
			panel.add(delete);
			delete.setText("Delete Frame");
			SkeletonFrame frame = (SkeletonFrame)selectedNode;
			if (frame.getParentSyncedFrame() != null)
			{
				addLabel(frame.getParentSyncedFrame().toString());
			}
			else
			{
				addLabel("No parent sync frame");
			}
			createList(frame.getSyncedFrameNames());
			line = new JSeparator();
			line.setPreferredSize(new Dimension(panel.getWidth(), SEPARATOR_WIDTH));
			panel.add(line);
			panel.add(addBone);
			
			drawingArea.setSelectedFrame(frame);
			drawingArea.getDrawArea().setDrawItem(frame);
			break;
		case BONE:
			if (selectedNode != null)
			{
				addLabel(selectedNode.toString());
				addNameLine();
			}
			SkeletonBone bone = (SkeletonBone)selectedNode;
			panel.add(rename);
			rename.setText("Rename Bone");
			addLabel(xPos, "X: " + bone.getX());
			addLabel(yPos, "Y: " + bone.getY());
			addLabel(rotation, "Rotation: " + Math.round(bone.getRotation()));
			JLabel parentBoneLabel = new JLabel("Parent bone:");
			panel.add(parentBoneLabel);
			JComboBox<SkeletonBone> otherBones = new JComboBox<SkeletonBone>(bone.getFrame().getArrayOfBonesExcept(bone));
			otherBones.setSelectedItem(bone.getParentBone());
			otherBones.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					bone.setParentBone((SkeletonBone)otherBones.getSelectedItem());
				}
			});
			panel.add(otherBones);
			
			bone.getFrame().setSelectedBone(bone);
			drawingArea.setSelectedFrame(bone.getFrame());
			drawingArea.getDrawArea().setDrawItem(bone.getFrame());
			break;
		default:
			break;
		}
		panel.revalidate();
		panel.repaint();
	}
	private void addNameLine()
	{
		JSeparator line = new JSeparator();
		line.setPreferredSize(new Dimension((int)(panel.getWidth() * 0.6), 1));
		panel.add(line);
	}
	private void addLabel(String text)
	{
		JLabel label = new JLabel(text, JLabel.CENTER);
		label.setPreferredSize(new Dimension(panel.getWidth(), (int)label.getPreferredSize().getHeight()));
		panel.add(label);
	}
	private void addLabel(JLabel label, String text)
	{
		label.setText(text);
		label.setPreferredSize(new Dimension(panel.getWidth(), (int)label.getPreferredSize().getHeight()));
		panel.add(label);
	}
	private void createList(String[] data)
	{
		JList<String> list = new JList<String>(data);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(5);
		list.setFixedCellWidth(80);
		JScrollPane pane = new JScrollPane(list);
		panel.add(pane);
	}
	public void updateBoneInfo()
	{
		SkeletonNode selectedNode = (SkeletonNode)tree.getLastSelectedPathComponent();
		SkeletonBone bone = (SkeletonBone)selectedNode;
		if (bone != null)
		{
			xPos.setText("X: " + bone.getX());
			yPos.setText("Y: " + bone.getY());
			rotation.setText("Rotation: " + Math.round(bone.getRotation()));
		}
	}
	
	public void createEvents()
	{
		addAnim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String animName = (String)JOptionPane.showInputDialog(panel, "","Animation Name",
	    				JOptionPane.PLAIN_MESSAGE, null, null, "Animation");
				if (animName != null)
				{
					DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
					Skeleton root = (Skeleton) model.getRoot();
					try {
						SkeletonAnimation newAnim = new SkeletonAnimation(animName, root);	
						model.insertNodeInto((MutableTreeNode)newAnim, root, root.getChildCount());
						model.reload();
						TreePath path = new TreePath(root).pathByAddingChild(newAnim);
						tree.setSelectionPath(path);
					}
					catch (IllegalArgumentException exception){
						JOptionPane.showMessageDialog(panel, exception.getMessage(), "Insert Failed!", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		addFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SkeletonNode selectedNode = (SkeletonNode)tree.getLastSelectedPathComponent();
				if (selectedNode != null)
				{
					TreePath path = tree.getSelectionPath();
					String frameName = (String)JOptionPane.showInputDialog(panel, "","Frame Name",
		    				JOptionPane.PLAIN_MESSAGE, null, null, "Frame");
					if (frameName != null)
					{
						DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
						SkeletonAnimation anim = (SkeletonAnimation)selectedNode;
						try {
							SkeletonFrame newFrame = new SkeletonFrame(frameName, anim);
							if (anim.getChildCount() == 0)
							{
								Skeleton skeleton = (Skeleton)model.getRoot();
								newFrame.setParentSyncedFrame(skeleton.getMasterFrame());
							}
							else
							{
								newFrame.setParentSyncedFrame((SkeletonFrame)anim.getChildAt(anim.getChildCount() - 1));
							}
							model.insertNodeInto((MutableTreeNode)newFrame,
									selectedNode, selectedNode.getChildCount());
							model.reload();
											
							tree.expandPath(path);
							tree.setSelectionPath(path.pathByAddingChild(selectedNode.getChildAt(selectedNode.getChildCount() - 1)));
						}
						catch (IllegalArgumentException exception) {
							JOptionPane.showMessageDialog(panel, exception.getMessage(), "Insert Failed!", JOptionPane.ERROR_MESSAGE);
						}
					}
				}			
			}
		});
		rename.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SkeletonNode selectedNode = (SkeletonNode)tree.getLastSelectedPathComponent();
				if (selectedNode != null)
				{
					TreePath path = tree.getSelectionPath();
					String newName = (String)JOptionPane.showInputDialog(panel, "","Rename",
		    				JOptionPane.PLAIN_MESSAGE, null, null, selectedNode.toString());
					if (newName != null)
					{
						selectedNode.setName(newName);
						DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
						model.reload();
						
						tree.expandPath(path);
						tree.setSelectionPath(path);
					}
				}
			}
		});
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SkeletonNode selectedNode = (SkeletonNode)tree.getLastSelectedPathComponent();
				if (selectedNode != null)
				{
					DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
					model.removeNodeFromParent(selectedNode);
				}
			}
		});
		addBone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SkeletonNode selectedNode = (SkeletonNode)tree.getLastSelectedPathComponent();
				if (selectedNode != null)
				{
					TreePath path = tree.getSelectionPath();
					String boneName = (String)JOptionPane.showInputDialog(panel, "","Bone Name",
		    				JOptionPane.PLAIN_MESSAGE, null, null, "Bone");
					if (boneName != null)
					{
						DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
						try {
							model.insertNodeInto((MutableTreeNode)new SkeletonBone(boneName, (SkeletonFrame)selectedNode),
									selectedNode, selectedNode.getChildCount());
							model.reload();
											
							tree.expandPath(path);
							tree.setSelectionPath(path.pathByAddingChild(selectedNode.getChildAt(selectedNode.getChildCount() - 1)));
						}
						catch (IllegalArgumentException exception) {
							JOptionPane.showMessageDialog(panel, exception.getMessage(), "Insert Failed!", JOptionPane.ERROR_MESSAGE);
						}
					}
				}			
			}
		});
	}
}
