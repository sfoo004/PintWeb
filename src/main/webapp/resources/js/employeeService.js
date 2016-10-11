/**
 * Created by Dionny on 11/27/2015.
 */
angular.module('statelessApp')
    .factory('Employee', function ($resource, $http, $q, Authentication) {
        return {
            getEmployees: function () {
                var deferred = $q.defer();
                var BloodDrive = $resource('/api/' + Authentication.getRole()
                    + '/getEmployees');

                BloodDrive.query(function (data) {
                    deferred.resolve(data);
                });

                return deferred.promise;
            },
            addEmployee: function (employee) {
                return $http.post('/api/' + Authentication.getRole() + '/createEmployee/', employee);
            }
        }
    });