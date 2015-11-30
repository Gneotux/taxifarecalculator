angular.module("taxifare").config(function($routeProvider){
    $routeProvider
        .when('/', {
            templateUrl:"/assets/javascripts/components/index/welcome.html"
        })
        .when('/taxifareform', {
            templateUrl:"/assets/javascripts/components/taxiFare/taxiFareForm/taxiFareForm.html",
            controller: 'taxiFareForm'
        })
        .when('/taxifareinfo', {
            templateUrl:"/assets/javascripts/components/taxiFare/taxiFareInfo/taxiFareInfo.html",
            controller: 'taxiFareInfo'
        }).otherwise({redirectTo: '/'});
});