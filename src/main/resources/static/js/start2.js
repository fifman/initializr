$(function () {
    window.selectedArchetypes = [];
    var initializeArchetypeSearchEngine = function (engine) {
        $.getJSON("/ui/archetypes", function (data) {
            window.providedArchetypes = data;
            engine.clear();
            $.each(data, function(idx, item) {
                if(item.weight === undefined) {
                    item.weight = 0;
                }
            });
            engine.add(data);
        });
    };
    var removeTag = function (id) {
        window.selectedArchetypes.splice(window.selectedArchetypes.indexOf(id), 1)
        $("#starters1 div[data-id='" + id + "']").remove();
        toggleDependenciesFromArchetype(id, false);
    };
    var addTag = function (id, name) {
        if ($("#starters1 div[data-id='" + id + "']").length == 0) {
            window.selectedArchetypes.push(id)
            $("#starters1").append("<div class='tag' data-id='" + id + "'>" + name +
                "<button type='button' class='close' aria-label='Close'><span aria-hidden='true'>&times;</span></button>" +
                "<input class='hidden' name='archetypes' value='" + id + "'>" +
                "</div>");
        }
        toggleDependenciesFromArchetype(id, true);
    };

    function findNameByDepId(id){
        if (window.providedDependencies && window.providedDependencies.length) {
            for (var i=0; i<window.providedDependencies.length; i++) {
                if (window.providedDependencies[i].id === id) {
                    return window.providedDependencies[i].name
                }
            }
        }
        return null;
    }

    var addDepTag = function (id) {
        var name = findNameByDepId(id);
        if ($("#starters div[data-id='" + id + "']").length == 0) {
            $("#starters").append("<div class='tag' data-id='" + id + "'>" + name +
                "<button type='button' class='close' aria-label='Close'><span aria-hidden='true'>&times;</span></button></div>");
        }
    };
    var removeDepTag = function (id) {
        $("#starters div[data-id='" + id + "']").remove();
    };

    function findDependenciesFromArchetype(id) {
        for (var i=0; i<window.providedArchetypes.length; i++) {
            if (window.providedArchetypes[i].id === id) {
                return window.providedArchetypes[i].dependencies;
            }
        }
        return null;
    }

    function toggleDependenciesFromArchetype(id, check) {
        var dependencies = findDependenciesFromArchetype(id);
        if (dependencies && dependencies.length) {
            for (var i=0; i< dependencies.length; i++) {
                $("#dependencies input[value='" + dependencies[i] + "']").prop('checked', check);
                if (check)
                    addDepTag(dependencies[i]);
                else
                    removeDepTag(dependencies[i]);
            }
        }
    }

    var maxSuggestions = 5;
    var starters1 = new Bloodhound({
        datumTokenizer: Bloodhound.tokenizers.obj.nonword('name', 'description', 'keywords', 'group', 'id'),
        queryTokenizer: Bloodhound.tokenizers.nonword,
        identify: function (obj) {
            return obj.id;
        },
        sorter: function(a,b) {
            return b.weight - a.weight;
        },
        limit: maxSuggestions,
        cache: false
    });
    initializeArchetypeSearchEngine(starters1);
    $('#autocomplete1').typeahead(
        {
            minLength: 2,
            autoSelect: true
        }, {
            name: 'starters1',
            display: 'name',
            source: starters1,
            templates: {
                suggestion: function (data) {
                    return "<div><strong>" + data.name + "</strong><br/><small>" + data.description + "</small></div>";
                },
                footer: function(search) {
                    if (search.suggestions && search.suggestions.length == maxSuggestions) {
                        return "<div class=\"tt-footer\">More matches, please refine your search</div>";
                    }
                    else {
                        return "";
                    }
                }
            }
        });
    $('#autocomplete1').bind('typeahead:select', function (ev, suggestion) {
        if (window.selectedArchetypes.indexOf(suggestion.id) > -1) {
            removeTag(suggestion.id);
        }
        else {
            addTag(suggestion.id, suggestion.name);
        }
        $('#autocomplete1').typeahead('val', '');
    });
    $("#starters1").on("click", "button", function () {
        var id = $(this).parent().attr("data-id");
        removeTag(id);
    });
    var autocompleteTrap = new Mousetrap($("#autocomplete1").get(0));
    autocompleteTrap.bind(['command+enter', 'alt+enter'], function (e) {
        $("#form").submit();
        return false;
    });
    autocompleteTrap.bind("enter", function(e) {
        if (e.preventDefault) {
            e.preventDefault();
        } else {
            e.returnValue = false;
        }
    });
});
