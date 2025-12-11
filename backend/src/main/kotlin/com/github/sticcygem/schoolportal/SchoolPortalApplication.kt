package com.github.sticcygem.schoolportal

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SchoolPortalApplication

fun main(args: Array<String>) {
    runApplication<SchoolPortalApplication>(*args)
}