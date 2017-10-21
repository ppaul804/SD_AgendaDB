package model.dao;

import conexao.ConexaoMySQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.bean.Agenda;

/**
 * Classe que 1 - Armazena/Atualiza um Registro; 2 - Remove um Registro; 3 -
 * Recupera um Registro; e, 4 - Finaliza a Aplicação.
 *
 * @author Pedro Paul
 */
public class AgendaDAO {

    public void insere(Agenda agenda) {

        Connection conexao = ConexaoMySQL.conectar();
        PreparedStatement stmt = null;

        try {
            stmt = conexao.prepareStatement("INSERT INTO agenda VALUES (?, ?);");
            stmt.setString(1, agenda.getNome());
            stmt.setString(2, agenda.getTelefone());

            stmt.executeUpdate();

            System.out.println("inserido!");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao inserir: " + ex);
        } finally {
            ConexaoMySQL.desconectar(conexao, stmt);
        }

    }//fim metodo insere

    public void atualiza(Agenda agenda) {
        Connection conexao = ConexaoMySQL.conectar();
        PreparedStatement stmt = null;

        try {
            stmt = conexao.prepareStatement("UPDATE agenda SET telefone=? WHERE nome=?;");
            stmt.setString(1, agenda.getTelefone());
            stmt.setString(2, agenda.getNome());

            stmt.executeUpdate();

            System.out.println("atualizado!");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar: " + ex);
        } finally {
            ConexaoMySQL.desconectar(conexao, stmt);
        }
    }//fim metodo atualiza

    public void remove(Agenda agenda) {
        Connection conexao = ConexaoMySQL.conectar();
        PreparedStatement stmt = null;

        try {
            stmt = conexao.prepareStatement("DELETE FROM agenda WHERE nome=?");
            stmt.setString(1, agenda.getNome());

            stmt.executeUpdate();

            System.out.println("excluido!");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir: " + ex);
        } finally {
            ConexaoMySQL.desconectar(conexao, stmt);
        }
    }

    public List<Agenda> recupera(Agenda agenda) {
        Connection conexao = ConexaoMySQL.conectar();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Agenda> listaContatos = new ArrayList<>();

        try {
            stmt = conexao.prepareStatement("SELECT * FROM agenda WHERE nome=?;");
            stmt.setString(1, agenda.getNome());

            rs = stmt.executeQuery();
            while (rs.next()) {
                Agenda contato = new Agenda(
                        rs.getString("nome"),
                        rs.getString("telefone")
                );
                listaContatos.add(contato);
            }//fim while

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao recuperar: " + ex);
        } finally {
            ConexaoMySQL.desconectar(conexao, stmt, rs);
        }
        return listaContatos;
    }

    public List<Agenda> recuperaTodos(Agenda agenda) {
        Connection conexao = ConexaoMySQL.conectar();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Agenda> listaContatos = new ArrayList<>();

        try {
            stmt = conexao.prepareStatement("SELECT * FROM agenda");

            rs = stmt.executeQuery();
            while (rs.next()) {
                Agenda contato = new Agenda(
                        rs.getString("nome"),
                        rs.getString("telefone")
                );
                listaContatos.add(contato);
            }//fim while

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao recuperar: " + ex);
        } finally {
            ConexaoMySQL.desconectar(conexao, stmt, rs);
        }
        return listaContatos;
    }

}//fim da classe AgendaDAO
