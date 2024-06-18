package com.rodionov.nbki.platform.mapper;

import com.rodionov.nbki.app.dto.CreditCreateDto;
import com.rodionov.nbki.app.dto.CreditGetDto;
import com.rodionov.nbki.app.dto.CreditUpdateDto;
import com.rodionov.nbki.domain.Credit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CreditMapper {

    @Mapping(target = "isClosed", constant = "false")
    @Mapping(target = "creditStart", expression = "java(java.time.LocalDate.now())")
     Credit CreditCreationDtoToEntity(CreditCreateDto dto);

    List<CreditGetDto> listEntityToListCreditGetDto(List<Credit> credits);

    CreditGetDto EntityToCreditGetDto(Credit credit);

    @Mapping(target = "creditEnd", expression = "java(com.rodionov.nbki.platform.mapper.CreditMapper.getCreditEndDateIfClosed(credit, dto))")
    default void updateCredit(@MappingTarget Credit credit, CreditUpdateDto dto){
        if ( dto == null ) {
            return;
        }
        credit.setCreditEnd(getCreditEndDateIfClosed(credit, dto));
        if (dto.creditAmount() != null){
            credit.setCreditAmount( dto.creditAmount() );
        }
        if (dto.isClosed() != null){
            credit.setIsClosed( dto.isClosed() );
        }
        if (dto.creditStart() != null){
            credit.setCreditStart( dto.creditStart() );
        }
    }

    private static LocalDate getCreditEndDateIfClosed( Credit credit, CreditUpdateDto dto){
        if (Boolean.FALSE.equals(credit.getIsClosed()) && Boolean.TRUE.equals(dto.isClosed())) {
            return LocalDate.now();
        }
        if (dto.isClosed() == null){
            return credit.getCreditEnd();
        }
        return null;
    }

}
