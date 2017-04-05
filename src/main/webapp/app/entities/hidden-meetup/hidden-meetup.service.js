(function() {
    'use strict';
    angular
        .module('meetupApp')
        .factory('HiddenMeetup', HiddenMeetup);

    HiddenMeetup.$inject = ['$resource', 'DateUtils'];

    function HiddenMeetup ($resource, DateUtils) {
        var resourceUrl =  'api/hidden-meetups/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertDateTimeFromServer(data.date);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
