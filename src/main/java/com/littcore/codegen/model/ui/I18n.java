package com.littcore.codegen.model.ui;

import com.littcore.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class I18n {

    private String code;

    private String zhCN;

    private String enUS;

    private List<I18n> subList = new ArrayList<>();

    Map<String, I18n> cacheMap = new HashMap<>();

    public I18n addSub(I18n parent, String code, String zhCN, String enUS)
    {
        String prefix = StringUtils.substringBefore(code, ".");
        String suffix = StringUtils.substringAfter(code, ".");

        I18n sub = null;
        if(cacheMap.containsKey(prefix))
        {
            sub = cacheMap.get(prefix);
        }
        else {
            sub = new I18n();
            sub.setCode(prefix);
            sub.setZhCN(zhCN);
            sub.setEnUS(enUS);

            parent.subList.add(sub);
            parent.cacheMap.put(prefix, sub);
        }


        if(!StringUtils.isEmpty(suffix))
        {
            this.addSub(sub, suffix, zhCN, enUS);
        }



        return this;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getZhCN() {
        return zhCN;
    }

    public void setZhCN(String zhCN) {
        this.zhCN = zhCN;
    }

    public String getEnUS() {
        return enUS;
    }

    public void setEnUS(String enUS) {
        this.enUS = enUS;
    }

    public List<I18n> getSubList() {
        return subList;
    }

    public void setSubList(List<I18n> subList) {
        this.subList = subList;
    }
}
