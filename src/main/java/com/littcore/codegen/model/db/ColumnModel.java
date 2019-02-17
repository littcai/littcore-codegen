
package com.littcore.codegen.model.db;

import com.littcore.codegen.util.StringUtils;

/**
 * @author Administrator
 *
 */
public class ColumnModel {
	
	private String name;
	
	private String dataType;	
	
	private int dataLength;
	
	private boolean nullable;
	
	private String comment;

	public ColumnModel(String name, String dataType) {
		super();
		this.name = name;
		this.dataType = dataType;
	}
	
	public String getHumpName() {
		return StringUtils.hump(name, false);
	}

	/**
	 * 根据数据库字段类型获得对应的Java数据类型.
	 * @return
	 */
	public String getJavaType()
	{
		if("INT".equals(dataType))
		{
			return "Integer";
		}
		else if("BIGINT".equals(dataType))
		{
			return "Long";
		}
		else if("DECIMAL".equals(dataType))
		{
			return "BigDecimal";
		}
		else if("TINYINT".equals(dataType) || "BIT".equals(dataType))
		{
			return "Boolean";
		}
		else if("DATE".equals(dataType))
		{
			return "LocalDate";
		}
		else if("TIME".equals(dataType))
		{
			return "LocalTime";
		}
		else if("DATETIME".equals(dataType))
		{
			return "LocalDateTime";
		}
		else {
			return "String";
		}
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ColumnModel [name=").append(name).append(", dataType=")
				.append(dataType).append(", dataLength=").append(dataLength)
				.append(", nullable=").append(nullable).append(", comment=")
				.append(comment).append("]");
		return builder.toString();
	}



	/**
	 * @return the dataLength
	 */
	public int getDataLength() {
		return dataLength;
	}



	/**
	 * @param dataLength the dataLength to set
	 */
	public void setDataLength(int dataLength) {
		this.dataLength = dataLength;
	}



	/**
	 * @return the nullable
	 */
	public boolean isNullable() {
		return nullable;
	}



	/**
	 * @param nullable the nullable to set
	 */
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}



	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}



	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

}
