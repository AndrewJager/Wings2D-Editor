package editor.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import editor.ui.sprite.AnimationLists;

public class AnimTimer {
	private Timer timer;
	
	public AnimTimer(Editor editor)
	{
		AnimationLists ani = editor.getSpriteEdit().getAnimLists();
		ActionListener renderUpdater = new ActionListener() { // Timer event to update the render
			public void actionPerformed(ActionEvent evt) {
	    		if (ani.getIsFrameSelected())
	    		{
	    			editor.getSpriteEdit().getRender().updateRender(editor.getPlaying());
	    			editor.getSpriteEdit().getDrawing().updateDrawing();
	    		}
			}
		};
		timer = new Timer(editor.getTimeStep(), renderUpdater);
		timer.start();
	}

	public Timer getTimer()
	{
		return timer;
	}
}
