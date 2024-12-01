package com.example.myfirst.data.entity

data class MyProf(
    val Address: String,
    val BirthDate: String,
    val Citizenship: String,
    val Contacts: String,
    val Email: String,
    val EmailConfirmed: Boolean,
    val EnglishFIO: String,
    val FIO: String,
    val Foreigner: Boolean,
    val Id: String,
    val RecordBooks: List<RecordBookX>,
    val SNILS: String,
    val Sex: String,
    val StudentCod: String
)