package net.elghz.userservice.service;

import lombok.AllArgsConstructor;
import net.elghz.userservice.dtos.CompetenceDTO;
import net.elghz.userservice.dtos.TechnicienDTO;
import net.elghz.userservice.dtos.utilisateurDTO;
import net.elghz.userservice.entities.Role;
import net.elghz.userservice.entities.Technicien;
import net.elghz.userservice.entities.utilisateur;
import net.elghz.userservice.mapper.mapper;
import net.elghz.userservice.repository.CompetenceRepository;
import net.elghz.userservice.repository.RoleRepository;

import net.elghz.userservice.repository.userRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

//@Service
@Transactional
@AllArgsConstructor
public class userService {
    private userRepository repo;
    private RoleRepository rrepo;

    private CompetenceRepository crepo;
    private mapper mp;
    public utilisateur addNewUser( utilisateur user){
        return repo.save(user);
    }

    public Role addNewRole( Role role){
        return  rrepo.save(role);
    }

    public void addRoleToUser(String username, String rolename){
        utilisateur optUser = repo.findByUsername(username);
        Role r = rrepo.findByRoleName(rolename);
        optUser.getRoles().add(r);

    }
    public utilisateur loadUser( String username){
        return repo.findByUsername(username);
    }

    public List<utilisateurDTO> users() {
        return repo.findAll().stream()
                .map(user -> {
                    if (user instanceof Technicien) {
                        Technicien technicien = (Technicien) user;
                        TechnicienDTO technicienDTO = mp.from(technicien);


                        Set<CompetenceDTO> competenceDTOs = technicien.getCompetences().stream()
                                .map(comp -> {
                                    CompetenceDTO compDTO = new CompetenceDTO();
                                    compDTO.setCompetence(comp.getCompetence());
                                    return compDTO;
                                })
                                .collect(Collectors.toSet());
                        technicienDTO.setCompetences(competenceDTOs);

                        return technicienDTO;
                    }  else {

                    }
                    return mp.from(user);
                })
                .collect(Collectors.toList());
    }


/*
    @Transactional
    public ResponseEntity<?> addTechnicen(utilisateur user , TechnicienDetailsDTO detailsDTO, List<CompetenceDTO> competenceDTO){

        String username = user.getUsername();
        utilisateur u = repo.findByUsername(username);
        if(u==null){

            utilisateur utilisateur = new utilisateur();
            utilisateur.setNom(user.getNom());
            utilisateur.setUsername(user.getUsername());
            utilisateur.setPassword(user.getPassword());
            Role r = rrepo.findByRoleName("TECHNICIEN");
            if (r == null) {
                r = new Role();
                r.setRoleName("TECHNICIEN");
                r = rrepo.save(r);
            }

            utilisateur.getRoles().add(r);

            repo.save(utilisateur);
            TechnicienDetails d = mp.from(detailsDTO);
            d.setUtilisateur(utilisateur);
            trepo.save(d);

            for(CompetenceDTO cc : competenceDTO){
                Competence cpt = mp.from(cc);

                crepo.save(cpt);

                d.getCompetences().add(cpt);


            }

            trepo.save(d);
            return new ResponseEntity<>(u, HttpStatus.OK)
;        }
        else{
            return  new ResponseEntity<>("Le username existe deja", HttpStatus.OK);
        }

    }
*//*
    public ResponseEntity<?> getTechnicienDetails(String username) {
        utilisateur user = repo.findByUsername(username);

        if (user == null) {
            return new ResponseEntity<>("Utilisateur non trouvé", HttpStatus.NOT_FOUND);
        }
        TechnicienDetails details = trepo.findByUtilisateur(user);

        if (details == null) {
            return new ResponseEntity<>("Détails du technicien non trouvés", HttpStatus.NOT_FOUND);
        }
        List<Competence> competences = new ArrayList<>(details.getCompetences());

        List<CompetenceDTO> competenceDTOs = new ArrayList<>();
        for (Competence competence : competences) {
            CompetenceDTO competenceDTO = new CompetenceDTO();
            competenceDTO.setCompetence(competence.getCompetence());
            competenceDTOs.add(competenceDTO);
        }
        Set<CompetenceDTO> competenceDTOSet = new HashSet<>(competenceDTOs);

        TechnicienDetailsDTO detailsDTO = new TechnicienDetailsDTO();
        detailsDTO.setDisponibilite(details.getDisponibilite());
        detailsDTO.setNbreIntervetion(details.getNbreIntervetion());
        detailsDTO.setCompetences(competenceDTOSet);
        utilisateurDTO userDTO = new utilisateurDTO();
        userDTO.setNom(user.getNom());
        userDTO.setUsername(user.getUsername());
        userDTO.setRoles(new ArrayList<>(user.getRoles()));
        Role roleDTO = null;
        if (!user.getRoles().isEmpty()) {
            roleDTO = user.getRoles().iterator().next();
        }

        String roleName = (roleDTO != null) ? roleDTO.getRoleName() : "N/A";
        roleDTO.setRoleName(roleName);

        addTechnicienDTO technicienFullDetailsDTO = new addTechnicienDTO();
        technicienFullDetailsDTO.setU(userDTO);
        technicienFullDetailsDTO.setDetailsDTO(detailsDTO);

        return new ResponseEntity<>(technicienFullDetailsDTO, HttpStatus.OK);
    }

    public ResponseEntity<?> getAllTechniciensDetails() {

        List<utilisateur> techniciens = repo.findByRoles_RoleName("TECHNICIEN");

        if (techniciens.isEmpty()) {
            return new ResponseEntity<>("Aucun technicien trouvé", HttpStatus.NOT_FOUND);
        }

        List<addTechnicienDTO> technicienDTOs = new ArrayList<>();

        for (utilisateur technicien : techniciens) {
            TechnicienDetails details = trepo.findByUtilisateur(technicien);

            if (details != null) {

                List<CompetenceDTO> competenceDTOs = new ArrayList<>();
                for (Competence competence : details.getCompetences()) {
                    CompetenceDTO competenceDTO = new CompetenceDTO();
                    competenceDTO.setCompetence(competence.getCompetence());
                    competenceDTOs.add(competenceDTO);
                }
                TechnicienDetailsDTO detailsDTO = new TechnicienDetailsDTO();
                detailsDTO.setDisponibilite(details.getDisponibilite());
                detailsDTO.setNbreIntervetion(details.getNbreIntervetion());
                detailsDTO.setCompetences(new HashSet<>(competenceDTOs));

                utilisateurDTO userDTO = new utilisateurDTO();
                userDTO.setNom(technicien.getNom());
                userDTO.setUsername(technicien.getUsername());
                userDTO.setRoles(new ArrayList<>(technicien.getRoles()));

                Role roleDTO = null;
                if (!technicien.getRoles().isEmpty()) {
                    roleDTO = technicien.getRoles().iterator().next();
                }
                String roleName = (roleDTO != null) ? roleDTO.getRoleName() : "N/A";
                roleDTO.setRoleName(roleName);

                addTechnicienDTO technicienFullDetailsDTO = new addTechnicienDTO();
                technicienFullDetailsDTO.setU(userDTO);
                technicienFullDetailsDTO.setDetailsDTO(detailsDTO);

                technicienDTOs.add(technicienFullDetailsDTO);
            }
        }

        return new ResponseEntity<>(technicienDTOs, HttpStatus.OK);
    }



    public ResponseEntity<?> recupererTechn( Long technicienId){
        Optional<TechnicienDetails> technicienOptional = trepo.findById(technicienId);
        if (technicienOptional.isPresent()) {
            TechnicienDetails technicien = technicienOptional.get() ; return  new ResponseEntity<>( technicien , HttpStatus.OK) ;}

          else {
            return  new ResponseEntity<>( "not found" , HttpStatus.OK) ;}


        }

    public void assignerCompetences(Long technicienId, List<Long> competenceIds) {
        // Récupérer le technicien à partir de son ID
        Optional<TechnicienDetails> technicienOptional = trepo.findById(technicienId);
        if (technicienOptional.isPresent()) {
            TechnicienDetails technicien = technicienOptional.get();

            // Récupérer les compétences à partir de leurs IDs
            List<Competence> competences = crepo.findAllById(competenceIds);

            // Associer les compétences au technicien
            technicien.getCompetences().addAll(competences);

            // Enregistrer le technicien mis à jour dans la base de données
            trepo.save(technicien);
        } else {
            throw new NoSuchElementException("Technicien non trouvé avec l'ID : " + technicienId);
        }
    }*/
}
