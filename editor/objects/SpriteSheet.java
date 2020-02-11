package editor.objects;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import framework.Utils;
import framework.imageFilters.BasicVariance;
import framework.imageFilters.BlurEdges;
import framework.imageFilters.DarkenFrom;
import framework.imageFilters.ImageFilter;
import framework.imageFilters.LightenFrom;
import framework.imageFilters.Outline;
import framework.imageFilters.ShadeDir;

public class SpriteSheet{
	private List<Animation> animations;
	private String name;
	
	public SpriteSheet(String name)
	{
		animations = new ArrayList<Animation>();
		this.name = name;
	}
	
	public SpriteSheet(String name, Scanner in, Options options)
	{
		animations = new ArrayList<Animation>();
		this.name = name;
		
		in.useDelimiter(Pattern.compile("(\\n)")); // Regex. IDK
		
		Animation newAnim;
		Frame newFrame;
		Item newObject;
		
		List<Frame> frames;
		List<Item> objects;
		while(in.hasNext())
		{
			String line = in.next();
			if (line.strip() != "")
			{
				String[] split = line.split(":");
				String token = split[0];
				String value = split[1];

				switch (token)
				{
					case "SPRITE":
						break;
					case "ANIM":
						newAnim = new Animation(value);
						animations.add(newAnim);
						break;
					case "FRAME":
						newFrame = new Frame(value);
						newFrame.setOptions(options);
						animations.get(animations.size() - 1).getFrames().add(newFrame);
						break;
					case "TIME":
						frames = animations.get(animations.size() - 1).getFrames();
						frames.get(frames.size() - 1).setFrameTime(Integer.parseInt(value));
						break;
					case "OBJECT":
						frames = animations.get(animations.size() - 1).getFrames();
						newObject = new Item(value, frames.get(frames.size() - 1));
						frames.get(frames.size() - 1).addObject(newObject);
						break;
					case "POINTS":
						String[] points = value.split(";");
						for (int i = 0; i < points.length; i++)
						{
							String[] coords = points[i].split(",");
							frames = animations.get(animations.size() - 1).getFrames();
							objects = frames.get(frames.size() - 1).getObjects();
							objects.get(objects.size() - 1).addPoint(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]));
						}
						break;
					case "COLOR":
						Color newColor = Utils.stringToColor(value, ";");
						frames = animations.get(animations.size() - 1).getFrames();
						objects = frames.get(frames.size() - 1).getObjects();
						objects.get(objects.size() - 1).setColor(newColor);
						break;
					case "FILTERS":
						String[] filters = value.split(";");
						frames = animations.get(animations.size() - 1).getFrames();
						objects = frames.get(frames.size() - 1).getObjects();
						Item curItem = objects.get(objects.size() - 1);
						for (int i = 0; i < filters.length; i++)
						{
							String filter = filters[i];
							String[] filterParts = filter.split(ImageFilter.delimiter);
							switch (filterParts[0])
							{
								case BasicVariance.fileTitle:
									BasicVariance basicVar = new BasicVariance(Integer.parseInt(filterParts[1]));
									curItem.addFilter(basicVar);
									break;
								case BlurEdges.fileTitle:
									curItem.addFilter(new BlurEdges());
									break;
								case DarkenFrom.fileTitle:
									ShadeDir dir = ShadeDir.createFromString(filterParts[1]);
									DarkenFrom dark = new DarkenFrom(dir, Double.parseDouble(filterParts[2]));
									curItem.addFilter(dark);
									break;
								case LightenFrom.fileTitle:
									ShadeDir alsoDir = ShadeDir.createFromString(filterParts[1]);
									LightenFrom light = new LightenFrom(alsoDir, Double.parseDouble(filterParts[2]));
									curItem.addFilter(light);
									break;
								case Outline.fileTitle:
									Color col = Utils.stringToColor(filterParts[1], ",");
									curItem.addFilter(new Outline(col));
									break;
							}
						}
						break;
				}
			}
		}
	}
	
	public void saveToFile()
	{
		try {
			PrintWriter out = new PrintWriter(new FileOutputStream("Test/" + name + ".txt"));
			out.write("SPRITE:" + name);
			out.write("\n");
			for(int i = 0; i < animations.size(); i++)
			{
				animations.get(i).saveToFile(out);
			}
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public Animation getAnimation(int anim)
	{
		return animations.get(anim);
	}
	
	public String[] getAnimNames()
	{
		String[] names = new String[animations.size()];
		for (int i = 0; i < animations.size(); i++)
		{
			names[i] = animations.get(i).getName();
		}
		return names;
	}
	public Animation getAnimByName(String name)
	{
		for (int i = 0; i < animations.size(); i++)
		{
			if (animations.get(i).getName().equals(name))
			{
				return animations.get(i);
			}
		}
		return null; // No object found
	}
	public void addAnimation(String animName)
	{
		animations.add(new Animation(animName));
	}
	
	public List<Animation> getAnimations()
	{
		return animations;
	}
}
