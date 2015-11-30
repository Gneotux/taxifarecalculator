angular.module("taxifare").controller('taxiFareForm', function($http, $scope, $window){

    $scope.send = function () {

        $scope.taxiRide= {
            "minutesAboveLimit": $scope.minutesAboveLimit,
            "milesBelowLimit": $scope.milesBelowLimit,
            "dateOfStart": $scope.dateOfStart.getTime()
        };


        $http.post("/taxifare", $scope.taxiRide).then(function(response) {
            $scope.taxiFareResponse = response.data;
            $scope.fareReady = true;
            $scope.$apply();
        }, function(error) {
            $window.alert("Error in the backend:" + error);
        });
    }
});