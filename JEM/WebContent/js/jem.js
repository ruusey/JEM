'use-strict';

$(function () {
    var client = Backbone.Model.extend({
        idAttribute: "id",
        urlRoot: 'v1/client'
    });
    var frame = Backbone.View.extend({
        el: $("#client-data"),
        tagName: 'span',
        className: "d-block p-2 bg-primary text-white",
        my_template: _.template("<strong><%= email %></strong> (<%= firstName %>) (<%= lastName %>)"),

        initialize: function () {
            this.render();
            this.model.on("change", this.render);
        },
        render: function () {
            $("#client-data").html(this.my_template(this.model.toJSON()));
            return this;
        }
    });
    var c1 = new client({ id: 1 });
    c1.fetch().done(function () {
        var appView = new frame({ model: c1 });
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

