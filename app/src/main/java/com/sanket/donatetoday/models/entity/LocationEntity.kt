package com.sanket.donatetoday.models.entity

import io.realm.kotlin.types.RealmObject

class LocationEntity: RealmObject {
    var fullAddress: String? = null
    var city: String? = null
    var country: String? = null
    var latitude: Double? = null
    var longitude: Double? = null

    constructor(fullAddress: String?, city: String?, country: String?, latitude: Double?, longitude: Double?): super(){
        this.fullAddress = fullAddress
        this.city = city
        this.country = country
        this.latitude = latitude
        this.longitude = longitude
    }
    constructor(): super()
}