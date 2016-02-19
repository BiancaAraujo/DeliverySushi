import java.sql.Connection;
import java.sql.SQLException;


public class Delivery {
	public static void main(String[] args) throws SQLException {		
		NimbusLookAndFeel.pegaNimbus();
		
		//Janela_Login_Usuario jalenaLog = new Janela_Login_Usuario();
		
		JanelaPrincipal janela = new JanelaPrincipal();
		Connection connection = new ConnectionFactory().getConnection();
		connection.close();
	}
}
