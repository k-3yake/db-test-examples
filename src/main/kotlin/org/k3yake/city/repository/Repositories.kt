package org.k3yake.city.repository

import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.*


/**
 * Created by katsuki-miyake on 18/02/27.
 */
@org.springframework.stereotype.Repository
interface CityRepository : JpaRepository<City,Long> {
    fun findByNameAndCountryAllIgnoringCase(name: String, country: String): City
    fun findByNameAndCountryId(name: String,id: Long): City
}

@Entity
data class City(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false)
    val name: String = "",
    @Column(nullable = false)
    val state: String = "",
    @Column(nullable = false)
    val map: String = "",
    @ManyToOne
    var country: Country = Country()
){
    constructor(name:String,country: String):this(){

    }
    fun country():String{
        return this.country.name
    }
}

@org.springframework.stereotype.Repository
interface PrefectureRepository : JpaRepository<Prefectur,Long> { }

@org.springframework.stereotype.Repository
interface CountryRepository : JpaRepository<Country,Long> {
    fun findByName(name: String):Country
}

@Entity
class Prefectur(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long = 0,
    @Column(nullable = false)
    val name: String = "",
    @ManyToOne
    var country: Country = Country()
){
}

@Entity
data class Country(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false,unique = true)
    val name: String = ""
/*
    @OneToMany(cascade = arrayOf(CascadeType.ALL),fetch = FetchType.EAGER)
    val prefecturs:MutableList<Prefectur> = mutableListOf()
*/
){
    constructor(name: String):this(id = 0,name = name)
}



