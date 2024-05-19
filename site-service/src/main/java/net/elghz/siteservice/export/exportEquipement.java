package net.elghz.siteservice.export;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import net.elghz.siteservice.entities.equipement;
import net.elghz.siteservice.service.CTService;
import net.elghz.siteservice.service.DCService;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

public class exportEquipement extends AbstractExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public  exportEquipement(){
        workbook = new XSSFWorkbook();
    }

    private void createCell(XSSFRow row, int columnIndex, String value, CellStyle style) {
        XSSFCell cell = row.createCell(columnIndex);
        sheet.autoSizeColumn(columnIndex);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }





    public void exportEquipement(List<equipement> sitesDTO, HttpServletResponse response) throws IOException {
        super.setResponseHeader("Les équipements", response, "application/octet-stream", ".xlsx");

        sheet = workbook.createSheet("Les équipements");
        XSSFRow headerRow = sheet.createRow(0);
        XSSFCellStyle headerCellStyle = workbook.createCellStyle();
        XSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeight(16);
        headerCellStyle.setFont(headerFont);

        int columnIndex = 0;
        createCell(headerRow, columnIndex++, "code", headerCellStyle);
        createCell(headerRow, columnIndex++, "name", headerCellStyle);
        createCell(headerRow, columnIndex++, "Type equipement", headerCellStyle);
        createCell(headerRow, columnIndex++, "Numéro de série", headerCellStyle);
        createCell(headerRow, columnIndex++, "Marque", headerCellStyle);
        createCell(headerRow, columnIndex++, "Statut", headerCellStyle);
        createCell(headerRow, columnIndex++, "Date de mise en service", headerCellStyle);
        createCell(headerRow, columnIndex++, "Description", headerCellStyle);
        createCell(headerRow, columnIndex++, "Site", headerCellStyle);
        createCell(headerRow, columnIndex++, "Immuble", headerCellStyle);
        createCell(headerRow, columnIndex++, "Etage", headerCellStyle);
        createCell(headerRow, columnIndex++, "Salle", headerCellStyle);


        int rowIndex = 1;
        XSSFCellStyle dataCellStyle = workbook.createCellStyle();
        XSSFFont dataFont = workbook.createFont();
        dataFont.setFontHeight(14);
        dataCellStyle.setFont(dataFont);

        for (equipement siteDTO : sitesDTO) {
            XSSFRow row = sheet.createRow(rowIndex++);
            if (siteDTO != null) {
                createCell(row, 0, siteDTO.getCode(), dataCellStyle);
                createCell(row, 1, siteDTO.getNom(), dataCellStyle);
                createCell(row, 2, siteDTO.getTypeEquipementt().getName(), dataCellStyle);
                createCell(row, 3, siteDTO.getNumeroSerie(), dataCellStyle);
                createCell(row, 4, siteDTO.getMarque(), dataCellStyle);
                createCell(row, 5, String.valueOf(siteDTO.getStatut()), dataCellStyle);
                createCell(row, 6, String.valueOf(siteDTO.getDateMiseService()), dataCellStyle);
                createCell(row, 7, siteDTO.getDescreption(), dataCellStyle);
                createCell(row, 8, siteDTO.getSalle().getEtage().getImmuble().getSite().getName(), dataCellStyle);
                createCell(row, 9, siteDTO.getSalle().getEtage().getImmuble().getName() , dataCellStyle);
                createCell(row, 10,  siteDTO.getSalle().getEtage().getCodeEtage(), dataCellStyle);
                createCell(row, 11,  siteDTO.getSalle().getCodeSalle(), dataCellStyle);

            }
        }
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
