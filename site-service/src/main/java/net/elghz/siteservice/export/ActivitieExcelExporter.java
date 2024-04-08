package net.elghz.siteservice.export;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import net.elghz.siteservice.dtos.typeActiviteDTO;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ActivitieExcelExporter extends  AbstractExporter {

    private XSSFWorkbook workbook ;
     private XSSFSheet sheet;
     public  ActivitieExcelExporter(){
         workbook = new XSSFWorkbook();
     }
    private  void writeHeaderLine(){

        sheet = workbook.createSheet("Les activités");
        XSSFRow row = sheet.createRow(0);
        XSSFCellStyle cellStyl= workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        cellStyl.setFont(font);
        createCell(row,0,"ID",cellStyl);
        createCell(row,1,"Type Activite",cellStyl);
    }

    private void  createCell(XSSFRow row, int columnIndex, Object value , CellStyle style){
        XSSFCell cell = row.createCell(columnIndex);
        sheet.autoSizeColumn(columnIndex);
        if(value instanceof  Integer)
        cell.setCellValue((Integer)value);
        else if (value instanceof  Boolean){
            cell.setCellValue((Boolean) value);
        }
        else if (value instanceof  Long){
            cell.setCellValue((Long) value);
        }
        else
            cell.setCellValue((String) value);
        cell.setCellStyle(style);

    }
    public void export(List<typeActiviteDTO> activitiesDTOS , HttpServletResponse respone) throws IOException {
        super.setResponseHeader("Les activités",respone,"application/octet-stream", ".xlsx");


        writeHeaderLine();
        writeDataLinr(activitiesDTOS);
        ServletOutputStream outputStream = respone.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    private void writeDataLinr(List<typeActiviteDTO> activitiesDTOS) {
         int rowIndex=1;
        XSSFCellStyle cellStyl= workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        cellStyl.setFont(font);
         for(typeActiviteDTO act :activitiesDTOS){
             XSSFRow row = sheet.createRow(rowIndex++);
             int columnIndex =0;
             createCell(row, columnIndex++, act.getId(), cellStyl);
             createCell(row, columnIndex++, act.getName(), cellStyl);
         }
    }
}
