package com.littcore.codegen.model;

import java.io.File;

import com.littcore.codegen.common.GenConstants.DatabaseType;
import com.littcore.codegen.common.GenConstants.LangType;
import com.littcore.codegen.gui.Gui;

/**
 * 当前选择项目上下文信息.
 * 
 * 用于在不同容器间共享
 * @author Administrator
 *
 */
public class ProjectContext {
	
	private Project project;
	
	private LangType langType;
	
	private DatabaseType databaseType;
	
	public String getModuleConfFilePath()
	{
		File filePath = new File(Gui.HOME_PATH+ File.separator+ project.getCode(), project.getCode()+ "-module-config-"+ langType.name() +".xml");
		return filePath.getAbsolutePath();
	}
	
	public String getModuleConfFilePath(LangType langType)
	{
		File filePath = new File(Gui.HOME_PATH+ File.separator+ project.getCode(), project.getCode()+ "-module-config-"+ langType.name() +".xml");
		return filePath.getAbsolutePath();
	}
	
	public String getDictConfFilePath()
	{		
		File filePath = new File(Gui.HOME_PATH+ File.separator+ project.getCode(), project.getCode()+ "-dict-config-"+ langType.name() +".xml");
		return filePath.getAbsolutePath();		
	}
	
	public String getDictConfFilePath(LangType langType)
	{		
		File filePath = new File(Gui.HOME_PATH+ File.separator+ project.getCode(), project.getCode()+ "-dict-config-"+ langType.name() +".xml");
		return filePath.getAbsolutePath();		
	}

	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * @return the langType
	 */
	public LangType getLangType() {
		return langType;
	}

	/**
	 * @param langType the langType to set
	 */
	public void setLangType(LangType langType) {
		this.langType = langType;
	}

	/**
	 * @return the databaseType
	 */
	public DatabaseType getDatabaseType() {
		return databaseType;
	}

	/**
	 * @param databaseType the databaseType to set
	 */
	public void setDatabaseType(DatabaseType databaseType) {
		this.databaseType = databaseType;
	}

}
