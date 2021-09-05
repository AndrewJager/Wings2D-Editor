package com.wings2d.editor.ui.project;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.wings2d.editor.objects.EditorSettings;
import com.wings2d.editor.objects.project.Project;
import com.wings2d.editor.objects.project.ProjectEntity;
import com.wings2d.editor.objects.skeleton.Skeleton;
import com.wings2d.editor.ui.UIElement;

public class ProjectSelect extends UIElement<ProjectEdit>{
	private JButton chooseProject, newAnim, saveProject, settingsBtn;
	private JLabel projectName;
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
		
		chooseProject = new JButton("Select Project Folder");
		panel.add(chooseProject);
		
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
		projectName = new JLabel("No project open");
		panel.add(projectName);
		saveProject = new JButton("Save");
		panel.add(saveProject);
		settingsBtn = new JButton("Settings");
		panel.add(settingsBtn);
		
	}

	public void createEvents() {
		chooseProject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser file = new JFileChooser();
				file.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				file.setCurrentDirectory(new File(System.getProperty("user.dir")));
				int result = file.showOpenDialog(panel);
				if (result == JFileChooser.APPROVE_OPTION)
				{
					try {
						Project newProj = new Project(file.getSelectedFile(), "Test", settings, con);
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
				Project project = (Project)projList.getSelectedValue();
				getEditPanel().setProject(project);
				getEditPanel().getEditor().getSettings().setProjectDirectory(project.getDirectory());
				getEditPanel().refreshInfo();
				projectName.setText("Name: " + getEditPanel().getProject().getName());
				newAnim.setEnabled(true);
			}
		});
		newAnim.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String skeletonName = JOptionPane.showInputDialog(panel, "Skeleton Name");
				JFileChooser file = new JFileChooser();
				file.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				file.setCurrentDirectory(getEditPanel().getProject().getDirectory());
				int result = file.showOpenDialog(panel);
				if (result == JFileChooser.APPROVE_OPTION)
				{
					Skeleton newSkeleton = new Skeleton(skeletonName, getEditPanel().getProject(), getEditPanel().getEditor().getSettings());
					getEditPanel().getProject().getEntities().add(newSkeleton);
					getEditPanel().refreshInfo();
					File newFile = new File(file.getSelectedFile() + "/" + skeletonName +".txt");
					try {
						newFile.createNewFile();
					} catch (IOException e1) {
	
					}
				}
			}
		});
		saveProject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getEditPanel().getProject().saveProject();
			}
		});
		settingsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getEditPanel().getEditor().showSettings();
			}
		});
	}
}
