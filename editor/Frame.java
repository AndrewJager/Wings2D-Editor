package editor;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import framework.Image;

public class Frame {
	private List<Item> objects;
	private String name;
	private Boolean isMaster;
	private Boolean isMoving = false;
	private Point2D objLoc;
	private int selectedPoint;
	private Frame parent;
	private Frame child = null;
	
	public Frame(String frameName, Boolean master)
	{
		name = frameName;
		objects = new ArrayList<Item>();
		isMaster = master;
	}
	
	public Frame(String frameName, Frame parentFrame)
	{
		name = frameName;
		objects = new ArrayList<Item>();
		isMaster = false;
		parent = parentFrame;
		parent.setChild(this);
		
		for (int i = 0; i < parent.getObjects().size(); i++)
		{
			this.addObject(parent.getObject(i).copy());
		}
	}
	
	public void processMousePress(Boolean editing, int selected, Point mouseLoc)
	{
		isMoving = false;
		Item object = getObject(selected);
		if (editing)
		{   
		    List<Ellipse2D> circles = new ArrayList<Ellipse2D>();
		    for (int i = 0; i < object.getPoints().size(); i++)
		    {
		    	circles.add(new Ellipse2D.Double(object.getPoints().get(i).getX() - 6,
		    			object.getPoints().get(i).getY() - 6, 12, 12));
		    }
		    
		    for (int i = 0; i < circles.size(); i++)
		    {
		    	if (circles.get(i).contains(mouseLoc))
		    	{
		    		isMoving = true;
		    		objLoc = new Point2D.Double(circles.get(i).getCenterX(), circles.get(i).getCenterY());
		    		selectedPoint = i;
		    	}
		    }
		}
		else
		{
			Ellipse2D circle = new Ellipse2D.Double(object.getPath().getBounds2D().getCenterX() - 6,
					object.getPath().getBounds2D().getCenterY() - 6, 12, 12);
			
			if (circle.contains(mouseLoc))
			{
				isMoving = true;
				objLoc = new Point2D.Double(object.getPath().getBounds2D().getCenterX(), 
						object.getPath().getBounds2D().getCenterY());
			}
		}
	}
	public void processMouseRelease(Boolean editing, Boolean cascade, int selected, Point mouseLoc)
	{
		
		if (isMoving)
		{
			isMoving = false;
			double xTranslate = mouseLoc.x - objLoc.getX();
			double yTranslate = mouseLoc.y - objLoc.getY();
			if (editing)
			{
				Point2D newPointLoc = new Point2D.Double(mouseLoc.getX(), mouseLoc.getY());
				Item object = getObject(selected);
				object.setPoint(selectedPoint, newPointLoc);
				if (child != null)
				{
					child.childCopyPoints(object, selected, object.getPoints());
				}
			}
			else
			{	
				moveObject(selected, cascade, xTranslate, yTranslate);
			}
		}
	}

	private void childCopyPoints(Item parentObj, int obj, List<Point2D> points)
	{
		Item object = getObject(obj);
		double xOffset = object.getPath().getBounds2D().getX() - parentObj.getPath().getBounds2D().getX();
		double yOffset = object.getPath().getBounds2D().getY() - parentObj.getPath().getBounds2D().getY();
		object.setPoints(new ArrayList<Point2D>());
		for (int i = 0; i < points.size(); i++)
		{
			object.addPoint(points.get(i).getX(), points.get(i).getY());
		}
		AffineTransform transform = new AffineTransform();
		transform.translate(xOffset, yOffset);
		object.getPath().transform(transform);
		
		if (child != null)
		{
			child.childCopyPoints(parentObj, obj, points);
		}
	}
	
	private void moveObject(int obj, Boolean cascade, double xTranslate, double yTranslate)
	{
		Item object = getObject(obj);
		AffineTransform transform = new AffineTransform();
		transform.translate(xTranslate, yTranslate);
		for (int i = 0; i < object.getPoints().size(); i++)
		{
			object.getPoint(i).setLocation(object.getPoint(i).getX() + xTranslate, object.getPoint(i).getY() + yTranslate);
		}
		object.getPath().transform(transform);
		if (child != null && cascade)
		{
			child.moveObject(obj, cascade, xTranslate, yTranslate);
		}
	}

	public void addVertex(int selectedObj)
	{
		if (selectedObj != -1)
		{
			Item curObject = objects.get(selectedObj);
			curObject.addPoint(curObject.getPath().getBounds2D().getX(), curObject.getPath().getBounds2D().getX());
			if (child != null)
			{
				child.addVertex(selectedObj);
			}
		}
	}
	
	public void setColor(int selectedObj, Color color)
	{
		if (selectedObj != -1)
		{
			Item curObject = objects.get(selectedObj);
			curObject.setColor(color);
			if (child != null)
			{
				child.setColor(selectedObj, color);
			}
		}
	}
	public Item getObject(int obj)
	{
		return objects.get(obj);
	}
	public List<Item> getObjects()
	{
		return objects;
	}
	public void addObject(String objectName)
	{
		objects.add(new Item(objectName, this));
		Item curObject = objects.get(objects.size() - 1);
		GeneralPath path = new GeneralPath();
		curObject.setPath(path);
		curObject.addPoint(50, 0);
		curObject.addPoint(100, 100);
		curObject.addPoint(0, 100);
		
		if (child != null)
		{
			child.addObject(objectName);
		}
	}
	public void addObject(Item obj)
	{
		objects.add(obj);
	}
	public String[] getObjectNames()
	{
		String[] names = new String[objects.size()];
		for (int i = 0; i < objects.size(); i++)
		{
			names[i] = objects.get(i).getName();
		}
		return names;
	}

	public String getName()
	{
		return name;
	}

	public Boolean getIsMaster() {
		return isMaster;
	}

	public void setIsMaster(Boolean isMaster) {
		this.isMaster = isMaster;
	}

	public Frame getParent() {
		return parent;
	}

	public Frame getChild() {
		return child;
	}

	public void setChild(Frame child) {
		this.child = child;
	}
}
