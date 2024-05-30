package com.pastebin.service.impl;

import com.pastebin.model.Text;
import com.pastebin.model.Url;
import com.pastebin.model.Views;
import com.pastebin.repository.TextRepository;
import com.pastebin.repository.UrlRepository;
import com.pastebin.service.HashGenerator;
import com.pastebin.service.TextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TextServiceImpl implements TextService {

    private final HashGenerator hashGenerator;
    private final TextRepository textRepository;
    private final UrlRepository urlRepository;


    @Override
    public synchronized Text saveText(String text, String time) {
        LocalDateTime localDateTime;
        if(time.isEmpty()){
            localDateTime = LocalDateTime.now().plusHours(8);

        }else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            localDateTime = LocalDateTime.parse(time, formatter);
        }
        String id = hashGenerator.generateHash();
        Text t = new Text();
        t.setText(text);
        t.setHashId(id);
        t.setExpiredAt(localDateTime);
        textRepository.save(t);
        return t;
    }

    @Override
    public Url saveUrl(String id) {
        Url url = new Url();
        url.setUrl("http://localhost:8082/v1/pastebin/text/" + id);
        urlRepository.save(url);
        return url;

    }

    @Override
    public Text findByHashId(String hashId) {
        return textRepository.findByHashId(hashId);
    }

    @Scheduled(fixedRate = 60000)
    public void clearFromExpiredText(){
        List<Text> textRepositoryAll = textRepository.findAll();
        for (Text text : textRepositoryAll) {
            if (text.getExpiredAt().isBefore(LocalDateTime.now())) {
                log.info(String.valueOf(text.getExpiredAt()));
                textRepository.delete(text);
                urlRepository.deleteByUrl("http://localhost:8082/v1/pastebin/text/"+text.getHashId());
                log.info(text + " deleted");
            }
        }
    }


}
