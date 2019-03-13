package com.pascalx.databaseDocer.poi;

import com.pascalx.databaseDocer.Dao.Mysql;
import com.pascalx.databaseDocer.View.MainFrame;
import com.pascalx.databaseDocer.entity.ColumnInfo;
import com.pascalx.databaseDocer.entity.RemarkInfo;
import com.pascalx.databaseDocer.entity.TableInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by yanghui10 on 2016/8/20.
 */
public class ExcelHelper {

    Workbook wb = new HSSFWorkbook();
    String path;
    CreationHelper createHelper = wb.getCreationHelper();
    FileOutputStream fileOut;

    CellStyle styleNormal=this.getStyleNormal();
    CellStyle headStyle=this.getHeadStyle();
    CellStyle linkStyle=this.getLinkedStyle();

    public void export(Map<TableInfo, List<ColumnInfo>> tableStructures,String path)throws Exception{
        StringBuilder exportingInfo=new StringBuilder();
        this.path=path;
        fileOut = new FileOutputStream(path);
        Sheet indexSheet=wb.createSheet("目录");
        createHeadRow(indexSheet,this.getHeadStyle(),"序号","表名");
        int index=1;
        for(Map.Entry<TableInfo, List<ColumnInfo>> tableStructure:tableStructures.entrySet()){
            String tbNameAndComment=tableStructure.getKey().getTableName()+"("+tableStructure.getKey().getTableComment()+")";
            exportingInfo.append("\n\n正在生成sheet: "+tbNameAndComment+"..........\n");
            Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
            link.setAddress("'"+tbNameAndComment+"'!A1");
            createRow(indexSheet,(short)index,link,1,this.styleNormal,String.valueOf(index)/*序号*/,tbNameAndComment);

//            style.setAlignment(CellStyle.ALIGN_CENTER);

            this.createSheet(tableStructure.getKey(),exportingInfo,index);

            index++;
        }
        indexSheet.autoSizeColumn(0);
        indexSheet.autoSizeColumn(1);
        wb.write(fileOut);
        fileOut.close();
    }

    public void createSheet(TableInfo tableInfo,StringBuilder exportingInfo,int index){
        String tbNameAndComment=tableInfo.getTableName()+"("+tableInfo.getTableComment()+")";
        Hyperlink linkIndex=createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
        linkIndex.setAddress("'目录'!A1");
        Sheet sheet=wb.createSheet(tbNameAndComment);
        createHeadRow(sheet,linkIndex,8,this.headStyle,"序号","字段名","注释","数据类型","必填","主键字段","默认值","备注","返回主目录");
        int indexxx=1;
        int count=tableInfo.getColumnInfo().size()+1+3;
        for(ColumnInfo columnInfo:tableInfo.getColumnInfo()){
            String required;
            String priKey;
            String defaultValue;
            if("NO".equals(columnInfo.getIsNullable())){
                required="是";
            }else if("YES".equals(columnInfo.getIsNullable())){
                required="否";
            }else{
                required="";
            }

            if("PRI".equals(columnInfo.getKey())){
                priKey="是";
            }else {
                priKey="";
            }

            if("auto_increment".equals(columnInfo.getExtra())){
                defaultValue="自增长";
            }else if("CURRENT_TIMESTAMP".equals(columnInfo.getDefaultValue())){
                defaultValue="当前系统时间";
            }else {
                defaultValue=columnInfo.getDefaultValue();
            }
            if(columnInfo.getRemarkInfos()!=null){
                Hyperlink linkdesc=createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
                linkdesc.setAddress("'"+sheet.getSheetName()+"'!A"+(count+2));
                createRow(sheet,(short)indexxx,linkdesc,7,this.styleNormal,String.valueOf(indexxx)/*序号*/,columnInfo.getName(),
                        columnInfo.getComment(),columnInfo.getType(),required,priKey,defaultValue,"详细说明");
                this.createDesc(sheet,columnInfo,count);
                count+=(5+columnInfo.getRemarkInfos().size());//下个desc +2表头行+3空行
            }else{
                createRow(sheet,(short)indexxx,this.styleNormal,String.valueOf(indexxx)/*序号*/,columnInfo.getName(),
                        columnInfo.getComment(),columnInfo.getType(),required,priKey,defaultValue," ");
            }
            exportingInfo.append("序号："+indexxx+"\n");
            exportingInfo.append("字段名："+columnInfo.getName()+"\n");
            exportingInfo.append("注释："+columnInfo.getComment()+"\n");
            exportingInfo.append("数据类型："+columnInfo.getType()+"\n");
            exportingInfo.append("必填："+required+"\n");
            exportingInfo.append("主键字段："+priKey+"\n");
            exportingInfo.append("默认值："+defaultValue+"\n");
            exportingInfo.append("备注："+" "+"\n");
            MainFrame.showExportingInfo(index,exportingInfo.toString());
            indexxx++;
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);
        sheet.autoSizeColumn(7);
        sheet.autoSizeColumn(8);
    }

    /**
     * 创建详细说明表格
     */
    public void createDesc(Sheet sheet,ColumnInfo columnInfo,int index){

        createRow(sheet,(short)(index),getStyleNormal(),columnInfo.getComment(),columnInfo.getName());
        createRow(sheet,(short)(index+1),getHeadStyle(),"序号","字段值","描述");
        int xuhao=1;
        for(RemarkInfo remarkInfo:columnInfo.getRemarkInfos()){
            createRow(sheet,(short)(index+1+xuhao),this.styleNormal,String.valueOf(xuhao),String.valueOf(remarkInfo.getCode()),remarkInfo.getDesc());
            xuhao++;
        }
    }
    /**
     * 给指定的sheet创建表头行,表头默认为第0行
     * @param sheet 指定的sheet
     * @param heads 表头内容数组
     */
    private void createHeadRow(Sheet sheet,CellStyle style,String...heads){
        createRow(sheet,(short)0,style,heads);
    }
    private void createHeadRow(Sheet sheet,Hyperlink link,int linkcolumn,CellStyle style,String...heads){
        createRow(sheet,(short)0,link,linkcolumn,style,heads);
    }


    /**
     * 给指定的sheet创建指定第i行
     * @param sheet
     * @param rowIndex
     * @param values
     */
    private void createRow(Sheet sheet,short rowIndex,CellStyle style,String...values){
        Row headRow=sheet.createRow(rowIndex);
        headRow.setHeightInPoints(17);
        for(int i=0;i<values.length;i++){
            createCell(headRow,(short)i,style,values[i]);
        }
    }
    /**
     * 给指定的sheet创建指定第i行
     * @param sheet
     * @param rowIndex
     * @param values
     */
    private void createRow(Sheet sheet,short rowIndex,Hyperlink link,int linkcolumn,CellStyle style,String...values){
        Row headRow=sheet.createRow(rowIndex);
        headRow.setHeightInPoints(17);
        for(int i=0;i<values.length;i++){
            if(i==linkcolumn){
                createCell(headRow,(short)i,values[i],getLinkedStyle(),link);
            }else{
                createCell(headRow,(short)i,style,values[i]);
            }
        }
    }
    private  void createCell(Row row, short column,CellStyle style, String value) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }
    private  void createCell(Row row, short column, String value,CellStyle style,Hyperlink link) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        cell.setHyperlink(link);
        cell.setCellStyle(style);
    }

    private Font getHeadFont(){
        Font font = wb.createFont();
        font.setFontHeightInPoints((short)10);
        font.setFontName("微软雅黑");
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
//        font.setItalic(true);//斜体
//        font.setStrikeout(true);//删除线
        font.setColor(IndexedColors.LIGHT_BLUE.getIndex());
        return font;
    }
    private Font getFontNormal(){
        Font font = wb.createFont();
        font.setFontHeightInPoints((short)10);
        font.setFontName("微软雅黑");
        return font;
    }
    private CellStyle getHeadStyle(){
        CellStyle style =wb.createCellStyle();
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setLeftBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setRightBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setTopBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillBackgroundColor(IndexedColors.DARK_GREEN.getIndex());
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(style.SOLID_FOREGROUND);
        style.setFont(getHeadFont());
        return style;
    }
//    private CellStyle getIndexStyle(){
//        CellStyle style =wb.createCellStyle();
//        style.setBorderBottom(CellStyle.BORDER_THIN);
//        style.setBorderLeft(CellStyle.BORDER_THIN);
//        style.setBorderRight(CellStyle.BORDER_THIN);
//        style.setBorderTop(CellStyle.BORDER_MEDIUM_DASHED);
//        style.setBottomBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
//        style.setLeftBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
//        style.setRightBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
//        style.setTopBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
//        style.setAlignment(CellStyle.ALIGN_CENTER);
//        Font font=getFontNormal();
//        font.setColor(IndexedColors.VIOLET.getIndex());
//        font.setUnderline((byte)1);
//        style.setFont(font);
//
//        return style;
//    }
    private CellStyle getStyleNormal(){
        CellStyle style =wb.createCellStyle();
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setLeftBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setRightBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setTopBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFont(getFontNormal());
        return style;
    }
    private CellStyle getLinkedStyle(){
        CellStyle style =wb.createCellStyle();
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setLeftBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setRightBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setTopBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setAlignment(CellStyle.ALIGN_CENTER);
        Font font=getFontNormal();
        font.setColor(IndexedColors.VIOLET.getIndex());
        font.setUnderline((byte)1);
        style.setFont(font);

        return style;
    }


    public void exportSys_dict_codeNotUse() throws Exception {
        wb=new HSSFWorkbook();
        Sheet sheet=wb.createSheet("未命中的com_type");
        createHeadRow(sheet,this.getHeadStyle(),"序号","com_type");
        int count=1;
        for(String com_type: Mysql.cacheRemarkInfoMap.keySet()){
            createRow(sheet,(short)count,getStyleNormal(),String.valueOf(count),com_type);
            count++;
        }
        FileOutputStream fos=new FileOutputStream("e://未命中的com_type.xls");
        wb.write(fos);
        fos.close();
    }
}
