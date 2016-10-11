var app = angular.module('statelessApp', ['ngResource', 'ngRoute', 'ui.bootstrap', 'ngIdle']).factory('TokenStorage', function () {
    var storageKey = 'auth_token';
    return {
        store: function (token) {
            return localStorage.setItem(storageKey, token);
        },
        retrieve: function () {
            return localStorage.getItem(storageKey);
        },
        clear: function () {
            return localStorage.removeItem(storageKey);
        }
    };
}).factory('TokenAuthInterceptor', function ($q, $location, TokenStorage) {
    return {
        request: function (config) {
            var authToken = TokenStorage.retrieve();
            if (authToken) {
                config.headers['X-AUTH-TOKEN'] = authToken;
            } else {
                $location.path("/");
            }
            return config;
        },
        responseError: function (error) {
            //if (error.status === 401 || error.status === 403) {
            //    TokenStorage.clear();
            //}
            return $q.reject(error);
        }
    };
}).config(function ($httpProvider) {

    $httpProvider.interceptors.push('TokenAuthInterceptor');

}).config(['KeepaliveProvider', 'IdleProvider', function (KeepaliveProvider, IdleProvider) {

    // Production values.
    window.IdleProvider = IdleProvider;

    IdleProvider.idle(1140);
    IdleProvider.timeout(60);

    // Test values.
    //IdleProvider.idle(5);
    //IdleProvider.timeout(5);

}]);

app.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider.
        when('/coordinator', {
            templateUrl: 'templates/bdSummaryPage.html',
            controller: 'BloodDriveCtrl'
        }).
        when('/coordinator/:bloodDriveId', {
            templateUrl: 'templates/bdDetailPageCoordinator.html',
            controller: 'BloodDriveCtrl'
        }).
        when('/nurse', {
            templateUrl: 'templates/bdSummaryPage.html',
            controller: 'BloodDriveCtrl'
        }).
        when('/nurse/:bloodDriveId', {
            templateUrl: 'templates/bdDetailPageNurse.html',
            controller: 'BloodDriveCtrl'
        }).
        when('/manager', {
            templateUrl: 'templates/employees.html',
            controller: 'EmployeeCtrl'
        }).
        otherwise({
            redirectTo: '/'
        });
    }]);

app.factory('Authentication', function () {
    var data = {
        firstName: '',
        lastName: '',
        role: ''
    };

    return {
        getFirstName: function () {
            return data.firstName;
        },
        setFirstName: function (firstName) {
            data.firstName = firstName;
        },
        getLastName: function () {
            return data.lastName;
        },
        setLastName: function (lastName) {
            data.lastName = lastName;
        },
        getRole: function () {
            return data.role;
        },
        setRole: function (role) {
            data.role = role;
        }
    };
});

app.controller('AuthCtrl', function ($scope, $http, TokenStorage, $window, $location, Authentication, Logger, Idle,
                                     $uibModal, $uibModalStack) {
    $scope.authenticated = false;
    $scope.token;

    Logger.log('authCtrl: load');

    function processLogin() {
        $scope.role = $scope.token.roles[0].toLowerCase();

        $scope.authenticated = true;
        $scope.displayName = $scope.user.firstName + " " + $scope.user.lastName;

        Logger.log('authCtrl: setting role to ' + $scope.role);
        Authentication.setRole($scope.role);

        if ($location.absUrl().indexOf('/' + $scope.role + '/') === -1) {
            Logger.log('authCtrl: changing url to /' + $scope.role);
            $location.url('/' + $scope.role);
        }

        // Start the session timeout.
        Idle.watch();
    }

    $scope.init = function () {
        Logger.log('authCtrl: init');
        $http.get('/api/users/current').success(function (user) {
            if (user.username !== 'anonymousUser') {
                $scope.token = JSON.parse(atob(TokenStorage.retrieve().split('.')[0]));
                $scope.user = user;
                processLogin();
            }
        });
    };

    $scope.login = function () {
        Logger.log('authCtrl: login');
        $http.post('/api/login', {
            username: $scope.username,
            password: $scope.password
        }).then(function (result) {
            $scope.error = false;
            TokenStorage.store(result.headers('X-AUTH-TOKEN'));
            $scope.init();
        }, function (result) {
            $scope.error = result.data.message;
        });
    };

    $scope.logout = function () {
        // Just clear the local storage
        TokenStorage.clear();
        $scope.username = "";
        $scope.password = "";
        $scope.authenticated = false;
        $scope.user = null;
        $scope.token = null;
        $scope.displayName = null;
        $scope.roleTemplate = null;
        $location.url('/');

        // Start the session timeout.
        Idle.unwatch();
    };

    function closeModals() {
        if ($scope.warning) {
            $scope.warning.close();
            $scope.warning = null;
        }
    }

    $scope.$on('IdleStart', function () {
        if (!$scope.authenticated) {
            return;
        }

        closeModals();

        $scope.warning = $uibModal.open({
            templateUrl: 'warning-dialog.html',
            windowClass: 'modal-danger'
        });
    });

    $scope.$on('IdleEnd', function () {
        closeModals();
    });

    $scope.$on('IdleTimeout', function () {
        closeModals();
        $uibModalStack.dismissAll();
        $scope.logout();
    });
});