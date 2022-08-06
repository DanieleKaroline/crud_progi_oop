package database;

import frms.Adm;
import frms.UserInterface;
import java.awt.Window;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class DBController {

    private Connection Conn;

    public boolean conectar() {
        try {
            String url = "jdbc:sqlite:database/loja_prog.db";
            this.Conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
        System.out.printf("Conectou!");
        return true;
    }

    public boolean desconectar() {
        try {
            if (!this.Conn.isClosed()) {
                this.Conn.close();
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
        System.out.printf("Desconectou!");
        return true;
    }

    public void cad_us(String cpf, String email, String senha, String nome, String endereco, int telefone) throws Exception {
        String sql_insert = "INSERT INTO usuario (cpf, email, senha, nome, endereco, telefone) VALUES (?, ?, ?, ?, ?, ?);";
        PreparedStatement stmt;
        try {
            stmt = this.Conn.prepareStatement(sql_insert);
            stmt.setString(1, cpf);
            stmt.setString(2, email);
            stmt.setString(3, senha);
            stmt.setString(4, nome);
            stmt.setString(5, endereco);
            stmt.setInt(6, telefone);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erro: " + e.getMessage());
        }
    }

    //busca usuario
    public ResultSet buscaUsuario(String nomes) throws Exception {
        String sel = "SELECT * FROM usuario WHERE nome LIKE'%" + nomes + "%'";
        ResultSet rset = null;
        try {
            Statement stmt = this.Conn.createStatement();
            rset = stmt.executeQuery(sel);
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar:  " + e.getMessage());
        }

        return rset;
    }

    //busca produto
    public ResultSet buscaProduto(String nome) throws Exception {
        String sel = "SELECT * FROM produto WHERE nome LIKE'%" + nome + "%'";
        ResultSet rset = null;

        try {
            Statement stmt = this.Conn.createStatement();
            rset = stmt.executeQuery(sel);
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar:  " + e.getMessage());
        }

        return rset;
    }

    //cadastra produto
    public void cad_prod(String nome, String marca, String genero, double valor, int estoque) throws Exception {

        String sql_insert = "INSERT INTO produto (nome, marca, genero, valor, qtd_estoque) VALUES (?,?,?,?,?);";
        PreparedStatement stmt;
        try {
            stmt = this.Conn.prepareStatement(sql_insert);

            stmt.setString(1, nome);
            stmt.setString(2, marca);
            stmt.setString(3, genero);
            stmt.setDouble(4, valor);
            stmt.setInt(5, estoque);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erro: " + e.getMessage());
        }
    }

    //cadastra produto no carrinho
    public void cad_cart(String cpf, int pd_id, int quantidade) throws Exception {
        String sql_insert = "INSERT INTO usuario_produto (us_cpf, pd_id, quantidade) VALUES (?, ?, ?);";
        PreparedStatement stmt;
        try {
            stmt = this.Conn.prepareStatement(sql_insert);
            stmt.setString(1, cpf);
            stmt.setInt(2, pd_id);
            stmt.setInt(3, quantidade);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "The product was uploaded to cart with success!");
        } catch (SQLException e) {
            throw new Exception("Erro: " + e.getMessage());
        }
    }

    public void updateQtd(int qtd,int id) throws Exception{
        String sql_update = "UPDATE produto SET qtd_estoque= qtd_estoque - ? WHERE id = ?;";

        PreparedStatement stmt;
        try {
            stmt = this.Conn.prepareStatement(sql_update);
            stmt.setInt(1, qtd);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erro: " + e.getMessage());
        }
    }
        //exibe tbl usuario
    public ResultSet carregaUsuario() throws Exception {
        String sel = "SELECT  cpf ,nome, email, telefone  FROM usuario;";
        ResultSet rset = null;

        try {
            Statement stmt = this.Conn.createStatement();
            rset = stmt.executeQuery(sel);
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar:  " + e.getMessage());
        }

        return rset;
    }


public ResultSet carregaCarrinho(String cpf) throws Exception {
        String sel = "SELECT id,nome,valor,usuario_produto.quantidade FROM produto INNER JOIN"
                + " usuario_produto ON usuario_produto.pd_id = produto.id = 1 AND us_cpf =" + cpf + ";";
        ResultSet rset = null;
        try {
            Statement stmt = this.Conn.createStatement();

            rset = stmt.executeQuery(sel);
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar:  " + e.getMessage());
        }
        return rset;
    }

    public ResultSet carregaProduto() throws Exception {
        String sel = "SELECT * FROM produto";
        ResultSet rset = null;

        try {
            Statement stmt = this.Conn.createStatement();
            rset = stmt.executeQuery(sel);
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar:  " + e.getMessage());
        }

        return rset;
    }

    //carrega usuario_produto
    public ResultSet carregaUsuarioProduto() throws Exception {
        String sel = "SELECT  us_cpf, pd_id, quantidade  FROM usuario_produto;";
        ResultSet rset = null;

        try {
            Statement stmt = this.Conn.createStatement();
            rset = stmt.executeQuery(sel);
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar:  " + e.getMessage());
        }

        return rset;
    }

    public boolean verifica_us(String email, String senha) throws Exception {
        String query = "SELECT * FROM usuario WHERE email = ? AND senha = ?;";
        ResultSet rset = null;
        boolean ret = false;
        PreparedStatement stmt;
        try {
            stmt = this.Conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, senha);

            rset = stmt.executeQuery();
            //primeiro ve se o cara n é adm
            if (email.equalsIgnoreCase("adm@email.com") && (senha.equals("123"))) {
                JOptionPane.showMessageDialog(null, "Login Admin");
                Adm frame = new Adm();
                frame.setVisible(true);
                ret = true;
            }
            //percorre tudo ate achar se tem igual
            while (rset.next()) {
                if (rset.getString("email").equals(email) && rset.getString("senha").equals(senha)) {
                    ret = true;
                    JOptionPane.showMessageDialog(null, "Usuário logado! ");
                    UserInterface frame = new UserInterface();
                    frame.setCpf(rset.getString("cpf"));
                    frame.setEmail(rset.getString("email"));
                    frame.setVisible(true);
                }

            }
            if (ret == false) {
                JOptionPane.showMessageDialog(null, "Usuario nao encontrado");
            }

        } catch (SQLException e) {
            throw new Exception("Erro: " + e.getMessage());
        }
        rset.close();
        stmt.close();

        return ret;
    }

    public ResultSet findsProduct(int id) throws SQLException, Exception {
        ResultSet product = null;
        String query = "SELECT * from produto WHERE id = ?;";
        ResultSet rset = null;
        PreparedStatement stmt;

        try {
            stmt = this.Conn.prepareStatement(query);
            stmt.setInt(1, id);
            rset = stmt.executeQuery();
            while (rset.next()) {
                if (rset.getInt("id") == id) {
                    JOptionPane.showMessageDialog(null, rset.getString("nome"));
                    product = rset;
                }
            }
        } catch (SQLException e) {
            throw new Exception("Something isn't right!");
        }
        rset.close();
        stmt.close();
        return product;
    }

    public void updateUserPhone(String email, int telefone) throws Exception {
        String sql_update = "UPDATE usuario SET telefone = ? WHERE email = ?;";

        PreparedStatement stmt;
        try {
            stmt = this.Conn.prepareStatement(sql_update);
            stmt.setInt(1, telefone);
            stmt.setString(2, email);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erro: " + e.getMessage());
        }
    }

    public void updateUserAdress(String email, String endereco) throws Exception {
        String sql_update = "UPDATE usuario SET endereco = ? WHERE email = ?;";

        PreparedStatement stmt;
        try {
            stmt = this.Conn.prepareStatement(sql_update);
            stmt.setString(1, endereco);
            stmt.setString(2, email);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erro: " + e.getMessage());
        }
    }

    public void updateUserPassword(String email, String senha) throws Exception {
        String sql_update = "UPDATE usuario SET senha = ? WHERE email = ?;";

        PreparedStatement stmt;
        try {
            stmt = this.Conn.prepareStatement(sql_update);
            stmt.setString(1, senha);
            stmt.setString(2, email);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erro: " + e.getMessage());
        }
    }
    
    public void deleteProduto(int id) throws Exception {
        String del_us_pd = "DELETE FROM usuario_produto WHERE pd_id = ?;" ;
        String del_pd = "DELETE FROM produto WHERE id = ?;";
        PreparedStatement stmt;
        try {
            stmt = this.Conn.prepareStatement(del_us_pd);
            stmt.setInt(1, id);
            stmt.executeUpdate();
             stmt = this.Conn.prepareStatement(del_pd);
             stmt.setInt(1, id);
            stmt.executeUpdate();     
        } catch (SQLException e) {
            throw new Exception("Erro: " + e.getMessage());
        }
    }
    public void deleteUsuario(String cpf) throws Exception {
        String del_us_pd = "DELETE FROM usuario_produto WHERE us_cpf = ?;" ;
        String del_us = "DELETE FROM usuario WHERE cpf = ?;";
        PreparedStatement stmt;
        try {
            stmt = this.Conn.prepareStatement(del_us_pd);
            stmt.setString(1, cpf);
            stmt.executeUpdate();
            stmt = this.Conn.prepareStatement(del_us);
            stmt.setString(1, cpf);
            stmt.executeUpdate();     
        } catch (SQLException e) {
            throw new Exception("Erro: " + e.getMessage());
        }
    }
    public void deleteCarrinho(String cpf) throws Exception {
       String del_c = "DELETE FROM usuario_produto WHERE us_cpf = ?;";
        ResultSet rset = null;

        PreparedStatement stmt;
        try {
            stmt = this.Conn.prepareStatement(del_c);
            stmt.setString(1, cpf);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar:  " + e.getMessage());
        }

    }
}
