package com.pastebin.repository;

import com.pastebin.model.Url;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UrlRepositoryTest {

    @Autowired
    private UrlRepository urlRepository;

    @Test
    void deleteByUrl() {
        Url url = new Url();
        url.setUrl("https://pastebin.com/");
        url.setId(1);
        urlRepository.save(url);

        urlRepository.delete(url);

        List<Url> all = urlRepository.findAll();

        assertEquals(0, all.size());


    }
}