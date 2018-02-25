package org.k3yake.city.api

import com.ninja_squad.dbsetup_kotlin.dbSetup
import org.assertj.db.api.Assertions
import org.assertj.db.type.Table
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.k3yake.Application
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import javax.sql.DataSource


/**
 * Created by katsuki-miyake on 18/02/20.
 */
@RunWith(SpringRunner::class)
@ContextConfiguration(classes = arrayOf(Application::class))
@WebAppConfiguration
class CityApiTestBySpringBootTest {

    @Autowired
    lateinit var wac: WebApplicationContext

    lateinit var mockMvc: MockMvc

    @Autowired
    @Qualifier("dataSource")
    lateinit var dataSource: DataSource

    @Before
    fun setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        dbSetup(to = dataSource) {
            deleteAllFrom("city")
            insertInto("city"){
                columns("country", "name", "state", "map")
                values("Australia", "Brisbane", "Queensland", "-27.470933, 153.023502")
            }
        }.launch()
    }

    @Test
    fun getTest() {
        this.mockMvc.perform(get("/city")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name").value("Brisbane"))
                .andExpect(jsonPath("$.state").value("Queensland"))
                .andExpect(jsonPath("$.country").value("Australia"))
                .andExpect(jsonPath("$.map").value("-27.470933, 153.023502"))
                .andReturn()
    }

    @Test
    fun postTest() {
        this.mockMvc.perform(post("/city")
                .content("""{"name":"ebisu", "country":"Japan"}""".toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        Assertions.assertThat(Table(dataSource, "city", arrayOf(Table.Order.asc("id")))).row(1)
                .value("name").isEqualTo("ebisu")
                .value("country").isEqualTo("Japan")
    }

    @Test
    fun postTest_nameがない場合_エラーになる() {
        this.mockMvc.perform(post("/city")
                .content("""{"country":"Japan"}""".toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().`is`(400))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("""{"message":"Empty name."}"""))
        Assertions.assertThat(Table(dataSource, "city", arrayOf(Table.Order.asc("id")))).hasNumberOfRows(1)
    }



}