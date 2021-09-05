package com.wings2d.editor.ui.project;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.wings2d.editor.objects.EditorSettings;
import com.wings2d.editor.objects.project.Project;
import com.wings2d.editor.objects.skeleton.Skeleton;
import com.wings2d.editor.ui.UIElement;

public class ProjectSelect extends UIElement<ProjectEdit>{
	private JButton newProject, settingsBtn, newAnim;
	private JList<Project> projList;
	private DefaultListModel<Project> model; 
	
	private List<Project> projects;
	
	private Connection con;
	private EditorSettings settings;

	public ProjectSelect(final ProjectEdit edit, final Connection con, final EditorSettings settings) {
		super(edit);
		this.con = con;
		this.settings = settings;
		
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		projects = Project.getAll(con, settings);
		
		newProject = new JButton("New Project");
		panel.add(newProject);
		
		model = new DefaultListModel<Project>();
		for(int i = 0; i < projects.size(); i++) {
			model.addElement(projects.get(i));
		}
		projList = new JList<Project>(model);
		projList.setPreferredSize(new Dimension(200, 50));
		panel.add(projList);
		
		newAnim = new JButton("New Animation");
		newAnim.setEnabled(false);
		panel.add(newAnim);
		
		settingsBtn = new JButton("Settings");
		panel.add(settingsBtn);
		
	}

	public void createEvents() {
		newProject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
	            String name = (String)JOptionPane.showInputDialog(
	                    panel,
	                    "Choose project name", 
	                    "New Project",            
	                    JOptionPane.PLAIN_MESSAGE,
	                    null,            
	                    null, 
	                    "New Project"
	                 );
	            
	            if (name != null) {
					Project newProj;
					try {
						newProj = new Project(name, settings, con);
						model.addElement(newProj);
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
	            }
			}
		});
		projList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					Project project = (Project)projList.getSelectedValue();
					getEditPanel().setProject(project);
					getEditPanel().refreshInfo();
					newAnim.setEnabled(true);
				}
			}
		});
		settingsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getEditPanel().getEditor().showSettings();
			}
		});
		newAnim.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String skeletonName = JOptionPane.showInputDialog(panel, "Skeleton Name");
				if (skeletonName != null) {
					new Skeleton(skeletonName, getEditPanel().getProject().getID(),
							getEditPanel().getEditor().getSettings(), con);
					getEditPanel().refreshInfo();
				}
			}
		});
	}
}
