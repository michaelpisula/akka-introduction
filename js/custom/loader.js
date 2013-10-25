define(['jquery'], function($) {

    return function() {
        var callback = null,
            dependenciesToLoad = 0,
            done = false;

        function dependencyLoaded() {
            if (done && dependenciesToLoad == 0 && callback) {
                callback.call(this);
            }
        }

        function load(dependencies, callback) {
            dependenciesToLoad++;
            require(dependencies, function() {
                callback.apply(this, arguments);
                dependenciesToLoad--;
                dependencyLoaded();
            })
        }

        function loadSlideDecorators() {
            load(['custom/slidedecorators'], function(SlideDecorators) {
                SlideDecorators().initialize();
            });
        }

        function loadDependencies() {
            loadSlideDecorators();
        }

        function initialize(_callback) {
            callback = _callback;

            loadDependencies();
            done = true;
            dependencyLoaded();
        }

        return {
            initialize: initialize
        };
    };
});