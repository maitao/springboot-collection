package com.mt.collection.processor.csdn;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zsh
 * @site qqzsh.top
 * @company wlgzs
 * @create 2019-04-08 22:18
 * @Description POI工具类
 */
public class POITest {
    public static void printExcel(Map<String,String> map) {
        System.out.println("正在写入Excel...");
        // 定义表头
        String[] title = { "序号", "作者", "链接" };
        // 创建excel工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建工作表sheet
        HSSFSheet sheet = workbook.createSheet();
        // 创建第一行
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = null;
        // 插入第一行数据的表头
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
        }
        int idx = 1;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            HSSFRow nrow = sheet.createRow(idx);
            HSSFCell ncell = nrow.createCell(0);
            ncell.setCellValue(idx++);
            ncell = nrow.createCell(1);
            ncell.setCellValue(entry.getKey());
            ncell = nrow.createCell(2);
            ncell.setCellValue(entry.getValue());
        }
        // 设置自动列宽
        for (int i = 0; i < title.length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 16 / 10);
        }
        // 创建excel文件
        File file = new File("result.xls");
        try {
            file.createNewFile();
            // 将excel写入
            FileOutputStream stream = FileUtils.openOutputStream(file);
            workbook.write(stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("写入完成");
    }

    public static void printExcel2(List<String> list,List<String> url,List<CnBlogs> list2) {
        System.out.println("正在写入Excel...");
        // 定义表头
        String[] title = { "序号","作者","原分类地址", "标题", "博客用户名","发布日期","最近一次博客地址" };
        // 创建excel工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建工作表sheet
        HSSFSheet sheet = workbook.createSheet();
        // 创建第一行
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = null;
        // 插入第一行数据的表头
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
        }
        int idx = 1;
        for (int i = 0;i<list.size();i++){
            CnBlogs cnBlogs = list2.get(i);
            HSSFRow nrow = sheet.createRow(idx);
            HSSFCell ncell = nrow.createCell(0);
            ncell.setCellValue(idx++);
            ncell = nrow.createCell(1);
            ncell.setCellValue(list.get(i));
            ncell = nrow.createCell(2);
            ncell.setCellValue(url.get(i));
            ncell = nrow.createCell(3);
            ncell.setCellValue(cnBlogs.getTitle());
            ncell = nrow.createCell(4);
            ncell.setCellValue(cnBlogs.getAuthor());
            ncell = nrow.createCell(5);
            ncell.setCellValue(cnBlogs.getDateTime());
            ncell = nrow.createCell(6);
            ncell.setCellValue(cnBlogs.getUrl());
        }
        // 设置自动列宽
        for (int i = 0; i < title.length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 16 / 10);
        }
        // 创建excel文件
        File file = new File("result2.xls");
        try {
            file.createNewFile();
            // 将excel写入
            FileOutputStream stream = FileUtils.openOutputStream(file);
            workbook.write(stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("写入完成");
    }

    public static Map<String,String> readExcel() throws IOException {
        Map<String,String> map = new HashMap<>();
        InputStream is=new FileInputStream("result.xls");
        HSSFWorkbook wb=new HSSFWorkbook(is);
        Sheet sheet=wb.getSheetAt(0);
        for (int i = 1; i <5; i++) {
            Row row=sheet.getRow(i);
            String name = "";
            for (Cell cell : row) {
                if (cell.getColumnIndex() == 1){
                    map.put(cell.toString(),"");
                    name = cell.toString();
                }else if (cell.getColumnIndex() == 2){
                    map.remove(name);
                    map.put(name,cell.toString());
                    name = "";
                }
            }
        }
        is.close();
        return map;
    }

    public static void main(String[] args) {
        try {
            Map<String, String> map = readExcel();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                //Map.entry<Integer,String> 映射项（键-值对）  有几个方法：用上面的名字entry
                //entry.getKey() ;entry.getValue(); entry.setValue();
                //map.entrySet()  返回此映射中包含的映射关系的 Set视图。
                System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
