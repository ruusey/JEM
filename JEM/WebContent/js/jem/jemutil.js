var sessionToken = null;
var serviceList = [];

function write(data) {
    console.log(JSON.stringify(data, null, 4));
}
var sendAuthentication = function (xhr) {

    var token = sessionToken;
    xhr.setRequestHeader('Authorization', ("user " + token));
}

function showSuccess(info) {
    write(arguments.callee.caller.toString());
    toastr.success(info);
}

function showWarning(info) {
    toastr.warning(info);
}

function showError(info) {
    write(arguments.callee.caller.toString());
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

        },
        error: function (err, response) {
            showError(err.responseText);
            deffered.fail(err.responseText);
        }
    });
    return deffered.promise();
}

function showRadiusSlider() {
    if ($("#radius-slider").slider("instance") !== undefined) {
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
            $("#radius-input").val("Search radius: " + $("#radius-slider").slider("value") + "mi").css("color", "#f6931f");
        } else {
            $("#radius-slider").slider("disable");
            $("#radius-input").val("Disabled").css("color", "grey");
        }

    });
    $("#radius-input").val("Search radius: " + $("#radius-slider").slider("value") + "mi");
}

function destroyRadiusSlider() {
    $("#radius-slider").slider("destroy");

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
        error: function (err, response) {
            showError(err.responseText);
            deffered.fail(err.responseText);
        }
    });
    return deffered.promise();
}

function fetchServices() {
    var deffered = $.Deferred();
    $.ajax({
        beforeSend: sendAuthentication,
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

function printSuccess(model, response, options) {
    var caller = printSuccess.caller.caller.caller.caller.name.toString();
    write(caller);
    if (caller.indexOf("r") >= 0 || caller.indexOf("auth") >= 0) return;
    showSuccess(JSON.stringify(response));
}

function printFail(model, response, options) {
    showError(JSON.stringify(response));
}

function initialize() {
    setLoggedOut();
       $(document).on('click', function (e) {
           $('[data-toggle="popover"],[data-original-title]').each(function () {
               //the 'is' for buttons that trigger popups
               //the 'has' for icons within a button that triggers a popup
               if (!$(this).is(e.target) && $(this).has(e.target).length === 0 && $('.popover').has(e.target).length === 0) {
                   (($(this).popover('hide').data('bs.popover') || {}).inState || {}).click = false // fix for BS 3.3.6
               }
    
           });
       });
    toastr.options = {
        "positionClass": "toast-top-center"
    };
    $("#map-container").on("resizestop", function (event, ui) {
        $('html, body').animate({
            scrollTop: $(document).height()
        }, 200);
    });

    var sync = Backbone.sync;
    Backbone.sync = function (method, model, options) {
        options.beforeSend = sendAuthentication,
            options.error = printFail

        sync(method, model, options);
    };
    $(document).on("logout", function (e, spView) {
        write(spView.model);
        //e.preventDefault();
        if (sessionToken != null) {
            showSuccess("Logout successful");
            setLoggedOut();
            $.removeCookie("id");
            _.delay(function () {
                window.location.href = window.location.href;
            }, 1000);
        }

    });


}

function logout(spView) {
    spView.spModel.clear();
    showSuccess("Logout successful");
    setLoggedOut();
    $.removeCookie("id");
    _.delay(function () {
        window.location.href = window.location.href;
    }, 1000);
}