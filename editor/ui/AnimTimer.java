package editor.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class AnimTimer {
	private Timer timer;
	
	public AnimTimer(Editor editor)
	{
		AnimationLists ani = editor.getAnimLists();
		ActionListener renderUpdater = new ActionListener() { // Timer event to update the render
			public void actionPerformed(ActionEvent evt) {
	    		if (ani.getIsFrameSelected())
	    		{
	    			editor.getRender().updateRender(editor.getPlaying());
	    			editor.getDrawing().updateDrawing();
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
