(function() {
    'use strict';
    angular
        .module('meetupApp')
        .factory('MeetupGroup', MeetupGroup);

    MeetupGroup.$inject = ['$resource'];

    function MeetupGroup ($resource) {
        var resourceUrl =  'api/meetup-groups/:id';

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
