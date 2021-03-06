package com.littcore.codegen.model;

import java.util.ArrayList;
import java.util.List;

import com.littcore.common.Utility;
import com.littcore.util.StringUtils;
import com.littcore.util.ValidateUtils;

/**
 * .
 * 
 * <pre><b>Description：</b>
 *    
 * </pre>
 * 
 * <pre><b>Changelog：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">Bob.cai</a>
 * @since 2012-1-5
 * @version 1.0
 */
public class Domain {
	
	private String code;
	
	private String fullCode; 
	
	/**
	 * 显示名称
	 */
	private String title;
	
	private String descr;
	
	private String menuName;
	
	/** 是否生成菜单. */
	private Boolean isMenu = true;
	
	/** 是否隐藏. */
	private Boolean isHide = false;
	
	private String packageName;
	
	/**
	 * The parent package.
	 */
	private String parentPackageName;
	
	
	private int position;
	
	private String icon;
	
	private String image;
	
	/**
	 * 子节点列表.
	 * Domain或Module
	 */
	private List subList = new ArrayList();
	
	public boolean validate()
	{
		if(ValidateUtils.isEmpty(code))
			throw new IllegalStateException("field code can not be null.");
		if(ValidateUtils.isEmpty(title))
			throw new IllegalStateException("field title can not be null.");
		
		return true;
	}
	
	public int getStatus()
	{
		if(isHide)
			return 2;
		else 
			return 1;
	}
	
	public boolean getIsDomain()
	{
		return true;
	}
	
	
	/**
	 * 获得模块的相对路径
	 * @return
	 */
	public String getDomainPath()
	{
		StringBuffer ret = new StringBuffer();
		ret.append(StringUtils.replace(parentPackageName, ".", Utility.SEPARATOR));
		ret.append(Utility.SEPARATOR);
		ret.append(this.packageName);
		return ret.toString();		
	}
	
	public void addSub(Domain domain)
	{
		subList.add(domain);
	}
	
	public void addSub(Module module)
	{
		subList.add(module);
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * @return the parentPackage
	 */
	public String getParentPackageName() {
		return parentPackageName;
	}

	/**
	 * @param parentPackage the parentPackage to set
	 */
	public void setParentPackageName(String parentPackage) {
		this.parentPackageName = parentPackage;
	}

	/**
	 * @return the subList
	 */
	public List getSubList() {
		return subList;
	}

	/**
	 * @param subList the subList to set
	 */
	public void setSubList(List subList) {
		this.subList = subList;
	}

	/**
	 * @return the descr
	 */
	public String getDescr() {
		return descr;
	}

	/**
	 * @param descr the descr to set
	 */
	public void setDescr(String descr) {
		this.descr = descr;
	}

	/**
	 * @return the menuName
	 */
	public String getMenuName() {
		return menuName;
	}

	/**
	 * @param menuName the menuName to set
	 */
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	/**
	 * @return the isMenu
	 */
	public Boolean getIsMenu() {
		return isMenu;
	}

	/**
	 * @param isMenu the isMenu to set
	 */
	public void setIsMenu(Boolean isMenu) {
		this.isMenu = isMenu;
	}

	/**
	 * @return the isHide
	 */
	public Boolean getIsHide() {
		return isHide;
	}

	/**
	 * @param isHide the isHide to set
	 */
	public void setIsHide(Boolean isHide) {
		this.isHide = isHide;
	}

	/**
	 * @return the fullCode
	 */
	public String getFullCode() {
		return fullCode;
	}

	/**
	 * @param fullCode the fullCode to set
	 */
	public void setFullCode(String fullCode) {
		this.fullCode = fullCode;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}

	
}
