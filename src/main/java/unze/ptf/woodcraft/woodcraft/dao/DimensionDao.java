package unze.ptf.woodcraft.woodcraft.dao;

import unze.ptf.woodcraft.woodcraft.db.Database;
import unze.ptf.woodcraft.woodcraft.model.Dimension;
import unze.ptf.woodcraft.woodcraft.model.DimensionType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DimensionDao {
    public Dimension create(int documentId, double startXCm, double startYCm, double endXCm, double endYCm,
                            double offsetXCm, double offsetYCm, DimensionType type,
                            Integer startNodeId, Integer endNodeId) {
        String sql = """
                INSERT INTO dimensions(document_id, start_x_cm, start_y_cm, end_x_cm, end_y_cm,
                offset_x_cm, offset_y_cm, type, start_node_id, end_node_id)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, documentId);
            statement.setDouble(2, startXCm);
            statement.setDouble(3, startYCm);
            statement.setDouble(4, endXCm);
            statement.setDouble(5, endYCm);
            statement.setDouble(6, offsetXCm);
            statement.setDouble(7, offsetYCm);
            statement.setString(8, type.name());
            if (startNodeId == null) {
                statement.setNull(9, java.sql.Types.INTEGER);
            } else {
                statement.setInt(9, startNodeId);
            }
            if (endNodeId == null) {
                statement.setNull(10, java.sql.Types.INTEGER);
            } else {
                statement.setInt(10, endNodeId);
            }
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return new Dimension(keys.getInt(1), documentId, startXCm, startYCm, endXCm, endYCm,
                            offsetXCm, offsetYCm, type, startNodeId, endNodeId);
                }
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to create dimension", exception);
        }
        return new Dimension(-1, documentId, startXCm, startYCm, endXCm, endYCm,
                offsetXCm, offsetYCm, type, startNodeId, endNodeId);
    }

    public List<Dimension> findByDocument(int documentId) {
        String sql = """
                SELECT id, document_id, start_x_cm, start_y_cm, end_x_cm, end_y_cm,
                offset_x_cm, offset_y_cm, type, start_node_id, end_node_id
                FROM dimensions
                WHERE document_id = ?
                """;
        List<Dimension> dimensions = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, documentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    dimensions.add(new Dimension(
                            resultSet.getInt("id"),
                            resultSet.getInt("document_id"),
                            resultSet.getDouble("start_x_cm"),
                            resultSet.getDouble("start_y_cm"),
                            resultSet.getDouble("end_x_cm"),
                            resultSet.getDouble("end_y_cm"),
                            resultSet.getDouble("offset_x_cm"),
                            resultSet.getDouble("offset_y_cm"),
                            DimensionType.valueOf(resultSet.getString("type")),
                            (Integer) resultSet.getObject("start_node_id"),
                            (Integer) resultSet.getObject("end_node_id")
                    ));
                }
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to load dimensions", exception);
        }
        return dimensions;
    }

    public void updateOffset(int dimensionId, double offsetXCm, double offsetYCm) {
        String sql = "UPDATE dimensions SET offset_x_cm = ?, offset_y_cm = ? WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, offsetXCm);
            statement.setDouble(2, offsetYCm);
            statement.setInt(3, dimensionId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to update dimension offset", exception);
        }
    }

    public void updateEndpoints(int dimensionId, double startXCm, double startYCm, double endXCm, double endYCm) {
        String sql = "UPDATE dimensions SET start_x_cm = ?, start_y_cm = ?, end_x_cm = ?, end_y_cm = ? WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, startXCm);
            statement.setDouble(2, startYCm);
            statement.setDouble(3, endXCm);
            statement.setDouble(4, endYCm);
            statement.setInt(5, dimensionId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to update dimension endpoints", exception);
        }
    }

    public void deleteById(int dimensionId) {
        String sql = "DELETE FROM dimensions WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, dimensionId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to delete dimension", exception);
        }
    }

    public void insertWithId(Dimension dimension) {
        String sql = """
                INSERT INTO dimensions(id, document_id, start_x_cm, start_y_cm, end_x_cm, end_y_cm,
                offset_x_cm, offset_y_cm, type, start_node_id, end_node_id)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, dimension.getId());
            statement.setInt(2, dimension.getDocumentId());
            statement.setDouble(3, dimension.getStartXCm());
            statement.setDouble(4, dimension.getStartYCm());
            statement.setDouble(5, dimension.getEndXCm());
            statement.setDouble(6, dimension.getEndYCm());
            statement.setDouble(7, dimension.getOffsetXCm());
            statement.setDouble(8, dimension.getOffsetYCm());
            statement.setString(9, dimension.getType().name());
            if (dimension.getStartNodeId() == null) {
                statement.setNull(10, java.sql.Types.INTEGER);
            } else {
                statement.setInt(10, dimension.getStartNodeId());
            }
            if (dimension.getEndNodeId() == null) {
                statement.setNull(11, java.sql.Types.INTEGER);
            } else {
                statement.setInt(11, dimension.getEndNodeId());
            }
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to insert dimension with id", exception);
        }
    }

    public void deleteByDocument(int documentId) {
        String sql = "DELETE FROM dimensions WHERE document_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, documentId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to delete dimensions by document", exception);
        }
    }
}
