package com.barabanov.moviebot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Language implements BaseEntity<Integer>
{
    public Language(Integer id,
                    String name,
                    List<Film> films)
    {
        this.id = id;
        this.name = name;
        films.forEach(this::addFilm);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "language")
    List<Film> films = new ArrayList<>();
    
    // используем этот метод, а не getFilms() чтобы проставлять зависимость и у Film автоматически, а не в клиенте.
    public void addFilm(Film film)
    {
        films.add(film);
        film.setLanguage(this);
    }
}
