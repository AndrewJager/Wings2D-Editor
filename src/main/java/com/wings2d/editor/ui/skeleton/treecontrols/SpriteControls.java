package com.wings2d.editor.ui.skeleton.treecontrols;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import com.wings2d.editor.objects.skeleton.SkeletonBone;
import com.wings2d.editor.objects.skeleton.SkeletonFrame;
import com.wings2d.editor.objects.skeleton.SkeletonNode;
import com.wings2d.editor.objects.skeleton.Sprite;
import com.wings2d.editor.ui.skeleton.filterEdits.CreateFilterDialog;
import com.wings2d.editor.ui.skeleton.filterEdits.FilterMap;
import com.wings2d.framework.imageFilters.BasicVariance;
import com.wings2d.framework.imageFilters.ImageFilter;

public class SpriteControls extends SkeletonTreeControlsUIElement{
	public static final String CARD_ID = "Sprite";
	
	private JButton changeColor, newVertex, newFilter;
	private JList<ImageFilter> filters;
	private Sprite curSprite;

	public SpriteControls(final SkeletonTreeControls controls) {
		super(controls);
		changeColor = new JButton("Change Color");
		newVertex = new JButton("New Vertex");
		newFilter = new JButton("New Filter");
		
		filters = new JList<ImageFilter>();
		filters.setLayoutOrientation(JList.VERTICAL);
		filters.setVisibleRowCount(5);
		filters.setFixedCellWidth(80);
	}

	@Override
	protected void updatePanelInfo(final SkeletonNode node) {
		curSprite = (Sprite)node;
		addLabel(curSprite.toString());
		panel.add(rename);
		panel.add(delete);
		panel.add(changeColor);
		panel.add(newVertex);
		panel.add(new JSeparator(JSeparator.HORIZONTAL));
		panel.add(newFilter);
		panel.add(new JSeparator(JSeparator.HORIZONTAL));
		
		filters.setListData(curSprite.getFilters().toArray(new ImageFilter[0]));
		JScrollPane pane = new JScrollPane(filters);
		panel.add(pane);
	}

	@Override
	protected void createOtherEvents() {
		changeColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(panel, "Choose Sprite color", curSprite.getColor());
				if (color != null)
				{
					curSprite.setColor(color);
					controls.getDrawingArea().getDrawArea().repaint();
				}
			}
		});
		newVertex.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Sprite sprite = (Sprite)controls.getTree().getLastSelectedPathComponent();
				sprite.addVertex();
			}
		});
		newFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				CreateFilterDialog filterDlg = new CreateFilterDialog(controls.getSkeleton().getEditor().getFrame());
				Class<? extends ImageFilter> result = filterDlg.showDialog();
				if (result != null)
				{
					ImageFilter newFilter = FilterMap.runDialog(result, controls.getSkeleton().getEditor().getFrame());
					if (newFilter != null)
					{
						curSprite.getFilters().add(newFilter);
						filters.setListData(curSprite.getFilters().toArray(new ImageFilter[0]));
					}
				}
			}
		});
	}

}
