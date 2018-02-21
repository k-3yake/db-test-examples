package org.k3yake.city

import org.assertj.db.api.Assertions
import org.assertj.db.type.Table
import org.dbunit.Assertion
import org.dbunit.database.DatabaseConnection
import org.dbunit.dataset.DefaultDataSet
import org.dbunit.dataset.DefaultTable
import org.dbunit.dataset.ITable
import org.dbunit.dataset.ReplacementDataSet
import org.dbunit.dataset.excel.XlsDataSet
import org.dbunit.operation.DatabaseOperation
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional
import java.io.File
import javax.sql.DataSource

/**
 * Created by katsuki-miyake on 18/02/18.
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class CityServiceTestByDbUnitDbSetupLike {

    @Autowired
    lateinit var cityService:CityService

    @Autowired
    @Qualifier("dataSource")
    lateinit var dataSource: DataSource

    @Before
    fun setup(){
        var con: DatabaseConnection? = null
        try {
            con = DatabaseConnection(dataSource.connection)
            val table = table(dataSource,"city"){
                addColum("id", "country", "name", "state", "map")
                addValues(0, "Australia", "Brisbane", "Queensland", "-27.470933, 153.023502")
            }
            DatabaseOperation.CLEAN_INSERT.execute(con!!, DefaultDataSet(table))
        }finally{
            con?.close()
        }
    }

    @Test
    fun test(){
        cityService.create(City("notExistCityName", "notExistCoutry"))
        val databaseDataSet = DatabaseConnection(dataSource.connection).createDataSet()
        val actual = databaseDataSet.getTable("city")
        val expect = table(dataSource,"city"){
                    addColum("id", "country", "name", "state", "map")
                    addValues(0, "Australia", "Brisbane", "Queensland", "-27.470933, 153.023502")
                    addValues(1, "notExistCoutry", "notExistCityName", "", "")
        }
        Assertion.assertEquals(expect, actual);
    }
}

fun table(dataSource: DataSource, name:String, config:Builder.() -> Unit):ITable{
    var builder = Builder()
    builder.config()
    val table = DefaultTable(DatabaseConnection(dataSource.connection).createDataSet().getTable(name).tableMetaData)
    builder.values.forEachIndexed{ rowIndex,row ->
        table.addRow()
        row.forEachIndexed{ columnIndex,value ->
            table.setValue(rowIndex, builder.columns.get(columnIndex), value)
        }
    }
    return table
}

class Builder() {
    var columns:List<String> = listOf()
    var values:List<List<Any>> = listOf()

    fun addColum(vararg added:String){
        columns = columns.plus(added)
    }

    fun addValues(vararg added:Any){
        values = values.plus(listOf(added.asList()))
    }

}