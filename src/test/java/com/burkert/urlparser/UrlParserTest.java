package com.burkert.urlparser;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@Test
public class UrlParserTest {

    // TODO: It is not possible to test inaccessibility in isolation. I would have to make an abstraction to be able to mock it.
    public void testParseWiki() {
        ParseResult result = new UrlParser().parse(Jsoup.parse(loadResourceAsString("wiki.html")));

        assertEquals(result.getTitle(), "Quadtree - Wikipedia, the free encyclopedia");
        assertEquals(result.getVersion(), "5.0");

        assertEquals(result.getHeadingsCountPerLevel().get(1), Integer.valueOf(1));
        assertEquals(result.getHeadingsCountPerLevel().get(2), Integer.valueOf(8));
        assertEquals(result.getHeadingsCountPerLevel().get(3), Integer.valueOf(22));
        assertEquals(result.getHeadingsCountPerLevel().get(4), Integer.valueOf(1));
        assertEquals(result.getHeadingsCountPerLevel().get(5), Integer.valueOf(0));
        assertEquals(result.getHeadingsCountPerLevel().get(6), Integer.valueOf(0));

        assertEquals(result.getExternalLinksCount(), Integer.valueOf(30));
        assertEquals(result.getInternalLinksCount(), Integer.valueOf(233));
        //        assertEquals(result.getInaccessibleLinksCount(), Integer.valueOf(0)); // Unstable for given page and timeout setting.

        assertFalse(result.isContainsLogin());
    }

    public void testParseKotlin() {
        ParseResult result = new UrlParser().parse(Jsoup.parse(loadResourceAsString("kotlin.html")));

        assertEquals(result.getTitle(), "Control Flow - Kotlin Programming Language");
        assertEquals(result.getVersion(), "5.0");

        assertEquals(result.getHeadingsCountPerLevel().get(1), Integer.valueOf(1));
        assertEquals(result.getHeadingsCountPerLevel().get(2), Integer.valueOf(5));
        assertEquals(result.getHeadingsCountPerLevel().get(3), Integer.valueOf(0));
        assertEquals(result.getHeadingsCountPerLevel().get(4), Integer.valueOf(0));
        assertEquals(result.getHeadingsCountPerLevel().get(5), Integer.valueOf(0));
        assertEquals(result.getHeadingsCountPerLevel().get(6), Integer.valueOf(0));

        assertEquals(result.getExternalLinksCount(), Integer.valueOf(3));
        assertEquals(result.getInternalLinksCount(), Integer.valueOf(68));
        assertEquals(result.getInaccessibleLinksCount(), Integer.valueOf(0));

        assertFalse(result.isContainsLogin());
    }

    public void testHtmlVersion4_01AndHeadings() {
        ParseResult result = new UrlParser().parse(Jsoup.parse(loadResourceAsString("version4.01.html")));

        assertEquals(result.getTitle(), "Test");
        assertEquals(result.getVersion(), "4.01 Strict");

        assertEquals(result.getHeadingsCountPerLevel().get(1), Integer.valueOf(1));
        assertEquals(result.getHeadingsCountPerLevel().get(2), Integer.valueOf(1));
        assertEquals(result.getHeadingsCountPerLevel().get(3), Integer.valueOf(1));
        assertEquals(result.getHeadingsCountPerLevel().get(4), Integer.valueOf(1));
        assertEquals(result.getHeadingsCountPerLevel().get(5), Integer.valueOf(1));
        assertEquals(result.getHeadingsCountPerLevel().get(6), Integer.valueOf(2));
    }

    public void testHtmlVersionUnknown() {
        ParseResult result = new UrlParser().parse(Jsoup.parse(loadResourceAsString("versionUnknown.html")));

        assertEquals(result.getTitle(), "Test");
        assertEquals(result.getVersion(), "Unknown");

        assertEquals(result.getHeadingsCountPerLevel().get(1), Integer.valueOf(0));
        assertEquals(result.getHeadingsCountPerLevel().get(2), Integer.valueOf(0));
        assertEquals(result.getHeadingsCountPerLevel().get(3), Integer.valueOf(0));
        assertEquals(result.getHeadingsCountPerLevel().get(4), Integer.valueOf(0));
        assertEquals(result.getHeadingsCountPerLevel().get(5), Integer.valueOf(0));
        assertEquals(result.getHeadingsCountPerLevel().get(6), Integer.valueOf(0));
        assertEquals(result.getExternalLinksCount(), Integer.valueOf(0));
        assertEquals(result.getInternalLinksCount(), Integer.valueOf(0));
        assertEquals(result.getInaccessibleLinksCount(), Integer.valueOf(0));
    }

    public void testParseLogin() {
        ParseResult result = new UrlParser().parse(Jsoup.parse(loadResourceAsString("wiki-login.html")));
        assertTrue(result.isContainsLogin());
    }

    private String loadResourceAsString(String filename) {
        InputStream inputStream = this.getClass().getResourceAsStream(filename);
        try {
            return IOUtils.toString(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
