package org.k3yake.city


import org.junit.Test
import org.junit.runner.RunWith
import org.k3yake.city.repository.City
import org.k3yake.city.repository.CityRepository
import org.k3yake.city.repository.Country
import org.k3yake.city.repository.CountryRepository
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals


/**
 * Created by katsuki-miyake on 18/02/17.
 */
@RunWith(MockitoJUnitRunner::class)
class CityServiceTestByRepositoryMock {

    @InjectMocks
    lateinit var cityService: CityService;

    @Mock
    lateinit var cityRepository: CityRepository

    @Mock
    lateinit var countryRepository: CountryRepository

    @Test
    fun City取得ののテスト_Cityを返す() {
        Mockito.doReturn(Country(1,"Australia"))
                .`when`(countryRepository)
                .findByName("Australia")
        Mockito.doReturn(City(name="name1",country = Country("country1")))
                .`when`(cityRepository)
                .findByNameAndCountryId("Brisbane",1)
        val city = cityService.findCity()
        assertEquals("name1",city.name)
        assertEquals("country1",city.country.name)
    }
}