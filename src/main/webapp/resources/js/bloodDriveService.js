/**
 * Created by Dionny on 11/26/2015.
 */
angular.module('statelessApp')
    .factory('BloodDrive', function ($resource, $http, $q, Authentication) {
        return {
            getBloodDrives: function () {
                var deferred = $q.defer();
                var BloodDrive = $resource('/api/' + Authentication.getRole()
                    + '/getBloodDrives');

                BloodDrive.query(function (data) {
                    deferred.resolve(data);
                });

                return deferred.promise;
            },
            getBloodDrive: function (id) {
                var deferred = $q.defer();
                var BD = $resource('/api/' + Authentication.getRole() +
                    '/getBloodDriveById/:bdId',
                    {bdId: '@id'});
                BD.get({bdId: id}, function (data) {
                    deferred.resolve(data);
                });

                return deferred.promise;
            },
            assignNurses: function (id, nurses) {
                return $http.post('/api/' + Authentication.getRole() + '/assignNurses/' + id, nurses);
            },
            unassignNurses: function (id, nurses) {
                return $http.post('/api/' + Authentication.getRole() + '/unassignNurses/' + id, nurses);
            },
            inputDonor: function (id, email) {
                return $http.post('/api/' + Authentication.getRole() + '/inputDonor/' + id, email);
            },
        }
    });