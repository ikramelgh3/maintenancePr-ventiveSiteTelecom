package net.elghz.siteservice.export;

import jakarta.servlet.http.HttpServletResponse;
import net.elghz.siteservice.dtos.typeActiviteDTO;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

public class ActivitieCSVExporter  extends  AbstractExporter{


    public void export(List<typeActiviteDTO> activiteDTOS, HttpServletResponse servletResponse) throws IOException {

        super.setResponseHeader("Les activités",servletResponse, "text/csv", ".csv");

        Writer writer = new OutputStreamWriter(servletResponse.getOutputStream(), "utf-8");
        writer.write('\uFEFF');

        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"ID", "Type activité"};
        String[] fieldMapping = {"id", "name"};

        for(typeActiviteDTO dto : activiteDTOS){
            csvBeanWriter.write(dto,fieldMapping);
        }

        csvBeanWriter.close();

    }
}
