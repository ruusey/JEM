 "use-strict";
 initialize();
var isMobile=false;
var myIp=null;
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
     pingModel.fetch({success:determineIsMobile});

 }, 1000);
 function determineIsMobile(model,xhr,response){
     if(sessionToken==null)return;
        var mobile = response.xhr.getResponseHeader('response-text');
        if(myIp==null){
            myIp=pingModel.get("sourceIp");
        }else{
            return;
        }
        if(mobile==="true"){
            var height = $( window ).height();
            $("#map-container").resizable().resizable( "option", "grid", [ 0, 10 ] );
            $("#map-container").css("height",(height/2)+"px").css("margin-bottom",(height/6)+"px");
            isMobile=true;
            initMap();
        }else{
            var height = $( window ).height();
            $("#map-container").resizable().resizable( "option", "grid", [ 0, 10 ] );
            $("#map-container").css("height",(height/3)+"px",).css("margin-bottom",(height/6)+"px");
            isMobile=false;
            initMap();
        }
        showSuccess("Is Mobile: "+isMobile);
 }
 $("#log-out-sp").on("click", function () {
     if (spModel) {
         spModel.clear();

         $.removeCookie("sp");
         showSuccess("Logout successful");
         setLoggedOut();
         _.delay(function () {
             location.reload()
         }, 1000);
     }

 });
 //($.cookie("sp"));
 if ($.cookie("sp")) {

     var id = $.cookie("sp");
     spModel.set("id", id);

     authenticateSp(id).done(function (token) {
         //(token);
         sessionToken = token;
     });

     spModel.fetch({
         beforeSend: sendAuthentication,
         success: function () {


             spView.render;
             $("#login-dropdown").removeClass("show");
             if (!$.cookie("sp")) {
                 $.cookie("sp", this.id);
             }
             
             setLoggedIn();
         }
     });

     //});
 } else {
     setLoggedOut();
     $("#fetch-data-submit").on("click", function () {

         var id = $("#exampleDropdownFormEmail1").val();
         var pass = $("#exampleDropdownFormPassword1").val();
         authenticate().done(function (token) {
             sessionToken = token;
         });

         //(sessionToken);
         spModel.set("id", id);
         // spModel.set("username", id);
         spModel.fetch({
             beforeSend: sendAuthentication,
             success: function () {

                 $("#login-dropdown").removeClass("show");
                 if (!$.cookie("sp")) {
                     //(spModel);
                     $.cookie("sp", spModel.get("id"));
                 }

             }
         }).done(function () {
             initMap();
             setLoggedIn();
         });
     });
 }


 $("#geoloc").on("click", function (e) {
     navigator.geolocation.getCurrentPosition(geoFetchSuccess);

     function geoFetchSuccess(pos) {

         var crd = pos.coords;

         var sp = spView.model;

         var lastPing = pingView.model;
         var currLoc = {};

         currLoc.lat = crd.latitude;
         currLoc.lng = crd.longitude;
         currLoc.dateTime = lastPing.get("timestamp");
         //(currLoc);
         sp.set("loc", currLoc)
         sp.save().done(function () {
             initMap();
         });

     };


 });

 function initMap() {
     var positionListener;
     var jobInfoWindows = [];
     var jobMarkers = [];
     var sp = spView.model;
     var myLocation = constructLatLng(sp);
     var myLocationInfo = new google.maps.InfoWindow;
     var map = new google.maps.Map(document.getElementById('map'), {
         center: myLocation,
         zoom: 9
     });
     var myPos = new google.maps.Marker({
         map: map,
         position: myLocation,
         draggable: true,
         label: "My Position",
         zIndex:1
     });
     myLocationInfo.setPosition(myLocation);
     map.dragInProgress = false;
    
     google.maps.event.addListener(myPos, 'dragend', function (evt) {
         var newLat = evt.latLng.lat();
         var newLng = evt.latLng.lng();

         var currLoc = constructLatLng(sp);

         currLoc.lat = newLat;
         currLoc.lng = newLng;
         currLoc.dateTime = pingView.model.get("timestamp");
         sp.set({loc:currLoc});
        sp.save({wait:true});
         $('#loc-modal').modal('show');
         $('#loc-modal-confirm').on('click', function (e) {
             $('#loc-modal').modal('hide');


             getAddress(sp).done(function (value) {
                 
                    
                 write(value);
                 sp.set({
                     friendlyLocation: value
                 });
                 sp.save({wait:true});
                 
                 myLocationInfo.setPosition(constructLatLng(sp));
                    myPos.setPosition(constructLatLng(sp));
                 myPos.setMap(map);
                 myLocationInfo.setContent($("<span>").text(value).append($("<br>")).append($("<h5>").text("My Location")).html());

                 myLocationInfo.open(map, myPos);
                 
             });
$           ('#loc-modal-confirm').off();
         });




        //  (sessionToken);
        //   getAddress(sp).done(function (value) {
        //      mi.label=(value);
        //       //infoWindow.setContent(value);
        //   });

     });

     $("#search-clear-icon").on("click",function () {
         jobMarkers = deleteMarkers(jobMarkers);
         jobInfoWindows = deleteWindows(jobInfoWindows);
         $("#job-search-input").val("");
         $("#fetch-search").text("Fetch all");

     });
     $("#fetch-search").on("click",function (e) {
         e.preventDefault();
         e.stopPropagation();
         var query = $("#job-search-input").val();
        //&& $("#radius-slider").slider("option", "disabled")
         if (query.length > 1 ) {
             $("#fetch-search").text("Search jobs");
             jobMarkers = deleteMarkers(jobMarkers);
             jobInfoWindows = deleteWindows(jobInfoWindows);
             var jobSearch = new JobQueryModel({
                 id: query
                 //urlRoot: "v1/job-search/"+sp.get("id")+"/"+query+"/"+$("#radius-slider").slider("value")
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

                         //jobInfoWindow.setPosition(jobPos);
                         var content;
                         fetchJobGeoloc(job).done(function (value) {
                             content = '<div id="iw-container">' +
                                 '<div class="iw-title">' + miLabel.text + '</div>' +
                                 '<div class="iw-content">' +
                                 '<div class="iw-subTitle">Location</div>' +
                                 '<span>' + value + '</span>' +
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

                     //jobInfoWindow.setPosition(jobPos);
                     var content;
                     fetchJobGeoloc(job).done(function (value) {
                         content = '<div id="iw-container">' +
                             '<div class="iw-title">' + miLabel.text + '</div>' +
                             '<div class="iw-content">' +
                             '<div class="iw-subTitle">Location</div>' +
                             '<span>' + value + '</span>' +
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
                 });

             });
         }

     });
     $('#job-search-input').on('input', function (e) {

         var input = $(this).val();

         if (input.length == 0) {

             $("#fetch-search").text("Get All");
             jobMarkers = deleteMarkers(jobMarkers);
             jobInfoWindows = deleteWindows(jobInfoWindows);
             fetchJobs().done(function (jobs) {

                 jobCollection.each(function (job) {
                     //(job);
                     var jobPos = constructGLatLng(job);
                     var jobInfoWindow = new google.maps.InfoWindow;
                     var miLabel = newMarkerLabelJob(job)
                     miLabel.set('position', jobPos);
                     var mi = new google.maps.Marker();
                     mi.bindTo('map', miLabel);
                     mi.bindTo('position', miLabel);

                     //jobInfoWindow.setPosition(jobPos);
                     var content;
                     fetchJobGeoloc(job).done(function (value) {
                         content = '<div id="iw-container">' +
                             '<div class="iw-title">' + miLabel.text + '</div>' +
                             '<div class="iw-content">' +
                             '<div class="iw-subTitle">Location</div>' +
                             '<span>' + value + '</span>' +
                             '<div class="iw-subTitle">Description</div>' +
                             '<span>' + job.get("shortDescription") + '</span>' +
                             '<div class="iw-subTitle">Info</div>' +
                             '<i class="fa fa-money">pay: ' + '&#36;' + job.get("pay") + '</i>' +
                             '</div>' +
                             '<div class="iw-bottom-gradient"></div>' +
                             '</div>';
                         jobInfoWindow.setContent(content);
                         mi.setMap(map);
                         mi.addListener.on('mouseover', function () {
                             jobInfoWindow.open(map, mi);
                         });
                         mi.addListener.on('mouseout', function () {
                             jobInfoWindow.close();
                         });
                         jobInfoWindows.push(jobInfoWindow);
                         jobMarkers.push(mi);

                     });

                 });

             });
         } else {
             $("#fetch-search").text("Search");
         }
     });

     updateMap().done(function (value) {
         myPos.setPosition(constructLatLng(sp));
         myLocationInfo.setPosition(constructLatLng(sp));
         myLocationInfo.setContent($("<span>").text(value).append($("<br>")).append($("<h5>").text("My Location")).html());

         myLocationInfo.open(map, myPos);

     });
     map.setCenter(myPos.getPosition());

 }

 function newMarkerLabelJob(obj) {
     var service = obj.get("service");
     var mapLabel = new MapLabel({
         text: service.replace("_", "\n"),
         position: constructLatLng(obj),
         fontSize: 20,
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
     //Loop through all the markers and remove
     for (var i = 0; i < markers.length; i++) {
         markers[i].setMap(null);
     }
     markers = [];
     return markers;
 };

 function deleteWindows(infoWindows) {
     //Loop through all the markers and remove
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

     loc.set("id", spView.model.get("id"));
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

 function showRadiusSlider() {
     if($("#radius-slider").slider("instance")!==undefined){
         destroyRadiusSlider();
     }
     $("#radius-slider").slider({
         range: "max",
         min: 1,
         max: 10,
         value: 5,
         slide: function (event, ui) {
             $("#radius-input").val("Search radius: " + ui.value + "mi");
         }
     });
     $("#disable-radius-search").on('click', function () {
         if ($("#radius-slider").slider("option", "disabled")) {
             $("#radius-slider").slider("enable");
             $("#radius-input").val("Search radius: " + $("#radius-slider").slider("value") + "mi").css("color","#f6931f");
         } else {
             $("#radius-slider").slider("disable");
             $("#radius-input").val("Disabled").css("color","grey");
         }

      });
     $("#radius-input").val("Search radius: " + $("#radius-slider").slider("value") + "mi");
 }

 function destroyRadiusSlider() {
     $("#radius-slider").slider("destroy");

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