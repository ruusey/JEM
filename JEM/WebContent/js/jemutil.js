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
 function getGeoLocation() {

     navigator.geolocation.getCurrentPosition(geoFetchSuccess);

 }
function getCurrentSP(){
    return spCollection.at(0);
}
function getCurrentTimestamp(){
    return  pingCollection.at(0).get("timestamp");
}
 function geoFetchSuccess(pos) {
     return pos.coords;
 }
  function setLoggedOut(){
      $("#login-link").show();
     $("#logout-list-item").hide();
 }
 function setLoggedIn(){
     $("#login-link").hide();
     $("#logout-list-item").show();
 }
 function initialize() {

     $('[data-toggle="popover"]').popover();
     toastr.options = {
         "positionClass": "toast-top-left"
     };

 }
 