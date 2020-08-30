package editor.objects.skeleton;

public class SkeletonMasterFrame extends SkeletonFrame{

	public SkeletonMasterFrame(String frameName) {
		super(frameName, null);
		
	}
	
	@Override
	public String toString()
	{
		return name + " (Master)";
	}
	@Override
	public int getTreeLevel()
	{
		return 1;
	}
}
