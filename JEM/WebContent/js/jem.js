
var client;

function loadAndHookFunctions() {
    client = new $.RestClient('/JEM/v1/');
    addClientEndpoints();
    getApiMethods();
    $("#clear").on('click', function () {
        $('#console').empty();
    });
    $("#docs").on('click', function () {
        window.location.href = '/JEM/docs';
    });
    $("#methods").on('click', function () {
        getApiMethods();
        //writeConsole(methods);
    });
    $("#ping").on('click', function () {
        pingServer();
    });

}
function pingServer(){
    client.ping.read().done(function (data, textStatus, xhrObject) {
        showSuccess(JSON.stringify(xhrObject));
    });
}
function getClients(){
    client.client-all.read().done(function (data, textStatus, xhrObject) {
        writeConsole(data);
    });
}
function addClientEndpoints() {
    //client.add('ping');
    client.add('methods');
    
}
function executeCall(event){
    var exec = event.data.met;
    console.log(exec);
    client.add(exec);
    client[exec].read().done(function (data) {
        writeConsole(data)
    });
}
function getApiMethods() {
    client.methods.read().done(function (data) {
        populateMethods(data);
    });
}
function populateMethods(data) {
    console.log(JSON.stringify(data));
    $.each(data,function(i,item){
        if(data[i]=='methods') return true;
        var button = $("<button/>").attr("type","button").attr("class","btn btn-primary").attr("id", data[i]).text(data[i].toUpperCase());
        
        button.click({met:data[i]},executeCall);
        $("#api_methods").append(button);
    });
    
}
function writeConsole(data) {
    $('#console').text(JSON.stringify(data, null, 4));
}
function showSuccess(info){
	toastr.success(info);
}
function showWarning(info){
	toastr.warning(info);
}
function showError(info){
	toastr.error(info);
}

