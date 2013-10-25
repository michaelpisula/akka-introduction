define(["jquery"], function($) {
    return function() {

        function buildRevealStructure() {
            var bodyElements = $('body>section');

            $('body').prepend('<div class="reveal"><div class="slides"></div></div>');
            $('.reveal>.slides').append(bodyElements);
        }

        return {
            buildRevealStructure: buildRevealStructure
        };
    };
});