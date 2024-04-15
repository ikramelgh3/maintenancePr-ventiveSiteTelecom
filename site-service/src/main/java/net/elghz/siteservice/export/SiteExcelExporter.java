package net.elghz.siteservice.export;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import net.elghz.siteservice.controller.DCController;
import net.elghz.siteservice.controller.centreTechniqueController;
import net.elghz.siteservice.dtos.*;
import net.elghz.siteservice.entities.Site;
import net.elghz.siteservice.entities.SiteFixe;
import net.elghz.siteservice.entities.SiteMobile;
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

    @Autowired
    private final CTService c;
    @Autowired
    private final DCService dcController;

    public SiteExcelExporter(CTService c, DCService dcController) {
        workbook = new XSSFWorkbook();
        this.dcController = dcController;
        this.c = c;
    }

    private void createCell(XSSFRow row, int columnIndex, String value, CellStyle style) {
        XSSFCell cell = row.createCell(columnIndex);
        sheet.autoSizeColumn(columnIndex);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    public void export(List<Site> sitesDTO, HttpServletResponse response) throws IOException {
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
        createCell(headerRow, columnIndex++, "Type", headerCellStyle);
        createCell(headerRow, columnIndex++, "Centre Technique", headerCellStyle);
        createCell(headerRow, columnIndex++, "DC", headerCellStyle);
        createCell(headerRow, columnIndex++, "DR", headerCellStyle);
        createCell(headerRow, columnIndex++, "Adresse", headerCellStyle);
        createCell(headerRow, columnIndex++, "Latitude", headerCellStyle);
        createCell(headerRow, columnIndex++, "Longitude", headerCellStyle);
        createCell(headerRow, columnIndex++, "Type d’installation", headerCellStyle);
        createCell(headerRow, columnIndex++, "Type d'alimentation", headerCellStyle);
        createCell(headerRow, columnIndex++, "Présence GE de secours", headerCellStyle);
        createCell(headerRow, columnIndex++, "Type de transmission ", headerCellStyle);
        createCell(headerRow, columnIndex++, "Support Antennes", headerCellStyle);
        createCell(headerRow, columnIndex++, "Hauteur du support d’antenne ", headerCellStyle);
        createCell(headerRow, columnIndex++, "Lieu d'installation BTS ", headerCellStyle);

        int rowIndex = 1;
        XSSFCellStyle dataCellStyle = workbook.createCellStyle();
        XSSFFont dataFont = workbook.createFont();
        dataFont.setFontHeight(14);
        dataCellStyle.setFont(dataFont);

        for (Site siteDTO : sitesDTO) {
            XSSFRow row = sheet.createRow(rowIndex++);
        /*
        String centreTechniqueName = siteDTO.getCentreTechnique() != null ? siteDTO.getCentreTechnique().getName() : "-";
        String dc = siteDTO.getCentreTechnique() != null ? String.valueOf(c.getDCByCTId(siteDTO.getId())) : "-";
        String dr = "-";
        /*
        if (siteDTO.getCentreTechnique() != null) {
            Long id = dcController.DCIdByNamr(dc);
            dr = String.valueOf(dcController.getDRByDC(id));
        }*/

            if (siteDTO != null) {

                if (siteDTO.getCentreTechnique() != null) {
                    String centreTechniqueName = siteDTO.getCentreTechnique().getName();

                    String dc = String.valueOf(c.getDCFromCT(centreTechniqueName).getName());
                    if (dc != null) {

                        String dr = String.valueOf(dcController.getDRFromDC(dc).getName());

                        if (dr != null) {
                            createCell(row, 0, siteDTO.getCode(), dataCellStyle);
                            createCell(row, 1, siteDTO.getName(), dataCellStyle);
                            createCell(row, 2, siteDTO.getTypeSite(), dataCellStyle);
                            createCell(row, 3, centreTechniqueName, dataCellStyle);
                            createCell(row, 4, dc, dataCellStyle);
                            createCell(row, 5, dr, dataCellStyle);
                            createCell(row, 6, siteDTO.getAddresse(), dataCellStyle);
                            createCell(row, 7, String.valueOf(siteDTO.getLatitude()), dataCellStyle);
                            createCell(row, 8, String.valueOf(siteDTO.getLongitude()), dataCellStyle);
                            createCell(row, 9, siteDTO.getTypeInstallation(), dataCellStyle);
                            createCell(row, 10, siteDTO.getTypeAlimentation(), dataCellStyle);
                            createCell(row, 11, String.valueOf(siteDTO.getPresenceGESecours()), dataCellStyle);
                            createCell(row, 12, siteDTO.getTypeTransmission(), dataCellStyle);

                            if (siteDTO.getTypeSite() != null) {
                                if (siteDTO.getTypeSite().equals("Mobile") && siteDTO instanceof SiteMobile) {
                                    SiteMobile mb = (SiteMobile) siteDTO;
                                    createCell(row, 13, mb.getSupportAntennes(), dataCellStyle);
                                    createCell(row, 14, String.valueOf(mb.getHauteurSupportAntenne()), dataCellStyle);
                                    createCell(row, 15, mb.getLieuInsatallationBTS(), dataCellStyle);
                                } else {

                                    createCell(row, 13, "", dataCellStyle);
                                    createCell(row, 14, "", dataCellStyle);
                                    createCell(row, 15, "", dataCellStyle);
                                }
                            } else {

                                createCell(row, 13, "", dataCellStyle);
                                createCell(row, 14, "", dataCellStyle);
                                createCell(row, 15, "", dataCellStyle);
                            }
                        }
                    }
                }else {


                    createCell(row, 0, siteDTO.getCode(), dataCellStyle);
                    createCell(row, 1, siteDTO.getName(), dataCellStyle);
                    createCell(row, 2, siteDTO.getTypeSite(), dataCellStyle);
                    createCell(row, 3, "", dataCellStyle);
                    createCell(row, 4, "", dataCellStyle);
                    //createCell(row, 5, dr, dataCellStyle);
                    createCell(row, 6, siteDTO.getAddresse(), dataCellStyle);
                    createCell(row, 7, String.valueOf(siteDTO.getLatitude()), dataCellStyle);
                    createCell(row, 8, String.valueOf(siteDTO.getLongitude()), dataCellStyle);
                    createCell(row, 9, siteDTO.getTypeInstallation(), dataCellStyle);
                    createCell(row, 10, siteDTO.getTypeAlimentation(), dataCellStyle);
                    createCell(row, 11, String.valueOf(siteDTO.getPresenceGESecours()), dataCellStyle);
                    createCell(row, 12, siteDTO.getTypeTransmission(), dataCellStyle);

                    if (siteDTO.getTypeSite() != null) {
                        if (siteDTO.getTypeSite().equals("Mobile") && siteDTO instanceof SiteMobile) {
                            SiteMobile mb = (SiteMobile) siteDTO;
                            createCell(row, 13, mb.getSupportAntennes(), dataCellStyle);
                            createCell(row, 14, String.valueOf(mb.getHauteurSupportAntenne()), dataCellStyle);
                            createCell(row, 15, mb.getLieuInsatallationBTS(), dataCellStyle);
                        } else {

                            createCell(row, 13, "", dataCellStyle);
                            createCell(row, 14, "", dataCellStyle);
                            createCell(row, 15, "", dataCellStyle);
                        }
                    } else {

                        createCell(row, 13, "", dataCellStyle);
                        createCell(row, 14, "", dataCellStyle);
                        createCell(row, 15, "", dataCellStyle);
                    }
                    //System.out.println("Le site est NULL");
                }
            }
        }

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

}
