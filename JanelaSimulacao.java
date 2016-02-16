import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.Rotation;

public class JanelaSimulacao extends JFrame implements ActionListener{
	
	private Container container;

	private ConnectionFactory con;
	private Connection c;
	
	float mediasG[] = null;//gastos
	float mediasR[] = null;//receita
	float mediasL[] = new float[12];//lucros
	
	float simG[] = null;//gastos
	float simR[] = null;//receita
	float simL[] = new float[12];//lucros
	ChartPanel chartPanelS;
	JFreeChart chartS;
	
	//Lista de eventos
	Vector<Object[]> linhas;
	DefaultTableModel model;	
	JTable eventos;
	JScrollPane painel;
	int tamLinhas;

	ResultSet rs = null;
	ResultSet rs2 = null;
	
	//Cadastro de eventos
	private JButton btCadastrarReceita;
	private JButton btCadastrarDespesa;
	
	//Alterar eventos
	private JButton btAlterarEvento;
	private JButton btExcluirEvento;

	private void initConexao(){if(con == null){ con = new ConnectionFactory(); c = con.getConnection();}}

	Verificacao formatos = new Verificacao();
    Graficos data = new Graficos();
    
	public JanelaSimulacao()
	{		
		container = this.getContentPane();
		
		//Gráfico do estado atual
		analiseTendencia();
		XYDataset dataset1 = createDataset();
		JFreeChart chart = data.createChart1(dataset1);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(500, 300));  
	    container.add(chartPanel);
	    
	    //Lista de eventos cadastrados
	    eventosCadastrados();
	    montaTabela();
	    painel = new JScrollPane(eventos);
	    painel.setPreferredSize(new Dimension(500, 300));  
		container.add(painel,BorderLayout.CENTER);
		
		XYDataset datasetS = createDatasetS();
		chartS = data.createChartS(datasetS);
		chartPanelS = new ChartPanel(chartS);
		
	    container.add(chartPanelS);
	    chartPanelS.setPreferredSize(new Dimension(500, 300)); 
	    //Cadastrar eventos
		btCadastrarReceita = new JButton("Cadastrar novo evento de receita");
		btCadastrarReceita.addActionListener(
	      	new ActionListener(){
	       	    public void actionPerformed(ActionEvent e){	        	    	
	       	    	String nome;
	       	    	String valor;
	       	    	String mes;
	       	    	nome = JOptionPane.showInputDialog("Nome do evento");
	       	    	while(nome.equals("")){
	       	    		JOptionPane.showMessageDialog(container, "Defina um nome para o evento!");
	       				nome = JOptionPane.showInputDialog("Nome do evento");
	       			}      	    
	       	    	valor = JOptionPane.showInputDialog("Valor da receita");
	       	    	while(valor.equals("")){
	       	    		JOptionPane.showMessageDialog(container, "Defina um valor para a receita!");
	       				valor = JOptionPane.showInputDialog("Valor da receita");
	       			}
	       	    	while(!formatos.isFloat(valor)){
	       	    		JOptionPane.showMessageDialog(container, "Entre com o valor em reais!");
	       	    		valor = JOptionPane.showInputDialog("Valor da receita");
	       			}
	       	    	mes = JOptionPane.showInputDialog("Mês do recebimento (número do mês)");
	       	    	while(mes.equals("")){
	       	    		JOptionPane.showMessageDialog(container, "Defina o mês do recebimento!");
	       				mes = JOptionPane.showInputDialog("Mês do recebimento (número do mês)");
	       			}
	       	    	while(!formatos.isInteger(mes)){
	       	    		JOptionPane.showMessageDialog(container, "Entre com o número inteiro que representa o mês!");
	       				mes = JOptionPane.showInputDialog("Mês do recebimento (número do mês)");
	       			}
	       	    	initConexao();
	      	        try {        		        	
		   	        	Statement teste = c.createStatement();
		   	            String query = "INSERT INTO Evento_simulador_receita (nome_sim_r,valor_sim_r,mes_sim_r) VALUES (?,?,?)";
		   	            PreparedStatement pStatement = c.prepareStatement(query);
		   	            pStatement.setString(1, nome);
		   	            pStatement.setString(2, valor);
		   	            pStatement.setString(3, mes);
		   	            pStatement.executeUpdate();
		   	            
		   	            String select = "SELECT LAST_INSERT_ID()";
		   	            PreparedStatement ptStatement = c.prepareStatement(select);
		   	            rs = ptStatement.executeQuery();
		   	            rs.next();
		   	            tamLinhas++;
		   	            linhas = new Vector<Object[]>();      
		   	            linhas.add(new Object[]{tamLinhas,nome,valor,mes,"Receita",rs.getString(1)}); 	           	
		   	            montaTabela(); 
		   	            painel = new JScrollPane(eventos);
		   	            simR[Integer.parseInt(mes)-1] = simR[Integer.parseInt(mes)-1] + Float.parseFloat(valor);
		   	            simL[Integer.parseInt(mes)-1] = simL[Integer.parseInt(mes)-1] + Float.parseFloat(valor);
		   	         XYDataset datasetSS = createDatasetS();
		   	            chartS = data.createChartS(datasetSS);
		   	            chartPanelS.setChart(chartS);
	      	        } catch (SQLException ex) {
	      	        	System.out.println("ERRO: " + ex);
	        	    }       	        
	        	}
	      	}
	    );
		container.add(btCadastrarReceita);
		
		btCadastrarDespesa = new JButton("Cadastrar novo evento de despesa");
		btCadastrarDespesa.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){	        	    	
					String nome;
					String valor;
					String mes;
					nome = JOptionPane.showInputDialog("Nome do evento");
					while(nome.equals("")){
						JOptionPane.showMessageDialog(container, "Defina um nome para o evento!");
						nome = JOptionPane.showInputDialog("Nome do evento");
	        		}      	    
	        	    valor = JOptionPane.showInputDialog("Valor da despesa");
	        	    while(valor.equals("")){
	        	    	JOptionPane.showMessageDialog(container, "Defina um valor para a receita!");
	        			valor = JOptionPane.showInputDialog("Valor da despesa");
	        		}
	        	    while(!formatos.isFloat(valor)){
	        	    	JOptionPane.showMessageDialog(container, "Entre com o valor em reais!");
	        	    	valor = JOptionPane.showInputDialog("Valor da despesa");
	        		}
	        	    mes = JOptionPane.showInputDialog("Mês do pagamento (número do mês)");
	        	    while(mes.equals("")){
	        	    	JOptionPane.showMessageDialog(container, "Defina o mês do pagamento!");
	        			mes = JOptionPane.showInputDialog("Mês do pagamento (número do mês)");
	        		}
	        	    while(!formatos.isInteger(mes)){
	        	    	JOptionPane.showMessageDialog(container, "Entre com o número inteiro que representa o mês!");
	        			mes = JOptionPane.showInputDialog("Mês do pagamento (número do mês)");
	        		}
	        	    initConexao();
	        	    try {        		        	
	        	    	Statement teste = c.createStatement();
	        	    	String query = "INSERT INTO Evento_simulador_despesa (nome_sim_d,valor_sim_d,mes_sim_d) VALUES (?,?,?)";
	        	    	PreparedStatement pStatement = c.prepareStatement(query);
	        	    	pStatement.setString(1, nome);
	        	    	pStatement.setString(2, valor);
	        	    	pStatement.setString(3, mes);
	        	    	pStatement.executeUpdate();
	        	    	
	        	    	String select2 = "SELECT LAST_INSERT_ID()";
	        	    	PreparedStatement ptStatement2 = c.prepareStatement(select2);
	        	    	rs2 = ptStatement2.executeQuery();
	        	    	rs2.next();
	        	    	tamLinhas++;
	        	    	linhas = new Vector<Object[]>();	        	    	
	        	    	linhas.add(new Object[]{tamLinhas,nome,valor,mes,"Despesa",rs2.getString(1)}); 	           	
	        	    	
	        	    	montaTabela(); 
	        	    	painel = new JScrollPane(eventos);

		   	            simG[Integer.parseInt(mes)-1] = simG[Integer.parseInt(mes)-1] - Float.parseFloat(valor);
		   	            simL[Integer.parseInt(mes)-1] = simL[Integer.parseInt(mes)-1] - Float.parseFloat(valor);
		   	         XYDataset dataset3 = createDatasetS();
		   	            chartS = data.createChart1(dataset3);
		   	            chartPanelS.setChart(chartS);
	        	    } catch (SQLException ex) {
	        	    	System.out.println("ERRO: " + ex);
	        	    }       	        
	        	}
			}
	     );
		container.add(btCadastrarDespesa);
		
		btAlterarEvento = new JButton("Alterar um evento cadastrado");
		btAlterarEvento.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){	
        		String num;
        		String nome;
        		String valor;
        		String mes;
        		
        		num = JOptionPane.showInputDialog("Número do evento");
        		while(num.equals("")){
        			JOptionPane.showMessageDialog(container, "Entre com o número do evento!");
        			num = JOptionPane.showInputDialog("Número do evento");
        		} 
        		while(!formatos.isInteger(num)){
        			JOptionPane.showMessageDialog(container, "Entre com o número inteiro que representa o evento!");
        			num = JOptionPane.showInputDialog("Número do evento");
        		}
        		int lin = Integer.parseInt(num)-1;
        		nome = JOptionPane.showInputDialog("Nome do evento",eventos.getValueAt(lin, 1));
        		while(nome.equals("")){
        			JOptionPane.showMessageDialog(container, "Defina um nome para o evento!");
        			nome = JOptionPane.showInputDialog("Nome do evento",eventos.getValueAt(lin, 1));
        		}      	    
        		float valorA = Float.parseFloat((String)eventos.getValueAt(lin, 2));
        		valor = JOptionPane.showInputDialog("Valor da receita",eventos.getValueAt(lin, 2));
        		while(valor.equals("")){
        			JOptionPane.showMessageDialog(container, "Defina um valor para a receita!");
        			valor = JOptionPane.showInputDialog("Valor da receita",eventos.getValueAt(lin, 2));
        		}
        		while(!formatos.isFloat(valor)){
        			JOptionPane.showMessageDialog(container, "Entre com o valor em reais!");
        			valor = JOptionPane.showInputDialog("Valor da receita",eventos.getValueAt(lin, 2));
        		}
        		int mesA = Integer.parseInt((String)eventos.getValueAt(lin, 3));
        		mes = JOptionPane.showInputDialog("Mês do recebimento (número do mês)",eventos.getValueAt(lin, 3));
        		while(mes.equals("")){
        			JOptionPane.showMessageDialog(container, "Defina o mês do recebimento!");
        			mes = JOptionPane.showInputDialog("Mês do recebimento (número do mês)",eventos.getValueAt(lin, 3));
        		}
        		while(!formatos.isInteger(mes)){
        			JOptionPane.showMessageDialog(container, "Entre com o número inteiro que representa o mês!");
        			mes = JOptionPane.showInputDialog("Mês do recebimento (número do mês)",eventos.getValueAt(lin, 3));
        		}
        		initConexao();
        		try {        		        	
        			Statement teste = c.createStatement();
        			String query=null;
        			if(eventos.getValueAt(lin, 4).equals("Receita")){
        				query = "UPDATE Evento_simulador_receita SET nome_sim_r=?, valor_sim_r=?, mes_sim_r=? WHERE cod_sim_r=?";
        			}        	 
        			else if(eventos.getValueAt(lin, 4).equals("Despesa")){
        				query = "UPDATE Evento_simulador_despesa SET nome_sim_d=?, valor_sim_d=?, mes_sim_d=? WHERE cod_sim_d=?";
        			}        	 
        			PreparedStatement pStatement = c.prepareStatement(query);
        			pStatement.setString(1, nome);
        			pStatement.setString(2, valor);
        			pStatement.setString(3, mes);
        			pStatement.setString(4, (String)eventos.getModel().getValueAt(lin,5));
        			pStatement.executeUpdate();
        			linhas.set(lin, new Object[]{lin+1,nome,valor,mes,eventos.getModel().getValueAt(lin, 4),eventos.getModel().getValueAt(lin, 5)});          		
        			model.setRowCount(0);
        	    	montaTabela(); 
        	    	painel = new JScrollPane(eventos);
        	    	if(eventos.getValueAt(lin, 4).equals("Receita")){
        	    		simR[mesA-1] = simR[mesA-1] - valorA;
        	    		simR[Integer.parseInt(mes)-1] = simR[Integer.parseInt(mes)-1] + Float.parseFloat(valor);
        	    		simL[mesA-1] = simL[mesA-1] - valorA;
    	   	            simL[Integer.parseInt(mes)-1] = simL[Integer.parseInt(mes)-1] + Float.parseFloat(valor);
        			}        	 
        			else if(eventos.getValueAt(lin, 4).equals("Despesa")){
        				simG[mesA-1] = simG[mesA-1] + valorA;
        				simG[Integer.parseInt(mes)-1] = simG[Integer.parseInt(mes)-1] - Float.parseFloat(valor);
        				simL[mesA-1] = simL[mesA-1] + valorA;
    	   	            simL[Integer.parseInt(mes)-1] = simL[Integer.parseInt(mes)-1] - Float.parseFloat(valor);
        			}
        	    	XYDataset dataset4 = createDatasetS();
        	    	 chartS = data.createChartS(dataset4);
  	   	            chartPanelS.setChart(chartS);	   	            
     	    	 }catch (SQLException ex) {
     	    		System.out.println("ERRO: " + ex);
     	    	 }    
        		}
		});
		container.add(btAlterarEvento);
		
		btExcluirEvento = new JButton("Excuir um evento cadastrado");
		btExcluirEvento.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){	
        		String num;
        		num = JOptionPane.showInputDialog("Número do evento a excluir");
        		while(num.equals("")){
        			JOptionPane.showMessageDialog(container, "Entre com o número do evento!");
        			num = JOptionPane.showInputDialog("Número do evento a exclui");
        		} 
        		while(!formatos.isInteger(num)){
        			JOptionPane.showMessageDialog(container, "Entre com o número inteiro que representa o evento!");
        			num = JOptionPane.showInputDialog("Número do evento a excluir");
        		}
        		//JOptionPane.showMessageDialog(container, "Você vai excluir o evento "+num+"!");
        		int lin = Integer.parseInt(num)-1;
        		float valorA = Float.parseFloat((String)eventos.getValueAt(lin, 2));
        		int mesA = Integer.parseInt((String)eventos.getValueAt(lin, 3));
        		String tipo = (String) eventos.getValueAt(lin, 4);
        		initConexao();
        		try {        		        	
        			Statement teste = c.createStatement();
        			String query=null;
        			if(eventos.getValueAt(lin, 4).equals("Receita")){
        				query = "DELETE FROM Evento_simulador_receita WHERE cod_sim_r=?";
        			}        	 
        			else if(eventos.getValueAt(lin, 4).equals("Despesa")){
        				query = "DELETE FROM Evento_simulador_despesa WHERE cod_sim_d=?";
        			}        	 
        			PreparedStatement pStatement = c.prepareStatement(query);
        			pStatement.setString(1, (String)eventos.getModel().getValueAt(lin,5));
        			pStatement.executeUpdate();
        			linhas.remove(lin);
        			for(int i=lin; i<linhas.size(); i++){
        				linhas.set(i, new Object[]{i+1,eventos.getModel().getValueAt(i+1, 1),eventos.getModel().getValueAt(i+1, 2),eventos.getModel().getValueAt(i+1, 3),eventos.getModel().getValueAt(i+1, 4),eventos.getModel().getValueAt(i+1, 5)});
        			}        			
        			model.setRowCount(0);
        	    	montaTabela(); 
        	    	painel = new JScrollPane(eventos);

        	    	if(tipo.equals("Receita")){
        	    		simR[mesA-1] = simR[mesA-1] - valorA;
        	    		simL[mesA-1] = simL[mesA-1] - valorA;
        			}        	 
        			else if(tipo.equals("Despesa")){
        				simG[mesA-1] = simG[mesA-1] + valorA;
        				simL[mesA-1] = simL[mesA-1] + valorA;
        			}
        	    	XYDataset dataset = createDatasetS();
        	    	 chartS = data.createChartS(dataset);
  	   	            chartPanelS.setChart(chartS);	
     	    	 }catch (SQLException ex) {
     	    		System.out.println("ERRO: " + ex);
     	    	 }    
        		}
		});
		container.add(btExcluirEvento);
		
		this.setLayout(new FlowLayout()); 
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);
		this.setTitle("Simulação de gastos e ganhos"); 
		this.setSize(800, 800);       
        
	}


	private XYDataset createDatasetS() {
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries series[] = new XYSeries[3];
		
		series[0] = new XYSeries("Ganho médio");
		series[1] = new XYSeries("Gasto médio");
		series[2] = new XYSeries("Lucro médio");
		for(int j=0; j<12; j++){
			series[0].add(j+1,simG[j]);
			series[1].add(j+1,simR[j]);
			series[2].add(j+1,simL[j]);
		}	
		dataset.addSeries(series[0]);
		dataset.addSeries(series[1]);
		dataset.addSeries(series[2]);

	    return dataset;		
	}
	

	private void eventosCadastrados() {
		// TODO Auto-generated method stub
		initConexao();		
        try {	            
	         String select = "SELECT * FROM Evento_simulador_receita";	
	         PreparedStatement ptStatement = c.prepareStatement(select);
	         rs = ptStatement.executeQuery();	
	         String select2 = "SELECT * FROM Evento_simulador_despesa";	
	         PreparedStatement ptStatement2 = c.prepareStatement(select2);
	         rs2 = ptStatement2.executeQuery();	          
	         Object[] colunas = {"Número","Evento", "Valor", "Mês", "Tipo","code"};  		
			 model = new DefaultTableModel();
			 model.setColumnIdentifiers(colunas); 		 
			 linhas = new Vector<Object[]>();
			 int i = 0;
			 
			 simR = new float[12];
			 for(int k=0; k<12; k++){
				 simR[k] = mediasR[k];
	         }		          	
	         while (rs.next()){
	        	linhas.add(new Object[]{i+1,rs.getString("nome_sim_r"),rs.getString("valor_sim_r"),rs.getString("mes_sim_r"),"Receita",rs.getString("cod_sim_r")}); 	           	
	        	i++;
	        	simR[Integer.parseInt(rs.getString("mes_sim_r"))-1] = simR[Integer.parseInt(rs.getString("mes_sim_r"))-1] + Float.parseFloat(rs.getString("valor_sim_r"));
	         }           
	         simG = new float[12];
			 for(int k=0; k<12; k++){
				 simG[k] = mediasG[k];
	         }         
	         while (rs2.next()){
	        	 linhas.add(new Object[]{i+1,rs2.getString("nome_sim_d"),rs2.getString("valor_sim_d"),rs2.getString("mes_sim_d"),"Despesa",rs2.getString("cod_sim_d")}); 	           	
	        	 i++;
	        	 simG[Integer.parseInt(rs2.getString("mes_sim_d"))-1] = simG[Integer.parseInt(rs2.getString("mes_sim_d"))-1] - Float.parseFloat(rs2.getString("valor_sim_d"));
	         }
	         
	         tamLinhas = linhas.size();
	         for(int k=0; k<12; k++){
	        	 simL[k] = simR[k]+ simG[k];
	         } 
        } catch (SQLException ex) {
            System.out.println("ERRO: " + ex);
        }
	}

	private void montaTabela() {
		// TODO Auto-generated method stub
		eventos = new JTable();
		
		for (Object[] linha : linhas) {  
	         model.addRow(linha);  
	     }  		 
		eventos.setModel(model); 		
		eventos.removeColumn(eventos.getColumn("code")); 
	}

	private void analiseTendencia() {
		// TODO Auto-generated method stub
		initConexao();		
        try {
	            mediasG = new float[12];
	            int quant[] = new int[12];
	          	for(int k=0; k<12; k++){
	            		mediasG[k] = 0;
	            		quant[k] = 0;
	            }	            
	            String select = "SELECT valor_item_d, data_pagamento_item_d, vencimento_item_d FROM Item_despesa";
	            PreparedStatement ptStatement = c.prepareStatement(select);
	            rs = ptStatement.executeQuery();	            
	            while (rs.next()){
	            	if(rs.getString("vencimento_item_d").equals("1")){
		            	// Armazena em mes o mês da despesa, em num a coluna da matriz
		            	String data = rs.getString("data_pagamento_item_d");
		            	char mes[] = new char[2];
		            	data.getChars(5, 7, mes, 0);
		            	String m = Character.toString(mes[0]) + Character.toString(mes[1]);
		            	int num =Integer.parseInt(m)-1;		            	       		
		            	// Guarda em valorf o valor do item
		            	String valor = rs.getString("valor_item_d");
		            	float valorf = Float.parseFloat(valor);
		            	mediasG[num] = mediasG[num] + valorf;
		            	quant[num] = quant[num] + 1;
	            	}
	    	    }
	            select = "SELECT valor_fonte_item_d, data_pagamento_fonte_item_d, pago_fonte_item_d FROM Fonte_item_despesa";
	            ptStatement = c.prepareStatement(select);
	            rs = ptStatement.executeQuery();
	            while (rs.next()){
	            	if(rs.getString("pago_fonte_item_d").equals("1")){
		            	// Armazena em mes o mês da despesa, em num a coluna da matriz
		            	String data = rs.getString("data_pagamento_fonte_item_d");
		            	char mes[] = new char[2];
		            	data.getChars(5, 7, mes, 0);
		            	String m = Character.toString(mes[0]) + Character.toString(mes[1]);
		            	int num =Integer.parseInt(m)-1;
		            	
		            	// Guarda em valorf o valor do item
		            	String valor = rs.getString("valor_fonte_item_d");
		            	float valorf = Float.parseFloat(valor);
		            	mediasG[num] = mediasG[num] + valorf;
		            	quant[num] = quant[num] + 1;
	            	}
	    	    }    	            
	            for(int k=0; k<12; k++){
	            	if(quant[k] > 0)
	            		mediasG[k] = -1*(mediasG[k] / quant[k]);
	            }
	            mediasR = new float[12];
	            quant = new int[12];
	          	for(int k=0; k<12; k++){
	            		mediasR[k] = 0;
	            		quant[k] = 0;
	            }	            
	            select = "SELECT valor_item_r, data_recebimento_item_r, recebimento_item_r FROM Item_receita";
	            ptStatement = c.prepareStatement(select);
	            rs = ptStatement.executeQuery();	            
	            while (rs.next()){
	            	if(rs.getString("recebimento_item_r").equals("1")){
		            	// Armazena em mes o mês da despesa, em num a coluna da matriz
		            	String data = rs.getString("data_recebimento_item_r");
		            	char mes[] = new char[2];
		            	data.getChars(5, 7, mes, 0);
		            	String m = Character.toString(mes[0]) + Character.toString(mes[1]);
		            	int num =Integer.parseInt(m)-1;		            	       		
		            	// Guarda em valorf o valor do item
		            	String valor = rs.getString("valor_item_r");
		            	float valorf = Float.parseFloat(valor);
		            	mediasR[num] = mediasR[num] + valorf;
		            	quant[num] = quant[num] + 1;
	            	}
	    	    }
	            select = "SELECT * FROM Fonte_item_receita";
	            ptStatement = c.prepareStatement(select);
	            rs = ptStatement.executeQuery();
	            while (rs.next()){
	            	if(rs.getString("recebido_fonte_item_r").equals("1")){
		            	// Armazena em mes o mês da despesa, em num a coluna da matriz
		            	String data = rs.getString("data_recebimento_fonte_item_r");
		            	char mes[] = new char[2];
		            	data.getChars(5, 7, mes, 0);
		            	String m = Character.toString(mes[0]) + Character.toString(mes[1]);
		            	int num =Integer.parseInt(m)-1;
		            	// Guarda em valorf o valor do item
		            	String valor = rs.getString("valor_fonte_item_r");
		            	float valorf = Float.parseFloat(valor);
		            	mediasR[num] = mediasR[num] + valorf;
		            	quant[num] = quant[num] + 1;
	            	}
	    	    }    	     
	            for(int k=0; k<12; k++){
	            	if(quant[k] > 0)
	            		mediasR[k] = mediasR[k] / quant[k];
	            	mediasL[k] = mediasR[k]+ mediasG[k];
	            }       
        } catch (SQLException ex) {
            System.out.println("ERRO: " + ex);
        } 
	}

	/**
	 * Cria um conjunto de dados de amostra 
	 */
 
	private XYDataset createDataset() {		
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries series[] = new XYSeries[3];
		
		series[0] = new XYSeries("Ganho médio");
		series[1] = new XYSeries("Gasto médio");
		series[2] = new XYSeries("Lucro médio");
		for(int j=0; j<12; j++){
			series[0].add(j+1,mediasG[j]);
			series[1].add(j+1,mediasR[j]);
			series[2].add(j+1,mediasL[j]);
		}	
		dataset.addSeries(series[0]);
		dataset.addSeries(series[1]);
		dataset.addSeries(series[2]);

	    return dataset;		
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		}
	
	
}
