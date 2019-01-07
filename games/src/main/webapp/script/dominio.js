"use strict";

 function Player() {
     this.userName = null;
     this.email = null;
     this.password = null;
     this.pathFoto = null;
 }

 Player.prototype.connect = function () {
     var request = new XMLHttpRequest();
     request.open("get", "connect.jsp");
     request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
     request.onreadystatechange = function () {
         if (request.readyState == 4) {
             var result = JSON.parse(request.responseText);
             if (result.tipo == "OK") {
                 player.id = result.id;
                 player.userName = resut.userName;
                 player.email = resut.email;
                 player.pathFoto = result.pathFoto;
                 sessionStorage.player = JSON.stringify(player);
                 usuario.cargarListas();
                 conectarWebSocket();
             } else if (resultado.tipo == "ERROR") {
                 alert("Error al acceder al sistema: " + resultado.texto);
             } else if (resultado.tipo == "NO_EXISTE") {
                 document.getElementById("mainContentInit").setAttribute("style", "display:true")
                 document.getElementById("mainContent").setAttribute("style", "display:none");
             }
         }
     };
     request.send();
 }

 Player.prototype.login = function () {
     var lblpsw = document.getElementById("lblpsw").value;
     var lblname = document.getElementById("lblname").value;

     var request = new XMLHttpRequest();
     request.open("post", "login.jsp");
     request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
     request.onreadystatechange = function () {
         if (request.readyState == 4) {
             var result = JSON.parse(request.responseText);
             if (result.tipo == "OK") {
                 player.userName = result.userName;
                 player.email = result.email;
                 player.img = result.img;
                 sessionStorage.player = JSON.stringify(player);
                 window.location.href = "http://localhost:8080/gamesList.html";
             } else {
                 alert("Error: " + result.texto);
             }
         }
     }
     var p = {
         name : lblname,
         pwd : lblpsw
     };
     var line = "p=" + JSON.stringify(p);
     request.send(line);
 }

Player.prototype.registrar = function () {
     var email = document.getElementById("cajaEmailRegistro").value;
     var pwd1 = document.getElementById("cajaPwd1").value;
     var pwd2 = document.getElementById("cajaPwd2").value;

     var request = new XMLHttpRequest();
     request.open("post", "registrar.jsp");
     request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
     request.onreadystatechange = function () {
         if (request.readyState == 4) {
             var resultado = JSON.parse(request.responseText);
             if (resultado.tipo == "OK") {
                 document.getElementById("mainContentInit").setAttribute("style", "display:none");
                 document.getElementById("mainContent").setAttribute("style", "display:visible");
                 usuario.conectar();
             } else {
                 alert("Error: " + resultado.texto);
             }
         }
     }
     var parametros = {
         email: email,
         pwd1: pwd1,
         pwd2: pwd2
     };
     var linea = "p=" + JSON.stringify(parametros);
     request.send(linea);
 }
