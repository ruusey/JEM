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
var JobSearchServiceModel = Backbone.Model.extend({
    idAttribute: "id",
    urlRoot: "v1/job-search/service"
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
        "click .search-service": "searchService",
        "click #sp-add-service": "addService"


    },
    initialize: function () {
        
        this.attributes = {};
        this.attributes.addingService = false;
        this.listenTo(this.model, "add", this.render);
        this.listenTo(this.model, "change", this.render);
        var self = this;
        $.ajax({
            url: 'v1/services',
            type: 'GET',
            contentType: 'application/json; charset=UTF-8',
            dataType: 'json'

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
        var self = (this.model.clone());
        var currServices = self.get("services");
        write(currServices);
        currServices = $.grep(currServices, function (value) {
            return value != toRemove;
        });
        this.model.set("services", currServices);
        this.model.save();
    },
     searchService: function (e) {
      //e.preventDefault();
        var span = $(e.target).parent();

        var toRemove = span.text();
        
        toRemove = $.trim(toRemove.replace(/[\t\n]+/g, ''));
       write(toRemove);
        
            $("#map").trigger("search",[toRemove]);
        
       
    },
	    addService : function(e) {
            
		var loc = $(e.target);
        
		var self = this;

		$(e.target).popover({
			placement : 'left',
			container : '#sp-add-service',
			title : "Available Services",
			html : true,
			content : this.attributes.popoverContent,
			selector : '#sp-add-service',
			trigger : 'manual'
		});

		$(e.target).popover("show");
		write("togggle");

		$.each($(".list-group-item.service"), function(idx, value) {

			$(value).on("click", function(e1) {
                 
				//$(this).addClass("active");
				var text = $.trim($(this).text().replace(/[\t\n]+/g, ''));
				var currServices = (self.model.clone().get("services"));
				
				currServices = jQuery.grep(currServices, function(value) {
					return value != text;
				});
				
				currServices.push(text);
				write(currServices);
				self.model.set("services", currServices);
				self.model.save();
                self.closePopover(e);
                
			});
        });
        
	},
    closePopover: function (e) {
        $(e.target).popover("hide");
       
    },
    edit: function () {
        $("#client-data").html(this.editTemplate(this.model.toJSON()));
        return this;
    },
    save: function () {
        var username = $("#new-username").val();
        var newName = String($("#new-name").val()).split(",");
        var firstName = $.trim(newName[0]);
        var lastName = $.trim(newName[1]);
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
        return this;
    },
    cancel: function () {
        $("#client-data").html(this.template(this.model.toJSON()));
    },
    render: function () {
        
        $("#client-data").html(this.template(this.model.toJSON()));
        
            showRadiusSlider();
        
        // getAddress(this.model).done(function (value) {
        //     $("#sp-location").text(value);
        //     this.model.set()

        // });

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
