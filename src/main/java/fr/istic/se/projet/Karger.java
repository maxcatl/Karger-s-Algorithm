package fr.istic.se.projet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public class Karger
{
    private static final Random random = new Random();

    /**
     * Hide the constructor
     */
    private Karger()
    {}

    /**
     * method minCut without the multithreading parameters, to call it in sequential mode.
     * Call the minCut method with the multithreading parameter as false.
     * @param graph the graph in which to find the mincut
     * @return the mincut
     */
    public static int minCut(Graph graph, int numberOfIterations)
    {
        return minCut(graph, numberOfIterations, false);
    }

    /**
     * Method minCut with a multithreading parameter, find the mincut of the given graph
     * If the multithreading parameter is true, find the mincut in the multithreading mode, else do it in a sequential mode
     * @param graph the graph in which to find the mincut
     * @param multithreading if set to true, the search is in multithreading mode, else it is in sequential mode
     * @throws IllegalArgumentException if the graph is null or does not contain at least two vertices
     * @return the mincut of the graph
     */
    public static int minCut(Graph graph, int numberOfIteration, boolean multithreading)
    {
        if (graph == null)
            throw new IllegalArgumentException("The graph must not be null");

        if (graph.getNumVertices() < 2)
        {
            throw new IllegalArgumentException("the graph must contain at least to vertices in order to find the mincut");
        }

        if (multithreading)
            return computeMultithreading(graph, numberOfIteration);

        return compute(graph, numberOfIteration);

    }

    /**
     * Find the mincut of the given graph in the sequential mode
     * @param graph the graph in which to find the mincut
     * @param numberOfIteration the number of times the algorithm must iterate
     * @return the mincut
     */
    private static int compute(Graph graph, int numberOfIteration)
    {
        int result = -1;

        for (int i = 0; i < numberOfIteration; i++)
        {
            Graph workGraph = new Graph(graph);
            while (workGraph.getNumVertices() > 2)
            {
                Graph.Vertex source = (Graph.Vertex) workGraph.getVerticesSet().toArray()[random.nextInt(workGraph.getNumVertices())];
                Graph.Vertex destination = workGraph.connectedVertices(source).get(random.nextInt(workGraph.connectedVertices(source).size()));

                try
                {
                    workGraph.mergeVertices(source, destination);
                }
                catch (Graph.GraphTooSmallException e)
                {
                    Logger.getGlobal().severe("The graph was too small");
                    break;
                }
            }
            int resultRun = workGraph.getNumEdges() / 2;
            if (result == -1 || resultRun < result)
                result = resultRun;
        }
        return result;
    }


    /**
     * Find the mincut of the given graph in the multithreading mode
     * launch numberOfThreads threads to find the mincut, then takes the lowest of the returned results
     * @param graph the graph in which to find the mincut
     * @param numberOfThreads the number of threads which should find the mincut
     * @return the mincut
     */
    private static int computeMultithreading(Graph graph, int numberOfThreads)
    {
        List<Integer> results = Collections.synchronizedList(new ArrayList<>(numberOfThreads));
        for (int i=0 ; i<numberOfThreads ; i++)
            results.add(0);

        List<KargerThread> threads = Collections.synchronizedList(new ArrayList<>(numberOfThreads));
        for (int i=0 ; i<numberOfThreads ; i++)
        {
            KargerThread thread = new KargerThread(graph, i, results);
            threads.add(i, thread);
            thread.start();
        }

            for (int i = 0; i < numberOfThreads; i++)
            {
                try
                {
                    threads.get(i).join();
                }
                catch (InterruptedException e)
                {
                    Logger.getGlobal().severe(e.getMessage());
                    threads.get(i).interrupt();

                }
            }

        return Collections.min(results);
    }


    /**
     * Class for a thread which implements the karger algorithm
     */
    private static class KargerThread extends Thread
    {

        Graph graph;
        int position;
        List<Integer> results;

        /**
         * constructor for a thread
         * @param graph the graph in which to find the mincut
         * @param position a number attributed to the thread, so it knows where to put its result in the result array
         * @param results an array shared between the threads, so they can put their result
         */
        public KargerThread(Graph graph, int position, List<Integer> results)
        {
            this.graph = graph;
            this.position = position;
            this.results = results;
        }


        /**
         * method run launched when thread.start() is called
         * implements the karger algorithm
         * put its result inside the result array attribute at its given position
         */
        @Override
        public void run()
        {
            Random random = ThreadLocalRandom.current();
            Graph workGraph = new Graph(graph);
            while (workGraph.getNumVertices() > 2)
            {
                Graph.Vertex source = (Graph.Vertex) workGraph.getVerticesSet().toArray()[random.nextInt(workGraph.getNumVertices())];
                Graph.Vertex destination = workGraph.connectedVertices(source).get(random.nextInt(workGraph.connectedVertices(source).size()));

                try
                {
                    workGraph.mergeVertices(source, destination);
                }
                catch (Graph.GraphTooSmallException e)
                {
                    Logger.getGlobal().severe("The graph was too small");
                    break;
                }
            }
            results.set(position, workGraph.getNumEdges() / 2);
        }
    }
}
