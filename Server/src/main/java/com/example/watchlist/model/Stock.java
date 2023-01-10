package com.example.watchlist.model;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "stock")
public class Stock {

    @Id
    private Long id;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "description")
    private String desc;

    @Column(name = "emotion")
    private String emotion;

    @Column(name = "motivational_message")
    private String motivationalMessage;

    @Column(name = "text")
    private String text;
}
