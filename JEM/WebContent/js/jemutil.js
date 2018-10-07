var sessionToken;

function write(data) {
    console.log(JSON.stringify(data, null, 4));
}
var sendAuthentication = function (xhr) {

    var token = sessionToken;
    xhr.setRequestHeader('Authorization', ("user " + token));
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
            
            deffered.resolve(msg);

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
            write(msg)
            deffered.resolve(msg);

        },
        error: function(err){
            write(err);
        }
    });
    return deffered.promise();
}

function getCurrentSP() {
    return spCollection.at(0);
}

function getCurrentTimestamp() {
    return pingCollection.at(0).get("timestamp");
}

function geoFetchSuccess(pos) {
    return pos.coords;
}

function setLoggedOut() {
    $("#login-link").show();
    $("#logout-list-item").hide();
}

function setLoggedIn() {
    $("#login-link").hide();
    $("#logout-list-item").show();
}

function initialize() {

    $('[data-toggle="popover"]').popover();
    toastr.options = {
        "positionClass": "toast-top-left"
    };

}