import com.pascalx.databaseDocer.Dao.Mysql;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by yanghui10 on 2016/8/19.
 */
public class Test {
    public static void main(String[] args)  throws Exception{

        Workbook wb = new HSSFWorkbook();

        createIndex(wb);

        FileOutputStream fileOut = new FileOutputStream("e://workbook.xls");
        wb.write(fileOut);
        fileOut.close();

    }

    public static void createIndex(Workbook wb){

        CreationHelper createHelper = wb.getCreationHelper();

        Sheet sheet= wb.createSheet("目录");
        Row row1 = sheet.createRow((short)0);


        CellStyle styleHead =wb.createCellStyle();
        styleHead.setAlignment(CellStyle.ALIGN_CENTER);
        styleHead.setFillBackgroundColor(IndexedColors.DARK_GREEN.getIndex());
        styleHead.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        styleHead.setFillPattern(styleHead.SOLID_FOREGROUND);

        Cell cell = row1.createCell(0);
        cell.setCellValue("nnnnnnnnnnnnnnnnnnnnnnnnnnn");
        row1.createCell(1).setCellValue("表名");
        cell.setCellStyle(styleHead);
        sheet.autoSizeColumn(0);

        CellStyle style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);


        ResultSet rs= null;
        try {
            rs = Mysql.query("select table_name,table_comment from information_schema.tables where table_schema='jr_geious' and table_type='base table';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int i=1;
        Row row;
        Sheet sheet1;
        Cell cell0;
        Cell cell1;
       try{
           while (rs.next()) {

               row=sheet.createRow((short)i);
               row.setRowStyle(style);
               cell0=row.createCell(0);
               cell0.setCellValue(i);
//               cell0.setCellStyle(style);
               Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
               String tableName=rs.getString(1);
               String tableComment=rs.getString(2);

               sheet1=wb.createSheet(tableName+"("+tableComment+")");
//               createTable(sheet1,createHelper,tableName);
               cell1=row.createCell(1);
               cell1.setCellValue(tableName+"("+tableComment+")");
               link.setAddress("'"+tableName+"("+tableComment+")"+"'!A1");
               cell1.setHyperlink(link);
//               cell1.setCellStyle(style);


               i++;
           }
       }catch (Exception e) {
           e.printStackTrace();
       }
    }

    public static void createTable(Sheet sheet, CreationHelper createHelper,String tableName){
        Row row1 = sheet.createRow((short)0);
        row1.createCell(0).setCellValue("序号");
        row1.createCell(1).setCellValue("字段名");
        row1.createCell(2).setCellValue("注释");
        row1.createCell(3).setCellValue("数据类型");
        row1.createCell(4).setCellValue("必填");
        row1.createCell(5).setCellValue("主键字段");
        row1.createCell(6).setCellValue("默认值");
        row1.createCell(7).setCellValue("备注");
        Cell cell8=row1.createCell(8);
        cell8.setCellValue("返回主目录");
        Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
        link.setAddress("'目录'!A1");
        cell8.setHyperlink(link);

        ResultSet rs= null;
        try {
            rs = Mysql.query("select column_name,column_comment,column_type,is_nullable,column_key,column_default,extra from information_schema.columns where table_schema='jr_geious' and table_name='"+tableName+"'");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int i=1;
        Row row;
        try{
            while (rs.next()) {
                row=sheet.createRow((short)i);
                Cell cell0=row.createCell(0);
                cell0.setCellValue(i);

                Cell cell1=row.createCell(1);
                cell1.setCellValue(rs.getString(1));

                Cell cell2=row.createCell(2);
                cell2.setCellValue(rs.getString(2));

                Cell cell3=row.createCell(3);
                cell3.setCellValue(rs.getString(3));

                Cell cell4=row.createCell(4);
                if(rs.getString(4).equals("NO")){
                    cell4.setCellValue("是");
                }else if(rs.getString(4).equals("YES")){
                    cell4.setCellValue("否");
                }


                Cell cell5=row.createCell(5);
                if("PRI".equals(rs.getString("column_key"))){
                    cell5.setCellValue("是");
                }else{
                    cell5.setCellValue("");
                }


                Cell cell6=row.createCell(6);
                if("auto_increment".equals(rs.getString("EXTRA"))){
                    cell6.setCellValue("自增长");
                }else if("CURRENT_TIMESTAMP".equals(rs.getString("COLUMN_DEFAULT"))){
                    cell6.setCellValue("当前系统时间");
                }else {
                    cell6.setCellValue(rs.getString("COLUMN_DEFAULT"));
                }

//
//                Cell cell7=row.createCell(7);
//                cell1.setCellValue(rs.getString(7));

                i++;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
