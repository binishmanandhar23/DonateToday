package com.sanket.donatetoday.modules.common.map

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.AddCircle
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.maps.android.compose.DragState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.sanket.donatetoday.BuildConfig
import com.sanket.donatetoday.R
import com.sanket.donatetoday.models.dto.LocationDTO
import com.sanket.donatetoday.modules.common.CardContainer
import com.sanket.donatetoday.modules.common.DonateTodayButton
import com.sanket.donatetoday.modules.common.DonateTodayChip
import com.sanket.donatetoday.modules.common.DonateTodayCircularButton
import com.sanket.donatetoday.modules.common.DonateTodaySingleLineTextField
import com.sanket.donatetoday.modules.common.UniversalHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalVerticalPaddingInDp
import com.sanket.donatetoday.ui.theme.ColorPrimary
import com.sanket.donatetoday.utils.bitmapDescriptorFromVector
import com.sanket.donatetoday.utils.emptyIfNull
import com.sanket.donatetoday.utils.getAddress
import com.sanket.donatetoday.utils.toLatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
private fun CoreMapComponent(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.BottomCenter,
    enableSearch: Boolean = true,
    enableMyLocationButton: Boolean = true,
    markerTitle: String? = null,
    markerSnippet: String? = null,
    userLocation: LocationDTO? = null,
    toolbarText: String = "Select Location",
    markers: List<LocationDTO> = emptyList(),
    isDraggable: Boolean = true,
    properties: MapProperties = MapProperties(isBuildingEnabled = true, isIndoorEnabled = true),
    mapUiSettings: MapUiSettings = MapUiSettings(
        zoomControlsEnabled = true,
        zoomGesturesEnabled = true,
        compassEnabled = true
    ),
    onBack: (() -> Unit)? = null,
    onLocation: ((location: LatLng, fullAddress: String, city: String?, country: String?) -> Unit)? = null,
    content: (@Composable AnimatedContentScope.() -> Unit)? = null
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        Places.initialize(
            context,
            BuildConfig.API_KEY
        )
    }
    var contentVisible by remember {
        mutableStateOf(true)
    }
    val defaultLocation = remember(userLocation) {
        LatLng(
            userLocation?.latitude ?: 27.712,
            userLocation?.longitude ?: 85.32
        )
    }
    var currentLocation: LatLng? by remember {
        mutableStateOf(null)
    }

    val markerState = rememberMarkerState(
        position = defaultLocation
    )
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 10f)
    }
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

    LaunchedEffect(key1 = userLocation) {
        if (userLocation == null && currentLocation != null)
            markerState.position = currentLocation!!
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
    LaunchedEffect(key1 = locationPermissionGranted) {
        if (locationPermissionGranted)
            LocationServices.getFusedLocationProviderClient(context)
                .getCurrentLocation(CurrentLocationRequest.Builder().build(), null)
                .addOnSuccessListener {
                    currentLocation = it.toLatLng()
                }
    }

    LaunchedEffect(key1 = markerState.dragState, key2 = markerState.position) {
        contentVisible = false
        if (markerState.dragState == DragState.END) {
            launch(Dispatchers.IO) {
                getAddress(
                    context = context,
                    latitude = markerState.position.latitude,
                    longitude = markerState.position.longitude
                ) { fullAddress, _, city, country ->
                    contentVisible = true
                    onLocation?.invoke(markerState.position, fullAddress, city, country)
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

    Box(modifier = modifier) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            properties = properties,
            cameraPositionState = cameraPositionState,
            uiSettings = mapUiSettings,
            onMapClick = {
                if (isDraggable)
                    markerState.position = it
            }
        ) {
            Marker(
                state = markerState,
                title = markerTitle,
                snippet = markerSnippet,
                icon = context.bitmapDescriptorFromVector(R.drawable.ic_location_pin_black),
                draggable = isDraggable
            )
            markers.forEach {
                if (it.latitude != null && it.longitude != null)
                    Marker(
                        state = MarkerState(position = LatLng(it.latitude, it.longitude)),
                        title = it.fullAddress,
                        snippet = "${it.fullAddress}/n${it.city}, ${it.country}",
                        icon = context.bitmapDescriptorFromVector(R.drawable.ic_location_pin_blue),
                    )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (onBack != null)
                LocationToolbar(headerText = toolbarText, onBack = onBack)
            if (enableSearch)
                SearchLocation {
                    markerState.position = it
                }
        }

        if (enableMyLocationButton)
            DonateTodayCircularButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 8.dp, top = 10.dp),
                imageVector = Icons.Rounded.MyLocation,
                onClick = {
                    currentLocation?.let {
                        markerState.position = it
                    }
                })

        AnimatedContent(
            modifier = Modifier.align(contentAlignment),
            targetState = contentVisible,
            transitionSpec = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up) + fadeIn() togetherWith slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down
                ) + fadeOut()
            },
            label = "Address Box",
        ) { show ->
            if (show)
                content?.invoke(this)
            else
                Spacer(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun ViewLocationOnMap(modifier: Modifier = Modifier, lat: Double, lon: Double, onBack: () -> Unit) {
    var locationDTO by remember {
        mutableStateOf(LocationDTO(latitude = lat, longitude = lon))
    }
    CoreMapComponent(
        modifier = modifier,
        userLocation = locationDTO,
        onBack = onBack,
        contentAlignment = Alignment.BottomCenter,
        enableSearch = false,
        enableMyLocationButton = false,
        toolbarText = "Location",
        isDraggable = false,
        onLocation = { location, fullAddress, city, country ->
            locationDTO = locationDTO.copy(
                fullAddress = fullAddress,
                city = city,
                country = country,
                latitude = location.latitude,
                longitude = location.longitude
            )
        }) {
        CardContainer(
            modifier = Modifier.padding(
                horizontal = UniversalHorizontalPaddingInDp,
                vertical = UniversalVerticalPaddingInDp
            ),
            cardColor = MaterialTheme.colors.background,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = UniversalHorizontalPaddingInDp,
                        vertical = UniversalVerticalPaddingInDp
                    )
                    .animateContentSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_location_pin_blue),
                        contentDescription = locationDTO.fullAddress
                    )
                    Text(
                        text = locationDTO.fullAddress.emptyIfNull(),
                        style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.secondary)
                    )
                }
            }
        }
    }
}

@Composable
fun SelectLocationFromMap(
    modifier: Modifier = Modifier,
    locationDTO: LocationDTO? = null,
    onBack: () -> Unit,
    onLocationUpdate: (LocationDTO) -> Unit
) {
    var locationDTOInner by remember(locationDTO) {
        mutableStateOf(locationDTO ?: LocationDTO())
    }
    CoreMapComponent(
        modifier = modifier,
        userLocation = locationDTO,
        onBack = onBack,
        contentAlignment = Alignment.BottomCenter,
        onLocation = { location, fullAddress, city, country ->
            locationDTOInner = locationDTOInner.copy(
                fullAddress = fullAddress,
                city = city,
                country = country,
                latitude = location.latitude,
                longitude = location.longitude
            )
        }) {
        CardContainer(
            modifier = Modifier.padding(
                horizontal = UniversalHorizontalPaddingInDp,
                vertical = UniversalVerticalPaddingInDp
            ),
            cardColor = MaterialTheme.colors.background,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = UniversalHorizontalPaddingInDp,
                        vertical = UniversalVerticalPaddingInDp
                    )
                    .animateContentSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_location_pin_blue),
                        contentDescription = locationDTOInner.fullAddress
                    )
                    Text(
                        text = locationDTOInner.fullAddress.emptyIfNull(),
                        style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.secondary)
                    )
                }
                DonateTodayButton(
                    text = "Select Location",
                    onClick = {
                        onLocationUpdate(locationDTOInner)
                    }
                )
            }
        }
    }
}

@Composable
fun SelectDropOffLocationsFromMap(
    modifier: Modifier = Modifier,
    dropOffLocations: List<LocationDTO>,
    onBack: () -> Unit,
    onAddDropOffLocation: (LocationDTO) -> Unit,
    onRemove: (LocationDTO) -> Unit
) {
    var locationDTOInner by remember {
        mutableStateOf(LocationDTO())
    }
    CoreMapComponent(
        modifier = modifier,
        toolbarText = "Select drop-off locations",
        userLocation = locationDTOInner,
        markers = dropOffLocations,
        onBack = onBack,
        contentAlignment = Alignment.BottomCenter,
        onLocation = { location, fullAddress, city, country ->
            locationDTOInner = locationDTOInner.copy(
                fullAddress = fullAddress,
                city = city,
                country = country,
                latitude = location.latitude,
                longitude = location.longitude
            )
        }) {
        CardContainer(
            modifier = Modifier
                .padding(
                    horizontal = UniversalHorizontalPaddingInDp,
                    vertical = UniversalVerticalPaddingInDp
                )
                .sizeIn(maxHeight = 320.dp),
            cardColor = MaterialTheme.colors.background,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = UniversalHorizontalPaddingInDp,
                        vertical = UniversalVerticalPaddingInDp
                    )
                    .animateContentSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.ic_location_pin_blue),
                        contentDescription = locationDTOInner.fullAddress
                    )
                    Text(
                        text = locationDTOInner.fullAddress.emptyIfNull(),
                        style = MaterialTheme.typography.subtitle1.copy(color = MaterialTheme.colors.secondary)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DonateTodaySingleLineTextField(
                        modifier = Modifier.weight(0.7f),
                        value = locationDTOInner.title.emptyIfNull(),
                        onValueChange = {
                            locationDTOInner = locationDTOInner.copy(title = it)
                        },
                        label = "Enter name for the location"
                    )
                    DonateTodayButton(modifier = Modifier.weight(0.3f), text = "Add") {
                        onAddDropOffLocation(locationDTOInner)
                    }
                }
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxWidth(),
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    items(dropOffLocations) {
                        DonateTodayChip(
                            innerModifier = Modifier.fillMaxWidth(),
                            text = (it.title ?: it.fullAddress).emptyIfNull()
                        ) {
                            onRemove(it)
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun LocationToolbar(
    headerText: String,
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
private fun SearchLocation(locationText: String = "", onLocation: (LatLng) -> Unit) {
    val context = LocalContext.current

    val intent = Autocomplete.IntentBuilder(
        AutocompleteActivityMode.OVERLAY,
        listOf(Place.Field.NAME, Place.Field.LAT_LNG)
    ).build(context)

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                val place = Autocomplete.getPlaceFromIntent(it.data!!)
                place.latLng?.let { latLng -> onLocation(latLng) }
            }
        }
    CardContainer(onClick = {
        launcher.launch(intent)
    }, cardColor = MaterialTheme.colors.background) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Location",
                tint = MaterialTheme.colors.primary
            )
            Text(
                text = locationText.ifEmpty { "Search Location" },
                style = MaterialTheme.typography.body1.copy(
                    color = MaterialTheme.colors.onBackground.copy(alpha = if (locationText.isEmpty()) 0.6f else 1f)
                )
            )
        }
    }
}

@Composable
private fun MapTypeChange(
    mapProperties: MapProperties,
    onPropertyChange: (MapProperties) -> Unit
) {
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
            modifier = Modifier
                .fillMaxWidth()
                .clickable(role = Role.Button, onClick = onAddNewPlace),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Add place",
                style = MaterialTheme.typography.h5.copy(
                    color = MaterialTheme.colors.secondary,
                    fontWeight = FontWeight.Bold
                )
            )
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = "Add new place",
                tint = MaterialTheme.colors.primary
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DonateTodayAddDropOffLocations(
    modifier: Modifier = Modifier,
    dropOffLocations: List<LocationDTO>,
    onAddLocation: () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "Drop off Locations",
            style = MaterialTheme.typography.h5.copy(
                color = MaterialTheme.colors.secondary,
                fontWeight = FontWeight.Bold
            )
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            stickyHeader {
                DonateTodayCircularButton(
                    imageVector = Icons.Rounded.AddCircle,
                    onClick = onAddLocation
                )
            }
            items(dropOffLocations) {
                if (it.title != null)
                    DonateTodayChip(
                        innerModifier = Modifier.widthIn(min = 70.dp, max = 150.dp),
                        text = it.title
                    ) {

                    }
            }
        }
    }
}