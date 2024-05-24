package net.elghz.siteservice.importFile;

import net.elghz.siteservice.dtos.CentreTechniqueDTO;
import net.elghz.siteservice.entities.CentreTechnique;
import net.elghz.siteservice.entities.DC;
import net.elghz.siteservice.entities.DR;
import net.elghz.siteservice.mapper.CTMapper;
import net.elghz.siteservice.repository.CTRepo;
import net.elghz.siteservice.repository.DCRepo;
import net.elghz.siteservice.repository.DRRepo;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class importerCentre {


    @Autowired
    CTRepo repo;
    @Autowired
    CTMapper mp;
    @Autowired
    DCRepo dcrepo;
    @Autowired
    DRRepo drRepo;

    @Transactional
    public List<CentreTechniqueDTO> importCentreTechniques(InputStream inputStream) throws IOException {
        List<CentreTechnique> localisationsToAdd = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next(); // Pass the header row

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // Traitement du DR
                Cell cell = row.getCell(0);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String drName = cell.getStringCellValue();
                    DR dr = drRepo.findByName(drName).orElseGet(() -> {
                        DR newDr = new DR();
                        newDr.setName(drName);
                        return drRepo.save(newDr);
                    });

                    // Traitement du DC
                    cell = row.getCell(1); // DC
                    final DR finalDr = dr; // Déclarer la variable dr comme finale ou effectivement finale
                    DC dc = null;
                    if (cell != null && cell.getCellType() == CellType.STRING) {
                        String dcName = cell.getStringCellValue();
                        dc = dcrepo.findByName(dcName).orElseGet(() -> {
                            DC newDc = new DC();
                            newDc.setName(dcName);
                            newDc.setDr(finalDr);
                            return dcrepo.save(newDc);
                        });
                    }

                    // Traitement du Centre Technique
                    cell = row.getCell(2); // Centre technique
                    final DC finalDc = dc; // Déclarer la variable dc comme finale ou effectivement finale
                    if (cell != null && cell.getCellType() == CellType.STRING) {
                        String centreTechniqueName = cell.getStringCellValue();
                        CentreTechnique centreTechnique = repo.findByName(centreTechniqueName).orElseGet(() -> {
                            CentreTechnique newCentre = new CentreTechnique();
                            newCentre.setName(centreTechniqueName);
                            newCentre.setDc(finalDc);
                            return repo.save(newCentre);
                        });
                        localisationsToAdd.add(centreTechnique);
                    }
                }
            }

            return localisationsToAdd.stream().map(mp::from).collect(Collectors.toList());
        }
    }
}