package org.k3yake.city

import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.k3yake.city.repository.City
import org.k3yake.city.repository.CityRepository
import org.k3yake.city.repository.Country
import org.k3yake.city.repository.CountryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals

/**
 * Created by katsuki-miyake on 18/03/03.
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class AutowiredExample {

    @Autowired lateinit var countryRepository:CountryRepository

    //@Ignore("テストデータのインサートをimport.sqlで行っており、他のテストの影響で失敗するため")
    @Test
    fun DBが使えることのテスト() {
        assertEquals(Country(1,"Australia"),countryRepository.findAll().get(0))
    }

}