package org.k3yake.city

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.*
import org.apache.tomcat.jni.Lock.name
import org.k3yake.greeting.Greeting
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.Repository
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.web.bind.annotation.*


/**
 * Created by katsuki-miyake on 18/02/15.
 */
@RestController
class CityController {

    @Autowired
    lateinit var cityService:CityService

    @GetMapping("/city")
    fun findCity() : City {
        return cityService.findCity()
    }

    @PutMapping("/city")
    fun putCity(@RequestBody city: City):City {
        cityService.create(city)
        return city
    }

}

@Transactional
@Service
class CityService {

    @Autowired
    lateinit var cityRepository:CityRepository

    fun findCity(): City {
        return cityRepository.findByNameAndCountryAllIgnoringCase("Brisbane","Australia")
    }

    fun  create(city: City) {
        cityRepository.save(city)
    }
}

interface CityRepository : JpaRepository<City,Long> {
    fun findByNameAndCountryAllIgnoringCase(name: String, country: String): City
}

@Entity
class City {

    @Id
    var id: Long = 0

    @Column(nullable = false)
    private var name: String = ""

    @Column(nullable = false)
    private var state: String  = ""

    @Column(nullable = false)
    private var country: String  = ""

    @Column(nullable = false)
    private val map: String  = ""

    constructor() {    }

    constructor(id: Long, name: String, country: String) {
        this.id = id
        this.name = name
        this.country = country
    }

    constructor(name: String, country: String) {
        this.name = name
        this.country = country
    }

    fun getName(): String {
        return this.name
    }

    fun getState(): String {
        return this.state
    }

    fun getCountry(): String {
        return this.country
    }

    fun getMap(): String {
        return this.map
    }

    override fun toString(): String {
        return getName() + "," + getState() + "," + getCountry()
    }
}
