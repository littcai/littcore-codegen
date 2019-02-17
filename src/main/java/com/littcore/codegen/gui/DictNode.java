package com.littcore.codegen.gui;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Administrator
 *
 */
public class DictNode extends DefaultMutableTreeNode {
	
	public static enum NodeType {
		Module, DictType, DictParam 
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 
	
	private String id;
	
	private String name;
	
	private NodeType type;
	
	private Object businessObject;	

	public DictNode(String id, String name, NodeType type) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name+"["+id+"]";
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @return the businessObject
	 */
	public Object getBusinessObject() {
		return businessObject;
	}

	/**
	 * @param businessObject the businessObject to set
	 */
	public void setBusinessObject(Object businessObject) {
		this.businessObject = businessObject;
	}

	/**
	 * @return the type
	 */
	public NodeType getType() {
		return type;
	}

}
