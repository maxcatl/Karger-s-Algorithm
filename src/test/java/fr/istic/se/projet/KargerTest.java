package fr.istic.se.projet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.logging.Level;

import static java.util.logging.Logger.*;
import static org.junit.jupiter.api.Assertions.*;

class KargerTest {

    @Test
    @DisplayName("minCut graph1 in sequential and multithreading")
    void testGraph1()
    {
        try
        {
            getGlobal().setLevel(Level.SEVERE);
            Graph graph = new Graph();
            graph.addEdges(new String[]{"1 -- 2", "2 -- 3", "3 -- 4", "4 -- 1"});
            assertEquals(2, Karger.minCut(graph, 100));
            assertEquals(2, Karger.minCut(graph, 100, true));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail();
        }
    }

        @Test
        @DisplayName("minCut graph2 in sequential and multithreading")
        void testGraph2()
        {
            try
            {
                getGlobal().setLevel(Level.SEVERE);
                Graph graph = new Graph();
                graph.addEdges(new String[]{"1 -- 2", "2 -- 3", "3 -- 4", "4 -- 1"});
                assertEquals(2, Karger.minCut(graph, 100));
                assertEquals(2, Karger.minCut(graph, 100, true));
            }
            catch (Exception e)
            {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        @DisplayName("minCut graph3 in sequential and multithreading")
        void testGraph3()
        {
            try
            {
                getGlobal().setLevel(Level.SEVERE);
                Graph graph = new Graph();
                assertEquals(23, graph.addEdges(new String[]{"1--2", "1--3", "1--4", "1--5", "2--3", "2--4", "2--5", "3--4", "3--5", "4--5",  "6--7", "6--8", "6--9", "6--10", "7--8", "7--9", "7--10", "8--9", "8--10", "9--10", "5--10", "4--6", "3--7"}));
                assertEquals(3, Karger.minCut(graph, 100));
                assertEquals(3, Karger.minCut(graph, 100, true));
            }
            catch (Exception e)
            {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        @DisplayName("minCut graph4 in sequential and multithreading")
        void testGraph4()
        {
            try
            {
                getGlobal().setLevel(Level.SEVERE);
                Graph graph = new Graph();
                assertEquals(7, graph.addEdges(new String[]{"a -- b", "b--c", "c--d", "d--a", "a--c", "e--c", "e--b"}));
                assertEquals(2, Karger.minCut(graph, 100));
                assertEquals(2, Karger.minCut(graph, 100, true));
            }
            catch (Exception e)
            {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        @DisplayName("Test with a bigger scale graph")
        void testGraphScale()
        {
            for (int i=0 ; i<10 ; i++)
            {
                Graph graph = new Graph();
                Random random = new Random();
                final int MAX_VERTICES = 50;
                final int MAX_EDGES = 1000; //actually half as each edge is also made the other way around
                final int NUMBER_ITERATIONS = 500; // must be increased if sequential and multithreading doesn't give the same result

                //Create a graph with the specified amount of edges
                for (int j = 0; j < MAX_EDGES; j++)
                {

                    String v1 = Integer.toString(random.nextInt(MAX_VERTICES));
                    String v2;

                    //avoid the loops
                    do
                    {
                        v2 = Integer.toString(random.nextInt(MAX_VERTICES));
                    } while (v1.equals(v2));

                    graph.addEdge(v1, v2);

                }

                //cut in sequential and multithreading mode
                try
                {
                    int mincutSeq = Karger.minCut(graph, NUMBER_ITERATIONS);
                    int minCutMt = Karger.minCut(graph, NUMBER_ITERATIONS, true);
                    assertEquals(mincutSeq, minCutMt);
                    getGlobal().log(Level.INFO, "found mincut : {0}", mincutSeq);
                }
                catch(Exception E)
                {
                    fail();
                }
            }
        }

}