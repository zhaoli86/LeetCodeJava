package com.company;

import java.util.ArrayList;
import java.util.List;

public class SortItemsByGroupsRespectingDependencies {
    public int[] sortItems(int n, int m, int[] group, List<List<Integer>> beforeItems) {
        List<Integer> res = new ArrayList<>();
        List<Integer>[] graph = new ArrayList[n + m];
        int[] indegree = new int[n + m];

        for (int i = 0; i < n + m; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int i = 0; i < group.length; i++) {
            if (group[i] == -1) {
                continue;
            }
            graph[n + group[i]].add(i);
            indegree[i]++;
        }

        for (int i = 0; i < beforeItems.size(); i++) {
            for (int item : beforeItems.get(i)) {
                int repBeforeItem = group[item] == -1 ? item : n + group[item];
                int repCurrentItem = group[i] == -1 ? i : n + group[i];

                if (repBeforeItem == repCurrentItem) {
                    graph[item].add(i);
                    indegree[i]++;
                }
                else {
                    graph[repBeforeItem].add(repCurrentItem);
                    indegree[repCurrentItem]++;
                }
            }
        }

        for (int i = 0; i < n + m; i++) {
            if (indegree[i] == 0) {
                dfs(graph, indegree, i, n, res);
            }
        }

        return (res.size() == n) ? res.stream().mapToInt(i -> i).toArray() : new int[]{};
    }

    private void dfs(List<Integer>[] graph, int[] indegree, int cur, int n, List<Integer> res) {
        if (cur < n) {
            res.add(cur);
        }
        indegree[cur]--;

        for (int child : graph[cur]) {
            if (--indegree[child] == 0) {
                dfs(graph, indegree, child, n, res);
            }
        }
    }
}
