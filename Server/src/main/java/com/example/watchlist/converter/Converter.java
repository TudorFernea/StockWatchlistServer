package com.example.watchlist.converter;

public interface Converter<Model, Dto> {

    Model convertDtoToModel(Dto dto);

    Dto convertModelToDto(Model model);

}