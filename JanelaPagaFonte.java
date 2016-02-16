import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
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
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JanelaPagaFonte extends JFrame implements ActionListener{
	
	private Container container;
	private JButton btCadastrar;
	
	private	JTextField tfDataPagamento;
	private	JTextField tfValorAtual;
    
	private JLabel lNome;
	private JLabel lValorTotal;
	private	JLabel lDataVencimento;
	private	JLabel lDataPagamento;
	
	private ConnectionFactory con;
	private Connection c;
	
	String code;

	//Para a lista de categorias:
	ResultSet rs = null;
	ResultSet rs2 = null;
	
	private void initConexao(){if(con == null){ con = new ConnectionFactory(); c = con.getConnection();}}
	
	Verificacao formatos = new Verificacao();

	public JanelaPagaFonte(String item_cod) 
	{		        
		code = item_cod;
		initConexao();
		String select = "SELECT * FROM Fonte_despesa WHERE cod_fonte_d = ?";
		try {
			
			PreparedStatement ptStatement = c.prepareStatement(select);        
	        ptStatement.setString(1, item_cod);
			rs = ptStatement.executeQuery();
			rs.next();
			
			btCadastrar = new JButton("Registrar pagamento");	
			tfDataPagamento = new JTextField(10);			
			lNome = new JLabel("Nome do item: "+rs.getString("nome_fonte_d"));
			
			select = "select DATE_ADD(?,INTERVAL ? day)";       	 
            ptStatement = c.prepareStatement(select);
			ptStatement.setString(1, rs.getString("data_ultimo_vencimento_fonte_r"));
			ptStatement.setString(2, rs.getString("periodo_fonte_d"));
			rs2 = ptStatement.executeQuery();
			rs2.next();
			lDataVencimento = new JLabel(" Data do vencimento do item: "+rs2.getString(1));
			lDataPagamento = new JLabel(" Data do pagamento do item (AAAA/MM/DD):");
			
			container = this.getContentPane();
		    
			container.add(lNome);
			
			if(rs.getString("tipo_valor_fonte_d").equals("0")){ //Valor fixo
				lValorTotal = new JLabel(" Valor: R$"+rs.getString("valor_fonte_d"));
				container.add(lValorTotal);
			}			
			else if(rs.getString("tipo_valor_fonte_d").equals("1")){ //Valor variável
				lValorTotal = new JLabel(" Valor:");
				tfValorAtual = new JTextField(10);
				tfValorAtual.setText(rs.getString("valor_fonte_d"));
				container.add(lValorTotal);
				container.add(tfValorAtual);
			}		       		
			container.add(lDataVencimento);
			container.add(lDataPagamento);
			container.add(tfDataPagamento);
			container.add(btCadastrar);

			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
           
        btCadastrar.addActionListener(this);
		this.setLayout(new FlowLayout()); 
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);
		this.setTitle("Cadastro de pagamento de despesa"); 
		this.setSize(800, 800);       
        
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==btCadastrar)
		{
			if((tfDataPagamento.getText()).equals("")){
				JOptionPane.showMessageDialog(this, "Defina a data em que foi feito o pagamento!");
			} 
			else if(!formatos.isDate(tfDataPagamento.getText())){
				JOptionPane.showMessageDialog(this, "Coloque uma data válida para o pagamento!");
			}else
				try {
					if(rs.getString("tipo_valor_fonte_d").equals("1") && tfValorAtual.getText().equals("")){
							JOptionPane.showMessageDialog(this, "Defina o valor a ser pago!");
						}
						else if(rs.getString("tipo_valor_fonte_d").equals("1") && !formatos.isFloat(tfValorAtual.getText())){
							JOptionPane.showMessageDialog(this, "Defina um valor real e positivo para o item!");
						}
					
					else{
						initConexao();
						String select = "UPDATE Fonte_despesa SET data_ultimo_vencimento_fonte_r=? WHERE cod_fonte_d=?";
						String select2 = "INSERT INTO Fonte_item_despesa (valor_fonte_item_d, data_pagamento_fonte_item_d, data_vencimento_fonte_item_d, pago_fonte_item_d, cod_fonte_d) VALUES (?,?,?,?,?)";
			            						
						try {
							PreparedStatement ptStatement = c.prepareStatement(select);
							ptStatement.setString(1, rs2.getString(1));//data último vencimento
							ptStatement.setString(2, code);
							ptStatement.executeUpdate();
					    
							PreparedStatement pStatement = c.prepareStatement(select2);
							
							float i = 0;
							if(rs.getString("tipo_valor_fonte_d").equals("0")){ //Valor fixo
								i = Float.parseFloat(rs.getString("valor_fonte_d"));
							}			
							else if(rs.getString("tipo_valor_fonte_d").equals("1")){ //Valor variável
								i = Float.parseFloat(tfValorAtual.getText());
							}
							
				            pStatement.setDouble(1, i);
				            pStatement.setString(2, tfDataPagamento.getText());
				            pStatement.setString(3, rs2.getString(1));//data do vencimento
				            pStatement.setString(4, "1");
				            pStatement.setString(5, code);				             
				            pStatement.executeUpdate();
				            
							JOptionPane.showMessageDialog(this, "Pagamento registrado com sucesso!");
					    	} catch (SQLException ex) {
					    		System.out.println("ERRO: " + ex);
					    } 			
					}
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}
		
		
	}

}

