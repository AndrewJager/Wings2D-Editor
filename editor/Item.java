package editor;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;

import framework.Image;
import framework.Level;
import framework.imageFilters.BasicVariance;
import framework.imageFilters.BlurEdges;
import framework.imageFilters.DarkenFrom;
import framework.imageFilters.ImageFilter;
import framework.imageFilters.LightenFrom;
import framework.imageFilters.Outline;
import framework.imageFilters.ShadeDir;

public class Item {
	private String name;
	private GeneralPath path;
	private Image image;
	private Color color;
	private Color fadedColor;
	private List<Point2D> points;
	private List<ImageFilter> filters;
	private Frame parent;
	
	public Item(String objectName, Frame parentFrame)
	{
		name = objectName;
		points = new ArrayList<Point2D>();
		filters = new ArrayList<ImageFilter>();
		setColor(Color.BLACK);
		parent = parentFrame;
	}
	
	public void makeImage(Level level)
	{
		double xPos = path.getBounds2D().getX();
		double yPos = path.getBounds2D().getY();
		GeneralPath imgPath = new GeneralPath();
		imgPath.moveTo(points.get(0).getX(), points.get(0).getY());
		for (int i = 1; i < points.size(); i++)
		{
			imgPath.lineTo(points.get(i).getX(), points.get(i).getY());
		}
		AffineTransform transform = new AffineTransform();
		transform.translate(-xPos, -yPos); // Move to 0/0
		imgPath.transform(transform);
		image = new Image(imgPath, color, level);
		image.setX(xPos * 0.25);
		image.setY(yPos * 0.25);
	}
	public Image getImage()
	{
		return image;
	}

	public void setPoint(int point, Point2D newPos)
	{
		points.set(point, newPos); 
		GeneralPath newPath = new GeneralPath();
		
		newPath.moveTo(points.get(0).getX(), points.get(0).getY());
		for (int i = 0; i < points.size(); i++)
		{
			newPath.lineTo(points.get(i).getX(), points.get(i).getY());
		}
		path = newPath;
	}
	public void addPoint(double x, double y)
	{
		points.add(new Point2D.Double(x, y));
		GeneralPath newPath = new GeneralPath();
		
		newPath.moveTo(points.get(0).getX(), points.get(0).getY());
		for (int i = 1; i < points.size(); i++)
		{
			newPath.lineTo(points.get(i).getX(), points.get(i).getY());
		}
		path = newPath;
	}
	public Item copy()
	{
		Item newObj = new Item(this.name, this.parent);
		newObj.setColor(new Color(this.color.getRGB()));
		newObj.setPath(new GeneralPath());
		for (int i = 0; i < this.points.size(); i++)
		{
			newObj.addPoint(this.points.get(i).getX(), this.points.get(i).getY());
		}
		
		return newObj;
	}
	
	public void addNewFilter(String filterName)
	{
		if (filterName == "Basic Variance")
		{
			addFilter(new BasicVariance());
		}
		else if (filterName == "Blur Edges")
		{
			addFilter(new BlurEdges());
		}
		else if (filterName == "Darken From")
		{
			addFilter(new DarkenFrom(ShadeDir.TOP, 1));
		}
		else if (filterName == "Lighten From")
		{
			addFilter(new LightenFrom(ShadeDir.TOP, 1));
		}
		else if (filterName == "Outline")
		{
			addFilter(new Outline(Color.BLACK));
		}
	}
	public void swapFilters(int a, int b)
	{
		if (a >= 0 && b >= 0 && a < filters.size() && b < filters.size())
		{
			ImageFilter temp = filters.get(a);
			filters.set(a, filters.get(b));
			filters.set(b, temp);
		}
	}
	public Point2D getPoint(int i)
	{
		return points.get(i);
	}
	public List<Point2D> getPoints()
	{
		return points;
	}
	public void setPoints(List<Point2D> newPoints)
	{
		points = newPoints;
	}
	public String getName()
	{
		return name;
	}
	public void setPath(GeneralPath newPath)
	{
		path = newPath;
	}
	public GeneralPath getPath()
	{
		return path;
	}
	public void setColor(Color col)
	{
		color = col;
		Color faded = new Color(col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha() / 2);
		fadedColor = faded;
	}
	public Color getColor()
	{
		return color;
	}
	public Color getFadedColor()
	{
		return fadedColor;
	}
	public void addFilter(ImageFilter filter)
	{
		filters.add(filter);
	}
	public List<ImageFilter> getFilters()
	{
		return filters;
	}
}
