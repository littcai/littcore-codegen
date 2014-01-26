package ${domain.parentPackageName}.${domain.packageName}.dao;

import org.slf4j.Logger;     
import org.slf4j.LoggerFactory;  

import com.litt.core.dao.GenericHibernateDao;

import ${domain.parentPackageName}.${domain.packageName}.po.${module.className};

/**
 * 
 * ${module.title} Dao.
 * <pre><b>Description：</b>
 *    ${module.descr}
 * </pre>
 * 
 * @author ${gobal.author}
 * @since ${gobal.date}
 * @version 1.0
 */
public class ${module.className}Dao extends GenericHibernateDao<${module.className}, Integer>
{
	/** Logger. */
    private static final Logger logger = LoggerFactory.getLogger(${module.className}Dao.class);    
    

}