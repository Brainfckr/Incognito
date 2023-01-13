
package Incognito;

import java.util.ArrayList;

/**
 *
 * @author Dunni
 */
public class Vertex {
    private final String data; // This represents QI + " " + level, it should hold more attributes as it
    private ArrayList<Edge> incidentEdges = new ArrayList<>(); 
    /**
     * Specifies if the table associated with this vertex has been ascertained
     * to be k-anonymous
     */
    private boolean marked = false;
    
    public Vertex(String data2){
        data = data2;
    }
    
    public void addIncidentEdges(Edge edge){
        if(!incidentEdges.contains(edge)){
            incidentEdges.add(edge);
        }
    }
    
     public void removeIncidentEdges(Edge edge){
         incidentEdges.remove(edge);
    }
     
     public String getData(){
         return data;
     }
     
     public int getNumOfIncidentEdges(){
         return incidentEdges.size();
     }
     
     public ArrayList<Edge> getIncidentEdges(){
         return incidentEdges;
     }
     
     public boolean isMarked(){
         return marked;
     }
     
     public void setMark(boolean val){
         marked = val;
     }
     
     /**
      * checks if this vertex has no adjacent vertices
      * @return boolean
      */
     public boolean isRoot(){
        return incidentEdges.isEmpty();
     }
     
     
    @Override
     public boolean equals(Object vertex){
         Vertex v = (Vertex) vertex;
         return data.hashCode() == v.data.hashCode();
         //not only this though
         //incident edges should be the same
     }
     
     public Vertex copy(){
         Vertex vertex = new Vertex(data);
         for(int i = 0; i < incidentEdges.size(); i++){
             vertex.incidentEdges.add(new Edge (vertex, incidentEdges.get(i).getTo()));
         }
         return vertex;
     }
     
     public Vertex getAdjacentVertex(Edge edge){
         return edge.getAdjacentVertex(this);
     }
     
     /**
      * Addition of heights of every attribute represented in this vertex
      * @return integer value
      */
     public int getVertexHeight(){
         String[] arr = data.split(":");
         int height = 0;
         for (String s : arr) {
             height = height + Integer.parseInt(s.substring(s.indexOf(" ") + 1));
         }
         return height;
     }
     
     /**
      * Insert direct generalizations of node into queue, keeping queue ordered by height
      * @param generalizations - queue
      * @return list of vertices that this vertex directs towards
      */
     public ArrayList<Vertex> getDirectGeneralizations(ArrayList<Vertex> generalizations){
         for (Edge incidentEdge : incidentEdges) {
             Vertex v = incidentEdge.getTo();
             if (!generalizations.contains(v)) {
                 generalizations.add(incidentEdge.getTo());
             }
         }
         return generalizations;
     }
     
    @Override
     public String toString(){
         return this.getData().trim();
     }
}

