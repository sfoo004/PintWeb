/**
 * Created by Dionny on 11/27/2015.
 */
angular.module('statelessApp')
    .factory('Features', function () {
        var features = {
            editEmployees: false
        };

        return {
            getFeatures: function () {
                return features;
            }
        }
    });