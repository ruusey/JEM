"use-strict";
var JobGeoLocModel = Backbone.Model.extend({
    idAttribute: "id",
    urlRoot: "v1/job/geoloc"
});
var SPGeoLocModel = Backbone.Model.extend({
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
var SPCollection = Backbone.Collection.extend({
    model: ServiceProviderModel
});
var SPGeoLocCollection = Backbone.Collection.extend({
    model: SPGeoLocModel
});
var PingCollection = Backbone.Collection.extend({
    model: PingModel

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
      "click #edit-sp"  : "edit",
      "click #edit-sp-cancel"  : "cancel",
      "click #edit-sp-save"  : "save"
    },
    initialize: function () {
        //this.render;
        this.listenTo(this.model, "add", this.render);
        this.listenTo(this.model, "change", this.render);
        console.log(JSON.stringify(this.model));
    },
    edit: function(){
        $("#client-data").html(this.editTemplate(this.model.toJSON()));
        return this;
    },
    save: function(){
        var username =  $("#new-username").val();
        var newName = $.trim(String($("#new-name").val())).split(",");
        var firstName = newName[0];
        var lastName = newName[1];
        var email = $("#new-email").val();
        var newRating = $("#new-rating").val();
        var newParams = {};
        newParams.userName = username;
        newParams.firstName=firstName;
        newParams.lastName=lastName;
        newParams.email=email;
        newParams.rating=parseInt(newRating);
        
        this.model.set(newParams);
        write(this.model);
        this.model.save();
    },
    cancel: function(){
        $("#client-data").html(this.template(this.model.toJSON()));
    },
    render: function () {
        $("#client-data").html(this.template(this.model.toJSON()));
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
