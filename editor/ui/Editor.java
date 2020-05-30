package editor.ui;

import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import framework.Level;
import framework.LevelManager;
import framework.animation.Frame;
import framework.animation.SpriteSheet;

public class Editor {
	private SpriteSheet activeSprite;
	private LevelManager manager;
	private int timeStep = 10;
	private boolean playing = false;
	private boolean shouldReRender = false;
	private Level demoLevel;
	private Frame.EditOptions options;
	private List<UIElement> elements;
	
	private JFrame frame;
	private DrawingArea drawing;
	private RenderArea render;
	private FilePanel file;
	private AnimationLists animLists;
	private EditOptionsPanel editOptionsPanel;
	private RenderControls renderControls;
	private ObjectInfo objectInfo;
	private FilterEdit filters;
	private AnimTimer animTimer;
	private AnimationInfo animInfo;
	private FrameInfo frameInfo;
	
	public int frameStartWidth = 1500;
	public int frameStartHeight = 800;

	public void run() {
		manager = new LevelManager();
		demoLevel = new Level(manager, 0);
		activeSprite = new SpriteSheet("Test", demoLevel);
		options = new Frame.EditOptions();
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		
		frame = new JFrame("Editor");
		
		drawing = new DrawingArea(this, new Rectangle(875, 10, 600, 600));
		render = new RenderArea(this, new Rectangle(700, 10, 150, 150));
		file = new FilePanel(this, new Rectangle(10, 10, 400, 50));
		animLists = new AnimationLists(this, new Rectangle(10, 70, 470, 600));
		editOptionsPanel = new EditOptionsPanel(this, new Rectangle(500, 240, 350, 180));
		renderControls = new RenderControls(this, new Rectangle(700, 170, 150, 35));
		objectInfo = new ObjectInfo(this, new Rectangle(500, 10, 180, 195));
		filters = new FilterEdit(this, new Rectangle(500, 540, 350, 210));
		animInfo = new AnimationInfo(this, new Rectangle(10, 680, 150, 50));
		frameInfo = new FrameInfo(this, new Rectangle(180, 680, 300, 50));
		
		animTimer = new AnimTimer(this);
		
		elements = new ArrayList<UIElement>();
		elements.add(drawing);
		elements.add(render);
		elements.add(file);
		elements.add(animLists);
		elements.add(editOptionsPanel);
		elements.add(renderControls);
		elements.add(objectInfo);
		elements.add(filters);
		elements.add(animInfo);
		elements.add(frameInfo);
		
		// Create events and add panels to frame
		for (int i = 0; i < elements.size(); i++)
		{
			elements.get(i).createEvents();
			frame.add(elements.get(i).getPanel());
		}
		
	    frame.addWindowListener(new WindowAdapter(){
	    	public void windowClosing(WindowEvent e){
	    		animTimer.getTimer().stop();
	    		System.exit(0);
	    	}
	    });	
		frame.addComponentListener(new ComponentAdapter() 
		{
			public void componentResized(ComponentEvent evt) {
				for (int i = 0; i < elements.size(); i++)
				{
					elements.get(i).resizePanel();
				}
			}
		});

		frame.setSize(frameStartWidth, frameStartHeight);
		frame.setLocationRelativeTo(null);
		frame.setLayout(null); 
		frame.setVisible(true);
	}

	public SpriteSheet getActiveSprite() {
		return activeSprite;
	}
	public LevelManager getManager() {
		return manager;
	}
	public int getTimeStep() {
		return timeStep;
	}
	public boolean getPlaying() {
		return playing;
	}
	public boolean getShouldReRender() {
		return shouldReRender;
	}
	public Level getDemoLevel() {
		return demoLevel;
	}
	public Frame.EditOptions getOptions() {
		return options;
	}
	public JFrame getFrame() {
		return frame;
	}
	public DrawingArea getDrawing() {
		return drawing;
	}
	public RenderArea getRender() {
		return render;
	}
	public FilePanel getFile() {
		return file;
	}
	public AnimationLists getAnimLists() {
		return animLists;
	}
	public EditOptionsPanel getEditOptions() {
		return editOptionsPanel;
	}
	public RenderControls getRenderControls() {
		return renderControls;
	}
	public ObjectInfo getObjectInfo() {
		return objectInfo;
	}
	public FilterEdit getFilters() {
		return filters;
	}

	public void setShouldReRender(Boolean render)
	{
		shouldReRender = render;
	}
	public void setActiveSprite(SpriteSheet sprite)
	{
		activeSprite = sprite;
	}
	public void setPlaying(Boolean play)
	{
		playing = play;
	}
}
