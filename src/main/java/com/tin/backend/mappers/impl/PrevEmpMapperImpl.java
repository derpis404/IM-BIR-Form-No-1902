package com.tin.backend.mappers.impl;

import com.tin.backend.domain.dto.PrevEmpDTO;
import com.tin.backend.domain.entities.PrevEmpEntity;
import com.tin.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PrevEmpMapperImpl implements Mapper<PrevEmpEntity, PrevEmpDTO> {

    private final ModelMapper modelMapper;

    public PrevEmpMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    @Override
    public PrevEmpDTO mapTo(PrevEmpEntity prevEmpEntity) {

        PrevEmpDTO dto = modelMapper.map(prevEmpEntity, PrevEmpDTO.class);

        return dto;
    }


    @Override
    public PrevEmpEntity mapFrom(PrevEmpDTO prevEmpDTO) {

        PrevEmpEntity entity = new PrevEmpEntity();

        entity.setMultiEmpTIN(prevEmpDTO.getMultiEmpTIN());
        entity.setMultiEmpName(prevEmpDTO.getMultiEmpName());


        return entity;
    }
}