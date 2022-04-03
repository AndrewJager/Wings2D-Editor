package com.wings2d.editor.objects.skeleton.path;

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
import com.wings2d.editor.objects.skeleton.DrawMode;
import com.wings2d.editor.objects.skeleton.SkeletonBone;
import com.wings2d.editor.objects.skeleton.SkeletonFilter;
import com.wings2d.editor.objects.skeleton.SkeletonNode;
import com.wings2d.editor.ui.edits.ActionNotDoneException;
import com.wings2d.framework.imageFilters.ImageFilter;

public class Sprite extends SkeletonNode implements Drawable{
	public static final String TABLE_NAME = "SPRITE";
	
	private int selectedPoint = -1;
	private Part selectedPart = null;
	private List<Part> parts;
	private boolean snap = false;
	private boolean snapping = false;
	private boolean snapX = false;
	private boolean snapY = false;
	private boolean multi = false;
	private Point2D snapPointX = null;
	private Point2D snapPointY = null;
	private List<OrderedHandle> distances;
	private List<Handle> selectedHandles;
	
	private int HANDLE_SIZE = 20;
	
	private DBUUID boneID;
	/** ID used to sync sprites between bones **/
	private DBUUID syncSpriteID;
	private DBColor color;
	private SkeletonBone parent;
	private EditorSettings settings;
	private Path2D path;
	private List<SkeletonFilter> filters;
	private BaseMultiResolutionImage multiImage;
	
	public static Sprite insert(final String spriteName, final SkeletonBone parent, final Connection con) {
		return new Sprite(spriteName, parent, con);
	}
	public static Sprite read(final UUID spriteID, final SkeletonBone parent, final Connection con) {
		return new Sprite(parent, spriteID, con);
	}

	/** Insert constructor */
	private Sprite(final String spriteName, final SkeletonBone parent, final Connection con) {
		this(parent);
		name.setStoredValue(spriteName);
		boneID.setStoredValue(parent.getID());
		
		color.setStoredValue(Color.LIGHT_GRAY);
		
		this.insert(con);
		this.query(con, id.getStoredValue());
		
		// Default shape
		double x = parent.getX();
		double y = parent.getY();
		parts.add(Part.insert(this, PathIterator.SEG_MOVETO, 0, x - 50, y - 50, con));
		parts.add(Part.insert(this, PathIterator.SEG_LINETO, 1, x - 50, y + 50, con));
		parts.add(Part.insert(this, PathIterator.SEG_LINETO, 2, x + 50, y + 50, con));
		parts.add(Part.insert(this, PathIterator.SEG_LINETO, 3, x + 50, y - 50, con));
		recalcPath();
	}

	/** Read constructor */
	public Sprite(final SkeletonBone parent, final UUID spriteID, final Connection con) {
		this(parent);
		
		this.query(con, spriteID);
		recalcPath();
	}
	
	public Sprite(final SkeletonBone parent) {
		super(TABLE_NAME);
		this.parent = parent;
		this.settings = parent.getSettings();
		filters = new ArrayList<SkeletonFilter>();
		
		parts = new ArrayList<Part>();
		distances = new ArrayList<OrderedHandle>();
		selectedHandles = new ArrayList<Handle>();
		
		fields.add(boneID = new DBUUID("Bone"));
		fields.add(syncSpriteID = new DBUUID("SyncSprite"));
		fields.add(color = new DBColor("Color"));
	}
	
	public Sprite copy(final SkeletonBone parent, final Connection con)
	{
		Sprite newSprite = new Sprite(this.getName(), parent, con);
		newSprite.color.setStoredValue(new Color(this.color.getStoredValue().getRGB()));
		for (int i = 0; i < parts.size(); i++) {
			newSprite.parts.add(this.parts.get(i).copy(newSprite, con));
		}
		newSprite.syncSpriteID.setStoredValue(this.getID());
		for (int i = 0; i < this.filters.size(); i++) {
			newSprite.filters.add(SkeletonFilter.insert(this.filters.get(i).getFilter().copy(), newSprite, parent.getStoredConnection()));
		}
		return newSprite;
	}

	@Override
	public void deleteChildren(final Connection con) {
		for(int i = 0; i < parts.size(); i++) {
			parts.get(i).delete(con);
		}
		for (int i = 0; i < filters.size(); i++) {
			filters.get(i).delete(con);
		}
	}
	
	@Override
	public void queryChildren(final UUID ID, final Connection con) {
		parts.clear();
		String sql = getBasicQuery(Part.TABLE_NAME, "Sprite", ID);
		sql = sql + " ORDER BY PathOrder ASC";
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				parts.add(Part.read(UUID.fromString(rs.getString("ID")), this, con));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		filters.clear();
		sql = " SELECT * FROM " + SkeletonFilter.TABLE_NAME + " WHERE Sprite = " + quoteStr(ID.toString());
		sql = sql + " ORDER BY Position DESC";
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				filters.add(SkeletonFilter.read(UUID.fromString(rs.getString("ID")), this, con));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void updateChildren(final Connection con) {
		for(int i = 0; i < parts.size(); i++) {
			parts.get(i).update(con);
		}
		for (int i = 0; i < filters.size(); i++) {
			filters.get(i).update(con);
		}
	}

	public void recalcPath() {
		path = new Path2D.Double();
		for (int i = 0; i < parts.size(); i++) {
			Part part = parts.get(i);
			List<Handle> points = part.getHandles();
			switch (part.getType())
			{
				case PathIterator.SEG_MOVETO -> {
					path.moveTo(points.get(0).getX(), points.get(0).getY());
				}
				case PathIterator.SEG_LINETO -> {
					path.lineTo(points.get(0).getX(), points.get(0).getY());
				}
				case PathIterator.SEG_QUADTO -> {
					path.quadTo(points.get(0).getX(), points.get(0).getY(), points.get(1).getX(), points.get(1).getY());
				}
				case PathIterator.SEG_CUBICTO -> {
					path.curveTo(points.get(0).getX(), points.get(0).getY(), points.get(1).getX(), points.get(1).getY(),
							points.get(2).getX(), points.get(2).getY());
				}
			};
		}
	}
	
	public Path2D getPath() {
		return path;
	}

	public void processPressed(final Point p, final double scale) {
		int DIST = 20;
		selectedPoint = -1;
		selectedPart = null;
		double x = p.getX();
		double y = p.getY();
		boolean found = false;
		for (int i = 0; (i < parts.size() && !found); i++) {
			List<Handle> handles = parts.get(i).getHandles();
			for (int j = 0; (j < handles.size() && !found); j++) {
				Handle h = handles.get(j);	
				if ((!multi) || h.getIsEnd()) {
					if ((x > (h.getX() * scale - DIST)) && (x < (h.getX() * scale + DIST))) {
						if ((y > (h.getY() * scale - DIST)) && (y < (h.getY() * scale + DIST))) {
							selectedPart = parts.get(i);
							selectedPoint = j;
							buildDistances();
							if (!multi) {
								selectedHandles.clear();
							}
							selectedHandles.add(selectedPart.getHandles().get(selectedPoint));
							found = true;
						}
					}
				}
			}
		}
	}
	
	private void buildDistances() {
		distances.clear();
		Point2D p = selectedPart.getHandles().get(selectedPoint);
		for (int i = 0; i < parts.size(); i++) {
			Part part = parts.get(i);
			if (part != selectedPart) {
				for (int j = 0; j < part.getHandles().size(); j++) {
					if (j == part.getHandles().size() - 1) { // Only use the point, not any curve handles
						Handle h = part.getHandles().get(j);
						double dist = Math.sqrt(Math.pow((p.getX() - h.getX()), 2) + Math.pow((p.getY() - h.getY()), 2));
						distances.add(new OrderedHandle(dist, h));
					}
				}	
			}
		}
		
		Collections.sort(distances);
	}

	public void processDragged(final Point p, final double scale) {
		int SNAP_DIST = 10;
		snapX = false;
		snapY = false;
		double x, y;
		snapping = false;
		snapPointX = null;
		snapPointY = null;
		
		if (selectedPart != null) {
			if (snap && selectedPart.getHandles().get(selectedPoint).getIsEnd()) {
				for (int i = 0; (i < distances.size()) && !(snapX && snapY); i++) {
					Point2D h = distances.get(i).getHandle();
					if (!snapX && (Sprite.inRange(p.getX(), h.getX() - SNAP_DIST, h.getX() + SNAP_DIST))) {
						snapX = true;
						snapping = true;
						snapPointX = h;
					}
					if (!snapY && (Sprite.inRange(p.getY(), h.getY() - SNAP_DIST, h.getY() + SNAP_DIST))) {
						snapY = true;
						snapping = true;
						snapPointY = h;
					}
				}
			}

			if (snapX) {
				x = snapPointX.getX(); 
			}
			else {
				x = p.getX();
			}
			if (snapY) {
				y = snapPointY.getY(); 
			}
			else {
				y = p.getY();
			}

			double reverseScale = 1 / scale;
			x = x * reverseScale;
			y = y * reverseScale;

			Handle h = selectedPart.getHandles().get(selectedPoint);
			double xTrans = x - h.getX();
			double yTrans = y - h.getY();
			selectedPart.setHandle(selectedPoint, x, y);
			
			if (multi) {
				for (int i = 0; i < selectedHandles.size(); i++) {
					if (selectedHandles.get(i) != h) {
						Handle sh = selectedHandles.get(i);
						sh.setLocation(sh.getX() + xTrans, sh.getY() + yTrans);
					}
				}
			}
			
			this.recalcPath();
		}
	}

	public void processRelease() {
		snapping = false;
		snapX = false;
		snapY = false;
	}
	public void addPath(final int type, final Point2D p) {
		switch(type) {
			case PathIterator.SEG_LINETO -> {
				this.addLine(p);
			}
			case PathIterator.SEG_QUADTO -> {
				this.addQuad(p);
			}
			case PathIterator.SEG_CUBICTO -> {
				this.addCubic(p);
			}
		}
	}
	public void addLine(final Point2D p) {
		parts.add(Part.insert(this, PathIterator.SEG_LINETO, this.getParts().size(), p.getX(), p.getY(), this.getBone().getStoredConnection()));
	}
	public void addQuad(final Point2D p) {
		Point2D end = parts.get(parts.size() - 1).getEndPoint();
		
		parts.add(Part.insert(this, PathIterator.SEG_QUADTO, this.getParts().size(), avg(end.getX(), p.getX()), 
				avg(end.getY(), p.getY()), p.getX(), p.getY(), this.getBone().getStoredConnection()));
	}
	public void addCubic(final Point2D p) {
		Point2D end = parts.get(parts.size() - 1).getEndPoint();
		Point2D center = new Point2D.Double(avg(end.getX(), p.getX()), avg(end.getY(), p.getY()));
		parts.add(Part.insert(this, PathIterator.SEG_CUBICTO, this.getParts().size(), 
				avg(end.getX(), center.getX()), avg(end.getY(), center.getY()),
				avg(center.getX(), p.getX()), avg(center.getY(), p.getY()), p.getX(), p.getY(), this.getBone().getStoredConnection()));
	}
	public void setSnap(final boolean snap) {
		this.snap = snap;
	}
	public void setMulti(final boolean multi) {
		this.multi = multi;
	}
	public void delete() {
		if (selectedPart != null && (selectedPart.getType() != PathIterator.SEG_MOVETO)) {
			parts.remove(selectedPart);
			selectedPart = null;
			selectedPoint = -1;
		}
	}
	
	public List<Part> getParts() {
		return parts;
	}
	
	private double avg(final double x, final double y) {
		return (x + y) / 2;
	}
	
	private void drawHandle(final double x, final double y, final Graphics2D g2d, final double scale) {
		g2d.drawRect((int)(x * scale) - (HANDLE_SIZE / 2), (int)(y * scale) - (HANDLE_SIZE / 2),
				HANDLE_SIZE, HANDLE_SIZE);
	}
	private void drawPoint(final double x, final double y, final Graphics2D g2d, final double scale) {
		g2d.drawArc((int)(x * scale) - (HANDLE_SIZE / 2), (int)(y * scale) - (HANDLE_SIZE / 2), 
				HANDLE_SIZE, HANDLE_SIZE, 0, 360);
	}
	
	public static boolean inRange(final double x, final double min, final double max) {
		return (x > min) && (x < max);
	}
	
	@Override
	public String toString()
	{
		return name.getStoredValue();
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
	
	public void rotate(final double delta, final boolean recalcParts) {
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(delta), path.getBounds2D().getCenterX(), path.getBounds2D().getCenterY());
		path = (Path2D)transform.createTransformedShape(path);
		if (recalcParts) {
			recreatePartsFromPath(path);
		}
	}
	
	public void rotateAround(final Point2D p, final double delta, final boolean recalcParts) {
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(delta), p.getX(), p.getY());
		path = (Path2D)transform.createTransformedShape(path);
		if (recalcParts) {
			recreatePartsFromPath(path);
		}
	}
	public void rotateAround(final Point2D point, final double delta) {
		rotateAround(point, delta, true);
	}
	
	/** Assumes that path will have the same points, except for position */
	public void recreatePartsFromPath(final Path2D path) {
		PathIterator iter = path.getPathIterator(null);
		int part = 0;
		while (!iter.isDone()) {
			double[] coords = new double[6];
			int segType = iter.currentSegment(coords);
			switch (segType)
			{
				case PathIterator.SEG_MOVETO, PathIterator.SEG_LINETO -> {
					parts.get(part).setHandle(0, coords[0], coords[1]);
				}
				case PathIterator.SEG_QUADTO -> {
					parts.get(part).setHandle(0, coords[0], coords[1]);
					parts.get(part).setHandle(1, coords[2], coords[3]);
				}
				case PathIterator.SEG_CUBICTO -> {
					parts.get(part).setHandle(0, coords[0], coords[1]);
					parts.get(part).setHandle(1, coords[2], coords[3]);
					parts.get(part).setHandle(2, coords[4], coords[5]);
				}
			};
			
			iter.next();
			part++;
		}
		
		this.updateChildren(this.getBone().getStoredConnection());
	}
	public void recreatePartsFromPath() {
		recreatePartsFromPath(this.path);
	}
	
	public void deselect() {
		selectedPart = null;
		selectedPoint = -1;
	}
	
	public Path2D getScaledPath(final double scale)
	{
		AffineTransform transform = new AffineTransform();
		transform.scale(scale, scale);
		return (Path2D)transform.createTransformedShape(getPath());
	}
	public Path2D getScaledAndTranslatedPath(final double scale)
	{
		AffineTransform transform = new AffineTransform();
		transform.scale(scale, scale);
		transform.translate(parent.getX(), parent.getY());
		return (Path2D)transform.createTransformedShape(getPath());
	}
	
	public void addFilter(final ImageFilter filter) {
		filters.add(SkeletonFilter.insert(filter, this, parent.getStoredConnection()));
	}
	public void removeFilter(final ImageFilter filter) {
		for (int i = 0; i < filters.size(); i++) {
			if (filters.get(i).getFilter() == filter) {
				filters.remove(i);
				break;
			}
		}
	}
	
	public SkeletonBone getBone()
	{
		return parent;
	}
	
	private void translate(final Path2D path, final double deltaX, final double deltaY, final boolean recalcParts)
	{
		AffineTransform transform = new AffineTransform();
		transform.translate(deltaX, deltaY);
		path.transform(transform);
		if (recalcParts) {
			recreatePartsFromPath(path);
		}
	}
	public void translate(final double deltaX, final double deltaY, final boolean recalcParts) {
		Path2D path = getPath();
		translate(path, deltaX, deltaY, recalcParts);
	}
	public void translate(final double deltaX, final double deltaY) {
		translate(deltaX, deltaY, true);
	}
	public void setLocation(final double x, final double y, final double scale, final boolean recalcParts)
	{
		double unscale = 1.0 / scale;
		double unscaledX = x * unscale;
		double unscaledY = y * unscale;
		double deltaX = unscaledX - (path.getBounds2D().getCenterX());
		double deltaY = unscaledY - (path.getBounds2D().getCenterY());
		translate(path, deltaX, deltaY, recalcParts);
	}
	public void setLocation(final double x, final double y, final double scale) {
		setLocation(x, y, scale, true);
	}
	public void setLocation(final Point loc, final double scale)
	{
		setLocation(loc.getX(), loc.getY(), scale);
	}
	public Point2D getLocation() {
		return new Point2D.Double(path.getBounds2D().getCenterX(), path.getBounds2D().getCenterY());
	}

	
	public void setSelectedPart(final int part) {
		this.selectedPart = parts.get(part);
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
	public List<ImageFilter> getFilters()
	{
		List<ImageFilter> filters2 = new ArrayList<ImageFilter>();
		for (int i = 0; i < filters.size(); i++) {
			filters2.add(filters.get(i).getFilter());
		}
		return filters2;
	}
	public List<SkeletonFilter> getSkeletonFilters() {
		return filters;
	}
	
	private BufferedImage createImage(final double scale)
	{
		Shape drawShape = getScaledPath(scale);
		BufferedImage newImage = new BufferedImage((int)drawShape.getBounds2D().getWidth(), (int)drawShape.getBounds2D().getHeight(), BufferedImage.TYPE_INT_ARGB);
		double imgXOffset = drawShape.getBounds2D().getX();
		double imgYOffset = drawShape.getBounds2D().getY();
		AffineTransform transform = new AffineTransform();
		transform.translate(-imgXOffset, -imgYOffset);
		drawShape = transform.createTransformedShape(drawShape);
		Graphics2D g2d = (Graphics2D)newImage.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(color.getStoredValue());
		g2d.fill(drawShape);
		
		for (int i = 0; i < filters.size(); i++)
		{
			filters.get(i).getFilter().filter(newImage);
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

	@Override
	public void insert(MutableTreeNode child, int index) {
		this.getParts().add(index, (Part)child);
	}

	@Override
	public void remove(int index) {
		this.getParts().remove(index);
	}

	@Override
	public void remove(MutableTreeNode node) {
		this.getParts().remove(node);
	}

	@Override
	public void setUserObject(Object object) {
		
	}

	@Override
	public void removeFromParent() {
		this.parent.getSprites().remove(this);
	}

	@Override
	public void setParent(MutableTreeNode newParent) {
		
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return this.getParts().get(childIndex);
	}

	@Override
	public int getChildCount() {
		return this.getParts().size();
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public int getIndex(TreeNode node) {
		return parts.indexOf(node);
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public boolean isLeaf() {
		return parts.size() == 0;
	}

	@Override
	public Enumeration<? extends TreeNode> children() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void draw(Graphics2D g2d, double scale, DrawMode mode) {
		g2d.setStroke(new BasicStroke(5));
		g2d.setColor(getColor());
		Path2D path = getPath();
		AffineTransform trans = new AffineTransform();
		trans.scale(scale, scale);
		path = (Path2D)trans.createTransformedShape(path);
		g2d.fill(path);
		
		if (mode == DrawMode.SPRITE_EDIT) {
			for (int i = 0; i < parts.size(); i++) 
			{
				Part part = parts.get(i);
				List<Handle> handles = part.getHandles();
				for (int j = 0; j < handles.size(); j++) {
					Handle h = handles.get(j);
					if (selectedHandles.contains(h)) {
						g2d.setColor(Color.RED);
					}
					else {
						g2d.setColor(Color.BLUE);
					}
					if (h.getIsEnd()) {
						drawPoint(h.getX(), h.getY(), g2d, scale);
					}
					else {
						if (!multi) {
							drawHandle(h.getX(), h.getY(), g2d, scale);
						}
					}
				}
				
				if (snapping) {
					Handle p = selectedPart.getHandles().get(selectedPoint);
					if (snapX) {
						g2d.drawLine((int)p.getX() - (HANDLE_SIZE / 2), (int)p.getY(), (int)snapPointX.getX() - (HANDLE_SIZE / 2), (int)snapPointX.getY());
						g2d.drawLine((int)p.getX() + (HANDLE_SIZE / 2), (int)p.getY(), (int)snapPointX.getX() + (HANDLE_SIZE / 2), (int)snapPointX.getY());
					}
					if (snapY) {
						g2d.drawLine((int)p.getX(), (int)p.getY() - (HANDLE_SIZE / 2), (int)snapPointY.getX(), (int)snapPointY.getY() - (HANDLE_SIZE / 2));
						g2d.drawLine((int)p.getX(), (int)p.getY() + (HANDLE_SIZE / 2), (int)snapPointY.getX(), (int)snapPointY.getY() + (HANDLE_SIZE / 2));
					}
				}
			}
		}
	}

	@Override
	public Dimension getDrawSize(double scale) {
		Shape bounds = getPath().getBounds2D();
		AffineTransform transform = new AffineTransform();
		transform.scale(scale, scale);
		bounds = transform.createTransformedShape(bounds);
		return new Dimension((int)bounds.getBounds().getWidth(), (int)bounds.getBounds().getHeight());
	}

	@Override
	public void drawRender(Graphics2D g2d, double scale) {
		Path2D path = getPath();
		g2d.drawImage(multiImage, (int)(path.getBounds2D().getX() * scale),
				(int)(path.getBounds2D().getY() * scale), null);
	}

	@Override
	public void resyncAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generateRender(double scale) {
		this.multiImage = this.createMultiImage(scale);
	}

	@Override
	public void moveUp() throws ActionNotDoneException {
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
	public void moveDown() throws ActionNotDoneException {
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
	public List<SkeletonNode> getNodes() {
		return null;
	}

	@Override
	public boolean isMaster() {
		return this.getBone().isMaster();
	}
}
