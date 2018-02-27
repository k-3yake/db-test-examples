package org.k3yake.city


import org.junit.Test
import org.junit.runner.RunWith
import org.k3yake.city.repository.City
import org.k3yake.city.repository.CityRepository
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

    @Test
    fun City取得ののテスト_Cityを返す() {
        Mockito.doReturn(City(name="name1",country="country1"))
                .`when`(cityRepository)
                .findByNameAndCountryAllIgnoringCase("Brisbane","Australia")
        val city = cityService.findCity()
        assertEquals("name1",city.name)
        assertEquals("country1",city.country)
    }
}