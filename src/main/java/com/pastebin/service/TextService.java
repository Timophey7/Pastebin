package com.pastebin.service;

import com.pastebin.model.Text;
import com.pastebin.model.Url;
import com.pastebin.model.Views;

public interface TextService {

    Text saveText(String text, String time);

    Url saveUrl(String id);

    Text findByHashId(String hashId);

}
