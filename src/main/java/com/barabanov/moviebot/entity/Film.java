package com.barabanov.moviebot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "language")
@EqualsAndHashCode(exclude = "language")
@Builder
@Entity
public class Film implements BaseEntity<Long>
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    private LocalDate releaseYear;

    // на самом деле Lazy тут не будет работать (вроде),
    // если не поставить optional = false. А его я не ставлю т.к. это задаст ограничение на порядок сохранения.
    // Тогда сначала Language, а потом Film и больше никак.
    @ManyToOne(fetch = FetchType.LAZY)
    private Language language;

    private Integer length;

    @Enumerated(EnumType.STRING)
    private Rating rating;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    private Integer audienceScore;

}


