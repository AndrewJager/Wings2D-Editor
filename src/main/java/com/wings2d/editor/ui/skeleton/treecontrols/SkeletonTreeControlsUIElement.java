package com.wings2d.editor.ui.skeleton.treecontrols;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.wings2d.editor.objects.skeleton.SkeletonBone;
import com.wings2d.editor.objects.skeleton.SkeletonMasterFrame;
import com.wings2d.editor.objects.skeleton.SkeletonNode;

public abstract class SkeletonTreeControlsUIElement {
	protected JPanel panel;
	protected SkeletonTreeControls controls;
	protected JButton rename, delete;
	
	protected static final int SEPARATOR_WIDTH = 5;

	public SkeletonTreeControlsUIElement(final SkeletonTreeControls controls)
	{
		this.controls = controls;
		panel = new JPanel();
		delete = new JButton("Delete");
		rename = new JButton("Rename");
	}
	
	public void updateInfo(final SkeletonNode node)
	{
		panel.removeAll();
		updatePanelInfo(node);
		panel.revalidate();
		panel.repaint();
	}
	public void createEvents()
	{
		createOtherEvents();
		rename.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SkeletonNode selectedNode = (SkeletonNode)controls.getTree().getLastSelectedPathComponent();
				if (selectedNode != null)
				{
					TreePath path = controls.getTree().getSelectionPath();
					String newName = (String)JOptionPane.showInputDialog(panel, "","Rename",
		    				JOptionPane.PLAIN_MESSAGE, null, null, selectedNode.toString());
					if (newName != null)
					{
						selectedNode.setName(newName);
						DefaultTreeModel model = (DefaultTreeModel) controls.getTree().getModel();
						model.reload();
						
						controls.getTree().expandPath(path);
						controls.getTree().setSelectionPath(path);
					}
				}
			}
		});
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SkeletonNode selectedNode = (SkeletonNode)controls.getTree().getLastSelectedPathComponent();
				if (selectedNode != null)
				{
					try {
						if (selectedNode instanceof SkeletonMasterFrame)
						{
							throw new IllegalStateException("Cannot delete Master Frame!");
						}
						SkeletonNode parentNode = (SkeletonNode) selectedNode.getParent();
						DefaultTreeModel model = (DefaultTreeModel)controls.getTree().getModel();
						model.removeNodeFromParent(selectedNode);
						model.reload();
						TreePath path = new TreePath(parentNode);
						controls.getTree().setSelectionPath(path);		
					}
					catch (IllegalStateException ex){
						JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
	}
	
	public JPanel getPanel()
	{
		return panel;
	}
	
	protected abstract void updatePanelInfo(final SkeletonNode node);
	protected abstract void createOtherEvents();
	/** Only update the text on the labels. Override this event for use when moving objects in editor **/
	protected void updateVisibleInfo(final SkeletonNode node) {}
	
	protected void addNameLine()
	{
		JSeparator line = new JSeparator();
		line.setPreferredSize(new Dimension((int)(panel.getWidth() * 0.6), 1));
		panel.add(line);
	}
	protected void addLabel(final String text)
	{
		JLabel label = new JLabel(text, JLabel.CENTER);
		label.setPreferredSize(new Dimension(panel.getWidth(), (int)label.getPreferredSize().getHeight()));
		panel.add(label);
	}
	protected void addLabel(final JLabel label, final String text)
	{
		label.setText(text);
		label.setPreferredSize(new Dimension(panel.getWidth(), (int)label.getPreferredSize().getHeight()));
		panel.add(label);
	}
	protected void createList(final String[] data)
	{
		JList<String> list = new JList<String>(data);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(5);
		list.setFixedCellWidth(80);
		JScrollPane pane = new JScrollPane(list);
		panel.add(pane);
	}
	protected void setSelectedBone(final SkeletonBone bone)
	{
		bone.getFrame().setSelectedBone(bone);
		controls.getDrawingArea().setSelectedFrame(bone.getFrame());
		controls.getDrawingArea().getDrawArea().setDrawItem(bone.getFrame());	
	}
}
