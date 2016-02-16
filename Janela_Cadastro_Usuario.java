import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Janela_Cadastro_Usuario extends JFrame implements ActionListener{
/*
Usuario: (cod_usuario, nome_usuario, email, login, senha)
*/ 
	private Container container;
	private JButton btCadastrar;
	private JButton btLimpar;

	private JTextField tfNome;
	private JTextField tfEmail;
	private JTextField tfLogin;
	private	JTextField tfSenha;
	
	private JLabel lNome;
	private JLabel lEmail;
	private JLabel lLogin;
	private JLabel lSenha;
	
	private ConnectionFactory con;
	private Connection c;

	
	private void initConexao(){if(con == null){ con = new ConnectionFactory(); c = con.getConnection();}}

	public Janela_Cadastro_Usuario() //throws SQLException
	{		
		btCadastrar = new JButton("Cadastrar");
		btLimpar = new JButton("Limpar formulário");
		
		tfNome = new JTextField(50);
		tfEmail = new JTextField(30);
		tfLogin = new JTextField(25);
		tfSenha = new JTextField(25);
		
		lNome = new JLabel("Nome Completo: ");
		lEmail = new JLabel("E-mail: ");
		lLogin = new JLabel("Login: ");
		lSenha = new JLabel("Senha: ");
				
		container = this.getContentPane();
		
		container.add(lNome);
		container.add(tfNome);
		container.add(lEmail);
		container.add(tfEmail);		
		container.add(lLogin);
		container.add(tfLogin); 		
		container.add(lSenha);
		container.add(tfSenha); 
		container.add(btCadastrar);
		container.add(btLimpar);
		                  
        btLimpar.addActionListener(this);
        btCadastrar.addActionListener(this);
		this.setLayout(new FlowLayout()); 
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);
		this.setTitle("Cadastro de Novo Usuário"); 
		this.setSize(800, 800);       
        
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btLimpar)
		{	        
	        limparUsuario();
		}
		
		else if (e.getSource()==btCadastrar)
		{
			if((tfNome.getText()).equals("")){
				JOptionPane.showMessageDialog(this, "Campo de nome precisa ser preenchido!");
			}
			else if((tfEmail.getText()).equals("")){
				JOptionPane.showMessageDialog(this, "Campo de e-mail precisa ser preenchido!");
			}
			else if((tfLogin.getText()).equals("")){
				JOptionPane.showMessageDialog(this, "Campo de login precisa ser preenchido!");
			}
			else if((tfSenha.getText()).equals("")){
				JOptionPane.showMessageDialog(this, "Campo de senha precisa ser preenchido!");
			}
			
			else{
			
				initConexao();
		        boolean status = false;	          
	
		        try {        		  
			        	Statement teste = c.createStatement();
			            String query = "INSERT INTO Usuario (nome_usuario, email_usuario, login_usuario, senha_usuario) VALUES (?,?,?,?)";
			            PreparedStatement pStatement = c.prepareStatement(query);			            
			            pStatement.setString(1, tfNome.getText());			            
			            pStatement.setString(2, tfEmail.getText());
			            pStatement.setString(3, tfLogin.getText());			            
			            pStatement.setString(4, tfSenha.getText());		            
			             
			            pStatement.executeUpdate();
			           
			            status = true;
			            JOptionPane.showMessageDialog(this, "Cadastro realizado com sucesso!"); 
			            
		        	} catch (SQLException ex) {
		        		System.out.println("ERRO: " + ex);
		        } 			
		
		         limparUsuario();
			}
		}	
		
	}
	
	public void limparUsuario()
	{
		tfNome.setText("");
		tfEmail.setText("");
		tfLogin.setText("");
		tfSenha.setText("");
	}

}
