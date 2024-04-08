package net.elghz.gestionparam.service;

import lombok.AllArgsConstructor;
import net.elghz.gestionparam.entities.parametrage;
import net.elghz.gestionparam.repository.parametrageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class parametrageService {

    private parametrageRepository repo;

    public List<parametrage> getAllParametrages() {
        return repo.findAll();
    }

    public Optional<parametrage> getParametrageById(Long id) {
        return repo.findById(id);
    }

    public parametrage saveParametrage(parametrage parametrage) {
        return repo.save(parametrage);
    }

    public boolean deleteParametrage(Long id) {
        Optional<parametrage> op= repo.findById(id);
        if(op.isPresent()){
            return  true;
        }
        else{
            return  false;
        }
    }

    public List<parametrage> findByType( String type){
        List<parametrage>params = repo.findAllByTypeParametrage(type);
        return params;
    }

    public Optional<parametrage> findByCode( String code){
        Optional<parametrage> params = repo.findByCodeParametrage(code);
        return params;
    }

}
