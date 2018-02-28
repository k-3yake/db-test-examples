package org.k3yake.city.repository

import com.ninja_squad.dbsetup_kotlin.dbSetup
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

@RunWith(SpringRunner::class)
@SpringBootTest
class PrefecturRepositoryTest {

    @Autowired lateinit var prefectureRepository: PrefectureRepository
    @Autowired lateinit var countryRepository: CountryRepository
    @Autowired lateinit var dataSource:DataSource

    @Test
    fun SavaTest(){
        dbSetup(to = dataSource) {
            deleteAllFrom("country")
            insertInto("country"){
                columns("id", "name")
                values(1, "Japan")
            }
        }.launch()
        val tokyo = Prefectur(1, "Tokyo")
        val osaka = Prefectur(2, "Osaka")
        val country = countryRepository.getOne(1)
        country.prefecturs.addAll(listOf(tokyo,osaka))
        countryRepository.save(country)
        Assertions.assertThat(Table(dataSource, "country"))
                .hasNumberOfRows(1)
                .row(0)
                .value("name").isEqualTo("Japan")
        Assertions.assertThat(Table(dataSource, "prefectur"))
                .hasNumberOfRows(2)
                .row(0).value("name").isEqualTo("Tokyo")
                .row(1).value("name").isEqualTo("Osaka")
    }
}