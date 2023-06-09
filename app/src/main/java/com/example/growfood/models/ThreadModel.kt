package com.example.growfood.models

class ThreadModel {
    var description: String = ""
    var key: String = ""
    var time: String = ""
    var likeCounts: String = ""
    var replies: ArrayList<Replies> = arrayListOf()
    var images: ArrayList<String> = arrayListOf("")
    var person: PersonModel = PersonModel("", "", 0)

    constructor() {

    }

    constructor(
        description: String,
        time: String,
        likeCounts: String,
        replies: ArrayList<Replies>,
        images: ArrayList<String>,
        person: PersonModel
    ) {
        this.description = description
        this.time = time
        this.likeCounts = likeCounts
        this.replies = replies
        this.images = images
        this.person = person
    }

}