package com.pastebin.controller;

import com.pastebin.model.Text;
import com.pastebin.model.Url;
import com.pastebin.model.Views;
import com.pastebin.repository.TextRepository;
import com.pastebin.repository.UrlRepository;
import com.pastebin.repository.ViewsRepository;
import com.pastebin.service.HashGenerator;
import com.pastebin.service.ViewService;
import com.pastebin.service.impl.MailServiceImpl;
import com.pastebin.service.impl.TextServiceImpl;
import io.micrometer.core.annotation.Timed;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequestMapping("/v1/pastebin")
public class HomeController {

    TextServiceImpl textService;
    ViewService viewService;
    MailServiceImpl mailService;

   @Timed("homePage")
   @GetMapping("/home")
   public String home(Model model,HttpSession session) {
       List<Views> views = viewService.mostPopularViews();
       model.addAttribute("views", views);
       Url url =(Url) session.getAttribute("url");
       if (url != null){
           model.addAttribute("url", url);
       }else {
           url = new Url();
           url.setUrl("http://localhost:8082/v1/pastebin/home");
           model.addAttribute("url", url);
           session.setAttribute("url", url);
       }
       return "home";
   }

   @Timed("postText")
   @PostMapping("/postText")
   public String postText(@RequestParam String text, @RequestParam String time,HttpSession session,@RequestParam String email){
       if (text.isEmpty()){
           return "redirect:/v1/pastebin/home";
       }
       Text textEntity = textService.saveText(text, time);
       Url url = textService.saveUrl(textEntity.getHashId());
       session.setAttribute("url",url);
       if (!email.isEmpty()){
           mailService.sendMail(email, url.getUrl());

       }
       return "redirect:/v1/pastebin/home";
   }

    @GetMapping("/text/{id}")
    public String getText(@PathVariable String id,Model model) {
        Views views = viewService.findByHashId(id);
        if (views == null) {
            viewService.saveView(id);
        } else {
            views.setViewsCount(views.getViewsCount() + 1);
            viewService.save(views);
        }
        Text text = textService.findByHashId(id);
        model.addAttribute("text", text);
        return "text";
    }


}
