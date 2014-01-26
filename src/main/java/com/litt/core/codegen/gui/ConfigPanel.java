package com.litt.core.codegen.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.litt.core.codegen.GenCode;
import com.litt.core.codegen.GenDictParamSql;
import com.litt.core.codegen.GenInitSql;
import com.litt.core.codegen.common.GenConstants.DatabaseType;
import com.litt.core.codegen.common.GenConstants.LangType;
import com.litt.core.codegen.model.Project;
import com.litt.core.codegen.model.ProjectContext;
import com.litt.core.util.XmlUtils;

public class ConfigPanel extends JPanel {
	
	private static Logger logger = LoggerFactory.getLogger(ConfigPanel.class);
	
	private JComboBox comboBox;
	
	private Project currentProject;
	private ProjectContext context = new ProjectContext();
	private JComboBox comboBox_databaseType;
	private JComboBox comboBox_langType;
	private EditPanel editPanel;
	private JCheckBox chckbxGenAllLang;
	private EditDictParamPanel dictPanel;
	
	
	
	public ConfigPanel()
	{
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 170, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblNewLabel = new JLabel("\u9879\u76EE\uFF1A");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);
		
		comboBox = new JComboBox();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				changeProject(e);				
			}			
		});
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.gridwidth = 3;
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 0;
		add(comboBox, gbc_comboBox);
		
		JButton btnNewButton = new JButton("\u8BFB\u53D6");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 4;
		gbc_btnNewButton.gridy = 0;
		add(btnNewButton, gbc_btnNewButton);		
		
		JLabel label = new JLabel("\u8BED\u8A00\uFF1A");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.EAST;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 1;
		add(label, gbc_label);
		
		comboBox_langType = new JComboBox();
		comboBox_langType.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				
				final LangType langType = (LangType)e.getItem();
				SwingUtilities.invokeLater(new Runnable(){

					@Override
					public void run() {
						
						switchLang(langType);
					}		
					
				});
			}
		});
		comboBox_langType.setModel(new DefaultComboBoxModel(LangType.values()));
		GridBagConstraints gbc_comboBox_langType = new GridBagConstraints();
		gbc_comboBox_langType.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_langType.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_langType.gridx = 1;
		gbc_comboBox_langType.gridy = 1;
		add(comboBox_langType, gbc_comboBox_langType);
		
		JLabel lblNewLabel_1 = new JLabel("\u6570\u636E\u5E93\u7C7B\u578B\uFF1A");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 2;
		add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		comboBox_databaseType = new JComboBox();
		comboBox_databaseType.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {				
				context.setDatabaseType((DatabaseType)e.getItem());
			}
		});
		comboBox_databaseType.setModel(new DefaultComboBoxModel(DatabaseType.values()));
		GridBagConstraints gbc_comboBox_databaseType = new GridBagConstraints();
		gbc_comboBox_databaseType.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_databaseType.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_databaseType.gridx = 1;
		gbc_comboBox_databaseType.gridy = 2;
		add(comboBox_databaseType, gbc_comboBox_databaseType);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolBar.gridwidth = 5;
		gbc_toolBar.insets = new Insets(0, 0, 5, 0);
		gbc_toolBar.gridx = 0;
		gbc_toolBar.gridy = 3;
		add(toolBar, gbc_toolBar);
		
		JButton btnNewButton_1 = new JButton("\u751F\u6210\u5B57\u5178");
		toolBar.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//生成字典脚本
				genDictParamSql();
			}
		});
		
		JButton btnNewButton_2 = new JButton("\u751F\u6210\u521D\u59CB\u5316\u811A\u672C");
		toolBar.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("\u751F\u6210\u4EE3\u7801");
		toolBar.add(btnNewButton_3);
		
		chckbxGenAllLang = new JCheckBox("Gen All Lang");
		chckbxGenAllLang.setSelected(true);
		toolBar.add(chckbxGenAllLang);
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//生成代码
				genCode();
			}
		});
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//生成初始化脚本
				genInitSql();
			}
		});
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.gridwidth = 5;
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 5);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 4;
		add(tabbedPane, gbc_tabbedPane);
		
		editPanel = new EditPanel(context);
		GridBagConstraints gbc_editPanel = new GridBagConstraints();
		gbc_editPanel.gridwidth = 5;
		gbc_editPanel.fill = GridBagConstraints.BOTH;
		gbc_editPanel.gridx = 0;
		gbc_editPanel.gridy = 5;
		//add(editPanel, gbc_editPanel);
		
		tabbedPane.addTab("Modules", editPanel);
		
		dictPanel = new EditDictParamPanel(context);
		tabbedPane.addTab("Dict", dictPanel);
		
		try {
			this.init();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	public void init() throws Exception
	{
		File configFile =  new File(Gui.HOME_PATH+ File.separator+ "config.xml");								
		
		Document document = XmlUtils.readXml(configFile);
		Element rootE = document.getRootElement();
		List productList = rootE.elements();
		for(int i=0;i<productList.size();i++)
		{
			Element productE = (Element)productList.get(i);							
			String code = productE.attributeValue("code");
			
			Project project = new Project(code);
			comboBox.addItem(project);
			if(i==0)
			{
				currentProject = project;
				this.context.setProject(project);
				this.context.setLangType((LangType)this.comboBox_langType.getSelectedItem());
				this.context.setDatabaseType((DatabaseType)this.comboBox_databaseType.getSelectedItem());
				this.initEditPanel(project);
				
			}
		}
	}
	
	private void changeProject(ItemEvent e) 
	{
		if(e.getStateChange()==ItemEvent.SELECTED)
		{
			try
			{
				Project project = (Project)e.getItem();														
				currentProject = project;
				this.context.setProject(project);
				this.initEditPanel(project);
			}
			catch (Exception e1)
			{
				logger.error("变更项目失败.", e1);
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}					
		}
	}
	
	private void switchLang(LangType langType)
	{
		this.context.setLangType(langType);
		this.initEditPanel(currentProject);
	}
	
	private void initEditPanel(Project project)
	{
		final LangType langType = (LangType)comboBox_langType.getSelectedItem();
		String projectPath = Gui.HOME_PATH+ File.separator+ currentProject.getCode();
		final File filePath = new File(projectPath, currentProject.getCode()+ "-module-config-"+ langType.name() +".xml");	
		if(!filePath.exists())
		{
			JOptionPane.showMessageDialog(this, "目标配置文件不存在！", "系统提示", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		File targetFilePath = new File(Gui.HOME_PATH+ File.separator+ currentProject.getCode(), "target");	
		try {
			final GenInitSql gen = new GenInitSql(projectPath, filePath.getPath(), targetFilePath.getPath(), (DatabaseType)comboBox_databaseType.getSelectedItem(), langType);
			final JPanel self = this;
			SwingUtilities.invokeLater(new Runnable(){

				@Override
				public void run() {
					try {
						editPanel.initModuleTree(gen.getDomainList());
						dictPanel.initData();
					} catch (Exception e) {
						logger.error("初始化失败", e);
						JOptionPane.showMessageDialog(self, "初始化模块树失败", "系统提示", JOptionPane.ERROR_MESSAGE);
					}					
				}		
				
			});
		} catch (Exception e1) {
			logger.error("生成失败.", e1);
			JOptionPane.showMessageDialog(this, e1.getMessage());
		}		
	}
	
	private void genDictParamSql() {
		
		String projectPath = Gui.HOME_PATH+ File.separator+ currentProject.getCode();
		File targetFilePath = new File(projectPath, "target");	
		try {
			File filePath = new File(projectPath, currentProject.getCode()+"-dict-config-"+ this.context.getLangType() +".xml");	
			GenDictParamSql gen = new GenDictParamSql(projectPath, filePath.getPath(), targetFilePath.getPath(), this.context.getDatabaseType(), this.context.getLangType());
			gen.gen();
			JOptionPane.showMessageDialog(this, "创建成功！", "系统提示", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e1) {
			logger.error("生成失败.", e1);
			JOptionPane.showMessageDialog(this, e1.getMessage());
		}
		//处理其他语言版本
		if(this.chckbxGenAllLang.isSelected())
		{
			LangType[] langTypes = LangType.values();
			for(LangType langType : langTypes)
			{
				if(langType.equals(this.context.getLangType()))	//当前语言版本已处理，忽略
					continue;
				
				try {
					File filePath = new File(projectPath, currentProject.getCode()+"-dict-config-"+ langType +".xml");	
					GenDictParamSql gen = new GenDictParamSql(projectPath, filePath.getPath(), targetFilePath.getPath(), this.context.getDatabaseType(), langType);
					gen.gen();					
				} catch (Exception e1) {
					logger.error("生成失败.", e1);
					JOptionPane.showMessageDialog(this, e1.getMessage());
					return;
				}
			}
		}
	}
	
	private void genInitSql() 
	{
		
		File filePath = new File(this.context.getModuleConfFilePath());	
		if(!filePath.exists())
		{
			JOptionPane.showMessageDialog(this, "目标配置文件不存在！", "系统提示", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		String projectPath = Gui.HOME_PATH+ File.separator+ currentProject.getCode();
		File targetFilePath = new File(projectPath, "target");	
		try {
			GenInitSql gen = new GenInitSql(projectPath, filePath.getPath(), targetFilePath.getPath(), (DatabaseType)comboBox_databaseType.getSelectedItem(), this.context.getLangType());
			gen.gen();
			JOptionPane.showMessageDialog(this, "创建成功！", "系统提示", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e1) {
			logger.error("生成失败.", e1);
			JOptionPane.showMessageDialog(this, e1.getMessage());
			return;
		}
		//处理其他语言版本
		if(this.chckbxGenAllLang.isSelected())
		{
			LangType[] langTypes = LangType.values();
			for(LangType langType : langTypes)
			{
				if(langType.equals(this.context.getLangType()))	//当前语言版本已处理，忽略
					continue;
				
				try {
					GenInitSql gen = new GenInitSql(projectPath, this.context.getModuleConfFilePath(langType), targetFilePath.getPath(), this.context.getDatabaseType(), langType);
					gen.gen();					
				} catch (Exception e1) {
					logger.error("生成失败.", e1);
					JOptionPane.showMessageDialog(this, e1.getMessage());
					return;
				}
			}
		}
	}
	
	private void genCode()
	{
		String projectPath = Gui.HOME_PATH+ File.separator+ currentProject.getCode();
		File filePath = new File(projectPath, currentProject.getCode()+"-module-config-en.xml");	
		File targetFilePath = new File(Gui.HOME_PATH+ File.separator+ currentProject.getCode(), "target");	
		
		try {
			GenCode genCode = new GenCode(projectPath, filePath.getPath());
			genCode.setTargetFilePath(targetFilePath.getPath());
			genCode.gen();
			JOptionPane.showMessageDialog(this, "创建成功！", "系统提示", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e1) {
			logger.error("生成失败.", e1);
			JOptionPane.showMessageDialog(this, e1.getMessage());
		}		
	}

	public JComboBox getComboBox() {
		return comboBox;
	}
	public JComboBox getComboBox_databaseType() {
		return comboBox_databaseType;
	}
	public JComboBox getComboBox_langType() {
		return comboBox_langType;
	}
	public EditPanel getEditPanel() {
		return editPanel;
	}
	public JCheckBox getChckbxGenAllLang() {
		return chckbxGenAllLang;
	}
	public EditDictParamPanel getDictPanel() {
		return dictPanel;
	}
}
