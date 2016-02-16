import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.management.Query;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class JanelaDespesaListaCategoria extends JFrame implements ActionListener{
	
	private Container container;
	private JButton btCategoria;
	private String categoria;
    
	private ConnectionFactory con;
	private Connection c;

	//Para a lista de categorias:
	ResultSet rs = null;
	DefaultListModel model = null;
	JList list = null;
	
	private void initConexao(){if(con == null){ con = new ConnectionFactory(); c = con.getConnection();}}

	public JanelaDespesaListaCategoria() 
	{
		setDefaultCloseOperation(HIDE_ON_CLOSE);
        setVisible(true);
		System.out.println("qqq");
		
		container = this.getContentPane();
		
		// Sessão da Categoria
		initConexao();
        boolean status = false;
        try {
        	Statement lista = c.createStatement();
        	String select = "SELECT * FROM Categoria_despesa";
            PreparedStatement ptStatement = c.prepareStatement(select);
            rs = ptStatement.executeQuery();
            
            model = new DefaultListModel(); //create a new list model
            
    	    while (rs.next()) //go through each row that your query returns
    	    {
    	        String itemCode = rs.getString("nome_categoriad"); //get the element in column "item_code"
    	        model.addElement(itemCode); //add each item to the model
    	    }
    	    list = new JList(model); 
    	    list.setModel(model);
    	    // Define a seleção única para a lista
    	    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
    	    status = true;
    	    
    	    btCategoria = new JButton("Selecionar categoria");
    	    btCategoria.addActionListener(
    	    	new ActionListener(){
    	        public void actionPerformed(ActionEvent e){
    	        	String valor = (String)list.getSelectedValue();
    	        	try {
						rs.first();
	    	    	    do{
	    	    	    	if(rs.getString("nome_categoriad").equals(valor)){
	    	    	    		categoria = rs.getString("cod_categoria_d");
	    	    	    	}
	    	    	    }while(rs.next());
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
    	        }
    	      }
    	    );
    	    container.add(new JScrollPane(list));  
    	    container.add(btCategoria); 
        } catch (SQLException ex) {
            System.out.println("ERRO: " + ex);
        } 
        
        /*btNovaCategoria = new JButton("Cadastrar nova categoria");
        btNovaCategoria.addActionListener(
        	new ActionListener(){
        	    public void actionPerformed(ActionEvent e){
        	     	JanelaDespesaCategoria n = new JanelaDespesaCategoria();	
        	    }
        	}
        );
        container.add(btNovaCategoria);*/
        // Fim da sessão de Categoria
        
		this.setLayout(new FlowLayout()); 
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);
		this.setTitle("Cadastro de Despesa"); 
		this.setSize(800, 800);   
		
		model.addElement("5"); //add each item to the model
    
    list = new JList(model); 
    list.setModel(model);
        
	}

	public String getCategoria() 
	{
		return categoria;
	}
	@Override
	public void actionPerformed(ActionEvent e) {}	

}
