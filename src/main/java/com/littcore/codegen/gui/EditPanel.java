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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import net.miginfocom.swing.MigLayout;

import com.littcore.codegen.ModuleConfigManager;
import com.littcore.codegen.common.GenConstants.LangType;
import com.littcore.codegen.model.Domain;
import com.littcore.codegen.model.Func;
import com.littcore.codegen.model.Module;
import com.littcore.codegen.model.ProjectContext;

public class EditPanel extends JPanel {
	private JPanel treePanel;
	private JPanel formPanel;
	private JTree tree;
		
	private DefaultMutableTreeNode rootNode = new  DefaultMutableTreeNode("功能树"); // 定义树结点
	private JButton btnNewDomain;
	private JButton btnDelete;
	private JLabel lblCode;
	private JTextField txtCode;
	private JLabel lblTitle;
	private JTextField txtTitle;
	private JLabel lblIsHide;
	private JLabel lblIsMenu;
	private JCheckBox chckbxIsHide;
	private JCheckBox chckbxIsMenu;
	private JLabel lblUrl;
	private JTextField txtUrl;
	private JLabel lblMenuName;
	private JTextField txtMenuName;
	private JLabel lblTableName;
	private JTextField txtTableName;
	private JLabel lblModelName;
	private JTextField txtModelName;
	private JButton btnSave;
	private JLabel lblDescr;
	private JTextArea txtrDescr;
	private JLabel lblPackageName;
	private JTextField txtPackageName;
	private JLabel lblParentPackageName;
	private JTextField txtParentPackageName;
	private JToolBar toolBar;
	private JButton btnNewmodule;
	private JButton btnNewfunc;

	private String newModuleNodeType;
	private String opType;
	
	
	private ModuleConfigManager manager;
	private JCheckBox chckbxSyncToAll;
	
	private ProjectContext context;
	
	/**
	 * Create the panel.
	 */
	public EditPanel(ProjectContext context) {
		final JPanel container = this;
		this.context = context;
		setLayout(new BorderLayout(0, 0));
		
		treePanel = new JPanel();
		//add(treePanel, BorderLayout.WEST);
		treePanel.setLayout(new BorderLayout(0, 0));		
		
		tree = new JTree(rootNode);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				System.out.println("tree is selected");
				SwingUtilities.invokeLater(new Runnable(){
					
					@Override
					public void run() {						
						resetForm();
						DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
						if(treeNode.isRoot())
						{
							return;
						}
						final ModuleNode node = (ModuleNode)treeNode;  
						Object businessObject = node.getBusinessObject();
						opType = "update";
						if(businessObject instanceof Domain)
						{
							newModuleNodeType = "domain";
						}
						else if(businessObject instanceof Module)
						{
							newModuleNodeType = "module";
						}
						else if(businessObject instanceof Func)
						{
							newModuleNodeType = "func";
						}
						renderForm(node);
					}
				});	
			}			
		});
		tree.setAutoscrolls(true);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); //设置单选模式
		
		treePanel.add(new  JScrollPane(tree));
		
		formPanel = new JPanel();
		//add(formPanel, BorderLayout.CENTER);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false);
		splitPane.add(treePanel);
		splitPane.add(formPanel);
		splitPane.setDividerLocation(200);
		splitPane.setOneTouchExpandable(true);
	    splitPane.setDividerSize(10);//设置分隔线宽度的大小，以pixel为计算单位。
	    add(splitPane, BorderLayout.CENTER);
		
		formPanel.setLayout(new MigLayout("", "[][grow]", "[][][][][][][][][][][][][][grow][][]"));
		
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		formPanel.add(toolBar, "cell 0 0 2 1");
		
		btnNewDomain = new JButton("NewDomain");
		btnNewDomain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				SwingUtilities.invokeLater(new Runnable(){
					
					@Override
					public void run() {
						opType = "add";
						resetForm();
						renderNew("domain");						
					}
				});	
			}
		});
		toolBar.add(btnNewDomain);
		
		btnNewmodule = new JButton("NewModule");
		btnNewmodule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable(){
					
					@Override
					public void run() {
						opType = "add";
						resetForm();
						renderNew("module");
					}
				});					
			}
		});
		toolBar.add(btnNewmodule);
		
		btnNewfunc = new JButton("NewFunc");
		btnNewfunc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable(){
					
					@Override
					public void run() {
						opType = "add";
						resetForm();
						renderNew("func");
					}
				});					
			}
		});
		toolBar.add(btnNewfunc);
		
		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int result = JOptionPane.showConfirmDialog(container, "确认删除？", "系统确认", JOptionPane.YES_NO_OPTION);
				
				if(result == 0 )
				{
					SwingUtilities.invokeLater(new Runnable(){
									
						@Override
						public void run() {
							deleteModuleNode();
							resetForm();							
						}
					});	
				}
			}
		});
		toolBar.add(btnDelete);
		
		lblCode = new JLabel("Code:");
		formPanel.add(lblCode, "cell 0 2,alignx trailing");
		
		txtCode = new JTextField();
		txtCode.setText("code");
		formPanel.add(txtCode, "cell 1 2,growx");
		txtCode.setColumns(10);		
		
		lblTitle = new JLabel("Title:");
		formPanel.add(lblTitle, "cell 0 4,alignx trailing");
		
		txtTitle = new JTextField();
		txtTitle.setText("title");
		formPanel.add(txtTitle, "cell 1 4,growx");
		txtTitle.setColumns(10);
		
		lblUrl = new JLabel("Url:");
		formPanel.add(lblUrl, "cell 0 5,alignx trailing");
		
		txtUrl = new JTextField();
		txtUrl.setText("url");
		formPanel.add(txtUrl, "cell 1 5,growx");
		txtUrl.setColumns(10);
		
		lblPackageName = new JLabel("packageName:");
		formPanel.add(lblPackageName, "cell 0 6,alignx trailing");
		
		txtPackageName = new JTextField();
		txtPackageName.setText("packageName");
		formPanel.add(txtPackageName, "cell 1 6,growx");
		txtPackageName.setColumns(10);
		
		lblParentPackageName = new JLabel("parentPackageName:");
		formPanel.add(lblParentPackageName, "cell 0 7,alignx trailing");
		
		txtParentPackageName = new JTextField();
		txtParentPackageName.setText("parent package name");
		formPanel.add(txtParentPackageName, "cell 1 7,growx");
		txtParentPackageName.setColumns(10);
		
		lblMenuName = new JLabel("Menu Name:");
		formPanel.add(lblMenuName, "cell 0 8,alignx trailing");
		
		txtMenuName = new JTextField();
		txtMenuName.setText("menuName");
		formPanel.add(txtMenuName, "cell 1 8,growx");
		txtMenuName.setColumns(10);
		
		lblTableName = new JLabel("tableName:");
		formPanel.add(lblTableName, "cell 0 9,alignx trailing");
		
		txtTableName = new JTextField();
		txtTableName.setText("tableName");
		formPanel.add(txtTableName, "cell 1 9,growx");
		txtTableName.setColumns(10);
		
		lblModelName = new JLabel("modelName:");
		formPanel.add(lblModelName, "cell 0 10,alignx trailing");
		
		txtModelName = new JTextField();
		txtModelName.setText("modelName");
		formPanel.add(txtModelName, "cell 1 10,growx");
		txtModelName.setColumns(10);
		
		lblIsHide = new JLabel("Hide:");
		formPanel.add(lblIsHide, "cell 0 11");
		
		chckbxIsHide = new JCheckBox("isHide");
		formPanel.add(chckbxIsHide, "cell 1 11");
		
		lblIsMenu = new JLabel("Menu:");
		formPanel.add(lblIsMenu, "cell 0 12");
		
		chckbxIsMenu = new JCheckBox("isMenu");
		formPanel.add(chckbxIsMenu, "cell 1 12");
		
		lblDescr = new JLabel("Descr:");
		formPanel.add(lblDescr, "cell 0 13");
		
		txtrDescr = new JTextArea();
		txtrDescr.setText("Descr");
		formPanel.add(new JScrollPane(txtrDescr), "cell 1 13,grow");
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable(){
					
					@Override
					public void run() {						
						saveModuleNode();
						if(!"update".equals(opType))
							resetForm();
					}
				});					
			}
		});
		formPanel.add(btnSave, "flowx,cell 1 15");
		
		chckbxSyncToAll = new JCheckBox("Sync to all lang");
		chckbxSyncToAll.setToolTipText("Module structure will be synced to all lang's config files.");
		chckbxSyncToAll.setSelected(true);
		formPanel.add(chckbxSyncToAll, "cell 1 15");
		
	}
	
	private void saveModuleNode()
	{
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		if(treeNode==null)
		{
			JOptionPane.showMessageDialog(this, "请选择一个节点", "系统提示", JOptionPane.WARNING_MESSAGE);
			return;
		}
		//父节点为ROOT，保存domain
		if(treeNode.isRoot())
		{
			
		}
		else 
		{
			final ModuleNode node = (ModuleNode)treeNode; 			
			Object businessObject = node.getBusinessObject();
			
			if("domain".equals(newModuleNodeType))
			{
				Domain domain = new Domain();
				domain.setCode(this.txtCode.getText());				
				domain.setTitle(this.txtTitle.getText());
				domain.setPackageName(this.txtPackageName.getText());
				domain.setParentPackageName(this.txtParentPackageName.getText());
				domain.setDescr(this.txtrDescr.getText());
				domain.setIsHide(this.chckbxIsHide.isSelected());
				domain.setIsMenu(this.chckbxIsMenu.isSelected());
				domain.setMenuName(this.txtMenuName.getText());		
				if("update".equals(opType))
				{
					manager.updateDomain(domain);
					node.setId(this.txtCode.getText());					
					node.setBusinessObject(domain);
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
								ModuleConfigManager langConfigManager = new ModuleConfigManager(this.context.getModuleConfFilePath(langType));
								langConfigManager.updateDomain(domain, true);
							} catch (Exception e) {
								JOptionPane.showMessageDialog(this, "保存失败！", "系统提示", JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				}
				else {
					manager.saveDomain(domain);
					this.addDomainNode(domain, treeNode);
					//如果要同步其他语言版本，则同时删除其他版本上的同名节点
					if(chckbxSyncToAll.isSelected())
					{
						LangType[] langTypes = LangType.values();
						for(LangType langType : langTypes)
						{
							if(langType.equals(this.context.getLangType()))	//当前语言版本已处理，忽略
								continue;
							
							try {
								ModuleConfigManager langConfigManager = new ModuleConfigManager(this.context.getModuleConfFilePath(langType));
								langConfigManager.saveDomain(domain);
							} catch (Exception e) {
								JOptionPane.showMessageDialog(this, "保存失败！", "系统提示", JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				}
				
			}
			else if("module".equals(newModuleNodeType))
			{
				Module module = new Module();
				module.setCode(this.txtCode.getText());
				module.setTitle(this.txtTitle.getText());
				module.setDescr(this.txtrDescr.getText());
				module.setIsHide(this.chckbxIsHide.isSelected());
				module.setIsMenu(this.chckbxIsMenu.isSelected());
				module.setMenuName(this.txtMenuName.getText());		
				module.setTableName(this.txtTableName.getText());
				module.setModelName(this.txtModelName.getText());
				module.setUrl(this.txtUrl.getText());
				if("update".equals(opType))
				{
					manager.updateModule(module);
					node.setId(this.txtCode.getText());
					node.setBusinessObject(module);
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
								ModuleConfigManager langConfigManager = new ModuleConfigManager(this.context.getModuleConfFilePath(langType));
								langConfigManager.updateModule(module, true);
							} catch (Exception e) {
								JOptionPane.showMessageDialog(this, "保存失败！", "系统提示", JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				}
				else {
					manager.saveModule(module);
					this.addModuleNode(node, module);
					//如果要同步其他语言版本，则同时删除其他版本上的同名节点
					if(chckbxSyncToAll.isSelected())
					{
						LangType[] langTypes = LangType.values();
						for(LangType langType : langTypes)
						{
							if(langType.equals(this.context.getLangType()))	//当前语言版本已处理，忽略
								continue;
							
							try {
								ModuleConfigManager langConfigManager = new ModuleConfigManager(this.context.getModuleConfFilePath(langType));
								langConfigManager.saveModule(module);
							} catch (Exception e) {
								JOptionPane.showMessageDialog(this, "保存失败！", "系统提示", JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				}	
			}
			else if("func".equals(newModuleNodeType))
			{
				Func func = new Func();
				func.setCode(this.txtCode.getText());
				func.setTitle(this.txtTitle.getText());
				func.setDescr(this.txtrDescr.getText());
				if("update".equals(opType))
				{
					manager.updateFunc(func);
					node.setId(this.txtCode.getText());
					node.setBusinessObject(func);
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
								ModuleConfigManager langConfigManager = new ModuleConfigManager(this.context.getModuleConfFilePath(langType));
								langConfigManager.updateFunc(func, true);
							} catch (Exception e) {
								JOptionPane.showMessageDialog(this, "保存失败！", "系统提示", JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				}
				else {
					manager.saveFunc(func);				
					this.addFuncNode(node, func);
					//如果要同步其他语言版本，则同时删除其他版本上的同名节点
					if(chckbxSyncToAll.isSelected())
					{
						LangType[] langTypes = LangType.values();
						for(LangType langType : langTypes)
						{
							if(langType.equals(this.context.getLangType()))	//当前语言版本已处理，忽略
								continue;
							
							try {
								ModuleConfigManager langConfigManager = new ModuleConfigManager(this.context.getModuleConfFilePath(langType));
								langConfigManager.saveFunc(func);
							} catch (Exception e) {
								JOptionPane.showMessageDialog(this, "保存失败！", "系统提示", JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				}	
			}
		}
		
	}
	
	private void deleteModuleNode()
	{
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		if(treeNode==null)
		{
			JOptionPane.showMessageDialog(this, "请选择一个节点", "系统提示", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if(treeNode.isRoot())
		{
			return;
		}
		
		final ModuleNode node = (ModuleNode)treeNode; 			
		Object businessObject = node.getBusinessObject();
		
		//执行删除逻辑
		manager.deleteNode(node.getId());
		treeNode.removeFromParent();
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
					ModuleConfigManager langConfigManager = new ModuleConfigManager(this.context.getModuleConfFilePath(langType));
					langConfigManager.deleteNode(node.getId());
				} catch (Exception e) {
					JOptionPane.showMessageDialog(this, "删除失败！", "系统提示", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		JOptionPane.showMessageDialog(this, "删除成功！", "系统提示", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void renderNew(String type)
	{
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		if(treeNode==null)
		{
			JOptionPane.showMessageDialog(this, "请选择一个节点", "系统提示", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		this.newModuleNodeType = type;
		
		if(treeNode.isRoot())
		{
			this.renderFormAsDomain();
			return;
		}
		
		final ModuleNode node = (ModuleNode)treeNode; 		
		
		Object businessObject = node.getBusinessObject();
		
		if("domain".equals(type))
		{
			this.renderFormAsDomain();
		}
		else if("module".equals(type))
		{
			if(businessObject instanceof Domain)
			{
				this.renderFormAsModule();
			}
			else
			{
				JOptionPane.showMessageDialog(this, "请选择一个Domain节点", "系统提示", JOptionPane.WARNING_MESSAGE);
			}
		}
		else if("func".equals(type))
		{
			if(businessObject instanceof Module)
			{
				this.renderFormAsFunc();
			}
			else
			{
				JOptionPane.showMessageDialog(this, "请选择一个Module节点", "系统提示", JOptionPane.WARNING_MESSAGE);
			}
		}		
	}
	
	private void renderFormAsDomain()
	{
		this.lblDescr.setVisible(true);
        txtrDescr.getParent().setVisible(true);
        this.lblIsHide.setVisible(true);
        chckbxIsHide.setVisible(true);
        this.lblIsMenu.setVisible(true);
        chckbxIsMenu.setVisible(true);
        this.lblMenuName.setVisible(true);
        txtMenuName.setVisible(true);
        
        this.lblPackageName.setVisible(true);
        txtPackageName.setVisible(true);
        this.lblParentPackageName.setVisible(true);
        txtParentPackageName.setVisible(true);  	
        
        this.lblTableName.setVisible(false);
        txtTableName.setVisible(false);
        this.lblModelName.setVisible(false);
    	txtModelName.setVisible(false);
    	this.lblUrl.setVisible(false);
    	txtUrl.setVisible(false);
	}
	
	private void renderFormAsModule()
	{
		this.lblDescr.setVisible(true);
        txtrDescr.getParent().setVisible(true);
        this.lblIsHide.setVisible(true);
        chckbxIsHide.setVisible(true);
        this.lblIsMenu.setVisible(true);
        chckbxIsMenu.setVisible(true);
        this.lblMenuName.setVisible(true);
        txtMenuName.setVisible(true);
        
        this.lblPackageName.setVisible(false);
        txtPackageName.setVisible(false);
        this.lblParentPackageName.setVisible(false);
        txtParentPackageName.setVisible(false);  	
        
        this.lblTableName.setVisible(true);
        txtTableName.setVisible(true);
        this.lblModelName.setVisible(true);
    	txtModelName.setVisible(true);
    	this.lblUrl.setVisible(true);
    	txtUrl.setVisible(true);
	}
	
	private void renderFormAsFunc()
	{        
        this.lblDescr.setVisible(false);
        txtrDescr.getParent().setVisible(false);
        this.lblIsHide.setVisible(false);
        chckbxIsHide.setVisible(false);
        this.lblIsMenu.setVisible(false);
        chckbxIsMenu.setVisible(false);
        this.lblMenuName.setVisible(false);
        txtMenuName.setVisible(false);
        
        this.lblPackageName.setVisible(false);
        txtPackageName.setVisible(false);
        this.lblParentPackageName.setVisible(false);
        txtParentPackageName.setVisible(false);  	
        
        this.lblTableName.setVisible(false);
        txtTableName.setVisible(false);
        this.lblModelName.setVisible(false);
    	txtModelName.setVisible(false);
    	this.lblUrl.setVisible(false);
    	txtUrl.setVisible(false);
	}
	
	/**
	 * @param node
	 */
	private void renderForm(ModuleNode node) {
		
		if (node == null)  
            return;                 
        String id = node.getId();                
        Object businessObject = node.getBusinessObject();
        if(businessObject instanceof Domain)
        {
        	this.renderFormAsDomain();
        	Domain domain = (Domain)businessObject;
        	this.txtCode.setText(domain.getCode());
        	this.txtTitle.setText(domain.getTitle());        	
        	this.txtrDescr.setText(domain.getDescr());        	
        	this.chckbxIsHide.setSelected(domain.getIsHide());
        	this.chckbxIsMenu.setSelected(domain.getIsMenu());        	
        	this.txtMenuName.setText(domain.getMenuName());
        	
        	this.txtPackageName.setText(domain.getPackageName());
        	this.txtParentPackageName.setText(domain.getParentPackageName());        	
        }
        else if(businessObject instanceof Module)
        {
        	this.renderFormAsModule();
        	Module module = (Module)businessObject;
        	this.txtCode.setText(module.getCode());
        	this.txtTitle.setText(module.getTitle());        	
        	this.txtrDescr.setText(module.getDescr());        	
        	this.chckbxIsHide.setSelected(module.getIsHide());
        	this.chckbxIsMenu.setSelected(module.getIsMenu());        	
        	this.txtMenuName.setText(module.getMenuName());
        	
        	this.txtTableName.setText(module.getTableName());
        	this.txtModelName.setText(module.getModelName());
        	this.txtUrl.setText(module.getUrl());
        }
        else if(businessObject instanceof Func)
        {
        	this.renderFormAsFunc();
        	Func func = (Func)businessObject;
        	this.txtCode.setText(func.getCode());
        	this.txtTitle.setText(func.getTitle());        	
        	this.txtrDescr.setText(func.getDescr());     
        }
	}
	
	private void resetForm() {
		txtCode.setText("");
        txtTitle.setText("");        	
        txtrDescr.setText("");        	
        chckbxIsHide.setSelected(false);
        chckbxIsMenu.setSelected(false);        	
        txtMenuName.setText("");
        
        txtPackageName.setText("");
        txtParentPackageName.setText("");        	
        
        txtTableName.setText("");
    	txtModelName.setText("");
    	txtUrl.setText("");		
	}    
	
	public void initModuleTree(List<Domain> domainList) throws Exception
	{		
		this.manager = new ModuleConfigManager(this.context.getModuleConfFilePath());
		
		//remove all nodes
		rootNode.removeAllChildren();
		//init new
		for(int i=0;i<domainList.size();i++)
		{
			Domain domain = domainList.get(i);			
			initDomainNode(domain, rootNode);
		}		
		tree.updateUI();
		this.resetForm();
		//rootNode.add(newChild);
	}

	/**
	 * @param domain
	 */
	private void initDomainNode(Domain domain, DefaultMutableTreeNode parentNode) {
		ModuleNode domainNode = addDomainNode(domain, parentNode);
		
		List subList = domain.getSubList();
		for(int j=0; j<subList.size(); j++)
		{
			Object sub = subList.get(j);
			
			if(sub instanceof Module)
			{
				Module module = (Module)sub;
				ModuleNode moduleNode = addModuleNode(domainNode, module);
				
				List<Func> funcList = module.getFuncList();
				for(int n=0; n<funcList.size(); n++)
				{
					Func func = funcList.get(n);
					addFuncNode(moduleNode, func);
				}
			}
			else
			{
				Domain subDomain = (Domain)sub;
				this.initDomainNode(subDomain, domainNode);
			}
		}
	}

	/**
	 * @param moduleNode
	 * @param func
	 */
	private void addFuncNode(ModuleNode moduleNode, Func func) {
		ModuleNode funcNode = new ModuleNode(func.getCode(), func.getTitle());
		funcNode.setBusinessObject(func);
		moduleNode.add(funcNode);
		
		this.tree.updateUI();
	}

	/**
	 * @param domain
	 * @param parentNode
	 * @return
	 */
	private ModuleNode addDomainNode(Domain domain,
			DefaultMutableTreeNode parentNode) {
		ModuleNode domainNode = new ModuleNode(domain.getCode(), domain.getTitle());
		domainNode.setBusinessObject(domain);
		parentNode.add(domainNode);
		
		this.tree.updateUI();
		return domainNode;
	}

	/**
	 * @param domainNode
	 * @param module
	 * @return
	 */
	private ModuleNode addModuleNode(ModuleNode domainNode, Module module) {
		ModuleNode moduleNode = new ModuleNode(module.getCode(), module.getTitle());
		moduleNode.setBusinessObject(module);
		domainNode.add(moduleNode);
		this.tree.updateUI();
		return moduleNode;
	}

	public JPanel getTreePanel() {
		return treePanel;
	}
	public JPanel getFormPanel() {
		return formPanel;
	}
	public JTree getTree() {
		return tree;
	}
}
