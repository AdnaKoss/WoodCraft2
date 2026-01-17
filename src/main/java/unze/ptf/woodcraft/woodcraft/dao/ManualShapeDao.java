package unze.ptf.woodcraft.woodcraft.dao;

import javafx.geometry.Point2D;
import unze.ptf.woodcraft.woodcraft.db.Database;
import unze.ptf.woodcraft.woodcraft.model.ManualShape;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManualShapeDao {
    public ManualShape create(ManualShape shape) {
        String sql = "INSERT INTO manual_shapes(document_id, points) VALUES (?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, shape.getDocumentId());
            statement.setString(2, serializePoints(shape.getPoints()));
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return new ManualShape(keys.getInt(1), shape.getDocumentId(), shape.getPoints());
                }
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to create manual shape", exception);
        }
        return shape;
    }

    public List<ManualShape> findByDocument(int documentId) {
        String sql = "SELECT id, document_id, points FROM manual_shapes WHERE document_id = ?";
        List<ManualShape> shapes = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, documentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    shapes.add(new ManualShape(
                            resultSet.getInt("id"),
                            resultSet.getInt("document_id"),
                            parsePoints(resultSet.getString("points"))
                    ));
                }
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to load manual shapes", exception);
        }
        return shapes;
    }

    public void updatePoints(int shapeId, List<Point2D> points) {
        String sql = "UPDATE manual_shapes SET points = ? WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, serializePoints(points));
            statement.setInt(2, shapeId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to update manual shape", exception);
        }
    }

    public void deleteById(int shapeId) {
        String sql = "DELETE FROM manual_shapes WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, shapeId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to delete manual shape", exception);
        }
    }

    public void deleteByDocument(int documentId) {
        String sql = "DELETE FROM manual_shapes WHERE document_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, documentId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to delete manual shapes", exception);
        }
    }

    public void insertWithId(ManualShape shape) {
        String sql = "INSERT INTO manual_shapes(id, document_id, points) VALUES (?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, shape.getId());
            statement.setInt(2, shape.getDocumentId());
            statement.setString(3, serializePoints(shape.getPoints()));
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to insert manual shape with id", exception);
        }
    }

    private String serializePoints(List<Point2D> points) {
        if (points == null || points.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < points.size(); i++) {
            Point2D point = points.get(i);
            builder.append(point.getX()).append(':').append(point.getY());
            if (i < points.size() - 1) {
                builder.append(',');
            }
        }
        return builder.toString();
    }

    private List<Point2D> parsePoints(String value) {
        List<Point2D> points = new ArrayList<>();
        if (value == null || value.isBlank()) {
            return points;
        }
        String[] parts = value.split(",");
        for (String part : parts) {
            String[] coords = part.trim().split(":");
            if (coords.length != 2) {
                continue;
            }
            try {
                double x = Double.parseDouble(coords[0]);
                double y = Double.parseDouble(coords[1]);
                points.add(new Point2D(x, y));
            } catch (NumberFormatException ignored) {
            }
        }
        return points;
    }
}
