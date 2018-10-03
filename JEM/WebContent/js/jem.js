"use-strict";

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

var ServiceProviderView = Backbone.View.extend({
    el: $("#client-data"),
    template: Handlebars.compile($("#entry-template").html()),
    model: ServiceProviderModel,
    initialize: function () {
        //this.render;
        this.listenTo(this.model, "add", this.render);
        this.listenTo(this.model, "change", this.render);
        console.log(JSON.stringify(this.model));
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
