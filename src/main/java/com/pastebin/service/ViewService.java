package com.pastebin.service;

import com.pastebin.model.Views;

import java.util.List;

public interface ViewService {

    void save(Views view);

    Views findByHashId(String hashId);

    List<Views> mostPopularViews();

}
