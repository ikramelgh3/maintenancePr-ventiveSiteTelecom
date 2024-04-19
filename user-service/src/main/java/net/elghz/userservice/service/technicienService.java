package net.elghz.userservice.service;


import net.elghz.userservice.dtos.CompetenceDTO;
import net.elghz.userservice.dtos.TechnicienDTO;

import net.elghz.userservice.entities.Competence;
import net.elghz.userservice.entities.Role;
import net.elghz.userservice.entities.Technicien;
import net.elghz.userservice.mapper.mapper;
import net.elghz.userservice.repository.CompetenceRepository;
import net.elghz.userservice.repository.RoleRepository;
import net.elghz.userservice.repository.TechnicienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class technicienService {

    @Autowired private TechnicienRepository repo;
    @Autowired  CompetenceRepository crepo;
    @Autowired private mapper mp;
    @Autowired private RoleRepository rrepo;

    public ResponseEntity<?> addTechnicen (TechnicienDTO ptDto){


        Technicien c = mp.from(ptDto);
        Optional<Role> technicienRoleOptional = Optional.ofNullable(rrepo.findByRoleName("TECHNICIEN"));
        if (technicienRoleOptional.isPresent()) {
            Role technicienRole = technicienRoleOptional.get();
            List<Role> roles = new ArrayList<>();
            roles.add(technicienRole);
            c.setRoles(roles);
        } else {
            return new ResponseEntity<>("Le rôle TECHNICIEN n'a pas été trouvé", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        repo.save(c);
        return new ResponseEntity<>("Les informations sur le technicien sont bien ajoutées", HttpStatus.OK);
    }
    public ResponseEntity<?> ajouterTechnicienAvecCompetences(TechnicienDTO technicienDTO) {
        Technicien technicien = new Technicien();
        technicien.setDisponibilite(technicienDTO.getDisponibilite());
        technicien.setNombreIntervention(technicienDTO.getNbreIntervetion());
        technicien.setNom(technicienDTO.getNom());
        technicien.setUsername(technicienDTO.getUsername());
        //technicien.setPassword(technicienDTO.getPassword());
        Set<CompetenceDTO> competenceDTOs = technicienDTO.getCompetences();
        Set<Competence> competences = new HashSet<>();
        for (CompetenceDTO competenceDTO : competenceDTOs) {
            Optional<Competence> optionalCompetence = Optional.ofNullable(crepo.findByCompetence(competenceDTO.getCompetence()));

            if (optionalCompetence.isPresent()) {

                competences.add(optionalCompetence.get());
            } else {

                Competence nouvelleCompetence = new Competence();
                nouvelleCompetence.setCompetence(competenceDTO.getCompetence());
                crepo.save(nouvelleCompetence);
                competences.add(nouvelleCompetence);
            }
        }

        technicien.setCompetences(competences);

        Optional<Role> technicienRoleOptional = Optional.ofNullable(rrepo.findByRoleName("TECHNICIEN"));
        if (technicienRoleOptional.isPresent()) {
            Role technicienRole = technicienRoleOptional.get();
            List<Role> roles = new ArrayList<>();
            roles.add(technicienRole);
            technicien.setRoles(roles);
        } else {
            return new ResponseEntity<>("Le rôle TECHNICIEN n'a pas été trouvé", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        repo.save(technicien);
        return new ResponseEntity<>("Les informations sur le technicien sont bien ajoutées", HttpStatus.OK);
    }


    public ResponseEntity<?> addTechniciens(List<TechnicienDTO> technicienDTOList) {
        try {
            for (TechnicienDTO technicienDTO : technicienDTOList) {
                Technicien technicien = new Technicien();
                technicien.setDisponibilite(technicienDTO.getDisponibilite());
                technicien.setNombreIntervention(technicienDTO.getNbreIntervetion());
                technicien.setNom(technicienDTO.getNom());
                technicien.setUsername(technicienDTO.getUsername());
                //technicien.setPassword(technicienDTO.getPassword());

                // Récupérer les compétences du DTO et les mapper en objets Competence
                Set<CompetenceDTO> competenceDTOs = technicienDTO.getCompetences();
                Set<Competence> competences = new HashSet<>();
                for (CompetenceDTO competenceDTO : competenceDTOs) {
                    Competence competence = new Competence();
                    competence.setCompetence(competenceDTO.getCompetence());
                    competences.add(competence);
                }
                technicien.setCompetences( competences);

                // Assigner le rôle de technicien
                Optional<Role> technicienRoleOptional = Optional.ofNullable(rrepo.findByRoleName("TECHNICIEN"));
                if (technicienRoleOptional.isPresent()) {
                    Role technicienRole = technicienRoleOptional.get();
                    List<Role> roles = new ArrayList<>();
                    roles.add(technicienRole);
                    technicien.setRoles(roles);
                } else {
                    return new ResponseEntity<>("Le rôle TECHNICIEN n'a pas été trouvé", HttpStatus.INTERNAL_SERVER_ERROR);
                }

                repo.save(technicien);
            }
            return new ResponseEntity<>("Les techniciens ont été ajoutés avec succès", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Une erreur s'est produite lors de l'ajout des techniciens : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> findById( Long id){
        Optional<Technicien> ch = repo.findById(id);

        if(ch.isPresent()){
            TechnicienDTO c = mp.from(ch.get());

            return new ResponseEntity<>(c, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Aucune technicien n'est trouvée avec ce id: "+id , HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity <?> deleteR( Long id)
    {
        Optional<Technicien> ch = repo.findById(id);

        if(ch.isPresent()){
           repo.delete(ch.get());
            return new ResponseEntity<>("Le technicien est supprimé", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Aucune technicien n'est trouvée avec ce id: "+id , HttpStatus.NOT_FOUND);
        }
    }
    public ResponseEntity<?> modifierTechnicien(Long technicienId, TechnicienDTO newTechnicienDTO) {
        Optional<Technicien> technicienOptional = repo.findById(technicienId);
        if (technicienOptional.isPresent()) {
            Technicien technicien = technicienOptional.get();
            technicien.setNom(newTechnicienDTO.getNom());
            technicien.setUsername(newTechnicienDTO.getUsername());
            technicien.setDisponibilite(newTechnicienDTO.getDisponibilite());
            technicien.setNombreIntervention(newTechnicienDTO.getNbreIntervetion());
            repo.save(technicien);

            return new ResponseEntity<>("Technicien avec l'ID " + technicienId + " mis à jour avec succès", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Technicien non trouvé avec l'ID : " + technicienId, HttpStatus.NOT_FOUND);
        }
    }
public ResponseEntity getAllTechniciensWithCompetences() {
    List<Technicien> technicienDetailsList = repo.findAll();
    List<TechnicienDTO> technicienDTOList = new ArrayList<>();

    for (Technicien technicienDetails : technicienDetailsList) {
        TechnicienDTO technicienDTO = mp.from(technicienDetails);
        technicienDTOList.add(technicienDTO);
    }


    return new ResponseEntity<>(technicienDTOList , HttpStatus.OK);
}


    public ResponseEntity<?> assignerCompetences(Long technicienId, List<Long> competenceIds) {
        Optional<Technicien> technicienOptional = repo.findById(technicienId);
        if (technicienOptional.isPresent()) {
            Technicien technicien = technicienOptional.get();
            List<Competence> competences = crepo.findAllById(competenceIds);
            technicien.getCompetences().addAll(competences);
            repo.save(technicien);
            return new ResponseEntity<>("Les competences sont bien ajoutées au technicein", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Technicein non trouvé", HttpStatus.OK);
        }
    }

    public ResponseEntity<?> dissocierCompetence(Long technicienId, Long competenceId) {
        Optional<Technicien> technicienOptional = repo.findById(technicienId);
        if (technicienOptional.isPresent()) {
            Technicien technicien = technicienOptional.get();
            Optional<Competence> competenceOptional = technicien.getCompetences().stream()
                    .filter(c -> c.getId().equals(competenceId))
                    .findFirst();
            if (competenceOptional.isPresent()) {
                technicien.getCompetences().remove(competenceOptional.get());
                repo.save(technicien);

                return new ResponseEntity<>("La compétence a été dissociée du technicien avec succès", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Le technicien avec l'ID " + technicienId + " ne possède pas la compétence avec l'ID " + competenceId, HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Technicien non trouvé avec l'ID : " + technicienId, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> getCompetencesByTechnicienId(Long technicienId) {
        Optional<Technicien> technicienOptional = repo.findById(technicienId);
        if (technicienOptional.isPresent()) {
            Technicien technicien = technicienOptional.get();

            // Mapper les compétences de Competence à CompetenceDTO
            List<CompetenceDTO> competenceDTOList = technicien.getCompetences().stream()
                    .map(competence -> {
                        CompetenceDTO competenceDTO = new CompetenceDTO();
                        competenceDTO.setCompetence(competence.getCompetence());
                        // Mapper d'autres attributs si nécessaire
                        return competenceDTO;
                    })
                    .collect(Collectors.toList());

            return new ResponseEntity<>(competenceDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Technicien non trouvé avec l'ID : " + technicienId, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> getTechniciensByCompetence(String competenceName) {
        List<Technicien> techniciens = repo.findByCompetences_Competence(competenceName);
        if (!techniciens.isEmpty()) {
            // Mapper les Techniciens vers TechnicienDTO
            List<TechnicienDTO> technicienDTOs = techniciens.stream()
                    .map(technicien -> {
                        TechnicienDTO technicienDTO = new TechnicienDTO();
                        technicienDTO.setNom(technicien.getNom());
                        technicienDTO.setUsername(technicien.getUsername());
                        // Mapper d'autres attributs si nécessaire
                        return technicienDTO;
                    })
                    .collect(Collectors.toList());

            return new ResponseEntity<>(technicienDTOs, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Aucun technicien trouvé avec la compétence : " + competenceName, HttpStatus.NOT_FOUND);
        }
    }


    public ResponseEntity<?> getTechniciensDisponibles() {
        List<Technicien> techniciens = repo.findByDisponibiliteIsTrue();
        List<TechnicienDTO> technicienDTOs = new ArrayList<>();
        for (Technicien technicien : techniciens) {
            technicienDTOs.add(mp.from(technicien));
        }
        return new ResponseEntity<>( technicienDTOs, HttpStatus.OK);
    }

    public ResponseEntity<?> rendreTechnicienDisponible(Long technicienId) {
        Optional<Technicien> technicienOptional = repo.findById(technicienId);
        if (technicienOptional.isPresent()) {
            Technicien technicien = technicienOptional.get();
            technicien.setDisponibilite(true);
            repo.save(technicien);

            return new ResponseEntity<>("Technicien avec l'ID " + technicienId + " rendu disponible avec succès", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Technicien non trouvé avec l'ID : " + technicienId, HttpStatus.NOT_FOUND);
        }
    }


}
