(function() {
    'use strict';
    angular
        .module('meetupApp')
        .factory('Meetup', Meetup);

    Meetup.$inject = ['$resource'];

    function Meetup ($resource) {
        var resourceUrl =  'api/meetups/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
