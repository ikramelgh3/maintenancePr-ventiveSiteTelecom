package net.elghz.planningmaintenance.export;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import net.elghz.planningmaintenance.dto.PlanningMaintenanceDTO;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.List;
@Component
public class PlanningsExcelExporter extends AbstractExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public PlanningsExcelExporter() {
        this.workbook = new XSSFWorkbook();
    }
    private String fileName;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    private void createCell(XSSFRow row, int columnIndex, String value, CellStyle style) {
        XSSFCell cell = row.createCell(columnIndex);
        sheet.autoSizeColumn(columnIndex);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    public void export(List<PlanningMaintenanceDTO> planningd, HttpServletResponse response) throws IOException {
        super.setResponseHeader("Les Plannings de maintenance", response, "application/octet-stream", ".xlsx");

        sheet = workbook.createSheet("Les plannings de maintenance");
        XSSFRow headerRow = sheet.createRow(0);
        XSSFCellStyle headerCellStyle = workbook.createCellStyle();
        XSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeight(16);
        headerCellStyle.setFont(headerFont);

        int columnIndex = 0;
        createCell(headerRow, columnIndex++, "ID", headerCellStyle);
        createCell(headerRow, columnIndex++, "Nom", headerCellStyle);
        createCell(headerRow, columnIndex++, "Semestre", headerCellStyle);
        createCell(headerRow, columnIndex++, "Description", headerCellStyle);
        createCell(headerRow, columnIndex++, "Responsable de maintenance", headerCellStyle);
        createCell(headerRow, columnIndex++, "Site", headerCellStyle);
        createCell(headerRow, columnIndex++, "Date de creation", headerCellStyle);
        createCell(headerRow, columnIndex++, "Date debut realisation", headerCellStyle);
        createCell(headerRow, columnIndex++, "Date fin de realisation", headerCellStyle);
        createCell(headerRow, columnIndex++, "Status", headerCellStyle);

        int rowIndex = 1;
        XSSFCellStyle dataCellStyle = workbook.createCellStyle();
        XSSFFont dataFont = workbook.createFont();
        dataFont.setFontHeight(14);
        dataCellStyle.setFont(dataFont);

        for (PlanningMaintenanceDTO pl : planningd) {
            XSSFRow row = sheet.createRow(rowIndex++);
            if (pl != null) {

                            createCell(row, 0, String.valueOf(pl.getId()), dataCellStyle);
                            createCell(row, 1, pl.getName(), dataCellStyle);
                            createCell(row, 2, pl.getSemestre(), dataCellStyle);
                            createCell(row, 3, pl.getDescription(), dataCellStyle);
                            createCell(row, 4, pl.getResponsableMaint().getUserName(), dataCellStyle);
                            createCell(row, 5, pl.getSite().getName(), dataCellStyle);
                            createCell(row, 6, String.valueOf(pl.getDateCreation()), dataCellStyle);
                            createCell(row, 7, String.valueOf(pl.getDateDebutRealisation()), dataCellStyle);
                            createCell(row, 8, String.valueOf(pl.getDateFinRealisation()), dataCellStyle);
                            createCell(row, 9, String.valueOf(pl.getStatus()), dataCellStyle);

                            }
                        }

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }


}
