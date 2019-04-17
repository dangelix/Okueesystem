app.service('envaseService',function($q, $http, $location,$window,$rootScope){
	this.addEnvase = function(inf) {
		var d = $q.defer();
		$http.post("/envase/add/", inf).then(
			function(response) {
				d.resolve(response.data);
			});
		return d.promise;
	}
	this.devolverEnvase=function(id,tipo,cantidad){
		var d = $q.defer();
		$http.post("/clientes/envases/devolucion/"+id+"/"+tipo+"/"+cantidad).then(
				function(response) {
					console.log(response);
					d.resolve(response.data);
				},
				function(response) {
				});
		return d.promise;
	}
	this.getEnvase = function() {
		var d = $q.defer();
		$http.get("/envase/getAll/").then(
			function(response) {
				d.resolve(response.data);
			});
		return d.promise;
	}
	this.delEnvase=function(id){
		var d = $q.defer();
		$http.post("/envase/delete/" +id).then(
				function(response) {
					console.log(response);
					d.resolve(response.data);
				},
				function(response) {
				});
		return d.promise;
	}
});

app.controller('envaseController',['$scope','$rootScope','$routeParams','$cookieStore','envaseService','$window','$http','$location',function($scope,$rootScope,$routeParams,$cookieStore,envaseService,$window,$http,$location){
envaseService.getEnvase().then(function(data){
	$scope.envases = data;
})
	$scope.altaEnvase = function(e){
	if(!e.descripcion){alert("No se ha introducido el tipo"); return}; if(!e.clave){alert("No se ha introducido la clave"); return};
		envaseService.addEnvase(e).then(function(data) {
			alert("Se ha agregado el nuevo Envase");
			$window.location.reload();
		})
	}
$scope.delEnvace = function(id){
	envaseService.delEnvase(id).then(function(data){
		alert("Se ha eliminado el Envase");
		$window.location.reload();
	})
}
}]);