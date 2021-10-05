package com.example.demo;

import com.example.demo.exception.news.NewsNotFoundException;
import com.example.demo.exception.request.BadRequestParametersException;
import com.example.demo.facade.NewsFacade;
import com.example.demo.repo.NewsRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static com.example.demo.dto.NewsProto.NewsDto;
import static com.example.demo.dto.NewsProto.NewsSimpleDto;
import static com.example.demo.dto.NewsProto.NewsSimpleDtoList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = {"/sql/user_list_before.sql", "/sql/news_list_before.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/news_list_after.sql", "/sql/user_list_after.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
@WithUserDetails("admin")
public class NewsEndPointTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NewsFacade newsFacade;

    @Autowired
    private NewsRepo newsRepo;

    @Test
    public void getNewsList_ShouldReturnNews() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/news"))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/x-protobuf;charset=UTF-8"))
                .andReturn();

        assertEquals(NewsSimpleDtoList.parseFrom(mvcResult.getResponse().getContentAsByteArray()), newsFacade.findAll());
    }


    @Test
    public void getNewsById_ShouldReturnNewsWithId() throws Exception {
        NewsDto newsDto = newsFacade.findById(1L).orElse(NewsDto.getDefaultInstance());

        MvcResult mvcResult = mockMvc.perform(get("/api/news/1"))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/x-protobuf;charset=UTF-8"))
                .andReturn();

        assertEquals(newsDto,
                NewsDto.parseFrom(mvcResult.getResponse().getContentAsByteArray()));
    }

    @Test
    public void getNewsByTitle_ShouldReturnNewsWithTitle() throws Exception {
        NewsSimpleDtoList newsList = newsFacade.findByTitle("Puppies are threat");

        MvcResult mvcResult = mockMvc.perform(get("/api/news?title=Puppies are threat"))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/x-protobuf;charset=UTF-8"))
                .andReturn();

        assertEquals(2,
                NewsSimpleDtoList.parseFrom(mvcResult.getResponse().getContentAsByteArray()).getNewsDtoCount());
        assertEquals(newsList,
                NewsSimpleDtoList.parseFrom(mvcResult.getResponse().getContentAsByteArray()));
    }

    @Test
    public void getNewsByTitleLike_ShouldReturnNewsWithTitleContains() throws Exception {
        NewsSimpleDtoList newsList = newsFacade.findByTitleContains("Puppies");

        MvcResult mvcResult = mockMvc.perform(get("/api/news?textLike=Puppies"))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/x-protobuf;charset=UTF-8"))
                .andReturn();

        assertEquals(5,
                NewsSimpleDtoList.parseFrom(mvcResult.getResponse().getContentAsByteArray()).getNewsDtoCount());
        assertEquals(newsList,
                NewsSimpleDtoList.parseFrom(mvcResult.getResponse().getContentAsByteArray()));
    }

    @Test
    public void getNewsByText_ShouldReturnNewsWithText() throws Exception {
        NewsSimpleDtoList newsList = newsFacade.findByText("Title are the most dangerous than soy milk");

        MvcResult mvcResult =
                mockMvc.perform(get("/api/news?text=Title are the most dangerous than soy milk"))
                        .andExpect(authenticated())
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith("application/x-protobuf;charset=UTF-8"))
                        .andReturn();

        assertEquals(2,
                NewsSimpleDtoList.parseFrom(mvcResult.getResponse().getContentAsByteArray()).getNewsDtoCount());
        assertEquals(newsList,
                NewsSimpleDtoList.parseFrom(mvcResult.getResponse().getContentAsByteArray()));
    }

    @Test
    public void getNewsByTextLike_ShouldReturnNewsWithTextContains() throws Exception {
        NewsSimpleDtoList newsList = newsFacade.findByTextContains("milk");

        MvcResult mvcResult = mockMvc.perform(get("/api/news?textLike=milk"))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/x-protobuf;charset=UTF-8"))
                .andReturn();

        assertEquals(9,
                NewsSimpleDtoList.parseFrom(mvcResult.getResponse().getContentAsByteArray()).getNewsDtoCount());
        assertEquals(newsList,
                NewsSimpleDtoList.parseFrom(mvcResult.getResponse().getContentAsByteArray()));
    }

    @Test
    public void getNewsByNonExistId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/news/-1"))
                .andExpect(authenticated())
                .andExpect(status().isNotFound());
    }

    @Test
    public void createNews_ShouldReturnCreated() throws Exception {
        assertTrue(newsRepo.findByTitle("testnewsname").isEmpty());

        NewsSimpleDto newsDto = NewsSimpleDto.newBuilder()
                .setDate("2000-04-22")
                .setTitle("testnewsname")
                .setText("123")
                .setUserId(4L)
                .build();

        MockHttpServletRequestBuilder requestBuilder = post("/api/news/")
                .contentType("application/x-protobuf;charset=UTF-8")
                .content(newsDto.toByteArray());

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andExpect(status().isCreated());

        assertEquals(1, newsRepo.findByTitle("testnewsname").size());
    }

    @Test
    public void updateNews_ShouldReturnOk() throws Exception {
        NewsSimpleDto newsDto = NewsSimpleDto.newBuilder()
                .setId(3L)
                .setDate("2000-04-22")
                .setTitle("Title for edit new")
                .setText("123")
                .setUserId(4L)
                .build();

        MockHttpServletRequestBuilder requestBuilder = put("/api/news/3")
                .contentType("application/x-protobuf;charset=UTF-8")
                .content(newsDto.toByteArray());

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andExpect(status().isOk());

        assertEquals(1, newsRepo.findByTitle("Title for edit new").size());
        assertTrue(newsRepo.findByTitle("Title for edit").isEmpty());
    }

    @Test
    public void updateNewsWithConflictId_ShouldReturnBadRequestParametersException() throws Exception {
        NewsSimpleDto newsDto = NewsSimpleDto.newBuilder()
                .setId(3L)
                .setDate("2000-04-22")
                .setTitle("Title for edit new")
                .setText("123")
                .setUserId(4L)
                .build();

        MockHttpServletRequestBuilder requestBuilder = put("/api/news/2")
                .contentType("application/x-protobuf;charset=UTF-8")
                .content(newsDto.toByteArray());

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestParametersException));
    }

    @Test
    public void deleteNews_ShouldReturnOk() throws Exception {
        assertEquals(1, newsRepo.findByTitle("Title for delete").size());

        mockMvc.perform(delete("/api/news/6"))
                .andExpect(authenticated())
                .andExpect(status().isOk());

        assertTrue(newsRepo.findByTitle("Title for delete").isEmpty());
    }

    @Test
    public void deleteNonExistNews_ShouldReturnNewsNotFoundException() throws Exception {
        mockMvc.perform(delete("/api/news/-1"))
                .andExpect(authenticated())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NewsNotFoundException));
    }
}
