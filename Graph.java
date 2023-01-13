
package Incognito;

import java.util.ArrayList;

/**
 *
 * @author adenugad
 */
public class Graph {

    private final ArrayList<Vertex> vertices = new ArrayList<>();
    private final ArrayList<Edge> edges = new ArrayList<>();
    
    public Graph(){
        
    }
    
     public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }
    
    public Graph copy(){
        Graph graph = new Graph();
        graph.vertices.addAll(vertices);
        graph.edges.addAll(edges);
        
        return graph;
    }
    
    public int numEdges(){
        return edges.size();
    }
    
    public int numVertices(){
        return vertices.size();
    }
    
    public void addEdge(Vertex x, Vertex y){
        Edge edge = new Edge(x, y);
        edges.add(edge);
        x.addIncidentEdges(edge);
        //y.addIncidentEdges(edge);
    }
    
    public void addEdge(Edge edge){
        if(!edges.contains(edge))
        {
        edges.add(edge);
        }
    }
    
    public void addVertex(Vertex v){
        if(!vertices.contains(v))
        {
        vertices.add(v);
        }
    }
    
    public void removeEdge(Edge e){
        edges.remove(e);
        e.from.getIncidentEdges().remove(e);
        //e.to.getIncidentEdges().remove(e);   
    }
    
    public void removeVertex(Vertex v){
        vertices.remove(v);
        for(int i = 0; i < v.getNumOfIncidentEdges(); i++){
            removeEdge(v.getIncidentEdges().get(i));
        }
    }
    
    public Vertex getVertex(int index){
        return vertices.get(index);
    }
    
    public void printOut(){
        //System.out.println(vertices.size());
        for (Vertex vertex : vertices) {
            for (int j = 0; j < vertex.getNumOfIncidentEdges(); j++) {
                System.out.println(vertex.getData() + " -> "
                        + vertex.getIncidentEdges().get(j).getAdjacentVertex(vertex).getData());
            }
            System.out.println();
        }
    }
    
    /**
     * Does graph have vertex ?
     * @param v - vertex
     * @return vertex if it's in graph
     */
    public Vertex hasVertex(Vertex v){
        for (Vertex vertex : vertices) {
            if (vertex.equals(v)) {
                return vertex;
            }
        }
        return v;
    }
    
    /**
     * Does graph have vertex ?
     * @param v - vertex
     * @return 
     */
    public boolean hasVertex2(Vertex v){
        for (Vertex vertex : vertices) {
            if (vertex.equals(v)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * all nodes(vertices) in graph with no edge directed to them
     * @return list of these vertices
     */
    public ArrayList<Vertex> getRoots(){
        ArrayList<Vertex> queue = new ArrayList<>();
        for (Vertex vertex : vertices) {
            boolean issaRoot = false;
            for (Edge edge : edges) {
                if (vertex.equals(edge.to)) {
                    issaRoot = true;
                    break;
                }
            }
            if (!issaRoot) {
                queue.add(vertex);
            }

        }
    return queue;
    }
    
   
}   
 
