package com.adsulganesh.pokemon

class Pokemon {

    var name:String ?= null
    var desc:String ?= null
    var power:Double ?= null
    var image:Int ?= null
    var lat:Double ?= null
    var log:Double ?= null
    var isCatched:Boolean = false

    constructor(name:String ?= null, desc:String ?= null, power:Double ?= null,
                image:Int ?= null, lat:Double ?= null, log:Double ?= null){

        this.name = name
        this.desc = desc
        this.power = power
        this.image = image
        this.lat = lat
        this.log = log
    }
}