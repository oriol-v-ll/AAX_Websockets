window.onload = init;
var socket = new WebSocket("ws://localhost:8080/websocketexample/actions");
socket.onmessage = onMessage;



function onMessage(event) {
    var kpi = JSON.parse(event.data);
    if (kpi.action === "add") {
    	printKPIComparationElement(kpi);
    }
    if (kpi.action === "remove") {
        document.getElementById(kpi.id).remove();
    }
    if (kpi.action === "actualizar") {
        var node = document.getElementById("comp"+kpi.id);
        var comparationText = node.children[4];
        comparationText = "<b>ComparaciónActualizada:</b> " + kpi.comparacion;;
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
	kpiDiv.setAttribute("id", kpi.id);
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
    
 
    
    var kpiType = document.createElement("span");
    kpiType.innerHTML = "<b>Type:</b> " + kpi.type2;
    kpiDiv.appendChild(kpiType);
    
    var kpiComparation = document.createElement("span");
    kpiComparation.setAttribute("id", "comp"+kpi.id);
    kpiComparation.innerHTML = "<b>Comparación:</b> " + kpi.comparacion;
    kpiDiv.appendChild(kpiComparation);
    
  
    
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


