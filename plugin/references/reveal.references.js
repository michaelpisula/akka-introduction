Reveal.References = function(options) {

    var DATA_NAME_ATTRIBUTE = 'data-name';
    var DATA_REFERENCE_ATTRIBUTE = 'data-reference';

    var REFERENCES_SECTION = '.reveal .slides section[data-references]';
    var REFERENCING_ELEMENT_SELECTOR = '.reveal .slides section [' + DATA_NAME_ATTRIBUTE + '][' + DATA_REFERENCE_ATTRIBUTE + ']';

    (function construct() {
        options = options || {};
        options.maxPerPage = options.maxPerPage || 10;
        options.header = options.header || "";
        options.wrapper = options.wrapper || {};
        options.wrapper.start = options.wrapper.start || "";
        options.wrapper.end = options.wrapper.end || "";
        options.sectionClass = options.sectionClass || null;
        options.referenceClass = options.referenceClass || "reference";
    })();

    function getReferencesElements() {
        return $(REFERENCES_SECTION);
    }

    function getReferencingElements() {
        return $(REFERENCING_ELEMENT_SELECTOR);
    }

    function getReferenceList(referencingElements) {
        var referenceList = [];
        referencingElements.each(function() {
            referenceList.push({name: $(this).attr(DATA_NAME_ATTRIBUTE), reference: $(this).attr(DATA_REFERENCE_ATTRIBUTE)});
        });
        return referenceList;
    }

    function getSectionStartText() {
        return '<section' + (options.sectionClass ? ' class="' + options.sectionClass + '"' : "") + '>' + options.header + options.wrapper.start;
    }

    function getReferenceTextFor(referenceElement) {
        return '<div' + (options.referenceClass ? ' class="' + options.referenceClass + '"' : "") + '>' +
            referenceElement.name + ": " + referenceElement.reference + '</div>';
    }

    function getSectionEndText() {
        return options.wrapper.end + "</section>";
    }

    function getSectionsText(referenceList) {
        var sectionsText = "";
        for (var i = 0; i < referenceList.length; i++) {
            if (i % options.maxPerPage == 0) {
                sectionsText += getSectionStartText()
            }

            sectionsText += getReferenceTextFor(referenceList[i]);

            if (i % options.maxPerPage == options.maxPerPage - 1) {
                sectionsText += getSectionEndText();
            }
        }

        return sectionsText;
    }

    function showReferences(referencesElements, referenceList) {
        var sectionsText = getSectionsText(referenceList);

        referencesElements.append($(sectionsText));
    }

    function createReferences() {
        var referencesElements = getReferencesElements();
        var referencingElements = getReferencingElements();
        var referenceList = getReferenceList(referencingElements);

        showReferences(referencesElements, referenceList);
    }

    return {
        createReferences: createReferences
    };
};