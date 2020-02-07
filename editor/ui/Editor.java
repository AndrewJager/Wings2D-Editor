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
	private static int TIME_STEP = 10;
	private boolean playing = false;
	private static boolean shouldReRender = false;
	private static Level demoLevel;
	private Options options;

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
		
		JFrame frame = new JFrame("Editor");
		
		DrawingArea drawing = new DrawingArea();
		RenderArea render = new RenderArea();
		FilePanel file = new FilePanel();
		AnimationLists animLists = new AnimationLists();
		EditOptions editOptions = new EditOptions(this);
		RenderControls renderControls = new RenderControls();
		ObjectInfo objectInfo = new ObjectInfo();
		FilterEdit filters = new FilterEdit();
		
		// Events
	    file.createEvents(animLists.getAnimList(), animLists.getFrameList(), render.getPanel(), frame, this);
	    animLists.createEvents(editOptions, drawing, frame, this);
	    editOptions.createEvents(animLists, drawing, this);
	    drawing.createEvents(animLists.getAnimList(), animLists.getFrameList(), animLists.getObjectList(), this);
	    renderControls.createEvents(this);
	    
	    
		ActionListener renderUpdater = new ActionListener() { // Timer event to update the render
			public void actionPerformed(ActionEvent evt) {
	    		if (animLists.getAnimList().getSelectedIndex() != -1 && animLists.getFrameList().getSelectedIndex() != -1)
	    		{
	    			Frame curFrame = activeSprite.getAnimation(animLists.getAnimList().getSelectedIndex()).getFrame(animLists.getFrameList().getSelectedIndex());
	    			updateRender(animLists.getFrameList(), curFrame, render.getPanel(), playing);
	    		}
			}
		};
		Timer timer = new Timer(TIME_STEP, renderUpdater);
		timer.start();

	    frame.addWindowListener(new WindowAdapter(){
	    	public void windowClosing(WindowEvent e){
	    		timer.stop();
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

	public static void updateRender(JList<String> frameList, Frame curFrame, JPanel render, boolean advanceFrame)
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
		curFrame.setTimePassed(curFrame.getTimePassed() + TIME_STEP);
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
	public static void updateDrawing(Frame curFrame, JPanel panel, int curObject)
	{
		Graphics2D g2d = (Graphics2D)panel.getGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, 600, 600);
		List<Item> objects = curFrame.getObjects();
		for (int i = 0; i < objects.size(); i++)
		{
			if (objects.get(i).getPath() != null)
			{
				GeneralPath path = objects.get(i).getPath();
				
				if (i == curObject)
				{
					g2d.setColor(objects.get(i).getColor());
				}
				else
				{
					g2d.setColor(objects.get(i).getFadedColor());
				}
				g2d.fill(path);
				g2d.setColor(Color.RED);
		    	if (i == curObject)
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
	
	public static void setFilterButtons(JPanel panel, JFrame frame, Item curObject, List<ImageFilter> filters)
	{
		panel.removeAll();
		for (int i = 0; i < filters.size(); i++)
		{
			JPanel btnPanel = new JPanel();
			btnPanel.setSize(100, 40);
			btnPanel.setBackground(Color.WHITE);
			JLabel number = new JLabel(Integer.toString(i));
			btnPanel.add(number);
			JLabel name = new JLabel(filters.get(i).getFilterName());
			btnPanel.add(name);
			JButton edit = new JButton("Edit");
			btnPanel.add(edit);
			JButton moveUp = new JButton("∧");
			btnPanel.add(moveUp);
			JButton moveDown = new JButton("∨");
			btnPanel.add(moveDown);
			btnPanel.setLayout(new FlowLayout());
			panel.add(btnPanel);
			
		    edit.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		JLabel num = (JLabel)moveUp.getParent().getComponent(0);
		    		int i = Integer.parseInt(num.getText());
		    		ImageFilter filter = filters.get(i);
		    		String filterName = filter.getFilterName();
		    		if (filterName == "Basic Variance")
		    		{
		    			BasicVariance variance = (BasicVariance)filters.get(i);
			    		String varAmount = (String)JOptionPane.showInputDialog(frame, "","Variance amount",
			    				JOptionPane.PLAIN_MESSAGE, null, null, variance.getVarAmt());
			    		int varAsInt;
			    		try {
			    		   varAsInt = Integer.parseInt(varAmount);
			    		}
			    		catch (NumberFormatException ex)
			    		{
			    		   varAsInt = 1;
			    		   JOptionPane.showMessageDialog(frame,
			    				    ex.getMessage(), "Invalid input", JOptionPane.WARNING_MESSAGE);
			    		}
			    		filters.set(i, new BasicVariance(varAsInt));
		    		}
		    		else if (filterName == "Blur Edges")
		    		{
			    		   JOptionPane.showMessageDialog(frame,
			    				    "Nothing to change for this filter", "Go home", JOptionPane.PLAIN_MESSAGE);
		    		}
		    		else if (filterName == "Darken From")
		    		{
		    			DarkenFrom darken = (DarkenFrom)filters.get(i);
			    		Object[] directions = {"Left", "Right", "Top", "Bottom"};
			    		String filterDir = (String)JOptionPane.showInputDialog(frame, "","Choose Direction",
			    				JOptionPane.PLAIN_MESSAGE, null, directions, ShadeDir.getAsString(darken.getDirection()));
			    		String varAmount = (String)JOptionPane.showInputDialog(frame, "","Variance amount",
			    				JOptionPane.PLAIN_MESSAGE, null, null, darken.getAmt());
			    		int varAsInt;
			    		try {
			    		   varAsInt = Integer.parseInt(varAmount);
			    		}
			    		catch (NumberFormatException ex)
			    		{
			    		   varAsInt = 1;
			    		   JOptionPane.showMessageDialog(frame,
			    				    ex.getMessage(), "Invalid input", JOptionPane.WARNING_MESSAGE);
			    		}
			    		filters.set(i, new DarkenFrom(ShadeDir.createFromString(filterDir), varAsInt));
		    		}
		    		else if (filterName == "Lighten From")
		    		{
		    			LightenFrom lighten = (LightenFrom)filters.get(i);
			    		Object[] directions = {"Left", "Right", "Top", "Bottom"};
			    		String filterDir = (String)JOptionPane.showInputDialog(frame, "","Choose Direction",
			    				JOptionPane.PLAIN_MESSAGE, null, directions, ShadeDir.getAsString(lighten.getDirection()));
			    		String varAmount = (String)JOptionPane.showInputDialog(frame, "","Amount",
			    				JOptionPane.PLAIN_MESSAGE, null, null, lighten.getAmt());
			    		int varAsInt;
			    		try {
			    		   varAsInt = Integer.parseInt(varAmount);
			    		}
			    		catch (NumberFormatException ex)
			    		{
			    		   varAsInt = 1;
			    		   JOptionPane.showMessageDialog(frame,
			    				    ex.getMessage(), "Invalid input", JOptionPane.WARNING_MESSAGE);
			    		}
			    		filters.set(i, new LightenFrom(ShadeDir.createFromString(filterDir), varAsInt));
		    		}
		    		else if (filterName == "Outline")
		    		{
		    			Outline outline = (Outline)filters.get(i);
		    			Color color = JColorChooser.showDialog(frame, "Select a color", outline.getColor());
		    			filters.set(i, new Outline(color));
		    		}
		    	}
		    });
		    moveUp.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		JLabel num = (JLabel)moveUp.getParent().getComponent(0);
		    		int i = Integer.parseInt(num.getText());
		    		curObject.swapFilters(i,  i - 1);
		    		setFilterButtons(panel, frame, curObject, filters);
		    	}
		    });
		    moveDown.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		JLabel num = (JLabel)moveUp.getParent().getComponent(0);
		    		int i = Integer.parseInt(num.getText());
		    		curObject.swapFilters(i,  i + 1);
		    		setFilterButtons(panel, frame, curObject, filters);
		    	}
		    });
		}

		frame.validate();
	}

	public SpriteSheet getActiveSprite() {
		return activeSprite;
	}

	public void setActiveSprite(SpriteSheet activeSprite) {
		this.activeSprite = activeSprite;
	}

	public boolean getShouldReRender() {
		return shouldReRender;
	}

	public void setShouldReRender(boolean shouldReRender) {
		this.shouldReRender = shouldReRender;
	}

	public Options getOptions() {
		return options;
	}

	public void setOptions(Options options) {
		this.options = options;
	}

	public LevelManager getManager() {
		return manager;
	}

	public void setManager(LevelManager manager) {
		this.manager = manager;
	}

	public static int getTIME_STEP() {
		return TIME_STEP;
	}

	public static void setTIME_STEP(int tIME_STEP) {
		TIME_STEP = tIME_STEP;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}
}
