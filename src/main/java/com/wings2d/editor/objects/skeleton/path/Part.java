package com.wings2d.editor.objects.skeleton.path;

import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import com.wings2d.editor.objects.save.DBDouble;
import com.wings2d.editor.objects.save.DBInt;
import com.wings2d.editor.objects.save.DBUUID;
import com.wings2d.editor.objects.skeleton.SkeletonNode;
import com.wings2d.editor.ui.edits.ActionNotDoneException;

public class Part extends SkeletonNode{
	public static final String TABLE_NAME = "PATH";
	private List<Handle> handles;
	private Sprite parent;
	
	private DBUUID spriteID;
	private DBDouble x1;
	private DBDouble y1;
	private DBDouble x2;
	private DBDouble y2;
	private DBDouble x3;
	private DBDouble y3;
	private DBInt index;
	private DBInt type;
	
	public static Part insert(final Sprite parent, final int type, final int idx, final double x1, final double y1, final Connection con) {
		return new Part(parent, type, idx, x1, y1, con);
	}
	public static Part insert(final Sprite parent, final int type, final int idx, final double x1, final double y1, final double x2, final double y2,
			final Connection con) {
		return new Part(parent, type, idx, x1, y1, x2, y2, con);
	}
	public static Part insert(final Sprite parent, final int idx, final double x1, final double y1, final double x2, final double y2,
			final Connection con) {
		return Part.insert(parent, PathIterator.SEG_QUADTO, idx, x1, y1, x2, y2, con);
	}
	public static Part insert(final Sprite parent, final int type, final int idx, final double x1, final double y1, final double x2, final double y2,
			final double x3, final double y3, final Connection con) {
		return new Part(parent, type, idx, x1, y1, x2, y2, x3, y3, con);
	}
	public static Part insert(final Sprite parent, final int idx, final double x1, final double y1, final double x2, final double y2,
			final double x3, final double y3, final Connection con) {
		return Part.insert(parent, PathIterator.SEG_CUBICTO, idx, x1, y1, x2, y2, x3, y3, con);
	}
	public static Part read(final UUID pointID, final Sprite parent, final Connection con) {
		return new Part(pointID, parent, con);
	}
	
	public Part(final Sprite parent) {
		super(TABLE_NAME, false);
		
		this.parent = parent;
		handles = new ArrayList<Handle>();
		
		fields.add(spriteID = new DBUUID("Sprite"));
		fields.add(x1 = new DBDouble("X1"));
		fields.add(y1 = new DBDouble("Y1"));
		fields.add(x2 = new DBDouble("X2"));
		fields.add(y2 = new DBDouble("Y2"));
		fields.add(x3 = new DBDouble("X3"));
		fields.add(y3 = new DBDouble("Y3"));
		fields.add(index = new DBInt("PathOrder"));
		fields.add(type = new DBInt("PathType"));
	}

	// Insert constructors
	// MoveTo or LineTo
	private Part(final Sprite parent, final int type, final int idx, final double x1, final double y1, final Connection con) {
		this(parent);
		if ((type != PathIterator.SEG_MOVETO) && (type != PathIterator.SEG_LINETO)) {
			throw new IllegalArgumentException("Invalid type " + type);
		}
		handles.add(new Handle(this.x1, this.y1, true));
		
		spriteID.setStoredValue(parent.getID());
		index.setStoredValue(idx);
		this.type.setStoredValue(type);
		
		handles.get(0).setLocation(x1, y1);
		
		this.insert(con);
		this.query(con, id.getStoredValue());
	}
	// QuadTo
	private Part(final Sprite parent, final int type, final int idx, final double x1, final double y1, final double x2, final double y2,
			final Connection con) {
		this(parent);
		if (type != PathIterator.SEG_QUADTO) {
			throw new IllegalArgumentException("Invalid type " + type);
		}
		
		handles.add(new Handle(this.x1, this.y1));
		handles.add(new Handle(this.x2, this.y2, true));
		
		spriteID.setStoredValue(parent.getID());
		index.setStoredValue(idx);
		this.type.setStoredValue(type);
		
		handles.get(0).setLocation(x1, y1);
		handles.get(1).setLocation(x2, y2);
		
		this.insert(con);
		this.query(con, id.getStoredValue());
	}
	// CurveTo
	private Part(final Sprite parent, final int type, final int idx, final double x1, final double y1, final double x2, final double y2,
			final double x3, final double y3, final Connection con) {
		this(parent);
		if (type != PathIterator.SEG_CUBICTO) {
			throw new IllegalArgumentException("Invalid type " + type);
		}
		
		handles.add(new Handle(this.x1, this.y1));
		handles.add(new Handle(this.x2, this.y2));
		handles.add(new Handle(this.x3, this.y3, true));
		
		spriteID.setStoredValue(parent.getID());
		index.setStoredValue(idx);
		this.type.setStoredValue(type);
		
		handles.get(0).setLocation(x1, y1);
		handles.get(1).setLocation(x2, y2);
		handles.get(2).setLocation(x3, y3);
		
		this.insert(con);
		this.query(con, id.getStoredValue());
	}
	
	/** Read constructor */
	private Part(final UUID partID, final Sprite parent, final Connection con) {
		this(parent);

		this.query(con, partID);
		if (type.getStoredValue() == PathIterator.SEG_MOVETO || type.getStoredValue() == PathIterator.SEG_LINETO) {
			handles.add(new Handle(x1, y1, true));
		}
		if (type.getStoredValue() == PathIterator.SEG_QUADTO) {
			handles.add(new Handle(x1, y1));
			handles.add(new Handle(x2, y2, true));
		}
		if (type.getStoredValue() == PathIterator.SEG_CUBICTO) {
			handles.add(new Handle(x1, y1));
			handles.add(new Handle(x2, y2));
			handles.add(new Handle(x3, y3, true));
		}
	}
	
	public Part copy(final Sprite parent, final Connection con) {
		Part newPart = switch(type.getStoredValue())
		{
			case PathIterator.SEG_MOVETO, PathIterator.SEG_LINETO -> {
				yield Part.insert(parent, type.getStoredValue(), index.getStoredValue(), x1.getStoredValue(), x2.getStoredValue(), con);
			}
			case PathIterator.SEG_QUADTO -> {
				yield Part.insert(parent, type.getStoredValue(), index.getStoredValue(), x1.getStoredValue(), x2.getStoredValue(), 
						x2.getStoredValue(), y2.getStoredValue(), con);
			}
			case PathIterator.SEG_CUBICTO -> {
				yield Part.insert(parent, type.getStoredValue(), index.getStoredValue(), x1.getStoredValue(), x2.getStoredValue(), 
						x2.getStoredValue(), y2.getStoredValue(), x3.getStoredValue(), y3.getStoredValue(), con);
			}
			default -> {
				throw new IllegalStateException("Type " + type.getStoredValue() + " is not valid!");
			}
		};
		
		return newPart;
	}
	
	@Override
	public String toString() {
		switch (type.getStoredValue()) {
			case PathIterator.SEG_MOVETO, PathIterator.SEG_LINETO -> {
				return "Point (X:" + x1.getStoredValue() + " Y:" + y1.getStoredValue() +")";
			}
			case PathIterator.SEG_QUADTO -> {
				return "Quad (X:" + x2.getStoredValue() + " Y:" + y2.getStoredValue() + ")" 
				+ " Handle (X:" + x1.getStoredValue() + " Y:" + y1.getStoredValue() + ")";
			}
			case PathIterator.SEG_CUBICTO -> {
				return "Cubic (X:" + x3.getStoredValue() + " Y:" + y3.getStoredValue() + ")" 
				+ " Handle 1 (X:" + x1.getStoredValue() + " Y:" + y1.getStoredValue() + ")"
				+ " Handle 2 (X:" + x2.getStoredValue() + " Y:" + y2.getStoredValue() + ")";
			}
			default -> {
				return "Invalid type " + type.getStoredValue();
			}
		}
	}
	
	public void setHandle(final int handle, final double x, final double y) {
		handles.get(handle).setLocation(x, y);
	}

	public List<Handle> getHandles() {
		return handles;
	}

	public int getType() {
		return type.getStoredValue();
	}
	
	public Point2D getEndPoint() {
		return handles.get(handles.size() - 1);
	}
	
	public Sprite getSprite() {
		return parent;
	}

	@Override
	public void insert(MutableTreeNode child, int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(MutableTreeNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUserObject(Object object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeFromParent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setParent(MutableTreeNode newParent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getChildCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TreeNode getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getIndex(TreeNode node) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getAllowsChildren() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Enumeration<? extends TreeNode> children() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resyncAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generateRender(double scale) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveUp() throws ActionNotDoneException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveDown() throws ActionNotDoneException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<SkeletonNode> getNodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isMaster() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void deleteChildren(Connection con) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void queryChildren(UUID id, Connection con) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateChildren(Connection con) {
		// TODO Auto-generated method stub
		
	}
}
