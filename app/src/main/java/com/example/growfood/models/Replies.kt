package com.example.growfood.models

class Replies {
    var description: String = ""
    var key: String = ""
    var time: String = ""
    var person: PersonModel = PersonModel("", "", 0)
    constructor() {

    }
    constructor(
        description: String,
        time: String,
        person: PersonModel
    ) {
        this.description = description
        this.time = time
        this.person = person
    }

}