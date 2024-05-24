package net.elghz.siteservice.importFile;

import net.elghz.siteservice.entities.TypeActivite;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class importTypeActivite {

    public static boolean isValidExcelFile(MultipartFile file){
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" );
    }
    public static List<TypeActivite> getCustomersDataFromExcel(InputStream inputStream){
        List<TypeActivite> customers = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("Les activités");
            int rowIndex =0;
            for (Row row : sheet){
                if (rowIndex ==0){
                    rowIndex++;
                    continue;
                }
                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;
                TypeActivite customer = new TypeActivite();
                while (cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    switch (cellIndex){
                        case 0 -> customer.setId((long) cell.getNumericCellValue());
                        case 1 -> customer.setName(cell.getStringCellValue());
                        default -> {
                        }
                    }
                    cellIndex++;
                }
                customers.add(customer);
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
        return customers;
    }

}