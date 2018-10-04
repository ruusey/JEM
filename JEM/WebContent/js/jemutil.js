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

 function geoFetchSuccess(pos) {
     return pos.coords;
 }
 function initialize() {

     $('[data-toggle="popover"]').popover();
     toastr.options = {
         "positionClass": "toast-top-center"
     };

 }
 