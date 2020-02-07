package editor.objects;

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
	private Options options;
	private int frameTime;
	private int timePassed;
	
	public Frame(String frameName, Boolean master)
	{
		name = frameName;
		objects = new ArrayList<Item>();
		isMaster = master;
		frameTime = 100;
		timePassed = 0;
	}
	
	public Frame(String frameName, Frame parentFrame)
	{
		name = frameName;
		objects = new ArrayList<Item>();
		isMaster = false;
		parent = parentFrame;
		parent.setChild(this);
		frameTime = 100;
		timePassed = 0;
		
		for (int i = 0; i < parent.getObjects().size(); i++)
		{
			this.addObject(parent.getObject(i).copy());
		}
	}
	
	public void processMousePress(int selected, Point mouseLoc)
	{
		isMoving = false;
		Item object = getObject(selected);
		if (options.getEditing())
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
	public void processMouseRelease(String selected, Point mouseLoc)
	{
		
		if (isMoving)
		{
			isMoving = false;
			double xTranslate = mouseLoc.x - objLoc.getX();
			double yTranslate = mouseLoc.y - objLoc.getY();
			if (options.getEditing())
			{
				Point2D newPointLoc = new Point2D.Double(mouseLoc.getX(), mouseLoc.getY());
				Item object = getObjectByName(selected);
				if (object != null)
				{
					object.setPoint(selectedPoint, newPointLoc);
					if (child != null)
					{
						child.childCopyPoints(object, selected, object.getPoints());
					}
				}
			}
			else
			{	
				moveObject(selected, xTranslate, yTranslate);
			}
		}
	}

	private void childCopyPoints(Item parentObj, String obj, List<Point2D> points)
	{
		Item object = getObjectByName(obj);
		if (object != null)
		{
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
	}
	
	private void moveObject(String obj, double xTranslate, double yTranslate)
	{
		Item object = getObjectByName(obj);
		if (object != null)
		{
			AffineTransform transform = new AffineTransform();
			transform.translate(xTranslate, yTranslate);
			for (int i = 0; i < object.getPoints().size(); i++)
			{
				object.getPoint(i).setLocation(object.getPoint(i).getX() + xTranslate, object.getPoint(i).getY() + yTranslate);
			}
			object.getPath().transform(transform);
			if (child != null && options.getCascadeChanges())
			{
				child.moveObject(obj, xTranslate, yTranslate);
			}
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
			if (child != null && options.getCascadeChanges())
			{
				child.setColor(selectedObj, color);
			}
		}
	}
	public void setColor(String obj, Color color)
	{
		Item curObject = getObjectByName(obj);
		if (curObject != null)
		{
			curObject.setColor(color);
			if (child != null && options.getCascadeChanges())
			{
				child.setColor(obj, color);
			}
		}
	}
	public Item getObject(int obj)
	{
		return objects.get(obj);
	}
	public Item getObjectByName(String name)
	{
		for (int i = 0; i < objects.size(); i++)
		{
			if (objects.get(i).getName().equals(name))
			{
				return objects.get(i);
			}
		}
		return null; // No object found
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

	public int getFrameTime() {
		return frameTime;
	}

	public void setFrameTime(int frameTime) {
		this.frameTime = frameTime;
	}

	public int getTimePassed() {
		return timePassed;
	}

	public void setTimePassed(int timePassed) {
		this.timePassed = timePassed;
	}

	public Options getOptions() {
		return options;
	}

	public void setOptions(Options options) {
		this.options = options;
	}
}
