package com.tin.backend.mappers.impl;

import com.tin.backend.domain.dto.TaxpayerDTO;
import com.tin.backend.domain.entities.TaxpayerEntity;
import com.tin.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TaxpayerMapperImpl implements Mapper<TaxpayerEntity, TaxpayerDTO> {

    private ModelMapper modelMapper;

    public TaxpayerMapperImpl(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    @Override
    public TaxpayerDTO mapTo(TaxpayerEntity taxpayerEntity){
        return modelMapper.map(taxpayerEntity, TaxpayerDTO.class);
    }

    @Override
    public TaxpayerEntity mapFrom(TaxpayerDTO taxpayerDTO){
        return modelMapper.map(taxpayerDTO, TaxpayerEntity.class);
    }


}
