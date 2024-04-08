package net.elghz.siteservice.export;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import net.elghz.siteservice.dtos.typeActiviteDTO;

import javax.swing.text.AbstractDocument;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class ActivitiePDFExporter extends  AbstractExporter{
    public void export(List<typeActiviteDTO> activiteDTOS, HttpServletResponse servletResponse) throws IOException {
    super.setResponseHeader("Les activités",servletResponse, "application/pdf", ".pdf");

        Document document = new Document(PageSize.A4) ;
        PdfWriter.getInstance(document, servletResponse.getOutputStream());
        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);
        Paragraph paragraph = new Paragraph("La liste des activités", font);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(70f);
        table.setSpacingBefore(10);
        table.setWidths(new float[]{0.5f,7.5f});

        writeTableHeader(table);
        writeTableData(table, activiteDTOS);
        document.add(table);
        document.close();




    }

    private void writeTableData(PdfPTable table, List<typeActiviteDTO> activiteDTOS) {

        for(typeActiviteDTO dto :activiteDTOS){
            table.addCell(String.valueOf(dto.getId()));
            table.addCell(dto.getName());
        }
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.lightGray);
        cell.setPadding(5);
        Font font = FontFactory.getFont(FontFactory.HELVETICA);

        font.setColor(Color.WHITE);
        cell.setPhrase(new Phrase("ID" , font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Type activité" , font));
        table.addCell(cell);


    }
}
