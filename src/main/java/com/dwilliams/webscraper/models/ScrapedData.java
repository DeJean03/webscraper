package com.dwilliams.webscraper.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "scraped_data")
public class ScrapedData {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Link cannot be blank.")
    @Pattern(regexp = "^(https?://)([\\w.-]+)\\.([a-z]{2,6})([/\\w.-]*)*/?$",
            message = "Link must be a valid URL.")
    private String link;




    @Column(name = "created_at")
    private LocalDateTime createAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;




    public void setLink(String link) {
        if (link == null || link.isEmpty()) {
            throw new IllegalArgumentException("Link must not be null or empty.");
        }



        this.link = link;
    }
    }
