
var consoleName = 'Economic Issues';
var currentView = 'CLOUD';	//	DASHBOARD,CLOUD,APPLICATIONS,COMPONENTS,DEPLOYMENT,PLATFORM

function loadAndHookFunctions() {

	console.log("HERE!");
	overviewLayout();
	hookSideBar();
	$('#hero_carousel').owlCarousel({
		navContainer:"",
		items:1,
		loop:true,
	    margin:10,
	    nav:true
	});
	$("#hero_scroll_overview").click(function (){
        $('html, body').animate({
            scrollTop: $("#caption_side").offset().top
        }, 1000);
    });
	$("#hero_scroll_history").click(function (){
        $('html, body').animate({
            scrollTop: $("#history_container").offset().top
        }, 1000);
    });
	$("#hero_scroll_culture").click(function (){
        $('html, body').animate({
            scrollTop: $("#culture_header").offset().top
        }, 1000);
    });
	$("#hero_scroll_policy").click(function (){
        $('html, body').animate({
            scrollTop: $("#policy_tab_container").offset().top
        }, 1000);
    });
	$("#hero_scroll_solutions").click(function (){
        $('html, body').animate({
            scrollTop: $("#solutions_tab_container").offset().top
        }, 1000);
    });
	
}

function overviewLayout(){
	$("#current_view_info").empty();
	$("#current_view_info").append(consoleName + '&nbsp;').append($("<i>").attr("class","fa fa-caret-right fa-lg").css("color","grey")).append("&nbsp;Overview");
	$("#overview_tab").show();
	$("#history_tab").hide();
	$("#policy_tab").hide();
	$("#culture_tab").hide();
	$("#success_tab").hide();
	
}
function historyLayout(){
	$("#current_view_info").empty();
	$("#current_view_info").append(consoleName + '&nbsp;').append($("<i>").attr("class","fa fa-caret-right fa-lg").css("color","grey")).append("&nbsp;History");
	$("#overview_tab").hide();
	$("#policy_tab").hide();
	$("#culture_tab").hide();
	$("#success_tab").hide();
	$("#history_tab").show();
}
function policyLayout(){
	$("#current_view_info").empty();
	$("#current_view_info").append(consoleName + '&nbsp;').append($("<i>").attr("class","fa fa-caret-right fa-lg").css("color","grey")).append("&nbsp;Policy");
	$("#overview_tab").hide();
	$("#history_tab").hide();
	$("#culture_tab").hide();
	$("#success_tab").hide();
	$("#policy_tab").show();
}
function cultureLayout(){
	$("#current_view_info").empty();
	$("#current_view_info").append(consoleName + '&nbsp;').append($("<i>").attr("class","fa fa-caret-right fa-lg").css("color","grey")).append("&nbsp;Culture");
	$("#overview_tab").hide();
	$("#history_tab").hide();
	$("#policy_tab").hide();
	$("#success_tab").hide();
	$("#culture_tab").show();
}
function successLayout(){
	$("#current_view_info").empty();
	$("#current_view_info").append(consoleName + '&nbsp;').append($("<i>").attr("class","fa fa-caret-right fa-lg").css("color","grey")).append("&nbsp;Limited Success");
	$("#overview_tab").hide();
	$("#history_tab").hide();
	$("#policy_tab").hide();
	$("#culture_tab").hide();
	$("#success_tab").show();
}
function hookSideBar(){
	
	$('#side_bar_menu a').off();
	$(".btn-navbar").off();
	$("#sidebar_expander").removeClass().addClass("fa fa-angle-double-right fa-2x");
	$("#side_menu_bar_container").off();
	$(".btn-navbar").click(function() {
		$("#sidebar_expander").hide();
		$("#side_bar_menu").show();
		$("#side_menu_bar_container").css("width", "inherit");
		$("#side_menu_bar_container").off();
		$("#side_menu_bar_container").mouseleave(function() {
			hookSideBar();
		})
		$("#side_menu_bar_container").click(function() {
			hookSideBar();
		})
	});
	$('#side_bar_menu a').click(function(e) {
		e.preventDefault(); 
		$('html, body').animate({
            scrollTop: 0
        }, 1000);
		var id = $(this).attr('id');
		console.log(id);
		if (id === "goto_overview") {
			currentView = 'OVERVIEW';
			overviewLayout();
		} else if (id === "goto_history") {
			currentView = 'HISTORY';
			historyLayout();
		} else if (id === "goto_policy") {
			currentView = 'POLICY';
			policyLayout();
		} else if (id === "goto_culture") {
			currentView = 'CULTURE';
			cultureLayout();
		} else if (id === "goto_success") {
			currentView = 'SUCCESS';	
			successLayout();
		}
		
		$(this).tab('show');
		
		//	Collapse if mobile
	    $(".nav-collapse").collapse('hide');
	
	});
	$("#sidebar_expander").show();
	
	$("#side_menu_bar_container").css("width","1em");
	$("#side_bar_menu").hide();
	$("#side_menu_bar_container").mouseover(function(){
		
		$("#sidebar_expander").hide();
		
		$("#side_bar_menu").show();
		$("#side_menu_bar_container").css("width","inherit");
		$("#side_menu_bar_container").off();
		$("#side_menu_bar_container").mouseleave(function(){
			hookSideBar();
		})
		$("#side_menu_bar_container").click(function(){
			hookSideBar();
		})
	});
}
