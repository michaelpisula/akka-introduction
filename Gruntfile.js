/* global module:false */
module.exports = function(grunt) {
    var port = grunt.option('port') || 8000;
    // Project configuration
    grunt.initConfig({

        connect: {
            server: {
                options: {
                    port: port,
                    base: '.'
                }
            }
        },

        watch: {
            main: {
                files: [ 'js/vendor/reveal.js' ],
                tasks: 'default'
            }
        }

    });

    // Dependencies
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-connect');

    // Serve presentation locally
    grunt.registerTask('serve', [ 'connect', 'watch' ]);
};