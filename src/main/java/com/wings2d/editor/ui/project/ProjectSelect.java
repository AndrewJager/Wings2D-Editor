package com.wings2d.editor.ui.project;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.wings2d.editor.objects.project.Project;
import com.wings2d.editor.objects.skeleton.Skeleton;

public class ProjectSelect extends ProjectUIElement{
	private JButton chooseProject, newAnim, saveProject;
	private JLabel projectName;

	public ProjectSelect(final ProjectEdit edit, final Rectangle bounds) {
		super(edit, bounds);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		chooseProject = new JButton("Select Project Folder");
		panel.add(chooseProject);
		newAnim = new JButton("New Animation");
		newAnim.setEnabled(false);
		panel.add(newAnim);
		projectName = new JLabel("No project open");
		panel.add(projectName);
		saveProject = new JButton("Save");
		panel.add(saveProject);
	}
	
	public void setProject(final File projectPath)
	{
		try
		{
			Project proj = new Project(projectPath);
			projectEdit.getEditor().getSettings().setProjectDirectory(projectPath);
			projectEdit.setProject(proj);
			projectEdit.refreshInfo();
			projectName.setText("Name: " + projectEdit.getProject().getName());
			newAnim.setEnabled(true);
		}
		catch (FileNotFoundException ex)
		{
			JOptionPane.showMessageDialog(panel, ex.getMessage());
		}
	}

	@Override
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
					setProject(file.getSelectedFile());
				}
			}
		});
		newAnim.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String skeletonName = JOptionPane.showInputDialog(panel, "Skeleton Name");
				JFileChooser file = new JFileChooser();
				file.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				file.setCurrentDirectory(projectEdit.getProject().getDirectory());
				int result = file.showOpenDialog(panel);
				if (result == JFileChooser.APPROVE_OPTION)
				{
					Skeleton newSkeleton = new Skeleton(skeletonName, projectEdit.getProject());
					projectEdit.getProject().getEntities().add(newSkeleton);
					projectEdit.refreshInfo();
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
				projectEdit.getProject().saveProject();
			}
		});
	}
}
