package editor.ui.project;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import editor.objects.project.Project;
import editor.objects.project.ProjectEntity;

public class ProjectItems extends ProjectUIElement{
	private JList<ProjectEntity> list;

	public ProjectItems(final ProjectEdit edit, final Rectangle bounds) {
		super(edit, bounds);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		DefaultListModel<ProjectEntity> model = new DefaultListModel<ProjectEntity>();
		list = new JList<ProjectEntity>(model);
		panel.add(list);
	}
	
	public void setListItems(final Project project)
	{
		DefaultListModel<ProjectEntity> model = (DefaultListModel<ProjectEntity>)list.getModel();
		model.clear();
		for (int i = 0; i < project.getEntities().size(); i++)
		{
			model.addElement(project.getEntities().get(i));
		}
		
	}

	@Override
	public void createEvents() {
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				project.setSelectedEntity(list.getSelectedValue());
			}
		});
	}
}
