package editor.ui.filterEdits;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;

import framework.imageFilters.DarkenFrom;
import framework.imageFilters.ShadeDir;

public class ShadeFromEdit {
	private ShadeDir dir;
	private double amt;
	
	private JDialog dialog;
	private JButton okBtn;
	private JComboBox<ShadeDir> directionSelect;
	
	
	public ShadeFromEdit(DarkenFrom initDark)
	{
		dir = initDark.getDirection();
		amt = initDark.getAmt();
		ShadeDir[] directions = new ShadeDir[4];
		for (int i = 0; i < ShadeDir.values().length; i++) 
		{
			directions[i] = ShadeDir.values()[i];
		}
		
		dialog = new JDialog();
		dialog.setSize(300, 200);
		dialog.setLocationRelativeTo(null);
		
		directionSelect = new JComboBox<ShadeDir>(directions);
		directionSelect.setSelectedItem(dir);
		dialog.add(directionSelect);
		
		okBtn = new JButton("Ok");
	    okBtn.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		dir = (ShadeDir) directionSelect.getSelectedItem();
	    		
	    		dialog.setVisible(false);
	    		dialog.dispose();
	    	}
	    });
		dialog.add(okBtn);
		
		
		dialog.setLayout(new FlowLayout());
		dialog.setModal(true);
		dialog.setVisible(true);
	}

	public DarkenFrom getDarken()
	{
		return new DarkenFrom(dir, amt);
	}
}
