package editor.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import editor.objects.Options;
import editor.objects.SpriteSheet;
import editor.objects.Frame;
import editor.objects.Item;
import framework.Level;
import framework.LevelManager;
import framework.imageFilters.BasicVariance;
import framework.imageFilters.DarkenFrom;
import framework.imageFilters.ImageFilter;
import framework.imageFilters.LightenFrom;
import framework.imageFilters.Outline;
import framework.imageFilters.ShadeDir;

public class Editor {
	private SpriteSheet activeSprite;
	private LevelManager manager;
	private int timeStep = 10;
	private boolean playing = false;
	private boolean shouldReRender = false;
	private Level demoLevel;
	private Options options;
	
	private JFrame frame;
	private DrawingArea drawing;
	private RenderArea render;
	private FilePanel file;
	private AnimationLists animLists;
	private EditOptions editOptions;
	private RenderControls renderControls;
	private ObjectInfo objectInfo;
	private FilterEdit filters;
	private AnimTimer animTimer;

	public void run() {
		activeSprite = new SpriteSheet();
		manager = new LevelManager();
		demoLevel = new Level(manager, 0);
		options = new Options();
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		
		frame = new JFrame("Editor");
		
		drawing = new DrawingArea(this);
		render = new RenderArea();
		file = new FilePanel();
		animLists = new AnimationLists(this);
		editOptions = new EditOptions(this);
		renderControls = new RenderControls();
		objectInfo = new ObjectInfo();
		filters = new FilterEdit();
		animTimer = new AnimTimer(animLists, this);
		
		// Events
	    file.createEvents(this);
	    animLists.createEvents();
	    editOptions.createEvents(this);
	    drawing.createEvents();
	    renderControls.createEvents(this);
	    objectInfo.createEvents(animLists, drawing, frame, filters, this);
		
	    frame.addWindowListener(new WindowAdapter(){
	    	public void windowClosing(WindowEvent e){
	    		animTimer.getTimer().stop();
	    		System.exit(0);
	    	}
	    });
	    


	    
	    
		frame.add(drawing.getPanel());
		frame.add(file.getPanel());
		frame.add(animLists.getAnimPanel());
		frame.add(animLists.getFramePanel());
		frame.add(animLists.getObjectPanel());
		frame.add(objectInfo.getPanel());
		frame.add(editOptions.getPanel());
		frame.add(renderControls.getPanel());
		frame.add(render.getPanel());
		frame.add(filters.getPanel());
		
		frame.setSize(1500,800);
		frame.setLocationRelativeTo(null);
		frame.setLayout(null); 
		frame.setVisible(true);
	}

	public void updateRender(JList<String> frameList, Frame curFrame, JPanel render, boolean advanceFrame)
	{
		if (shouldReRender)
		{
			Graphics2D g2d = (Graphics2D)render.getGraphics();
			g2d.setColor(Color.WHITE);
			g2d.fillRect(0, 0, 150, 150);
			List<Item> objects = curFrame.getObjects();
			for (int i = 0; i < objects.size(); i++)
			{
				objects.get(i).makeImage(demoLevel);
				objects.get(i).getImage().render(g2d, false);
			}
			shouldReRender = false;
		}
		
		if (advanceFrame)
		{
		curFrame.setTimePassed(curFrame.getTimePassed() + timeStep);
			if (curFrame.getTimePassed() >= curFrame.getFrameTime())
			{
				curFrame.setTimePassed(0);
				// Go to next frame
				if (frameList.getSelectedIndex() >= frameList.getModel().getSize() - 1)
				{
					frameList.setSelectedIndex(0);
				}
				else
				{
					frameList.setSelectedIndex(frameList.getSelectedIndex() + 1);
				}
				shouldReRender = true;
			}
		}
	}
	public void updateDrawing(AnimationLists ani)
	{
		Graphics2D g2d = (Graphics2D)drawing.getPanel().getGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, 600, 600);
		Frame curFrame = ani.getSelectedFrame();
		int objIndex = ani.getObjectList().getSelectedIndex();
		List<Item> objects = curFrame.getObjects();
		for (int i = 0; i < objects.size(); i++)
		{
			if (objects.get(i).getPath() != null)
			{
				GeneralPath path = objects.get(i).getPath();
				
				if (i == objIndex)
				{
					g2d.setColor(objects.get(i).getColor());
				}
				else
				{
					g2d.setColor(objects.get(i).getFadedColor());
				}
				g2d.fill(path);
				g2d.setColor(Color.RED);
		    	if (i == objIndex)
		    	{
					if (curFrame.getOptions().getEditing())
					{    
					    for (int j = 0; j < objects.get(i).getPoints().size(); j++)
					    {
					    	drawHandle(objects.get(i).getPoints().get(j).getX(), objects.get(i).getPoints().get(j).getY(), g2d);
					    }
					}
					else
					{
						drawHandle(path.getBounds2D().getCenterX(), path.getBounds2D().getCenterY(), g2d);
					}
					
					if (curFrame.getOptions().getRotating())
					{
						drawHandle(path.getBounds2D().getCenterX(), path.getBounds2D().getY(), g2d);
					}
		    	}
			}
		}
	}
	private static void drawHandle(double x, double y, Graphics2D g2d)
	{
		Ellipse2D circle = new Ellipse2D.Double(x - 6, y - 6, 12, 12);
		g2d.draw(circle);
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
	public Options getOptions() {
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
	public EditOptions getEditOptions() {
		return editOptions;
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
