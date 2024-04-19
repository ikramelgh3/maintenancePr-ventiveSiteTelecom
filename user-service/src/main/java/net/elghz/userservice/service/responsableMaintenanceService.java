package net.elghz.userservice.service;

import lombok.AllArgsConstructor;
import net.elghz.userservice.dtos.TechnicienDTO;
import net.elghz.userservice.dtos.responsableDTO;
import net.elghz.userservice.entities.ResponsableMaintenance;
import net.elghz.userservice.entities.Role;
import net.elghz.userservice.entities.Technicien;
import net.elghz.userservice.feign.checklistRestClient;
import net.elghz.userservice.mapper.mapper;
import net.elghz.userservice.model.ChecklistDTO;
import net.elghz.userservice.repository.RespoMaintRepo;
import net.elghz.userservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class responsableMaintenanceService {

    private RespoMaintRepo repo;
    private RoleRepository rrepo;
    private mapper mp;
    private checklistRestClient restClient;

    public ResponseEntity<?> ajouterRespo(responsableDTO respodto) {

        Optional <ResponsableMaintenance> opt = repo.findByUsername(respodto.getUsername());
        if(opt.isPresent()){
            return new ResponseEntity<>("Le responsable de maintenance existe déjà.", HttpStatus.OK);
        }

        ResponsableMaintenance respo = mp.from(respodto);
        Role roleRespo = rrepo.findByRoleName("RESPONSABLE DE MAINTENANCE");
        if (roleRespo == null) {
            throw new RuntimeException("Le rôle RESPONSABLE_MAINTENANCE n'a pas été trouvé.");
        }
        respo.setRoles(Collections.singletonList(roleRespo));
        repo.save(respo);
        return new ResponseEntity<>("Le responsable de maintenance est bien ajouté.", HttpStatus.OK);
    }

    public ResponsableMaintenance findById(Long id){
        Optional<ResponsableMaintenance> opt = repo.findById(id);
        if(!opt.isPresent()){
            throw new RuntimeException("Responsable de maintenance n'est pas trouvé");
        }
        ResponsableMaintenance rep = opt.get();
       // responsableDTO dt = mp.from(rep);
        return rep;



    }


    public ResponsableMaintenance findByIdR(Long id) {
        Optional<ResponsableMaintenance> ch = repo.findById(id);

        if (ch.isPresent()) {
            return ch.get();
        } else {
            throw new NoSuchElementException("Aucun responsable de maintenance trouvé avec cet ID : " + id);
        }
    }

    public ResponseEntity<?> getChecklistOfResponMaint(String username){
        Optional<ResponsableMaintenance> rp = repo.findByUsername(username);
        if(rp.isPresent()){

            List<ChecklistDTO> checklists = restClient.getChecklistsByResp(rp.get().getId());
            rp.get().setChecklist(checklists);
            return new ResponseEntity<>(checklists, HttpStatus.OK);
        }

        return new ResponseEntity<>("Le responsable de maintenance n'existe pas!!!" , HttpStatus.NOT_FOUND);

    }






}
