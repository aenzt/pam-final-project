package com.example.growfood.models

class PersonModel {
    var id: String = ""
    var name: String = ""
    var imgProfile: Int = 0

    constructor() {

    }

    constructor(id: String, name: String, imgProfile: Int) {
        this.name = name
        this.id = id
        this.imgProfile = imgProfile
    }
}