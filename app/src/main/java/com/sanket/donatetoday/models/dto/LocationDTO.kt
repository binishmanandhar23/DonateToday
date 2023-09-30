package com.sanket.donatetoday.models.dto

import com.sanket.donatetoday.models.entity.LocationEntity
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.RealmList

data class LocationDTO(
    val title: String? = null,
    val fullAddress: String? = null,
    val city: String? = null,
    val country: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)

fun LocationDTO.toLocationEntity() = LocationEntity(title, fullAddress, city, country, latitude, longitude)

fun LocationEntity.toLocationDTO() = LocationDTO(title, fullAddress, city, country, latitude, longitude)

fun List<LocationDTO>.toRealmListOfLocationEntity() = map { it.toLocationEntity() }.toRealmList()

fun RealmList<LocationEntity>?.toListOfLocationDTO(): List<LocationDTO> = this?.map { it.toLocationDTO() }?: emptyList()
