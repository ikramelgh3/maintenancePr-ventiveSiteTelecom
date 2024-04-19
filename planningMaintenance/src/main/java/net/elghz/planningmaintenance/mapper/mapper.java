package net.elghz.planningmaintenance.mapper;

import net.elghz.planningmaintenance.dto.PlanningMaintenanceDTO;
import net.elghz.planningmaintenance.entities.PlanningMaintenance;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
@Service
public class mapper {

        private final ModelMapper modelMapper = new ModelMapper();

        public PlanningMaintenanceDTO from (PlanningMaintenance e){
            return modelMapper.map(e, PlanningMaintenanceDTO.class);
        }

        public PlanningMaintenance from (PlanningMaintenanceDTO   attrDTO){
            return modelMapper.map(attrDTO,PlanningMaintenance.class);
        }
}
