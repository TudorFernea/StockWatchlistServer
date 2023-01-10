package com.example.watchlist.controller;

import com.example.watchlist.converter.StockConverter;
import com.example.watchlist.dto.StockDTO;
import com.example.watchlist.model.Stock;
import com.example.watchlist.repository.StockRepository;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
public class StockController {

    private StockRepository stockRepository;

    private StockConverter stockConverter;

    public StockController(StockRepository stockRepository, StockConverter stockConverter){
        this.stockRepository = stockRepository;
        this.stockConverter = stockConverter;
    }

    @GetMapping("/stocks")
    public Set<StockDTO> retrieveAllStories(){
        System.out.println("GET ALL");
        return stockConverter.convertModelsToDtos(stockRepository.findAll());
    }

    @PostMapping("/stocks")
    public Stock createStock(@RequestBody Stock stock){
        System.out.println("POST");

        return stockRepository.save(stock);
    }

    @DeleteMapping("/stocks/{id}")
    public Stock deleteStock(@PathVariable long id) {
        System.out.println("DELETE");
        Optional<Stock> stockOptional = stockRepository.findById(id);

        if(stockOptional.isEmpty()){
            return null;
        }

        stockRepository.deleteById(id);
        return stockOptional.get();
    }

    @PutMapping("/stocks/{id}")
    public Stock updateStock(@PathVariable long id, @RequestBody Stock stock){
        System.out.println("PUT");
        Optional<Stock> stockOptional = stockRepository.findById(id);

        if(stockOptional.isEmpty()){
            return null;
        }

        stock.setId(id);
        return stockRepository.save(stock);
    }

}
