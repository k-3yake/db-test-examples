package org.k3yake.city.repository

import com.ninja_squad.dbsetup_kotlin.dbSetup
import org.assertj.db.api.Assertions
import org.assertj.db.type.Changes
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
class CityRepositoryTestByDbSetupAndAssertJDB {
    @Autowired lateinit var cityRepository: CityRepository
    @Autowired lateinit var dataSource:DataSource
    @Autowired lateinit var countryRepository: CountryRepository

    @Test
    fun Cityの保存のテスト_Countryがまだない場合_CityとCountryが登録される_テーブルの状態確認によるテスト(){
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
    fun Cityの保存のテスト_Countryがまだない場合_CityとCountryが登録される_テーブルの変更確認によるテスト(){
        //準備
        dbSetup(to = dataSource) {
            deleteAllFrom("city","country")
        }.launch()
        val changes = Changes(dataSource).setStartPointNow() //AssetJ-DBによる変更記録開始

        //実行
        val country = Country(name = "notExistCountry")
        countryRepository.save(country)
        val city = City(name = "name1", country = country)
        cityRepository.save(city)

        //確認
        changes.setEndPointNow() //AssetJ-DBによる変更記録終了
        Assertions.assertThat(changes)
                .hasNumberOfChanges(1)
                .changeOnTable("country")
                .isCreation()
                .rowAtEndPoint()
                .value("name").isEqualTo("notExistCountry")
                .changeOnTable("city")
                .isCreation()
                .rowAtEndPoint()
                .value("name").isEqualTo("name1")
    }


    @Test
    fun Cityの保存のテスト_countryが既にある場合_Cityのみが登録される_テーブルの状態確認によるテスト(){
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
        Assertions.assertThat(Table(dataSource, "country"))
                .hasNumberOfRows(1) //行数は確認しているが変更されていないことを確認していない。手抜きでござる
        Assertions.assertThat(Table(dataSource, "city"))
                .hasNumberOfRows(1)
                .row(0)
                .value("name").isEqualTo("name1")
                .value("country_id").isEqualTo(1)
    }

    @Test
    fun Cityの保存のテスト_countryが既にある場合_Cityのみが登録される_テーブルの変更確認によるテスト(){
        //準備
        dbSetup(to = dataSource) {
            deleteAllFrom("city","country")
            insertInto("country"){
                columns("id", "name")
                values(1, "Japan")
            }
        }.launch()
        val changes = Changes(dataSource).setStartPointNow() //AssetJ-DBによる変更記録開始
        val city = City(name = "name1", country = countryRepository.findById(1).get())

        //実行
        cityRepository.save(city)

        //確認
        changes.setEndPointNow();
        Assertions.assertThat(changes)
                .hasNumberOfChanges(1)//下のアサーションと組み合わせて、cityテーブルのみ変更されていること（countryが変更されていないこと）が確認出来ている
                .changeOnTable("city")
                .isCreation()
                .rowAtEndPoint()
                .value("name").isEqualTo("name1")
    }
}