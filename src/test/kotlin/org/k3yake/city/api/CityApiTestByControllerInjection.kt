package org.k3yake.city.api

import com.ninja_squad.dbsetup_kotlin.dbSetup
import org.assertj.db.api.Assertions
import org.assertj.db.type.Table
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.k3yake.Application
import org.k3yake.city.CityController
import org.k3yake.city.repository.City
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
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


/**
 * Created by katsuki-miyake on 18/02/20.
 */
@RunWith(SpringRunner::class)
@ContextConfiguration(classes = arrayOf(Application::class))
@WebAppConfiguration
class CityApiTestByControllerInjection {

    @Autowired
    lateinit var cityController: CityController

    lateinit var mockMvc: MockMvc

    @Autowired
    @Qualifier("dataSource")
    lateinit var dataSource: DataSource

    @Before
    fun Befor(){
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
        var city = this.cityController.findCity()
        assertEquals(city.name,"Brisbane")
        assertEquals(city.state,"Queensland")
        assertEquals(city.country,"Australia")
        assertEquals(city.map,"-27.470933, 153.023502")
    }

    @Test
    fun createTest() {
        val city = City(name="ebisu",country="Japan")
        this.cityController.createCity(city)
        Assertions.assertThat(Table(dataSource, "city", arrayOf(Table.Order.asc("id"))))
                .hasNumberOfRows(2)
                .row(1)
                .value("name").isEqualTo("ebisu")
                .value("country").isEqualTo("Japan")
    }
}