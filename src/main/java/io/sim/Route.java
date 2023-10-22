package io.sim;

public class Route {
    // adicionar construtor V
    // adicionar destrutor 
    // adicionar atributos privados de rotas -> edges
    private String edges; // limites da rota
    
    public Route (){
        this.edges = "empty_route";
    }

    public void RouteDestructor () {
        this.edges = "deleted_route"; 
    }

    public void setRoute (String route) {
        this.edges = route;
    }

    public String getRoute () {
        return edges;
    }
    
}