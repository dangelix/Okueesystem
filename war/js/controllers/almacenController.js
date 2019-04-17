app.service('almacenService', [
	'$http',
	'$q',
	'$location',
	'$rootScope',
	'$window',
	'proveedoresService',
	function($http, $q, $location,$rootScope,$window,proveedoresService) {

	
	this.getAlmacen = function() {
		var d = $q.defer();
		$http.get("/almacen/findAll").then(function(response) {
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
	this.altaAlmacen = function(send) {
		var d = $q.defer();
		$http.post("/almacen/add",send).then(function(response) {
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
	this.delAlmacen = function(id) {
		var d = $q.defer();
		$http.post("/almacen/delete/"+id).then(function(response) {
			d.resolve(response.data);
		}, function(response) {
			d.reject(response);
		});
		return d.promise;
	};
	
}])
app.controller("almacenController",[
	'$scope',
	'almacenService',
	'$routeParams',
	'$location',
	'$window',
	'proveedoresService',
	function($scope, almacenService, $routeParams, $location, $window, proveedoresService){
		
		$scope.load= function(){
			almacenService.getAlmacen().then(function(data) {
				console.log(data);
				$scope.almacen = data;
			})
		}
		$scope.load();
		$scope.registraAlmacen = function(send) {		
			almacenService.altaAlmacen(send).then(function(newTornillo) {
						alert("Almacen Agregado");
//						$window.location.reload();
						$location.path("/almacen");
					})
		}
		$scope.eliminar= function(a){
			if(confirm("¿Desea eliminar el almacen " + a.nombre +"?")){
				almacenService.delAlmacen(a.id).then(function(data){
				alert("Almacen eliminado");
				$window.location.reload();
			});
			}
		}

}]);