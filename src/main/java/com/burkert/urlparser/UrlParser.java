package com.burkert.urlparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class UrlParser {

    public ParseResult parse(Document doc) {
        ParseResult result = new ParseResult();
        result.setTitle(doc.title());
        result.setVersion(resolveHtmlVersion(doc));
        result.setHeadingsCountPerLevel(getHeaderNumbersByLevel(doc));

        Elements login = doc.select("form[name='userlogin']"); // I need a better definition on how to search for login forms.
        result.setContainsLogin(!login.isEmpty());

        processLinks(doc, result);

        return result;
    }

    private Map<Integer, Integer> getHeaderNumbersByLevel(Document doc) {
        HashMap<Integer, Integer> result = new HashMap<>();
        for (int i = 1; i < 7; i++) {
            Elements headers = doc.select("h" + i);
            result.put(i, headers.size());
        }

        return result;
    }

    private void processLinks(Document doc, ParseResult result) {
        Elements links = doc.select("a[href]");
        int internal = 0;

        List<String> externalLinks = new ArrayList<>();
        for (Element link : links) {
            String href = link.attr("href");
            if (href.startsWith("http")) { // May consider comparing "abs:href" and "href"
                externalLinks.add(href);
            } else {
                internal++;
            }
        }
        result.setExternalLinksCount(externalLinks.size());
        result.setInternalLinksCount(internal);

        // Execute in parallel since there can be many external links and some may take time to load.
        int inaccessible = externalLinks.parallelStream().map(l -> isAccessible(l) ? 0 : 1).reduce(0, (a, b) -> a + b);
        result.setInaccessibleLinksCount(inaccessible);
    }

    private boolean isAccessible(String href) {
        try {
            System.out.println("Testing link: " + href);
            Jsoup.connect(href).timeout(1000).get();
            return true;
        } catch (IOException e) {
            System.out.println(String.format("The link %s is inaccessible or took too much time to load", href));
            return false;
        }
    }

    private String resolveHtmlVersion(Document doc) {
        String doctype = doc.childNode(0).toString().toLowerCase();
        switch (doctype) {
            case "<!doctype html>":
                return "5.0";
            case "<!doctype html public \"-//w3c//dtd html 4.01//en\" \"http://www.w3.org/tr/html4/strict.dtd\">":
                return "4.01 Strict";
            // TODO: add other versions.
            default:
                return "Unknown";
        }
    }
}
