 "use strict";

 function Player() {
     this.id = -1;
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

 Usuario.prototype.login = function () {
     var email = document.getElementById("cajaEmailLogin").value;
     var pwd = document.getElementById("cajaPwd").value;

     var request = new XMLHttpRequest();
     request.open("post", "identificar.jsp");
     request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
     request.onreadystatechange = function () {
         if (request.readyState == 4) {
             var resultado = JSON.parse(request.responseText);
             if (resultado.tipo == "OK") {
                 usuario.email = resultado.email;
                 usuario.id = resultado.id;
                 usuario.pathFoto = resultado.pathFoto;
                 sessionStorage.usuario = JSON.stringify(usuario);
                 document.getElementById("mainContentInit").setAttribute("style", "display:none");
                 document.getElementById("mainContent").setAttribute("style", "display:visible");
                 usuario.mostrarBienvenida(true);
                 usuario.cargarListas();
                 conectarWebSocket();
             } else {
                 alert("Error: " + resultado.texto);
             }
         }
     }
     var parametros = {
         email: email,
         pwd: pwd
     };
     var linea = "p=" + JSON.stringify(parametros);
     request.send(linea);
 }

 Usuario.prototype.registrar = function () {
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

 Usuario.prototype.mostrarBienvenida = function (visible) {
     if (this.pathFoto != null) {

         document.getElementById("fotoUsuarioImagen").src = "/listaDeLaCompra/upload/" + this.pathFoto;
     }
     var td = document.getElementById("welcomeUser");
     if (visible) {
         td.setAttribute("style", "display:visible");
         td.innerHTML = "Bienvenid@, " + this.email;
     } else
         td.setAttribute("style", "display:none");
 }

 Usuario.prototype.crearLista = function () {
     var nombre = document.getElementById("nuevaLista").value;
     lista = new Lista();
     lista.crear(nombre);
 }

 Usuario.prototype.invitar = function () {
     var emailInvitado = document.getElementById("emailInvitado").value;
     var request = new XMLHttpRequest();
     request.open("post", "invitar.jsp");
     request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
     request.onreadystatechange = function () {
         if (request.readyState == 4) {
             var resultado = JSON.parse(request.responseText);
             if (resultado.tipo == "OK") {
                 alert("Se ha invitado a " + emailInvitado);
                 document.getElementById("formUsers").setAttribute("style", "display:none");
             } else if (resultado.tipo == "ERROR") {
                 alert("Error al acceder al sistema: " + resultado.error);
             }
         }
     }
     request.send("emailInvitado=" + emailInvitado + "&idLista=" + sessionStorage.idLista);
 }

 Usuario.prototype.cargarListas = function () {
     var request = new XMLHttpRequest();
     request.open("get", "cargarListas.jsp");
     request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
     request.onreadystatechange = function () {
         if (request.readyState == 4) {
             var resultado = JSON.parse(request.responseText);
             if (resultado.tipo == "OK") {
                 var listas = resultado.listas;
                 panelListas.innerHTML = "";
                 var headingPanel = document.createElement("div");
                 headingPanel.setAttribute("class", "panel-heading");
                 headingPanel.innerHTML = "Mis Listas";
                 panelListas.appendChild(headingPanel);
                 for (var i = 0; i < listas.length; i++) {
                     var lista = listas[i];
                     var bodyPanel = document.createElement("div");
                     bodyPanel.setAttribute("class", "panel-body");
                     panelListas.appendChild(bodyPanel);
                     var href = document.createElement("a");
                     bodyPanel.appendChild(href);
                     href.setAttribute("href", "javascript:mostrar(" + lista.id + ")");
                     href.innerHTML = lista.nombre;
                     var br = document.createElement("br");
                     bodyPanel.appendChild(br);
                 }
             } else {
                 alert("Error: " + resultado.texto);
             }
         }
     }
     request.send();
 }

 function Lista() {
     this.idLista = -1;
     this.nombre = null;
 }

 Lista.prototype.cargar = function (idLista) {
     var request = new XMLHttpRequest();
     request.open("post", "cargarLista.jsp");
     request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
     request.onreadystatechange = function () {
         if (request.readyState == 4) {
             var resultado = JSON.parse(request.responseText);
             if (resultado.tipo == "OK") {
                 lista.idLista = idLista;
                 lista.nombre = resultado.lista.nombre;
                 lista.productos = resultado.lista.productos;
                 lista.miembros = resultado.lista.miembros;
                 lista.mostrar();
             } else if (resultado.tipo == "ERROR") {
                 alert("Error al acceder al sistema: " + resultado.error);
             }
         }
     }
     request.send("idLista=" + idLista);
 }

 Lista.prototype.mostrar1 = function () {
     tableLista.innerHTML = "<tr><th rowspan='2'>Producto</th><th colspan='2'>Unidades</th><th rowspan='2'>Hecho</th><th rowspan='3'>Eliminar</th></tr><tr><th>Existencias</th><th>Deseadas</th></tr>";
     sessionStorage.idLista = this.idLista;
     for (var i = 0; i < this.productos.length; i++) {
         var producto = this.productos[i];
         var tr = document.createElement("tr");
         tableLista.appendChild(tr);
         if (producto.cantidadDeseada - producto.cantidadExistente > 0) {
             tr.setAttribute("style", "background-color:red");
         } else {
             tr.setAttribute("style", "background-color:green");
         }
         var td = document.createElement("td");
         tr.appendChild(td);
         td.innerHTML = "<a href='javascript:lista.editarProducto(" +
             producto.idProducto + ")'>" + producto.nombre + "</a>";
         td = document.createElement("td");
         tr.appendChild(td);
         td.innerHTML = producto.cantidadExistente;
         td = document.createElement("td");
         tr.appendChild(td);
         td.innerHTML = producto.cantidadDeseada;
         td = document.createElement("td");
         tr.appendChild(td);
         var btnHecho = document.createElement("button");
         td.appendChild(btnHecho);
         btnHecho.innerHTML = "Hecho";
         btnHecho.setAttribute("onclick", "lista.comprar(" + producto.idProducto +
             ")");
         td = document.createElement("td");
         tr.appendChild(td);
         var btnEliminar = document.createElement("button");
         td.appendChild(btnEliminar);
         btnEliminar.innerHTML = "Eliminar";
         btnEliminar.setAttribute("onclick", "lista.eliminar(" +
             producto.idProducto + ")");
     }

     var trMiembros = document.createElement("tr");
     tableLista.appendChild(trMiembros);
     var th = document.createElement("th");
     trMiembros.appendChild(th);
     th.setAttribute("colspan", "4");
     th.innerHTML = "Miembros";
     for (var i = 0; i < this.miembros.length; i++) {
         var trMiembro = document.createElement("tr");
         tableLista.appendChild(trMiembro);
         var td = document.createElement("td");
         trMiembro.appendChild(td);
         td.setAttribute("colspan", "4");
         td.innerHTML = this.miembros[i].email;
     }
     var btnInvitar = document.createElement("button");
     tableLista.appendChild(btnInvitar);
     btnInvitar.innerHTML = "Invitar a alguien";
     btnInvitar
         .setAttribute(
             "onclick",
             "javascript:document.getElementById('divInvitar').setAttribute('style', 'display:visible')");

     var btnAbandonar = document.createElement("button");
     tableLista.appendChild(btnAbandonar);
     btnAbandonar.innerHTML = "Abandonar";
     btnAbandonar
         .setAttribute(
             "onclick",
             "javascript:document.getElementById('divAbandonar').setAttribute('style', 'display:visible')");
 }

 Lista.prototype.mostrar = function () {
     var listaSel = document.getElementById("listaSel");
     var containerLista = document.getElementById("panelListasBody");
     var panelMiembrosBody = document.getElementById("panelMiembrosBody");

     containerLista.innerHTML = "";
     panelMiembrosBody.innerHTML = "";
     listaSel.setAttribute("style", "display:visible");

     sessionStorage.idLista = this.idLista;
     for (var i = 0; i < this.productos.length; i++) {
         var producto = this.productos[i];


         var btnOk = document.createElement("button");
         btnOk.innerHTML = "<img src='img/hecho.png' style='width:16px'>";
         btnOk.setAttribute("onclick", "lista.comprar(" + producto.idProducto + ")");

         var btnDel = document.createElement("button");
         btnDel.innerHTML = "<img src='img/eliminar.gif' style='width:16px'>";
         btnDel.setAttribute("onclick", "lista.eliminar(" + producto.idProducto + ")");

         var btnEdit = document.createElement("button");
         btnEdit.innerHTML = "<img src='img/editar.png' style='width:16px'>";
         btnEdit.setAttribute("onclick", "lista.editarProducto(" + producto.idProducto + ")");

         var filaProducto = document.createElement("div");
         filaProducto.setAttribute("class", "row");
         containerLista.appendChild(filaProducto);

         var fotoProducto = document.createElement("img");
         fotoProducto.setAttribute("class", "col-lg-2 col-xs-2");
         if (producto.pathFoto != null) {
             fotoProducto.setAttribute("src", "/listaDeLaCompra/upload/" + producto.pathFoto);
         }
         containerLista.appendChild(fotoProducto);

         var nombreProducto = document.createElement("div");
         nombreProducto.setAttribute("class", "col-lg-2 col-xs-2");
         containerLista.appendChild(nombreProducto);
         nombreProducto.innerHTML = producto.nombre;
         var existenciasProducto = document.createElement("div");
         existenciasProducto.setAttribute("class", "col-lg-2 col-xs-2");
         containerLista.appendChild(existenciasProducto);
         existenciasProducto.innerHTML = producto.cantidadExistente;
         var deseadoProducto = document.createElement("div");
         deseadoProducto.setAttribute("class", "col-lg-3 col-xs-2");
         containerLista.appendChild(deseadoProducto);
         deseadoProducto.innerHTML = producto.cantidadDeseada;
         var btnHecho = document.createElement("div");
         btnHecho.setAttribute("class", "col-lg-1 col-xs-1");
         containerLista.appendChild(btnHecho);
         btnHecho.appendChild(btnOk);
         var btnEliminar = document.createElement("div");
         btnEliminar.setAttribute("class", "col-lg-1 col-xs-1");
         containerLista.appendChild(btnEliminar);
         btnEliminar.appendChild(btnDel);
         var btnEditar = document.createElement("div");
         btnEditar.setAttribute("class", "col-lg-1 col-xs-1");
         containerLista.appendChild(btnEditar);
         btnEditar.appendChild(btnEdit);
     }

     for (var i = 0; i < this.miembros.length; i++) {
         var miembro = document.createElement("div");
         miembro.innerHTML = this.miembros[i].email;
         panelMiembrosBody.appendChild(miembro);
     }
 }

 Lista.prototype.editarProducto = function (idProducto) {
     var producto = null;
     for (var i = 0; i < this.productos.length; i++) {
         if (this.productos[i].idProducto == idProducto) {
             producto = this.productos[i];
             break;
         }
     }

     /*Mostrar Foto
    sessionStorage.producto = JSON.stringify(producto);
    sessionStorage.productoPath = producto.pathFoto;
	*/
     document.getElementById("formProduct").setAttribute("style", "display:visible");
     var btnEditProduct = document.getElementById("btnEditProduct");
     btnEditProduct.setAttribute("style", "display:visible");
     btnEditProduct.setAttribute("onclick", "guardarProducto(" + idProducto + ")");
     document.getElementById("nombreProducto").value = producto.nombre;
     document.getElementById("cantidadDeseada").value = producto.cantidadDeseada;
     document.getElementById("cantidadExistente").value = producto.cantidadExistente;
     document.getElementById("uploadProductPhoto").setAttribute("style", "display:visible");
     document.getElementById("idProductoFotoProducto").value = idProducto;
 }

 Lista.prototype.crear = function (nombreLista) {
     var request = new XMLHttpRequest();
     request.open("post", "crearLista.jsp");
     request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
     request.onreadystatechange = function () {
         if (request.readyState == 4) {
             var resultado = JSON.parse(request.responseText);
             if (resultado.tipo == "OK") {
                 alert("Se ha creado la lista " + nombreLista);
                 usuario.cargarListas();
             } else if (resultado.tipo == "ERROR") {
                 alert("Error al acceder al sistema: " + resultado.error);
             }
         }
     }
     request.send("nombreLista=" + nombreLista);
 }

 Lista.prototype.comprar = function (idProducto) {
     var request = new XMLHttpRequest();
     request.open("post", "comprar.jsp");
     request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
     request.onreadystatechange = function () {
         if (request.readyState == 4) {
             var resultado = JSON.parse(request.responseText);
             if (resultado.tipo == "OK") {
                 lista.cargar(lista.idLista);
             } else {
                 alert("Error: " + resultado.texto);
             }
         }
     }
     var p = {
         idLista: this.idLista,
         idProducto: idProducto
     };
     var linea = "p=" + JSON.stringify(p);
     request.send(linea);
 }

 Lista.prototype.insertarProducto = function () {
     var nombre = document.getElementById("nombreProducto").value;
     var cantidadDeseada = parseInt(document.getElementById("cantidadDeseada").value);
     var cantidadExistente = parseInt(document.getElementById("cantidadExistente").value);

     var request = new XMLHttpRequest();
     request.open("post", "addProducto.jsp");
     request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
     request.onreadystatechange = function () {
         if (request.readyState == 4) {
             var resultado = JSON.parse(request.responseText);
             if (resultado.tipo == "OK") {
                 lista.cargar(lista.idLista);
             } else {
                 alert("Error: " + resultado.texto);
             }
         }
     }

     var p = {
         idLista: this.idLista,
         nombre: nombre,
         cantidadDeseada: cantidadDeseada,
         cantidadExistente: cantidadExistente
     };
     var linea = "p=" + JSON.stringify(p);
     request.send(linea);
 }

 Lista.prototype.eliminar = function (idProducto) {
     var request = new XMLHttpRequest();
     request.open("post", "eliminarProducto.jsp");
     request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
     request.onreadystatechange = function () {
         if (request.readyState == 4) {
             var resultado = JSON.parse(request.responseText);
             if (resultado.tipo == "OK") {
                 lista.cargar(lista.idLista);
             } else {
                 alert("Error: " + resultado.texto);
             }
         }
     }
     var p = {
         idLista: this.idLista,
         idProducto: idProducto
     };
     var linea = "p=" + JSON.stringify(p);
     request.send(linea);
 }

 Lista.prototype.abandonar = function (idLista) {
     var request = new XMLHttpRequest();
     request.open("post", "abandonar.jsp");
     request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
     request.onreadystatechange = function () {
         if (request.readyState == 4) {
             var resultado = JSON.parse(request.responseText);
             if (resultado.tipo == "OK") {
                 alert("Mensaje: " + resultado.texto);
                 usuario.cargarListas();
             } else {
                 alert("Error: " + resultado.texto);
             }
         }
     }
     var p = {
         idLista: this.idLista,
     };
     var linea = "p=" + JSON.stringify(p);
     request.send(linea);
 }


 Usuario.prototype.registrarUsuarioGoogle = function (profile) {
     var request = new XMLHttpRequest();
     request.open("post", "registrarRedSocial.jsp");
     request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
     request.onreadystatechange = function () {
         if (request.readyState == 4) {
             var resultado = JSON.parse(request.responseText);
             if (resultado.tipo == "OK") {
                 usuario.email = resultado.email;
                 usuario.id = resultado.id;
                 sessionStorage.usuario = JSON.stringify(usuario);
                 document.getElementById("mainContentInit").setAttribute("style", "display:none");
                 document.getElementById("mainContent").setAttribute("style", "display:visible");
                 usuario.mostrarBienvenida(true);
                 usuario.cargarListas();
                 conectarWebSocket();
                 alert("OK: " + resultado);
             } else {
                 alert("Error: " + resultado.texto);
             }
         }
     }
     var p = {
         email: profile.getEmail(),
     };
     var linea = "p=" + JSON.stringify(p);
     request.send(linea);
 }