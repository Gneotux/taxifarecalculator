angular.module("taxifare").directive('taxiFareInfo', function() {
    return {
        templateUrl: '/assets/javascripts/components/taxiFare/taxiFareInfo/taxiFareInfo.html',
        scope: { taxiFareObject: "="}
    }
});