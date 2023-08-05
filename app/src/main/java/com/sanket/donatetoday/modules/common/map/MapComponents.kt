package com.sanket.donatetoday.modules.common.map

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.sanket.donatetoday.modules.common.CardContainer
import com.sanket.donatetoday.ui.theme.ColorPrimary

@Composable
fun DonateTodayMap(
    modifier: Modifier = Modifier,
    properties: MapProperties = MapProperties(),
    mapUiSettings: MapUiSettings = MapUiSettings(
        zoomControlsEnabled = true,
        zoomGesturesEnabled = true
    )
) {
    var mapProperties by remember(properties) {
        mutableStateOf(properties)
    }
    val normalBackgroundColor by animateColorAsState(targetValue = if (mapProperties.mapType == MapType.NORMAL) ColorPrimary else Color.Transparent,
        label = ""
    )
    val terrainBackgroundColor by animateColorAsState(targetValue = if (mapProperties.mapType == MapType.TERRAIN) ColorPrimary else Color.Transparent,
        label = ""
    )
    val satelliteBackgroundColor by animateColorAsState(targetValue = if (mapProperties.mapType == MapType.SATELLITE) ColorPrimary else Color.Transparent,
        label = ""
    )
    Box(modifier = modifier) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            properties = properties,
            uiSettings = mapUiSettings
        )
        CardContainer(modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = 10.dp)) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                TextButton(onClick = {
                    mapProperties = mapProperties.copy(mapType = MapType.NORMAL)
                }) {
                    Text(
                        modifier = Modifier.background(color = normalBackgroundColor),
                        text = MapType.NORMAL.name.lowercase().capitalize(Locale.current)
                    )
                }
                TextButton(onClick = {
                    mapProperties = mapProperties.copy(mapType = MapType.TERRAIN)
                }) {
                    Text(
                        modifier = Modifier.background(color = terrainBackgroundColor),
                        text = MapType.TERRAIN.name.lowercase().capitalize(Locale.current)
                    )
                }
                TextButton(onClick = {
                    mapProperties = mapProperties.copy(mapType = MapType.SATELLITE)
                }) {
                    Text(
                        modifier = Modifier.background(color = satelliteBackgroundColor),
                        text = MapType.SATELLITE.name.lowercase().capitalize(Locale.current)
                    )
                }
            }
        }
    }
}

@Composable
fun DonateTodayAddPlaces(modifier: Modifier = Modifier, onAddNewPlace: () -> Unit){
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "Add places",
                style = MaterialTheme.typography.h5.copy(
                    color = MaterialTheme.colors.secondary,
                    fontWeight = FontWeight.Bold
                )
            )
            IconButton(onClick = onAddNewPlace) {
                Icon(imageVector = Icons.Default.AddCircle, contentDescription = "Add new place", tint = MaterialTheme.colors.primary)
            }
        }
    }
}