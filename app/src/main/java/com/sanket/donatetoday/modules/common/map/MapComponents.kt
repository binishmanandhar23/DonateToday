package com.sanket.donatetoday.modules.common.map

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.DragState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.sanket.donatetoday.modules.common.CardContainer
import com.sanket.donatetoday.modules.common.DonateTodayCircularButton
import com.sanket.donatetoday.ui.theme.ColorPrimary
import com.sanket.donatetoday.utils.getAddress
import com.sanket.donatetoday.utils.toLatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun DonateTodayMap(
    modifier: Modifier = Modifier,
    properties: MapProperties = MapProperties(isBuildingEnabled = true, isIndoorEnabled = true),
    mapUiSettings: MapUiSettings = MapUiSettings(
        zoomControlsEnabled = true,
        zoomGesturesEnabled = true,
        compassEnabled = true
    ),
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var mapProperties by remember(properties) {
        mutableStateOf(properties)
    }
    var locationPermissionGranted by remember {
        mutableStateOf(false)
    }
    val addCurrentLocationOption: () -> Unit = {
        mapProperties = mapProperties.copy(isMyLocationEnabled = true)
        locationPermissionGranted = true
    }
    val fineLocationPermissionState =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it.all { true })
                addCurrentLocationOption()
        }

    val userLocation = remember {
        LatLng(
            27.712,
            85.32
        )
    }

    var currentLocation: LatLng? by remember {
        mutableStateOf(null)
    }

    val markerState = rememberMarkerState(
        position = userLocation
    )
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation, 10f)
    }


    LaunchedEffect(key1 = Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) or ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> addCurrentLocationOption()

            else -> fineLocationPermissionState.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    LaunchedEffect(key1 = markerState.dragState, key2 = markerState.position) {
        if (markerState.dragState == DragState.END) {
            launch(Dispatchers.IO) {
                getAddress(
                    context = context,
                    latitude = markerState.position.latitude,
                    longitude = markerState.position.longitude
                ) { street, city, country ->

                }
            }
            cameraPositionState.animate(
                update = CameraUpdateFactory.newCameraPosition(
                    CameraPosition.fromLatLngZoom(
                        markerState.position,
                        cameraPositionState.position.zoom
                    )
                ), durationMs = 1000
            )
        }
    }

    if (locationPermissionGranted)
        LocationServices.getFusedLocationProviderClient(context)
            .getCurrentLocation(CurrentLocationRequest.Builder().build(), null)
            .addOnSuccessListener {
                currentLocation = it.toLatLng()
            }



    Box(modifier = modifier) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            properties = properties,
            cameraPositionState = cameraPositionState,
            uiSettings = mapUiSettings,
            onMapClick = {
                markerState.position = it
            }
        ) {
            Marker(
                state = markerState,
                title = "Kathmandu",
                snippet = "This is Kathmandu",
                draggable = true
            )
        }

        LocationToolbar(onBack = onBack)

        DonateTodayCircularButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 8.dp, bottom = 100.dp),
            imageVector = Icons.Rounded.MyLocation,
            onClick = {
                currentLocation?.let {
                    markerState.position = it
                }
            })
    }
}

@Composable
private fun LocationToolbar(
    headerText: String = "Select Location",
    onBack: () -> Unit,
    onClear: (() -> Unit)? = null
) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    MaterialTheme.colors.surface,
                    Color.Transparent
                )
            )
        )
        .padding(horizontal = 16.dp, vertical = 10.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
) {
    IconButton(
        modifier = Modifier,
        onClick = onBack
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBackIos,
            tint = MaterialTheme.colors.primary,
            contentDescription = "Back button"
        )
    }
    Text(
        text = headerText,
        style = MaterialTheme.typography.h5,
        color = MaterialTheme.colors.primary,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        modifier = Modifier
            .widthIn(max = 150.dp)
    )
    if (onClear != null)
        IconButton(
            modifier = Modifier,
            onClick = onClear
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                tint = Color.Black,
                contentDescription = "Clear button"
            )
        }
    else
        Spacer(modifier = Modifier.size(40.dp))
}

@Composable
private fun MapTypeChange(mapProperties: MapProperties, onPropertyChange: (MapProperties) -> Unit) {
    val normalBackgroundColor by animateColorAsState(
        targetValue = if (mapProperties.mapType == MapType.NORMAL) ColorPrimary else Color.Transparent,
        label = ""
    )
    val normalTextColor by animateColorAsState(
        targetValue = if (mapProperties.mapType == MapType.NORMAL) Color.White else ColorPrimary,
        label = ""
    )
    val terrainBackgroundColor by animateColorAsState(
        targetValue = if (mapProperties.mapType == MapType.TERRAIN) ColorPrimary else Color.Transparent,
        label = ""
    )
    val terrainTextColor by animateColorAsState(
        targetValue = if (mapProperties.mapType == MapType.TERRAIN) Color.White else ColorPrimary,
        label = ""
    )
    val satelliteBackgroundColor by animateColorAsState(
        targetValue = if (mapProperties.mapType == MapType.SATELLITE) ColorPrimary else Color.Transparent,
        label = ""
    )
    val satelliteTextColor by animateColorAsState(
        targetValue = if (mapProperties.mapType == MapType.SATELLITE) Color.White else ColorPrimary,
        label = ""
    )
    /*CardContainer(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 10.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                TextButton(onClick = {
                    mapProperties = mapProperties.copy(mapType = MapType.NORMAL)
                }) {
                    Text(
                        modifier = Modifier
                            .background(
                                color = normalBackgroundColor,
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(horizontal = 8.dp, vertical = 5.dp),
                        text = MapType.NORMAL.name.lowercase().capitalize(Locale.current),
                        color = normalTextColor
                    )
                }
                TextButton(onClick = {
                    mapProperties = mapProperties.copy(mapType = MapType.TERRAIN)
                }) {
                    Text(
                        modifier = Modifier
                            .background(
                                color = terrainBackgroundColor,
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(horizontal = 8.dp, vertical = 5.dp),
                        text = MapType.TERRAIN.name.lowercase().capitalize(Locale.current),
                        color = terrainTextColor
                    )
                }
                TextButton(onClick = {
                    mapProperties = mapProperties.copy(mapType = MapType.SATELLITE)
                }) {
                    Text(
                        modifier = Modifier
                            .background(
                                color = satelliteBackgroundColor,
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(horizontal = 8.dp, vertical = 5.dp),
                        text = MapType.SATELLITE.name.lowercase().capitalize(Locale.current),
                        color = satelliteTextColor
                    )
                }
            }
        }*/
}

@Composable
fun DonateTodayAddPlaces(modifier: Modifier = Modifier, onAddNewPlace: () -> Unit) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Add places",
                style = MaterialTheme.typography.h5.copy(
                    color = MaterialTheme.colors.secondary,
                    fontWeight = FontWeight.Bold
                )
            )
            IconButton(onClick = onAddNewPlace) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Add new place",
                    tint = MaterialTheme.colors.primary
                )
            }
        }
    }
}