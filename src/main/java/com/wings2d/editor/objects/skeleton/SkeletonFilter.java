package com.wings2d.editor.objects.skeleton;

import java.sql.Connection;
import java.util.UUID;

import com.wings2d.editor.objects.save.DBInt;
import com.wings2d.editor.objects.save.DBString;
import com.wings2d.editor.objects.save.DBUUID;
import com.wings2d.editor.objects.skeleton.path.Sprite;
import com.wings2d.framework.imageFilters.FilterFactory;
import com.wings2d.framework.imageFilters.ImageFilter;

public class SkeletonFilter extends DBEditObject{
	public static final String TABLE_NAME = "FILTER"; 
	
	private ImageFilter filter;
	private Sprite parent;
	
	private DBUUID spriteID;
	private DBString filterType;
	private DBString filterData;
	private DBInt index;
	
	public static SkeletonFilter insert(final ImageFilter filter, final Sprite parent, final Connection con) {
		return new SkeletonFilter(filter, parent, con);
	}
	public static SkeletonFilter read(final UUID filterID, final Sprite parent, final Connection con) {
		return new SkeletonFilter(filterID, parent, con);
	}
	
	//Insert constructor
	private SkeletonFilter(final ImageFilter filter, final Sprite parent, final Connection con) {
		this(parent);
		this.filter = filter;
		spriteID.setStoredValue(parent.getID());
		filterType.setStoredValue(filter.getFilterName());
		filterData.setStoredValue(filter.getFileString());
		index.setStoredValue(0);
		
		this.insert(con);
		this.query(con, id.getStoredValue());
	}
	
	// Read constructor
	private SkeletonFilter(final UUID filterID, final Sprite parent, final Connection con) {
		this(parent);
		
		this.query(con, filterID);
		
		filter = FilterFactory.fromFileString(filterData.getStoredValue());
	}
	

	private SkeletonFilter(final Sprite parent) {
		super(TABLE_NAME, false);
		this.parent = parent;

		fields.add(spriteID = new DBUUID("Sprite"));
		fields.add(filterType = new DBString("FilterType"));
		fields.add(filterData = new DBString("Data"));
		fields.add(index = new DBInt("Position"));
	}
	
	public ImageFilter getFilter() {
		return filter;
	}
	public void setFilter(final ImageFilter filter) {
		this.filter = filter;
		filterType.setStoredValue(filter.getFilterName());
		filterData.setStoredValue(filter.getFileString());
		index.setStoredValue(0);
	}

	@Override
	protected void deleteChildren(Connection con) {}
	@Override
	protected void queryChildren(UUID id, Connection con) {}
	@Override
	protected void updateChildren(Connection con) {}

}
