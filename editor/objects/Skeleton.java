package editor.objects;

import java.util.ArrayList;
import java.util.List;

public class Skeleton {
	private List<SkeletonAnimation> animations;
	private SkeletonFrame masterFrame;
	
	public Skeleton()
	{
		animations = new ArrayList<SkeletonAnimation>();
		masterFrame = new SkeletonFrame();
	}
	
	public SkeletonFrame getMasterFrame() {
		return masterFrame;
	}
}
