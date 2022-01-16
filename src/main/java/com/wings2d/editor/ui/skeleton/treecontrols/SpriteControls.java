package com.wings2d.editor.ui.skeleton.treecontrols;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import com.wings2d.editor.objects.skeleton.SkeletonBone;
import com.wings2d.editor.objects.skeleton.SkeletonNode;
import com.wings2d.editor.objects.skeleton.Sprite;
import com.wings2d.editor.ui.edits.AddFilter;
import com.wings2d.editor.ui.edits.AddVertex;
import com.wings2d.editor.ui.edits.SetSpriteColor;
import com.wings2d.editor.ui.skeleton.filterEdits.CreateFilterDialog;
import com.wings2d.editor.ui.skeleton.filterEdits.FilterEditRunner;
import com.wings2d.framework.imageFilters.ImageFilter;

public class SpriteControls extends SkeletonTreeControlsUIElement{
	public static final String CARD_ID = "Sprite";
	
	private JPanel namePanel, colorPanel, colorIndicator, vertexPanel, filterAddPanel;
	private JButton changeColor, newVertex, addFilter;
	private JList<ImageFilter> filters;
	private Sprite curSprite;

	public SpriteControls(final SkeletonTreeControls controls, final Connection con) {
		super(controls, con);
		
		namePanel = new JPanel();
		
		colorPanel = new JPanel();
		colorIndicator = new JPanel();
		colorIndicator.setPreferredSize(new Dimension(10, 10));
		colorPanel.add(colorIndicator);
		changeColor = new JButton("Change Color");
		colorPanel.add(changeColor);
		
		vertexPanel = new JPanel();
		newVertex = new JButton("New Vertex");
		vertexPanel.add(newVertex);
		
		filterAddPanel = new JPanel();
		addFilter = new JButton("New Filter");
		filterAddPanel.add(addFilter);
		
		filters = new JList<ImageFilter>();
		filters.setLayoutOrientation(JList.VERTICAL);
		filters.setVisibleRowCount(5);
		filters.setFixedCellWidth(80);
		SpriteControls passThis = this;
		filters.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e))
				{
					FiltersPopupMenu menu = new FiltersPopupMenu(filters.getSelectedValue(), curSprite.getFilters(), passThis);
					menu.show(e.getComponent(), e.getX(), e.getY());
				}
				else
				{
					if (e.getClickCount() == 2) { // Double click
						ImageFilter newFilter = FilterEditRunner.runEditDialog(filters.getSelectedValue(), controls.getEditPanel().getEditor().getFrame());
						if (newFilter != null) {
							curSprite.getSkeletonFilters().get(filters.getSelectedIndex()).setFilter(newFilter);
							passThis.updatePanelInfo(curSprite);
						}
					}
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
		});
	}

	@Override
	protected void updatePanelInfo(final SkeletonNode node) {
		super.updatePanelInfo(node);
		curSprite = (Sprite)node;

		panel.add(namePanel); 
		namePanel.removeAll();
		namePanel.add(new JLabel(curSprite.toString()));
		panel.add(new JSeparator());
		
		panel.add(controlsPanel);
		panel.add(colorPanel);
		colorIndicator.setBackground(curSprite.getColor());
		panel.add(vertexPanel);
		panel.add(new JSeparator());
		
		panel.add(filterAddPanel);
		
		JPanel filterPanel = new JPanel();
		filters.setListData(curSprite.getFilters().toArray(new ImageFilter[0]));
		JScrollPane pane = new JScrollPane(filters);
		filterPanel.add(pane);
		filters.setFixedCellWidth(80);
		panel.add(filterPanel);
		
		panel.revalidate();
		
		SkeletonBone bone = curSprite.getBone();
		setSelectedBone(bone);
	}

	@Override
	protected void createOtherEvents() {
		changeColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(panel, "Choose Sprite color", curSprite.getColor());
				if (color != null)
				{
					controls.getEditPanel().getEditor().getEditsManager().edit(new SetSpriteColor(curSprite, color));
				}
			}
		});
		newVertex.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controls.getEditPanel().getEditor().getEditsManager().edit(new AddVertex(curSprite, 
						controls.getDrawingArea().getDrawArea().getUserLoc()));
			}
		});
		addFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				CreateFilterDialog filterDlg = new CreateFilterDialog(controls.getEditPanel().getEditor().getFrame());
				Class<? extends ImageFilter> result = filterDlg.showDialog();
				if (result != null)
				{
					ImageFilter newFilter = FilterEditRunner.runCreateDialog(result, controls.getEditPanel().getEditor().getFrame());
					if (newFilter != null)
					{
						controls.getEditPanel().getEditor().getEditsManager().edit(new AddFilter(curSprite, newFilter));
					}
				}
			}
		});
	}

	public Sprite getCurrentSprite() {
		return curSprite;
	}
}
