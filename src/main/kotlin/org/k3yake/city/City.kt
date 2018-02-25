package org.k3yake.city

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.HttpHeaders
import org.springframework.web.context.request.WebRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException


/**
 * Created by katsuki-miyake on 18/02/15.
 */
@ControllerAdvice
@RestController
class CityController: ResponseEntityExceptionHandler() {


  override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers:HttpHeaders, status:HttpStatus, request:WebRequest): ResponseEntity<Any> {
      return ResponseEntity(ErrorDetails(ex.bindingResult.allErrors[0].defaultMessage!!), HttpStatus.BAD_REQUEST);
  }

    @Autowired
    lateinit var cityService:CityService

    @InitBinder
    protected fun initBinder(binder: WebDataBinder) {
        binder.validator = CityValidator()
    }

    @GetMapping("/city")
    fun findCity() : City {
        return cityService.findCity()
    }

    @PostMapping("/city")
    fun createCity(@Valid @RequestBody city: City):City {
        cityService.create(city)
        return city
    }
}

data class ErrorDetails(val message:String)

@Component
class CityValidator: Validator{

    override fun supports(clazz: Class<*>): Boolean {
        return clazz.isAssignableFrom(City::class.java)
    }

    override fun validate(target: Any, errors: Errors) {
        var city = target as City
        if (StringUtils.isEmpty(city.getName())){
            errors.reject("400","Empty name.")
        }
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

@org.springframework.stereotype.Repository
interface CityRepository : JpaRepository<City,Long> {
    fun findByNameAndCountryAllIgnoringCase(name: String, country: String): City
}

@Entity
class City {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long = 0

    @Column(nullable = false)
    private var name: String = ""

    @Column(nullable = false)
    private var state: String  = ""

    @Column(nullable = false)
    private var country: String  = ""

    @Column(nullable = false)
    private val map: String  = ""

    constructor() {    }

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
}
