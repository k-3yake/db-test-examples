package org.k3yake.city.api

import org.assertj.db.api.Assertions
import org.assertj.db.type.Table
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.k3yake.Application
import org.k3yake.city.City
import org.k3yake.city.CityController
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.http.ResponseEntity
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.core.env.Environment
import org.springframework.test.web.servlet.result.PrintingResultHandler
import javax.sql.DataSource


/**
 * Created by katsuki-miyake on 18/02/20.
 */
@RunWith(SpringRunner::class)
@ContextConfiguration(classes = arrayOf(Application::class))
@WebAppConfiguration
class CityApiTest {

    @Autowired
    lateinit var wac: WebApplicationContext

    @Autowired
    lateinit var builderProvider: ObjectProvider<RestTemplateBuilder>

    @Autowired
    lateinit var environment: Environment

    lateinit var mockMvc: MockMvc

    lateinit var template:TestRestTemplate

    @Autowired
    @Qualifier("dataSource")
    lateinit var dataSource: DataSource

    @Before
    fun setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    fun getTest() {
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

    @Test
    fun putTest() {
        this.mockMvc.perform(put("/city")
                .content("""{"id":2, "name":"ebisu", "country":"Japan"}""".toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        val table = Table(dataSource, "city", arrayOf(Table.Order.asc("id")))
        Assertions.assertThat(table).row(1)
                .value("id").isEqualTo(2)
                .value("name").isEqualTo("ebisu")
                .value("country").isEqualTo("Japan")

    }

}