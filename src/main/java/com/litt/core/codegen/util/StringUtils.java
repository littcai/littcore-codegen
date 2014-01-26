package com.litt.core.codegen.util;

import com.litt.core.util.RegexUtils;

/** 
 * 
 * TODO.
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
 * @since Nov 19, 2008
 * @version 1.0
 *
 */
public final class StringUtils
{
	private StringUtils(){};
	
	/**
	 * 驼峰命名法转横线式.
	 * 主要用于生成JSP的文件名
	 * @param string
	 * @return
	 */
	public static String unhump(String string)
	{
		StringBuffer stringBufer = new StringBuffer();
		for(int i=0;i<string.length();i++)
		{
			String one = String.valueOf(string.charAt(i));
			if(RegexUtils.validate(one, RegexUtils.CAPITAL_LETTER_REGEXP))	//大写字母
			{
				stringBufer.append("_"+one.toLowerCase());
			}
			else
				stringBufer.append(one);
		}
		return stringBufer.toString();
	}
	
	/**
	 * 横线式转驼峰命名法.
	 * 
	 * @param string the string
	 * @param firstUpperCase the first upper case
	 * 
	 * @return the string
	 */
	public static String hump(String string,boolean firstUpperCase)
	{
		String temp = string.toLowerCase();
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<temp.length();i++)
		{
			String one = String.valueOf(temp.charAt(i));		
			if(i==0&&firstUpperCase)	//第一位大写
			{
				one = one.toUpperCase();
				sb.append(one);
			}
			else
			{
				if("_".equals(one))	
				{
					continue;					
				}
				else if(i>0&&temp.charAt(i-1)=='_')	
					sb.append(one.toUpperCase());
				else
					sb.append(one);
			}
			
		}
		return sb.toString();
	}	
	
	/**
	 * 文本转UNICODE.
	 *
	 * @param text the text
	 * @return the string
	 */
	public static String toUnicode(String text)
	{
		int bufLen = text.length() * 2;
		StringBuilder sb = new StringBuilder(bufLen);
		for (int i = 0; i < text.length(); i++) {
			char chr = (char) text.charAt(i);  
			
			if ((chr > 61) && (chr < 127)) {
                if (chr == '\\') {
                    sb.append('\\');
                    sb.append('\\');
                    continue;
                }
                sb.append(chr);
                continue;
            }
			
			
			if(isChinese(chr))
				sb.append("\\u" +  Integer.toHexString(text.charAt(i) & 0xffff));
			else
				sb.append(chr);
		}
		return sb.toString();
	}
	
	/** 
     * 判断是否为中文字符 
     * @param c 
     * @return 
     */  
    public static boolean isChinese(char c) {  
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);  
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS  
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS  
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A  
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION  
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION  
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {  
            return true;  
        }  
        return false;  
    }  
	
}
