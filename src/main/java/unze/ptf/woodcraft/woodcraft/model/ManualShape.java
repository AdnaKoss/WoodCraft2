package unze.ptf.woodcraft.woodcraft.model;

import javafx.geometry.Point2D;

import java.util.List;

public class ManualShape {
    private final int id;
    private final int documentId;
    private final List<Point2D> points;

    public ManualShape(int id, int documentId, List<Point2D> points) {
        this.id = id;
        this.documentId = documentId;
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public int getDocumentId() {
        return documentId;
    }

    public List<Point2D> getPoints() {
        return points;
    }
}
