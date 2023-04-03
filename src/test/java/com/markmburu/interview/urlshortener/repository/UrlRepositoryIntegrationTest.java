package com.markmburu.interview.urlshortener.repository;

import com.markmburu.interview.urlshortener.model.ShortenUrlEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UrlRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ShortenUrlRepository shortenUrlRepository;

    @Test
    public void shouldInsertAndGetFullurl() {
        ShortenUrlEntity shortenUrlEntity = new ShortenUrlEntity("http://example.com");
        shortenUrlRepository.save(shortenUrlEntity);

        assertThat(shortenUrlEntity.getId(), notNullValue());

        ShortenUrlEntity shortenUrlEntityFromDb = shortenUrlRepository.findById(shortenUrlEntity.getId()).get();
        assertThat(shortenUrlEntityFromDb.getId(), equalTo(shortenUrlEntity.getId()));
    }

}