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
class CityRepositoryTestByDbSetup {
    @Autowired lateinit var cityRepository: CityRepository
    @Autowired lateinit var dataSource:DataSource
    @Autowired lateinit var countryRepository: CountryRepository

    @Test
    fun Cityの保存のテスト_Countryがまだない場合_CityとCountryが登録される(){
        //準備
        dbSetup(to = dataSource) {
            deleteAllFrom("city","country")
        }.launch()

        //実行
        val country = Country(name = "notExistCountry")
        countryRepository.save(country)
        val city = City(name = "name1", country = country)
        cityRepository.save(city)

        //確認
        Assertions.assertThat(Table(dataSource, "country"))
                .hasNumberOfRows(1)
                .row(0)
                .value("name").isEqualTo("notExistCountry")
        Assertions.assertThat(Table(dataSource, "city"))
                .hasNumberOfRows(1)
                .row(0)
                .value("name").isEqualTo("name1")
    }

    @Test
    fun Cityの保存のテスト_countryが既にある場合_Cityのみが登録される(){
        //準備
        dbSetup(to = dataSource) {
            deleteAllFrom("city","country")
            insertInto("country"){
                columns("id", "name")
                values(1, "Japan")
            }
        }.launch()
        val city = City(name = "name1", country = countryRepository.findById(1).get())

        //実行
        cityRepository.save(city)

        //確認
        Assertions.assertThat(Table(dataSource, "country")).hasNumberOfRows(1)
        Assertions.assertThat(Table(dataSource, "city")).hasNumberOfRows(1)
                .row(0)
                .value("name").isEqualTo("name1")
                .value("country_id").isEqualTo(1)
    }
}