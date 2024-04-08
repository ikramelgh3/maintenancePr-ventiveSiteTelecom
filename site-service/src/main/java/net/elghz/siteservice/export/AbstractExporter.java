package net.elghz.siteservice.export;

import jakarta.servlet.http.HttpServletResponse;
import net.elghz.siteservice.dtos.typeActiviteDTO;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AbstractExporter {

    public void setResponseHeader( String nom,HttpServletResponse respone , String contentType, String extension)throws IOException {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-d");
        String timestamp = dateFormatter.format(new Date());
        String fileName = nom +timestamp + extension;
        respone.setContentType(contentType);
        String headerKey = "Content-Disposition";
        String  headerValue = "attachment; filename=" +fileName;
        respone.setHeader(headerKey, headerValue);
    }
}
