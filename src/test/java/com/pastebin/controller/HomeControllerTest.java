package com.pastebin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pastebin.model.Text;
import com.pastebin.model.Url;
import com.pastebin.model.Views;
import com.pastebin.service.MailService;
import com.pastebin.service.TextService;
import com.pastebin.service.ViewService;
import com.pastebin.service.impl.MailServiceImpl;
import com.pastebin.service.impl.TextServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = HomeController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TextServiceImpl textService;
    @MockBean
    private ViewService viewService;
    @MockBean
    private MailServiceImpl mailService;

    @Test
    void homeWhenUrlNotNullShouldReturnStatusIsOk() throws Exception {
        Views views = new Views();
        views.setHashId("1234ret");
        views.setViewsCount(12);
        List<Views> viewsList = List.of(views);

        when(viewService.mostPopularViews()).thenReturn(viewsList);

        ResultActions perform = mockMvc.perform(get("http://localhost:8082/home"));

        perform
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("url"))
                .andExpect(model().attributeExists("views"));

    }

    @Test
    void homeWhenUrlNullShouldReturnStatusIsOk() throws Exception {
        Url url = new Url();
        url.setUrl("http://localhost:8082/home");
        Views views = new Views();
        views.setHashId("1234ret");
        views.setViewsCount(12);
        List<Views> viewsList = List.of(views);

        when(viewService.mostPopularViews()).thenReturn(viewsList);

        ResultActions perform = mockMvc.perform(get("http://localhost:8082/home"));

        perform
                .andExpect(status().isOk())
                .andExpect(model().attribute("url",url))
                .andExpect(model().attributeExists("views"));

    }

    @Test
    void postTextWhenTextIsEmptyShouldRedirectToHome() throws Exception {


        ResultActions perform = mockMvc.perform(post("http://localhost:8082/postText")
                .param("text","")
                .param("time","2005-05-25 12:25:05")
                .param("email","text@gmail.com")
        );

        perform
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeDoesNotExist("url"));

    }

    @Test
    void postTextWhenTextIsNotEmptyShouldRedirectToHome() throws Exception {
        String time = String.valueOf(LocalDateTime.now().plusHours(1));
        Text text = new Text();
        text.setText("text");
        text.setExpiredAt(LocalDateTime.now().plusHours(1));
        text.setHashId("hash");
        Url url = new Url();
        url.setId(1);
        url.setUrl("http://test.com/hash");

        when(textService.saveText("test", time)).thenReturn(text);
        when(textService.saveUrl(text.getHashId())).thenReturn(url);

        ResultActions perform = mockMvc.perform(post("/postText")
                .param("text", "test")
                .param("time", time)
                .param("email", "test@test.com")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED));

        perform.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

    }


    @Test
    void getTextShouldReturnStatusIsOk() throws Exception {
        String id = "1234ret";
        Text text = new Text();
        text.setId(1);
        text.setHashId(id);
        text.setText("test");
        Views views = new Views();
        when(viewService.findByHashId(id)).thenReturn(views);
        when(textService.findByHashId(id)).thenReturn(text);

        ResultActions perform = mockMvc.perform(get("/text/" + id));

        perform
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("text"));


    }
}