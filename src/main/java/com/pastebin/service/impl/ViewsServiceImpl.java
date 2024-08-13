package com.pastebin.service.impl;

import com.pastebin.model.Views;
import com.pastebin.repository.ViewsRepository;
import com.pastebin.service.ViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "redis")
public class ViewsServiceImpl implements ViewService {

    private final ViewsRepository viewsRepository;

    @Override
    @Cacheable(value = "redis",key = "#hashId")
    public Views findByHashId(String hashId) {
        return viewsRepository.findByHashId(hashId);
    }

    @Override
    public List<Views> mostPopularViews() {
        List<Views> views = new ArrayList<>();
        viewsRepository.findAll().forEach(v -> views.add(v));
        Comparator<Views> viewsComparator = new Comparator<Views>() {
            @Override
            public int compare(Views o1, Views o2) {
                return Integer.compare(o2.getViewsCount(), o1.getViewsCount());
            }
        };
        views.sort(viewsComparator);
        return views;
    }

    @Override
    public void saveView(String id) {
        Views views = new Views();
        views.setHashId(id);
        views.setViewsCount(1);
        save(views);
    }

    @Override
    public void save(Views view) {
        viewsRepository.save(view);
    }
}
