package org.k3yake.city

import org.dbunit.Assertion
import org.dbunit.database.DatabaseConnection
import org.dbunit.dataset.DefaultDataSet
import org.dbunit.dataset.excel.XlsDataSet
import org.dbunit.operation.DatabaseOperation
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.io.File
import javax.sql.DataSource
import org.dbunit.dataset.ReplacementDataSet
import org.k3yake.city.repository.City
import org.k3yake.city.repository.Country


/**
 * Created by katsuki-miyake on 18/02/18.
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class CityServiceTestByDbUnit {

    @Autowired
    lateinit var cityService:CityService

    @Autowired
    @Qualifier("dataSource")
    lateinit var dataSource: DataSource

    @Before
    fun setup(){
        var con: DatabaseConnection? = null
        try {
            // テストデータのインサート
            con = DatabaseConnection(dataSource.connection)
            val xlsDataSet = XlsDataSet(File("src/test/resources/testData.xlsx"))
            val city = DefaultDataSet(xlsDataSet.getTable("city"))
            val country = DefaultDataSet(xlsDataSet.getTable("country"))
            DatabaseOperation.DELETE_ALL.execute(con!!, city)
            DatabaseOperation.DELETE_ALL.execute(con!!, country)
            DatabaseOperation.INSERT.execute(con!!, country)
            DatabaseOperation.INSERT.execute(con!!, city)
        }finally{
            con?.close()
        }
    }

    @Test
    fun test(){
        cityService.create(City(name="notExistCityName", country= Country("notExistCoutry")))
        val databaseDataSet = DatabaseConnection(dataSource.connection).createDataSet()
        val expectedDataSet = ReplacementDataSet(XlsDataSet(File("src/test/resources/testData_expect.xlsx")))
        expectedDataSet.addReplacementSubstring("[EmptyStr]","")
        Assertion.assertEqualsIgnoreCols(expectedDataSet.getTable("city"), databaseDataSet.getTable("city"), arrayOf("id","country_id"));
        Assertion.assertEqualsIgnoreCols(expectedDataSet.getTable("country"), databaseDataSet.getTable("country"), arrayOf("id"));
    }
}