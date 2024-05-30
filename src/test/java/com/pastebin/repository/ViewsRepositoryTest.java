package com.pastebin.repository;

import com.pastebin.model.Views;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataRedisTest
public class ViewsRepositoryTest {

    @Autowired
    private ViewsRepository viewsRepository;

    @Test
    public void findByHashIdShouldReturnViews(){
        Views views = new Views();
        views.setHashId("1234ret");
        views.setViewsCount(1);
        viewsRepository.save(views);

        Views byHashId = viewsRepository.findByHashId("1234ret");

        assertNotNull(byHashId);
        assertEquals(views.getHashId(), byHashId.getHashId());
        assertEquals(views.getViewsCount(), byHashId.getViewsCount());
    }

    @Test
    public void findByViewsCountShouldReturnNull(){
        Views byHashId = viewsRepository.findByHashId("123");

        assertNull(byHashId);
    }

}