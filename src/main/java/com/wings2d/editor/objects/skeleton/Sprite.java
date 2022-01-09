package com.wings2d.editor.objects.skeleton;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.BaseMultiResolutionImage;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import com.wings2d.editor.objects.Drawable;
import com.wings2d.editor.objects.EditorSettings;
import com.wings2d.editor.objects.save.DBColor;
import com.wings2d.editor.objects.save.DBUUID;
import com.wings2d.editor.ui.edits.ActionNotDoneException;
import com.wings2d.framework.imageFilters.ImageFilter;
import com.wings2d.framework.shape.ShapeComparator;

public class Sprite extends SkeletonNode implements Drawable{
	public static final String TABLE_NAME = "SPRITE";
	
	private DBUUID boneID;
	/** ID used to sync sprites between bones **/
	private DBUUID syncSpriteID;
	private DBColor color;

	private SkeletonBone parent;
	private EditorSettings settings;

	private Path2D path;
	private int selectedVertex = -1;
	
	private double imgXOffset;
	private double imgYOffset;
	private BaseMultiResolutionImage multiImage;
	private List<ImageFilter> filters;
	private List<SpritePoint> points;
	
	public static Sprite insert(final String spriteName, final SkeletonBone parent, final Connection con) {
		return new Sprite(spriteName, parent, con);
	}
	public static Sprite read(final UUID spriteID, final SkeletonBone parent, final Connection con) {
		return new Sprite(parent, spriteID, con);
	}
	
	/** Insert constructor */
	private Sprite(final String spriteName, final SkeletonBone parent, final Connection con)
	{
		this(parent);
		name.setStoredValue(spriteName);
		boneID.setStoredValue(parent.getID());
		
		color.setStoredValue(Color.LIGHT_GRAY);
		
		this.insert(con);
		this.query(con, id.getStoredValue());
		
		// Default shape
		moveTo(-30, -30);
		lineTo(30, -30);
		lineTo(30, 30);
		lineTo(-30, 30);
		path.closePath();
	}
	
	/** Read constructor */
	public Sprite(final SkeletonBone parent, final UUID spriteID, final Connection con) {
		this(parent);
		
		this.query(con, spriteID);
		recreatePathFromPoints(points);
	}
	
	public Sprite(final SkeletonBone parent) {
		super(TABLE_NAME);
		this.parent = parent;
		this.settings = parent.getSettings();
		filters = new ArrayList<ImageFilter>();
		points = new ArrayList<SpritePoint>();
		path = new Path2D.Double();
		
		fields.add(boneID = new DBUUID("Bone"));
		fields.add(syncSpriteID = new DBUUID("SyncSprite"));
		fields.add(color = new DBColor("Color"));
	}
	
	
	@Override
	public void deleteChildren(final Connection con) {
		for(int i = 0; i < points.size(); i++) {
			points.get(i).delete(con);
		}
	}
	
	@Override
	public void queryChildren(final UUID ID, final Connection con) {
		points.clear();
		String sql = " SELECT * FROM " + SpritePoint.TABLE_NAME + " WHERE Sprite = " + quoteStr(ID.toString());
		sql = sql + " ORDER BY Position DESC";
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				points.add(SpritePoint.read(UUID.fromString(rs.getString("ID")), this, con));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void updateChildren(final Connection con) {
		for(int i = 0; i < points.size(); i++) {
			points.get(i).update(con);
		}
	}

	public Sprite copy(final SkeletonBone parent, final Connection con) throws Exception
	{
		Sprite newSprite = new Sprite(this.getName(), parent, con);
		newSprite.color.setStoredValue(new Color(this.color.getStoredValue().getRGB()));
		newSprite.path = new Path2D.Double(this.path);
		newSprite.points = new ArrayList<SpritePoint>();
		for (int i = 0; i < this.points.size(); i++) {
			newSprite.points.add(SpritePoint.insert(this.points.get(i).getX(), this.points.get(i).getY(),
					newSprite.getAmountOfPoints(), newSprite, con));
		}
		newSprite.syncSpriteID.setStoredValue(this.getID());
		return newSprite;
	}
	
	public String toString()
	{
		return name.getStoredValue();
	}
	public Path2D getPath()
	{
		return path;
	}
	public Path2D getScaledPath(final double scale)
	{
		AffineTransform transform = new AffineTransform();
		transform.scale(scale, scale);
		return (Path2D)transform.createTransformedShape(path);
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
			if (iter.currentSegment(new float[6]) != PathIterator.SEG_CLOSE)
			{
				points++;
			}
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
	public void translate(final double deltaX, final double deltaY)
	{
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(parent.getRotation()));
		transform.translate(deltaX, deltaY);
		transform.rotate(-Math.toRadians(parent.getRotation()));
		path.transform(transform);
		List<SkeletonBone> syncedBones = parent.getSyncedBones();
		for (int i = 0; i < syncedBones.size(); i++)
		{
			if (syncedBones.get(i).getSpriteBySyncID(syncSpriteID.getStoredValue()) != null)
			{
				syncedBones.get(i).getSpriteBySyncID(syncSpriteID.getStoredValue()).translate(deltaX, deltaY);
			}
		}
	}
	public void setLocation(final double x, final double y, final double scale)
	{
		double unscale = 1.0 / scale;
		double unscaledX = x * unscale;
		double unscaledY = y * unscale;
		double deltaX = unscaledX - (path.getBounds2D().getX() + (path.getBounds2D().getWidth() / 2) + parent.getX());
		double deltaY = unscaledY - (path.getBounds2D().getY() + (path.getBounds2D().getHeight() / 2)+ parent.getY());
		translate(deltaX, deltaY);
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
			if (iter.currentSegment(coords) != PathIterator.SEG_CLOSE)
			{
				vertices.add(new Point2D.Double(coords[0], coords[1]));
			}
			iter.next();
		}
		return vertices;
	}
	public void setVertexLocation(final double x, final double y, final int vertex, final double scale, final boolean relativeToParent)
	{
		double unscale = 1.0 / scale;
		double unscaledX = x * unscale;
		double unscaledY = y * unscale;
		List<Point2D> tmpPoints = getVertices();

		if (relativeToParent) {
			tmpPoints.get(vertex).setLocation(unscaledX - parent.getX(), 
					unscaledY - parent.getY());
		}
		else {
			tmpPoints.get(vertex).setLocation(unscaledX, 
					unscaledY);
		}
		points.get(vertex).setPoint(tmpPoints.get(vertex));
		recreatePathFromPoints(tmpPoints, true);
	}
	private void recreatePathFromPoints(final List<SpritePoint> points) {
		List<Point2D> otherPoints = new ArrayList<Point2D>();
		for (int i = 0; i < points.size(); i++) {
			SpritePoint point = points.get(i);
			otherPoints.add(new Point2D.Double(point.getX(), point.getY()));
		}
		recreatePathFromPoints(otherPoints, true);
	}
	private void recreatePathFromPoints(final List<Point2D> points, final boolean close)
	{
		path = new Path2D.Double();
		path.moveTo(points.get(0).getX(), points.get(0).getY());
		if (points.size() > 1)
		{
			for (int i = 1; i < points.size(); i++)
			{
				path.lineTo(points.get(i).getX(), points.get(i).getY());
			}
		}
		if (close) 
		{
			path.closePath();
		}
	}
	public void setVertexLocation(final Point2D loc, final int vertex, final double scale)
	{
		setVertexLocation(loc.getX(), loc.getY(), vertex, scale, true);
	}
	/** Calls setVertexLocation with the vertex returned by getSelectedVertex() **/
	public void setVertexLocation(final Point2D loc, final double scale)
	{
		setVertexLocation(loc, getSelectedVertex(), scale);
	}
	public void translateVertex(final Shape parentPath, final double deltaX, final double deltaY, final int vertex)
	{
		// Set children first to avoid comparing shapes after this shape is changed
		List<SkeletonBone> syncedBones = parent.getSyncedBones();
		for (int i = 0; i < syncedBones.size(); i++)
		{
			Sprite sprite = syncedBones.get(i).getSpriteBySyncID(syncSpriteID.getStoredValue());
			if (ShapeComparator.similarShapes(this.path, sprite.path))
			{
				sprite.translateVertex(this.path, deltaX, deltaY, vertex);
			}
		}

		double angle = ShapeComparator.getRotationFrom(this.path, parentPath, false);
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(angle));
		path = (Path2D)transform.createTransformedShape(path);
		
		List<Point2D> points = getVertices();
		Point2D movePoint = points.get(vertex);
		points.get(vertex).setLocation(movePoint.getX() + deltaX, movePoint.getY() + deltaY);
		recreatePathFromPoints(points, true);
		transform = new AffineTransform();
		transform.rotate(Math.toRadians(-angle));
		path = (Path2D)transform.createTransformedShape(path);
	}
	public void setRelativeVertexLocation(final double x, final double y, final double scale) {
		setRelativeVertexLocation(x, y , getSelectedVertex(), scale);
	}
	public void setRelativeVertexLocation(final double x, final double y, final int vertex, final double scale) {
		setVertexLocation(x, y, vertex, scale, false);
	}
	public void rotateAroundBone(final double delta)
	{
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(delta), 0, 0);
		path = (Path2D)transform.createTransformedShape(path);
	}
	public Color getColor()
	{
		return color.getStoredValue();
	}
	public void setColor(final Color color)
	{
		this.color.setStoredValue(color);
		List<SkeletonBone> syncedBones = parent.getSyncedBones();
		for (int i = 0; i < syncedBones.size(); i++)
		{
			Sprite sprite = syncedBones.get(i).getSpriteBySyncID(syncSpriteID.getStoredValue());
			sprite.setColor(color);
		}
	}
	public UUID getSyncID()
	{
		if (syncSpriteID.getStoredValue() != null) {
			return syncSpriteID.getStoredValue();
		}
		else {
			return null;
		}
	}
	public SkeletonBone getBone()
	{
		return parent;
	}
	public void lineTo(final double x, final double y) {
		points.add(SpritePoint.insert(x, y, this.getAmountOfPoints(), this, parent.getStoredConnection()));
		path.lineTo(x, y);
	}
	public void moveTo(final double x, final double y) {
		points.add(SpritePoint.insert(x, y, this.getAmountOfPoints(), this, parent.getStoredConnection()));
		path.moveTo(x, y);
	}
	public void addVertex(final Point2D point)
	{
		List<SkeletonBone> syncedBones = parent.getSyncedBones();
		for (int i = 0; i < syncedBones.size(); i++)
		{
			Sprite sprite = syncedBones.get(i).getSpriteBySyncID(getSyncID());
			double xOffset = path.getBounds2D().getCenterX() + parent.getX() - point.getX();
			double yOffset = path.getBounds2D().getCenterY() + parent.getY() - point.getY();
			sprite.addVertex(this.getPath(), xOffset, yOffset);
		}
		recreatePathFromPoints(getVertices(), false);
		lineTo((point.getX() - parent.getX()), point.getY() - parent.getY());
		path.closePath();
	}
	public void addVertex(final Shape baseShape, final double xOffset, final double yOffset)
	{
		List<SkeletonBone> syncedBones = parent.getSyncedBones();
		for (int i = 0; i < syncedBones.size(); i++)
		{
			Sprite sprite = syncedBones.get(i).getSpriteBySyncID(getSyncID());
			sprite.addVertex(baseShape, xOffset, yOffset);
		}
		double angle = ShapeComparator.getRotationFrom(this.path, baseShape, false);
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(angle));
		path = (Path2D)transform.createTransformedShape(path);
		
		recreatePathFromPoints(getVertices(), false);
		lineTo(path.getBounds2D().getCenterX() - xOffset, path.getBounds2D().getCenterY() - yOffset);
		path.closePath();
		
		transform = new AffineTransform();
		transform.rotate(Math.toRadians(-angle));
		path = (Path2D)transform.createTransformedShape(path);
	}
	
	public List<ImageFilter> getFilters()
	{
		return filters;
	}
	public void addFilter(final ImageFilter filter) {
		filters.add(filter);
		for (int i = 0; i < this.parent.getSyncedBones().size(); i++)
		{
			SkeletonBone bone = this.parent.getSyncedBones().get(i);
			bone.getSpriteBySyncID(this.getSyncID()).addFilter(filter);
		}
	}
	public void removeFilter(final ImageFilter filter) {
		filters.remove(filter);
		for (int i = 0; i < this.parent.getSyncedBones().size(); i++)
		{
			SkeletonBone bone = this.parent.getSyncedBones().get(i);
			bone.getSpriteBySyncID(this.getSyncID()).removeFilter(filter);
		}
	}
	public List<SpritePoint> getPoints() {
		return points;
	}
	public EditorSettings getSettings() {
		return settings;
	}
	
	private BufferedImage createImage(final double scale)
	{
		Shape drawShape = getScaledPath(scale);
		BufferedImage newImage = new BufferedImage((int)drawShape.getBounds2D().getWidth(), (int)drawShape.getBounds2D().getHeight(), BufferedImage.TYPE_INT_ARGB);
		imgXOffset = drawShape.getBounds2D().getX();
		imgYOffset = drawShape.getBounds2D().getY();
		AffineTransform transform = new AffineTransform();
		transform.translate(-imgXOffset, -imgYOffset);
		drawShape = transform.createTransformedShape(drawShape);
		Graphics2D g2d = (Graphics2D)newImage.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(color.getStoredValue());
		g2d.fill(drawShape);
		
		for (int i = 0; i < filters.size(); i++)
		{
			filters.get(i).filter(newImage);
		}
		
		return newImage;
	}
	private BaseMultiResolutionImage createMultiImage(final double displayScale)
	{
		double[] imgSizes = new double[] {1.0, 1.25, 1.5};
		BufferedImage[] imgs = new BufferedImage[imgSizes.length];
		for (int i = 0; i < imgSizes.length; i++)
		{
			imgs[i] = createImage(displayScale * imgSizes[i]);
		}
		
		return new BaseMultiResolutionImage(imgs);
	}

	// MutableTreeNode methods
	@Override
	public void insert(final MutableTreeNode child, final int index) {
		SpritePoint point = (SpritePoint)child;
		points.add(point);
		for (int i = 0; i < this.parent.getSyncedBones().size(); i++)
		{
			SkeletonBone bone = this.parent.getSyncedBones().get(i);
			bone.getSpriteBySyncID(this.getSyncID()).getPoints().add(SpritePoint.insert(point.getX(), point.getY(), 
					bone.getSpriteBySyncID(getSyncID()).getAmountOfPoints(),
					bone.getSpriteBySyncID(this.getSyncID()), parent.getStoredConnection()));
		}
	}
	@Override
	public void remove(final int index) {
		points.remove(index);
		recreatePathFromPoints(points);
	}
	@Override
	public void remove(final MutableTreeNode node) {
		points.remove(node);
		recreatePathFromPoints(points);
	}
	@Override
	public void setUserObject(final Object object) {}
	@Override
	public void removeFromParent() {
		parent.remove(this);
	}
	@Override
	public void setParent(final MutableTreeNode newParent) {
		parent = (SkeletonBone)newParent;
	}
	@Override
	public TreeNode getChildAt(final int childIndex) {
		return points.get(childIndex);
	}
	@Override
	public int getChildCount() {
		return points.size();
	}
	@Override
	public TreeNode getParent() {
		return parent;
	}
	@Override
	public int getIndex(final TreeNode node) {
		return points.indexOf(node);
	}
	@Override
	public boolean getAllowsChildren() {
		return true;
	}
	@Override
	public boolean isLeaf() {
		return (points.size() == 0);
	}
	@Override
	public Enumeration<? extends TreeNode> children() {
		return Collections.enumeration(points);
	}


	// SkeletonNode methods
	@Override
	public void setName(final String newName) {
		super.setName(newName);
		List<SkeletonBone> syncedBones = parent.getSyncedBones();
		for (int i = 0; i < syncedBones.size(); i++)
		{
			if (syncedBones.get(i).getSpriteBySyncID(getSyncID()) != null)
			{
				syncedBones.get(i).getSpriteBySyncID(getSyncID()).setName(newName);
			}
		}
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
		g2d.setColor(color.getStoredValue());
		g2d.fill(draw);
		
		if (mode == DrawMode.SPRITE_MOVE)
		{
			g2d.setStroke(new BasicStroke(5));
			g2d.setColor(Color.YELLOW);
			g2d.draw(draw);
		}
		if (mode == DrawMode.SPRITE_EDIT)
		{
			PathIterator iter = path.getPathIterator(transform);
			g2d.setColor(Color.YELLOW);
			while(!iter.isDone())
			{
				int handleSize = settings.getHandleSize();
				double[] coords = new double[6];
				if (iter.currentSegment(coords) != PathIterator.SEG_CLOSE);
				{
					g2d.drawArc((int)(coords[0] - (handleSize / 2)), (int)(coords[1] - (handleSize / 2)), handleSize, handleSize, 0, 360);
				}
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
	@Override
	public void generateRender(final double scale)
	{
		this.multiImage = this.createMultiImage(scale);
	}
	@Override
	public void drawRender(final Graphics2D g2d, final double scale)
	{
		g2d.drawImage(multiImage, (int)((parent.getX() + path.getBounds2D().getX()) * scale),
				(int)((parent.getY() + path.getBounds2D().getY()) * scale), null);
	}
	@Override
	public void moveUp() throws ActionNotDoneException
	{
		List<Sprite> sprites = parent.getSprites();
		int index = sprites.indexOf(this);
		if (index > 0) 
		{
			Collections.swap(sprites, index, index - 1);
		}
		else {
			throw new ActionNotDoneException(MOVE_UP_ERROR, false);
		}
	}
	@Override
	public void moveDown() throws ActionNotDoneException
	{
		List<Sprite> sprites = parent.getSprites();
		int index = sprites.indexOf(this);
		if (index < sprites.size() - 1) 
		{
			Collections.swap(sprites, index, index + 1);
		}
		else {
			throw new ActionNotDoneException(MOVE_DOWN_ERROR, false);
		}
	}
	@Override
	public List<SkeletonNode> getNodes()
	{
		return null;
	}
	@Override
	public boolean isMaster() {
		return this.getBone().isMaster();
	}
}
