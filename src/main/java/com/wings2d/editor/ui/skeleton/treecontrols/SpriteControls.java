package com.wings2d.editor.ui.skeleton.treecontrols;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
import com.wings2d.editor.ui.skeleton.filterEdits.CreateFilterDialog;
import com.wings2d.editor.ui.skeleton.filterEdits.FilterMap;
import com.wings2d.framework.imageFilters.ImageFilter;

public class SpriteControls extends SkeletonTreeControlsUIElement{
	public static final String CARD_ID = "Sprite";
	
	private JPanel namePanel, colorPanel, colorIndicator, vertexPanel, filterAddPanel;
	private JButton changeColor, newVertex, addFilter;
	private JList<ImageFilter> filters;
	private Sprite curSprite;

	public SpriteControls(final SkeletonTreeControls controls) {
		super(controls);
		
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
						ImageFilter newFilter = FilterMap.runEditDialog(filters.getSelectedValue(), controls.getSkeleton().getEditor().getFrame());
						if (newFilter != null) {
							curSprite.getFilters().set(filters.getSelectedIndex(), newFilter);
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
					curSprite.setColor(color);
					colorIndicator.setBackground(color);
					controls.getDrawingArea().getDrawArea().repaint();
				}
			}
		});
		newVertex.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Sprite sprite = (Sprite)controls.getTree().getLastSelectedPathComponent();
				sprite.addVertex(controls.getDrawingArea().getDrawArea().getUserLoc());
				controls.getDrawingArea().getDrawArea().repaint();
			}
		});
		addFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				CreateFilterDialog filterDlg = new CreateFilterDialog(controls.getSkeleton().getEditor().getFrame());
				Class<? extends ImageFilter> result = filterDlg.showDialog();
				if (result != null)
				{
					ImageFilter newFilter = FilterMap.runCreateDialog(result, controls.getSkeleton().getEditor().getFrame());
					if (newFilter != null)
					{
						curSprite.getFilters().add(newFilter);
						filters.setListData(curSprite.getFilters().toArray(new ImageFilter[0]));
					}
				}
			}
		});
	}

	public Sprite getCurrentSprite() {
		return curSprite;
	}
}
