"use-strict";
var JobGeoLocModel = Backbone.Model.extend({
    idAttribute: "id",
    urlRoot: "v1/job/geoloc"
});
var SPGeoLocModel = Backbone.Model.extend({
    idAttribute: "id",
    urlRoot: "v1/sp/geoloc"
});
var SPUserGeoLocModel = Backbone.Model.extend({
    idAttribute: "id",
    urlRoot: "v1/sp/geoloc"
});
var PingModel = Backbone.Model.extend({
    urlRoot: "v1/ping"
});

var ServiceProviderModel = Backbone.Model.extend({
    idAttribute: "id",
    urlRoot: "v1/sp"
});
var SPGeoLocCollection = Backbone.Collection.extend({
    model: SPGeoLocModel
});
var PingCollection = Backbone.Collection.extend({
    model: PingModel

});
var JobQueryModel = Backbone.Model.extend({
    idAttribute: "id",
    urlRoot: "v1/job-search"
});
var JobModel = Backbone.Model.extend({
    idAttribute: "id"
});
var JobCollection = Backbone.Collection.extend({
    model: JobModel,
    url: "v1/job-all"
})
var ServiceProviderView = Backbone.View.extend({
    el: $("#client-data"),
    template: Handlebars.compile($("#entry-template").html()),
    editTemplate: Handlebars.compile($("#sp-edit-template").html()),
    model: ServiceProviderModel,

    events: {
        "click #edit-sp": "edit",
        "click #edit-sp-cancel": "cancel",
        "click #edit-sp-save": "save",
        "click .remove-service": "removeService",
        "click #sp-add-service": "addService"


    },
    initialize: function () {
        this.attributes = {};
        this.attributes.addingService = false;
        this.listenTo(this.model, "add", this.render);
        this.listenTo(this.model, "change", this.render);
        console.log(JSON.stringify(this.model));
        var self = this;
        $.ajax({
            url: 'v1/services',
            type: 'GET',
            contentType: 'application/json; charset=UTF-8',
            dataType: 'json',
            async: false,


        }).done(function (msg) {

            var ul = $("<ul>").attr("class", "list-group").attr("id", "sp-service-list");
            for (var i = 0, l = msg.length; i < l; i++) {
                var li = $("<li>").attr("class", "list-group-item service").text(msg[i])
                ul.append(li);


            }
            self.attributes.popoverContent = ul.html();
        });

    },
    removeService: function (e) {
        var span = $(e.target).parent();

        var toRemove = span.text();
        toRemove = $.trim(toRemove.replace(/[\t\n]+/g, ''));
        write(toRemove);
        var currServices = _.clone(this.model.get("services"));
        write(currServices);
        currServices = jQuery.grep(currServices, function (value) {
            return value != toRemove;
        });
        write(currServices);
        this.model.set("services", currServices);
        spModel.save({
            wait: true
        });
    },
    addService: function (e) {

        var loc = $(e.target);
         
        var self = this;
       
        loc.popover({
            placement: 'left',
            container: 'body',
            title: "Available Services",
            html: true,
            content: this.attributes.popoverContent,
            trigger: 'manual'
        });
       
            loc.popover("show");
            write("togggle");
        
    
       
            $.each($(".list-group-item.service"), function (idx, value) {
                
                $(value).one("click", function () {
                    $(this).addClass("active");
                    var currServices = _.clone(self.model.get("services"));

                    currServices.push($(this).text());
                    currServices = jQuery.grep(currServices, function (value) {
                        return value != $(e.target).text();
                    });
                    write(currServices);
                    self.model.set("services", currServices);
                    self.model.save({wait: true});

                });
            });
       
        




    },
    closePopover: function (e) {
        $(e.target).popover("dispose");
       
    },
    edit: function () {
        $("#client-data").html(this.editTemplate(this.model.toJSON()));
        return this;
    },
    save: function () {
        var username = $("#new-username").val();
        var newName = $.trim(String($("#new-name").val())).split(",");
        var firstName = newName[0];
        var lastName = newName[1];
        var email = $("#new-email").val();
        var newRating = $("#new-rating").val();
        var newParams = {};
        newParams.userName = username;
        newParams.firstName = firstName;
        newParams.lastName = lastName;
        newParams.email = email;
        newParams.rating = parseInt(newRating);

        this.model.set(newParams);
        write(this.model);
        this.model.save();
    },
    cancel: function () {
        $("#client-data").html(this.template(this.model.toJSON()));
    },
    render: function () {
        $("#client-data").html(this.template(this.model.toJSON()));
        getAddress(this.model).done(function (value) {
            $("#sp-location").text(value);

        });

        return this;
    },

});

var PingView = Backbone.View.extend({
    el: $("#server-time-container"),
    template: Handlebars.compile($("#server-time-template").html()),
    model: PingModel,
    initialize: function () {

        this.listenTo(this.model, "change", this.render);
    },
    render: function () {
        //console.log(JSON.stringify(this.model));
        $("#server-time-container").html(this.template(this.model.toJSON()));
        return this;
    }
});
