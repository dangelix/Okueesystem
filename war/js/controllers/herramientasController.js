app.service('herramientasService', [
	'$http',
	'$q',
	'$location',
	'$rootScope',
	'$window',
	'proveedoresService',
	function($http, $q, $location,$rootScope,$window,proveedoresService) {
	
	this.registraHerramienta = function(newHerramienta) {
		var d = $q.defer();
		$http.post("/productos/add/", newHerramienta).then(
			function(response) {
				console.log(response);
				d.resolve(response.data);
			});
		return d.promise;
	}
	this.asignarProducto = function(send) {
		var d = $q.defer();
		$http.post("/clienteProducto/add/", send).then(
			function(response) {
				console.log(response);
				d.resolve(response.data);
			});
		return d.promise;
	}
	this.eliminarHerramienta = function(herramienta) {
		var d = $q.defer();
		$http.post("/productos/elimina/", herramienta).then(
			function(response) {
				console.log(response);
				d.resolve(response.data);
			});
		return d.promise;
	}
	
	this.findHerramientas = function(page) {
		var d = $q.defer();
		$http.get("/productos/pages/"+page).then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
	this.findHerramientasAll = function(page) {
		var d = $q.defer();
		$http.get("/productos/findAll/").then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
	
	this.findHerramienta = function(id) {
		var d = $q.defer();
		$http.get("/productos/find/"+id).then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
	this.getProductAsig = function(id) {
		var d = $q.defer();
		$http.get("/clienteProducto/getByCliente/"+id).then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
	this.findProveedores = function() {
		var d = $q.defer();
		$http.get("/proveedores/findAll/").then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
	this.numPages = function() {
		var d = $q.defer();
		$http.get("/productos/numPages").then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
	this.busqueda = function(buscar) {
		var d = $q.defer();
		$http.get("/productos/search/"+buscar).then(function(response) {
			console.log(response);
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
}])
app.controller("herramientasController",[
	'$scope',
	'herramientasService',
	'$routeParams',
	'$location',
	'$window',
	'proveedoresService',
	function($scope, herramientasService,$routeParams,$location,$window, proveedoresService){
	
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
	
	$scope.cargarPagina=function(pag){
		if($scope.paginaActual!=pag){
			$scope.herramientas(pag);
		}
	}
	herramientasService.numPages().then(function(data){
		$scope.maxPage=data;
		$scope.llenarPags();
		
	})
	$scope.fechaFormat = function(date){
		if(date == "SIN LOTES"){
			return "SIN LOTES"
		}else{
		var fecha = new Date(date);
		return fecha
		}
	}
	
	$scope.busqueda={};
	$scope.registraHerramienta = function(newHerramienta) {
		console.log(newHerramienta);		
		herramientasService.registraHerramienta(newHerramienta).then(function(newHerramienta) {
					alert("Herramienta Agregada");
					$window.location.reload();
					$location.path("/herramientas");
				})
	}
	$scope.herramientasload = function(page) {
		herramientasService.findHerramientas(page).then(
			function(data) {
				$scope.herramientas = data;
				$scope.llenarPags();
				console.log(data);
			})
	}
	
	$scope.editar = function(id) {
		$location.path("/herramientas/edit/" + id);
	}
	
	$scope.proveedores = function() {
		proveedoresService.findProveedores($routeParams.id).then(
			function(data) {
				$scope.proveedores = data;				
				console.log(data);
			})
	}
	$scope.proveedores();
	
	$scope.lotes = function(id) {			
		$location.path("/altaLotes/0/" + id);
	}
	
	$scope.buscar = function(buscar){
		
		herramientasService.busqueda(buscar).then(
				function(data) {
					$scope.herramientas = data;	
					$scope.busqueda.buscar="";
					console.log(data);
				})
	}
	$scope.mostrarHerramientas = function(){
		herramientasService.findHerramientas(1).then(
				function(data) {
					$scope.herramientas = data;
					$scope.paginaActual=1;
					$scope.llenarPags();
					console.log(data);
				})
	}
	$scope.cargaPagina=function(page){
		$scope.paginaActual=page;
		$scope.herramientasload(page);
	}
	$scope.cargaPagina(1);
	
	$scope.eliminar= function(herramienta){
			if(confirm("¿Desea eliminar el producto?")){
			herramientasService.eliminarHerramienta(herramienta).then(function(data){
				alert("Artículo eliminado");
				$window.location.reload();
			});
			}
	}
	
}]);
app.controller("herramientasEditController",[
	'$scope',
	'herramientasService',
	'$routeParams',
	'$location',
	'$window',
	'proveedoresService',
	function($scope, herramientasService,$routeParams,$location, $window, proveedoresService){
		herramientasService.findHerramienta($routeParams.id).then(function(data){
			$scope.newHerramienta=data;
			update_qrcode();
		})

		$scope.editaHerramienta = function(newHerramienta) {
			console.log(newHerramienta);		
			herramientasService.registraHerramienta(newHerramienta).then(function(newHerramineta) {
						alert("Herramienta Modificada");
						$window.location.reload();
						$location.path("/herramientas");
					})
		}
		$scope.proveedores = function() {
			proveedoresService.findProveedores($routeParams.id).then(
				function(data) {
					$scope.proveedores = data;				
					console.log(data);
				})
		}
		$scope.proveedores();
		
			
}]);
app.controller("productoController",[
	'$scope',
	'herramientasService',
	'$routeParams',
	'$location',
	'$window',
	'proveedoresService',
	'clientesService',
	function($scope, herramientasService,$routeParams,$location,$window, proveedoresService,clientesService){
	
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
	
	$scope.cargarPagina=function(pag){
		if($scope.paginaActual!=pag){
			$scope.herramientas(pag);
		}
	}
	herramientasService.numPages().then(function(data){
		$scope.maxPage=data;
		$scope.llenarPags();
		
	})
	$scope.getProducto = function(id){
		for(i in $scope.herramientas){
			if(id == $scope.herramientas[i].id){
				return $scope.herramientas[i].nombre;
			}
		}
	}
	$scope.busqueda={};
	$scope.registraHerramienta = function(newHerramienta) {
		console.log(newHerramienta);		
		herramientasService.registraHerramienta(newHerramienta).then(function(newHerramienta) {
					alert("Herramienta Agregada");
					$window.location.reload();
					$location.path("/herramientas");
				})
	}
	$scope.herramientasload = function(page) {
		herramientasService.findHerramientasAll().then(
			function(data) {
				$scope.herramientas = data;
				$scope.llenarPags();
				console.log(data);
			})
	}
	
	$scope.editar = function(id) {
		$location.path("/herramientas/edit/" + id);
	}
	
	$scope.proveedores = function() {
		proveedoresService.findProveedores($routeParams.id).then(
			function(data) {
				$scope.proveedores = data;				
				console.log(data);
			})
	}
	$scope.clientes = function() {
		clientesService.findClientesFull().then(
			function(data) {
				$scope.clientes = data;		
				$scope.sClientes = data;
				console.log(data);
			})
	}
	$scope.clientes();
	$scope.proveedores();
	
	$scope.lotes = function(id) {			
		$location.path("/altaLotes/0/" + id);
	}
	
	$scope.buscar = function(buscar){
		
		herramientasService.busqueda(buscar).then(
				function(data) {
					$scope.herramientas = data;	
					$scope.busqueda.buscar="";
					console.log(data);
				})
	}
	$scope.mostrarHerramientas = function(){
		herramientasService.findHerramientas(1).then(
				function(data) {
					$scope.herramientas = data;
					$scope.paginaActual=1;
					$scope.llenarPags();
					console.log(data);
				})
	}
	$scope.cargaPagina=function(page){
		$scope.paginaActual=page;
		$scope.herramientasload(page);
	}
	$scope.cargaPagina(1);
	
	$scope.eliminar= function(herramienta){
			if(confirm("¿Desea eliminar el producto?")){
			herramientasService.eliminarHerramienta(herramienta).then(function(data){
				alert("Artículo eliminado");
				$window.location.reload();
			});
			}
	}
	$scope.asigarProduct= function(send){
		herramientasService.asignarProducto(send).then(function(data){
			alert("Producto Asignado");
			$window.location.reload();
		});
	}
	$scope.buscaProduc = function(id){
		herramientasService.getProductAsig(id).then(function(data){
			$scope.PClient = data;
//			console.log("Prooductos asignados",data);
		});
	}
	
}]);
