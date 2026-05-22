package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertAluno {

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/escola";
        String user = "root";
        String password = "admin";

        try {
            // Estabelece a conexão com o banco de dados.
            Connection connection = DriverManager.getConnection(jdbcUrl, user, password);

            // Cria um Statement.
            Statement stmt = connection.createStatement();

            // SQL para inserção.
            String sql = "INSERT INTO alunos.alunos (matricula, nome) VALUES ('1234567890', 'João da Silva')";

            // Executa a inserção.
            stmt.executeUpdate(sql);

            System.out.println("Aluno inserido com sucesso!");

            // Fecha o Statement e a Conexão.
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
