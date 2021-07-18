package com.wings2d.editor.ui.skeleton.treecontrols;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.wings2d.framework.imageFilters.ImageFilter;

public class FiltersPopupMenu extends JPopupMenu{
	private static final long serialVersionUID = 1L;

	private JMenuItem delete;
	private JMenuItem moveUp;
	private JMenuItem moveDown;
	
	public FiltersPopupMenu(final ImageFilter filter, final List<ImageFilter> filters, final SpriteControls controls)
	{
		delete = new JMenuItem("Delete");
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				filters.remove(filter);
				controls.updatePanelInfo(controls.getCurrentSprite());
			}
		});
		this.add(delete);
		
		moveUp = new JMenuItem("Move Up");
		moveUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = filters.indexOf(filter);
				if (index > 0)
				{
					Collections.swap(filters, index, index - 1);
					controls.updatePanelInfo(controls.getCurrentSprite());
				}
			}
		});
		this.add(moveUp);
		
		moveDown = new JMenuItem("Move Down");
		moveDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = filters.indexOf(filter);
				if (index < filters.size() - 1)
				{
					Collections.swap(filters, index, index + 1);
					controls.updatePanelInfo(controls.getCurrentSprite());
				}
			}
		});
		this.add(moveDown);
	}
}
