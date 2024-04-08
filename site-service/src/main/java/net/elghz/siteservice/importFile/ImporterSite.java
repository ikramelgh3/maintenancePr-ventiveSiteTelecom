package net.elghz.siteservice.importFile;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import net.elghz.siteservice.dtos.attributeDTO;
import net.elghz.siteservice.dtos.siteDTO;
import net.elghz.siteservice.dtos.typeActiviteDTO;
import net.elghz.siteservice.entities.*;
import net.elghz.siteservice.enumeration.SiteType;
import net.elghz.siteservice.exception.ActiviteNotFoundException;
import net.elghz.siteservice.mapper.typeActiviteMapper;
import net.elghz.siteservice.repository.*;
import net.elghz.siteservice.service.ActiviteService;
import net.elghz.siteservice.service.CTService;
import net.elghz.siteservice.service.DCService;
import net.elghz.siteservice.service.DRService;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
public class ImporterSite {
    @Autowired
    private SiteRepository repo;
    @Autowired
    private attributeRepo aprepo;
    @Autowired
    DCRepo dcRepo;
@Autowired  ActiviteRepo acrepo;
    @Autowired
    ActiviteService activiteService; @Autowired
    typeActiviteMapper amapper;
    @Autowired
    CategorieRepo categorieRepo;
    @Autowired
    DRRepo drRepo;
    @Autowired
    CTRepo ctrepo;
    @Autowired
    CategorieRepo catrepo;
    @Autowired
    DCService dcService;
    @Autowired
    CTService ctService;

    @Transactional
    public void importSites(InputStream inputStream) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next(); // Pass the header row

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Site site = new Site();

                // Code to set ID if needed

                Cell cell = row.getCell(1); // Name
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String siteName = cell.getStringCellValue();
                    // Vérifie si un site avec le même nom existe déjà dans la base de données
                    if (repo.existsByName(siteName)) {
                        throw new DataIntegrityViolationException("Un site avec le nom '" + siteName + "' existe déjà dans la base de données.");
                    }
                    site.setName(siteName);
                }

                cell = row.getCell(2); // Type
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String type = cell.getStringCellValue();
                    site.setType(SiteType.valueOf(type.toUpperCase()));
                }

                // Centre technique
                cell = row.getCell(3); // Centre technique
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String centreTechniqueName = cell.getStringCellValue();
                    Optional<CentreTechnique> optionalCentreTechnique = ctrepo.findByName(centreTechniqueName);

                    if (optionalCentreTechnique.isPresent()) {
                        CentreTechnique centreTechnique = optionalCentreTechnique.get();
                        site.setCentreTechnique(centreTechnique);
                    }
                }

                // DC
                cell = row.getCell(4); // DC
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String dcName = cell.getStringCellValue();
                    Optional<DC> optionalDC = dcRepo.findByName(dcName);

                    if (optionalDC.isPresent()) {
                        DC dc = optionalDC.get();
                        if(site.getCentreTechnique() != null) {
                            site.getCentreTechnique().setDc(dc);
                        }
                    }
                }

                // DR
                cell = row.getCell(5); // DR
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String drName = cell.getStringCellValue();
                    Optional<DR> optionalDR = drRepo.findByName(drName);

                    if (optionalDR.isPresent()) {
                        DR dr = optionalDR.get();
                        if(site.getCentreTechnique() != null && site.getCentreTechnique().getDc() != null) {
                            site.getCentreTechnique().getDc().setDr(dr);
                        }
                    }
                }

                // Save the site
                repo.save(site);
            }
        }
    }



    @Transactional
    public void importSitesParTypeActivite(InputStream inputStream , Long id) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {

            if(acrepo.findById(id).isPresent()){
                TypeActivite typeActivite = acrepo.findById(id).get();

                Sheet sheet = workbook.getSheetAt(0);


                Iterator<Row> rowIterator = sheet.iterator();
                rowIterator.next(); // Pass the header row

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Site site = new Site();
                    site.addTypeActivite(typeActivite);
                    // Code to set ID if needed

                    Cell cell = row.getCell(1); // Name
                    if (cell != null && cell.getCellType() == CellType.STRING) {
                        String siteName = cell.getStringCellValue();
                        // Vérifie si un site avec le même nom existe déjà dans la base de données
                        if (repo.existsByName(siteName)) {
                            throw new DataIntegrityViolationException("Un site avec le nom '" + siteName + "' existe déjà dans la base de données.");
                        }
                        site.setName(siteName);
                    }

                    cell = row.getCell(2); // Type
                    if (cell != null && cell.getCellType() == CellType.STRING) {
                        String type = cell.getStringCellValue();
                        site.setType(SiteType.valueOf(type.toUpperCase()));
                    }

                    // Centre technique
                    cell = row.getCell(3); // Centre technique
                    if (cell != null && cell.getCellType() == CellType.STRING) {
                        String centreTechniqueName = cell.getStringCellValue();
                        Optional<CentreTechnique> optionalCentreTechnique = ctrepo.findByName(centreTechniqueName);

                        if (optionalCentreTechnique.isPresent()) {
                            CentreTechnique centreTechnique = optionalCentreTechnique.get();
                            site.setCentreTechnique(centreTechnique);
                        }
                    }

                    // DC
                    cell = row.getCell(4); // DC
                    if (cell != null && cell.getCellType() == CellType.STRING) {
                        String dcName = cell.getStringCellValue();
                        Optional<DC> optionalDC = dcRepo.findByName(dcName);

                        if (optionalDC.isPresent()) {
                            DC dc = optionalDC.get();
                            if(site.getCentreTechnique() != null) {
                                site.getCentreTechnique().setDc(dc);
                            }
                        }
                    }

                    // DR
                    cell = row.getCell(5); // DR
                    if (cell != null && cell.getCellType() == CellType.STRING) {
                        String drName = cell.getStringCellValue();
                        Optional<DR> optionalDR = drRepo.findByName(drName);

                        if (optionalDR.isPresent()) {
                            DR dr = optionalDR.get();
                            if(site.getCentreTechnique() != null && site.getCentreTechnique().getDc() != null) {
                                site.getCentreTechnique().getDc().setDr(dr);

                            }
                        }
                    }

                    // Save the site
                    repo.save(site);
            }


            }else{
                throw new ActiviteNotFoundException("Aucune activite trouvée avec l'id : " + id);

            }
        }
    }

}
