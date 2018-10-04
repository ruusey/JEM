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
         spModel.clear();
         $.removeCookie("sp");
         location.reload();
     }

 });
 if ($.cookie("sp")) {
     //$("#login-link").on("click",function () {
     var id = $.cookie("sp");
     spModel.set("id", id);

     spModel.fetch({
         success: function (spModel) {
             spCollection.add(spModel, {
                 merge: true
             });
             $("#login-dropdown").removeClass("show");
             if (!$.cookie("sp")) {
                 $.cookie("sp", id);
             }

         }
     }).done(function(){
        initMap();
     });

     //});
 } else {
     $("#fetch-data-submit").on("click", function () {

         var id = $("#exampleDropdownFormEmail1").val();
         spModel.set("id", id);

         spModel.fetch({
             success: function (spModel) {
                 spCollection.add(spModel, {
                     merge: true
                 });
                 $("#login-dropdown").removeClass("show");
                 if (!$.cookie("sp")) {
                     $.cookie("sp", id);
                 }

             }
         }).done(function(){
            initMap();
         });
     });
 }

 $("#sync-service").on("click", function (e) {

     var sp = spCollection.at(0);
     sp.create({
         success: spSaveSuccess,
         error: spSaveFailure
     });
 });

 function spSaveSuccess(model, response, options) {
     write(model);
     write(response);
     write(options);
 }

 function spSaveFailure(model, response, options) {
     write(model);
     write(response);
     write(options);
 }

 //  $(".dropdown-submenu a.test").on("click", function (e) {

 //      e.stopPropagation();
 //      e.preventDefault();

 //  });
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
            sp.save().done(function(){
                initMap();
            });

     };


 });
 // $("#sync-service").on("click", function (e) {

 //     ServiceProviderView.save();



 // });
 function initMap() {
   
   
   
     
     var sp = spCollection.at(0);
     
     var infoWindow = new google.maps.InfoWindow;
     var map = new google.maps.Map(document.getElementById('map'), {
         center: {
             lat: sp.get("loc").lat,
             lng: sp.get("loc").lng
         },
         zoom: 10
     });
     var marker = new google.maps.Marker({
         position: map.center,
         map: map,
         draggable: true
     });
     google.maps.event.addListener(marker, 'dragend', function (evt) {
         var newLat = evt.latLng.lat();
         var newLng = evt.latLng.lng();
         var lastPing = pingCollection.at(0);

         var currLoc = _.clone(sp.get("loc"));

         currLoc.lat = newLat;
         currLoc.lng = newLng;
         currLoc.dateTime = lastPing.get("timestamp");
         $('#loc-modal').modal('show');
         $('#loc-modal-confirm').on('click', function (e) {
             $('#loc-modal').modal('hide');
             sp.set("loc", currLoc);
             sp.save().done(function () {
                 updateMap().done(function (value) {

                     infoWindow.setContent(value);
                 });
             });
         });


         updateMap().done(function (value) {

             infoWindow.setContent(value);
         });

     });

    

      fetchJobs().done(function (jobs) {

                  //write(jobs);

                  for (var i = 0; i < jobs.length; i++) {
                      var job = jobs.at(i);
                        write(job);
                      var mi = new google.maps.Marker({

                          position: new google.maps.LatLng(job.get("loc").lat, job.get("loc").lng),
                          map: map,
                          title: job.get("service")
                      });
                      infoWindow.open(map, mi);
                  }
            
        
    });
     updateMap().done(function (value) {

         infoWindow.setContent(value);
     });
     infoWindow.setPosition(map.center);
     infoWindow.open(map, marker);
     // Try HTML5 geolocation.

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
 function fetchJobs(){
    var deferred = $.Deferred();
    var jobCollection = new JobCollection();
    jobCollection.fetch({
             success: function (res, xhr, response) {
                
                deferred.resolve(res);
             }
     });
     return deferred.promise();
 }

 

 