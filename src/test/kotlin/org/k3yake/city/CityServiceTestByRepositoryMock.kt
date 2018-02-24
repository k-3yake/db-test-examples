package org.k3yake.city


import org.junit.Test
import org.junit.runner.RunWith
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
        Mockito.doReturn(City("name1","country1"))
                .`when`(cityRepository)
                .findByNameAndCountryAllIgnoringCase("Brisbane","Australia")
        val city = cityService.findCity()
        assertEquals("name1",city.getName())
        assertEquals("country1",city.getCountry())
    }
}