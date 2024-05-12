package net.elghz.planningmaintenance.importFile;

import jakarta.transaction.Transactional;
import net.elghz.planningmaintenance.dto.PlanningMaintenanceDTO;
import net.elghz.planningmaintenance.enumeration.PlanningStatus;
import net.elghz.planningmaintenance.feign.ResponsableMaintRestClient;
import net.elghz.planningmaintenance.feign.SiteRestClient;
import net.elghz.planningmaintenance.mapper.mapper;
import net.elghz.planningmaintenance.repository.planningRepo;
import net.elghz.planningmaintenance.service.planningService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Component
public class ImporterPlanning {
    @Autowired
    private planningRepo repo;
    @Autowired
    private SiteRestClient srepo;
    @Autowired
    ResponsableMaintRestClient rrepo;
    @Autowired
    planningService service;
    @Autowired
    mapper mp;
    @Transactional

    public List<PlanningMaintenanceDTO> importPlanningd(InputStream inputStream) throws IOException {
        List<PlanningMaintenanceDTO> planningMaintenances = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                PlanningMaintenanceDTO site = new PlanningMaintenanceDTO();

                // Code to set ID if needed

                Cell cell = row.getCell(1);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String siteName = cell.getStringCellValue();
                    if (repo.existsByName(siteName)) {
                        throw new DataIntegrityViolationException("Un Planning avec le nom '" + siteName + "' existe déjà dans la base de données.");
                    }
                    site.setName(siteName);
                }

                cell = row.getCell(2);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String type = cell.getStringCellValue();
                    site.setSemestre(type);
                }
                cell = row.getCell(3);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String des = cell.getStringCellValue();
                    site.setDescription(des);
                }

                cell = row.getCell(4);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String res = cell.getStringCellValue();
                    site.setResponsableMaint(rrepo.findByName(res));
                    site.setId_Respo(rrepo.findByName(res).getId());
                }

                cell = row.getCell(5);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String st= cell.getStringCellValue();
                    site.setSite(srepo.getSiteByNamr(st));
                    site.setId_Site(srepo.getSiteByNamr(st).getId());
                }


                cell = row.getCell(6);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String dateString = cell.getStringCellValue();
                    try {
                        Date date = dateFormat.parse(dateString);
                        site.setDateCreation(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                cell = row.getCell(7);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String dateString = cell.getStringCellValue();
                    try {
                        Date date = dateFormat.parse(dateString);
                        site.setDateDebutRealisation(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }


                cell = row.getCell(8);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String dateString = cell.getStringCellValue();
                    try {
                        Date date = dateFormat.parse(dateString);
                        site.setDateFinRealisation(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }


                cell = row.getCell(9);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String st= cell.getStringCellValue();
                    site.setStatus(PlanningStatus.valueOf(st));
                }



                repo.save(mp.from(site));
                planningMaintenances.add(site);
            }
        }
        return planningMaintenances;
    }



}
