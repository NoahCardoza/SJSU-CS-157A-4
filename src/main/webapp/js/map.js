const markers = []

function getMapRadius(map) {
    var mapBoundNorthEast = map.getBounds().getNorthEast();
    var mapDistance = mapBoundNorthEast.distanceTo(map.getCenter());
    return mapDistance;
}

function refreshLocationMarkers(map, locations) {
    markers.forEach(marker => marker.remove());
    markers.length = 0;
    locations.forEach(location => {
        const marker = L.marker([location.latitude, location.longitude]).addTo(map);
        let popupHtml = `<b>${location.name}</b>`
        if (location.address) {
            popupHtml += `<br />${location.address}`
        }

        marker.bindPopup(popupHtml);

        marker.on('click', function(e) {
            const amenitiesHtml = location.amenities.map(amenity => `
            <div class="card mb-3">
                <img class="card-img-top" src="${amenity.image.url}" style="height: 200px; width: 100%; object-fit: cover;">
                <div class="card-body">
                    <h5 class="card-title">${amenity.name}</h5>
                    <p class="card-text">${amenity.description}</p>
                    <a type="select" target="_blank" href="/amenities?f=get&id=${amenity.id}" class="btn btn-sm btn-primary">More</a>
                </div>
            </div>
            `).join('');
            $("#amenities-container").addClass("open")
            $("#amenities-container-inner").html(`
                <div class="card my-3">
                    <div class="card-body">
                        <h3>${location.name}</h3>
                        <h4>${location.address || ''}</h4>
                        <a class="btn btn-primary btn-sm" target="_blank" href="/locations?f=get&id=${location.id}">Location Page</a>
                    </div>
                </div>
      
                <div class="mt-3">
                    ${amenitiesHtml}
                </div>
            `)
        });
        markers.push(marker);
    });
}

function refreshLocations(map) {
    const formData = new FormData($('#search-form').get(0));

    const center = map.getCenter();

    formData.append('latitude', center.lat);
    formData.append('longitude', center.lng);
    formData.append('radius', Math.round(getMapRadius(map)));

    fetch(`/locations?f=ajax`, {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(({ locations }) => {
            refreshLocationMarkers(map, locations);
        });

    map.on('click', function(e) {
        $("#amenities-container").removeClass("open")
    })
}

function setupSearchForm(map) {
    $('#search-form').submit(function(e) {
        e.preventDefault();
        onSearchLocations(map);
    });
}

function onSearchLocations(map) {
    const formData = new FormData($('#search-form').get(0));

    const center = map.getCenter();
    formData.append('latitude', center.lat);
    formData.append('longitude', center.lng);
    formData.append('radius', Math.round(getMapRadius(map)));
    $('#search-form').data()
    fetch(`/locations?f=ajax`, {
        method: 'POST',
        body: formData,
    })
        .then(response => response.json())
        .then(({ locations, html }) => {
            refreshLocationMarkers(map, locations);
            $('#search-form-container').html(html);
            setupSearchForm(map);
        });

}

function getUserCoordinates() {
    return new Promise((resolve, reject) => {
        navigator.geolocation.getCurrentPosition(resolve, reject);
    });
}

function createToast({ title, message }) {
    return `
    <div class="toast-header">
        <strong class="mr-auto">${title}</strong>
        <button type="button" class="btn-close me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
    </div>
    <div class="toast-body">
        ${message}
    </div>
    `;
}

(async function main() {

    const { coords } =
        await getUserCoordinates().catch(({ code }) => {
        if (code === 1) {
            $('.toast').html(createToast({
                title: 'Location access denied',
                message: 'Please allow location access for the best experience.'
            })).toast('show');
        } else {
            $('.toast').html(createToast({
                title: 'Location access unavailable',
                message: 'Please allow location access for the best experience.'
            })).toast('show');
        }
        // Default to San Jose State University if location access is denied or unavailable
        return { coords: { latitude: 37.33521652348346, longitude: -121.8810553444476 } };
    });
    const map = L.map('map', {
        minZoom: 10,
        zoomControl: false
    }).setView([coords.latitude, coords.longitude], 17);

    L.control.zoom({
        position: 'bottomright'
    }).addTo(map);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors'
    }).addTo(map);

    refreshLocations(map);
    setupSearchForm(map);

    map.on('moveend', function(e) {
        refreshLocations(map);
    });
})();