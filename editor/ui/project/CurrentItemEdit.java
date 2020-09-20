package editor.ui.project;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;

import editor.objects.project.ProjectEntity;
import editor.objects.skeleton.Skeleton;

public class CurrentItemEdit extends ProjectUIElement{
	private JLabel nameLabel;
	private JButton editItem;
	private ProjectEntity selectedItem;
	
	public CurrentItemEdit(final ProjectEdit edit, final Rectangle bounds) {
		super(edit, bounds);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		nameLabel = new JLabel("Name");
		panel.add(nameLabel);
		editItem = new JButton("Edit");
		editItem.setEnabled(false);
		panel.add(editItem);
	}
	
	public void setItem(final ProjectEntity item)
	{
		editItem.setEnabled(true);
		if (item instanceof Skeleton)
		{
			nameLabel.setText(item.toString());
			selectedItem = item;
		}
	}

	@Override
	public void createEvents() {
		editItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selectedItem != null)
				{
					if (selectedItem instanceof Skeleton)
					{
						projectEdit.getEditor().showSkeleton((Skeleton)selectedItem);
					}
				}
			}
		});
	}
}
