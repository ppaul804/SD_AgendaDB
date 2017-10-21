package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexaoMySQL {

    public static Connection conexao = null;
    //Dados do Banco
    private static final String USUARIO = "root";
    private static final String SENHA = "uniceub";
    private static final String URL = "jdbc:mysql://localhost:3306/agendadb?autoReconnect=true&useSSL=false";//url do banco
    private static final String DRIVER = "com.mysql.jdbc.Driver";

    public static Connection conectar() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USUARIO, SENHA);

        } catch (ClassNotFoundException | SQLException ex) {
            throw new RuntimeException("Erro na conexão: ", ex);
        }

    }

    public static void desconectar(Connection conexao) {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConexaoMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void desconectar(Connection conexao, PreparedStatement stmt) {
        desconectar(conexao);

        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConexaoMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void desconectar(Connection conexao, PreparedStatement stmt, ResultSet rs) {
        desconectar(conexao, stmt);

        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConexaoMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
