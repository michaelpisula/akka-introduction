define(["jquery"], function($) {
    return function() {

        function buildRevealStructure() {
            var bodyElements = $('body>section');

            $('body').prepend('<div class="reveal"><div class="slides"></div></div>');
            $('.reveal>.slides').append(bodyElements);

            $('body.auto-fragment .reveal section ul:not(.no-burn) > li,body.auto-fragment .reveal section ol:not(.no-burn) > li').addClass("fragment");
        }

        return {
            buildRevealStructure: buildRevealStructure
        };
    };
});
