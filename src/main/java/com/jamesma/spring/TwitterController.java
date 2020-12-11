package com.jamesma.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tweets")
public class TwitterController {

    @Autowired
    private InMemoryTweetService tweetService;

    @GetMapping(value = "/top")
    public String showAll(Model model) {
        model.addAttribute("tweets", tweetService.findAll());
        return "topTweets";
    }

    @GetMapping(value = "/create")
    public String showCreateForm(Model model) {
        Tweet tweet = new Tweet();
        model.addAttribute("form", tweet);
        return "createTweetForm";
    }

    @PostMapping(value = "/save")
    public String saveTweet(@ModelAttribute Tweet tweet, Model model) {
        tweetService.save(tweet);
        model.addAttribute("tweets", tweetService.findAll());
        return "redirect:/tweets/top";
    }
}
