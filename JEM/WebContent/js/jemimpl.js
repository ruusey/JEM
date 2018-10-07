 "use-strict";
 initialize();

 var spCollection = new SPCollection();
 var pingCollection = new PingCollection();
 var spGeoCollection = new SPGeoLocCollection();
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
         success: function (pingModel) {
             pingCollection.add(pingModel, {
                 merge: true
             });
         }
     });
 }, 1000);
 $("#log-out-sp").on("click", function () {
     if (spModel) {
         var temp = _.clone(spModel);
         spModel.clear();
         spCollection.reset();
         $.removeCookie("sp");
         
         setLoggedOut();
         location.reload();

     }

 });
 if ($.cookie("sp")) {
   
     var id = $.cookie("sp");
     spModel.set("id", id);
     
    authenticateSp(id).done(function(token){
        write(token);
        sessionToken=token;
    });
    
     spModel.fetch({
         beforeSend: sendAuthentication,
         success: function (spModel) {
             spCollection.add(spModel, {
                 merge: true
             });
             $("#login-dropdown").removeClass("show");
             if (!$.cookie("sp")) {
                 $.cookie("sp", id);
             }

         }
     }).done(function () {
         setLoggedIn();
         initMap();
     });

     //});
 } else {
     setLoggedOut();
     $("#fetch-data-submit").on("click", function () {

         var id = $("#exampleDropdownFormEmail1").val();
         var pass = $("#exampleDropdownFormPassword1").val();
         authenticate().done(function(token){
            sessionToken=token;
        });
         write(sessionToken);
         spModel.set("id", id);
       // spModel.set("username", id);
         spModel.fetch({
             beforeSend: sendAuthentication,
             success: function (spModel) {
                 spCollection.add(spModel, {
                     merge: true
                 });
                 $("#login-dropdown").removeClass("show");
                 if (!$.cookie("sp")) {
                     $.cookie("sp", id);
                 }

             }
         }).done(function () {
             initMap();
         });
     });
 }


 $("#geoloc").on("click", function (e) {
     navigator.geolocation.getCurrentPosition(geoFetchSuccess);

     function geoFetchSuccess(pos) {

         var crd = pos.coords;

         var sp = spCollection.at(0);

         var lastPing = pingCollection.at(0);
         write(pingCollection);
         var currLoc = {};

         currLoc.lat = crd.latitude;
         currLoc.lng = crd.longitude;
         currLoc.dateTime = lastPing.get("timestamp");
         write(currLoc);
         sp.set("loc", currLoc)
         sp.save().done(function () {
             initMap();
         });

     };


 });

 function initMap() {

     var sp = getCurrentSP();
     var myLocation = constructLatLng(sp);
     var myLocationInfo = new google.maps.InfoWindow;
     var map = new google.maps.Map(document.getElementById('map'), {
         center: myLocation,
         zoom: 10
     });
     var myPos = new google.maps.Marker({
         map: map,
         position: myLocation,
         draggable: true,
         label: "My Position"
     });
     myLocationInfo.setPosition(myLocation);
     google.maps.event.addListener(myPos, 'dragend', function (evt) {
         var newLat = evt.latLng.lat();
         var newLng = evt.latLng.lng();
         var currLoc = _.clone(sp.get("loc"));

         currLoc.lat = newLat;
         currLoc.lng = newLng;
         currLoc.dateTime = getCurrentTimestamp();
         $('#loc-modal').modal('show');
         $('#loc-modal-confirm').on('click', function (e) {
             $('#loc-modal').modal('hide');
             sp.set("loc", currLoc);
             sp.save({
                 wait: true
             }).done(function () {
                 updateMap().done(function (value) {
                     myPos.setPosition(constructLatLng(sp));
                     myPos.setMap(map);
                     myLocationInfo.setPosition(constructLatLng(sp));
                     myLocationInfo.setContent($("<span>").text(value).append($("<br>")).append($("<h5>").text(value)).html());

                     myLocationInfo.open(map, myPos);
                 });
             });
         });

         write(sessionToken);
         //  updateMap().done(function (value) {
         //     marker.label=(value);
         //      //infoWindow.setContent(value);
         //  });

     });



     fetchJobs().done(function (jobs) {

         jobCollection.each(function (job) {
             //write(job);
             var jobPos = constructLatLng(job);
             var jobInfoWindow = new google.maps.InfoWindow;
             var mi = new google.maps.Marker({
                 map: map,
                 position: jobPos,
                 label: job.get("service")

             });
             jobInfoWindow.setPosition(jobPos);
             var content;
             fetchJobGeoloc(job).done(function (value) {
                 content = '<div id="iw-container">' +
                     '<div class="iw-title">' + mi.getLabel() + '</div>' +
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
                 jobInfoWindow.open(map, mi);

             });




         });



     });
     updateMap().done(function (value) {
         myPos.setPosition(constructLatLng(sp));
         myLocationInfo.setPosition(constructLatLng(sp));
         myLocationInfo.setContent($("<span>").text(value).append($("<br>")).append($("<h5>").text("My Location")).html());

         myLocationInfo.open(map, myPos);
     });
     //  infoWindow.setPosition(map.center);
     //  infoWindow.open(map, myPos);
     // Try HTML5 geolocation.

 }

 function constructLatLng(obj) {
     return {
         lat: obj.get("loc").lat,
         lng: obj.get("loc").lng
     }
 }

 function updateMap() {
     var deferred = $.Deferred();
     var sp = spCollection.at(0);
     //write(sp);
     var loc = new SPGeoLocModel();

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