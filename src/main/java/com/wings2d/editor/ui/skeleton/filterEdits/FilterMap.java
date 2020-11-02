package com.wings2d.editor.ui.skeleton.filterEdits;

import java.awt.Frame;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.wings2d.framework.imageFilters.BasicVariance;
import com.wings2d.framework.imageFilters.ImageFilter;

/**
 * Contains a HashMap mapping the UI edit classes to their corresponding object classes
 */
public class FilterMap {
	@SuppressWarnings("rawtypes")
	public static Map<Class<? extends ImageFilter>, Class<? extends FilterEdit>> FILTER_MAP
			= new HashMap<Class<? extends ImageFilter>, Class<? extends FilterEdit>>()
			{
				{
					put(BasicVariance.class, BasicVarianceEdit.class);
				}
			};
			
	@SuppressWarnings("rawtypes")
	public static ImageFilter runDialog(final Class<? extends ImageFilter> filterClass, final Frame owner)
	{
		ImageFilter result = null;
		try {
			Class<? extends FilterEdit> editClass = FILTER_MAP.get(filterClass);
			Constructor<? extends FilterEdit> con = editClass.getConstructor(Frame.class);
			FilterEdit edit = con.newInstance(owner);
			result = edit.showDialog();
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException 
				| InvocationTargetException e) {
			e.printStackTrace();
		} 
		return result;
	}
}
