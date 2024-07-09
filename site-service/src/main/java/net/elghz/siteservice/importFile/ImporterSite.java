package net.elghz.siteservice.importFile;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import java.text.SimpleDateFormat;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import net.elghz.siteservice.dtos.*;
import net.elghz.siteservice.entities.*;
import net.elghz.siteservice.enumeration.SiteType;
import net.elghz.siteservice.enumeration.Statut;
import net.elghz.siteservice.exception.ActiviteNotFoundException;
import net.elghz.siteservice.mapper.equipementMapper;
import net.elghz.siteservice.mapper.siteMapper;
import net.elghz.siteservice.mapper.typeActiviteMapper;
import net.elghz.siteservice.repository.*;
import net.elghz.siteservice.service.*;
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
import java.text.ParseException;
import java.util.*;

@Component
public class ImporterSite {
    @Autowired
    private SiteRepository repo;
    @Autowired
    private attributeRepo aprepo;
    @Autowired
    DCRepo dcRepo;
    @Autowired typeEquipementRepo typeEquipement;
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
    equipementMapper emapper;
    @Autowired
    siteMapper smapper;
    @Autowired
    CategorieRepo catrepo;
    @Autowired
    DCService dcService;
    @Autowired
    CTService ctService;
    @Autowired SalleService salleService;
    @Autowired ImmubleService immubleService;
    @Autowired EtageService etageService;
    @Autowired SalleRepo salleRepo;
    @Autowired ImmubleRepo immubleRepo;
    @Autowired EtageRepo etageRepo;
    @Autowired equipementRepo equipementRepo;
    @Autowired equipementService equipementService;

    @Autowired
    siteService service;

    @Transactional
    public List<equipementDTO> importEquipements(InputStream inputStream) throws IOException {
        List<equipementDTO> equipementDTOS = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next(); // Pass the header row

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                equipement eq = new equipement();


                Cell cell = row.getCell(0);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String num = cell.getStringCellValue().trim().trim();
                    eq.setCode(num);
                }

                cell = row.getCell(1);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String type = cell.getStringCellValue();
                    eq.setNom(type);
                }
                cell = row.getCell(2);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String type = cell.getStringCellValue().trim();
                    eq.setNumeroSerie(type);
                }


                cell = row.getCell(3);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String type = cell.getStringCellValue().trim();
                    eq.setMarque(type);
                }
                cell = row.getCell(5);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String type = cell.getStringCellValue().trim();
                    eq.setDescreption(type);
                }


                cell = row.getCell(4); //typeEquip
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String dcName = cell.getStringCellValue().trim();
                    Optional<typeEquipement> optionalDC = typeEquipement.findByName(dcName);

                    typeEquipement dc;
                    if (optionalDC.isPresent()) {
                        dc = optionalDC.get();
                    } else {
                        // Créer un nouveau DC s'il n'existe pas
                        dc = new typeEquipement();
                        dc.setName(dcName);
                        // Sauvegarder le nouveau DC dans la base de données
                        dc = typeEquipement.save(dc);
                    }
                  eq.setTypeEquipementt(dc);
                }

                cell = row.getCell(6);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String type = cell.getStringCellValue().trim();
                    Statut statut = Statut.fromValue(type);
                    eq.setStatut(statut);
                }



                cell = row.getCell(7); // salle
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String idS  = cell.getStringCellValue().trim();
                    Optional<salle> optionalCentreTechnique = salleRepo.findByCodeSalle(idS);
                    System.out.println(idS);
                    salle centreTechnique;
                    if (optionalCentreTechnique.isPresent()) {
                        centreTechnique = optionalCentreTechnique.get();
                    } else {
                        // Créer un nouveau centre technique s'il n'existe pas
                        centreTechnique = new salle();
                        centreTechnique.setCodeSalle(idS);
                        centreTechnique = salleRepo.save(centreTechnique);
                    }
                    eq.setSalle(centreTechnique);

                    System.out.println(idS);
                }

                // etage
                cell = row.getCell(8); // etage
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String  dcName = cell.getStringCellValue().trim();
                    Optional<etage> optionalDC = etageRepo.findByCodeEtagge(dcName);

                    etage dc;
                    if (optionalDC.isPresent()) {
                        dc = optionalDC.get();
                    } else {
                        // Créer un nouveau DC s'il n'existe pas
                        dc = new etage();
                        dc.setCodeEtagge(dcName);
                        // Sauvegarder le nouveau DC dans la base de données
                        dc = etageRepo.save(dc);
                    }
                    if (eq.getSalle() != null) {
                        eq.getSalle().setEtage(dc);
                        System.out.println(dcName);
                    }
                }

                // imm
                cell = row.getCell(9); //immuble
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String  drName = cell.getStringCellValue().trim();
                    Optional<immuble> optionalDR = immubleRepo.findByCodeImmuble(drName);

                    immuble dr;
                    if (optionalDR.isPresent()) {
                        dr = optionalDR.get();
                    } else {
                        // Créer un nouveau DR s'il n'existe pas
                        dr = new immuble();
                        dr.setCodeImmuble(drName);
                        // Sauvegarder le nouveau DR dans la base de données
                        dr = immubleRepo.save(dr);
                        System.out.println(drName);
                    }
                    if (eq.getSalle() != null && eq.getSalle().getEtage() != null) {
                        eq.getSalle().getEtage().setImmuble(dr);
                    }
                }

                cell = row.getCell(10); //site
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String drName =cell.getStringCellValue().trim();
                    Optional<Site> optionalDR = repo.findByName(drName);

                    Site dr;
                    if (optionalDR.isPresent()) {
                        dr = optionalDR.get();
                    } else {
                        // Créer un nouveau DR s'il n'existe pas
                        dr = new Site();
                        dr.setName(drName);
                        // Sauvegarder le nouveau DR dans la base de données
                        dr = repo.save(dr);
                        System.out.println(drName);
                    }
                    if (eq.getSalle() != null && eq.getSalle().getEtage() != null && eq.getSalle().getEtage().getImmuble()!=null) {
                        eq.getSalle().getEtage().getImmuble().setSite(dr);
                    }
                }

                equipementRepo.save(eq);
                equipementDTOS.add(emapper.from(eq));
            }
        }
        return  equipementDTOS;
    }


    @Transactional
    public void importSitesParTypeActivite(InputStream inputStream, Long id) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {

            Optional<TypeActivite> optionalTypeActivite = acrepo.findById(id);
            if (optionalTypeActivite.isPresent()) {
                TypeActivite typeActivite = optionalTypeActivite.get();

                Sheet sheet = workbook.getSheetAt(0);

                Iterator<Row> rowIterator = sheet.iterator();
                rowIterator.next(); // Pass the header row

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Cell  cell = row.getCell(2);
                    if (cell != null && cell.getCellType() == CellType.STRING) {
                        String typeSite = cell.getStringCellValue();
                        System.out.println(typeSite);
                        if(typeSite.equals("Mobile")){
                            SiteMobile site = new SiteMobile();
                            site.addTypeActivite(typeActivite);
                            service.saveSiteMobile(site);
                            site.setTypeSite(typeSite);

                            cell = row.getCell(1); // Name
                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                String siteName = cell.getStringCellValue();
                                if (repo.existsByName(siteName)) {
                                    throw new DataIntegrityViolationException("Un site avec le nom '" + siteName + "' existe déjà dans la base de données.");
                                }
                                site.setName(siteName);
                                System.out.println(siteName);
                            }

                            cell = row.getCell(0);
                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                String code = cell.getStringCellValue();
                                site.setCode(code);
                            }



                            // Centre technique
                            cell = row.getCell(3
                            ); // Centre technique
                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                String centreTechniqueName = cell.getStringCellValue();
                                Optional<CentreTechnique> optionalCentreTechnique = ctrepo.findByName(centreTechniqueName);

                                CentreTechnique centreTechnique;
                                if (optionalCentreTechnique.isPresent()) {
                                    centreTechnique = optionalCentreTechnique.get();
                                } else {
                                    // Créer un nouveau centre technique s'il n'existe pas
                                    centreTechnique = new CentreTechnique();
                                    centreTechnique.setName(centreTechniqueName);
                                    // Sauvegarder le nouveau centre technique dans la base de données
                                    centreTechnique = ctrepo.save(centreTechnique);
                                }
                                site.setCentreTechnique(centreTechnique);
                                System.out.println(centreTechniqueName);
                            }

                            // DC
                            cell = row.getCell(4); // DC
                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                String dcName = cell.getStringCellValue();
                                Optional<DC> optionalDC = dcRepo.findByName(dcName);

                                DC dc;
                                if (optionalDC.isPresent()) {
                                    dc = optionalDC.get();
                                } else {
                                    // Créer un nouveau DC s'il n'existe pas
                                    dc = new DC();
                                    dc.setName(dcName);
                                    // Sauvegarder le nouveau DC dans la base de données
                                    dc = dcRepo.save(dc);
                                }
                                if (site.getCentreTechnique() != null) {
                                    site.getCentreTechnique().setDc(dc);
                                    System.out.println(dcName
                                    );
                                }
                            }

                            // DR
                            cell = row.getCell(5); // DR
                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                String drName = cell.getStringCellValue();
                                Optional<DR> optionalDR = drRepo.findByName(drName);

                                DR dr;
                                if (optionalDR.isPresent()) {
                                    dr = optionalDR.get();
                                } else {
                                    // Créer un nouveau DR s'il n'existe pas
                                    dr = new DR();
                                    dr.setName(drName);
                                    // Sauvegarder le nouveau DR dans la base de données
                                    dr = drRepo.save(dr);
                                    System.out.println(drName);
                                }
                                if (site.getCentreTechnique() != null && site.getCentreTechnique().getDc() != null) {
                                    site.getCentreTechnique().getDc().setDr(dr);
                                }
                            }

                            cell = row.getCell(6);
                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                String addr = cell.getStringCellValue();
                                site.setAddresse(addr);
                                System.out.println(addr);
                            }
                            cell = row.getCell(7);
                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                String lg = cell.getStringCellValue();
                                site.setLatitude(Double.valueOf(lg));
                            } cell = row.getCell(8);
                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                String longi = cell.getStringCellValue();
                                site.setLongitude(Double.valueOf(longi));
                            } cell = row.getCell(9);
                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                String typeIns = cell.getStringCellValue();
                                site.setTypeInstallation(typeIns);
                            } cell = row.getCell(10);
                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                String type = cell.getStringCellValue();
                                site.setTypeAlimentation(type);
                            } cell = row.getCell(11);
                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                String type = cell.getStringCellValue();
                                site.setPresenceGESecours(Boolean.valueOf(type));
                            } cell = row.getCell(12);
                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                String type = cell.getStringCellValue();
                                site.setTypeTransmission(type);
                            } cell = row.getCell(13);
                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                SiteMobile mb = (SiteMobile) site;
                                String type = cell.getStringCellValue();
                                mb.setSupportAntennes(type);
                            } cell = row.getCell(14);
                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                SiteMobile mb = (SiteMobile) site;
                                String type = cell.getStringCellValue();
                                mb.setHauteurSupportAntenne(Double.valueOf(type));
                                System.out.println(type);
                            } cell = row.getCell(15);
                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                SiteMobile mb = (SiteMobile) site;
                                String type = cell.getStringCellValue();
                                mb.setLieuInsatallationBTS(type);

                            }

                            // Enregistrer le site
                            service.saveSiteMobile(site);
                        }

                        else if
                        (typeSite.equals("Fixe")){
                            SiteFixe site = new SiteFixe();
                            site.addTypeActivite(typeActivite);
                            service.saveSiteFixe(site);
                            site.setTypeSite(typeSite);


                            cell = row.getCell(1); // Name
                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                String siteName = cell.getStringCellValue();
                                if (repo.existsByName(siteName)) {
                                    throw new DataIntegrityViolationException("Un site avec le nom '" + siteName + "' existe déjà dans la base de données.");
                                }
                                site.setName(siteName);
                            }

                            cell = row.getCell(0);
                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                String code = cell.getStringCellValue();
                                site.setCode(code);
                            }



                            // Centre technique
                            cell = row.getCell(3
                            ); // Centre technique
                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                String centreTechniqueName = cell.getStringCellValue();
                                Optional<CentreTechnique> optionalCentreTechnique = ctrepo.findByName(centreTechniqueName);

                                CentreTechnique centreTechnique;
                                if (optionalCentreTechnique.isPresent()) {
                                    centreTechnique = optionalCentreTechnique.get();
                                } else {
                                    // Créer un nouveau centre technique s'il n'existe pas
                                    centreTechnique = new CentreTechnique();
                                    centreTechnique.setName(centreTechniqueName);
                                    // Sauvegarder le nouveau centre technique dans la base de données
                                    centreTechnique = ctrepo.save(centreTechnique);
                                }
                                site.setCentreTechnique(centreTechnique);
                            }

                            // DC
                            cell = row.getCell(4); // DC
                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                String dcName = cell.getStringCellValue();
                                Optional<DC> optionalDC = dcRepo.findByName(dcName);

                                DC dc;
                                if (optionalDC.isPresent()) {
                                    dc = optionalDC.get();
                                } else {
                                    // Créer un nouveau DC s'il n'existe pas
                                    dc = new DC();
                                    dc.setName(dcName);
                                    // Sauvegarder le nouveau DC dans la base de données
                                    dc = dcRepo.save(dc);
                                }
                                if (site.getCentreTechnique() != null) {
                                    site.getCentreTechnique().setDc(dc);
                                }
                            }

                            // DR
                            cell = row.getCell(5); // DR
                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                String drName = cell.getStringCellValue();
                                Optional<DR> optionalDR = drRepo.findByName(drName);

                                DR dr;
                                if (optionalDR.isPresent()) {
                                    dr = optionalDR.get();
                                } else {
                                    // Créer un nouveau DR s'il n'existe pas
                                    dr = new DR();
                                    dr.setName(drName);
                                    // Sauvegarder le nouveau DR dans la base de données
                                    dr = drRepo.save(dr);
                                }
                                if (site.getCentreTechnique() != null && site.getCentreTechnique().getDc() != null) {
                                    site.getCentreTechnique().getDc().setDr(dr);
                                }
                            }

                            cell = row.getCell(6);
                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                String addr = cell.getStringCellValue();
                                site.setAddresse(addr);
                            }
                            cell = row.getCell(7);
                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                String lg = cell.getStringCellValue();
                                site.setLatitude(Double.valueOf(lg));
                            } cell = row.getCell(8);
                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                String longi = cell.getStringCellValue();
                                site.setLongitude(Double.valueOf(longi));
                            } cell = row.getCell(9);
                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                String typeIns = cell.getStringCellValue();
                                site.setTypeInstallation(typeIns);
                            } cell = row.getCell(10);
                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                String type = cell.getStringCellValue();
                                site.setTypeAlimentation(type);
                            } cell = row.getCell(11);
                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                String type = cell.getStringCellValue();
                                site.setPresenceGESecours(Boolean.valueOf(type));
                            } cell = row.getCell(12);
                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                String type = cell.getStringCellValue();
                                site.setTypeTransmission(type);
                            }

                            // Enregistrer le site
                            service.saveSiteFixe(site);
                        }

                        else {
                            System.out.println("siite fixe ");
                        }
                    }




                }

            } else {
                throw new ActiviteNotFoundException("Aucune activité trouvée avec l'id : " + id);
            }
        }
    }


    @Transactional
    public List<siteDTO> importSites(InputStream inputStream) throws IOException {
        List<siteDTO > siteDTOS = new  ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next(); // Pass the header row

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell  cell = row.getCell(2);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String typeSite = cell.getStringCellValue();
                    System.out.println(typeSite);
                    if(typeSite.equals("Mobile")){
                        SiteMobile site = new SiteMobile();
                        service.saveSiteMobile(site);
                        site.setTypeSite(typeSite);


                        cell = row.getCell(1); // Name
                        if (cell != null && cell.getCellType() == CellType.STRING) {
                            String siteName = cell.getStringCellValue();
                            site.setName(siteName);
                        }

                        cell = row.getCell(0);
                        if (cell != null && cell.getCellType() == CellType.STRING) {
                            String code = cell.getStringCellValue();
                            site.setCode(code);
                        }


                        cell = row.getCell(3); // Centre technique
                        if (cell != null && cell.getCellType() == CellType.STRING) {
                            String centreTechniqueName = cell.getStringCellValue();
                            Optional<CentreTechnique> optionalCentreTechnique = ctrepo.findByName(centreTechniqueName);

                            if (optionalCentreTechnique.isPresent()) {
                                site.setCentreTechnique(optionalCentreTechnique.get());
                            } else {
                                // Afficher un message d'erreur ou gérer le cas où le centre technique n'existe pas
                                throw new DataIntegrityViolationException("Le centre technique '" + centreTechniqueName + "' n'existe pas dans la base de données.");
                            }
                        }


                        cell = row.getCell(4);
                        if (cell != null && cell.getCellType() == CellType.STRING) {
                            String addr = cell.getStringCellValue();
                            site.setAddresse(addr);
                        }
                        cell = row.getCell(5);
                        if (cell != null) {
                            if (cell.getCellType() == CellType.NUMERIC) {
                                site.setLatitude(cell.getNumericCellValue());
                            } else if (cell.getCellType() == CellType.STRING) {
                                try {
                                    site.setLatitude(Double.parseDouble(cell.getStringCellValue()));
                                } catch (NumberFormatException e) {
                                    // Gérer les cas où la chaîne ne peut pas être convertie en double
                                    e.printStackTrace();
                                }
                            } else {
                                // Gérer les autres types de cellules si nécessaire
                                System.out.println("La cellule ne contient pas une valeur numérique.");
                            }
                        }

                        cell = row.getCell(6);
                        if (cell != null) {
                            if (cell.getCellType() == CellType.NUMERIC) {
                                site.setLongitude(cell.getNumericCellValue());
                            } else if (cell.getCellType() == CellType.STRING) {
                                try {
                                    site.setLongitude(Double.parseDouble(cell.getStringCellValue()));
                                } catch (NumberFormatException e) {
                                    // Gérer les cas où la chaîne ne peut pas être convertie en double
                                    e.printStackTrace();
                                }
                            } else {
                                // Gérer les autres types de cellules si nécessaire
                                System.out.println("La cellule ne contient pas une valeur numérique.");
                            }
                        }
                        cell = row.getCell(7);
                        if (cell != null && cell.getCellType() == CellType.STRING) {
                            String typeIns = cell.getStringCellValue();
                            site.setTypeInstallation(typeIns);
                        } cell = row.getCell(8);
                        if (cell != null && cell.getCellType() == CellType.STRING) {
                            String type = cell.getStringCellValue();
                            site.setTypeAlimentation(type);
                        } cell = row.getCell(9);
                        if (cell != null && cell.getCellType() == CellType.STRING) {
                            String type = cell.getStringCellValue();
                            site.setPresenceGESecours(Boolean.valueOf(type));
                        } cell = row.getCell(10);
                        if (cell != null && cell.getCellType() == CellType.STRING) {
                            String type = cell.getStringCellValue();
                            site.setTypeTransmission(type);
                        } cell = row.getCell(11);
                        if (cell != null && cell.getCellType() == CellType.STRING) {
                            SiteMobile mb = (SiteMobile) site;
                            String type = cell.getStringCellValue();
                            mb.setSupportAntennes(type);
                        }


                        cell = row.getCell(12);
                        if (cell != null) {
                            if (cell.getCellType() == CellType.NUMERIC) {
                                site.setHauteurSupportAntenne(cell.getNumericCellValue());
                            } else if (cell.getCellType() == CellType.STRING) {
                                try {
                                    site.setLongitude(Double.parseDouble(cell.getStringCellValue()));
                                } catch (NumberFormatException e) {
                                    // Gérer les cas où la chaîne ne peut pas être convertie en double
                                    e.printStackTrace();
                                }
                            } else {
                                // Gérer les autres types de cellules si nécessaire
                                System.out.println("La cellule ne contient pas une valeur numérique.");
                            }
                        }
                        cell = row.getCell(13);
                        if (cell != null && cell.getCellType() == CellType.STRING) {
                            SiteMobile mb = (SiteMobile) site;
                            String type = cell.getStringCellValue();
                            mb.setLieuInsatallationBTS(type);

                        }

                        // Enregistrer le site
                        service.saveSiteMobile(site);
                        siteDTOS.add(smapper.fromMobile(site));
                    }

                    else if
                    (typeSite.equals("Fixe")){
                        SiteFixe site = new SiteFixe();
                        service.saveSiteFixe(site);
                        site.setTypeSite(typeSite);


                        cell = row.getCell(1); // Name
                        if (cell != null && cell.getCellType() == CellType.STRING) {
                            String siteName = cell.getStringCellValue();
                            if (repo.existsByName(siteName)) {
                                throw new DataIntegrityViolationException("Un site avec le nom '" + siteName + "' existe déjà dans la base de données.");
                            }
                            site.setName(siteName);
                        }

                        cell = row.getCell(0);
                        if (cell != null && cell.getCellType() == CellType.STRING) {
                            String code = cell.getStringCellValue();
                            site.setCode(code);
                        }



                        // Centre technique
                        cell = row.getCell(3
                        ); // Centre technique
                        if (cell != null && cell.getCellType() == CellType.STRING) {
                            String centreTechniqueName = cell.getStringCellValue();
                            Optional<CentreTechnique> optionalCentreTechnique = ctrepo.findByName(centreTechniqueName);

                            CentreTechnique centreTechnique;
                            if (optionalCentreTechnique.isPresent()) {
                                centreTechnique = optionalCentreTechnique.get();
                            } else {
                                // Créer un nouveau centre technique s'il n'existe pas
                                centreTechnique = new CentreTechnique();
                                centreTechnique.setName(centreTechniqueName);
                                // Sauvegarder le nouveau centre technique dans la base de données
                                centreTechnique = ctrepo.save(centreTechnique);
                            }
                            site.setCentreTechnique(centreTechnique);
                        }


                        cell = row.getCell(4);
                        if (cell != null && cell.getCellType() == CellType.STRING) {
                            String addr = cell.getStringCellValue();
                            site.setAddresse(addr);
                        }
                        cell = row.getCell(5);
                        if (cell != null) {
                            if (cell.getCellType() == CellType.NUMERIC) {
                                site.setLatitude(cell.getNumericCellValue());
                            } else if (cell.getCellType() == CellType.STRING) {
                                try {
                                    site.setLatitude(Double.parseDouble(cell.getStringCellValue()));
                                } catch (NumberFormatException e) {
                                    // Gérer les cas où la chaîne ne peut pas être convertie en double
                                    e.printStackTrace();
                                }
                            } else {
                                // Gérer les autres types de cellules si nécessaire
                                System.out.println("La cellule ne contient pas une valeur numérique.");
                            }
                        }

                        cell = row.getCell(6);
                        if (cell != null) {
                            if (cell.getCellType() == CellType.NUMERIC) {
                                site.setLongitude(cell.getNumericCellValue());
                            } else if (cell.getCellType() == CellType.STRING) {
                                try {
                                    site.setLongitude(Double.parseDouble(cell.getStringCellValue()));
                                } catch (NumberFormatException e) {
                                    // Gérer les cas où la chaîne ne peut pas être convertie en double
                                    e.printStackTrace();
                                }
                            } else {
                                System.out.println("La cellule ne contient pas une valeur numérique.");
                            }
                        }
                        cell = row.getCell(7);
                        if (cell != null && cell.getCellType() == CellType.STRING) {
                            String typeIns = cell.getStringCellValue();
                            site.setTypeInstallation(typeIns);
                        } cell = row.getCell(8);
                        if (cell != null && cell.getCellType() == CellType.STRING) {
                            String type = cell.getStringCellValue();
                            site.setTypeAlimentation(type);
                        } cell = row.getCell(9);
                        if (cell != null && cell.getCellType() == CellType.STRING) {
                            String type = cell.getStringCellValue();
                            site.setPresenceGESecours(Boolean.valueOf(type));
                        } cell = row.getCell(10);
                        if (cell != null && cell.getCellType() == CellType.STRING) {
                            String type = cell.getStringCellValue();
                            site.setTypeTransmission(type);
                        }

                        // Enregistrer le site
                        service.saveSiteFixe(site);
                        siteDTOS.add(smapper.fromFixe(site));

                    }

                    else {
                        System.out.println("siite fixe ");

                    }
                }




            }


        }
        return siteDTOS;
    }






}