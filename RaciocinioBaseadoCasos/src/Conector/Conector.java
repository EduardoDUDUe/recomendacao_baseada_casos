package Conector;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class Conector {

    public static Connection conetabd() {

      

        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/rbc", "postgres", "123");

            // JOptionPane.showMessageDialog(null, "Conectado com sucesso, Boa sorte");
            return con;

        } catch (Exception error) {
            JOptionPane.showMessageDialog(null, "Erro na conexão com o Banco de dados");
        }
        return null;

    }

}
