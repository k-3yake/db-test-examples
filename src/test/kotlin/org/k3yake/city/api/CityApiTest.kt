package org.k3yake.city.api

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.k3yake.Application
import org.k3yake.city.CityController
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext





/**
 * Created by katsuki-miyake on 18/02/20.
 */
@RunWith(SpringRunner::class)
@ContextConfiguration(classes = arrayOf(Application::class))
@WebAppConfiguration
class CityApiTest {

    @Autowired
    lateinit var wac: WebApplicationContext

    lateinit var mockMvc: MockMvc

    @Before
    fun setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    fun listUsers() {
        val resutl = this.mockMvc.perform(get("/city")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name").value("Brisbane"))
                .andExpect(jsonPath("$.state").value("Queensland"))
                .andExpect(jsonPath("$.country").value("Australia"))
                .andExpect(jsonPath("$.map").value("-27.470933, 153.023502"))
                .andReturn()
        println(resutl.response.contentAsString)
    }
}