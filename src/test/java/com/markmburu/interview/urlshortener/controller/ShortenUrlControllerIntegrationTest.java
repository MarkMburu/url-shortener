package com.markmburu.interview.urlshortener.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.markmburu.interview.urlshortener.dto.OriginalUrl;
import com.markmburu.interview.urlshortener.service.ShortenUrlService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ShortenUrlControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ShortenUrlService shortenUrlService;


    @Test
    public void givenFullUrlReturnStatusOk() throws Exception {
        OriginalUrl originalUrl = new OriginalUrl("https://example.com/foo");

        mvc.perform(post("/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(originalUrl)))
                .andExpect(status().isOk());
    }

    @Test
    public void givenFullUrlReturnJsonWithShortUrlProp() throws Exception {
        OriginalUrl originalUrlObj = new OriginalUrl("https://example.com/foo");

        mvc.perform(post("/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(originalUrlObj)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl").exists());
    }

    @Test
    public void givenFullUrlReturnJsonWithShortUrlValueHasHttp() throws Exception {
        OriginalUrl originalUrl = new OriginalUrl("https://example.com/foo");
        mvc.perform(post("/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(originalUrl)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl", startsWith("http")));
    }

    @Test
    public void shouldNotInsertFullUrlIfAlreadyExists() throws Exception {
        OriginalUrl originalUrl = new OriginalUrl("https://example.com/foo");

        String shortUrl1 = mvc.perform(post("/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(originalUrl)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl", startsWith("http"))).andReturn().getResponse().getContentAsString();

        String shortUrl2 = mvc.perform(post("/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(originalUrl)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl", startsWith("http"))).andReturn().getResponse().getContentAsString();

        Assert.assertEquals(shortUrl1, shortUrl2);
    }

    @Test
    public void shouldNotInsertFullUrlIfDoesNotExist() throws Exception {
        OriginalUrl originalUrl1 = new OriginalUrl("https://example.com/foo1");
        OriginalUrl originalUrl2 = new OriginalUrl("https://example.com/foo2");

        String shortUrl1 = mvc.perform(post("/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(originalUrl1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl", startsWith("http"))).andReturn().getResponse().getContentAsString();

        String shortUrl2 = mvc.perform(post("/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(originalUrl2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl", startsWith("http"))).andReturn().getResponse().getContentAsString();

        Assert.assertNotEquals(shortUrl1, shortUrl2);
    }

    public static String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

}