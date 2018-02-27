package org.k3yake.city.repository

import org.assertj.db.api.Assertions
import org.assertj.db.type.Table
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import javax.sql.DataSource

/**
 * Created by katsuki-miyake on 18/02/24.
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class CityRepositoryTest {

    @Autowired lateinit var cityRepository: CityRepository
    @Autowired lateinit var dataSource:DataSource

    @Test
    fun SavaTest(){
        val currentRowCount = Table(dataSource, "city").getRowsList().size
        val currentRowIndex = currentRowCount - 1
        cityRepository.save(City(name = "name1", country = "country1"))

        Assertions.assertThat(Table(dataSource, "city"))
                .hasNumberOfRows(currentRowCount + 1)
                .row(currentRowIndex + 1)
                .value("name").isEqualTo("name1")
                .value("country").isEqualTo("country1")
    }
}