package org.k3yake.city.repository

import com.ninja_squad.dbsetup_kotlin.dbSetup
import mockit.Expectations
import mockit.Injectable
import mockit.Tested
import org.assertj.db.api.Assertions
import org.assertj.db.type.Changes
import org.assertj.db.type.Table
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import javax.sql.DataSource

/**
 * Created by katsuki-miyake on 18/02/24.
 */
class CityRepositoryTestByMock {
    @Tested
    lateinit var cityDomainRepository: CityDomainRepository
    @Injectable
    lateinit var cityReposiotry: CityRepository
    @Injectable
    lateinit var countryRepository: CountryRepository

    @Test
    fun Cityの保存のテスト_Countryがまだない場合_CityとCountryが登録される(){
        //準備
        object: Expectations() { init{
            val country = Country("notExistCountry")
            countryRepository.findByName(country.name); result = null
            countryRepository.save(country); result = country
            cityReposiotry.save(City(name="name1",country = country))
        }}

        //実行
        val city = CityDomain(name="name1",country = "notExistCountry")
        cityDomainRepository.create(city)
    }

    @Test
    fun Cityの保存のテスト_countryが既にある場合_Cityのみが登録される_テーブルの状態確認によるテスト(){
        //準備
        object: Expectations() { init{
            val country = Country("Japan")
            countryRepository.findByName(country.name); result = country
            cityReposiotry.save(City(name="name1",country = country))
        }}

        //実行
        cityDomainRepository.create(CityDomain(name="name1", country="Japan"))
    }
}