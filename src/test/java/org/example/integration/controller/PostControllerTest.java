package org.example.integration.controller;

import org.example.configuration.AbstractIntegrationTest;
import org.example.myblogspringboot.configuration.DataSourceConfiguration;
import org.example.configuration.TestConfiguration;
import org.example.myblogspringboot.configuration.ThymeleafConfiguration;
import org.example.myblogspringboot.dto.CommentDto;
import org.example.myblogspringboot.dto.PostDto;
import org.example.myblogspringboot.dto.TagDto;
import org.example.myblogspringboot.service.CommentService;
import org.example.myblogspringboot.service.FileService;
import org.example.myblogspringboot.service.PostService;
import org.example.myblogspringboot.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitWebConfig(
        classes = {
                DataSourceConfiguration.class,
                TestConfiguration.class,
                ThymeleafConfiguration.class
        }
)
@Testcontainers
@TestPropertySource("classpath:application-test.properties")
public class PostControllerTest extends AbstractIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private PostService postService;
    @Autowired
    private TagService tagService;
    @Autowired
    private CommentService commentService;
    @MockitoSpyBean
    private FileService fileService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .alwaysDo(print())
                .build();
        DataSource dataSource = webApplicationContext.getBean(DataSource.class);

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("scripts/schema.sql"));
        populator.addScript(new ClassPathResource("scripts/clear_values.sql"));
        populator.addScript(new ClassPathResource("scripts/init.sql"));
        populator.execute(dataSource);
    }

    @Test
    void postsTest() throws Exception {
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(xpath("count(//table//tr)").number(6.0))
                .andExpect(xpath("//table/tbody/tr[6]/td[1][contains(.,Евлоев)]").exists());
    }

    @Test
    void addPageTest() throws Exception {
        mockMvc.perform(get("/posts/add"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("add-post"));
    }

    @Test
    void postTest() throws Exception {
        PostDto foundPost = postService.findById(1L);
        //THEN
        mockMvc.perform(get("/posts/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", foundPost));
    }

    @Test
    void addPostTest() throws Exception {
        File imageFile = ResourceUtils.getFile("classpath:test-image.jpeg");
        byte[] content = Files.readAllBytes(imageFile.toPath());
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test-image.jpeg",
                "image/jpeg",
                content
        );

        mockMvc.perform(multipart("/posts")
                        .file(image)
                        .param("title", "New title")
                        .param("tags", "FirstTag SecondTag")
                        .param("text", "New text"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }

    @Test
    void updatePostTest() throws Exception {
        //GIVEN
        File imageFile = ResourceUtils.getFile("classpath:test-image.jpeg");
        byte[] content = Files.readAllBytes(imageFile.toPath());
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test-image.jpeg",
                "image/jpeg",
                content
        );
        //WHEN
        doNothing().when(fileService).delete(anyString());
        mockMvc.perform(multipart("/posts/{id}", 1L)
                .file(image)
                .param("title", "Updated title")
                .param("tags", "FirstTag SecondTag")
                .param("text", "Updated text"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
        //THEN
        PostDto updatedDto = postService.findById(1L);
        List<TagDto> tags = tagService.findByPostId(1L);
        assertThat(updatedDto.text()).isEqualTo("Updated text");
        assertThat(tags).size().isEqualTo(2);
    }

    @Test
    void updatePageTest() throws Exception {
        PostDto foundPost = postService.findById(1L);
        //THEN
        mockMvc.perform(get("/posts/{id}/edit", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("add-post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", foundPost));
    }

    @Test
    void addCommentTest() throws Exception {
        mockMvc.perform(post("/posts/{id}/comments", 1L)
                        .param("text", "New comment"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
        List<CommentDto> comments = commentService.findByPostId(1L);
        String addedComment = comments.getFirst().comment();
        assertThat(comments).size().isEqualTo(1);
        assertThat(addedComment).isEqualTo("New comment");
    }

    @Test
    void updateCommentTest() throws Exception {
        //GIVEN
        postService.addComment(1L, "New comment");
        List<CommentDto> comments = commentService.findByPostId(1L);
        String addedComment = comments.getFirst().comment();
        assertThat(comments).size().isEqualTo(1);
        assertThat(addedComment).isEqualTo("New comment");
        //THEN
        mockMvc.perform(post("/posts/{postId}/comments/{commentId}", 1L, 1L)
                        .param("text", "Updated comment"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
        List<CommentDto> updatedComments = commentService.findByPostId(1L);
        String updatedComment = updatedComments.getFirst().comment();
        assertThat(updatedComments).size().isEqualTo(1);
        assertThat(updatedComment).isEqualTo("Updated comment");
    }

    @Test
    void deleteCommentTest() throws Exception {
        //GIVEN
        postService.addComment(1L, "First new comment");
        postService.addComment(1L, "Second new comment");
        List<CommentDto> comments = commentService.findByPostId(1L);
        String addedComment = comments.getFirst().comment();
        assertThat(comments).size().isEqualTo(2);
        assertThat(addedComment).isEqualTo("First new comment");
        //THEN
        mockMvc.perform(post("/posts/{postId}/comments/{commentId}/delete", 1L, 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
        List<CommentDto> foundComments = commentService.findByPostId(1L);
        String foundComment = foundComments.getFirst().comment();
        assertThat(foundComments).size().isEqualTo(1);
        assertThat(foundComment).isEqualTo("Second new comment");
    }
}
