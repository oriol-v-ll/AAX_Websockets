window.onload = init;
var socket = new WebSocket("ws://localhost:8080/websocketexample/actions");
socket.onmessage = onMessage;



function onMessage(event) {
    var device = JSON.parse(event.data);
    if (device.action === "add") {
    	printKPIComparationElement(device);
    }
    if (device.action === "remove") {
        document.getElementById(device.id).remove();
    }
    if (device.action === "toggle") {
        var node = document.getElementById(device.id);
        var statusText = node.children[2];
        if (device.status === "On") {
            statusText.innerHTML = "Status: " + device.status + " (<a href=\"#\" OnClick=toggleDevice(" + device.id + ")>Turn off</a>)";
        } else if (device.status === "Off") {
            statusText.innerHTML = "Status: " + device.status + " (<a href=\"#\" OnClick=toggleDevice(" + device.id + ")>Turn on</a>)";
        }
    }
}


function addComparation(name1, name2, type1, type2){
	var CompareKPI = {
        action: "compare",
        name1: name1,
        type1: type1,
        name2: name2,
        type2: type2,
    }; 
 socket.send(JSON.stringify(CompareKPI));
}

function removeKPI(element) {
    var id = element;
    var DeviceAction = {
        action: "remove",
        id: id
    };
    socket.send(JSON.stringify(DeviceAction));
}



function printKPIComparationElement(kpi){
	var content = document.getElementById("content");
	
	var kpiDiv = document.createElement("div");
	kpiDiv.setAttribute("id", kpiDiv.id);
	kpiDiv.setAttribute("class", "kpi" + kpi.type);
    content.appendChild(kpiDiv);
    
    var kpiName1 = document.createElement("span");
    kpiName1.setAttribute("class", "kpiName1");
    kpiName1.innerHTML = kpi.name;
    kpiDiv.appendChild(kpiName1);

    var kpiType = document.createElement("span");
    kpiType.innerHTML = "<b>Type:</b> " + kpi.type;
    kpiDiv.appendChild(kpiType);
    
    var kpiName2 = document.createElement("span");
    kpiName2.setAttribute("class", "kpiName2");
    kpiName2.innerHTML = kpi.name2;
    kpiDiv.appendChild(kpiName2);
    
    var kpiComparation = document.createElement("span");
    kpiComparation.setAttribute("class", "comparation");
    kpiComparation.innerHTML = kpi.comparation;
    kpiDiv.appendChild(kpiName2);
    
    var kpiType = document.createElement("span");
    kpiType.innerHTML = "<b>Type:</b> " + kpi.type2;
    kpiDiv.appendChild(kpiType);
    
    /*Creación del sistema del numero aleatorio + hora*/
    
    /*Creación del sistema del numero aleatorio + hora*/
    
    var removeKpi = document.createElement("span");
    removeKpi.setAttribute("class", "removeKpi");
    removeKpi.innerHTML = "<a href=\"#\" OnClick=removeKPI(" + kpi.id + ")>Remove comparation</a>";
    kpiDiv.appendChild(removeKpi);
    
	
}


function showForm() {
    document.getElementById("addDeviceForm").style.display = '';
}

function hideForm() {
    document.getElementById("addDeviceForm").style.display = "none";
}

function formSubmit() {
    var form = document.getElementById("addDeviceForm");
	var name1 = form.elements["device_name1"].value;
	var type1 = form.elements["device_type1"].value;
    var name2 = form.elements["device_name2"].value;
    var type2 = form.elements["device_type2"].value;
    hideForm();
    document.getElementById("addDeviceForm").reset();
    addComparation(name1,name2,type1,type2);
}


function form(){

}
function init() {
    hideForm();
}


