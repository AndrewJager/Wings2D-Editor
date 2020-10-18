package com.wings2d.editor.ui.skeleton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.wings2d.editor.objects.skeleton.Skeleton;
import com.wings2d.editor.objects.skeleton.SkeletonAnimation;
import com.wings2d.editor.objects.skeleton.SkeletonBone;
import com.wings2d.editor.objects.skeleton.SkeletonFrame;
import com.wings2d.editor.objects.skeleton.SkeletonNode;
import com.wings2d.editor.objects.skeleton.SkeletonTreeModelListener;
import com.wings2d.editor.objects.skeleton.Sprite;
import com.wings2d.editor.ui.skeleton.treecontrols.SkeletonTreeControls;

public class SkeletonTree extends SkeletonUIElement{
	private JTree tree;
	private SkeletonTreeModelListener treeListener;
	private JScrollPane scrollPane;
	private JPopupMenu menu;
	private JMenuItem editItem, moveUpItem, moveDownItem;

	public SkeletonTree(final SkeletonEdit edit, final Rectangle bounds) {
		super(edit, bounds);

		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));	
		
		tree = new JTree();
		tree.setEditable(true);
		treeListener = new SkeletonTreeModelListener(tree);
		tree.getModel().addTreeModelListener(treeListener);	
		
		scrollPane = new JScrollPane(tree);
		panel.add(scrollPane, BorderLayout.CENTER);
		
		menu = new JPopupMenu();
		editItem = new JMenuItem("Edit");
		menu.add(editItem);
		moveUpItem = new JMenuItem("Move up");
		menu.add(moveUpItem);
		moveDownItem = new JMenuItem("Move down");
		menu.add(moveDownItem);
		tree.setComponentPopupMenu(menu);
	}
	
	public void setSkeleton(final Skeleton skeleton)
	{
		DefaultTreeModel newModel = new DefaultTreeModel(skeleton);
		newModel.addTreeModelListener(treeListener);
		tree.setModel(newModel);
	}
	/** 
	 * Gets the currently selected frame, the parent frame of the selected item, or the first frame of the animation,
	 * depending on the item that is selected. Returns null if no selection.
	 * @return {@link SkeletonFrame} to render, or null if no item selected
	 */
	public SkeletonFrame getFrameToRender()
	{
		SkeletonFrame renderFrame = null;
		SkeletonNode selectedNode = (SkeletonNode)tree.getLastSelectedPathComponent();
		if (selectedNode != null)
		{
			if (selectedNode instanceof Skeleton)
			{
				Skeleton skel = (Skeleton)selectedNode;
				renderFrame = skel.getMasterFrame();
			}
			else if (selectedNode instanceof SkeletonAnimation)
			{
				SkeletonAnimation anim = (SkeletonAnimation)selectedNode;
				renderFrame = anim.getFrames().get(0);
			}
			else if (selectedNode instanceof SkeletonFrame)
			{
				renderFrame = (SkeletonFrame)selectedNode;
			}
			else if (selectedNode instanceof SkeletonBone)
			{
				SkeletonBone bone = (SkeletonBone)selectedNode;
				renderFrame = bone.getFrame();
			}
			else if (selectedNode instanceof Sprite)
			{
				Sprite sprite = (Sprite)selectedNode;
				renderFrame = sprite.getBone().getFrame();
			}
		}
		return renderFrame;
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
						treeControls.showSkeletonControls(selectedNode);
					}
					if (selectedNode instanceof SkeletonAnimation)
					{
						treeControls.showAnimationControls(selectedNode);
					}
					else if (selectedNode instanceof SkeletonFrame)
					{
						treeControls.showFrameControls(selectedNode);
					}
					else if (selectedNode instanceof SkeletonBone)
					{
						treeControls.showBoneControls(selectedNode);
						SkeletonBone bone = (SkeletonBone)selectedNode;
						bone.getFrame().deselectAllBones();
						bone.setIsSelected(true);
					}
					else if (selectedNode instanceof Sprite)
					{
						treeControls.showSpriteControls(selectedNode);
						SkeletonBone bone = (SkeletonBone)selectedNode.getParent();
						bone.setSelectedSprite((Sprite)selectedNode);
					}
					skeleton.getDrawingArea().getDrawArea().repaint();
				}
			}
		});
		tree.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() >= 2) // Double click
				{
					TreePath selectionPath = tree.getSelectionPath();
					tree.startEditingAtPath(selectionPath);
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e))
				{
                    int row = tree.getRowForLocation(e.getPoint().x, e.getPoint().y);
                    if(row != -1)
                    {
                        tree.setSelectionRow(row);
                    }
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
		});
		editItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TreePath selectionPath = tree.getSelectionPath();
				tree.startEditingAtPath(selectionPath);
			}
		});
		moveUpItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SkeletonNode selectedNode = (SkeletonNode)tree.getLastSelectedPathComponent();
				selectedNode.moveUp();
				DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
				model.reload();
			}
		});
		moveDownItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SkeletonNode selectedNode = (SkeletonNode)tree.getLastSelectedPathComponent();
				selectedNode.moveDown();
				DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
				model.reload();
			}
		});
	}

	public JTree getTree()
	{
		return tree;
	}
}
