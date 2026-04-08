package com.rent.healthbook.consultant.service;

import com.rent.healthbook.consultant.dto.Consultant;
import com.rent.healthbook.consultant.entity.ConsultantEntity;
import com.rent.healthbook.web.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class ConsultantMapper {

    public ConsultantEntity toEntity(Consultant consultant){
        if (consultant == null)
            throw new ValidationException("Consultant should be not null");
        return new ConsultantEntity(
          consultant.id(),
          consultant.isOnline(),
          consultant.idProfile()
        );
    }

    public Consultant toDomainConsultant(ConsultantEntity consultant){
        if (consultant == null)
            throw new ValidationException("Consultant should be not null");
        return new Consultant(
                consultant.getId(),
                consultant.getOnline(),
                consultant.getIdProfile()
        );
    }
}
