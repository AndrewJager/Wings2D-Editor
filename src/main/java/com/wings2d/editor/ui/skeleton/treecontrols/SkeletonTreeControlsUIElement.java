package com.wings2d.editor.ui.skeleton.treecontrols;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultTreeModel;

import com.wings2d.editor.objects.skeleton.SkeletonBone;
import com.wings2d.editor.objects.skeleton.SkeletonMasterFrame;
import com.wings2d.editor.objects.skeleton.SkeletonNode;
import com.wings2d.editor.ui.edits.SetName;

public abstract class SkeletonTreeControlsUIElement {
	protected JPanel panel;
	protected SkeletonTreeControls controls;
	protected JPanel controlsPanel;
	protected JButton rename, delete;
	
	protected Connection con;
	
	protected static final int SEPARATOR_WIDTH = 5;

	public SkeletonTreeControlsUIElement(final SkeletonTreeControls controls, final Connection con)
	{
		this.controls = controls;
		this.con = con;
		
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		controlsPanel = new JPanel();
		delete = new JButton("Delete");
		rename = new JButton("Rename");
		controlsPanel.add(rename);
		controlsPanel.add(delete);
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
					String newName = (String)JOptionPane.showInputDialog(panel, "","Rename",
		    				JOptionPane.PLAIN_MESSAGE, null, null, selectedNode.toString());
					if (newName != null)
					{
						controls.getEditPanel().getEditor().getEditsManager().edit(new SetName(selectedNode, newName));
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
						int result = JOptionPane.showConfirmDialog(panel, "Deleting a node cannot be undone. Continue?", "Warning", JOptionPane.YES_NO_OPTION);
						if (result == JOptionPane.OK_OPTION) {
							SkeletonNode.delete(selectedNode.getID(), selectedNode.getTableName(), con);
							
							DefaultTreeModel model = (DefaultTreeModel)controls.getTree().getModel();
							model.removeNodeFromParent(selectedNode);
							controls.getEditPanel().getSkeletonTree().reloadModel();
						}
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
	
	protected void updatePanelInfo(final SkeletonNode node) {
		panel.removeAll();
	}
	protected abstract void createOtherEvents();
	/** Only update the text on the labels. Override this event for use when moving objects in editor **/
	protected void updateVisibleInfo(final SkeletonNode node) {}
	
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
