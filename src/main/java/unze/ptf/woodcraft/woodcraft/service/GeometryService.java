package unze.ptf.woodcraft.woodcraft.service;

import unze.ptf.woodcraft.woodcraft.model.Edge;
import unze.ptf.woodcraft.woodcraft.model.NodePoint;
import unze.ptf.woodcraft.woodcraft.model.ShapePolygon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeometryService {
    public CycleResult detectCycleForEdge(List<Edge> existingEdges, int startNodeId, int endNodeId) {
        Map<Integer, List<Integer>> adjacency = new HashMap<>();
        for (Edge edge : existingEdges) {
            adjacency.computeIfAbsent(edge.getStartNodeId(), key -> new ArrayList<>()).add(edge.getEndNodeId());
            adjacency.computeIfAbsent(edge.getEndNodeId(), key -> new ArrayList<>()).add(edge.getStartNodeId());
        }

        Map<Integer, Integer> previous = new HashMap<>();
        List<Integer> queue = new ArrayList<>();
        queue.add(startNodeId);
        previous.put(startNodeId, null);

        int index = 0;
        while (index < queue.size()) {
            int current = queue.get(index++);
            if (current == endNodeId) {
                break;
            }
            for (int neighbor : adjacency.getOrDefault(current, List.of())) {
                if (!previous.containsKey(neighbor)) {
                    previous.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        if (!previous.containsKey(endNodeId)) {
            return CycleResult.empty();
        }

        List<Integer> path = new ArrayList<>();
        Integer cursor = endNodeId;
        while (cursor != null) {
            path.add(0, cursor);
            cursor = previous.get(cursor);
        }

        if (path.isEmpty() || path.get(0) != startNodeId) {
            return CycleResult.empty();
        }

        List<Integer> cycleNodes = new ArrayList<>(path);
        return new CycleResult(true, normalizeCycle(cycleNodes));
    }

    public double computeAreaCm2(List<NodePoint> nodes) {
        if (nodes.size() < 3) {
            return 0;
        }
        double sum = 0;
        for (int i = 0; i < nodes.size(); i++) {
            NodePoint current = nodes.get(i);
            NodePoint next = nodes.get((i + 1) % nodes.size());
            sum += (current.getXCm() * next.getYCm()) - (next.getXCm() * current.getYCm());
        }
        return Math.abs(sum) / 2.0;
    }

    public double computePerimeterCm(List<NodePoint> nodes) {
        if (nodes.size() < 2) {
            return 0;
        }
        double perimeter = 0;
        for (int i = 0; i < nodes.size(); i++) {
            NodePoint current = nodes.get(i);
            NodePoint next = nodes.get((i + 1) % nodes.size());
            double dx = current.getXCm() - next.getXCm();
            double dy = current.getYCm() - next.getYCm();
            perimeter += Math.hypot(dx, dy);
        }
        return perimeter;
    }

    private List<Integer> normalizeCycle(List<Integer> path) {
        int size = path.size();
        List<Integer> forward = new ArrayList<>(path);
        List<Integer> backward = new ArrayList<>();
        for (int i = path.size() - 1; i >= 0; i--) {
            backward.add(path.get(i));
        }
        List<Integer> forwardKey = rotationKey(forward, size);
        List<Integer> backwardKey = rotationKey(backward, size);
        return compareCycles(forwardKey, backwardKey) <= 0 ? forwardKey : backwardKey;
    }

    private List<Integer> rotationKey(List<Integer> cycle, int size) {
        int minIndex = 0;
        for (int i = 1; i < size; i++) {
            if (cycle.get(i) < cycle.get(minIndex)) {
                minIndex = i;
            }
        }
        List<Integer> rotated = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int index = (minIndex + i) % size;
            rotated.add(cycle.get(index));
        }
        return List.copyOf(rotated);
    }

    private int compareCycles(List<Integer> left, List<Integer> right) {
        int size = left.size() < right.size() ? left.size() : right.size();
        for (int i = 0; i < size; i++) {
            int compare = Integer.compare(left.get(i), right.get(i));
            if (compare != 0) {
                return compare;
            }
        }
        return Integer.compare(left.size(), right.size());
    }

    public ShapePolygon buildShapeFromCycle(int documentId, Integer materialId, List<Integer> nodeIds,
                                            Map<Integer, NodePoint> nodeMap) {
        List<NodePoint> nodes = new ArrayList<>();
        for (int nodeId : nodeIds) {
            NodePoint node = nodeMap.get(nodeId);
            if (node != null) {
                nodes.add(node);
            }
        }
        double area = computeAreaCm2(nodes);
        double perimeter = computePerimeterCm(nodes);
        return new ShapePolygon(-1, documentId, materialId, 1, nodeIds, nodes, area, perimeter);
    }

    public record CycleResult(boolean cycleDetected, List<Integer> nodeIds) {
        public static CycleResult empty() {
            return new CycleResult(false, List.of());
        }
    }
}
