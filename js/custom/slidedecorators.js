define(['jquery', 'text!../../fragments/title-slide.html'], function($, titleSlideText) {
    return function() {

        var SECTION_HEADER_SELECTOR = '.reveal .slides section h2';

        function addBackButtons() {
            $('body').on('click', SECTION_HEADER_SELECTOR, function() {
                window.location = "/";
            });
        }

        function replaceTitleText(title, names) {
            var slideText = titleSlideText;

            slideText = slideText.replace("{{title}}", title);
            slideText = slideText.replace("{{names}}", names);

            return slideText;
        }

        function setTitleSlide() {
            $('[data-title-slide]').each(function() {
                var element = $(this);
                var titleSlideText = replaceTitleText(element.attr('data-title'), element.attr('data-names'));

                element.html(titleSlideText);
            });
        }

        function isSolutionSlideSet() {
            return (/solution/gi).test(window.location.search);
        }

        function hideSolutions() {
            $('.solution').css('visibility', 'hidden');
            $('.reveal .slides section.solution').remove();
        }

        function hideSolutionsIfNecessary() {
            if (!isSolutionSlideSet()) {
                hideSolutions();
            }
        }

        function initialize() {
            setTitleSlide();
            hideSolutionsIfNecessary();
            addBackButtons();
        }

        return {
            initialize: initialize
        };
    };
});