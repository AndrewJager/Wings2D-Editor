package editor.ui.sprite;

import java.awt.Rectangle;

import editor.ui.AnimTimer;
import editor.ui.Editor;
import editor.ui.UIPanel;

public class SpriteEdit extends UIPanel{
	private DrawingArea drawing;
	private RenderArea render;
	private FilePanel file;
	private AnimationLists animLists;
	private EditOptionsPanel editOptionsPanel;
	private RenderControls renderControls;
	private ObjectInfo objectInfo;
	private FilterEdit filters;
	private AnimationInfo animInfo;
	private FrameInfo frameInfo;

	public SpriteEdit(Editor edit) {
		super(edit);
		
		drawing = new DrawingArea(edit, new Rectangle(875, 10, 600, 600));
		render = new RenderArea(edit, new Rectangle(700, 10, 150, 150));
		file = new FilePanel(edit, new Rectangle(10, 10, 400, 50));
		animLists = new AnimationLists(edit, new Rectangle(10, 70, 470, 600));
		editOptionsPanel = new EditOptionsPanel(edit, new Rectangle(500, 240, 350, 180));
		renderControls = new RenderControls(edit, new Rectangle(700, 170, 150, 35));
		objectInfo = new ObjectInfo(edit, new Rectangle(500, 10, 180, 195));
		filters = new FilterEdit(edit, new Rectangle(500, 540, 350, 210));
		animInfo = new AnimationInfo(edit, new Rectangle(10, 680, 150, 50));
		frameInfo = new FrameInfo(edit, new Rectangle(180, 680, 300, 50));		
		
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
	public FrameInfo getFrameInfo() {
		return frameInfo;
	}
	public AnimationInfo getAnimationInfo() {
		return animInfo;
	}
}
