package com.markmburu.interview.urlshortener.repository;

import com.markmburu.interview.urlshortener.model.ShortenUrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShortenUrlRepository extends JpaRepository<ShortenUrlEntity, Long> {

    @Query("SELECT u FROM url u WHERE u.fullUrl = ?1")
    List<ShortenUrlEntity> findUrlByFullUrl(String fullUrl);
}
