package com.burkert.urlparser;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(UrlParseController.class)
@RunWith(SpringRunner.class)
public class UrlParseControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testBasic() throws Exception {
        mvc.perform(get("/parse"))
                .andExpect(view().name("parse"))
                .andExpect(model().attributeExists("url"))
                .andExpect(status().isOk());

        mvc.perform(post("/parse")
                .param("url", "http://en.wikipedia.org/wiki/Quadtree"))
                .andExpect(view().name("parse"))
                .andExpect(model().attributeExists("result"))
                .andExpect(model().attribute("result", hasProperty("title", Matchers.equalTo("Quadtree - Wikipedia, the free encyclopedia"))))
                .andExpect(status().isOk())
                .andReturn();
    }
}
