package com.example.growfood.models

class PersonModel {
    var id: String = ""
    var imgProfile: Int = 0

    constructor() {

    }

    constructor(id: String, imgProfile: Int) {
        this.id = id
        this.imgProfile = imgProfile
    }
}