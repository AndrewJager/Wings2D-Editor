package editor.objects;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Animation {
	private List<Frame> frames;
	private String name;
	
	public Animation(String animName)
	{
		name = animName;
		frames = new ArrayList<Frame>();
	}
	public void saveToFile(PrintWriter out)
	{
		out.write("ANIM:" + name + "\n");
		for (int i = 0; i < frames.size(); i++)
		{
			frames.get(i).saveToFile(out);
		}
		out.write("\n");
	}

	public Frame getFrame(int frame)
	{
		return frames.get(frame);
	}
	public List<Frame> getFrames()
	{
		return frames;
	}
	
	public String[] getFrameNames()
	{
		String[] names = new String[frames.size()];
		for (int i = 0; i < frames.size(); i++)
		{
			names[i] = frames.get(i).getName();
		}
		return names;
	}
	
	public void addFrame(String frameName)
	{
		frames.add(new Frame(frameName));
	}
	public void addFrame(String frameName, Frame parent)
	{
		frames.add(new Frame(frameName, parent));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
