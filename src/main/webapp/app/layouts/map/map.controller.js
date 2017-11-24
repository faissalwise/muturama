app.controller('MapController', function(NgMap) {
  NgMap.getMap().then(function(map) {
    console.log(map.getCenter());
    console.log('markers', map.markers);
    console.log('shapes', map.shapes);
    $scope.googleMapsUrl="https://maps.googleapis.com/maps/api/js?key=AIzaSyAOQilJ5ocn1mR6FiLwbIIMUJY1KMoagwY";
  });
});