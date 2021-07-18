package com.wings2d.editor.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;
import com.wings2d.editor.objects.EditOptions;
import com.wings2d.editor.objects.EditorSettings;
import com.wings2d.editor.objects.skeleton.Skeleton;
import com.wings2d.editor.ui.edits.EditsManager;
import com.wings2d.editor.ui.project.ProjectEdit;
import com.wings2d.editor.ui.skeleton.SkeletonEdit;
import com.wings2d.framework.Level;
import com.wings2d.framework.LevelManager;

public class Editor {
	private LevelManager manager;
	private int timeStep = 10;
	private boolean playing = false;
	private Level demoLevel;
	private EditOptions options;
	private List<UIPanel> panels;
	private EditorSettings settings;
	
	private JFrame frame;
	private JPanel mainPanel;
	private CardLayout cards;
	
	private ProjectEdit projectEdit;
	private SkeletonEdit skeletonEdit;
	
	private double UIScale;
	public int frameStartWidth = 1500;
	public int frameStartHeight = 800;
	public double RENDER_SCALE = 0.25;
	public double RENDER_SCALE_TO_ACTUAL = 1 / RENDER_SCALE;
	
	private EditsManager undo;

	public void run() {
		manager = new LevelManager();
		demoLevel = new Level(manager, 0);
		options = new EditOptions(this);
		
		FlatLightLaf.install();
		UIManager.put( "ScrollBar.showButtons", true );
		
		frame = new JFrame("Editor");
		frame.setBackground(Color.BLACK);
		frame.setLayout(new GridBagLayout());
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		mainPanel = new JPanel();
		cards = new CardLayout();
		mainPanel.setLayout(cards);
		mainPanel.setBackground(Color.DARK_GRAY);
		frame.add(mainPanel);
		
		panels = new ArrayList<UIPanel>();
		projectEdit = new ProjectEdit(this);
		skeletonEdit = new SkeletonEdit(this);
		
		undo = new EditsManager(skeletonEdit.getSkeletonTree());
		
		mainPanel.add(projectEdit.getPanel(), ProjectEdit.CARD_ID);		
		mainPanel.add(skeletonEdit.getPanel(), SkeletonEdit.CARD_ID);
		showProject();
		
		for (int i = 0; i < panels.size(); i++)
		{
			panels.get(i).initElements();
		}
		
		settings = new EditorSettings();
		if (settings.getProjectDirectory() != null)
		{
			projectEdit.getProjectSelect().setProject(settings.getProjectDirectory());
		}

	    frame.addWindowListener(new WindowAdapter(){
	    	public void windowClosing(WindowEvent e){
	    		settings.saveToFile();
	    		System.exit(0);
	    	}
	    });	
		frame.addComponentListener(new ComponentAdapter() 
		{
			public void componentResized(ComponentEvent evt) {		
				int width = frame.getWidth();
		        int height = frame.getHeight();
		        double NINE_TO_SIXTEEN = 1.77777778;
		        double SIXTEEN_TO_NINE = 0.5625;

		    	if ((width * SIXTEEN_TO_NINE) <= height )
		    	{
		    		mainPanel.setSize(new Dimension(width, (int)(width * SIXTEEN_TO_NINE)));
		    	}
		    	else
		    	{
		    		mainPanel.setSize(new Dimension((int)(height * NINE_TO_SIXTEEN), height));
		    	}
		    	frame.revalidate();
		    	int xPos = (frame.getWidth() - mainPanel.getWidth()) / 2;
		    	int yPos = (frame.getHeight() - mainPanel.getHeight()) / 2;
		    	mainPanel.setLocation((int)(xPos * 0.5), yPos);
		    	mainPanel.setLocation(0, 0);
		    	mainPanel.revalidate();
		    	
		    	UIScale = Double.valueOf(getMainPanel().getWidth()) / frameStartWidth;
				for (int i = 0; i < panels.size(); i++)
				{
					panels.get(i).resize(mainPanel, UIScale);
				}
				
//				if (animLists.getAnimList().getSelectedIndex() != -1)
//				{
//					drawing.setShouldRedraw(true);
//				}
//				render.setShouldReRender(true);
				manager.setScale(RENDER_SCALE * UIScale);
				demoLevel.rescale();
			}
		});
		frame.setSize(frameStartWidth, frameStartHeight);
		frame.setMinimumSize(new Dimension(1250, 720));
		frame.setLocationRelativeTo(null);
		frame.setLayout(null); 
		frame.setVisible(true);
		frameStartWidth = frame.getWidth();
		frameStartHeight = frame.getHeight();
	}
	
	public void showProject()
	{
		cards.show(mainPanel, ProjectEdit.CARD_ID);
	}
	public void showSkeleton(final Skeleton skeleton)
	{
		skeletonEdit.setCurrentSkeleton(skeleton);
		cards.show(mainPanel, SkeletonEdit.CARD_ID);
	}

	public LevelManager getManager() {
		return manager;
	}
	public int getTimeStep() {
		return timeStep;
	}
	public Level getDemoLevel() {
		return demoLevel;
	}
	public EditOptions getOptions() {
		return options;
	}
	public JFrame getFrame() {
		return frame;
	}
	public JPanel getMainPanel() {
		return mainPanel;
	}
	public SkeletonEdit getSkeletonEdit() {
		return skeletonEdit;
	}
	public boolean getPlaying() {
		return playing;
	}
	public void setPlaying(final Boolean play)
	{
		playing = play;
	}
	public double getUIScale()
	{
		return UIScale;
	}
	public List<UIPanel> getPanels()
	{
		return panels;
	}
	public EditorSettings getSettings()
	{
		return settings;
	}
	public EditsManager getEditsManager() {
		return undo;
	}
}
