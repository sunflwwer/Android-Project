package com.example.myapplication

data class HinfoData(
    val sort: String?,
    val FamilyN: String?,
    val ScienceN: String?,
    val name: String?
)

data class PhpResponse(val result: ArrayList<HinfoData>)

