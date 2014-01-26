package ${dictModule.packageName};

import java.io.Serializable;

/**
 * 
 * .
 * <pre><b>Description：</b>
 *    
 * </pre>
 * 
 * @author ${gobal.author}
 * @since ${gobal.date}
 * @version 1.0
 */
public enum ${dictParamType.enumName} implements Serializable
{
	<#list dictParamType.dictParamList as dictParam>	
	<#if dictParam_index!=0>,</#if>${dictParam.name}(${dictParam.dictValue}, "${dictParam.dictContent}")
	</#list>
	;
    
    private int value;    
    
    private String descr;
    
    private ${dictParamType.enumName}(int value, String descr)
	{
		this.value = value;
		this.descr = descr;
	}
	
	public static ${dictParamType.enumName} valueOf(int value)
    {
    	switch (value) {
    		<#list dictParamType.dictParamList as dictParam>	
    		case ${dictParam.dictValue}:
    			return ${dictParam.name};
    		</#list>
    		default:
    			throw new IllegalArgumentException("Unknown value:"+value);
    	}	
    }    
    
    /**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @return the descr
	 */
	public String getDescr() {
		return descr;
	}
	
}