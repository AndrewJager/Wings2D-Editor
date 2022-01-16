package com.wings2d.editor.ui.skeleton.filterEdits;

import java.awt.Frame;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.wings2d.framework.imageFilters.FilterEdit;
import com.wings2d.framework.imageFilters.ImageFilter;

public class FilterEditRunner {
	// I am both proud and ashamed of this class
	
	public static ImageFilter runCreateDialog(final Class<? extends ImageFilter> filterClass, final Frame owner)
	{
		ImageFilter result = null;
		try {
			Class<? extends FilterEdit<? extends ImageFilter>> editClass = getEditClass(filterClass);

			Constructor<? extends FilterEdit<? extends ImageFilter>> con = editClass.getConstructor(Frame.class);
			FilterEdit<? extends ImageFilter> edit = con.newInstance(owner);
			result = edit.showDialog();

		} catch (NoSuchMethodException  | InstantiationException | IllegalAccessException
				| InvocationTargetException e) {
			e.printStackTrace();
		} 
		return result;
	}
	
	public static ImageFilter runEditDialog(final ImageFilter filter, final Frame owner) {
		ImageFilter result = null;
		try {
			Class<? extends FilterEdit<? extends ImageFilter>> editClass = getEditClass(filter.getClass());

			Constructor<? extends FilterEdit<? extends ImageFilter>> con = editClass.getConstructor(filter.getClass(), Frame.class);
			FilterEdit<? extends ImageFilter> edit = con.newInstance(filter, owner);
			result = edit.showDialog();

		} catch (NoSuchMethodException  | InstantiationException | IllegalAccessException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private static Class<? extends FilterEdit<? extends ImageFilter>> getEditClass(final Class<? extends ImageFilter> filterClass) {
		// This would be much cleaner if Java allowed us to create an abstract static method in an interface
		Method editGetter = null;
		try {
			editGetter = filterClass.getMethod("getEditClass", new Class[0]);
		}
		catch (NoSuchMethodException e) {
			throw new RuntimeException(filterClass.getSimpleName() + " does not have a getEditClass method!");
		}
		
		Class<? extends FilterEdit<? extends ImageFilter>> editClass = null;
		try {
			editClass = (Class<? extends FilterEdit<? extends ImageFilter>>)editGetter.invoke(null);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		if (editClass == null) {
			throw new RuntimeException("Edit class returned by GetEditClass is null!");
		}
		return editClass;
	}
}
