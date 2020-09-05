package editor.ui.skeleton;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;

import editor.objects.skeleton.SkeletonBone;
import editor.objects.skeleton.SkeletonFrame;
import editor.ui.DrawingArea;

public class SkeletonDrawingArea extends SkeletonUIElement{
	private JTree tree;
	private SkeletonFrame frame;
	private SkeletonBone rotateBone, dragBone;

	public SkeletonDrawingArea(SkeletonEdit edit, Rectangle bounds) {
		super(edit, bounds);

		// Replace panel with drawing area
		panel = new DrawingArea();
		tree = edit.getSkeletonTree().getTree();
	}
	public void setSelectedFrame(SkeletonFrame f)
	{
		frame = f;
	}
	public DrawingArea getDrawArea()
	{
		return (DrawingArea)panel;
	}

	@Override
	public void createEvents() {
		panel.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e))
				{
					if (frame != null)
					{
						rotateBone = frame.getBoneAtPosition(e.getPoint());
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
						dragBone = frame.getBoneAtPosition(e.getPoint());
						SkeletonBone selectedHandle = frame.getHandleBone(e.getPoint());
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
		panel.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e))
				{
					if (dragBone != null)
					{
						if (dragBone.getRotating())
						{
							dragBone.rotateByHandle(e.getPoint());
						}
						else
						{
							dragBone.setLocation(e.getPoint());
						}
						panel.repaint();
					}
				}
			}
			@Override
			public void mouseMoved(MouseEvent e) {}
		});
	}
}
