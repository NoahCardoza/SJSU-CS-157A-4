const markers = []

function getMapRadius(map) {
    var mapBoundNorthEast = map.getBounds().getNorthEast();
    var mapDistance = mapBoundNorthEast.distanceTo(map.getCenter());
    return mapDistance;
}

function refreshLocations(map) {
    fetch(`/locations?f=ajax&latitude=${map.getCenter().lat}&longitude=${map.getCenter().lng}&radius=${Math.round(getMapRadius(map))}`)
        .then(response => response.json())
        .then(locations => {
            markers.forEach(marker => marker.remove());
            markers.length = 0;
            locations.forEach(location => {
                const marker = L.marker([location.latitude, location.longitude]).addTo(map);
                marker.bindPopup(`<b>${location.name}</b><br>${location.address}`);
                markers.push(marker);
            });
        });
}

function getUserCoordinates() {
    return new Promise((resolve, reject) => {
        navigator.geolocation.getCurrentPosition(resolve, reject);
    });
}




(async function main() {
    const { coords } = await getUserCoordinates();
    const map = L.map('map').setView([coords.latitude, coords.longitude], 13);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors'
    }).addTo(map);

    refreshLocations(map);

    map.on('moveend', function(e) {
        refreshLocations(map);
        // console.log(map.getCenter());
        // console.log(getMapRadius(map));
    });
})();