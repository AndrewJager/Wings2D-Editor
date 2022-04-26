package com.wings2d.editor.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;
import com.wings2d.editor.objects.EditOptions;
import com.wings2d.editor.objects.EditorSettings;
import com.wings2d.editor.objects.project.BindingsManager;
import com.wings2d.editor.objects.save.DBAccess;
import com.wings2d.editor.objects.skeleton.Skeleton;
import com.wings2d.editor.ui.edits.EditsManager;
import com.wings2d.editor.ui.project.ProjectEdit;
import com.wings2d.editor.ui.settings.Hotkeys;
import com.wings2d.editor.ui.settings.SettingsEdit;
import com.wings2d.editor.ui.skeleton.SkeletonEdit;
import com.wings2d.framework.Level;
import com.wings2d.framework.LevelManager;

public class Editor {
	private LevelManager manager;
	private int timeStep = 10;
	private boolean playing = false;
	private Level demoLevel;
	
	private EditOptions options;
	private EditorSettings settings;
	private DBAccess db;
	
	
	private JFrame frame;
	private JPanel mainPanel;
	private CardLayout cards;
	
	private ProjectEdit projectEdit;
	private SkeletonEdit skeletonEdit;
	private SettingsEdit settingsEdit;
	
	private double UIScale;
	public int frameStartWidth = 1500;
	public int frameStartHeight = 800;
	public double RENDER_SCALE = 0.25;
	public double RENDER_SCALE_TO_ACTUAL = 1 / RENDER_SCALE;
	
	private EditsManager undo;

	public void run() {
		db = new DBAccess();
		
		manager = new LevelManager();
		demoLevel = new Level(manager, 0);
		options = new EditOptions(this);
		settings = new EditorSettings(db.getConnection());
		
		FlatLightLaf.setup();
		UIManager.put( "ScrollBar.showButtons", true );
		
		frame = new JFrame("Editor");
		frame.setBackground(Color.BLACK);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		mainPanel = new JPanel();
		cards = new CardLayout();
		mainPanel.setLayout(cards);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		frame.add(mainPanel);
		
		projectEdit = new ProjectEdit(this, db.getConnection(), settings);
		skeletonEdit = new SkeletonEdit(this, db.getConnection());
		settingsEdit = new SettingsEdit(this, settings);
		
		undo = new EditsManager(skeletonEdit.getSkeletonTree());
		
		mainPanel.add(projectEdit.getPanel(), ProjectEdit.CARD_ID);		
		mainPanel.add(skeletonEdit.getPanel(), SkeletonEdit.CARD_ID);
		mainPanel.add(settingsEdit.getPanel(), SettingsEdit.CARD_ID);
		showProject();
		
		projectEdit.initElements();
		skeletonEdit.initElements();
		settingsEdit.initElements();
		
		
		HashMap<String, Runnable> keyActions = new HashMap<String, Runnable>();
		keyActions.put(Hotkeys.Undo.name(), () -> getEditsManager().undo());
		keyActions.put(Hotkeys.Redo.name(), () -> getEditsManager().redo());
		keyActions.put(Hotkeys.Snap.name(), () -> skeletonEdit.getDrawingArea().getControls().getSnapBtn().doClick());
		keyActions.put(Hotkeys.Render.name(), () -> skeletonEdit.getRenderArea().getControls().getRenderBtn().doClick());
		keyActions.put(Hotkeys.Save.name(), () -> skeletonEdit.getTopBar().getSaveBtn().doClick());

		new BindingsManager(settings.getKeyBinds(), keyActions, mainPanel);
		
		
	    frame.addWindowListener(new WindowAdapter(){
	    	@Override
	    	public void windowClosing(WindowEvent e){
	    		db.closeConnection();
	    		System.exit(0);
	    	}
	    });	
		frame.addComponentListener(new ComponentAdapter() 
		{
			@Override
			public void componentResized(ComponentEvent evt) {		
//				int width = frame.getWidth();
//		        int height = frame.getHeight();
//		        double NINE_TO_SIXTEEN = 1.77777778;
//		        double SIXTEEN_TO_NINE = 0.5625;
//
//		    	if ((width * SIXTEEN_TO_NINE) <= height )
//		    	{
//		    		mainPanel.setSize(new Dimension(width, (int)(width * SIXTEEN_TO_NINE)));
//		    	}
//		    	else
//		    	{
//		    		mainPanel.setSize(new Dimension((int)(height * NINE_TO_SIXTEEN), height));
//		    	}
//		    	frame.revalidate();
//		    	int xPos = (frame.getWidth() - mainPanel.getWidth()) / 2;
//		    	int yPos = (frame.getHeight() - mainPanel.getHeight()) / 2;
//		    	mainPanel.setLocation((int)(xPos * 0.5), yPos);
//		    	mainPanel.setLocation(0, 0);
//		    	mainPanel.revalidate();
//		    	
		    	UIScale = Double.valueOf(getMainPanel().getWidth()) / frameStartWidth;
				
//				if (animLists.getAnimList().getSelectedIndex() != -1)
//				{
//					drawing.setShouldRedraw(true);
//				}
//				render.setShouldReRender(true);
//				manager.setScale(RENDER_SCALE * UIScale);
//				demoLevel.rescale();
			}
		});
		frame.setSize(frameStartWidth, frameStartHeight);
		frame.setMinimumSize(new Dimension(1250, 720));
		frame.setLocationRelativeTo(null);
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
		skeletonEdit.getTopBar().setHeaderText(skeleton.getName());
		cards.show(mainPanel, SkeletonEdit.CARD_ID);
	}
	public void showSettings() {
		cards.show(mainPanel, SettingsEdit.CARD_ID);
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
	public EditorSettings getSettings()
	{
		return settings;
	}
	public EditsManager getEditsManager() {
		return undo;
	}
	public Connection getConnection() {
		return db.getConnection();
	}
}
