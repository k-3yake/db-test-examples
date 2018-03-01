package org.k3yake.city

import org.dbunit.Assertion
import org.dbunit.database.DatabaseConnection
import org.dbunit.dataset.DefaultDataSet
import org.dbunit.dataset.DefaultTable
import org.dbunit.dataset.ITable
import org.dbunit.operation.DatabaseOperation
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.k3yake.city.repository.City
import org.k3yake.city.repository.Country
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
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
            val country = table(dataSource,"country"){
                addColum("id", "name")
                addValues(1,"Australia")
            }
            val city = table(dataSource,"city"){
                addColum("name", "state", "map","country_id")
                addValues("Brisbane", "Queensland", "-27.470933, 153.023502",1)
            }
            DatabaseOperation.DELETE_ALL.execute(con!!, DefaultDataSet(city))
            DatabaseOperation.DELETE_ALL.execute(con!!, DefaultDataSet(country))
            DatabaseOperation.INSERT.execute(con!!, DefaultDataSet(country))
            DatabaseOperation.INSERT.execute(con!!, DefaultDataSet(city))
        }finally{
            con?.close()
        }
    }

    @Test
    fun test(){
        cityService.create(City(name="notExistCityName", country= Country("notExistCoutry")))
        val databaseDataSet = DatabaseConnection(dataSource.connection).createDataSet()
        val expectCountry = table(dataSource,"country"){
                    addColum("name")
                    addValues("Australia")
                    addValues("notExistCoutry")
        }
        Assertion.assertEqualsIgnoreCols(expectCountry, databaseDataSet.getTable("country"), arrayOf("id"));
        val expectCity = table(dataSource,"city"){
            addColum("name", "state", "map")
            addValues("Brisbane", "Queensland", "-27.470933, 153.023502")
            addValues("notExistCityName", "", "")
        }
        Assertion.assertEqualsIgnoreCols(expectCity, databaseDataSet.getTable("city"), arrayOf("id","country_id"));
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