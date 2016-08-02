package com.burkert.urlparser;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UrlParseController {

    @RequestMapping(value = "/parse", method = RequestMethod.GET)
    public String parseForm(Model model) {
        model.addAttribute("url", new Url());
        return "parse";
    }

    @RequestMapping(value = "/parse", method = RequestMethod.POST)
    public String parseForm(@ModelAttribute Url url, Model model) {
        if (url.getUrl() == null || url.getUrl().trim().isEmpty()) {
            model.addAttribute("error", "Please set URL.");
            return "parse";
        }
        try {
            Document document = Jsoup.connect(url.getUrl()).timeout(3000).get();
            ParseResult parseResult = new UrlParser().parse(document);
            model.addAttribute("result", parseResult);
            model.addAttribute("error", null);
        } catch (HttpStatusException e) {
            String message = String.format("Unable to process URL. Reason: '%s'. HTTP status: %s", e.getMessage(), e.getStatusCode());
            model.addAttribute("error", message);
        } catch (Exception e) {
            String message = String.format("Unable to process URL. Reason: '%s'", e.getMessage());
            model.addAttribute("error", message);
        }

        return "parse";
    }
}
