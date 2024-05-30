package com.pastebin.service.impl;

import com.pastebin.model.Text;
import com.pastebin.model.Url;
import com.pastebin.repository.TextRepository;
import com.pastebin.repository.UrlRepository;
import com.pastebin.service.HashGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TextServiceImplTest {

    @Mock
    private HashGenerator hashGenerator;

    @Mock
    private TextRepository textRepository;

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private TextServiceImpl textService;

    @Test
    void saveText() {
        String text = "test";
        String time = "2005-05-05 12:12:12";

        when(hashGenerator.generateHash()).thenReturn("1234ret");

        Text savedText = textService.saveText(text, time);

        assertNotNull(savedText);
        assertEquals(text, savedText.getText());
        assertEquals("1234ret",savedText.getHashId());
    }

    @Test
    void saveUrl() {
        when(urlRepository.save(any(Url.class))).thenReturn(any(Url.class));

        textService.saveUrl("123ret");

        verify(urlRepository).save(any(Url.class));


    }

    @Test
    void findByHashId() {
    }

    @Test
    void clearFromExpiredText() {
        Text activeText = new Text();
        activeText.setHashId("5678ret");
        activeText.setId(2);
        activeText.setExpiredAt(LocalDateTime.now().plusHours(12));
        Text expiredText = new Text();
        expiredText.setHashId("1234ret");
        expiredText.setText("test");
        expiredText.setId(1);
        expiredText.setExpiredAt(LocalDateTime.now().minusDays(1));
        List<Text> textList = List.of(activeText,expiredText);

        when(textRepository.findAll()).thenReturn(textList);


        textService.clearFromExpiredText();

        verify(textRepository,times(1)).delete(expiredText);
        verify(urlRepository,times(1)).deleteByUrl("http://localhost:8082/text/"+expiredText.getHashId());
        verify(textRepository,never()).delete(activeText);
        verify(urlRepository,never()).deleteByUrl("http://localhost:8082/text/"+activeText.getHashId());

    }
}