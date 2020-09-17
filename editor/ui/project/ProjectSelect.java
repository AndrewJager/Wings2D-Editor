package editor.ui.project;

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
import javax.swing.JOptionPane;

import editor.objects.project.Project;
import editor.objects.skeleton.Skeleton;

public class ProjectSelect extends ProjectUIElement{
	private JButton chooseProject, newAnim;

	public ProjectSelect(final ProjectEdit edit, final Rectangle bounds) {
		super(edit, bounds);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		chooseProject = new JButton("Select Project Folder");
		panel.add(chooseProject);
		newAnim = new JButton("New Animation");
		panel.add(newAnim);
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
					try
					{
						Project proj = new Project(file.getSelectedFile());
						project.setProject(proj);
						project.refreshInfo();
					}
					catch (FileNotFoundException ex)
					{
						JOptionPane.showMessageDialog(panel, ex.getMessage());
					}
				}
			}
		});
		newAnim.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String skeletonName = JOptionPane.showInputDialog(panel, "Skeleton Name");
				JFileChooser file = new JFileChooser();
				file.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				file.setCurrentDirectory(new File(System.getProperty("user.dir")));
				int result = file.showOpenDialog(panel);
				if (result == JFileChooser.APPROVE_OPTION)
				{
					Skeleton newSkeleton = new Skeleton(skeletonName);
					project.getProject().getEntities().add(newSkeleton);
					project.refreshInfo();
					File newFile = new File(file.getSelectedFile() + "/" + skeletonName +".txt");
					try {
						newFile.createNewFile();
					} catch (IOException e1) {
	
					}
				}
			}
		});
	}
}
