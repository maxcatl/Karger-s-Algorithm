package fr.istic.se.projet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    private Graph graph;
    private Graph.Vertex v1;
    private Graph.Vertex v2;


    @Test
    @BeforeEach
    void InitializeTests()
    {
        graph = new Graph();
        assertDoesNotThrow(() -> v1 = new Graph.Vertex("1"));
        assertDoesNotThrow(() -> v2 = new Graph.Vertex("2"));
    }


    @Test
    @DisplayName("Test Vertex display")
    void displayVertex()
    {
        Graph.Vertex vertex = new Graph.Vertex("");
        assertEquals("", vertex.getLabel());
        assertEquals(vertex.getLabel(), vertex.toString());
    }

    @Test
    @DisplayName("Test the setLabel method of the vertex class")
    void setLabelVertex()
    {
        Graph.Vertex vertex = new Graph.Vertex("1");
        assertEquals("1", vertex.getLabel());
        vertex.setLabel("2");
        assertEquals("2", vertex.getLabel());
    }

    @Test
    @DisplayName("Test for equality between vertices")
    void testEqualityVertices()
    {
        String s = "";
        Graph.Vertex v = new Graph.Vertex("1");
        assertEquals(v1, v);
        assertNotEquals(null, v1);
        //noinspection AssertBetweenInconvertibleTypes
        assertNotEquals(v1, s);

        assertEquals(new Graph.Vertex("(2/3)"), new Graph.Vertex("(2/3)"));
    }

    @Test
    @DisplayName("Test to add Vertices and Edges in the graph")
    void testAddEdges()
    {
        Logger.getGlobal().info("\nTest to add Vertices and Edges in the graph");
        graph.addEdge("1", "2");
        graph.addEdge(new Graph.Vertex("2"), new Graph.Vertex("3"));

        Logger.getGlobal().info(graph.toString());

        assertEquals(4, graph.getNumEdges());

        graph.addEdge("3", "2");
        Logger.getGlobal().info("" + graph);
        assertEquals(6, graph.getNumEdges());
    }

    @Test
    @DisplayName("Test to add edges at a big scale")
    void testAddEdgesBigScale()
    {
        Random random = new Random();

        int max = random.nextInt(50000);
        for (int i=0 ; i<max ; i++)
        {
            assertDoesNotThrow(() -> graph.addEdge(Integer.toString(random.nextInt()), Integer.toString(random.nextInt())));
        }
        assertEquals(max*2, graph.getNumEdges());
    }

    @Test
    @DisplayName("test to add illegal edges")
    void testAddIllegalEdges()
    {
        assertThrows(IllegalArgumentException.class, ()->graph.addEdge(null, "200"));
        assertThrows(IllegalArgumentException.class, ()->graph.addEdge("100", null));
        assertThrows(IllegalArgumentException.class, ()->graph.addEdge(" ", "200"));
        assertThrows(IllegalArgumentException.class, ()->graph.addEdge("100", ""));
        assertThrows(IllegalArgumentException.class, ()->graph.addEdge(new Graph.Vertex("100"), null));
        assertThrows(IllegalArgumentException.class, ()->graph.addEdge(null, new Graph.Vertex("200")));

    }

    @Test
    @DisplayName("Test to remove Edges between vertices in the graph")
    void testRemoveEdges()
    {
        Logger.getGlobal().info("\nTest to remove Edges between vertices in the graph");
        graph.addEdge("1", "2");
        graph.addEdge(new Graph.Vertex("2"), new Graph.Vertex("3"));

        Logger.getGlobal().info(graph.toString());

        graph.removeEdge("2", "3");
        Logger.getGlobal().info("\n remove(\"2\", \"3\")\n" + graph);
        assertEquals(2, graph.getNumEdges());

        graph.removeEdge(new Graph.Vertex("2"), new Graph.Vertex("1"));
        Logger.getGlobal().info("\n remove(\"2\", \"1\")\n" + graph);
        assertEquals(0, graph.getNumEdges());
    }

    @Test
    @DisplayName("Test to remove edges not part of the graph")
    void testRemoveIllegalEdges()
    {
        Logger.getGlobal().info("\nTest to remove edges not part of the graph");

        assertThrows(IllegalArgumentException.class, () -> graph.removeEdge("5", "6"));

        graph.addEdge("1", "2");
        Logger.getGlobal().info(graph.toString());

        assertThrows(IllegalArgumentException.class, () -> graph.removeEdge("2", "3"));
        Logger.getGlobal().info(graph.toString());
    }



    @Test
    @DisplayName("test the contains method")
    void testContains()
    {
        assertFalse(graph.contains(v1));

        graph.addEdge(v1, v2);

        assertTrue(graph.contains(v1));
        assertFalse(graph.contains(new Graph.Vertex("3")));
        assertFalse(graph.contains(null));
    }

    @Test
    @DisplayName("test the connected method")
    void testConnected()
    {
        Graph.Vertex v3 = new Graph.Vertex("3");

        graph.addEdge(v1, v2);
        graph.addEdge(v1, v3);

        assertTrue(graph.connected(v1, v2));
        assertFalse(graph.connected(v2, v3));
        Graph.Vertex v4 = new Graph.Vertex("4");
        assertThrows(IllegalArgumentException.class, () -> graph.connected(v1, v4));
    }

    @Test
    @DisplayName("test the merge method")
    void testMerge()
    {
        graph.addEdge("0", "1");
        graph.addEdge("0", "3");
        graph.addEdge("0", "2");
        graph.addEdge("1", "3");
        graph.addEdge("3", "2");

//        assertThrows(IllegalArgumentException.class, () -> graph.mergeVertices("2", "3"));


        try {
            assertDoesNotThrow(() -> graph.mergeVertices("0", "1"));
            assertEquals(8, graph.getNumEdges());

            assertDoesNotThrow(() -> graph.mergeVertices("(0/1)", "3"));
            assertEquals(4, graph.getNumEdges());
            Logger.getGlobal().info("" + graph);

        }catch(Exception e) {
            Logger.getGlobal().severe(e.getMessage());
            fail();
        }

        assertThrows(Exception.class, () -> graph.mergeVertices("((0/1)/3)", "2"));
    }

    /**
     * IL FAUT QUE CA THROW SA MERE
     */
    @Test
    @DisplayName("test the addEdges method")
    void testEasyAddEdges()
    {
        assertEquals(3, graph.addEdges(new String[]{"5 -- 6", "  6--7  ", "gopgtkpkrtg--tkgoprtkg"}));
        assertEquals(3*2, graph.getNumEdges());

        assertEquals(0, graph.addEdges(new String[]{"fr  --  59", " foreifj foirejfoie rfi", "ghj 5 -- 9"}));
        assertEquals(3*2, graph.getNumEdges());

        graph.addEdges(new String[]{null});
    }


    @Test
    @DisplayName("test the getNumVertices method")
    void testGetNumVertices()
    {
        assertEquals(0, graph.getNumVertices());
        graph.addEdge(v1, v2);
        assertEquals(2, graph.getNumVertices());
        graph.addEdge("1", "3");
        assertEquals(3, graph.getNumVertices());
        graph.removeEdge("1", "2");
        assertEquals(2, graph.getNumVertices());

        assertThrows(IllegalArgumentException.class, () -> graph.removeEdge("1", "2"));
        assertEquals(2, graph.getNumVertices());
    }

    @Test
    @DisplayName("test the getVertices method")
    void testGetVertices()
    {
        assertEquals(Collections.emptySet(), graph.getVerticesSet());
//        Set<Graph.Vertex> vertexSet= Set.of(v1, v2);
        Set<Graph.Vertex> vertexSet = new HashSet<>(Arrays.asList(v1, v2));
        graph.addEdge(v1, v2);
        assertEquals(vertexSet, graph.getVerticesSet());

        vertexSet.add(new Graph.Vertex("3"));
        graph.addEdge("3", "1");
        assertEquals(vertexSet, graph.getVerticesSet());

        vertexSet.remove(v2);
        graph.removeEdge("2", "1");
        assertEquals(vertexSet, graph.getVerticesSet());

        assertThrows(IllegalArgumentException.class, () -> graph.removeEdge("2", "3"));
        assertEquals(vertexSet, graph.getVerticesSet());
    }

    @Test
    @DisplayName("test the connectedVertices method")
    void testConnectedVertices()
    {
        assertThrows(IllegalArgumentException.class, () -> graph.connectedVertices(v1));
        assertThrows(IllegalArgumentException.class, () -> graph.connectedVertices(null));

        List<Graph.Vertex> vertexList = new ArrayList<>(List.of(v1));
        graph.addEdge(v1, v2);
        assertEquals(vertexList, graph.connectedVertices(v2));

        vertexList.add(v2);
        vertexList.add(new Graph.Vertex("3"));
        vertexList.remove(v1);
        graph.addEdge("3", "1");
        assertEquals(vertexList, graph.connectedVertices(v1));

        vertexList.remove(v2);
        graph.removeEdge("1", "2");
        assertEquals(vertexList, graph.connectedVertices(v1));

        assertThrows(IllegalArgumentException.class, () -> graph.removeEdge("1", "2"));
        assertEquals(vertexList, graph.connectedVertices(v1));
    }



}