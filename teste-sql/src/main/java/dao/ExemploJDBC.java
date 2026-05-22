package dao;

import java.sql.*;
import java.util.Scanner;

public class ExemploJDBC {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/escola";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite a matrícula do aluno:");
        String matricula = scanner.nextLine();

        System.out.println("Digite o nome do aluno:");
        String nome = scanner.nextLine();

        try {
            Connection connection = estabelecerConexao();
            inserirAluno(connection, matricula, nome);
            listarAlunos(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection estabelecerConexao() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    public static void inserirAluno(Connection connection, String matricula, String nome) throws SQLException {
        String sql = "INSERT INTO alunos.alunos (matricula, nome) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, matricula);
            preparedStatement.setString(2, nome);
            preparedStatement.executeUpdate();
        }
    }

    public static void listarAlunos(Connection connection) throws SQLException {
        String sql = "SELECT matricula, nome FROM alunos.alunos";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String matricula = rs.getString("matricula");
                String nome = rs.getString("nome");
                System.out.println("Matrícula: " + matricula + ", Nome: " + nome);
            }
        }
    }
}