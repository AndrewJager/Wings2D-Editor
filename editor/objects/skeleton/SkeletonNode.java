package editor.objects.skeleton;

import java.io.PrintWriter;

import javax.swing.tree.MutableTreeNode;

public interface SkeletonNode extends MutableTreeNode{
	public int getTreeLevel();
	public void setName(final String newName);
	public void saveToFile(final PrintWriter out);
	public void resyncAll();
}
