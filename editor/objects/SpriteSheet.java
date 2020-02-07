package editor.objects;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SpriteSheet{
	private static final long serialVersionUID = 1L;
	private List<Animation> animations;
	
	public SpriteSheet()
	{
		animations = new ArrayList<Animation>();
	}
	
	public SpriteSheet(File saveFile)
	{
		//create from file
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
