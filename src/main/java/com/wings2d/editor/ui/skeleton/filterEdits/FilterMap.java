package com.wings2d.editor.ui.skeleton.filterEdits;

import java.awt.Frame;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.wings2d.framework.imageFilters.BasicVariance;
import com.wings2d.framework.imageFilters.DarkenFrom;
import com.wings2d.framework.imageFilters.ImageFilter;
import com.wings2d.framework.imageFilters.LightenFrom;
/**
 * Contains a HashMap mapping the UI edit classes to their corresponding object classes
 */
public class FilterMap {
	public static Map<Class<? extends ImageFilter>, Class<? extends FilterEdit>> FILTER_MAP
			= new HashMap<Class<? extends ImageFilter>, Class<? extends FilterEdit>>()
			{
				private static final long serialVersionUID = 1L;

				{
					put(BasicVariance.class, BasicVarianceEdit.class);
					put(LightenFrom.class, LightenFromEdit.class);
					put(DarkenFrom.class, DarkenFromEdit.class);
				}
			};
			
	public static ImageFilter runCreateDialog(final Class<? extends ImageFilter> filterClass, final Frame owner)
	{
		ImageFilter result = null;
		try {
			Class<? extends FilterEdit> editClass = FILTER_MAP.get(filterClass);
			if (editClass != null)
			{
				Constructor<? extends FilterEdit> con = editClass.getConstructor(Frame.class);
				FilterEdit edit = con.newInstance(owner);
				result = edit.showDialog();
			}
			else
			{
				throw new RuntimeException("Class is not defined in FilterMap! Please add it to the list!");
			}
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException 
				| InvocationTargetException e) {
			e.printStackTrace();
		} 
		return result;
	}
	
	public static ImageFilter runEditDialog(final ImageFilter filter, final Frame owner) {
		ImageFilter result = null;
		try {
			Class<? extends FilterEdit> editClass = FILTER_MAP.get(filter.getClass());
			if (editClass != null)
			{
				Constructor<? extends FilterEdit> con = editClass.getConstructor(filter.getClass(), Frame.class);
				FilterEdit edit = con.newInstance(filter, owner);
				result = edit.showDialog();
			}
			else
			{
				throw new RuntimeException("Class is not defined in FilterMap! Please add it to the list!");
			}
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException 
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return result;
	}
}
