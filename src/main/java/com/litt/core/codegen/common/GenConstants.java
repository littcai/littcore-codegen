package com.litt.core.codegen.common;

/** 
 * 
 * 常量.
 * 
 * <pre><b>描述：</b>
 *    TODO 
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    TODO
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-9-21
 * @version 1.0
 *
 */
public class GenConstants
{	
	public enum DatabaseType {
		oracle, sqlserver, mysql
	}
	
	public enum LangType {
		en, zh_CN
	}
	
	public static String TAG_NAME_DOMAIN = "domain";
	public static String TAG_NAME_MODULE = "module";
	public static String TAG_NAME_FUNC = "func";
}
