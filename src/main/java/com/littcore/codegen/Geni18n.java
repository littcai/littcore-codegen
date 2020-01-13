package com.littcore.codegen;

import com.alibaba.fastjson.JSON;
import com.littcore.codegen.common.GenConstants;
import com.littcore.codegen.model.ui.I18n;
import com.littcore.exception.BusiException;
import com.littcore.util.ArrayUtils;
import com.littcore.util.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Geni18n extends BaseGen {

    private File file;

    private Workbook workbook;

    private int sheetNums;
    private String[] sheetNames;

    private String templateFilePath;

    private String targetFilePath;

    public Geni18n(String projectPath, String filePath, String targetFilePath) throws Exception
    {
        super(projectPath);
        this.file = new File(filePath);
        this.targetFilePath = targetFilePath;
    }

    public void prepareData()throws Exception
    {
        if(workbook==null)
        {
            try{
                String suffix = FilenameUtils.getExtension(file.getName());
                FileInputStream fileinputstream = new FileInputStream(file);
                boolean is2007 = "xlsx".equalsIgnoreCase(suffix)?true:false;

                this.workbook = is2007?new XSSFWorkbook(fileinputstream):new HSSFWorkbook(fileinputstream);

                this.sheetNums = this.workbook.getNumberOfSheets();
                for (int i = 0; i < this.sheetNums; i++) {
                    String sheetName = workbook.getSheetName(i);
                    this.sheetNames = (String[]) ArrayUtils.add(sheetNames, sheetName);
                }
            }
            catch(IOException e)
            {
                throw new BusiException("can't open file:"+file.getName());
            }
        }
    }

    public List<I18n> gen() throws Exception
    {
        this.prepareData();

        List<I18n> retList = new ArrayList<I18n>();
        Map<String, I18n> cacheMap = new HashMap<>();

        Sheet sheet = workbook.getSheetAt(0);

        //取第一行标题行
        Row headRow = sheet.getRow(0);
        int colNum = headRow.getLastCellNum();

        int rowNum = sheet.getLastRowNum();
        for (int i = 2; i <= rowNum; i++) {
            Row row = sheet.getRow(i);
            if(row == null)
                continue;
            int cellNum = row.getLastCellNum();
            if(cellNum < colNum)
                continue;

            Cell cell_key = row.getCell(0);
            Cell cell_zh_CN = row.getCell(1);
            Cell cell_en_US = row.getCell(2);

            String key = cell_key.getStringCellValue();
            String zh_CN = cell_zh_CN.getStringCellValue();
            String en_US = cell_en_US.getStringCellValue();

            String prefix = StringUtils.substringBefore(key, ".");
            String suffix = StringUtils.substringAfter(key, ".");
            //第一层
            I18n root = null;
            if(cacheMap.containsKey(prefix))
            {
                root = cacheMap.get(prefix);
            }
            else {
                root = new I18n();
                root.setCode(prefix);
                cacheMap.put(prefix, root);
                retList.add(root);
            }
            //第二层
            root.addSub(root, suffix, zh_CN, en_US);
        }
        super.addProp("rootList", retList);
        this.genFile("i18n/zh-CN.ftl", super.getPropMap(), targetFilePath, "zh-CN.js");
        this.genFile("i18n/en-US.ftl", super.getPropMap(), targetFilePath, "en-US.js");
        return retList;
    }

    public static void main(String[] args) throws Exception
    {
        Geni18n gen = new Geni18n("D:\\CODEGEN\\gsa-cargo", "D:\\i18n.xls", "D:\\");
        List<I18n> rsList = gen.gen();
        System.out.println(JSON.toJSONString(rsList, true));

    }


    public String getTargetFilePath() {
        return targetFilePath;
    }

    public void setTargetFilePath(String targetFilePath) {
        this.targetFilePath = targetFilePath;
    }
}
