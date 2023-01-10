package com.example.watchlist.converter;

import com.example.watchlist.dto.StockDTO;
import com.example.watchlist.model.Stock;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class StockConverter implements Converter<Stock, StockDTO>{

    public Set<Long> convertModelsToIDs(Set<Stock> models) {
        return models.stream()
                .map(model -> model.getId())
                .collect(Collectors.toSet());
    }

    public Set<Long> convertDTOsToIDs(Set<StockDTO> dtos) {
        return dtos.stream()
                .map(dto -> dto.getId())
                .collect(Collectors.toSet());
    }

    public Set<StockDTO> convertModelsToDtos(Collection<Stock> models) {
        return models.stream()
                .map(model -> convertModelToDto(model))
                .collect(Collectors.toSet());
    }

    public Set<Stock> convertDtosToModels(Collection<StockDTO> dtos) {
        return dtos.stream()
                .map(this::convertDtoToModel)
                .collect(Collectors.toSet());
    }

    @Override
    public Stock convertDtoToModel(StockDTO stockDTO) {
        Stock stock = new Stock(stockDTO.getId(), stockDTO.getSymbol(), stockDTO.getDesc(), stockDTO.getEmotion(), stockDTO.getMotivationalMessage(), stockDTO.getText());
        return stock;
    }

    @Override
    public StockDTO convertModelToDto(Stock stock) {
        StockDTO dto = new StockDTO(stock.getId(), stock.getSymbol(), stock.getDesc(), stock.getEmotion(), stock.getMotivationalMessage(), stock.getText());

        return dto;
    }
}
