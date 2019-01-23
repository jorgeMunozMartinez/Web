"use strict";

function Player() {
	this.id = -1;
	this.userName = null;
	this.email = null;
	this.pwd = null;
	this.img = null;
	this.Match = null;
}

Player.prototype.login = function() {
	var userI = document.getElementById("lUserName").value;
	var pwdI = document.getElementById("lPwd").value;

	var request = new XMLHttpRequest();
	request.open("POST", "loginJSP.jsp");
	request.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if (request.readyState == 4) {
			var response = JSON.parse(request.responseText);
			var estado = response.get("type");
			if (estado == "OK") {
				sessionStorage.userName = response.get("email");
				sessionStorage.img = response.get("img");
				sessionStorage.email = response.get("email");
				window.location.href = "http://localhost:8080/gamesList.html";
			}
		}
	}
	var p = {
		user : userI,
		pwd : pwdI
	};
	var linea = "p=" + JSON.stringify(parametros);
	request.send(linea);
}
