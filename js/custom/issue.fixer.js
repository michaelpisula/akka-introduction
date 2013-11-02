define(["jquery"], function($) {
    return function() {

        function fixFragmentStateAfterReloading() {
            // Workaround for Reveal.js issue #684
            Reveal.addEventListener('slidechanged', function() {
                var PAST_SLIDES_SELECTOR = ".reveal .slides section.past, .reveal .slides section.past section";
                var FUTURE_SLIDES_SELECTOR = ".reveal .slides section.future, .reveal .slides section.future section";

                $(".fragment", $(PAST_SLIDES_SELECTOR)).addClass('visible');
                $(".fragment", $(FUTURE_SLIDES_SELECTOR)).removeClass('visible');
            });
        }

        function fixIssues() {
            fixFragmentStateAfterReloading();
        }

        return {
            fixIssues: fixIssues
        };
    };
});