package net.elghz.checklistservice.services;

import jakarta.transaction.Transactional;
import net.elghz.checklistservice.dtos.PointMesureDTO;
import net.elghz.checklistservice.entities.PointMesure;
import net.elghz.checklistservice.feign.EquipementRestClient;
import net.elghz.checklistservice.mapper.mapper;
import net.elghz.checklistservice.model.typeEquipement;
import net.elghz.checklistservice.repository.pointMesureRepo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
@Component
public class importerPointsMesures {

    @Autowired
    private pointMesureRepo repo;
    @Autowired
    private mapper mp;
    @Autowired
    private EquipementRestClient rest;
    @Transactional
    public List<PointMesureDTO> importPointsDeMesureFromExcel(InputStream inputStream) throws IOException {
        List<PointMesureDTO> ptsMesures = new ArrayList<>();

        try (Workbook workbook =  WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                String type = row.getCell(0).getStringCellValue().trim();
                typeEquipement t = rest.findByName(type).orElseGet(() -> {
                    typeEquipement newDr = new typeEquipement();
                    newDr.setName(type);
                    return rest.addTypeEquipement(newDr);
                });

                String description = row.getCell(1).getStringCellValue();

                // Vérifier si le point de mesure existe déjà dans la base de données
                PointMesure existingPointMesure = repo.findByAttributAndTypeEquipementId(description, t.getId());

                if (existingPointMesure != null) {
                    // Si le point de mesure existe déjà, passez au suivant
                    continue;
                }

                String resultatsPossibles = row.getCell(2).getStringCellValue();

                PointMesure pointMesure = new PointMesure();
                pointMesure.setAttribut(description);
                pointMesure.setTypeEquipent(t);
                pointMesure.setTypeEquipementId(t.getId());

                List<String> resultatsPossiblesList = new ArrayList<>(Arrays.asList(resultatsPossibles.split("/")));
                pointMesure.setResultatsPossibles(resultatsPossiblesList);
                pointMesure.setRespoMaint(null);
                pointMesure.setRespo_Id(null);

                repo.save(pointMesure);
                ptsMesures.add(mp.from(pointMesure));
            }
        }
        return ptsMesures;
    }
}
