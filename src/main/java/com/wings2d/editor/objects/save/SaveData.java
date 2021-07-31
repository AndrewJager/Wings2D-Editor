package com.wings2d.editor.objects.save;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SaveData {
	private List<Writable> data;
	final String split;
	
	public SaveData(final String split) {
		data = new ArrayList<Writable>();
		this.split = split;
	}
	
	public void parseFile(final Scanner in) throws RuntimeException{
		while (in.hasNext()) {
			String[] tokens = in.next().split(split);
			Writable item = getItemByToken(tokens[0]);
			if (item != null) {
				item.setFromToken(tokens[1]);
			}
			else {
				throw new RuntimeException("Token " + tokens[0] + " does not have a paired class!");
			}
		}
	}
	
	private Writable getItemByToken(final String token) {
		Writable item = null;
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).getToken().equals(token)) {
				
				item = data.get(i);
			}
		}
		return item;
	}
	
	public void saveData(final PrintWriter out) {
		out.print(""); // Clear the file
		for (int i = 0; i < data.size(); i++) {
			Writable dat = data.get(i);
			out.print(dat.getToken() + split + dat.getSaveData() + "\n");
		}
	}
	
	public WritableInt add(final WritableInt newData) {
		data.add(newData);
		return newData;
	}
	public WritableString add(final WritableString newData) {
		data.add(newData);
		return newData;
	}
	public WritableFile add(final WritableFile newData) {
		data.add(newData);
		return newData;
	}
}