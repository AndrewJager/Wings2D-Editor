package com.wings2d.editor.ui.skeleton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;

import com.wings2d.editor.objects.EditorSettings;
import com.wings2d.editor.objects.skeleton.DrawMode;
import com.wings2d.editor.objects.skeleton.SkeletonBone;
import com.wings2d.editor.objects.skeleton.SkeletonFrame;
import com.wings2d.editor.objects.skeleton.SkeletonNode;
import com.wings2d.editor.objects.skeleton.path.DrawingLogic;
import com.wings2d.editor.objects.skeleton.path.Sprite;
import com.wings2d.editor.ui.DrawingArea;
import com.wings2d.editor.ui.UIElement;
import com.wings2d.editor.ui.edits.SetBoneLocation;
import com.wings2d.editor.ui.edits.SetBoneRotation;
import com.wings2d.editor.ui.skeleton.treecontrols.SkeletonTreeControls;

public class SkeletonDrawingPanel extends UIElement<SkeletonEdit>{

	
	private DrawingArea drawArea;
	private JScrollPane pane;
	private JTree tree;
	private SkeletonTreeControls treeControls;
	private double curX, curY, curRot;
	
	private DrawingLogic logic;

	public SkeletonDrawingPanel(final SkeletonEdit edit) {
		super(edit);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panel.setPreferredSize(new Dimension(400, 400));

		drawArea = new DrawingArea(edit.getEditor(), DrawingArea.DrawType.DRAW);
		pane = new JScrollPane(drawArea);
		panel.setLayout(new GridLayout(0,1));
		panel.add(pane);
		tree = edit.getSkeletonTree().getTree();
		
		logic = new DrawingLogic(getEditPanel(), drawArea);
	}
	public void setSelectedFrame(final SkeletonFrame f)
	{
		logic.setFrame(f);
	}
	public DrawingArea getDrawArea()
	{
		return drawArea;
	}
	public void setTreeControls(final SkeletonTreeControls c)
	{
		treeControls = c;
	}

	@Override
	public void createEvents() {
		drawArea.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {
				logic.processPressed(e);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				logic.processRelease(e);
			}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}	
		});
		drawArea.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				logic.processDragged(e);
				
				drawArea.resizeToDrawItem(getEditPanel().getEditor().getUIScale());
				panel.repaint();
			}

			@Override
			public void mouseMoved(MouseEvent e) {}
		});
		drawArea.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				drawArea.zoom(e.getWheelRotation());
				drawArea.resizeToDrawItem(getEditPanel().getEditor().getUIScale());
			}
		});
	}
}
