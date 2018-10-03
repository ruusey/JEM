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
         });
     //});
 } else {
     $("#fetch-data-submit").on("click",function () {

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
         });
     });
 }

 $("#edit-service-provider").on("click", function (e) {

     var sp = spCollection.at(0);
     write(sp);

 });
 
  $("#sync-service").on("click", function (e) {

     var sp = spCollection.at(0);
        sp.create({success:spSaveSuccess,error:spSaveFailure});
 });
 function spSaveSuccess(model,response,options){
    write(model);
    write(response);
    write(options);
 }
 function spSaveFailure(model,response,options){
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
        
        var currLoc = _.clone(sp.get("loc"));
        
        currLoc.lat= crd.latitude;
        currLoc.lng = crd.longitude;
        currLoc.dateTime=lastPing.get("timestamp");
        write(currLoc); 
        sp.set("loc",currLoc)


     };


 });
 // $("#sync-service").on("click", function (e) {

 //     ServiceProviderView.save();



 // });

 function getGeoLocation() {
     navigator.geolocation.getCurrentPosition(geoFetchSuccess);

 }

 function geoFetchSuccess(pos) {
     return pos.coords;
 }

 function initialize() {
     $('[data-toggle="popover"]').popover();
     toastr.options = {
         "positionClass": "toast-top-center"
     };

 }

 function write(data) {
     console.log(JSON.stringify(data, null, 4));
 }

 function showSuccess(info) {
     toastr.success(info);
 }

 function showWarning(info) {
     toastr.warning(info);
 }

 function showError(info) {
     toastr.error(info);
 }