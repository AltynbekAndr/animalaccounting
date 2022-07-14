package kz.putinbyte.iszhfermer.entities.animals

import java.io.File

data class AnimalBook(
    var number: String? = null,
    var date: String? = null,
    var pageNumber: String? = null,
    var files: List<Int>? = null
)
