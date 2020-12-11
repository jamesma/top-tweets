package com.jamesma;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public String showCreateForm(TweetForm tweetForm) {
        return "createTweetForm";
    }

    @PostMapping(value = "/save")
    public String saveTweet(@Valid @ModelAttribute TweetForm tweetForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "createTweetForm";
        }
        tweetService.save(tweetForm);
        return "redirect:/tweets/top";
    }

    @PostMapping(value = "/retweet/{id}")
    public String retweet(@PathVariable("id") long id) {
        tweetService.retweet(id);
        return "redirect:/tweets/top";
    }
}