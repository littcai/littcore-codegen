package com.littcore.codegen.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.littcore.common.Utility;
import com.littcore.util.PropertiesUtils;
import com.littcore.util.ResourceUtils;

/**
 * @author Administrator
 *
 */
public class Gui extends JFrame {

	public static boolean HOME_PATH_ENABLED = false;
	public static String HOME_PATH = "D:\\codegen";
	
	public Gui() {
		try {
			Properties props = PropertiesUtils.loadProperties(ResourceUtils.getFile("classpath:init.properties"));
			HOME_PATH = this.getClass().getResource("/").getPath();
			HOME_PATH_ENABLED = Utility.parseBoolean(props.getProperty("HOME_PATH_ENABLED", "FALSE"));
			if(HOME_PATH_ENABLED)
			{
				HOME_PATH = props.getProperty("HOME_PATH");
			}
			
//			File configFile =  new File(Gui.HOME_PATH, "config.xml");	
//			if(!configFile.exists())
//			{
//				FileUtils.copyFileToDirectory(ResourceUtils.getFile("classpath:config.xml"), new File(Gui.HOME_PATH));
//			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		setTitle("\u4EE3\u7801\u751F\u6210\u5668");
		getContentPane().setLayout(new BorderLayout(0, 0));
		this.setBounds(100, 100, 800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel = new ConfigPanel();
		tabbedPane.addTab("配置", null, panel, null);
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
				try
				{
					Gui window = new Gui();
					window.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

}
