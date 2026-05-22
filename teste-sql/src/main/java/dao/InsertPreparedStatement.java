package dao;

import java.sql.*;

public class InsertPreparedStatement {

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/escola";
        String user = "root";
        String password = "admin";

        try {
            // Estabelece a conexão com o banco de dados.
            Connection connection = DriverManager.getConnection(jdbcUrl, user, password);

            // Insere um aluno usando PreparedStatement.
            String insertSql = "INSERT INTO alunos.alunos (matricula, nome) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setString(1, "1234509876");
            preparedStatement.setString(2, "José da Silva");
            preparedStatement.executeUpdate();
            preparedStatement.close();

            // Recupera todos os alunos.
            Statement stmt = connection.createStatement();
            String selectSql = "SELECT matricula, nome FROM alunos.alunos";
            ResultSet rs = stmt.executeQuery(selectSql);

            // Exibe os alunos.
            while (rs.next()) {
                String matricula = rs.getString("matricula");
                String nome = rs.getString("nome");
                System.out.println("Matrícula: " + matricula + ", Nome: " + nome);
            }

            // Fecha ResultSet, Statement e Conexão.
            rs.close();
            stmt.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
