package com.dwilliams.webscraper.repositories;

import com.dwilliams.webscraper.models.ScrapedData;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScrapedDataRepository extends JpaRepository<ScrapedData, Long> {


    List<ScrapedData> findByTitleContainingIgnoreCase(String keyword);

    Optional<ScrapedData> findByLink(@NotBlank(message = "Link cannot be blank") String link);

    long countByTitle(String title);

    void deleteByLink(@NotBlank(message = "Link cannot be blank") String link);

    List<ScrapedData> findByTitleAndLink(@NotBlank(message = "Title cannot be blank") String title, @NotBlank(message = "Link cannot be blank") String link);

    boolean existsByTitle(String title);


}





