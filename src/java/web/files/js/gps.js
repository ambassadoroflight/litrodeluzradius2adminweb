/* global google */

/**
 * Google Map lib Helper.
 */

var LAST_POSITION = 'LAST_POSITION';
var CURRENT_POSITION_ICON_PATH = "framework/images/icons/gps/current_location_marker.png";

/**
 * Agrega marca a el mapa.
 * @param {type} map
 * @param {type} data
 * @returns {undefined}
 */
function addMarker(map, data) {
    new google.maps.Marker({
        position: {lat: data.lat, lng: data.lng},
        map: map
    });
}

/**
 * Agrega marca de ejemplo
 * @param {type} map
 * @param {type} lat
 * @param {type} lng
 * @returns {undefined}
 */
function addSampleMarker(map, lat, lng) {
    new google.maps.Marker({
        position: {lat: lat, lng: lng},
        map: map
    });
}

/**
 * Agrega marcas desde Ajax WebService
 * @param {type} map
 * @param {type} url
 * @returns {undefined}
 */
function addAjaxMarker(map, url) {
    $.ajax({
        method: "POST",
        url: url
    }).done(function (data) {
        for (var i in data) {
            addMarker(map, data[i]);
        }
    });
}

function createMarker(map, element, newLatLng, label) {
    var marker = new google.maps.Marker({
        position: newLatLng,
        map: map,
        title: element.key,
        shape: {
            coords: [6, 6, 6, 36, 34, 36, 34, 6],
            type: 'poly'
        }
    });
    marker.key = element.key;
    addMarkerInfo(map, marker, element);

    if ((label !== null) && (label.trim().length > 0)) {
        marker.label = {
            text: element.name,
            color: "black",
            fontSize: "11pt",
            fontWeight: "bold"
        };
    }

    if (element.iconURL && (element.iconURL !== "")) {
        var image = {
            url: element.iconURL,
            size: new google.maps.Size(40, 58),
            origin: new google.maps.Point(0, 0),
            anchor: new google.maps.Point(20, 29),
        };
        marker.setIcon(image);
    }

    return marker;
}

function createNewCoordinate(element, newLatLng, map, mapCoordinates, mapMarkers, polyline) {
    var marker = createMarker(map, element, newLatLng, "");

    mapMarkers.set(marker.key, marker);

    if (polyline !== null) {
        var path = polyline.getPath();
        path.push(newLatLng);
    }

    mapCoordinates.set(element.key, element);
}

function addAjaxMarkerList(map, mapCoordinates, mapMarkers, polyline, url) {
    $.ajax({
        method: "GET",
        url: url
    }).done(function (data) {
        length = data.length;
        
        mapCoordinates.clear();
        
        mapMarkers.forEach(function (value, key, mapMarkers) {
            value.setMap(null);
        });
        mapMarkers.clear();

        if ((polyline !== null) && (polyline.getPath().length > length)) {
            polyline.getPath().clear();
        }

        for (var i = 0; i < length; i++) {
            var element = data[i];
            var newLatLng = new google.maps.LatLng(element.latitude, element.longitude);

            if (!mapCoordinates.has(element.key)) {
                createNewCoordinate(element, newLatLng, map, mapCoordinates, mapMarkers, polyline);
            }

            if (mapMarkers.has(element.key)) {
                var marker = mapMarkers.get(element.key);
                marker.setPosition(newLatLng);
            }

            if ((polyline !== null) && (i === (length - 1))) {
                if (!mapMarkers.has(LAST_POSITION)) {
                    element.key = LAST_POSITION;
                    element.event = LAST_POSITION;
                    element.iconURL = CURRENT_POSITION_ICON_PATH;

                    var lastpoint = createMarker(map, element, newLatLng, element.name);
                    mapMarkers.set(lastpoint.key, lastpoint);
                } else {
                    var lastpoint = mapMarkers.get(LAST_POSITION);
                    lastpoint.setPosition(newLatLng);
                }
            }
        }
    });
}

function addAjaxMarkerInterval(map, mapCoordinates, mapMarkers, polyline, url, milliseg) {
    addAjaxMarkerList(map, mapCoordinates, mapMarkers, polyline, url);

    setInterval(function () {
        addAjaxMarkerList(map, mapCoordinates, mapMarkers, polyline, url);
    }, milliseg);
}

function addAjaxMarkerIntervalWithoutLine(map, mapCoordinates, mapMarkers, url, milliseg) {
    addAjaxMarkerList(map, mapCoordinates, mapMarkers, null, url);

    setInterval(function () {
        addAjaxMarkerList(map, mapCoordinates, mapMarkers, null, url);
    }, milliseg);
}

function addMarkerInfo(map, marker, element) {
    var markerInfo = element.info;
    var infowindow = new google.maps.InfoWindow({
        content: markerInfo
    });
    marker.addListener('click', function () {
        infowindow.open(map, marker);
    });
}