package com.wings2d.editor.ui.skeleton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.wings2d.editor.objects.skeleton.Skeleton;
import com.wings2d.editor.objects.skeleton.SkeletonAnimation;
import com.wings2d.editor.objects.skeleton.SkeletonBone;
import com.wings2d.editor.objects.skeleton.SkeletonFrame;
import com.wings2d.editor.objects.skeleton.SkeletonMasterFrame;
import com.wings2d.editor.objects.skeleton.SkeletonNode;
import com.wings2d.editor.objects.skeleton.SkeletonTreeCellRenderer;
import com.wings2d.editor.objects.skeleton.SkeletonTreeModelListener;
import com.wings2d.editor.objects.skeleton.Sprite;
import com.wings2d.editor.objects.skeleton.SpritePoint;
import com.wings2d.editor.ui.edits.MoveDownInTree;
import com.wings2d.editor.ui.edits.MoveUpInTree;
import com.wings2d.editor.ui.skeleton.treecontrols.SkeletonTreeControls;

public class SkeletonTree extends SkeletonUIElement{
	private JTree tree;
	private SkeletonTreeModelListener treeListener;
	private JScrollPane scrollPane;
	private JPopupMenu menu;
	private JMenuItem editItem, moveUpItem, moveDownItem;
	private List<TreePath> expandedPaths;

	public SkeletonTree(final SkeletonEdit edit, final Rectangle bounds) {
		super(edit, bounds);

		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));	
		
		tree = new JTree();
		tree.setEditable(true);
		treeListener = new SkeletonTreeModelListener(tree, this.getSkeleton().getEditor());
		tree.getModel().addTreeModelListener(treeListener);	
		tree.setCellRenderer(new SkeletonTreeCellRenderer());
		
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
		
		expandedPaths = new ArrayList<TreePath>();
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
			else if (selectedNode instanceof SpritePoint) {
				SpritePoint point = (SpritePoint)selectedNode;
				renderFrame = point.getSprite().getBone().getFrame();
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
					else if (selectedNode instanceof SpritePoint) {
						treeControls.showPointControls(selectedNode);
						SpritePoint point = (SpritePoint)selectedNode;
						point.getSprite().setSelectedVertex(point.getSprite().getPoints().indexOf(point));
					}
					skeleton.getDrawingArea().getDrawArea().repaint();
				}
			}
		});
		tree.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {}
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
		menu.addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				SkeletonNode selectedNode = (SkeletonNode)tree.getLastSelectedPathComponent();
				editItem.setEnabled(true);
				moveUpItem.setEnabled(true);
				moveDownItem.setEnabled(true);
				if (selectedNode instanceof Skeleton)
				{
					moveUpItem.setEnabled(false);
					moveDownItem.setEnabled(false);
				}
				else if (selectedNode instanceof SkeletonMasterFrame)
				{
					editItem.setEnabled(false);
					moveUpItem.setEnabled(false);
					moveDownItem.setEnabled(false);
				}
				else if (selectedNode instanceof SkeletonFrame) {
					SkeletonFrame frame = (SkeletonFrame)selectedNode;
					if (frame.getParentSyncedFrame() != null) {
						editItem.setEnabled(false);
					}
				}
				else if(selectedNode instanceof SkeletonBone) {
					SkeletonBone bone = (SkeletonBone)selectedNode;
					if (bone.getParentSyncedBone() != null) {
						editItem.setEnabled(false);
					}
				}
				else if(selectedNode instanceof Sprite) {
					Sprite sprite = (Sprite)selectedNode;
					if (sprite.getBone().getParentSyncedBone() != null) {
						editItem.setEnabled(false);
					}
				}
				else if (selectedNode instanceof SpritePoint) {
					editItem.setEnabled(false);
					moveUpItem.setEnabled(false);
					moveDownItem.setEnabled(false);
				}

				if (selectedNode.getParent() != null)
				{
					if (selectedNode.getParent().getIndex(selectedNode) == 0
							|| (selectedNode.getParent().getIndex(selectedNode) == 1 && selectedNode instanceof SkeletonAnimation))
					{
						moveUpItem.setEnabled(false);
					}
	
					if (selectedNode.getParent().getIndex(selectedNode) >= selectedNode.getParent().getChildCount() - 1)
					{
						moveDownItem.setEnabled(false);
					}
				}
			}
			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {}
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
				getSkeleton().getEditor().getEditsManager().edit(new MoveUpInTree(selectedNode));
				reloadModel();
			}
		});
		moveDownItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SkeletonNode selectedNode = (SkeletonNode)tree.getLastSelectedPathComponent();
				getSkeleton().getEditor().getEditsManager().edit(new MoveDownInTree(selectedNode));
				reloadModel();
			}
		});
	}

	public JTree getTree()
	{
		return tree;
	}
	
	private void getExpandedNodes() {
		expandedPaths.clear();
		for (int i = 0; i < tree.getRowCount(); i++) {
			if (tree.isExpanded(i)) {
				TreePath path = tree.getPathForRow(i);
				expandedPaths.add(path);
			}
		}
	}
	private void setExpandedNodes() {
		for (int i = 0; i < expandedPaths.size(); i++) {
			tree.expandPath(expandedPaths.get(i));
		}
	}
	
	/** Reload the tree model while keeping the expanded state of the tree **/
	public void reloadModel() {
		DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
		getExpandedNodes();
		model.reload();
		setExpandedNodes();
	}
}
