var mainApp = angular.module("shortUrl", []);
mainApp.controller('shortyC', function($scope, $http) {
    var shortyC = $scope;
    $scope.shorten = function(){
        if ($scope.requestThis != undefined && $scope.requestThis.length >= 3) {
            var res = $http.post('/smallUrl', {"requestThis":$scope.requestThis})
             .then(function onSuccess(response) {
                shortyC.shortenedUrl = response.data.shortenedUrl;
                shortyC.originalUrl = response.data.originalUrl;
              }).catch(function onError(response) {
                shortyC.shortenedUrl = "Error " + response.status + response.statusText + response.headers;
                shortyC.originalUrl = response.data.originalUrl;
              });
        } else {
            shortyC.originalUrl = "Must be over 2 characters!";
            shortyC.shortenedUrl = "And can't be empty string!";
        }
        $scope.requestThis = "";
    };
});

mainApp.controller('DbView', function($scope, $http) {
    $scope.all = function(){
    var DbView = this;
        var res = $http.get('/smallUrl/all')
         .then(function onSuccess(response) {
            console.log(response.data);
            DbView.allList = response.data;
          }).catch(function onError(response) {
            DbView.allList = ["Error " + response.status + response.statusText + response.headers];
          });

    };
});