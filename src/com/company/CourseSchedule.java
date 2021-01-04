package com.company;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class GNode {
    public Integer inDegrees = 0;
    public List<Integer> outNodes = new LinkedList<Integer>();
}


public class CourseSchedule {

    public boolean canFinish(int numCourses, int[][] prerequisites) {

        if (prerequisites.length == 0)
            return true; // no cycle could be formed in empty graph.

        // course -> list of next courses
        HashMap<Integer, GNode> graph = new HashMap<>();

        // build the graph first
        for (int[] relation : prerequisites) {
            // relation[1] -> relation[0]
            GNode prevCourse = this.getCreateGNode(graph, relation[1]);
            GNode nextCourse = this.getCreateGNode(graph, relation[0]);

            prevCourse.outNodes.add(relation[0]);
            nextCourse.inDegrees += 1;
        }

        // We start from courses that have no prerequisites.
        int totalDeps = prerequisites.length;
        LinkedList<Integer> nodepCourses = new LinkedList<Integer>();
        for (Map.Entry<Integer, GNode> entry : graph.entrySet()) {
            GNode node = entry.getValue();
            if (node.inDegrees == 0)
                nodepCourses.add(entry.getKey());
        }

        int removedEdges = 0;
        while (nodepCourses.size() > 0) {
            Integer course = nodepCourses.pop();

            for (Integer nextCourse : graph.get(course).outNodes) {
                GNode childNode = graph.get(nextCourse);
                childNode.inDegrees -= 1;
                removedEdges += 1;
                if (childNode.inDegrees == 0)
                    nodepCourses.add(nextCourse);
            }
        }

        if (removedEdges != totalDeps)
            // if there are still some edges left, then there exist some cycles
            // Due to the dead-lock (dependencies), we cannot remove the cyclic edges
            return false;
        else
            return true;
    }

    /**
     * Retrieve the existing <key, value> from graph, otherwise create a new one.
     */
    protected GNode getCreateGNode(HashMap<Integer, GNode> graph, Integer course) {
        GNode node = null;
        if (graph.containsKey(course)) {
            node = graph.get(course);
        } else {
            node = new GNode();
            graph.put(course, node);
        }
        return node;
    }
}


class Solution2 {
    public boolean canFinish(int numCourses, int[][] prerequisites) {

        // course -> list of next courses
        HashMap<Integer, List<Integer>> courseDict = new HashMap<>();

        // build the graph first
        for (int[] relation : prerequisites) {
            // relation[0] depends on relation[1]
            if (courseDict.containsKey(relation[1])) {
                courseDict.get(relation[1]).add(relation[0]);
            } else {
                List<Integer> nextCourses = new LinkedList<>();
                nextCourses.add(relation[0]);
                courseDict.put(relation[1], nextCourses);
            }
        }

        boolean[] path = new boolean[numCourses];

        for (int currCourse = 0; currCourse < numCourses; ++currCourse) {
            if (this.isCyclic(currCourse, courseDict, path)) {
                return false;
            }
        }

        return true;
    }


    /*
     * backtracking method to check that no cycle would be formed starting from currCourse
     */
    protected boolean isCyclic(
            Integer currCourse,
            HashMap<Integer, List<Integer>> courseDict,
            boolean[] path) {

        if (path[currCourse]) {
            // come across a previously visited node, i.e. detect the cycle
            return true;
        }

        // no following courses, no loop.
        if (!courseDict.containsKey(currCourse))
            return false;

        // before backtracking, mark the node in the path
        path[currCourse] = true;

        // backtracking
        boolean ret = false;
        for (Integer nextCourse : courseDict.get(currCourse)) {
            ret = this.isCyclic(nextCourse, courseDict, path);
            if (ret)
                break;
        }
        // after backtracking, remove the node from the path
        path[currCourse] = false;
        return ret;
    }
}