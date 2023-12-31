package com.sanket.donatetoday.models.entity

import io.realm.kotlin.types.RealmObject

class LocationEntity: RealmObject {
    var title: String? = null
    var fullAddress: String? = null
    var city: String? = null
    var country: String? = null
    var latitude: Double? = null
    var longitude: Double? = null

    constructor(title: String?, fullAddress: String?, city: String?, country: String?, latitude: Double?, longitude: Double?): super(){
        this.title = title
        this.fullAddress = fullAddress
        this.city = city
        this.country = country
        this.latitude = latitude
        this.longitude = longitude
    }
    constructor(): super()
}