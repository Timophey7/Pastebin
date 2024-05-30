package com.pastebin.service.impl;

import com.pastebin.model.Views;
import com.pastebin.repository.ViewsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ViewsServiceImplTest {

    @Mock
    private ViewsRepository viewsRepository;

    @InjectMocks
    private ViewsServiceImpl viewsService;

    @Test
    void mostPopularViews() {

        Views views1 = new Views();
        views1.setHashId("1234ret");
        views1.setViewsCount(12);
        Views views2 = new Views();
        views2.setHashId("12rew");
        views2.setViewsCount(2);

        List<Views> viewsList = List.of(views2,views1);

        when(viewsRepository.findAll()).thenReturn(viewsList);

        List<Views> views = viewsService.mostPopularViews();
        assertNotNull(views);
        assertEquals(views.get(0),views1);

    }
}