package com.havrun.bookTrackerAPI.entity.Book;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "keywords")
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Integer id;

    @Column(nullable = false)
    private String keyword;

}
