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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Janela_Login_Usuario extends JFrame implements ActionListener{	
	
	static JanelaPrincipal janela_Principal;
	static Janela_Cadastro_Usuario janela_Cadastro_Usuario;
    private JTextField tfLogin;
    private JLabel lLogin;
    
    private JPasswordField tfSenha;
    private JLabel lSenha;
    
    private JButton btNovoCadastro;
    private JButton btEntrar;
    
    private Container container;

    private ConnectionFactory con;
    private Connection c;
    
    private void initConexao(){if(con == null){ con = new ConnectionFactory(); c = con.getConnection();}}

    public Janela_Login_Usuario()
    {
       
    
        tfLogin = new JTextField(25);
        tfSenha = new JPasswordField(25);
                
        //JLabel titulolbl = new JLabel("Forneça Login e senha");
        lLogin = new JLabel("Login: ");
        lSenha = new JLabel("Senha: ");
        
        btNovoCadastro = new JButton("Não possui cadastro?");
        btEntrar = new JButton("Entrar");
        
        container = this.getContentPane(); 
        //container.add(titulolbl);
       // titulolbl.setBounds(10, 5, 200, 20);
        container.add(lLogin);
        container.add(tfLogin);
        container.add(lSenha);
        container.add(tfSenha);
        container.add(btEntrar);
        container.add(btNovoCadastro);

        btNovoCadastro.addActionListener(this);
        btEntrar.addActionListener(this);
        
        this.setLayout(new FlowLayout());
        this.pack();
        this.setResizable(true);
        /*this.setExtendedState(JFrame.MAXIMIZED_BOTH); //ver todas as opções do JFrame*/        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setTitle("Tela de Login");
        this.setSize(300, 200);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub
        

    	if(e.getSource()==btEntrar){
    		//EVENTO QUE FAZ O CONTROLE DE LOGIN       
                    
    		if (tfLogin.getText().equals("") || tfSenha.getPassword().toString().equals(""))// se login e senha em branco
    			JOptionPane.showMessageDialog(null,"É obrigatório completar os campos login e senha!!!");//mensagem

    		else {
    			String senha = new String(tfSenha.getPassword());
    			initConexao();
    			try{
    				String sql = "select * from Usuario where login_usuario = ?";                                                             
    				PreparedStatement ps= c.prepareStatement(sql); 
    				ps.setString(1, tfLogin.getText());  
    				ResultSet rs;  
    				rs = ps.executeQuery(); 
    				rs.next();
    				rs.beforeFirst();
    				if(!rs.next()){
    					JOptionPane.showMessageDialog(null,"Usuário não cadastrado!");
    				}
    				else if (!rs.getString("senha_usuario").equals(senha)) {  
    					JOptionPane.showMessageDialog(null,"Senha incorreta!");  
    				}  
    				else{
    					JOptionPane.showMessageDialog(null,"Login Efetuado com Sucesso!");
    					JanelaNotificacoes jalenaNot = new JanelaNotificacoes(true);
    					dispose();
    				}
    			}

    			catch (SQLException erro)
    			{
    				erro.printStackTrace();
    			}
    	}
    }

    else if(e.getSource() == btNovoCadastro)
    {
    	janela_Cadastro_Usuario = new Janela_Cadastro_Usuario();
    }    

} 
  
}
