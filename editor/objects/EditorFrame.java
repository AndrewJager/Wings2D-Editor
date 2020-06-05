package editor.objects;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import framework.Level;


public class EditorFrame {
	/** Name of this frame */
	private String name;
	/** Animation that this Frame belongs to */
	private EditorAnimation parent;
	/** Level that this Frame belongs to */
	private Level level;
	/** Options object for editor */
	private EditOptions options;
	/** The joints for this frame */
	private List<EditorJoint> joints;
	/** Time this frame will display for */
	private double frameTime;
	
	
	// Editor stuff
	private boolean editorIsMoving;
	private Point2D editorObjLoc;
	private int editorSelectedPoint;
	private EditorFrame editorChild = null;
	private int editorFrameTime;
	private int editorTimePassed;
	
	public EditorFrame(EditorAnimation parent, String name)
	{
		this.name = name;
		this.parent = parent;
		this.level = parent.getLevel();
		this.joints = new ArrayList<EditorJoint>();
		this.frameTime = 0; // Use the Animations delay time by default
		this.editorFrameTime = 100;
		this.editorTimePassed = 0;
	}
	
	public EditorFrame (EditorAnimation parent, String name, EditorFrame editParent)
	{
		this(parent, name);
		editParent.setEditorChild(this);
		
		for (int i = 0; i < editParent.getJoints().size(); i++)
		{
			this.addJoint(editParent.getJoint(i).copy());
		}
	}
	
	public void saveToFile(PrintWriter out)
	{
		out.write("FRAME:" + name + "\n");
		out.write("TIME:" + editorFrameTime + "\n");
		for (int i = 0; i < joints.size(); i++)
		{
			joints.get(i).saveToFile(out);
		}
		out.write("\n");
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	public String getName()
	{
		return name;
	}
	public EditorAnimation getAnimation()
	{
		return parent;
	}
	public Level getLevel()
	{
		return level;
	}
	public void addJoint(EditorJoint joint)
	{
		joints.add(joint);
	}
	public void addNewJoint(String jointName)
	{
		joints.add(new EditorJoint(this, jointName));
	}
	public List<EditorJoint> getJoints()
	{
		return joints;
	}
	public EditorJoint getJoint(int joint)
	{
		return joints.get(joint);
	}
	
	public EditorJoint getJointByName(String name)
	{
		for (int i = 0; i < joints.size(); i++)
		{
			if (joints.get(i).getName().equals(name))
			{
				return joints.get(i);
			}
		}
		return null; // No object found
	}
	public String[] getJointNames()
	{
		String[] names = new String[joints.size()];
		for (int i = 0; i < joints.size(); i++)
		{
			names[i] = joints.get(i).getName();
		}
		return names;
	}
	public double getFrameTime() {
		return frameTime;
	}

	public void setFrameTime(double frameTime) {
		this.frameTime = frameTime;
	}
	public void generateImages()
	{
		for (int i = 0; i < joints.size(); i++)
		{
			joints.get(i).makeImage();
		}
	}


	public void render(Graphics2D g2d, boolean debug)
	{
		for(int i = 0; i < joints.size(); i++)
		{
			joints.get(i).render(g2d, debug);
		}
	}
	
	

	// Editor logic processing
	public EditOptions getEditOptions()
	{
		return options;
	}
	public void setEditOptions(EditOptions options)
	{
		this.options = options;
	}
	public void setEditorChild(EditorFrame child)
	{
		this.editorChild = child;
	}
	public int getTimePassed() {
		return editorTimePassed;
	}
	public void setTimePassed(int timePassed) {
		this.editorTimePassed = timePassed;
	}
	public int getEditorFrameTime() {
		return editorFrameTime;
	}
	public void setEditorFrameTime(int frameTime) {
		this.editorFrameTime = frameTime;
	}
	public void addDefaultJoint(String jointName)
	{
		joints.add(new EditorJoint(this, jointName));
		EditorJoint curJoint = joints.get(joints.size() - 1);
		Path2D path = new Path2D.Double();
		curJoint.setPath(path);
		curJoint.addPoint(50, 0);
		curJoint.addPoint(100, 100);
		curJoint.addPoint(0, 100);
		
		if (editorChild != null)
		{
			editorChild.addDefaultJoint(jointName);
		}
	}
	public void processMousePress(int selected, Point mouseLoc)
	{
		editorIsMoving = false;
		EditorJoint joint = getJoint(selected);
		double scale = parent.getLevel().getManager().getScale();
		scale *= options.getEditor().RENDER_SCALE_TO_ACTUAL;
		AffineTransform transform = new AffineTransform();
		transform.scale(scale, scale);
		if (options.getEditing())
		{   
		    List<Ellipse2D> circles = new ArrayList<Ellipse2D>();
		    for (int i = 0; i < joint.getPoints().size(); i++)
		    {
		    	circles.add(new Ellipse2D.Double(joint.getPoints().get(i).getX() - (options.getEditHandleSize() / 2) + joint.getX(),
		    			joint.getPoints().get(i).getY() - (options.getEditHandleSize() / 2) + joint.getY(), options.getEditHandleSize(), 
		    			options.getEditHandleSize()));
		    }
		    
		    for (int i = 0; i < circles.size(); i++)
		    {
		    	if (circles.get(i).contains(mouseLoc))
		    	{
		    		editorIsMoving = true;
		    		editorObjLoc = new Point2D.Double(circles.get(i).getCenterX(), circles.get(i).getCenterY());
		    		editorSelectedPoint = i;
		    	}
		    }
		}
		else
		{
			Path2D.Double scaledPath = (Path2D.Double)joint.getPath().clone();
			scaledPath.transform(transform);
			Point2D.Double scaledPoint = new Point2D.Double(joint.getX(), joint.getY());
			transform.transform(scaledPoint, scaledPoint);
			Ellipse2D circle = new Ellipse2D.Double(scaledPath.getBounds2D().getCenterX() - (options.getEditHandleSize() / 2) + scaledPoint.getX(),
					scaledPath.getBounds2D().getCenterY() - (options.getEditHandleSize() / 2) + scaledPoint.getY(), options.getEditHandleSize(), 
					options.getEditHandleSize());
			if (circle.contains(mouseLoc))
			{
				editorIsMoving = true;
				editorObjLoc = new Point2D.Double(scaledPath.getBounds2D().getCenterX() + scaledPoint.getX(),
						scaledPath.getBounds2D().getCenterY() + scaledPoint.getY());
			}
		}
	}
	public void processMouseRelease(String selected, Point mouseLoc)
	{	
		if (editorIsMoving)
		{
			editorIsMoving = false;
			double scale = parent.getLevel().getManager().getScale();
			scale *= options.getEditor().RENDER_SCALE_TO_ACTUAL;
			double unScale = 1 / (scale);
			double xTranslate = mouseLoc.x - editorObjLoc.getX();
			double yTranslate = mouseLoc.y - editorObjLoc.getY();
			xTranslate *= unScale;
			yTranslate *= unScale;
			if (options.getEditing())
			{
				EditorJoint joint = getJointByName(selected);
				Point2D newPointLoc = new Point2D.Double(mouseLoc.getX() - joint.getX(), mouseLoc.getY() - joint.getY());
				if (joint != null)
				{
					joint.setPoint(editorSelectedPoint, newPointLoc);
					if (editorChild != null)
					{
						editorChild.childCopyPoints(joint, selected, joint.getPoints());
					}
				}
			}
			else
			{	
				moveObject(selected, xTranslate, yTranslate);
			}
		}
	}
	
	private void childCopyPoints(EditorJoint parentJoint, String jointName, List<Point2D> points)
	{
		EditorJoint joint = getJointByName(jointName);
		if (joint != null)
		{
			double xOffset = joint.getPath().getBounds2D().getX() - parentJoint.getPath().getBounds2D().getX();
			double yOffset = joint.getPath().getBounds2D().getY() - parentJoint.getPath().getBounds2D().getY();
			joint.setPoints(new ArrayList<Point2D>());
			for (int i = 0; i < points.size(); i++)
			{
				joint.addPoint(points.get(i).getX(), points.get(i).getY());
			}
			AffineTransform transform = new AffineTransform();
			transform.translate(xOffset, yOffset);
			joint.getPath().transform(transform);
			
			if (editorChild != null) // Copy to child frame
			{
				editorChild.childCopyPoints(parentJoint, jointName, points);
			}
		}
	}
	
	private void moveObject(String jointName, double xTranslate, double yTranslate)
	{
		EditorJoint joint = getJointByName(jointName);
		if (joint != null)
		{
			AffineTransform transform = new AffineTransform();
			transform.translate(xTranslate, yTranslate);
			joint.setX(joint.getX() + xTranslate);
			joint.setY(joint.getY() + yTranslate);
			if (editorChild != null && options.getCascadeChanges())
			{
				editorChild.moveObject(jointName, xTranslate, yTranslate);
			}
		}
	}

	public void addVertex(int selected)
	{
		if (selected != -1)
		{
			EditorJoint joint = joints.get(selected);
			joint.addPoint(joint.getPath().getBounds2D().getX(), joint.getPath().getBounds2D().getX());
			if (editorChild != null)
			{
				editorChild.addVertex(selected);
			}
		}
	}
	
	public void addNewJointFilter(String filterName, String jointName)
	{
		if (editorChild != null && options.getCascadeChanges())
		{
			EditorJoint joint = editorChild.getJointByName(jointName);
			joint.addNewFilter(filterName); // Will call the child frames addNewJointFillter in a roundabout way
		}
	}
	
	public void swapJointFilters(String jointName, int a, int b)
	{
		if (editorChild != null && options.getCascadeChanges())
		{
			EditorJoint thisJoint = getJointByName(jointName); 
			EditorJoint joint = editorChild.getJointByName(jointName);
			if (joint.getFilters().size() == thisJoint.getFilters().size()) // Don't swap if filter lists are clearly different. Should probably rework this to actually compare the filter lists.
			{
				joint.swapFilters(a, b); // Will call the child frame's swapJointFilters in a roundabout way
			}
		}
	}
	
	public void deleteJointFilter(String jointName, int filter)
	{
		if (editorChild != null && options.getCascadeChanges())
		{
			EditorJoint thisJoint = getJointByName(jointName); 
			EditorJoint joint = editorChild.getJointByName(jointName);
			if (joint.getFilters().size() == thisJoint.getFilters().size()) // Don't swap if filter lists are clearly different. Should probably rework this to actually compare the filter lists.
			{
				joint.deleteFilter(filter); // Will call the child frame's deleteJointFilter in a roundabout way
			}
		}
	}
}
