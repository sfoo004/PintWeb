/**
 * Created by Dionny on 11/27/2015.
 */
angular.module('statelessApp')
    .controller('AddEmployeeCtrl', function ($scope, $resource, $routeParams, $uibModalInstance, Logger, existingUser,
                                             Employee) {
        if (existingUser) {
            $scope.mode = "Edit Employee";
            _.extend($scope, existingUser);
        } else {
            $scope.mode = "Add Employee";
        }

        $scope.ok = function () {
            // For better user experience, we should perform the creation here.
            var employeeInformation = {
                email: $scope.email,
                password: $scope.password,
                firstName: $scope.firstName,
                lastName: $scope.lastName,
                phoneNumber: $scope.phoneNumber,
                role: $scope.role
            };

            Employee.addEmployee(employeeInformation)
                .then(function () {
                        $uibModalInstance.close(true);
                    },
                    function (error) {
                        if(!error.data){
                            swal("Oops...", "Something went wrong!", "error");
                        } else {
                            $scope.error = error.data;
                        }
                    });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    });

angular.module('statelessApp')
    .controller('EmployeeCtrl', function ($scope, $resource, $routeParams, $uibModal,
                                          Employee, Authentication, Logger, Features) {
        $scope.features = Features.getFeatures();

        Logger.log('EmployeeCtrl: load');
        $scope.load = function () {
            $scope.contentReady = false;

            if (!Authentication.getRole()) {
                return;
            }

            Logger.log(Authentication.getRole());

            Logger.log('EmployeeCtrl: getting all');
            Employee.getEmployees().then(function (data) {
                $scope.employees = data;
                $scope.contentReady = true;
            });
            Logger.log($scope.employees);

            $scope.rowClick = function (employee) {
                employee.selected = !employee.selected;
            };

            $scope.toggleObjSelection = function ($event, employee) {
                $scope.rowClick(employee);
                $event.stopPropagation();
            };
        };

        $scope.open = function (user) {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'templates/addEmployeeModal.html',
                controller: 'AddEmployeeCtrl',
                resolve: {
                    existingUser: function () {
                        return user;
                    }
                }
            });

            modalInstance.result.then(function (result) {
                if (result) {
                    swal("Success!", "The employee was created successfully.", "success")
                    $scope.load();
                }
            }, function () {
                //$log.info('Modal dismissed at: ' + new Date());
            });
        };
        $scope.$watch(function () {
            return Authentication.getRole();
        }, $scope.load, true);
    });