
var sessionToken=null;
var serviceList = [];
function write(data) {
    console.log(JSON.stringify(data, null, 4));
}
var sendAuthentication = function (xhr) {

    var token = sessionToken;
    xhr.setRequestHeader('Authorization', ("user " + token));
}

function showSuccess(info) {
    if(info==null || info==undefined) return;
    write(showSuccess.caller);
    toastr.success(info);
}

function showWarning(info) {
    if(info==null || info==undefined) return;
    toastr.warning(info);
}

function showError(info) {
    if(info==null || info==undefined) return;
    write(showError.caller);
    toastr.error(info);
}

function getGeoLocation() {
    navigator.geolocation.getCurrentPosition(geoFetchSuccess);
}
function authenticate() {
    var deffered = $.Deferred();
    var arr = {
        username: $("#exampleDropdownFormEmail1").val(),
        password: $("#exampleDropdownFormPassword1").val(),
    }
    $.ajax({
        url: 'v1/auth',
        type: 'POST',
        data: JSON.stringify(arr),
        contentType: 'application/json; charset=UTF-8',
        dataType: 'text',
        async: false,
        success: function (msg) {
            showSuccess("Welcome Back: " + isMobile);
            deffered.resolve(msg);

        },
         error: function(err,response){
            showError(err.responseText);
            deffered.fail(err.responseText);
        }
    });
    return deffered.promise();
}

function authenticateSp(uId) {
    var deffered = $.Deferred();
   
    var arr = {
        username: uId,
        password: "",
    }
    write(arr);
    $.ajax({
        url: 'v1/auth',
        type: 'POST',
        data: JSON.stringify(arr),
        contentType: 'application/json; charset=UTF-8',
        dataType: 'text',
        async: false,
        success: function (msg) {
            //write(msg)
            deffered.resolve(msg);
        },
        error: function(err,response){
            showError(err.responseText);
            deffered.fail(err.responseText);
        }
    });
    return deffered.promise();
}
function fetchServices(){
    var deffered = $.Deferred();
     $.ajax({
            url: 'v1/services',
            type: 'GET',
            contentType: 'application/json; charset=UTF-8',
            dataType: 'json',
            async: false,


        }).done(function (msg) {
            for (var i = 0, l = msg.length; i < l; i++) {
              serviceList.push(msg[i]);
            }
         });    
}

function geoFetchSuccess(pos) {
    return pos.coords;
}

function setLoggedOut() {
    $("#login-link").show();
    $("#logout-list-item").hide();
}

function setLoggedIn() {
    $("#logout-list-item").show();
    $("#login-link").hide();
    $("#fetch-search").text("Get All");
    
}
function topInViewport(element) {
    return $(element).offset().top >= $(window).scrollTop() && $(element).offset().top <= $(window).scrollTop() + $(window).height();
 }
function initialize() {
	setLoggedOut();
//    $(document).on('click', function (e) {
//        $('[data-toggle="popover"],[data-original-title]').each(function () {
//            //the 'is' for buttons that trigger popups
//            //the 'has' for icons within a button that triggers a popup
//            if (!$(this).is(e.target) && $(this).has(e.target).length === 0 && $('.popover').has(e.target).length === 0) {
//                (($(this).popover('hide').data('bs.popover') || {}).inState || {}).click = false // fix for BS 3.3.6
//            }
//
//        });
//    });
    toastr.options = {
        "positionClass": "toast-top-center"
    };
    $("#map-container").on( "resizestop", function( event, ui ) {
        $('html, body').animate({
                    scrollTop: $(document).height()
        }, 200);
    } );
   
    

   

}