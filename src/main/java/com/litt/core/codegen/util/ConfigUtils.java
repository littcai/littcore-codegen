package com.litt.core.codegen.util;

import java.io.File;
import java.io.FileReader;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

import com.litt.core.util.ResourceUtils;

/**
 * 配置辅助工具.
 * 
 * <pre><b>描述：</b>
 *    更新配置文件
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2011-3-2
 * @version 1.0
 */
public class ConfigUtils {
	
	/**
	 * 通过castor组件加载配置文件.
	 * @param <T> 配置文件泛型
	 * @param clazz 配置文件类
	 * @param mappingFilePath 映射文件路径
	 * @param confFilePath 配置文件路径
	 * @return 配置实例
	 */
	public static <T> T loadByCastor(Class<T> clazz, String mappingFilePath, String confFilePath)
	{
		try {
			File confMappingFile = ResourceUtils.getFile(mappingFilePath);
			File confFile = ResourceUtils.getFile(confFilePath);
			
			Mapping mapping = new Mapping();
			InputSource is = new InputSource(new FileReader(confMappingFile));
			mapping.loadMapping(is);
			FileReader reader = new FileReader(confFile);
			Unmarshaller unmarshaller = new Unmarshaller(clazz);
			unmarshaller.setMapping(mapping);
			T config = (T)unmarshaller.unmarshal(reader);
			return config;
		} catch (Exception e) {
			throw new RuntimeException("加载配置文件异常！", e);
		} 
	}

}
