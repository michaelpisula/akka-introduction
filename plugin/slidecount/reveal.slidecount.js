$(function() {
    function isPrinted() {
        // TODO better solution
        return (/print-pdf/gi).test(window.location.search);
    }

    function setCurrentSlide() {
        var slides = $('.reveal .slides section').filter(function() {
            var element = $(this);
            return element.find("section").length == 0 && (!(isPrinted() && element.hasClass('not-printed')));
        });

        slides.splice(0, 1);
        var numberOfSlides = slides.length;

        slides.each(function(currentSlideIndex) {
            var element = $(this);
            var currentSlide = currentSlideIndex + 1;
            element.append("<div class='slide-number'>" + currentSlide + "/" + numberOfSlides + '</div>');
        });
    }

    Reveal.addEventListener('ready', setCurrentSlide);
});