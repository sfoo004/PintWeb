/**
 * Created by Dionny on 11/24/2015.
 */

angular.module('statelessApp')
    .controller('NurseAssignmentCtrl', function ($scope, $resource, $routeParams, $uibModalInstance, nurses, Logger) {
        $scope.nurses = nurses;

        $scope.rowClick = function (nurse) {
            Logger.log(nurse);
            nurse.selected = !nurse.selected;
        };

        $scope.toggleObjSelection = function ($event, nurse) {
            $scope.rowClick(nurse);
            $event.stopPropagation();
        };

        $scope.selectedNurses = function () {
            if (!$scope.nurses) {
                return 0;
            }
            return _.where($scope.nurses, {'selected': true}).length;
        };

        $scope.ok = function () {
            $uibModalInstance.close(_.where($scope.nurses, {'selected': true}));
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    });

angular.module('statelessApp')
    .controller('InputDonorCtrl', function ($scope, $resource, $routeParams, $uibModalInstance, bloodDriveId,
                                            BloodDrive, Logger) {
        $scope.bloodDriveId = bloodDriveId;

        $scope.ok = function () {
            BloodDrive.inputDonor($scope.bloodDriveId, $scope.email)
                .then(function () {
                        $uibModalInstance.close(true);
                    },
                    function (error) {
                        if (!error.data) {
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
    .controller('BloodDriveCtrl', function ($scope, $resource, $routeParams, $uibModal,
                                            BloodDrive, Authentication, Logger) {
        Logger.log('bloodDriveCtrl: load');
        $scope.load = function () {
            $scope.contentReady = false;

            if (!Authentication.getRole()) {



                return;
            }

            Logger.log(Authentication.getRole());

            $scope.role = Authentication.getRole();
            $scope.bloodDriveId = $routeParams.bloodDriveId;

            if ($scope.bloodDriveId) {
                Logger.log('bloodDriveCtrl: getting details');
                BloodDrive.getBloodDrive($scope.bloodDriveId).then(function (data) {
                    $scope.bloodDrive = data;
                    Logger.log($scope.bloodDrive);
                    $scope.contentReady = true;
                });
            }

            if (!$scope.bloodDriveId) {
                Logger.log('bloodDriveCtrl: getting all');
                BloodDrive.getBloodDrives().then(function (data) {
                    $scope.bloodDrives = data;
                    $scope.contentReady = true;
                });
                Logger.log($scope.bloodDrives);
            }

            $scope.rowClick = function (nurse) {
                nurse.selected = !nurse.selected;
            };

            $scope.toggleObjSelection = function ($event, nurse) {
                $scope.rowClick(nurse);
                $event.stopPropagation();
            };

            $scope.selectedNurses = function () {
                if (!$scope.bloodDrive) {
                    return 0;
                }
                return _.where($scope.bloodDrive.assignedNurses, {'selected': true}).length;
            };

            $scope.removeNurses = function () {
                var selectedNurses = _.where($scope.bloodDrive.assignedNurses, {'selected': true});
                var selected = _.pluck(selectedNurses, 'userId');
                if (!selected || selected.length == 0) {
                    return;
                }

                BloodDrive.unassignNurses($scope.bloodDriveId, selected)
                    .then(function () {
                            swal("Success!", "Nurses were successfully unassigned.", "success")
                            $scope.load();
                        },
                        function () {
                            swal("Oops...", "Something went wrong!", "error");
                        });
            };
        };

        $scope.open = function () {
            if ($scope.role == 'coordinator') {
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'templates/nurseAssignModal.html',
                    controller: 'NurseAssignmentCtrl',
                    size: $scope.bloodDrive.unassignedNurses.length === 0 ? '' : 'lg',
                    resolve: {
                        nurses: function () {
                            _.forEach($scope.bloodDrive.unassignedNurses, function (nurse) {
                                nurse.selected = false;
                            });
                            return $scope.bloodDrive.unassignedNurses;
                        }
                    }
                });

                modalInstance.result.then(function (selectedNurses) {
                    var selected = _.pluck(selectedNurses, 'userId');
                    if (!selected || selected.length == 0) {
                        return;
                    }
                    BloodDrive.assignNurses($scope.bloodDriveId, selected)
                        .then(function () {
                                swal("Success!", "Nurses were successfully assigned.", "success")
                                $scope.load();
                            },
                            function () {
                                swal("Oops...", "Something went wrong!", "error");
                            });

                }, function () {
                    //$log.info('Modal dismissed at: ' + new Date());
                });
            } else if ($scope.role == 'nurse') {
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'templates/inputDonorModal.html',
                    controller: 'InputDonorCtrl',
                    resolve: {
                        bloodDriveId: function () {
                            return $scope.bloodDriveId;
                        }
                    }
                });

                modalInstance.result.then(function (result) {
                    if (result) {
                        swal("Success!", "The donor was successfully recorded.", "success")
                        $scope.load();
                    }
                }, function () {
                    //$log.info('Modal dismissed at: ' + new Date());
                });
            }
        };
        $scope.$watch(function () {
            return Authentication.getRole();
        }, $scope.load, true);
    });