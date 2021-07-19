package com.wings2d.editor.ui.edits;

import java.util.ArrayList;
import java.util.List;

import com.wings2d.editor.ui.skeleton.SkeletonTree;

public class EditsManager {
	private List<Edit> edits;
	private int curEdit;
	private SkeletonTree tree;
	
	public EditsManager(final SkeletonTree tree) {
		edits = new ArrayList<Edit>();
		curEdit = -1;
		this.tree = tree;
	}
	
	public void edit(final Edit edit) {
		if (edit.doEdit()) {
			edits.add(edit);
			curEdit = edits.size() - 1;
			tree.reloadModel();
		}
	}
	
	public void undo() {
		if (curEdit > -1) {
			edits.get(curEdit).undoEdit();
			curEdit--;
			tree.reloadModel();
		}
	}
	public void redo() {
		if ((curEdit + 1) < edits.size()) {
			edits.get(curEdit + 1).doEdit();
			curEdit++;
			tree.reloadModel();
		}
	}
}
