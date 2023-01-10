package com.example.watchlist.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class StockDTO {

    private Long id;

    private String symbol;

    private String desc;

    private String emotion;

    private String motivationalMessage;

    private String text;
}
