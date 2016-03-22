import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

class ButtonColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener{  

private ConnectionFactory con;
private Connection c;
private void initConexao(){if(con == null){ con = new ConnectionFactory(); c = con.getConnection();}}

        JTable table;  
        JButton renderButton;  
        JButton editButton;  
        String text;  
        String metodo;
        
    	ResultSet rs = null;
    	ResultSet rs2 = null;
    	ResultSet rs3 = null;
    	ResultSet rs4 = null;
    	ResultSet rs5 = null;
   
        public ButtonColumn(JTable table, int column, String tabela){  
            super();  
            metodo = tabela;
            this.table = table;  
            renderButton = new JButton();  
   
            editButton = new JButton();  
            editButton.setFocusPainted(false);  
            editButton.addActionListener(this);  
   
            TableColumnModel columnModel = table.getColumnModel();  
            columnModel.getColumn(column).setCellRenderer(this);  
            columnModel.getColumn(column).setCellEditor(this);  
        }  
   
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)  {  
            if (hasFocus){  
                renderButton.setForeground(table.getForeground());  
                renderButton.setBackground(UIManager.getColor("Button.background"));  
            }  
            else if (isSelected){  
                renderButton.setForeground(table.getSelectionForeground());  
                 renderButton.setBackground(table.getSelectionBackground());  
            }  
            else{  
                renderButton.setForeground(table.getForeground());  
                renderButton.setBackground(UIManager.getColor("Button.background"));  
            }  
   
            renderButton.setText( (value == null) ? "" : value.toString() );  
            return renderButton;  
        }  
   
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            text = (value == null) ? "" : value.toString();  
            editButton.setText( text );  
            return editButton;  
        }  
   
        public Object getCellEditorValue(){  
            return text;  
        }  
   
        public void actionPerformed(ActionEvent e){  
        	if(metodo.equals("pedidosAbertos")){
        		fireEditingStopped();  
        		
        		String code;
        		String CPF = null;
        		float valor = 0;
        		float valorAntigo = 0;
        		int ganhos = 0;
        		int gastos = 0;
        		int pontos = 0;
        		
        		code = (String) table.getModel().getValueAt(table.getSelectedRow(), 0);
        		
        		((DefaultTableModel) table.getModel()).removeRow(table.getSelectedRow()); 
        		
        		initConexao();
        		
        		
				String update = "UPDATE Pedido SET status=? WHERE pedido_Id=?";
				String select = "SELECT Pedido.pedido_Id, valor_total, cpf FROM Pedido WHERE pedido_Id=?";
				String select2 = "SELECT Empresa.caixa FROM Empresa";
				String update2 = "UPDATE Empresa SET caixa=?";
				String select3 = "SELECT Pedido.pedido_Id, pontos_ganhos, pontos_gastos FROM Pedido WHERE pedido_Id=?";
				String select4 = "SELECT Cliente.pontos_fidelidade FROM Cliente WHERE cpf=?";
				String update3 = "UPDATE Cliente SET pontos_fidelidade=? WHERE cpf=?";
			
				

				try {
					
					// Atualiza o status do pedido para pago
					PreparedStatement ptStatement = c.prepareStatement(update);
					ptStatement.setString(1, "1");
					ptStatement.setString(2, code);
					ptStatement.executeUpdate();
					
					
					// Atualiza o caixa da empresa com o valor do pedido pago
					PreparedStatement ptStatement2 = c.prepareStatement(select);
					ptStatement2.setString(1, code);
			        rs2 = ptStatement2.executeQuery();
			        
			        while(rs2.next())
			        {
			        	valor = Float.parseFloat(rs2.getString("valor_total"));
			        	CPF = rs2.getString("cpf");
			        }
			         
			         PreparedStatement ptStatement3 = c.prepareStatement(select2);
				     rs3 = ptStatement3.executeQuery();
				     rs3.next();
				     
				     valorAntigo = Float.parseFloat(rs3.getString("caixa"));
			         
			         PreparedStatement ptStatement4 = c.prepareStatement(update2);
						ptStatement4.setFloat(1, valor+valorAntigo);
						ptStatement4.executeUpdate();
						
					// Atualiza os pontos fidelidade do cliente
						PreparedStatement ptStatement5 = c.prepareStatement(select3);
						ptStatement5.setString(1, code);
				        rs4 = ptStatement5.executeQuery();
				        
				        while(rs4.next())
				        {
				        	gastos = Integer.parseInt(rs4.getString("pontos_gastos"));
				        	ganhos = Integer.parseInt(rs4.getString("pontos_ganhos"));
				        }
				        
				        System.out.println("Ganhos " + ganhos);
				        System.out.println("Gastos " + gastos);
				        
				        PreparedStatement ptStatement6 = c.prepareStatement(select4);
						ptStatement6.setString(1, CPF);
				        rs5 = ptStatement6.executeQuery();
				        
				        while(rs5.next())
				        {
				        	pontos = Integer.parseInt(rs5.getString("pontos_fidelidade"));
				        }
				        
				        System.out.println("Pontos " + pontos);
				        
				        PreparedStatement ptStatement7 = c.prepareStatement(update3);
						ptStatement7.setFloat(1, pontos-gastos+ganhos);
						ptStatement7.setString(2, CPF);
						ptStatement7.executeUpdate();
			            
					JOptionPane.showMessageDialog(editButton, this, "Pedido confirmado com sucesso!", 0);
		        	} catch (SQLException ex) {
		        		System.out.println("ERRO: " + ex);
		        } 			
        		
                //ConfirmaPagamento janela = new ConfirmaPagamento((String) table.getModel().getValueAt(table.getSelectedRow(), 6));
        	}
        }  
    
    
    }  
