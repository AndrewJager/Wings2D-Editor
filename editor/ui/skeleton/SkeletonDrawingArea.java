package editor.ui.skeleton;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import editor.objects.skeleton.SkeletonBone;
import editor.objects.skeleton.SkeletonFrame;
import editor.ui.DrawingArea;

public class SkeletonDrawingArea extends SkeletonUIElement{
	private JTree tree;
	private SkeletonFrame frame;
	private SkeletonBone selectedBone, dragBone;

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
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {
				if (frame != null)
				{
					dragBone = frame.getBoneAtPosition(e.getPoint());
					if (dragBone != null)
					{
						TreePath path = new TreePath(frame);
						path = path.pathByAddingChild(dragBone);
						tree.setSelectionPath(path);
					}
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				dragBone = null;
			}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}	
		});
		panel.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				if (dragBone != null)
				{
					dragBone.setLocation(e.getPoint());
					panel.repaint();
				}
			}
			@Override
			public void mouseMoved(MouseEvent e) {}
		});
	}
}
