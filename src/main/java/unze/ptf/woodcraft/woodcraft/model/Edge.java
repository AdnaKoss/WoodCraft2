package unze.ptf.woodcraft.woodcraft.model;

public class Edge {
    private final int id;
    private final int documentId;
    private final int startNodeId;
    private final int endNodeId;
    private final Double controlStartXCm;
    private final Double controlStartYCm;
    private final Double controlEndXCm;
    private final Double controlEndYCm;

    public Edge(int id, int documentId, int startNodeId, int endNodeId) {
        this(id, documentId, startNodeId, endNodeId, null, null, null, null);
    }

    public Edge(int id, int documentId, int startNodeId, int endNodeId,
                Double controlStartXCm, Double controlStartYCm,
                Double controlEndXCm, Double controlEndYCm) {
        this.id = id;
        this.documentId = documentId;
        this.startNodeId = startNodeId;
        this.endNodeId = endNodeId;
        this.controlStartXCm = controlStartXCm;
        this.controlStartYCm = controlStartYCm;
        this.controlEndXCm = controlEndXCm;
        this.controlEndYCm = controlEndYCm;
    }

    public int getId() {
        return id;
    }

    public int getDocumentId() {
        return documentId;
    }

    public int getStartNodeId() {
        return startNodeId;
    }

    public int getEndNodeId() {
        return endNodeId;
    }

    public Double getControlStartXCm() {
        return controlStartXCm;
    }

    public Double getControlStartYCm() {
        return controlStartYCm;
    }

    public Double getControlEndXCm() {
        return controlEndXCm;
    }

    public Double getControlEndYCm() {
        return controlEndYCm;
    }
}
