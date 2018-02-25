package org.k3yake.city

import org.dbunit.Assertion
import org.dbunit.database.DatabaseConnection
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
import org.dbunit.dataset.ITable
import org.dbunit.dataset.IDataSet
import org.dbunit.dataset.ReplacementDataSet
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder
import org.dbunit.util.fileloader.XlsDataFileLoader


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
            DatabaseOperation.CLEAN_INSERT.execute(con!!, XlsDataSet(File("src/test/resources/testData.xlsx")))
        }finally{
            con?.close()
        }
    }

    @Test
    fun test(){
        cityService.create(City(name="notExistCityName", country="notExistCoutry"))
        val databaseDataSet = DatabaseConnection(dataSource.connection).createDataSet()
        val actualTable = databaseDataSet.getTable("city")
        val expectedDataSet = ReplacementDataSet(XlsDataSet(File("src/test/resources/testData_expect.xlsx")))
        expectedDataSet.addReplacementSubstring("[EmptyStr]","")
        val expectedTable = expectedDataSet.getTable("city")
        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, arrayOf("id"));
    }
}