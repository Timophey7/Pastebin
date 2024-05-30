package com.pastebin.repository;

import com.pastebin.model.Text;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@DataJpaTest
class TextRepositoryTest {

    @Autowired
    private TextRepository textRepository;

    @Test
    public void findByHashIdShouldReturnText(){
        Text text = new Text();
        text.setId(1);
        text.setHashId("1234ret");
        text.setText("text");
        textRepository.save(text);

        Text byHashId = textRepository.findByHashId("1234ret");

        assertNotNull(byHashId);
        assertEquals(text,byHashId);
    }

    @Test
    public void findByHashIdShouldReturnNull(){

        Text byHashId = textRepository.findByHashId("123");

        assertNull(byHashId);

    }


}