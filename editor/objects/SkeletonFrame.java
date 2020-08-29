package editor.objects;

import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class SkeletonFrame implements MutableTreeNode, ISkeleton{
	private SkeletonAnimation parent;
	private List<SkeletonBone> bones;
	private String name;
	
	public void setName(String newName)
	{
		name = newName;
	}
	public String toString()
	{
		return name;
	}
	public int getTreeLevel()
	{
		return 2;
	}
	@Override
	public TreeNode getChildAt(int childIndex) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getChildCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public TreeNode getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getIndex(TreeNode node) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean getAllowsChildren() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Enumeration<? extends TreeNode> children() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void insert(MutableTreeNode child, int index) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void remove(int index) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void remove(MutableTreeNode node) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setUserObject(Object object) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void removeFromParent() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setParent(MutableTreeNode newParent) {
		// TODO Auto-generated method stub
		
	}
	

}
