package editor.objects;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class SpriteSheet{
	private List<Animation> animations;
	private String name;
	
	public SpriteSheet(String name)
	{
		animations = new ArrayList<Animation>();
		this.name = name;
	}
	
	public SpriteSheet(String name, Scanner in)
	{
		animations = new ArrayList<Animation>();
		this.name = name;
		
		in.useDelimiter(Pattern.compile("(\\n)|;")); // Regex. IDK
		while(in.hasNext())
		{
			String line = in.next();
			System.out.println(line);
			if (line.strip() != "")
			{
				String[] split = line.split(":");
				String token = split[0];
				String value = split[1];
				Animation newAnim;
				Frame newFrame;
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
						animations.get(animations.size() - 1).getFrames().add(newFrame);
						break;
					case "TIME":
						List<Frame> frames = animations.get(animations.size() - 1).getFrames();
						frames.get(frames.size() - 1).setFrameTime(Integer.parseInt(value));
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
	
	public void addAnimation(String animName)
	{
		animations.add(new Animation(animName));
	}
	
	public List<Animation> getAnimations()
	{
		return animations;
	}
}
