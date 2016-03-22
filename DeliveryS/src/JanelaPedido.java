import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class JanelaPedido extends JFrame implements ActionListener{
	
	private Container container;
	
	private int ganhos;
	
	// Fase 0 - Calcular o valor total do pedido
	private JTable itensE;
	private JTable itensP;
	private JTable itensS;
	private JTable itensB;
	float valorTotal;
	
	// Fase 1 - Inserir CPF do cliente e confirmar
	private	JTextField tfCpf;
	private JLabel lConfirmar;
	private JButton btConfirmar;
	
	// Fase 2 - Utilizar pontos de fidelidade	
	String pontosD = "0";
	private JButton btDescontar;
	
	// Fase 3 - Finalizar pedido
	private JButton btEfetuar;
	private	JTextField tfEntregador;
	
	// Fase 4 - Informar forma de pagamento
	String pagamento = new String();
	private JButton btConcluir;
	
	// Para conectar com o BD
	private ConnectionFactory con;
	private Connection c;
	ResultSet rs = null;	
	private void initConexao(){if(con == null){ con = new ConnectionFactory(); c = con.getConnection();}}
	
	public JanelaPedido(JTable itensEntrada, JTable itensPrincipal, JTable itensSobremesa, JTable itensBebida){	
		
		container = this.getContentPane();
		
		// Fase 0 - Calcular o valor total do pedido
		itensE = itensEntrada;
		itensP = itensPrincipal;
		itensS = itensSobremesa;
		itensB = itensBebida;
				
		// Fase 1 - Inserir CPF do cliente e confirmar
		lConfirmar = new JLabel("Confirmar pedido: Informar CPF");		
		container.add(lConfirmar);		
		
		tfCpf = new JTextField(12);
		container.add(tfCpf);
		
		btConfirmar = new JButton("Confirmar");	
		btConfirmar.addActionListener(this);
		container.add(btConfirmar);		
		
		// Fase 2 - Utilizar pontos de fidelidade
		btDescontar = new JButton("Utilizar pontos para desconto");
		btDescontar.addActionListener(this);
		
		// Fase 3 - Finalizar pedido
		btEfetuar = new JButton("Efetuar Pedido");
		btEfetuar.addActionListener(this);
		tfEntregador = new JTextField(20);
		
		// Fase 4 - Informar forma de pagamento
		btConcluir = new JButton("Concluir pedido");
		btConcluir.addActionListener(this);
				
		// Propriedades da janela
		this.setLayout(new FlowLayout()); 
		this.setVisible(true);
		this.setTitle("Cadastro de pagamento de despesa"); 
		this.setSize(800, 800);
	}


	@Override
	public void actionPerformed(ActionEvent e) {		
		if(e.getSource() == btConfirmar){
			if(tfCpf.getText() == ""){
				JOptionPane.showMessageDialog(this, "Entre com o CPF do cliente!");
			}
			else{				
				// Pega os pontos de finalidade do cliente (e já verifica se ele está cadastrado)
				int pontos = cadastrado();			
				
				if(pontos == -1){JOptionPane.showMessageDialog(this, "O cliente não está cadastrado!");}
				
				else{
					// FASE 0
					// Calcula o valor total que o cliente irá pagar no pedido
					valorTotal = 0;
					ganhos = 0;
					calculaValorTotal(itensE);
					calculaValorTotal(itensP);
					calculaValorTotal(itensS);
					calculaValorTotal(itensB);
						
					// FASE 2
					// Altera os botões na janela
					// Tira o "Confirmar", coloca o valor total e os pontos de fidelidade
					// E põe os botões para usar os pontos ou ir direto para a finalização
					container.remove(btConfirmar);
					container.add(new JLabel("Valor total do pedido: "+String.format("%.2f",valorTotal)));
					container.add(new JLabel("Pontos de fidelidade disponíveis: "+pontos));
					container.add(btEfetuar);
					container.add(btDescontar);
					container.revalidate();	

				}
			}
		}
		
		if(e.getSource() == btDescontar){
			// FASE 2
			// Pega os pontos que o cliente deseja utilizar
   	    	pontosD = JOptionPane.showInputDialog("Insira a quantidade de pontos de fidelidade a ser utilizada");
   	    	while(pontosD.equals("")){
   	    		JOptionPane.showMessageDialog(container, "Insira a quantidade de pontos de fidelidade a ser utilizada");
   				pontosD = JOptionPane.showInputDialog("Nome do evento");
   			}
   	    	
   	    	// Desconta os pontos
   	    	// FAZER: ajustar fórmula (essa é 1 pra 1)
   	    	float valor = valorTotal - Float.parseFloat(pontosD);
   	    	
//   	    	pontosFidelidade[0] = pontosFidelidade[0] - Integer.parseInt(pontosD);
//   	    	System.out.println("Teste... "+pontosFidelidade[0]);
   	    	
   	    	
   	    	// Remove "Efetuar", põe o valor total descontado, e põe o "Efetuar" de novo (pra ficar depois do novo valor total)
   	    	// FASE 3
   	    	container.remove(btEfetuar);
   	    	container.add(new JLabel("Novo valor total: "+String.format("%.2f",valor)));  
   	    	container.add(btEfetuar);
			container.revalidate();
		}
		
		if(e.getSource() == btEfetuar){
			// FASE 3
			
			// Montar lista com as opções de pagamento
			final JComboBox combo = new JComboBox(); 	
	        combo.addItem("Dinheiro");
	        combo.addItem("Cartão");
	        
	        this.getContentPane().add(combo);
	        this.setVisible(true);
		            
	        // Selecionar forma de pagamento na lista
	        ActionListener actionListener = new ActionListener() {
	        	public void actionPerformed(ActionEvent actionEvent) {
	        		pagamento = (String)combo.getSelectedItem();
	        	}
	        };	              
	        combo.addActionListener(actionListener);       
   	    	
	        // FASE 4
	        // Remover os dois botões e adicionar o "Concluir"
   	    	container.remove(btEfetuar);
   	    	container.remove(btDescontar);
   	    	container.add(tfEntregador); 	    	
   	    	container.add(btConcluir);
			container.revalidate();
		}
		
		if(e.getSource() == btConcluir){
			// FASE 4
			if(pagamento.equals("")){
				JOptionPane.showMessageDialog(container, "Escolha a forma de pagamento");
			}

			else if(tfEntregador.getText().equals("")){
				JOptionPane.showMessageDialog(container, "Entre com o nome do entregador!");
			}

			else{
				// FAZER a finalização
				/*
				 * Acrescentar na Fase 3 um JTextField para pôr o nome do entregador (esqueci de fazer isso)
				 * 												(não esquecer de conferir se foi preenchido)
				 * Guardar no BD em Pedido as informações desse pedido
				 * Guardar no BD em Lista_Item os itens comprados no pedido
				 * */
						initConexao();
						String insert = "INSERT INTO Pedido (valor_total, forma_pagamento, entregador, status, cpf, pontos_ganhos, pontos_gastos) VALUES (?, ?, ?, ?, ?, ?, ?)";
						String select = "SELECT Pedido.pedido_Id FROM Pedido WHERE cpf = ? and status = '0'";
						String update = "UPDATE Lista_Item SET pedido_Id=? WHERE pedido_Id IS NULL";
						ResultSet rs = null;
						String code;
						
						PreparedStatement ptStatement;
						PreparedStatement ptStatement2;
						PreparedStatement ptStatement3;
						
						System.out.println("Ganhos "+ganhos);
						System.out.println("Gastos "+Integer.parseInt(pontosD));
						
						try {
							ptStatement = c.prepareStatement(insert);
							ptStatement.setFloat(1, valorTotal);
							ptStatement.setString(2, pagamento);
							ptStatement.setString(3, tfEntregador.getText());
							ptStatement.setInt(4, 0); //0 = não pago/ 1= pago
							ptStatement.setString(5, tfCpf.getText());
							ptStatement.setInt(6, ganhos);
							ptStatement.setInt(7, Integer.parseInt(pontosD));

							ptStatement.executeUpdate();
							
							ptStatement2 = c.prepareStatement(select);
							ptStatement2.setString(1, tfCpf.getText());
							rs = ptStatement2.executeQuery();
						    rs.next();
						     
						     code = rs.getString("pedido_Id");
						     
						     //System.out.println(":D " + code);
						     
						    ptStatement3 = c.prepareStatement(update);
							ptStatement3.setString(1, code);
							//ptStatement3.setString(2, code);
							ptStatement3.executeUpdate();
							
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
				
				JOptionPane.showMessageDialog(this, "Pedido realizado com sucesso!");

			}
		}
	}

	// Essa função calcula preço total do pedido
	private void calculaValorTotal(JTable itens) {
		int linhas = itens.getRowCount(), i;
		Object quant;
		initConexao();
		ResultSet rs2 = null;
		String select = "SELECT preco, pontos_fidelidade_item FROM Item_Cardapio WHERE nome_item = ?";
		String insert = "INSERT into Lista_Item (nome_item, quantidade) VALUES (?, ?)";
		PreparedStatement ptStatement;
		
		try {
			ptStatement = c.prepareStatement(select);
			for(i=0;i<linhas;i++){
				quant = itens.getValueAt(i, 4);
				if(Integer.parseInt(quant.toString()) > 0){
					ptStatement.setString(1, itens.getValueAt(i, 0).toString());
					rs = ptStatement.executeQuery();
					rs.next();
					valorTotal = valorTotal + Integer.parseInt(quant.toString()) * Float.parseFloat(rs.getString("preco"));	
					ganhos = Integer.parseInt(rs.getString("pontos_fidelidade_item")) + ganhos;
				}				
			}	
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		PreparedStatement ptStatement2;
		
		try {
						
			ptStatement2 = c.prepareStatement(insert);
			
			for(i=0;i<linhas;i++){
				quant = itens.getValueAt(i, 4);
				if(Integer.parseInt(quant.toString()) > 0){
					
					System.out.println(itens.getValueAt(i, 0).toString());
					
					ptStatement2.setString(1, itens.getValueAt(i, 0).toString());
					//ptStatement2.setString(2, "0");
					ptStatement2.setInt(2, Integer.parseInt(quant.toString()));

					ptStatement2.executeUpdate();
					//rs2 = ptStatement2.executeQuery();
					//rs2.next();
					
					/*rs = ptStatement.executeQuery();
					rs.next();
					valorTotal = valorTotal + Integer.parseInt(quant.toString()) * Float.parseFloat(rs.getString("preco"));		*/		
				}	
			}		
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// Essa função retorna quantos pontos de fidelidade o cliente tem; e -1 se não estiver cadastrado
	private int cadastrado() {
		initConexao();
		String select = "SELECT pontos_fidelidade FROM Cliente WHERE cpf = ?";
		PreparedStatement ptStatement;
		
		try {
			ptStatement = c.prepareStatement(select);
			ptStatement.setString(1, tfCpf.getText());
			rs = ptStatement.executeQuery();
			rs.next();
			
			// Cliente não cadastrado
			if(rs.isAfterLast()){
				return -1;
			}
			return Integer.parseInt(rs.getString("pontos_fidelidade"));
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return -1;
	}

}
