package au.edu.rmit.sept.webapp.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTest {

    private static String URL = "/";

    @Autowired
    private MockMvc mvc;

    // @MockBean
    // MovieService service;

    @Autowired
    private HomeController controller;

    // Test that the context is creating the controller
    @Test
    void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    // Test that "VetCare" appears on homepage load
    @Test
    void shouldDisplayTitle() throws Exception {
        mvc.perform(get(URL)).andExpect(status().isOk())
                .andExpect(content().string(containsString("VetCare")));
    }

    // See https://www.geeksforgeeks.org/spring-boot-mockmvc-example/ for more
    // examples
}
