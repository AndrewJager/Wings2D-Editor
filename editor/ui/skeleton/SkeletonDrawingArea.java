package editor.ui.skeleton;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;

import editor.objects.skeleton.SkeletonBone;
import editor.objects.skeleton.SkeletonFrame;
import editor.ui.DrawingArea;

public class SkeletonDrawingArea extends SkeletonUIElement{
	private DrawingArea drawArea;
	private JScrollPane pane;
	private JTree tree;
	private SkeletonFrame frame;
	private SkeletonBone rotateBone, dragBone;
	private SkeletonTreeControls controls;

	public SkeletonDrawingArea(final SkeletonEdit edit, final Rectangle bounds) {
		super(edit, bounds);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		// Replace panel with drawing area
		drawArea = new DrawingArea(edit.getEditor());
		pane = new JScrollPane(drawArea);
		panel.setLayout(new GridLayout(0,1));
		panel.add(pane);
		tree = edit.getSkeletonTree().getTree();
	}
	public void setSelectedFrame(final SkeletonFrame f)
	{
		frame = f;
	}
	public DrawingArea getDrawArea()
	{
		return drawArea;
	}
	public void setControls(final SkeletonTreeControls c)
	{
		controls = c;
	}

	@Override
	public void createEvents() {
		drawArea.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e))
				{
					if (frame != null)
					{
						rotateBone = frame.getBoneAtPosition(e.getPoint(), skeleton.getEditor().getUIScale() * drawArea.getZoomScale());
						if (rotateBone != null)
						{
							TreePath path = new TreePath(frame);
							path = path.pathByAddingChild(rotateBone);
							tree.setSelectionPath(path);
							rotateBone.setShowRotHandle(true);
							panel.repaint();
						}
					}
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e))
				{
					if (frame != null)
					{
						dragBone = frame.getBoneAtPosition(e.getPoint(), skeleton.getEditor().getUIScale() * drawArea.getZoomScale());
						SkeletonBone selectedHandle = frame.getHandleBone(e.getPoint(), skeleton.getEditor().getUIScale() * drawArea.getZoomScale());
						if (dragBone != null)
						{
							TreePath path = new TreePath(frame);
							path = path.pathByAddingChild(dragBone);
							tree.setSelectionPath(path);
							dragBone.setRotating(false);
							dragBone.setShowRotHandle(false);
						}
						if (selectedHandle != null)
						{
							dragBone = selectedHandle;
							selectedHandle.setRotating(true);
						}
					}
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e))
				{
					if (frame != null)
					{
						if (dragBone != null)
						{
							dragBone.setRotating(false);
							dragBone = null;
						}
					}
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}	
		});
		drawArea.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e))
				{
					if (dragBone != null)
					{
						if (dragBone.getRotating())
						{
							dragBone.rotateByHandle(e.getPoint(), skeleton.getEditor().getUIScale() * drawArea.getZoomScale());
						}
						else
						{
							dragBone.setLocation(e.getPoint(), skeleton.getEditor().getUIScale() * drawArea.getZoomScale());
						}
						controls.updateBoneInfo();
						drawArea.resizeToDrawItem(skeleton.getEditor().getUIScale());
						panel.repaint();
					}
				}
			}
			@Override
			public void mouseMoved(MouseEvent e) {}
		});
		drawArea.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				drawArea.zoom(e.getWheelRotation());
			}
		});
	}
}
