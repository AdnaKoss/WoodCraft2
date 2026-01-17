package unze.ptf.woodcraft.woodcraft.model;

public class Dimension {
    private final int id;
    private final int documentId;
    private final double startXCm;
    private final double startYCm;
    private final double endXCm;
    private final double endYCm;
    private final double offsetXCm;
    private final double offsetYCm;
    private final DimensionType type;
    private final Integer startNodeId;
    private final Integer endNodeId;

    public Dimension(int id, int documentId, double startXCm, double startYCm, double endXCm, double endYCm,
                     double offsetXCm, double offsetYCm, DimensionType type,
                     Integer startNodeId, Integer endNodeId) {
        this.id = id;
        this.documentId = documentId;
        this.startXCm = startXCm;
        this.startYCm = startYCm;
        this.endXCm = endXCm;
        this.endYCm = endYCm;
        this.offsetXCm = offsetXCm;
        this.offsetYCm = offsetYCm;
        this.type = type;
        this.startNodeId = startNodeId;
        this.endNodeId = endNodeId;
    }

    public int getId() {
        return id;
    }

    public int getDocumentId() {
        return documentId;
    }

    public double getStartXCm() {
        return startXCm;
    }

    public double getStartYCm() {
        return startYCm;
    }

    public double getEndXCm() {
        return endXCm;
    }

    public double getEndYCm() {
        return endYCm;
    }

    public double getOffsetXCm() {
        return offsetXCm;
    }

    public double getOffsetYCm() {
        return offsetYCm;
    }

    public DimensionType getType() {
        return type;
    }

    public Integer getStartNodeId() {
        return startNodeId;
    }

    public Integer getEndNodeId() {
        return endNodeId;
    }
}
