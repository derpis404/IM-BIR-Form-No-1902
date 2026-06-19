package com.tin.backend.mappers.impl;

import com.tin.backend.domain.dto.SpouseDTO;
import com.tin.backend.domain.dto.TaxpayerDTO;
import com.tin.backend.domain.entities.SpouseEntity;
import com.tin.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SpouseMapperImpl implements Mapper<SpouseEntity, SpouseDTO> {

    private final ModelMapper modelMapper;

    public SpouseMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public SpouseDTO mapTo(SpouseEntity spouseEntity) {

        SpouseDTO dto = modelMapper.map(spouseEntity, SpouseDTO.class);

        if (spouseEntity.getTaxpayer() != null) {
            dto.setTaxpayer(
                modelMapper.map(spouseEntity.getTaxpayer(), TaxpayerDTO.class)
            );
        }

        return dto;
    }

    @Override
    public SpouseEntity mapFrom(SpouseDTO spouseDTO) {

        SpouseEntity entity = modelMapper.map(spouseDTO, SpouseEntity.class);

        if (spouseDTO.getSpouseTIN() != null) {
            entity.setSpouseTIN(spouseDTO.getSpouseTIN());
        }

        return entity;
    }
}