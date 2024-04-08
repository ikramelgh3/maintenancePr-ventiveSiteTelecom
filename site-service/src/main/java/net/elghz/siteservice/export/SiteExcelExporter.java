package net.elghz.siteservice.export;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import net.elghz.siteservice.controller.DCController;
import net.elghz.siteservice.controller.centreTechniqueController;
import net.elghz.siteservice.dtos.attributeDTO;
import net.elghz.siteservice.dtos.categorieDTO;
import net.elghz.siteservice.dtos.siteDTO;
import net.elghz.siteservice.dtos.typeActiviteDTO;
import net.elghz.siteservice.service.CTService;
import net.elghz.siteservice.service.DCService;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
@Component
public class SiteExcelExporter extends AbstractExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    @Autowired  private  final CTService c ;
    @Autowired  private final DCService dcController ;
    public SiteExcelExporter( CTService c ,DCService dcController) {
        workbook = new XSSFWorkbook();
        this.dcController=dcController;
        this.c=c;
    }

    private void createCell(XSSFRow row, int columnIndex, String value, CellStyle style) {
        XSSFCell cell = row.createCell(columnIndex);
        sheet.autoSizeColumn(columnIndex);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }public void export(List<siteDTO> sitesDTO, HttpServletResponse response) throws IOException {
        super.setResponseHeader("Les sites", response, "application/octet-stream", ".xlsx");

        sheet = workbook.createSheet("Les sites");
        XSSFRow headerRow = sheet.createRow(0);
        XSSFCellStyle headerCellStyle = workbook.createCellStyle();
        XSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeight(16);
        headerCellStyle.setFont(headerFont);

        int columnIndex = 0;
        createCell(headerRow, columnIndex++, "code", headerCellStyle);
        createCell(headerRow, columnIndex++, "name", headerCellStyle);
        createCell(headerRow, columnIndex++, "type", headerCellStyle);
        createCell(headerRow, columnIndex++, "Centre Technique", headerCellStyle);
        createCell(headerRow, columnIndex++, "DC", headerCellStyle);
        createCell(headerRow, columnIndex++, "DR", headerCellStyle);

        Set<String> attributeNames = new HashSet<>();
        for (siteDTO site : sitesDTO) {
            for (attributeDTO attribute : site. getAttributs()) {
                attributeNames.add(attribute.getName());
            }
        }
        for (String attributeName : attributeNames) {
            createCell(headerRow, columnIndex++, attributeName, headerCellStyle);
        }

        int rowIndex = 1;
        XSSFCellStyle dataCellStyle = workbook.createCellStyle();
        XSSFFont dataFont = workbook.createFont();
        dataFont.setFontHeight(14);
        dataCellStyle.setFont(dataFont);

        for (siteDTO siteDTO : sitesDTO) {
            XSSFRow row = sheet.createRow(rowIndex++);
            String centreTechniqueName = siteDTO.getCentreTechnique() != null ? siteDTO.getCentreTechnique().getName() : "-";
            String dc = siteDTO.getCentreTechnique() != null ? String.valueOf(c.getDCByCTId(siteDTO.getId())) : "-";
            String dr = "-";
            if (siteDTO.getCentreTechnique() != null) {
                Long id = dcController.DCIdByNamr(dc);
                dr = String.valueOf(dcController.getDRByDC(id));
            }
            createCell(row, 0, String.valueOf(siteDTO.getId()), dataCellStyle);
            createCell(row, 1, siteDTO.getName(), dataCellStyle);
            createCell(row, 2, siteDTO.getType().toString(), dataCellStyle);
            createCell(row, 3, centreTechniqueName, dataCellStyle);
            createCell(row, 4, dc, dataCellStyle);
            createCell(row, 5, dr, dataCellStyle);


            columnIndex = 6;
            for (String attributeName : attributeNames) {
                boolean attributeFound = false;
                for (attributeDTO attribute : siteDTO.getAttributs()) {
                    if (attribute.getName().equals(attributeName)) {
                        createCell(row, columnIndex++, attribute.getAttributeValue(), dataCellStyle);
                        attributeFound = true;
                        break;
                    }
                }
                if (!attributeFound) {
                    createCell(row, columnIndex++, "", dataCellStyle);
                }
            }
        }

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

}