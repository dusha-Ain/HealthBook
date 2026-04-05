package com.rent.healthbook.consultant.service;

import com.rent.healthbook.consultant.dto.Consultant;
import com.rent.healthbook.consultant.entity.ConsultantEntity;
import org.springframework.stereotype.Component;

@Component
public class ConsultantMapper {

    public ConsultantEntity toEntity(Consultant consultant){
        return new ConsultantEntity(
          consultant.id(),
          consultant.isOnline(),
          consultant.idProfile()
        );
    }

    public Consultant toDomainConsultant(ConsultantEntity consultant){
        return new Consultant(
                consultant.getId(),
                consultant.getOnline(),
                consultant.getIdProfile()
        );
    }
}
