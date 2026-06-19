package com.tin.backend.mappers.impl;

import com.tin.backend.domain.dto.CurEmpDTO;
import com.tin.backend.domain.entities.CurEmpEntity;
import com.tin.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CurEmpMapperImpl implements Mapper<CurEmpEntity, CurEmpDTO> {
    private  ModelMapper modelMapper;

    public CurEmpMapperImpl(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    @Override
    public CurEmpDTO mapTo(CurEmpEntity curEmpEntity){
        return modelMapper.map(curEmpEntity, CurEmpDTO.class);
    }

    @Override
    public CurEmpEntity mapFrom(CurEmpDTO curEmpDTO){
        return modelMapper.map(curEmpDTO, CurEmpEntity.class);
    }
}
