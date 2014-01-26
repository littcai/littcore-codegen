package ${domain.parentPackageName}.${domain.packageName}.web;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import ${domain.parentPackageName}.${domain.packageName}.po.${module.className};
import ${domain.parentPackageName}.${domain.packageName}.service.impl.${module.className}ServiceImpl;

import com.litt.core.dao.page.IPageList;
import com.litt.core.common.Utility;
import com.litt.core.dao.ql.PageParam;
import com.litt.core.module.annotation.Func;
import com.litt.core.web.mvc.action.BaseController;
import com.litt.core.web.plugin.jqgrid.GridUtils;

/**
 * 
 * ${module.title} controller.
 * <pre><b>Description：</b>
 *    ${module.descr}
 * </pre>
 * 
 * @author ${gobal.author}
 * @since ${gobal.date}
 * @version 1.0
 */
@Controller
public class ${module.className}Controller extends BaseController
{
	private final static Logger logger = LoggerFactory.getLogger(${module.className}Controller.class);

	@Resource
	private I${module.className}Service ${module.instanceName}Service;
	
	/**
	 * default page.
	 * 
	 * @param request the request
	 * @param respnose the respnose
	 * 
	 * @return ModelAndView
	 */	
	@RequestMapping 
	public ModelAndView index(WebRequest request, ModelMap modelMap)
	{	
		//get params from request
		String name = request.getParameter("name");	
					
		//return params to response
		modelMap.addAttribute("name", name);		
        return new ModelAndView("/${domain.packageName}/${module.instanceName}/list");
	}   
	
	/**
	 * Get Page Grid.
	 * 
	 * @param request the request
	 * @param modelMap the model map
	 * 
	 * @return the model and view
	 */
	@RequestMapping
	public ModelAndView pageGrid(WebRequest request, ModelMap modelMap) 
	{
		//get params from request		
		String name = request.getParameter("name");
		
		//package the params
		PageParam pageParam = GridUtils.getPageParam(request);
		pageParam.addCond("name", name);	
				
		//Get page result
		IPageList page = ${module.instanceName}Service.listPage(pageParam);		
		GridUtils.setGrid(page, modelMap);
		return new ModelAndView("jsonView");		
	}   
	
	/**
	 * Add Page.
	 * 
	 * @return ModelAndView
	 */	
	@RequestMapping
	public ModelAndView add() 
	{        
        return new ModelAndView("/${domain.packageName}/${module.instanceName}/add");
    }
	
	/**
	 * Edit Page.
	 * 
	 * @param id 
	 * 
	 * @return ModelAndView
	 */
	@RequestMapping 
	public ModelAndView edit(@RequestParam Integer id) 
	{ 
		${module.className} ${module.instanceName} = ${module.instanceName}Service.load(id);		
        return new ModelAndView("/${domain.packageName}/${module.instanceName}/edit").addObject("${module.instanceName}", ${module.instanceName});
    }	
    
	/**
	 * Show Page.
	 * 
	 * @param id 
	 * 
	 * @return ModelAndView
	 */
	@RequestMapping 
	public ModelAndView show(@RequestParam Integer id) 
	{ 
		${module.className} ${module.instanceName} = ${module.instanceName}Service.load(id);		
        return new ModelAndView("/${domain.packageName}/${module.instanceName}/show").addObject("${module.instanceName}", ${module.instanceName});
    }   
    
    /**
	 * Save.
	 * @param request 
	 * @param modelMap
	 * @throws Exception 
	 */
	@Func(funcCode="01",moduleCode="${module.code}")
	@RequestMapping 
	public void save(WebRequest request, ModelMap modelMap) throws Exception
	{	
		${module.className} ${module.instanceName} = new ${module.className}();
		BeanUtils.populate(${module.instanceName}, request.getParameterMap());			
		${module.instanceName}Service.save(${module.instanceName});
	}
	
	/**
	 * Update.
	 * @param request 
	 * @param modelMap
	 * @throws Exception 
	 */
	@Func(funcCode="02",moduleCode="${module.code}")
	@RequestMapping 
	public void update(WebRequest request, ModelMap modelMap) throws Exception
	{
		${module.className} ${module.instanceName} = ${module.instanceName}Service.load(Utility.parseInt(request.getParameter("id")));
		BeanUtils.populate(${module.instanceName}, request.getParameterMap());
		${module.instanceName}Service.update(${module.instanceName});
	}
	
	/**
	 * Delete.
	 * @param id id
	 * @throws Exception 
	 */
	@Func(funcCode="03",moduleCode="${module.code}")
	@RequestMapping 
	public void delete(@RequestParam Integer id) throws Exception
	{
		${module.instanceName}Service.delete(id);
	}


}
