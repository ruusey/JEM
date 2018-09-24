'use-strict';

$(function () {
    toastr.options = {
        "positionClass": "toast-top-center"
    }

    var client = Backbone.Model.extend({
        idAttribute: "id",
        urlRoot: 'v1/client'
    });
    var sp = Backbone.Model.extend({
        idAttribute: "id",
        urlRoot: 'v1/sp',
        validate: function (attrs) {
            if (!attrs.email) {
                return 'Please fill email field.';
            }
            if (!attrs.feedback) {
                return 'Please fill feedback field.';
            }
        }
    });
    var ping = Backbone.Model.extend({
        urlRoot: 'v1/ping'
    });
    var frame = Backbone.View.extend({
        el: $("#client-data"),
        tagName: 'span',
        className: "d-block p-2 bg-primary text-white",
        my_template: _.template($("#sptemplate").html()),

        initialize: function () {
            this.render();
            this.model.on("change", this.render);
        },
        render: function () {
            $("#client-data").html(this.my_template(this.model.toJSON()));
            return this;
        }
    });
    /////////////////////////////
    //END ENDPOINT MODELS
    /////////////////////////////
    var ping = new ping();
    ping.fetch().done(function () {
        $("#server-time").html(ping.toJSON().timestamp);
    });

    $("#fetch-data-submit").click(function () {
        var id = $("#exampleDropdownFormEmail1").val();
        var sp1 = new sp({
            id: id
        });
        sp1.fetch({
            success: function (model, response, options) {
                $("#login-dropdown").removeClass("show");
                showSuccess(options.xhr.getResponseHeader('response-text'));
                var appView = new frame({
                    model: model
                });
            }
        });
        // sp1.fetch().done(function () {

        //     var appView = new frame({
        //         model: sp1
        //     });
        // });
    });
    $('.dropdown-submenu a.test').on("click", function (e) {
        
        e.stopPropagation();
        e.preventDefault();
        
    });
    var c1 = new client({
        id: 1
    });

});




// function pingServer() {
//     clients.ping.read().done(function (data, textStatus, xhrObject) {
//         showSuccess(JSON.stringify(xhrObject));
//     });
// }

// function getClients() {
//     clients.client - all.read().done(function (data, textStatus, xhrObject) {
//         writeConsole(data);
//     });
// }

// function addClientEndpoints() {
//     //client.add('ping');
//     clients.add('methods');

// }

// function executeCall(event) {
//     var exec = event.data.met;
//     console.log(exec);
//     client.add(exec);
//     client[exec].read().done(function (data) {
//         writeConsole(data)
//     });
// }

// function getApiMethods() {
//     client.methods.read().done(function (data) {
//         populateMethods(data);
//     });
// }

// function populateMethods(data) {
//     console.log(JSON.stringify(data));
//     $.each(data, function (i, item) {
//         if (data[i] == 'methods') return true;
//         var button = $("<button/>").attr("type", "button").attr("class", "btn btn-primary").attr("id", data[i]).text(data[i].toUpperCase());

//         button.click({
//             met: data[i]
//         }, executeCall);
//         $("#api_methods").append(button);
//     });

// }

function writeConsole(data) {
    $('#console').text(JSON.stringify(data, null, 4));
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

