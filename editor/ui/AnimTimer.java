package editor.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import editor.objects.Frame;

public class AnimTimer {
	private Timer timer;
	
	public AnimTimer(AnimationLists animLists, Editor editor)
	{
		ActionListener renderUpdater = new ActionListener() { // Timer event to update the render
			public void actionPerformed(ActionEvent evt) {
	    		if (animLists.getAnimList().getSelectedIndex() != -1 && animLists.getFrameList().getSelectedIndex() != -1)
	    		{
	    			Frame curFrame = editor.getActiveSprite().getAnimation(animLists.getAnimList().getSelectedIndex()).getFrame(animLists.getFrameList().getSelectedIndex());
	    			editor.updateRender(animLists.getFrameList(), curFrame, editor.getRender().getPanel(), editor.getPlaying());
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
