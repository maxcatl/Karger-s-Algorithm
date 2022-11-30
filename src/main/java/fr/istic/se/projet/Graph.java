package fr.istic.se.projet;

import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Graph
{
    /**
     * Class representing a vertex/node in the graph
     * It has a label
     */
    public static class Vertex
    {
        private String label;

        /**
         * Create a new Vertex with the string as label
         * @param label the label
         */
        public Vertex(String label)
        {
            this.label = label;
        }

        /**
         * Getter for the label attribute
         * @return the label
         */
        public String getLabel()
        {
            return label;
        }

        /**
         * Setter for the label attribute
         * @param newLabel the new label for the vertex
         */
        public void setLabel(String newLabel)
        {
            label = newLabel;
        }

        /**
         * Return a string describing the object, here the label attribute
         * @return the label
         */
        @Override
        public String toString()
        {
            return label;
        }

        /**
         * Override the equals method from Object Class
         * Two vertices are considered equals if they have the same label
         * @param otherVertex the other vertex to compare
         * @return true if the labels are the same, false otherwise
         */
        @Override
        public boolean equals(Object otherVertex)
        {
            if (otherVertex == this)
                return true;

            if (otherVertex == null)
                return false;

            if (!(otherVertex instanceof Vertex))
                return false;

            return this.label.equals(((Vertex) otherVertex).getLabel());
        }

        /**
         * Override the hashCode method, used in the hashmap and hashset to compare the vertices
         * The hashCode returned is the hashCode of the label.
         * @return the hashcode of the label as hashcode of the vertex
         */
        @Override
        public int hashCode()
        {
            return label.hashCode();
        }

    }

    /**
     * Exception class when a graph does not have enough vertices to execute an action (like merging)
     */
    public static class GraphTooSmallException extends Exception
    {
        /**
         * Constructor for the GraphTooSmallException.
         * @param message The detail message.
         */
        public GraphTooSmallException(String message)
        {
            super(message);
        }
    }


    private int numEdges = 0;
    //the adjacency map representing the different edges of the graph
    private HashMap<Vertex, List<Vertex>> adjMap = new HashMap<>();

    /**
     * Override the method toString to return a text description of the graph.
     * return all the edges as a --> b, one edge per line.
     * if the graph is empty, return a text saying it's empty
     * @return the text description of the graph
     */
    @Override
    public String toString()
    {
        if (adjMap.isEmpty())
            return "The graph is empty";
        StringBuilder res = new StringBuilder();
        for (Map.Entry<Vertex, List<Vertex>> entry : adjMap.entrySet())
        {
            for (Vertex v : entry.getValue())
                res.append(entry.getKey()).append(" --> ").append(v).append("\n");
            res.append("\n");
        }
        return res.toString();
    }


    /**
     * return the number of edges inside the graph
     * @return the numEdges attribute
     */
    public int getNumEdges()
    {
        computeNumEdges();
        return numEdges;
    }

    /**
     * Compute the number of edges inside the graph.
     * It updates the numEdges attribute and return the value
     * @return the number of edges in the graph
     */
    public int computeNumEdges()
    {
        int res = 0;
        for (Map.Entry<Vertex, List<Vertex>> entry : adjMap.entrySet())
        {
            res += entry.getValue().size();
        }
        numEdges = res;
        return res;
    }

    /**
     * Return the number of vertices in the graph
     * @return the number of vertices in the graph
     */
    public int getNumVertices()
    {
        return adjMap.keySet().size();
    }

    /**
     * Return all the vertices present in the graph
     * @return a set containing all the vertices in the graph
     */
    public Set<Vertex> getVerticesSet()
    {
        return Collections.unmodifiableSet(adjMap.keySet());
    }

    /**
     * return all the connected vertices to the given vertex
     * @param vertex the vertex at which the neighbours must be found
     * @return a list containing the connected vertices to the given vertex
     * @throws IllegalArgumentException if the given vertex is null or not in the graph
     */
    public List<Vertex> connectedVertices(Vertex vertex)
    {
        if (vertex == null)
            throw new IllegalArgumentException("The vertex must not be null");

        if (!contains(vertex))
            throw new IllegalArgumentException(String.format("the specified vertex is not in the graph (specified : %s)", vertex));

        return Collections.unmodifiableList(adjMap.get(vertex));
    }
    

    /**
     * Method to easily add several edges to the graph.
     * The edges must be passed as an array with the form<p>
     *  ["v1 -- v2", "v3 -- v2"]
     * <p>
     * This method does not throw an error if one or several vertices are not effectively added, but returns the number of edges added to the graph.
     * @param listEdges a string array with the edges to add
     * @throws IllegalArgumentException if the list is null
     * @return the number of edges added
     */
    public int addEdges(String[] listEdges) {
        if (listEdges == null)
            throw new IllegalArgumentException("The list must not be null");

        int added = 0;
        for (String edge : listEdges)
        {
            if (edge == null)
                continue;

            Matcher m = Pattern.compile("^\s*([^\s]+)\s?--\s?([^\s]+)\s*$").matcher(edge);
            if (m.find() && m.groupCount() == 2)
            {
                addEdge(m.group(1), m.group(2));
                added++;
            }
        }
        computeNumEdges();
        return added;
    }


    /**
     * Add an edge between the 2 given Vertices.
     * The vertices are created from the given strings which are their respective label
     * The graph is non directed, so 2 edges are created.
     * @param v1 The label of the first vertex
     * @param v2 The label of the second vertex
     */
    public void addEdge(String v1, String v2)
    {
        if (v1 == null || v2 == null)
            throw new IllegalArgumentException("the labels must not be null");

        if (v1.isBlank() || v2.isBlank())
            throw new IllegalArgumentException("the labels must not be blank");

        try
        {
            addEdge(new Vertex(v1), new Vertex(v2));
        }
        catch(IllegalArgumentException e)
        {
            Logger.getGlobal().severe(e.getMessage());
        }
    }

    /**
     * Add an edge between the 2 given Vertices.
     * The graph is non directed, so 2 edges are created.
     * If the edge already exists in the graph, nothing happen.
     * If a vertex is not yet in the graph, it is added.
     * If one of the vertices or both are null, a IllegalArgumentException is thrown.
     * @param vertex1 The first vertex
     * @param vertex2 The second vertex
     * @throws IllegalArgumentException if at least one of the vertices is null
     */
    public void addEdge(Vertex vertex1, Vertex vertex2)
    {
        if (vertex1 == null || vertex2 == null)
            throw new IllegalArgumentException("The vertices must not be null");

        //both ways because non-directed graph
        addEdgeAlgo(vertex1, vertex2);
        addEdgeAlgo(vertex2, vertex1);
    }

    /**
     * Private method used to add the two vertices in the graph, ie in the hashmap
     * @param v1 The first Vertex to add
     * @param v2 The second Vertex to add
     */
    private void addEdgeAlgo(Vertex v1, Vertex v2)
    {
        adjMap.putIfAbsent(v1, new ArrayList<>());
        adjMap.get(v1).add(v2);
    }

    /**
     * Remove the edge between the 2 vertices with the given label.
     * After the removal, if the vertices are not connected anymore to any other vertex, they are removed from the graph.
     * If at least one of the vertices is not present in the list, a IllegalArgumentException is thrown.
     * @param v1 the String og the first vertex
     * @param v2 the String of the second vertex
     * @throws IllegalArgumentException if at least one of the vertices is not in the graph
     */
    public void removeEdge(String v1, String v2)
    {
        removeEdge(new Vertex(v1), new Vertex(v2));
    }

    /**
     * Remove the edge between the 2 given vertices.
     * After the removal, if the vertices are not connected anymore to any other vertex, they are removed from the graph.
     * If at least one of the vertices is not present in the list, a IllegalArgumentException is thrown.
     * @param v1 the String og the first vertex
     * @param v2 the String of the second vertex
     * @throws IllegalArgumentException if at least one of the vertices is not in the graph
     */
    public void removeEdge(Vertex v1, Vertex v2)
    {
        if (v1 == null || v2 == null)
            throw new IllegalArgumentException("The vertices must not be null for the deletion.");

        if (!adjMap.containsKey(v1) || !adjMap.containsKey(v2))
            throw new IllegalArgumentException("The vertices are not in the graph.");

        adjMap.get(v1).remove(v2);
        adjMap.get(v2).remove(v1);
        computeNumEdges();

        //if a vertex is not connected to any other vertex, we remove it
        if(adjMap.get(v1).isEmpty())
            adjMap.remove(v1);
        if(adjMap.get(v2).isEmpty())
            adjMap.remove(v2);
    }

    /**
     * Returns true if the given Vertex is in the graph, false otherwise.
     * If a null vertex is given, returns false
     * @param v The Vertex to test
     * @return true if v is in the graph, false otherwise
     */
    public boolean contains(Vertex v)
    {
        if (v == null)
            return false;

        return adjMap.containsKey(v);
    }

    /**
     * Returns true if the two given vertices are connected by an edge.
     * If at least one of the vertices is null or not part of the graph, a IllegalArgumentException is thrown
     * @param v1 The first vertex to check
     * @param v2 The second vertex to check
     * @return true if the vertices are connected by en edge, false otherwise
     * @throws IllegalArgumentException if one of the vertices is null or not part of the graph
     */
    public boolean connected(Vertex v1, Vertex v2)
    {
        if (v1 == null || v2 == null)
            throw new IllegalArgumentException("vertices must not be null.");

        if (!contains(v1) || !contains(v2))
            throw new IllegalArgumentException("The vertices must be part of the graph.");

        return adjMap.get(v1).contains(v2);
    }

    /**
     * Merge the 2 given vertices in one
     * See the mergeVertices(Vertex, Vertex) for more information
     * @param v1 The First Vertex to merge
     * @param v2 The Second Vertex to merge
     * @throws GraphTooSmallException if there are not at least two vertices in the graph.
     * @throws IllegalArgumentException if at least one vertex is null or not part of the graph, or the vertices are not connected by an edge.
     */
    public void mergeVertices(String v1, String v2) throws GraphTooSmallException
    {
        mergeVertices(new Vertex(v1), new Vertex(v2));
    }

    /**
     * Merge the 2 given vertices in one.
     * The label of the vertices are concatenated in the final vertex with a slash separating them and parentheses wrapping them.
     * The Edges from and to the previous vertices are redirected to the resulting vertex.
     * The loops are deleted if they are created by the merge.
     * There must be at least two vertices in order to merge the vertices, else an Exception is thrown
     * If at least one of the vertices is null or not part of the graph, or the vertices are not part connected by an edge, a IllegalArgumentException is thrown.
     * @param v1 the first vertex to merge
     * @param v2 the second vertex to merge
     * @throws IllegalArgumentException if at least one vertex is null or not part of the graph, or the vertices are not connected by an edge.
     * @throws GraphTooSmallException if there are not at least two vertices in the graph.
     */
    public void mergeVertices(Vertex v1, Vertex v2) throws GraphTooSmallException
    {
        if (adjMap.keySet().size() <= 2)
            throw new GraphTooSmallException("There is not enough vertices in the graph.");

        if (v1 == null || v2 == null)
            throw new IllegalArgumentException("vertices must not be null.");

        if (!contains(v1) || !contains(v2))
            throw new IllegalArgumentException(String.format("The vertices must be part of the graph. Specified : %s and %s", v1, v2));

        if (!connected(v1, v2))
            throw new IllegalArgumentException("The vertices must be connected to be merged.");


        //Create the new vertex and the list with the connected vertices
        Vertex newVertex = new Vertex("(" + v1.getLabel() + "/" + v2.getLabel() + ")");
        List<Vertex> newList = (new ArrayList<>(adjMap.get(v1)));
        newList.addAll(adjMap.get(v2));


        //remove the loops
        ListIterator<Vertex> it = newList.listIterator();
        Vertex ve;
        while(it.hasNext())
        {
            ve = it.next();
            if (ve.equals(v1) || ve.equals(v2))
                it.remove();
        }

        //remove the old vertices
        adjMap.remove(v1);
        adjMap.remove(v2);

        //redirect the edges from the vertices to the new one
        for (Map.Entry<Vertex, List<Vertex>> entry : adjMap.entrySet())
        {
            it = entry.getValue().listIterator();
            while (it.hasNext())
            {
                ve = it.next();
                if (ve.equals(v1) || ve.equals(v2))
                {
                    it.remove();
                    it.add(newVertex);
                }
            }
        }

        //add the new vertex to the map
        adjMap.put(newVertex, newList);
    }


    /**
     * copy constructor
     * A copy is made of the hashmap from g
     * The graph h must not be null, or an exception is thrown
     * @param g the graph to copy
     * @throws IllegalArgumentException if the graph is null
     */
    public Graph(Graph g)
    {
        if (g == null)
            throw new IllegalArgumentException("The graph must not be null");

        adjMap = new HashMap<>();
        for (Map.Entry<Vertex, List<Vertex>> entry : g.adjMap.entrySet())
        {
            adjMap.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        computeNumEdges();
    }

    /**
     * default constructor
     */
    public Graph(){}


}