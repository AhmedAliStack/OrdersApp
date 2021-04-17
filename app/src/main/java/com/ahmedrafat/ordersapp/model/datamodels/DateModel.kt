package com.ahmedrafat.ordersapp.model.datamodels

class DateModel {
    var day: String
    var month: String
    var year: String
    var date_txt: String


    constructor(day: String, month: String, year: String, date_txt: String) {
        this.day = day
        this.month = month
        this.year = year
        this.date_txt = date_txt
    }
}