angular.module('shortUrl', [])
.controller('shortyC', function($scope, $http) {

    $scope.shorten = function(){
    //{"originalUrl":"asdasdasda","shortenedUrl":"[B@3ca10d26"}
    var shortyC = this;
        var res = $http.post('/smallUrl', {"requestThis":$scope.requestThis})
         .then(function onSuccess(response) {
            // Handle success
            var data = response.data;
            shortyC.shortenedUrl = data.shortenedUrl;
            shortyC.originalUrl = data.originalUrl;
          }).catch(function onError(response) {
            // Handle error
            var data = response.data;
            var status = response.status;
            var statusText = response.statusText;
            var headers = response.headers;
            shortyC.shortenedUrl = "Error " + status + statusText + headers;
            shortyC.originalUrl = data.originalUrl;

          });
        $scope.requestThis = "";
};

});