package com.littcore.codegen.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import net.miginfocom.swing.MigLayout;

import com.littcore.codegen.DictConfigManager;
import com.littcore.codegen.common.GenConstants.LangType;
import com.littcore.codegen.gui.DictNode.NodeType;
import com.littcore.codegen.model.DictConfig;
import com.littcore.codegen.model.DictModule;
import com.littcore.codegen.model.DictParam;
import com.littcore.codegen.model.DictParamType;
import com.littcore.codegen.model.ProjectContext;
import com.littcore.codegen.util.ConfigUtils;

public class EditDictParamPanel extends JPanel {
	
	private ProjectContext context;
	private JTree tree;
	private DefaultMutableTreeNode rootNode = new  DefaultMutableTreeNode("字典树"); // 定义树结点
	private JTextField txtDictType;
	private JTextField txtDictTypeName;
	private JTextField txtDictValue;
	private JTextField txtDictContent;	
	private JTextField txtModule;
	
	private String opType;
	private NodeType newNodeType;
	private JCheckBox chckbxSyncToAll;
	private JTextField txtRemark;
	private JLabel lblModule;
	private JLabel lblDictType;
	private JLabel lblDictTypeName;
	private JLabel lblDictValue;
	private JLabel lblDictContent;
	private JLabel lblRemark;
	
	/**
	 * Create the panel.
	 */
	public EditDictParamPanel(ProjectContext context) {
		this.context = context;
		
		setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerLocation(200);
		splitPane.setOneTouchExpandable(true);
	    splitPane.setDividerSize(10);//设置分隔线宽度的大小，以pixel为计算单位。
		add(splitPane);
		
		JPanel formPanel = new JPanel();
		splitPane.setRightComponent(formPanel);
		formPanel.setLayout(new MigLayout("", "[][grow]", "[][][][][][][][]"));
		
		JToolBar toolBar = new JToolBar();
		formPanel.add(toolBar, "cell 0 0 2 1");
		
		JButton btnNewDictParamType = new JButton("New Dict Param Type");
		btnNewDictParamType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable(){
					
					@Override
					public void run() {	
						resetForm();
						DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
						if(treeNode.isRoot())
						{
							return;
						}
						final DictNode node = (DictNode)treeNode;  
						opType = "new";
						newNodeType = NodeType.DictType;								
						renderForm(node);	
					}
				});	
			}
		});
		
		JButton btnNewModule = new JButton("New Module");
		btnNewModule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable(){
					
					@Override
					public void run() {	
						resetForm();						
						opType = "new";
						newNodeType = NodeType.Module;								
						renderModule();
					}
				});	
			}
		});
		toolBar.add(btnNewModule);
		toolBar.add(btnNewDictParamType);
		
		JButton btnNewDictParam = new JButton("New Dict Param");
		btnNewDictParam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable(){
					
					@Override
					public void run() {
						resetForm();
						DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
						if(treeNode.isRoot())
						{
							return;
						}
						final DictNode node = (DictNode)treeNode;  
						opType = "new";
						newNodeType = NodeType.DictParam;								
						renderForm(node);
					}
				});	
			}
		});
		toolBar.add(btnNewDictParam);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				SwingUtilities.invokeLater(new Runnable(){
					
					@Override
					public void run() {			
						
						try {
							deleteNode();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, "删除失败！", "系统提示", JOptionPane.ERROR_MESSAGE);
						}
						
					}
				});	
			}
		});
		toolBar.add(btnDelete);
		
		lblModule = new JLabel("Module:");
		formPanel.add(lblModule, "cell 0 1,alignx trailing");
		
		txtModule = new JTextField();
		txtModule.setText("module");
		formPanel.add(txtModule, "cell 1 1,growx");
		txtModule.setColumns(10);
		
		lblDictType = new JLabel("Dict Type:");
		formPanel.add(lblDictType, "cell 0 2,alignx trailing");
		
		txtDictType = new JTextField();
		formPanel.add(txtDictType, "cell 1 2,growx");
		txtDictType.setColumns(10);
		
		lblDictTypeName = new JLabel("Dict Type Name:");
		formPanel.add(lblDictTypeName, "cell 0 3,alignx trailing");
		
		txtDictTypeName = new JTextField();
		txtDictTypeName.setText("Dict Type Name");
		formPanel.add(txtDictTypeName, "cell 1 3,growx");
		txtDictTypeName.setColumns(10);
		
		lblDictValue = new JLabel("Dict Value:");
		formPanel.add(lblDictValue, "cell 0 4,alignx trailing");
		
		txtDictValue = new JTextField();
		txtDictValue.setText("Dict Value");
		formPanel.add(txtDictValue, "cell 1 4,growx");
		txtDictValue.setColumns(10);
		
		lblDictContent = new JLabel("Dict Content:");
		formPanel.add(lblDictContent, "cell 0 5,alignx trailing");
		
		txtDictContent = new JTextField();
		txtDictContent.setText("Dict Content");
		formPanel.add(txtDictContent, "cell 1 5,growx");
		txtDictContent.setColumns(10);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable(){
					
					@Override
					public void run() {			
						
						try {
							if("update".equals(opType))
							{
								updateForm();
							}
							else
							{
								saveForm();
							}
						} catch (Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(null, "保存失败！", "系统提示", JOptionPane.ERROR_MESSAGE);
						}
						
					}
				});				
			}
		});
		
		lblRemark = new JLabel("Remark:");
		formPanel.add(lblRemark, "cell 0 6,alignx trailing");
		
		txtRemark = new JTextField();
		txtRemark.setText("Remark");
		formPanel.add(txtRemark, "cell 1 6,growx");
		txtRemark.setColumns(10);
		formPanel.add(btnSave, "flowx,cell 1 7");
		
		chckbxSyncToAll = new JCheckBox("Sync to all lang");
		chckbxSyncToAll.setSelected(true);
		formPanel.add(chckbxSyncToAll, "cell 1 7");
		
		tree = new JTree(rootNode);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				changeDictTree();
			}
		});
		tree.setAutoscrolls(true);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); //设置单选模式
		splitPane.setLeftComponent(new JScrollPane(tree));

	}
	
	public void initData()
	{
		//remove all nodes
		rootNode.removeAllChildren();
		
		DictConfig config = ConfigUtils.loadByCastor(DictConfig.class, "classpath:dict-conf-mapping.xml", this.context.getDictConfFilePath());
		DictModule[] dictList = config.getDictModuleList();
		//构造tree
		for(DictModule module : dictList)
		{
			DictNode moduleNode = new DictNode(module.getTableName(), module.getTableName(), DictNode.NodeType.Module);
			moduleNode.setBusinessObject(module);
			rootNode.add(moduleNode);
			
			List<DictParamType> dictParamTypeList = module.getDictParamTypeList();
			for(DictParamType dictParamType : dictParamTypeList)
			{
				DictNode typeNode = new DictNode(dictParamType.getDictType(), dictParamType.getDictTypeName(), DictNode.NodeType.DictType);
				typeNode.setBusinessObject(dictParamType);
				moduleNode.add(typeNode);
				
				List<DictParam> dictParamList = dictParamType.getDictParamList();
				for(DictParam dictParam : dictParamList)
				{
					DictNode paramNode = new DictNode(dictParam.getDictValue(), dictParam.getDictContent(), DictNode.NodeType.DictParam);
					paramNode.setBusinessObject(dictParam);
					typeNode.add(paramNode);
				}	
			}
		}	
		tree.updateUI();
	}
	
	public void changeDictTree()
	{
		SwingUtilities.invokeLater(new Runnable(){
			
			@Override
			public void run() {						
				resetForm();
				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
				if(treeNode.isRoot())
				{
					return;
				}
				final DictNode node = (DictNode)treeNode;  
				Object businessObject = node.getBusinessObject();
				opType = "update";
				newNodeType = node.getType();								
				renderForm(node);
			}
		});	
	}
	
	private void resetForm()
	{
		this.txtDictContent.setText("");
		this.txtDictType.setText("");
		this.txtDictTypeName.setText("");
		this.txtDictValue.setText("");
		this.txtModule.setText("");
		this.txtRemark.setText("");
	}
	
	private void renderModule()
	{
		this.lblDictType.setVisible(false);
		this.txtDictType.setVisible(false);
		this.lblDictTypeName.setVisible(false);
		this.txtDictTypeName.setVisible(false);
		this.lblDictValue.setVisible(false);
		this.txtDictValue.setVisible(false);
		this.lblDictContent.setVisible(false);
		this.txtDictContent.setVisible(false);
		this.lblRemark.setVisible(false);
		this.txtRemark.setVisible(false);
	}
	
	private void renderDictType()
	{
		this.lblDictType.setVisible(true);
		this.txtDictType.setVisible(true);
		this.lblDictTypeName.setVisible(true);
		this.txtDictTypeName.setVisible(true);
		
		this.lblDictValue.setVisible(false);
		this.txtDictValue.setVisible(false);
		this.lblDictContent.setVisible(false);
		this.txtDictContent.setVisible(false);
		this.lblRemark.setVisible(false);
		this.txtRemark.setVisible(false);
	}
	
	private void renderDictValue()
	{
		this.lblDictType.setVisible(true);
		this.txtDictType.setVisible(true);
		this.lblDictTypeName.setVisible(true);
		this.txtDictTypeName.setVisible(true);
		
		this.lblDictValue.setVisible(true);
		this.txtDictValue.setVisible(true);
		this.lblDictContent.setVisible(true);
		this.txtDictContent.setVisible(true);
		this.lblRemark.setVisible(true);
		this.txtRemark.setVisible(true);
	}
	
	private void renderForm(DictNode node)
	{
		Object businessObject = node.getBusinessObject();
		switch(node.getType())
		{					
			case Module:
			{
				this.renderModule();
				
				DictModule module = (DictModule)businessObject;
				this.txtModule.setText(module.getTableName());
				
				return;
			}
			case DictType:
			{
				this.renderDictType();
				DictParamType dictType = (DictParamType)businessObject;
				this.txtDictType.setText(dictType.getDictType());
				this.txtDictTypeName.setText(dictType.getDictTypeName());
				
				DictModule module = (DictModule)((DictNode)node.getParent()).getBusinessObject();
				this.txtModule.setText(module.getTableName());			
				return;
			}		
			case DictParam:
			{
				this.renderDictValue();
				DictParam dict = (DictParam)businessObject;
				this.txtDictValue.setText(dict.getDictValue());
				this.txtDictContent.setText(dict.getDictContent());
				
				DictParamType dictType = (DictParamType)((DictNode)node.getParent()).getBusinessObject();
				this.txtDictType.setText(dictType.getDictType());
				this.txtDictTypeName.setText(dictType.getDictTypeName());
				
				DictModule module = (DictModule)((DictNode)node.getParent().getParent()).getBusinessObject();
				this.txtModule.setText(module.getTableName());				
				return;		
			}	
		}
	}
	
	private void saveForm() throws Exception
	{
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		if(treeNode.isRoot())
		{
			return;
		}
		final DictNode node = (DictNode)treeNode;  
		Object businessObject = node.getBusinessObject();
		
		switch(newNodeType)
		{					
			case Module:
			{			
				DictModule module = new DictModule();
				module.setTableName(this.txtModule.getText());
				DictConfigManager manager = new DictConfigManager(context.getDictConfFilePath());
				manager.saveModule(module);
				tree.updateUI();
				//同步其他语言版本
				if(chckbxSyncToAll.isSelected())
				{
					LangType[] langTypes = LangType.values();
					for(LangType langType : langTypes)
					{
						if(langType.equals(this.context.getLangType()))	//当前语言版本已处理，忽略
							continue;
						
						try {
							DictConfigManager langConfigManager = new DictConfigManager(this.context.getDictConfFilePath(langType));
							langConfigManager.saveModule(module);		
						} catch (Exception e) {
							JOptionPane.showMessageDialog(this, "保存失败！", "系统提示", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				return;
			}
			case DictType:
			{
				DictModule module = (DictModule)businessObject;
				
				DictParamType dictType = new DictParamType();
				dictType.setDictType(this.txtDictType.getText());
				dictType.setDictTypeName(this.txtDictTypeName.getText());				
				
				DictConfigManager manager = new DictConfigManager(context.getDictConfFilePath());
				manager.saveDictType(module.getTableName(), dictType);	
				tree.updateUI();
				//同步其他语言版本
				if(chckbxSyncToAll.isSelected())
				{
					LangType[] langTypes = LangType.values();
					for(LangType langType : langTypes)
					{
						if(langType.equals(this.context.getLangType()))	//当前语言版本已处理，忽略
							continue;
						
						try {
							DictConfigManager langConfigManager = new DictConfigManager(this.context.getDictConfFilePath(langType));
							langConfigManager.saveDictType(module.getTableName(), dictType);			
						} catch (Exception e) {
							JOptionPane.showMessageDialog(this, "保存失败！", "系统提示", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				return;
			}		
			case DictParam:
			{
				DictParamType dictType = (DictParamType)businessObject;				
				DictModule module = (DictModule)((DictNode)node.getParent()).getBusinessObject();				
				
				DictParam dictParam = new DictParam();
				dictParam.setDictValue(this.txtDictValue.getText());
				dictParam.setDictContent(this.txtDictContent.getText());
				dictParam.setRemark(this.txtRemark.getText());
				
				DictConfigManager manager = new DictConfigManager(context.getDictConfFilePath());
				manager.saveDictParam(module.getTableName(), dictType.getDictType(), dictParam);		
				tree.updateUI();
				//同步其他语言版本
				if(chckbxSyncToAll.isSelected())
				{
					LangType[] langTypes = LangType.values();
					for(LangType langType : langTypes)
					{
						if(langType.equals(this.context.getLangType()))	//当前语言版本已处理，忽略
							continue;
						
						try {
							DictConfigManager langConfigManager = new DictConfigManager(this.context.getDictConfFilePath(langType));
							langConfigManager.saveDictParam(module.getTableName(), dictType.getDictType(), dictParam);	
						} catch (Exception e) {
							JOptionPane.showMessageDialog(this, "保存失败！", "系统提示", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				return;		
			}	
		}		
	}
	
	private void updateForm() throws Exception
	{
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		if(treeNode.isRoot())
		{
			return;
		}
		final DictNode node = (DictNode)treeNode;  
		Object businessObject = node.getBusinessObject();
		switch(node.getType())
		{					
			case Module:
			{				
				return;
			}
			case DictType:
			{
				DictModule module = (DictModule)((DictNode)node.getParent()).getBusinessObject();
				
				DictParamType dictType = (DictParamType)businessObject;
				dictType.setDictTypeName(this.txtDictTypeName.getText());	
				
				
				DictConfigManager manager = new DictConfigManager(context.getDictConfFilePath());
				manager.updateDictType(module.getTableName(), dictType, false);	
				node.setName(dictType.getDictTypeName());
				tree.updateUI();
				//如果要同步其他语言版本，则同时删除其他版本上的同名节点
				if(chckbxSyncToAll.isSelected())
				{
					LangType[] langTypes = LangType.values();
					for(LangType langType : langTypes)
					{
						if(langType.equals(this.context.getLangType()))	//当前语言版本已处理，忽略
							continue;
						
						try {
							DictConfigManager langConfigManager = new DictConfigManager(this.context.getDictConfFilePath(langType));
							langConfigManager.updateDictType(module.getTableName(), dictType, this.chckbxSyncToAll.isSelected());		
						} catch (Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(this, "保存失败！", "系统提示", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				return;
			}		
			case DictParam:
			{
				DictParamType dictType = (DictParamType)((DictNode)node.getParent()).getBusinessObject();				
				DictModule module = (DictModule)((DictNode)node.getParent().getParent()).getBusinessObject();				
				
				DictParam dictParam = (DictParam)businessObject;
				dictParam.setDictContent(this.txtDictContent.getText());
				dictParam.setRemark(this.txtRemark.getText());
				
				DictConfigManager manager = new DictConfigManager(context.getDictConfFilePath());
				manager.updateDictParam(module.getTableName(), dictType.getDictType(), dictParam, false);	
				node.setName(dictParam.getDictContent());
				tree.updateUI();
				//同步其他语言版本
				if(chckbxSyncToAll.isSelected())
				{
					LangType[] langTypes = LangType.values();
					for(LangType langType : langTypes)
					{
						if(langType.equals(this.context.getLangType()))	//当前语言版本已处理，忽略
							continue;
						
						try {
							DictConfigManager langConfigManager = new DictConfigManager(this.context.getDictConfFilePath(langType));
							langConfigManager.updateDictParam(module.getTableName(), dictType.getDictType(), dictParam, this.chckbxSyncToAll.isSelected());			
						} catch (Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(this, "保存失败！", "系统提示", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				return;		
			}	
		}		
	}
	
	private void deleteNode() throws Exception
	{
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		if(treeNode.isRoot())
		{
			return;
		}
		final DictNode node = (DictNode)treeNode;  
		Object businessObject = node.getBusinessObject();
		switch(node.getType())
		{					
			case Module:
			{		
				DictModule module = (DictModule)businessObject;
				DictConfigManager manager = new DictConfigManager(context.getDictConfFilePath());
				manager.deleteModule(module.getTableName());
				//如果要同步其他语言版本，则同时删除其他版本上的同名节点
				if(chckbxSyncToAll.isSelected())
				{
					LangType[] langTypes = LangType.values();
					for(LangType langType : langTypes)
					{
						if(langType.equals(this.context.getLangType()))	//当前语言版本已处理，忽略
							continue;
						
						try {
							DictConfigManager langConfigManager = new DictConfigManager(this.context.getDictConfFilePath(langType));
							langConfigManager.deleteModule(module.getTableName());		
						} catch (Exception e) {
							JOptionPane.showMessageDialog(this, "删除失败！", "系统提示", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				return;
			}
			case DictType:
			{
				DictModule module = (DictModule)((DictNode)node.getParent()).getBusinessObject();				
				DictParamType dictType = (DictParamType)businessObject;				
				
				DictConfigManager manager = new DictConfigManager(context.getDictConfFilePath());
				manager.deleteDictType(dictType.getDictType());
				tree.updateUI();
				//如果要同步其他语言版本，则同时删除其他版本上的同名节点
				if(chckbxSyncToAll.isSelected())
				{
					LangType[] langTypes = LangType.values();
					for(LangType langType : langTypes)
					{
						if(langType.equals(this.context.getLangType()))	//当前语言版本已处理，忽略
							continue;
						
						try {
							DictConfigManager langConfigManager = new DictConfigManager(this.context.getDictConfFilePath(langType));
							langConfigManager.deleteDictType(dictType.getDictType());		
						} catch (Exception e) {
							JOptionPane.showMessageDialog(this, "删除失败！", "系统提示", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				return;
			}		
			case DictParam:
			{
				DictParamType dictType = (DictParamType)((DictNode)node.getParent()).getBusinessObject();				
				DictModule module = (DictModule)((DictNode)node.getParent().getParent()).getBusinessObject();				
				
				DictParam dictParam = (DictParam)businessObject;				
				
				DictConfigManager manager = new DictConfigManager(context.getDictConfFilePath());
				manager.deleteDictParam(dictType.getDictType(), dictParam.getDictValue());
				tree.updateUI();
				//同步其他语言版本
				if(chckbxSyncToAll.isSelected())
				{
					LangType[] langTypes = LangType.values();
					for(LangType langType : langTypes)
					{
						if(langType.equals(this.context.getLangType()))	//当前语言版本已处理，忽略
							continue;
						
						try {
							DictConfigManager langConfigManager = new DictConfigManager(this.context.getDictConfFilePath(langType));
							langConfigManager.deleteDictParam(dictType.getDictType(), dictParam.getDictValue());			
						} catch (Exception e) {
							JOptionPane.showMessageDialog(this, "保存失败！", "系统提示", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				return;		
			}	
		}		
	}

	public JTree getTree() {
		return tree;
	}
	public JCheckBox getChckbxIsSync() {
		return chckbxSyncToAll;
	}
	public JTextField getTxtRemark() {
		return txtRemark;
	}
	public JLabel getLblModule() {
		return lblModule;
	}
	public JLabel getLblDictType() {
		return lblDictType;
	}
	public JLabel getLblDictTypeName() {
		return lblDictTypeName;
	}
	public JLabel getLblDictValue() {
		return lblDictValue;
	}
	public JLabel getLblDictContent() {
		return lblDictContent;
	}
	public JLabel getLblRemark() {
		return lblRemark;
	}
}
