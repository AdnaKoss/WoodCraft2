package unze.ptf.woodcraft.woodcraft.dao;

import unze.ptf.woodcraft.woodcraft.db.Database;
import unze.ptf.woodcraft.woodcraft.model.Edge;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EdgeDao {
    public Edge create(int documentId, int startNodeId, int endNodeId) {
        String sql = """
            INSERT INTO edges(document_id, start_node_id, end_node_id,
            control_start_x_cm, control_start_y_cm, control_end_x_cm, control_end_y_cm)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, documentId);
            statement.setInt(2, startNodeId);
            statement.setInt(3, endNodeId);
            statement.setNull(4, java.sql.Types.REAL);
            statement.setNull(5, java.sql.Types.REAL);
            statement.setNull(6, java.sql.Types.REAL);
            statement.setNull(7, java.sql.Types.REAL);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return new Edge(keys.getInt(1), documentId, startNodeId, endNodeId);
                }
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to create edge", exception);
        }
        return new Edge(-1, documentId, startNodeId, endNodeId);
    }

    public void deleteByNode(int nodeId) {
        String sql = "DELETE FROM edges WHERE start_node_id = ? OR end_node_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, nodeId);
            statement.setInt(2, nodeId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to delete edges", exception);
        }
    }

    public void deleteById(int edgeId) {
        String sql = "DELETE FROM edges WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, edgeId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to delete edge", exception);
        }
    }

    public void insertWithId(Edge edge) {
        String sql = """
            INSERT INTO edges(id, document_id, start_node_id, end_node_id,
            control_start_x_cm, control_start_y_cm, control_end_x_cm, control_end_y_cm)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, edge.getId());
            statement.setInt(2, edge.getDocumentId());
            statement.setInt(3, edge.getStartNodeId());
            statement.setInt(4, edge.getEndNodeId());
            setControl(statement, 5, edge.getControlStartXCm());
            setControl(statement, 6, edge.getControlStartYCm());
            setControl(statement, 7, edge.getControlEndXCm());
            setControl(statement, 8, edge.getControlEndYCm());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to insert edge with id", exception);
        }
    }

    public void deleteByDocument(int documentId) {
        String sql = "DELETE FROM edges WHERE document_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, documentId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to delete edges by document", exception);
        }
    }

    public void updateControls(int edgeId, Double startXCm, Double startYCm, Double endXCm, Double endYCm) {
        String sql = """
            UPDATE edges
            SET control_start_x_cm = ?, control_start_y_cm = ?,
                control_end_x_cm = ?, control_end_y_cm = ?
            WHERE id = ?
            """;
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            setControl(statement, 1, startXCm);
            setControl(statement, 2, startYCm);
            setControl(statement, 3, endXCm);
            setControl(statement, 4, endYCm);
            statement.setInt(5, edgeId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to update edge controls", exception);
        }
    }

    public List<Edge> findByDocument(int documentId) {
        String sql = """
            SELECT id, document_id, start_node_id, end_node_id,
                   control_start_x_cm, control_start_y_cm, control_end_x_cm, control_end_y_cm
            FROM edges
            WHERE document_id = ?
            """;
        List<Edge> edges = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, documentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    edges.add(new Edge(
                            resultSet.getInt("id"),
                            resultSet.getInt("document_id"),
                            resultSet.getInt("start_node_id"),
                            resultSet.getInt("end_node_id"),
                            (Double) resultSet.getObject("control_start_x_cm"),
                            (Double) resultSet.getObject("control_start_y_cm"),
                            (Double) resultSet.getObject("control_end_x_cm"),
                            (Double) resultSet.getObject("control_end_y_cm")
                    ));
                }
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to load edges", exception);
        }
        return edges;
    }

    private void setControl(PreparedStatement statement, int index, Double value) throws SQLException {
        if (value == null) {
            statement.setNull(index, java.sql.Types.REAL);
        } else {
            statement.setDouble(index, value);
        }
    }
}
