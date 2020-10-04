package com.wings2d.editor.objects.skeleton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import com.wings2d.editor.objects.Drawable;
import com.wings2d.framework.ShapeComparator;

public class Sprite implements SkeletonNode, Drawable{
	public static final String SPRITE_TOKEN = "SPRITE";
	public static final String VERTEX_TOKEN = "VERTEX";
	public static final String SYNC_ID_TOKEN = "SYNCID";
	
	private String name;
	private SkeletonBone parent;
	/** ID used to sync sprites between bones **/
	private UUID syncID;
	private Path2D path;
	private Color color;
	private int selectedVertex = -1;
	
	public Sprite(final String spriteName, final SkeletonBone parent)
	{
		this.name = spriteName;
		this.parent = parent;
		syncID = UUID.randomUUID();
		color = Color.LIGHT_GRAY;
		path = new Path2D.Double();
		path.moveTo(-30, -30);
		path.lineTo(30, -30);
		path.lineTo(30, 30);
		path.lineTo(-30, 30);
	}
	
	public Sprite(final Scanner in, final SkeletonBone parent)
	{
		this.parent = parent;
		path = new Path2D.Double();
		boolean keepReading = true;
		boolean firstPoint = true;
		while(in.hasNext() && keepReading)
		{
			String[] tokens = in.next().split(":");
			switch(tokens[0])
			{
			case NAME_TOKEN:
				name = tokens[1];
				break;
			case COLOR_TOKEN:
				color = new Color(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));
				break;
			case VERTEX_TOKEN:
				if (firstPoint)
				{
					path.moveTo(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]));
					firstPoint = false;
				}
				else
				{
					path.lineTo(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]));
				}
				break;
			case SYNC_ID_TOKEN:
				syncID = UUID.fromString(tokens[1]);
				break;
			case END_TOKEN:
				keepReading = false;
				break;
			}
		}
	}
	public Sprite copy(final SkeletonBone parent)
	{
		Sprite newSprite = new Sprite(new String(this.name), parent);
		newSprite.color = new Color(this.color.getRGB());
		newSprite.path = new Path2D.Double(this.path);
		newSprite.syncID = this.syncID;
		return newSprite;
	}
	
	public String toString()
	{
		return name;
	}
	public Path2D getPath()
	{
		return path;
	}
	public Path2D getScaledAndTranslatedPath(final double scale)
	{
		AffineTransform transform = new AffineTransform();
		transform.scale(scale, scale);
		transform.translate(parent.getX(), parent.getY());
		return (Path2D)transform.createTransformedShape(path);
	}
	public int getAmountOfPoints()
	{
		int points = 0;
		PathIterator iter = path.getPathIterator(null);
		while(!iter.isDone())
		{
			points++;
			iter.next();
		}
		return points;
	}
	public void setSelectedVertex(final int vertex)
	{
		if (vertex > getAmountOfPoints() || vertex < -1)
		{
			throw new IllegalArgumentException("Point " + vertex + " is outside the amount of points in the Sprite's path!");
		}
		selectedVertex = vertex;
	}
	public int getSelectedVertex()
	{
		return selectedVertex;
	}
	public Point2D getCoordsOfSelectedVertex(final double scale)
	{
		AffineTransform transform = new AffineTransform();
		transform.scale(scale, scale);
		transform.translate(parent.getX() - (path.getBounds2D().getWidth() / 2), 
				parent.getY() - (path.getBounds2D().getHeight() / 2));
		PathIterator iter = path.getPathIterator(transform);
		int i = 0;
		Point2D coordsPoint = null;
		while(!iter.isDone())
		{
			if (i == selectedVertex)
			{
				double[] coords = new double[6];
				iter.currentSegment(coords);
				coordsPoint = new Point2D.Double(coords[0], coords[1]);
				break;
			}
			i++;
		}
		return coordsPoint;
	}
	public void setLocation(final double x, final double y, final double scale)
	{
		double unscale = 1.0 / scale;
		double unscaledX = x * unscale;
		double unscaledY = y * unscale;
		double deltaX = unscaledX - (path.getBounds2D().getX() + (path.getBounds2D().getWidth() / 2) + parent.getX());
		double deltaY = unscaledY - (path.getBounds2D().getY() + (path.getBounds2D().getHeight() / 2)+ parent.getY());
		AffineTransform transform = new AffineTransform();
		transform.translate(deltaX, deltaY);
		path.transform(transform);
	}
	public void setLocation(final Point loc, final double scale)
	{
		setLocation(loc.getX(), loc.getY(), scale);
	}
	public List<Point2D> getVertices()
	{
		List<Point2D> vertices = new ArrayList<Point2D>();
		PathIterator iter = path.getPathIterator(null);
		double[] coords = new double[6];
		while(!iter.isDone())
		{
			iter.currentSegment(coords);
			vertices.add(new Point2D.Double(coords[0], coords[1]));
			iter.next();
		}
		return vertices;
	}
	public void setVertexLocation(final double x, final double y, final int vertex, final double scale)
	{
		double unscale = 1.0 / scale;
		double unscaledX = x * unscale;
		double unscaledY = y * unscale;
		List<Point2D> points = getVertices();
		// Set children first to avoid comparing shapes after this shape is changed
		List<SkeletonBone> syncedBones = parent.getSyncedBones();
		for (int i = 0; i < syncedBones.size(); i++)
		{
			Sprite sprite = syncedBones.get(i).getSpriteBySyncID(syncID);
			if (ShapeComparator.similarShapes(this.path, sprite.path))
			{
				sprite.translateVertex(unscaledX - points.get(getSelectedVertex()).getX() - parent.getX(), 
						unscaledY - points.get(getSelectedVertex()).getY() - parent.getY(), getSelectedVertex(), scale);
			}
		}
		points.get(vertex).setLocation(unscaledX - parent.getX(), 
				unscaledY - parent.getY());
		path = new Path2D.Double();
		path.moveTo(points.get(0).getX(), points.get(0).getY());
		if (points.size() > 1)
		{
			for (int i = 1; i < points.size(); i++)
			{
				path.lineTo(points.get(i).getX(), points.get(i).getY());
			}
		}
	}
	public void setVertexLocation(final Point loc, final int vertex, final double scale)
	{
		setVertexLocation(loc.getX(), loc.getY(), vertex, scale);
	}
	/** Calls setVertexLocation with the vertex returned by getSelectedVertex() **/
	public void setVertexLocation(final Point loc, final double scale)
	{
		setVertexLocation(loc, getSelectedVertex(), scale);
	}
	public void translateVertex(final double deltaX, final double deltaY, final int vertex, final double scale)
	{
		// Set children first to avoid comparing shapes after this shape is changed
		List<SkeletonBone> syncedBones = parent.getSyncedBones();
		for (int i = 0; i < syncedBones.size(); i++)
		{
			Sprite sprite = syncedBones.get(i).getSpriteBySyncID(syncID);
			if (ShapeComparator.similarShapes(this.path, sprite.path))
			{
				sprite.translateVertex(deltaX, deltaY, vertex, scale);
			}
		}
		List<Point2D> points = getVertices();
		Point2D point = points.get(vertex);
		points.get(vertex).setLocation(point.getX() + deltaX, point.getY() + deltaY);
		path = new Path2D.Double();
		path.moveTo(points.get(0).getX(), points.get(0).getY());
		if (points.size() > 1)
		{
			for (int i = 1; i < points.size(); i++)
			{
				path.lineTo(points.get(i).getX(), points.get(i).getY());
			}
		}
	}
	public void rotateAroundBone(final double delta)
	{
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(delta), 0, 0);
		path = (Path2D)transform.createTransformedShape(path);
	}
	public Color getColor()
	{
		return color;
	}
	public void setColor(final Color color)
	{
		this.color = color;
	}
	public UUID getSyncID()
	{
		return syncID;
	}
	public SkeletonBone getBone()
	{
		return parent;
	}

	// MutableTreeNode methods
	@Override
	public void insert(final MutableTreeNode child, final int index) {}
	@Override
	public void remove(final int index) {}
	@Override
	public void remove(final MutableTreeNode node) {}
	@Override
	public void setUserObject(final Object object) {}
	@Override
	public void removeFromParent() {}
	@Override
	public void setParent(final MutableTreeNode newParent) {}
	@Override
	public TreeNode getChildAt(final int childIndex) {return null;}
	@Override
	public int getChildCount() {return 0;}
	@Override
	public TreeNode getParent() {
		return parent;
	}
	@Override
	public int getIndex(final TreeNode node) {return 0;}
	@Override
	public boolean getAllowsChildren() {return false;}
	@Override
	public boolean isLeaf() {return true;}
	@Override
	public Enumeration<? extends TreeNode> children() {return null;}


	// SkeletonNode methods
	@Override
	public void setName(final String newName) {
		this.name = newName;
	}
	@Override
	public void saveToFile(final PrintWriter out) {
		out.write(SPRITE_TOKEN + "\n");
		out.print(NAME_TOKEN + ":" + name + "\n");
		out.write(SYNC_ID_TOKEN + ":" + syncID + "\n");
		out.write(COLOR_TOKEN + ":" + color.getRed() + ":" + color.getGreen() + ":" + color.getBlue() + "\n");
		List<Point2D> vertices = getVertices();
		for (int i = 0; i < vertices.size(); i++)
		{
			Point2D vertex = vertices.get(i);
			out.write(VERTEX_TOKEN + ":" + vertex.getX() + ":" + vertex.getY()+ "\n"); 
		}
		
		out.write(END_TOKEN + ":" + SPRITE_TOKEN + "\n");
	}
	@Override
	public void resyncAll() {
		
	}

	// Drawable methods
	@Override
	public void draw(final Graphics2D g2d, final double scale, final DrawMode mode) {
		AffineTransform transform = new AffineTransform();
		transform.scale(scale, scale);
		transform.translate(parent.getX(), 
				parent.getY());
		Shape draw = transform.createTransformedShape(path);
		g2d.setColor(color);
		g2d.fill(draw);
		
		if (mode == DrawMode.SPRITE)
		{
			PathIterator iter = path.getPathIterator(transform);
			g2d.setColor(Color.YELLOW);
			while(!iter.isDone())
			{
				int handleSize = 8;
				double[] coords = new double[6];
				iter.currentSegment(coords);
				g2d.drawArc((int)(coords[0] - (handleSize / 2)), (int)(coords[1] - (handleSize / 2)), handleSize, handleSize, 0, 360);
				iter.next();
			}
		}
	}
	@Override
	public Dimension getDrawSize(final double scale) {
		Shape bounds = path.getBounds2D();
		AffineTransform transform = new AffineTransform();
		transform.scale(scale, scale);
		bounds = transform.createTransformedShape(bounds);
		return new Dimension((int)bounds.getBounds().getWidth(), (int)bounds.getBounds().getHeight());
	}
}
