$(window).on("load", function () {
    initialize();
    var isMobile = false;
    var myIp = null;
    var pingModel = new PingModel();
    var pingView = new PingView({
        model: pingModel
    });
    var spModel = new ServiceProviderModel();
    var spView = new ServiceProviderView({
        model: spModel
    });
    var jobCollection = new JobCollection();

    setInterval(function () {
        pingModel.fetch({
            success: determineIsMobile

        });

    }, 1000);

    function determineIsMobile(model, xhr, response) {
        // if (sessionToken == null) {
        //     attemptLogin();
        //     return;
        // };
        var mobile = response.xhr.getResponseHeader('response-text');
        if (myIp == null) {
            myIp = pingModel.get("sourceIp");
        } else {
            return;
        }
        if (mobile === "true") {
            var height = $(window).height();
            $("#map-container").resizable({
                handles: "s"
            }).resizable("option", "grid", [0, 10]);
            $("#map-container").css("height", (height / 2) + "px").css("margin-bottom", (height / 6) + "px");
            isMobile = true;
            attemptLogin();

        } else {
            var height = $(window).height();
            $("#map-container").resizable({
                handles: "s"
            }).resizable("option", "grid", [0, 10]);
            $("#map-container").css("height", (height / 1.5) + "px").css("margin-bottom", (height / 6) + "px");
            isMobile = false;
            attemptLogin();
        }

    }
    $("#log-out-sp").on("click", function () {
        $(document).trigger("logout", spView);
    })

    function attemptLogin() {

        if ($.cookie("id") != undefined) {

            var id = $.cookie("id");

            authenticateSp(id).done(function (token) {
                sessionToken = token;
            }).then(function () {
                if (sessionToken == null) {
                    showError("Invalid Username/Password");
                    return;
                }
            });

            spView.model.set("id", id);

            spView.model.fetch({
                success: function () {

                    $("#login-dropdown").removeClass("show");
                    if ($.cookie("id") == undefined) {
                        $.cookie("id", spView.model.get("id"));
                    }
                    showSuccess("Welcome back " + spView.model.get("userName"));
                    initMap();
                    setLoggedIn();
                },
                error: function (model, response, options) {
                    showError(response.xhr.responseText);
                }
            });
        } else {
            _.once(function () {
                showWarning("No ServiceProvider cookie present!");
            });

        }
    }
    $("#exampleDropdownFormPassword1").on("keypress", function (e) {
        //e.preventDefault();
        if (e.which == 13) {
            var id = $("#exampleDropdownFormEmail1").val();

            authenticate().done(function (token) {
                sessionToken = token;
            });
            if (sessionToken == null) {
                showError("Invalid Username/Password");
                return false;
            }
            // (sessionToken);
            spView.model.set("id", id);

            spView.model.fetch({
                beforeSend: sendAuthentication,
                success: function () {
                    if ($.cookie("id") == undefined) {
                        $.cookie("id", spView.model.get("id"));
                    }

                    $("#login-dropdown").removeClass("show");
                    initMap();
                    setLoggedIn();
                    showSuccess("Login sucessful " + spView.model.get("userName"));
                },
                error: function (model, response, options) {
                    showError(response.xhr.responseText);
                }
            });
        }

    });
    $("#fetch-data-submit").on("click", function (e) {
        e.preventDefault();
        var id = $("#exampleDropdownFormEmail1").val();
        var pass = $("#exampleDropdownFormPassword1").val();
        authenticate().done(function (token) {
            sessionToken = token;
        });
        if (sessionToken == null) return null;

        // (sessionToken);
        spView.model.set("id", id);
        // spModel.set("username", id);
        spView.model.fetch({
            beforeSend: sendAuthentication,
            success: function () {
                if ($.cookie("id") == undefined) {
                    $.cookie("id", spView.model.get("id"));
                }
                $("#login-dropdown").removeClass("show");
                initMap();
                setLoggedIn();
            },
            error: function (model, response, options) {
                showError(response.xhr.responseText);
            }
        });
    });



    $("#geoloc").on("click", function (e) {
        e.preventDefault();
        navigator.geolocation.getCurrentPosition(geoFetchSuccess);

        function geoFetchSuccess(pos) {
            var crd = pos.coords;
            var lastPing = pingView.model;
            var currLoc = {};

            currLoc.lat = crd.latitude;
            currLoc.lng = crd.longitude;
            currLoc.dateTime = lastPing.get("timestamp");

            spView.model.set("loc", currLoc)
            spView.model.save().done(function () {
                initMap();
            });
        };
    });
    function isInfoWindowOpen(infoWindow){
    var map = infoWindow.getMap();
    return (map !== null && typeof map !== "undefined");
}
    function initMap() {
        var jobInfoWindows = [];
        var jobMarkers = [];
        var myLocation = constructLatLng(spView.model);
        var myLocationInfo = new google.maps.InfoWindow;
        var map = new google.maps.Map(document.getElementById('map'), {
            center: myLocation,
            zoom: 9
        });
        var myPos = new google.maps.Marker({
            map: map,
            position: myLocation,
            draggable: true,
            label: {text: "Me", color: "white"},
            gestureHandling: 'cooperative',
            zIndex: 99999
        });
        myLocationInfo.setPosition(myLocation);
        map.dragInProgress = false;
       
        google.maps.event.addListener(myPos, 'dragend', function (evt) {

            $('#loc-modal').modal('show');
            $('#loc-modal-confirm').one('click', function (e) {

                $('#loc-modal').modal('hide');

                var newLat = evt.latLng.lat();
                var newLng = evt.latLng.lng();

                var currLoc = constructLatLng(spView.model);

                currLoc.lat = newLat;
                currLoc.lng = newLng;
                currLoc.dateTime = pingView.model.get("timestamp");
                spView.model.set({
                    loc: currLoc
                });
                spView.model.save()
                getAddress(spView.model).done(function (value) {
                    write(value);
                    spView.model.set({
                        friendlyLocation: value
                    });
                    spView.model.save();

                    myLocationInfo.setPosition(constructLatLng(spView.model));
                    myPos.setPosition(constructLatLng(spView.model));
                    myPos.setMap(map);
                    myLocationInfo.setContent($("<span>").text(value).append($("<br>")).append($("<h5>").text("My Location")).html());

                    myLocationInfo.open(map, myPos);
                });
                

            });
            $("#loc-modal-deny").on("click", function () {
                getAddress(spView.model).done(function (value) {
                    write(value);
                    spView.model.set({
                        friendlyLocation: value
                    });
                    spView.model.save();

                    myLocationInfo.setPosition(constructLatLng(spView.model));
                    myPos.setPosition(constructLatLng(spView.model));
                    myPos.setMap(map);
                    myLocationInfo.setContent($("<span>").text(value).append($("<br>")).append($("<h5>").text("My Location")).html());

                    myLocationInfo.open(map, myPos);
                });
            });

            // (sessionToken);
            // getAddress(sp).done(function (value) {
            // mi.label=(value);
            // //infoWindow.setContent(value);
            // });

        });

        $("#search-clear-icon").on("click", function (e) {

            jobMarkers = deleteMarkers(jobMarkers);
            jobInfoWindows = deleteWindows(jobInfoWindows);
            $("#job-search-input").val("");
            $("#fetch-search").text("Fetch all");

        });
        $("#fetch-search").on("click", function (e) {

            var query = $("#job-search-input").val();

            write(query);
            if (query.length > 1) {
                $("#fetch-search").text("Search jobs");
                jobMarkers = deleteMarkers(jobMarkers);
                jobInfoWindows = deleteWindows(jobInfoWindows);
                var jobSearch = new JobQueryModel({
                    id: query
                    // urlRoot:
                    // "v1/job-search/"+sp.get("id")+"/"+query+"/"+$("#radius-slider").slider("value")
                });
                jobSearch.fetch({
                    success: function (jobs, response, options) {
                        jobCollection.reset(null);
                        jobCollection.set(jobCollection.parse(response));

                        if (jobCollection.length > 0) {
                            showSuccess(options.xhr.getResponseHeader('response-text'));
                        } else {
                            showError(options.xhr.getResponseHeader('response-text'));
                        }
                        jobCollection.each(function (job) {

                            var jobPos = constructGLatLng(job);
                            var jobInfoWindow = new google.maps.InfoWindow;
                            var miLabel = newMarkerLabelJob(job)
                            miLabel.set('position', jobPos);
                            var mi = new google.maps.Marker();
                            mi.bindTo('map', miLabel);
                            mi.bindTo('position', miLabel);

                            // jobInfoWindow.setPosition(jobPos);
                            var content;
                            if (job.has("friendlyLocation")) {
                                content = '<div id="iw-container">' +
                                    '<div class="iw-title">' + miLabel.text + '</div>' +
                                    '<div class="iw-content">' +
                                    '<div class="iw-subTitle">Location</div>' +
                                    '<span>' + job.get("friendlyLocation") + '</span>' +
                                    '<div class="iw-subTitle">Description</div>' +
                                    '<span>' + job.get("shortDescription") + '</span>' +
                                    '<div class="iw-subTitle">Info</div>' +
                                    '<i class="fa fa-money">pay: ' + '&#36;' + job.get("pay") + '</i>' +
                                    '</div>' +
                                    '<div class="iw-bottom-gradient"></div>' +
                                    '</div>';
                                jobInfoWindow.setContent(content);
                                mi.setMap(map);
                                mi.addListener('mouseover', function () {
                                    jobInfoWindow.open(map, mi);
                                });
                                mi.addListener('mouseout', function () {
                                    jobInfoWindow.close();
                                });
                                jobInfoWindows.push(jobInfoWindow);
                                jobMarkers.push(mi);
                            } else {
                                fetchJobGeoloc(job).done(function (value) {
                                    job.set("friendlyLocation", value);
                                    job.save();
                                    content = '<div id="iw-container">' +
                                        '<div class="iw-title">' + miLabel.text + '</div>' +
                                        '<div class="iw-content">' +
                                        '<div class="iw-subTitle">Location</div>' +
                                        '<span>' + job.get("friendlyLocation") + '</span>' +
                                        '<div class="iw-subTitle">Description</div>' +
                                        '<span>' + job.get("shortDescription") + '</span>' +
                                        '<div class="iw-subTitle">Info</div>' +
                                        '<i class="fa fa-money">pay: ' + '&#36;' + job.get("pay") + '</i>' +
                                        '</div>' +
                                        '<div class="iw-bottom-gradient"></div>' +
                                        '</div>';
                                    jobInfoWindow.setContent(content);
                                    mi.setMap(map);
                                    mi.addListener('mouseover', function () {
                                        jobInfoWindow.open(map, mi);
                                    });
                                    mi.addListener('mouseout', function () {
                                        jobInfoWindow.close();
                                    });
                                    jobInfoWindows.push(jobInfoWindow);
                                    jobMarkers.push(mi);

                                });
                            }
                        });
                    }
                });

            } else {
                jobMarkers = deleteMarkers(jobMarkers);
                jobInfoWindows = deleteWindows(jobInfoWindows);
                fetchJobs().done(function (jobs) {

                    jobs.each(function (job) {
                        (job);
                        var jobPos = constructGLatLng(job);
                        var jobInfoWindow = new google.maps.InfoWindow;
                        var miLabel = newMarkerLabelJob(job)
                        miLabel.set('position', jobPos);
                        var mi = new google.maps.Marker();
                        mi.bindTo('map', miLabel);
                        mi.bindTo('position', miLabel);

                        // jobInfoWindow.setPosition(jobPos);
                        var content;
                        if (job.has("friendlyLocation")) {
                            content = '<div id="iw-container">' +
                                '<div class="iw-title">' + miLabel.text + '</div>' +
                                '<div class="iw-content">' +
                                '<div class="iw-subTitle">Location</div>' +
                                '<span>' + job.get("friendlyLocation") + '</span>' +
                                '<div class="iw-subTitle">Description</div>' +
                                '<span>' + job.get("shortDescription") + '</span>' +
                                '<div class="iw-subTitle">Info</div>' +
                                '<i class="fa fa-money">pay: ' + '&#36;' + job.get("pay") + '</i>' +
                                '</div>' +
                                '<div class="iw-bottom-gradient"></div>' +
                                '</div>';
                            jobInfoWindow.setContent(content);
                            mi.setMap(map);
                            mi.addListener('mouseover', function () {
                                jobInfoWindow.open(map, mi);
                            });
                            mi.addListener('mouseout', function () {
                                jobInfoWindow.close();
                            });
                            jobInfoWindows.push(jobInfoWindow);
                            jobMarkers.push(mi);
                        } else {
                            fetchJobGeoloc(job).done(function (value) {
                                job.set("friendlyLocation", value);
                                job.save();
                                content = '<div id="iw-container">' +
                                    '<div class="iw-title">' + miLabel.text + '</div>' +
                                    '<div class="iw-content">' +
                                    '<div class="iw-subTitle">Location</div>' +
                                    '<span>' + job.get("friendlyLocation") + '</span>' +
                                    '<div class="iw-subTitle">Description</div>' +
                                    '<span>' + job.get("shortDescription") + '</span>' +
                                    '<div class="iw-subTitle">Info</div>' +
                                    '<i class="fa fa-money">pay: ' + '&#36;' + job.get("pay") + '</i>' +
                                    '</div>' +
                                    '<div class="iw-bottom-gradient"></div>' +
                                    '</div>';
                                jobInfoWindow.setContent(content);
                                mi.setMap(map);
                                mi.addListener('mouseover', function () {
                                    jobInfoWindow.open(map, mi);
                                });
                                mi.addListener('mouseout', function () {
                                    jobInfoWindow.close();
                                });
                                jobInfoWindows.push(jobInfoWindow);
                                jobMarkers.push(mi);

                            });
                        }
                    });

                });
            }

        });
        $("#map").on("search", function (e, str) {

            var query = str;

            write(query);


            jobMarkers = deleteMarkers(jobMarkers);
            jobInfoWindows = deleteWindows(jobInfoWindows);
            var jobSearch = new JobQueryModel({
                id: query
                // urlRoot:
                // "v1/job-search/"+sp.get("id")+"/"+query+"/"+$("#radius-slider").slider("value")
            });
            jobSearch.fetch({
                success: function (jobs, response, options) {
                    jobCollection.reset(null);
                    jobCollection.set(jobCollection.parse(response));

                    if (jobCollection.length > 0) {
                        showSuccess(options.xhr.getResponseHeader('response-text'));
                    } else {
                        showError(options.xhr.getResponseHeader('response-text'));
                    }
                    jobCollection.each(function (job) {

                        var jobPos = constructGLatLng(job);
                        var jobInfoWindow = new google.maps.InfoWindow;
                        var miLabel = newMarkerLabelJob(job)
                        miLabel.set('position', jobPos);
                        var mi = new google.maps.Marker();
                        mi.bindTo('map', miLabel);
                        mi.bindTo('position', miLabel);

                        // jobInfoWindow.setPosition(jobPos);
                        var content;
                        if (job.has("friendlyLocation")) {
                            content = '<div id="iw-container">' +
                                '<div class="iw-title">' + miLabel.text + '</div>' +
                                '<div class="iw-content">' +
                                '<div class="iw-subTitle">Location</div>' +
                                '<span>' + job.get("friendlyLocation") + '</span>' +
                                '<div class="iw-subTitle">Description</div>' +
                                '<span>' + job.get("shortDescription") + '</span>' +
                                '<div class="iw-subTitle">Info</div>' +
                                '<i class="fa fa-money">pay: ' + '&#36;' + job.get("pay") + '</i>' +
                                '</div>' +
                                '<div class="iw-bottom-gradient"></div>' +
                                '</div>';
                            jobInfoWindow.setContent(content);
                            mi.setMap(map);
                            mi.addListener('mouseover', function () {
                                jobInfoWindow.open(map, mi);
                            });
                            mi.addListener('mouseout', function () {
                                jobInfoWindow.close();
                            });
                            jobInfoWindows.push(jobInfoWindow);
                            jobMarkers.push(mi);
                        } else {
                            fetchJobGeoloc(job).done(function (value) {
                                job.set("friendlyLocation", value);
                                job.save();
                                content = '<div id="iw-container">' +
                                    '<div class="iw-title">' + miLabel.text + '</div>' +
                                    '<div class="iw-content">' +
                                    '<div class="iw-subTitle">Location</div>' +
                                    '<span>' + job.get("friendlyLocation") + '</span>' +
                                    '<div class="iw-subTitle">Description</div>' +
                                    '<span>' + job.get("shortDescription") + '</span>' +
                                    '<div class="iw-subTitle">Info</div>' +
                                    '<i class="fa fa-money">pay: ' + '&#36;' + job.get("pay") + '</i>' +
                                    '</div>' +
                                    '<div class="iw-bottom-gradient"></div>' +
                                    '</div>';
                                jobInfoWindow.setContent(content);
                                mi.setMap(map);
                                mi.addListener('mouseover', function () {
                                    jobInfoWindow.open(map, mi);
                                });
                                mi.addListener('mouseout', function () {
                                    jobInfoWindow.close();
                                });
                                jobInfoWindows.push(jobInfoWindow);
                                jobMarkers.push(mi);

                            });
                        }
                    });
                }
            });
        });

        $('#job-search-input').on('input', function (e) {

            var input = $(this).val();

            if (input.length == 0) {

                $("#fetch-search").text("Get All");
                jobMarkers = deleteMarkers(jobMarkers);
                jobInfoWindows = deleteWindows(jobInfoWindows);
                fetchJobs().done(function (jobs) {

                    jobCollection.each(function (job) {
                        // (job);
                        var jobPos = constructGLatLng(job);
                        var jobInfoWindow = new google.maps.InfoWindow;
                        var miLabel = newMarkerLabelJob(job)
                        miLabel.set('position', jobPos);
                        var mi = new google.maps.Marker();
                        mi.bindTo('map', miLabel);
                        mi.bindTo('position', miLabel);

                        // jobInfoWindow.setPosition(jobPos);
                        var content;
                        if (job.has("friendlyLocation")) {
                            content = '<div id="iw-container">' +
                                '<div class="iw-title">' + miLabel.text + '</div>' +
                                '<div class="iw-content">' +
                                '<div class="iw-subTitle">Location</div>' +
                                '<span>' + job.get("friendlyLocation") + '</span>' +
                                '<div class="iw-subTitle">Description</div>' +
                                '<span>' + job.get("shortDescription") + '</span>' +
                                '<div class="iw-subTitle">Info</div>' +
                                '<i class="fa fa-money">pay: ' + '&#36;' + job.get("pay") + '</i>' +
                                '</div>' +
                                '<div class="iw-bottom-gradient"></div>' +
                                '</div>';
                            jobInfoWindow.setContent(content);
                            mi.setMap(map);
                            mi.addListener('mouseover', function () {
                                jobInfoWindow.open(map, mi);
                            });
                            mi.addListener('mouseout', function () {
                                jobInfoWindow.close();
                            });
                            jobInfoWindows.push(jobInfoWindow);
                            jobMarkers.push(mi);
                        } else {
                            fetchJobGeoloc(job).done(function (value) {
                                job.set("friendlyLocation", value);
                                job.save();
                                content = '<div id="iw-container">' +
                                    '<div class="iw-title">' + miLabel.text + '</div>' +
                                    '<div class="iw-content">' +
                                    '<div class="iw-subTitle">Location</div>' +
                                    '<span>' + job.get("friendlyLocation") + '</span>' +
                                    '<div class="iw-subTitle">Description</div>' +
                                    '<span>' + job.get("shortDescription") + '</span>' +
                                    '<div class="iw-subTitle">Info</div>' +
                                    '<i class="fa fa-money">pay: ' + '&#36;' + job.get("pay") + '</i>' +
                                    '</div>' +
                                    '<div class="iw-bottom-gradient"></div>' +
                                    '</div>';
                                jobInfoWindow.setContent(content);
                                mi.setMap(map);
                                mi.addListener('mouseover', function () {
                                    jobInfoWindow.open(map, mi);
                                });
                                mi.addListener('mouseout', function () {
                                    jobInfoWindow.close();
                                });
                                jobInfoWindows.push(jobInfoWindow);
                                jobMarkers.push(mi);

                            });
                        }


                    });

                });
            } else {
                $("#fetch-search").text("Search");
            }
        });
        if (spView.model.has("friendlyLocation")) {

            myLocationInfo.setPosition(constructLatLng(spView.model));
            myPos.setPosition(constructLatLng(spView.model));
            myPos.setMap(map);
            myLocationInfo.setContent($("<span>").text(spView.model.get("friendlyLocation")).append($("<br>")).append($("<h5>").text("My Location")).html());

            myLocationInfo.open(map, myPos);
            myPos.addListener('click', function() {
            if(isInfoWindowOpen(myLocationInfo)) return false;
                    myLocationInfo.open(map, myPos);
            });
        } else {
            getAddress(spView.model).done(function (value) {
                //write(value);
                spView.model.set({
                    friendlyLocation: value
                });
                spView.model.save();

                myLocationInfo.setPosition(constructLatLng(spView.model));
                myPos.setPosition(constructLatLng(spView.model));
                myPos.setMap(map);
                myLocationInfo.setContent($("<span>").text(value).append($("<br>")).append($("<h5>").text("My Location")).html());

                myLocationInfo.open(map, myPos);
                 myPos.addListener('click', function() {
            if(isInfoWindowOpen(myLocationInfo)) return false;
                    myLocationInfo.open(map, myPos);
            });
            });
        }

        map.setCenter(myPos.getPosition());

    }

    function newMarkerLabelJob(obj) {
       

        var service = obj.get("service");
        service = service.replace("_", "\r\n");
        var mapLabel = new MapLabel({
            text: service,
            position: constructLatLng(obj),
            fontSize: '1em',
            align: 'center'
        });
        return mapLabel;
    }

    function newMarkerLabel() {
        var mapLabel = new MapLabel({
            text: "My Location",
            fontSize: 35,
            align: 'right'
        });
        return mapLabel;
    }

    function deleteMarkers(markers) {
        // Loop through all the markers and remove
        for (var i = 0; i < markers.length; i++) {
            markers[i].setMap(null);
        }
        markers = [];
        return markers;
    };

    function deleteWindows(infoWindows) {
        // Loop through all the markers and remove
        for (var i = 0; i < infoWindows.length; i++) {
            infoWindows[i].close
        }
        infoWindows = [];
        return infoWindows;
    };

    function constructLatLng(obj) {
        return {
            lat: obj.get("loc").lat,
            lng: obj.get("loc").lng
        }
    }

    function constructGLatLng(obj) {
        return new google.maps.LatLng(obj.get("loc").lat, obj.get("loc").lng);
    }

    function updateMap() {
        var deferred = $.Deferred();
        var sp = spView.model;
        if (sp.get("friendlyLocation")) {
            deferred.resolve(sp.get("friendlyLocation"));
        } else {
            var loc = new SPGeoLocModel();

            loc.set("id", sp.get("id"));
            loc.fetch({
                dataType: "text",
                success: function (res, xhr, response) {
                    deferred.resolve(response.xhr.responseText);
                }
            });
        }

        return deferred.promise();
    }

    function getAddress(sp) {
        var deferred = $.Deferred();
        var loc = new SPUserGeoLocModel();

        loc.set("id", sp.get("id"));
        loc.fetch({
            dataType: "text",
            success: function (res, xhr, response) {
                deferred.resolve(response.xhr.responseText);
            }
        });
        return deferred.promise();
    }

    function fetchJobs() {
        var deferred = $.Deferred();
        jobCollection.fetch({
            success: function (res, xhr, response) {

                deferred.resolve(res);
            }
        });
        return deferred.promise();
    }



    function fetchJobGeoloc(job) {
        var deferred = $.Deferred();

        var loc = new JobGeoLocModel();

        loc.set("id", job.get("id"));
        loc.fetch({
            dataType: "text",
            success: function (res, xhr, response) {
                deferred.resolve(response.xhr.responseText);
            }
        });
        return deferred.promise();
    }
});
