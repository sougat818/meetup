(function() {
    'use strict';
    angular
        .module('meetupApp')
        .factory('Meetup', Meetup);

    Meetup.$inject = ['$resource', 'DateUtils'];

    function Meetup ($resource, DateUtils) {
        var resourceUrl =  'api/meetups/:id';

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
