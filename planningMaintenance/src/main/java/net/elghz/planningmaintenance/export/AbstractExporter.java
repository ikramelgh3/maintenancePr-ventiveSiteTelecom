package net.elghz.planningmaintenance.export;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AbstractExporter {

    public void setResponseHeader(String nom, HttpServletResponse response, String contentType, String extension) throws IOException {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-d");
        String timestamp = dateFormatter.format(new Date());
        String fileName = nom + timestamp + extension;
        response.setContentType(contentType);
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=\"" + fileName + "\"";
        response.setHeader(headerKey, headerValue);
    }

}
