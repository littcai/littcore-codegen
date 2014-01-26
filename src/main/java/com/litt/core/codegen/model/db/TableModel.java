/**
 * 
 */
package com.litt.core.codegen.model.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 *
 */
public class TableModel implements Serializable {
	
	private String tableName;
	
	private List<ColumnModel> columnList = new ArrayList<ColumnModel>();	

	public TableModel(String tableName) {
		super();
		this.tableName = tableName;
	}
	
	
	
	public void addColumn(ColumnModel column)
	{
		columnList.add(column);
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return the columnList
	 */
	public List<ColumnModel> getColumnList() {
		return columnList;
	}

	/**
	 * @param columnList the columnList to set
	 */
	public void setColumnList(List<ColumnModel> columnList) {
		this.columnList = columnList;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TableModel [tableName=").append(tableName)
				.append(", columnList=").append(columnList).append("]");
		return builder.toString();
	}
	
	

}
