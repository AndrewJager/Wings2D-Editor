package com.wings2d.editor.objects.skeleton;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.swing.tree.MutableTreeNode;

import com.wings2d.editor.objects.save.DBString;
import com.wings2d.editor.ui.edits.ActionNotDoneException;

public abstract class SkeletonNode implements MutableTreeNode{
	protected String tableName;
	protected DBString name;
	protected DBString id;
	
	public SkeletonNode(final String tableName) {
		this.tableName = tableName;
	}
	
	public static void delete(final String id, final String tableName, final Connection con) {
		String sql = "DELETE FROM " + tableName + " WHERE ID = " + "'" + id +"'";
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setName(final String newName) {
		this.name.setValue(newName);
	}
	public String getName() {
		return name.getValue();
	}
	public String getID() {
		return id.getValue();
	}
	public String getTableName() {
		return tableName;
	}

	public abstract void resyncAll();
	public abstract void generateRender(final double scale);
	public abstract void moveUp() throws ActionNotDoneException;
	public abstract void moveDown() throws ActionNotDoneException;
	public abstract List<SkeletonNode> getNodes();
	public abstract boolean isMaster();
	
	public static final String NAME_TOKEN = "NAME";
	public static final String END_TOKEN = "END";
	public static final String ID_TOKEN = "ID";
	public static final String COLOR_TOKEN = "COLOR";
	
	public static final String MOVE_UP_ERROR = "Cannot move up when item is first in list!";
	public static final String MOVE_DOWN_ERROR = "Cannot move down when item is last in list!";
}
