function getFechilla(fecha){
	
	var regreso="";
	var dato= fecha.getMonth()+1;
	dato= completar(dato);
	regreso= fecha.getFullYear()+"-"+dato+"-";
	dato = fecha.getDate();
	regreso+=completar(dato)+" ";
	dato= fecha.getHours();
	regreso+= completar(dato)+":";
	dato = fecha.getMinutes();
	regreso+= completar(dato)+":";
	dato = fecha.getSeconds();
	regreso+=completar(dato)+"";
	
	return regreso;
	
}

function completar(dato){
	
	if(dato<10){
		return 0+""+dato;
	}

	return dato+"";
	
}

app.service("ventasService",['$http','$q',function($http,$q){
	this.addVenta=function(venta){
		var d = $q.defer();
		$http.post("/ventas/add/", venta).then(
			function(response) {
				console.log(response);
				d.resolve(response.data);
			});
		return d.promise;
	};
	
	this.updateVenta=function(venta){
		var d = $q.defer();
		$http.post("/ventas/update", venta).then(
			function(response) {
				console.log(response);
				d.resolve(response.data);
			});
		return d.promise;
	};
	
	this.cancelarVenta=function(venta){
		var d = $q.defer();
		$http.post("/ventas/cancelarVenta", venta).then(
			function(response) {
				console.log(response);
				d.resolve(response.data);
			});
		return d.promise;
	};
	
	this.buscar=function(fi,ff){
		var d = $q.defer();
		send={
				params:{
					fi:fi,
					ff,ff
				}
		}
		$http.get("/ventas/buscar/",send).then(
			function(response) {
				console.log(response);
				d.resolve(response.data);
			}, function(response) {
				if(response.status==403){
					alert("No está autorizado para realizar esta acción");
					$location.path("/");
				}
			});
		return d.promise;
	}
	
	this.numPagesInventario = function() {
		var d = $q.defer();
		$http.get("/inventario/numPages").then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
	
	this.getProductos= function(page){
		var d = $q.defer();
		$http.get("/ventas/productos/"+page).then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	}
	
	this.findVentas= function(url,page){
		var d = $q.defer();
		$http.get(url+page).then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	}
	this.facturarVenta= function(venta){
		var d = $q.defer();
		$http.post("/ventas/facturar/",venta).then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	}
	
	this.facturarVenta33= function(venta){
		var d = $q.defer();
		$http.post("/ventas33/facturar/",venta).then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	}
	
	this.cancelarFactura= function(venta){
		var d = $q.defer();
		$http.post("/ventas/cancelarAck/",venta).then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	}
	
	this.cancelarFactura33= function(venta){
		var d = $q.defer();
		$http.post("/ventas33/cancelarAck/",venta).then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	}
	
	this.enviarFactura= function(venta){
		var d = $q.defer();
		$http.post("/ventas/sendmail/",venta).then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	}
	this.enviarFactura33= function(venta){
		var d = $q.defer();
		$http.post("/ventas33/sendmail/",venta).then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	}
	
	this.numPages = function(){
		var d = $q.defer();
		$http.get("/ventas/numPages").then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	}
	// cancelarAck
}]);

app.controller("ventaController",['$window','clientesService','ventasService','tornillosService','herramientasService','$scope','$location','envaseService',function($window,clientesService,ventasService,tornillosService,herramientasService,$scope,$location,envaseService){
	$scope.MetodoPago=true;
	$scope.paginaActual=1;
	
	$scope.llenarPags=function(){
		var inicio=0;
		if($scope.paginaActual>3){
			inicio=$scope.paginaActual-3;
		}
		var fin = inicio+5;
		if(fin>$scope.maxPage){
			fin=$scope.maxPage;
		}
		$scope.paginas=[];
		for(var i = inicio; i< fin; i++){
			$scope.paginas.push(i+1);
		}
		for(var i = inicio; i<= fin; i++){
			$('#pag'+i).removeClass("active");
		}
		$('#pag'+$scope.paginaActual).addClass("active");	
	}
	
	$scope.quitaConcepto=function(index){
		$scope.venta.detalles.splice(index, 1);
		$scope.venta.total=0;
		for(var i=0; i<$scope.venta.detalles.length; i++){
			$scope.venta.total+=	parseFloat($scope.venta.detalles[i].importe);
			$scope.venta.total=parseFloat($scope.venta.total.toFixed(2));
		}
		
	}
	
	$scope.cargarPagina=function(pag){
		if($scope.paginaActual!=pag){
			$scope.paginaActual=pag;
			$scope.cargaProductos(pag);
		}
	}
	ventasService.numPagesInventario().then(function(data){
		$scope.maxPage=data;
		$scope.llenarPags();
		
	})
	
	envaseService.getEnvase().then(function(data){
		$scope.envases = data;
	})
	$scope.venta={
			fecha:new Date(),
			detalles:[],
			envase:[],
			formaDePago:"Efectivo",
			metodoDePago:"PUE"
	};
	$scope.cargaClientes=function(){
		clientesService.findClientesFull().then(function(data){
			$scope.clientes=data;
		})
	}
	
	$scope.cargaClientes();
	
	$scope.botonBuscar=false;
	
	$scope.$watch('indice',function(){
		if($scope.indice && $scope.indice!=-1){
			$scope.clienteSelected=$scope.clientes[$scope.indice];
			
			$scope.venta.idCliente=$scope.clienteSelected.id;
			$scope.venta.descuento=$scope.clienteSelected.descuento;
		}
		if($scope.indice && $scope.indice==-1){
			$scope.venta.idCliente=0;
			$scope.venta.descuento=0;
		}
	})
	
	$scope.calculaDesc=function(){
		if($scope.descuento){
			$scope.venta.total=0;
			for(var i=0; i<$scope.venta.detalles.length; i++){
				$scope.venta.detalles[i].importe =	$scope.venta.detalles[i].importe - ($scope.venta.detalles[i].importe * ($scope.venta.descuento/100));
				$scope.venta.detalles[i].importe= $scope.venta.detalles[i].importe.toFixed(2);
				$scope.venta.total+=	parseFloat($scope.venta.detalles[i].importe);
			}
		}else{
			$scope.venta.total=0;
			for(var i=0; i<$scope.venta.detalles.length; i++){ 
				$scope.venta.detalles[i].importe =	($scope.venta.detalles[i].importe*100) / (100 - $scope.venta.descuento);
				$scope.venta.detalles[i].importe= $scope.venta.detalles[i].importe.toFixed(2);
				$scope.venta.total+=	parseFloat($scope.venta.detalles[i].importe);
			}
		}
	}
// $scope.productos=[];
// $scope.herramientas = function() {
// herramientasService.findHerramientasAll().then(
// function(data) {
// $scope.herramientas = data;
// for(var i =0; i<data.length; i++){
// $scope.productos.push(data[i]);
// }
//				
// })
// }
// $scope.herramientas();
// $scope.tornillos = function() {
// tornillosService.findTornillos().then(
// function(data) {
// $scope.tornillos = data;
// for(var i =0; i<data.length; i++){
// $scope.productos.push(data[i]);
// }
// })
// }
// $scope.tornillos();
	
	$scope.agregarDetalle=function(producto,ind){
		if($scope.venta.idCliente === undefined){
			alert("Selecciona un cliente");
			return;
		}
		if(producto.precio){
			if(producto.cantidad){
		$scope.MetodoPago=false;
		var detalle={}
		detalle.idProducto=producto.id;
		detalle.descripcion= producto.nombre;
		detalle.porcentaje= producto.porcentaje;
		detalle.tipoPrecio = document.getElementById('precio'+ind).selectedOptions[0].text
		if(producto.medidas){
			detalle.descripcion= detalle.descripcion+" "+producto.medidas;
		}
		detalle.cantidad=producto.cantidad;
		detalle.precioUnitario=producto.precio;
		detalle.importe= producto.importe;
		detalle.claveUnidad=producto.claveUnidad;
		detalle.claveSat=producto.claveSat;
		detalle.unidad=producto.unidadSat;
		if($scope.descuento){
				detalle.importe =	producto.importe - (producto.importe * ($scope.venta.descuento/100));
				detalle.importe= parseFloat(detalle.importe.toFixed(2));
		}
		
		detalle.tipo=producto.tipo;
		$scope.venta.detalles.push(detalle);
		$scope.venta.total=0;
		for(var i=0; i<$scope.venta.detalles.length; i++){
			$scope.venta.total+=	parseFloat($scope.venta.detalles[i].importe);
			$scope.venta.total=parseFloat($scope.venta.total.toFixed(2));
		}
			}else{
				alert("Indique la cantida");
			}
		}else{
			alert("Seleccione un precio para el producto");
		}
	}
	
	$scope.busqueda=[];
	
	$scope.buscar = function(buscar){
		$scope.botonBuscar=true;
		$scope.todos=false;
		$scope.busqueda=[];
		$scope.paginaActual=-1;
		if(buscar.tipo){
			if(buscar.tipo=="Herramientas"){
				herramientasService.busqueda(buscar.buscar).then(function(data) {
					// $scope.herramientas = data;
					$scope.productos=data;
					$scope.botonBuscar=false;
					$scope.setPorcentaje();
				});
			}else{
				tornillosService.busqueda(buscar.buscar).then(
						function(data) {
							$scope.productos = data;
							$scope.botonBuscar=false;
							$scope.setPorcentaje();
						});
			}
			
		}else{
			alert("Especifique el tipo de Producto");
		}
	}
	
	$scope.cargaProductos=function(page){
	ventasService.getProductos(page).then(function(data){
		$scope.productos=[];
		$scope.todos=true;
		for(var i =0; i<data[0][1].length;i++){
			$scope.productos.push(data[1][i]);
		}
		for(var i =0; i<data[0].length;i++){
			$scope.productos.push(data[0][i]);
		}
		$scope.llenarPags();
	})
	}

	$scope.getPorcentaje = function(){
		if($scope.indice == -1){
			for(o in $scope.productos){
				$scope.productos[o].porcentaje= 100;
			}
		}else{
		var id = $scope.clientes[$scope.indice].id;
		
	herramientasService.getProductAsig(id).then(function(data){
		$scope.pClient = data;
		console.log("porcentaje ",$scope.pClient)
		$scope.setPorcentaje();
	});
		}
	}
	$scope.setPorcentaje = function(){
	for(o in $scope.productos){
		$scope.productos[o].porcentaje= 100;
		
		for(i in $scope.pClient){
			if($scope.productos[o].id == $scope.pClient[i].idProducto){
				$scope.productos[o].porcentaje = $scope.pClient[i].porcentaje;
			}
		}
	}
	}
	$scope.calculaImporte=function(producto){
		var valor=producto.cantidad*producto.precio;
//		var tipo = document.getElementById("precio");
//		var selected = tipo.options[tipo.selectedIndex].text;
//		if(valor<=0){alert("El Precio unitario en\n "+ selected +"\nDebe ser mayor a 0")}
		return valor.toFixed(2)*1;
	}
	$scope.cargaProductos(1);
	
	$scope.agregarEnvase = function(){
		if(!$scope.tipoEnvase){
			alert("Capture el tipo de envase");
			return
		}
		if(!$scope.cantidadEnvase){
			alert("Capture la cantidad del envase");
			return
		}
		var renglon= {tipoEnvase: $scope.tipoEnvase, cantidadEnvase: $scope.cantidadEnvase}
		$scope.venta.envase.push(renglon)
		$scope.tipoEnvase="";
		$scope.cantidadEnvase="";
		
	}
	$scope.eliminarEnvase = function(ind){
		$scope.venta.envase.splice(ind, 1);
	}
	
	$scope.guardarVenta= function (estatus){
		console.log();
		if($scope.venta.idCliente === undefined){
			alert("Selecciona un cliente");
		}else{
			$scope.venta.estatus=estatus;
			ventasService.addVenta($scope.venta).then(function(data){
				alert("La venta ha sido guardada");
				$location.path('/ventasList');
				$window.location.reload();
			});
		}
	}
	$scope.busquedas={tipo:"Herramientas"}
	
}]);
app.controller("ventaListController",['clientesService','ventasService','tornillosService','herramientasService','$scope','$location','$window',function(clientesService,ventasService,tornillosService,herramientasService,$scope,$location,$window){

	$scope.url="/ventas/findAll/"
	$scope.llenarPags=function(){
		var inicio=0;
		if($scope.paginaActual>3){
			inicio=$scope.paginaActual-3;
		}
		var fin = inicio+5;
		if(fin>$scope.maxPage){
			fin=$scope.maxPage;
		}
		$scope.paginas=[];
		for(var i = inicio; i< fin; i++){
			$scope.paginas.push(i+1);
		}
		for(var i = inicio; i<= fin; i++){
			$('#pag'+i).removeClass("active");
		}
		$('#pag'+$scope.paginaActual).addClass("active");
		
	}

	$scope.semaforo = function(v){
		var isCredito = false;
		for(i in v.detalles){
			if(v.detalles[i].tipoPrecio == "Crédito"){
				isCredito = true;
			}
		}
		if(isCredito){
	for(i in $scope.clientes){
			if(v.idCliente == $scope.clientes[i].id){
				var fechaFin = new Date().getTime();
				var fechaInicio = new Date(v.fecha).getTime();
				var diff = fechaFin - fechaInicio;
				var dias = diff/(1000*60*60*24);
				var str = ""+dias;
				var res = str.split(".");
				if($scope.clientes[i].diasCredito){
					var valor = ($scope.clientes[i].diasCredito - res[0]) * 1;
				}else{ valor = 0}
				var clas= "";
				if (valor > 3){
					clas= "text-info"
				}
				if (valor <= 3 && valor > 0){
					clas= "amarillo"
				}
				if (valor <= 0){
					clas= "rojo"
			}
				return clas;
			}
		}
	}else{
		return "text-info";
	}
	}
	$scope.cargarVentas = function(page) {
		ventasService.findVentas($scope.url,page).then(
			function(data) {
				$scope.ventas = data;
				for(var i=0;i < $scope.ventas.length; i++){
					
					$scope.ventas[i].fechaString = new Date($scope.ventas[i].fecha);
//					$scope.ventas[i].fecha = new Date($scope.ventas[i].fecha.getTime() - (6*1000*3600));
					$scope.ventas[i].fechaString = getFechilla($scope.ventas[i].fechaString);
					
				}
				console.log(data);
				$scope.llenarPags();
			})
	}

	$scope.mailFactura=function(venta){
		ventasService.enviarFactura(venta).then(function(venta){
			alert("Factura enviada");
		})
	}
	
	$scope.mailFactura33=function(venta){
		ventasService.enviarFactura33(venta).then(function(venta){
			alert("Factura enviada");
		})
	}
	
	$scope.paraTimbrar= function(venta){
		$scope.ventaTimbrar= venta;
	}

	$scope.facturar = function(){
		var send=$scope.ventaTimbrar+"$$$"+$scope.usoCFDI;
		if($scope.ventaTimbrar.idCliente!=0){
			var r = confirm("¿Seguro que desea facturar?");
			if(r==true){
			ventasService.facturarVenta(send).then(
					function(data){
						console.log(data);
						if(data[0]=="0"){
							alert("Facturado con éxito");
						}else{
							alert(data[1]);
						}
						$window.location.reload();
					})
			}
		}else{
			alert('Esta Venta no tiene asociado un Cliente registrado');
		}
	}
	
	$scope.facturar33 = function(){
		var send={venta:$scope.ventaTimbrar,
				uso:$scope.usoCFDI
		};
		if($scope.ventaTimbrar.idCliente!=0){
			var r = confirm("¿Seguro que desea facturar?");
			if(r==true){
			ventasService.facturarVenta33(send).then(
					function(data){
						console.log(data);
						if(data[0]=="0"){
							alert("Facturado con éxito");
						}else{
							alert(data[1]);
						}
						$window.location.reload();
					})
			}
		}else{
			alert('Esta Venta no tiene asociado un Cliente registrado');
		}
	}
	
	$scope.cargarPagina=function(pag){
		if($scope.paginaActual!=pag){
			$scope.paginaActual=pag;
			$scope.cargarVentas(pag);
		}
	}
	
	ventasService.numPages().then(function(data){
		$scope.maxPage=data;
		$scope.llenarPags();
		
	});
	$('.datepicker').datepicker({format: 'mm-dd-yyyy'});
	
	$('.input-daterange').datepicker({
	    format: "mm-dd-yyyy"
	});
	
	
	
	$('.input-daterange input').each(function() {
	    $(this).datepicker("format","mm-dd-yyyy");
	});
	
	
	// busqueda por fechas
	$scope.buscar=function(){
		$scope.url
		ventasService.buscar($scope.fechaInicio,$scope.fechaFin).then(function(data){
			$scope.ventas= data;
			$scope.todos=false;
			
		});
	}
	
	$scope.cancelarFactura=function(venta){
		ventasService.cancelarFactura(venta.id).then(function(data){
			if(data[0]=="0"){
				alert("La factura se canceló con éxito");
			}else{
				alert(data[1]);
			}
			$window.location.reload();
		})
	}
	
	$scope.cancelarFactura33=function(venta){
		ventasService.cancelarFactura33(venta.id).then(function(data){
			if(data[0]=="0"){
				alert("La factura se canceló con éxito");
			}else{
				alert(data[1]);
			}
			$window.location.reload();
		})
	}

	$scope.cargarPagina(1);
	
	$scope.confirmarVenta=function(venta){
		$scope.ventaActual=venta;
		$scope.funcion= function(){
			$scope.ventaActual.estatus="VENDIDO";
			ventasService.updateVenta($scope.ventaActual).then(function(data){
				alert("Se actualizó la venta");
				$window.location.reload();
			});
		}
		$('#confirmacion').modal('show');
	}
	
	$scope.cancelarVenta=function(venta){
		$scope.ventaActual= venta;
		$scope.funcion=function(){
			ventasService.cancelarVenta($scope.ventaActual).then(function(data){
				alert("Se canceló la venta");
				$window.location.reload();
			});
		}
		$('#confirmacion').modal('show');
	}
	
	$scope.cambiarCliente=function(venta){
		$scope.ventaActual=venta;
		$('#cambioCliente').modal('show');
		$scope.cargaClientes();
	}
	
	$scope.cargaClientes=function(){
		clientesService.findClientesFull().then(function(data){
			$scope.clientes=data;
			console.log("Clientes",$scope.clientes )
		})
	}
	$scope.cargaClientes();
	$scope.actualizarVenta=function(){
		var cliente= $scope.clientes[$scope.indice];
		$scope.ventaActual.cliente= cliente.nombre;
		$scope.ventaActual.idCliente= cliente.id;
		ventasService.updateVenta($scope.ventaActual).then(function(data){
			alert("Se actualizó la venta");
			$window.location.reload();
		});
	}
}]);
