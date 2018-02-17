package org.k3yake.city

import com.ninja_squad.dbsetup.DbSetup
import com.ninja_squad.dbsetup.DbSetupTracker
import com.ninja_squad.dbsetup.Operations
import com.ninja_squad.dbsetup.Operations.deleteAllFrom
import com.ninja_squad.dbsetup.destination.DataSourceDestination
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import javax.sql.DataSource
import org.springframework.beans.factory.annotation.Qualifier
import com.ninja_squad.dbsetup.Operations.insertInto
import com.ninja_squad.dbsetup_kotlin.dbSetup
import com.ninja_squad.dbsetup_kotlin.launchWith
import org.assertj.db.type.Table
import org.junit.Before
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import org.assertj.db.api.Assertions.assertThat
import org.springframework.transaction.annotation.Transactional


/**
 * Created by katsuki-miyake on 18/02/17.
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class CityServiceTestByDbSetup {

    @Autowired
    lateinit var cityService:CityService

    @Autowired
    @Qualifier("dataSource")
    lateinit var dataSource: DataSource

    @Before
    fun Befor(){
        dbSetup(to = dataSource) {
            deleteAllFrom("city")
            insertInto("city"){
                columns("id", "country", "name", "state", "map")
                values(0, "Australia", "Brisbane", "Queensland", "-27.470933, 153.023502")
            }
        }.launch()
    }

    @Test
    fun 都市登録のテスト_同一の国かつ都市名の都市がない場合_登録が成功する() {
        cityService.create(City("notExistCityName", "notExistCoutry"))
        val table = Table(dataSource, "city", arrayOf(Table.Order.asc("id")))
        assertThat(table).row(1)
                .value("id").isEqualTo(1)
                .value("name").isEqualTo("notExistCityName")
                .value("country").isEqualTo("notExistCoutry")
    }
}