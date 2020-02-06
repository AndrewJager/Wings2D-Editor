package editor;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import framework.Level;
import framework.LevelManager;
import framework.imageFilters.BasicVariance;
import framework.imageFilters.BlurEdges;
import framework.imageFilters.DarkenFrom;
import framework.imageFilters.ImageFilter;
import framework.imageFilters.LightenFrom;
import framework.imageFilters.Outline;
import framework.imageFilters.ShadeDir;

public class Editor {
	private static SpriteSheet activeSprite;
	private static LevelManager manager;
	private static Level demoLevel;
	private static int TIME_STEP = 10;
	private static boolean playing = false;
	private static boolean shouldReRender = false;

	public static void main(String[] args) {
		activeSprite = new SpriteSheet();
		manager = new LevelManager();
		demoLevel = new Level(manager, 0);
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		
		JFrame frame = new JFrame("Editor");
		
		JPanel drawing = new JPanel();
		drawing.setBounds(875, 10, 600, 600);
		drawing.setBackground(Color.WHITE);
		
		JPanel filePanel = new JPanel();
		filePanel.setBounds(10, 10, 400, 50);
		filePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JLabel fileLabel = new JLabel("File:");
		filePanel.add(fileLabel);
		JLabel fileName = new JLabel("None");
		filePanel.add(fileName);
		JButton selectButton = new JButton("Select"); 
		filePanel.add(selectButton);
		JButton newButton = new JButton("New");
	    filePanel.add(newButton);
	    JButton render = new JButton("Render");
	    filePanel.add(render);
		filePanel.setLayout(new FlowLayout());

		JPanel animsPanel = new JPanel();
		animsPanel.setBounds(10, 70, 150, 600);
		animsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JLabel animsLabel = new JLabel("Animations:");
		animsPanel.add(animsLabel);
		JList<String> animList = new JList<String>();
		JScrollPane scrolls = new JScrollPane(animList);  
		animsPanel.add(scrolls);
		JButton newAnim = new JButton("New Animation");
		animsPanel.add(newAnim);
		animsPanel.setLayout(new BoxLayout(animsPanel, BoxLayout.PAGE_AXIS));
		
		JPanel framesPanel = new JPanel();
		framesPanel.setBounds(170, 70, 150, 600);
		framesPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JLabel framesLabel = new JLabel("Frames:");
		framesPanel.add(framesLabel);
		JList<String> frameList = new JList<String>();
		JScrollPane frameScroll = new JScrollPane(frameList);  
		framesPanel.add(frameScroll);
		JButton newFrame = new JButton("New Frame");
		framesPanel.add(newFrame);
		framesPanel.setLayout(new BoxLayout(framesPanel, BoxLayout.PAGE_AXIS));
		
		JPanel objectsPanel = new JPanel();
		objectsPanel.setBounds(330, 70, 150, 600);
		objectsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JLabel objectsLabel = new JLabel("Objects:");
		objectsPanel.add(objectsLabel);
		JList<String> objectList = new JList<String>();
		JScrollPane objectsScroll = new JScrollPane(objectList);
		objectsPanel.add(objectsScroll);
		JButton newObject = new JButton("New Object");
		objectsPanel.add(newObject);
		objectsPanel.setLayout(new BoxLayout(objectsPanel, BoxLayout.PAGE_AXIS));
		
		JPanel renderArea = new JPanel();
		renderArea.setBounds(700, 10, 150, 150);
		renderArea.setBackground(Color.WHITE);
		
		JPanel renderControls = new JPanel();
		renderControls.setBounds(700, 170, 150, 35);
		renderControls.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JButton play = new JButton("Play");
		renderControls.add(play);
		JButton pause = new JButton("Pause");
		renderControls.add(pause);
		JLabel currentFrame = new JLabel("0");
		renderControls.add(currentFrame);
		renderControls.setLayout(new FlowLayout());
		
		JPanel objectInfo = new JPanel();
		objectInfo.setBounds(500, 430, 350, 100);
		objectInfo.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JLabel infoLabel = new JLabel("Object Info");
		objectInfo.add(infoLabel);
		JButton addVertex = new JButton("Add vertix");
		objectInfo.add(addVertex);
		JButton setColor = new JButton("Change color");
		objectInfo.add(setColor);
		JButton addFilter = new JButton("Add filter");
		objectInfo.add(addFilter);
		JLabel xOffset = new JLabel("0");
		objectInfo.add(xOffset);
		JLabel yOffset = new JLabel("0");
		objectInfo.add(yOffset);
		JLabel rotation = new JLabel("0");
		objectInfo.add(rotation);
		objectInfo.setLayout(new BoxLayout(objectInfo, BoxLayout.PAGE_AXIS));
		
		JPanel editOptions = new JPanel();
		editOptions.setBounds(500, 240, 350, 180);
		editOptions.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JCheckBox cascade = new JCheckBox("Cascade transforms");
		cascade.setSelected(true);
		editOptions.add(cascade);
		SpinnerModel spinModel = new SpinnerNumberModel(0.25, 0.001, 2, 0.05);
		JSpinner imgScale = new JSpinner(spinModel);
		manager.setScale((double) imgScale.getValue());
		JCheckBox editing = new JCheckBox("Editing");
		editOptions.add(editing);
		JCheckBox rotate = new JCheckBox("Rotation Handle");
		editOptions.add(rotate);
		JCheckBox scale = new JCheckBox("Scale Handles");
		editOptions.add(scale);
		editOptions.add(imgScale);
		editOptions.setLayout(new GridLayout(3, 2));
		
		JPanel filterPanel = new JPanel();
		filterPanel.setBounds(500, 540, 350, 210);
		filterPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		filterPanel.setLayout(new FlowLayout());
		
		// Events
	    selectButton.addActionListener(new ActionListener(){  
	    	public void actionPerformed(ActionEvent e){  
	    		JFileChooser file = new JFileChooser("test");
	    		int result = file.showSaveDialog(frame);
	    		if (result == JFileChooser.APPROVE_OPTION) 
	    		{
	    			 fileName.setText(file.getSelectedFile().getName());
	    		} else if (result == JFileChooser.CANCEL_OPTION) 
	    		{
	    		    System.out.println("Cancel was selected");
	    		}
	        }  
	    });
	    newButton.addActionListener(new ActionListener(){  
	    	public void actionPerformed(ActionEvent e){  
				activeSprite = new SpriteSheet();
	        }  
	    }); 
	    newAnim.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		String animName = (String)JOptionPane.showInputDialog(frame, "","Add animation",
	    				JOptionPane.PLAIN_MESSAGE, null, null, "Animation");
	    		activeSprite.addAnimation(animName);
	    		animList.setListData(activeSprite.getAnimNames());
	    		animList.setSelectedIndex(activeSprite.getAnimations().size() - 1);
	    	}
	    });
	    newFrame.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (animList.getSelectedIndex() != -1)
	    		{
		    		Animation curAnim = activeSprite.getAnimation(animList.getSelectedIndex());
		    		String frameName = curAnim.getName()
		    				+ "_" + frameList.getModel().getSize();
		    		if (frameList.getModel().getSize() <= 0)
		    		{
		    			curAnim.addFrame(frameName, true);
		    		}
		    		else
		    		{
		    			curAnim.addFrame(frameName, curAnim.getFrame(frameList.getModel().getSize() - 1));
		    		}
		    		frameList.setListData(curAnim.getFrameNames());
		    		frameList.setSelectedIndex(curAnim.getFrames().size() - 1);
	    		}
	    	}
	    });
	    newObject.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		String objectName = (String)JOptionPane.showInputDialog(frame, "","Add object",
	    				JOptionPane.PLAIN_MESSAGE, null, null, "Object");
	    		Frame curFrame = activeSprite.getAnimation(animList.getSelectedIndex()).getFrame(frameList.getSelectedIndex());
	    		curFrame.addObject(objectName);
	    		objectList.setListData(curFrame.getObjectNames());
	    		objectList.setSelectedIndex(curFrame.getObjects().size() - 1);
	    		shouldReRender = true;
	    	}
	    });
        objectList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                if (!evt.getValueIsAdjusting()) {
                	editing.setSelected(false);
        			updateDrawing(activeSprite.getAnimation(animList.getSelectedIndex()).getFrame(frameList.getSelectedIndex()), drawing,
        					editing.isSelected(), rotate.isSelected(), scale.isSelected(), objectList.getSelectedIndex());
                }
            }
        });
        frameList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                if (!evt.getValueIsAdjusting()) {
                	editing.setSelected(false);
                	if (animList.getSelectedIndex() != -1 && frameList.getSelectedIndex() != -1)
                	{
	                	Frame curFrame = activeSprite.getAnimation(animList.getSelectedIndex()).getFrame(frameList.getSelectedIndex());
	                	if (curFrame.getIsMaster())
	                	{
	                		editing.setEnabled(true);
	                	}
	                	else
	                	{
	                		editing.setEnabled(false);
	                	}
	                	objectList.setListData(curFrame.getObjectNames());
	        			updateDrawing(activeSprite.getAnimation(animList.getSelectedIndex()).getFrame(frameList.getSelectedIndex()), drawing,
	        					editing.isSelected(), rotate.isSelected(), scale.isSelected(), objectList.getSelectedIndex());
                	}
                }
            }
        });
	    drawing.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
            	if (frameList.getSelectedIndex() != -1 && animList.getSelectedIndex() != -1 && objectList.getSelectedIndex() != -1)
            	{
            		Frame curFrame = activeSprite.getAnimation(animList.getSelectedIndex()).getFrame(frameList.getSelectedIndex());
                	curFrame.processMousePress(editing.isSelected(), objectList.getSelectedIndex(), evt.getPoint());
            	}
                
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
            	if (animList.getSelectedIndex() != -1 && frameList.getSelectedIndex() != -1 && objectList.getSelectedIndex() != -1)
            	{
            		Frame curFrame = activeSprite.getAnimation(animList.getSelectedIndex()).getFrame(frameList.getSelectedIndex());
                	curFrame.processMouseRelease(editing.isSelected(), cascade.isSelected(), objectList.getSelectedIndex(), evt.getPoint());
    				updateDrawing(activeSprite.getAnimation(animList.getSelectedIndex()).getFrame(frameList.getSelectedIndex()), drawing,
    						editing.isSelected(), rotate.isSelected(), scale.isSelected(), objectList.getSelectedIndex());
    				shouldReRender = true;
            	}
            }
        });
	    editing.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (animList.getSelectedIndex() != -1 && frameList.getSelectedIndex() != -1 && objectList.getSelectedIndex() != -1)
	    		{
	    			updateDrawing(activeSprite.getAnimation(animList.getSelectedIndex()).getFrame(frameList.getSelectedIndex()), drawing,
	    					editing.isSelected(), rotate.isSelected(), scale.isSelected(), objectList.getSelectedIndex());
	    		}
	    	}
	    });
	    addVertex.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (animList.getSelectedIndex() != -1 && frameList.getSelectedIndex() != -1 && objectList.getSelectedIndex() != -1)
	    		{
	    			Frame curFrame = activeSprite.getAnimation(animList.getSelectedIndex()).getFrame(frameList.getSelectedIndex());
	    			curFrame.addVertex(objectList.getSelectedIndex());
	    			updateDrawing(activeSprite.getAnimation(animList.getSelectedIndex()).getFrame(frameList.getSelectedIndex()), drawing,
	    					editing.isSelected(), rotate.isSelected(), scale.isSelected(), objectList.getSelectedIndex());
	    			shouldReRender = true;
	    		}
	    	}
	    });
	    setColor.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (animList.getSelectedIndex() != -1 && frameList.getSelectedIndex() != -1 && objectList.getSelectedIndex() != -1)
	    		{
	    			Frame curFrame = activeSprite.getAnimation(animList.getSelectedIndex()).getFrame(frameList.getSelectedIndex());
	    			Color color = JColorChooser.showDialog(frame, "Select a color", curFrame.getObject(objectList.getSelectedIndex()).getColor());    
	    			curFrame.setColor(objectList.getSelectedIndex(), color); 
	    			updateDrawing(activeSprite.getAnimation(animList.getSelectedIndex()).getFrame(frameList.getSelectedIndex()), drawing,
	    					editing.isSelected(), rotate.isSelected(), scale.isSelected(), objectList.getSelectedIndex());
	    			shouldReRender = true;
	    		}
	    	}
	    });
        imgScale.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
            	manager.setScale((double) imgScale.getValue());
            }
        });
	    render.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (animList.getSelectedIndex() != -1 && frameList.getSelectedIndex() != -1)
	    		{
	    			Frame curFrame = activeSprite.getAnimation(animList.getSelectedIndex()).getFrame(frameList.getSelectedIndex());
	    			shouldReRender = true;
	    			updateRender(frameList, curFrame, renderArea, false);
	    		}
	    	}
	    });
	    addFilter.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (animList.getSelectedIndex() != -1 && frameList.getSelectedIndex() != -1 && objectList.getSelectedIndex() != -1)
	    		{
	    			Frame curFrame = activeSprite.getAnimation(animList.getSelectedIndex()).getFrame(frameList.getSelectedIndex());
	    			Item curObject = curFrame.getObject(objectList.getSelectedIndex());
		    		Object[] filters = {"Basic Variance", "Blur Edges", "Darken From", "Lighten From", "Outline"};
		    		String filterName = (String)JOptionPane.showInputDialog(frame, "","Choose Filter",
		    				JOptionPane.PLAIN_MESSAGE, null, filters, "Basic Variance");
		    		curObject.addNewFilter(filterName);
		    		
		    		setFilterButtons(filterPanel, frame, curObject, curObject.getFilters());
		    		shouldReRender = true;
	    		}
	    	}
	    });
		ActionListener renderUpdater = new ActionListener() { // Timer event to update the render
			public void actionPerformed(ActionEvent evt) {
	    		if (animList.getSelectedIndex() != -1 && frameList.getSelectedIndex() != -1)
	    		{
	    			Frame curFrame = activeSprite.getAnimation(animList.getSelectedIndex()).getFrame(frameList.getSelectedIndex());
	    			updateRender(frameList, curFrame, renderArea, playing);
	    		}
			}
		};
		Timer timer = new Timer(TIME_STEP, renderUpdater);
		timer.start();
	    play.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		playing = true;
	    	}
	    });
	    play.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		playing = true;
	    	}
	    });
	    pause.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		playing = false;
	    	}
	    });
	    frame.addWindowListener(new WindowAdapter(){
	    	public void windowClosing(WindowEvent e){
	    		timer.stop();
	    		System.exit(0);
	    	}
	    });


	    
	    
		frame.add(drawing);
		frame.add(filePanel);
		frame.add(animsPanel);
		frame.add(framesPanel);
		frame.add(objectsPanel);
		frame.add(objectInfo);
		frame.add(editOptions);
		frame.add(renderControls);
		frame.add(renderArea);
		frame.add(filterPanel);
		
		frame.setSize(1500,800);
		frame.setLocationRelativeTo(null);
		frame.setLayout(null); 
		frame.setVisible(true);
	}

	private static void updateRender(JList<String> frameList, Frame curFrame, JPanel render, boolean advanceFrame)
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
	private static void updateDrawing(Frame curFrame, JPanel panel, Boolean editing, Boolean rotating, Boolean scaling, int curObject)
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
					if (editing)
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
					
					if (rotating)
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
	
	private static void setFilterButtons(JPanel panel, JFrame frame, Item curObject, List<ImageFilter> filters)
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
}
