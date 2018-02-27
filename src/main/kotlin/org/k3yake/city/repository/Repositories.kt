package org.k3yake.city.repository

import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.*


/**
 * Created by katsuki-miyake on 18/02/27.
 */
@org.springframework.stereotype.Repository
interface CityRepository : JpaRepository<City,Long> {
    fun findByNameAndCountryAllIgnoringCase(name: String, country: String): City
}

@Entity
public data class City(

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Column(nullable = false)
        val name: String = "",

        @Column(nullable = false)
        val state: String = "",

        @Column(nullable = false)
        val country: String = "",

        @Column(nullable = false)
        val map: String = ""
){
}
