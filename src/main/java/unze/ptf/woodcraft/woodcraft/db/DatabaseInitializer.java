package unze.ptf.woodcraft.woodcraft.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseInitializer {
    private DatabaseInitializer() {
    }

    public static void initialize() {
        try (Connection connection = Database.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    password_hash TEXT NOT NULL,
                    role TEXT NOT NULL
                )
                """);
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS documents (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    width_cm REAL NOT NULL DEFAULT 244,
                    height_cm REAL NOT NULL DEFAULT 122,
                    kerf_mm REAL NOT NULL DEFAULT 3,
                    unit_system TEXT NOT NULL DEFAULT 'CM',
                    FOREIGN KEY (user_id) REFERENCES users(id)
                )
                """);
            try {
                statement.executeUpdate("ALTER TABLE documents ADD COLUMN width_cm REAL NOT NULL DEFAULT 244");
            } catch (SQLException ignored) {
            }
            try {
                statement.executeUpdate("ALTER TABLE documents ADD COLUMN height_cm REAL NOT NULL DEFAULT 122");
            } catch (SQLException ignored) {
            }
            try {
                statement.executeUpdate("ALTER TABLE documents ADD COLUMN kerf_mm REAL NOT NULL DEFAULT 3");
            } catch (SQLException ignored) {
            }
            try {
                statement.executeUpdate("ALTER TABLE documents ADD COLUMN unit_system TEXT NOT NULL DEFAULT 'CM'");
            } catch (SQLException ignored) {
            }
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS materials (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    type TEXT NOT NULL,
                    sheet_width_cm REAL,
                    sheet_height_cm REAL,
                    sheet_price REAL,
                    price_per_square_meter REAL,
                    price_per_linear_meter REAL,
                    image_path TEXT,
                    grain_direction TEXT NOT NULL DEFAULT 'NONE',
                    edge_banding_cost_per_meter REAL NOT NULL DEFAULT 0,
                    FOREIGN KEY (user_id) REFERENCES users(id)
                )
                """);
            try {
                statement.executeUpdate("ALTER TABLE materials ADD COLUMN image_path TEXT");
            } catch (SQLException ignored) {
                // Column already exists or migration not needed.
            }
            try {
                statement.executeUpdate("ALTER TABLE materials ADD COLUMN grain_direction TEXT NOT NULL DEFAULT 'NONE'");
            } catch (SQLException ignored) {
            }
            try {
                statement.executeUpdate("ALTER TABLE materials ADD COLUMN edge_banding_cost_per_meter REAL NOT NULL DEFAULT 0");
            } catch (SQLException ignored) {
            }
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS nodes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    document_id INTEGER NOT NULL,
                    x_cm REAL NOT NULL,
                    y_cm REAL NOT NULL,
                    FOREIGN KEY (document_id) REFERENCES documents(id)
                )
                """);
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS edges (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    document_id INTEGER NOT NULL,
                    start_node_id INTEGER NOT NULL,
                    end_node_id INTEGER NOT NULL,
                    control_start_x_cm REAL,
                    control_start_y_cm REAL,
                    control_end_x_cm REAL,
                    control_end_y_cm REAL,
                    FOREIGN KEY (document_id) REFERENCES documents(id),
                    FOREIGN KEY (start_node_id) REFERENCES nodes(id),
                    FOREIGN KEY (end_node_id) REFERENCES nodes(id)
                )
                """);
            try {
                statement.executeUpdate("ALTER TABLE edges ADD COLUMN control_start_x_cm REAL");
            } catch (SQLException ignored) {
            }
            try {
                statement.executeUpdate("ALTER TABLE edges ADD COLUMN control_start_y_cm REAL");
            } catch (SQLException ignored) {
            }
            try {
                statement.executeUpdate("ALTER TABLE edges ADD COLUMN control_end_x_cm REAL");
            } catch (SQLException ignored) {
            }
            try {
                statement.executeUpdate("ALTER TABLE edges ADD COLUMN control_end_y_cm REAL");
            } catch (SQLException ignored) {
            }
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS guides (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    document_id INTEGER NOT NULL,
                    orientation TEXT NOT NULL,
                    position_cm REAL NOT NULL,
                    FOREIGN KEY (document_id) REFERENCES documents(id)
                )
                """);
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS shapes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    document_id INTEGER NOT NULL,
                    material_id INTEGER,
                    quantity INTEGER NOT NULL DEFAULT 1,
                    node_ids TEXT NOT NULL,
                    area_cm2 REAL NOT NULL,
                    perimeter_cm REAL NOT NULL,
                    FOREIGN KEY (document_id) REFERENCES documents(id),
                    FOREIGN KEY (material_id) REFERENCES materials(id)
                )
                """);
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS manual_shapes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    document_id INTEGER NOT NULL,
                    points TEXT NOT NULL,
                    FOREIGN KEY (document_id) REFERENCES documents(id)
                )
                """);
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS dimensions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    document_id INTEGER NOT NULL,
                    start_x_cm REAL NOT NULL,
                    start_y_cm REAL NOT NULL,
                    end_x_cm REAL NOT NULL,
                    end_y_cm REAL NOT NULL,
                    offset_x_cm REAL NOT NULL DEFAULT 0,
                    offset_y_cm REAL NOT NULL DEFAULT 0,
                    type TEXT NOT NULL DEFAULT 'ALIGNED',
                    start_node_id INTEGER,
                    end_node_id INTEGER,
                    FOREIGN KEY (document_id) REFERENCES documents(id)
                )
                """);
            try {
                statement.executeUpdate("ALTER TABLE dimensions ADD COLUMN start_node_id INTEGER");
            } catch (SQLException ignored) {
            }
            try {
                statement.executeUpdate("ALTER TABLE dimensions ADD COLUMN end_node_id INTEGER");
            } catch (SQLException ignored) {
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to initialize database", exception);
        }
    }
}
