import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
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
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

class TextColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener, InputMethodListener{  

private ConnectionFactory con;
private Connection c;
private void initConexao(){if(con == null){ con = new ConnectionFactory(); c = con.getConnection();}}

        JTable table;  
        JTextField renderButton;  
        JTextField editButton;  
        String text;  
        String metodo;
        
    	ResultSet rs = null;
    	ResultSet rs2 = null;
   
        public TextColumn(JTable table, int column, String tabela){  
            super();  
            metodo = tabela;
            this.table = table;  
            renderButton = new JTextField(3);  
   
            editButton = new JTextField(3); 
            //editButton.setFocusable(false);  
            //editButton.addActionListener(this);  
            //editButton.setText("0");
            editButton.addInputMethodListener(this);
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

		@Override
		public void caretPositionChanged(InputMethodEvent arg0) {
			// TODO Auto-generated method stub
			System.out.println("www");
		}

		@Override
		public void inputMethodTextChanged(InputMethodEvent arg0) {
			// TODO Auto-generated method stub
			renderButton.setText(arg0.toString());
			System.out.println("qqq");
		}

		/*@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
		} */ 
   
        public void actionPerformed(ActionEvent e){ 
        	System.out.println("eee");
        	/*if(metodo.equals("itemCardapio")){
        		editButton.setText(arg0.toString());
        	}*/
        	/*if(metodo.equals("itemNaoPago")){
        		fireEditingStopped();  
                JanelaPagaItem janela = new JanelaPagaItem((String) table.getModel().getValueAt(table.getSelectedRow(), 4));
        	}
        	else if(metodo.equals("fonteNaoPaga")){
        		fireEditingStopped();  
                JanelaPagaFonte janela = new JanelaPagaFonte((String) table.getModel().getValueAt(table.getSelectedRow(), 4));
        	}
        	else if(metodo.equals("itemNaoRecebido")){
        		fireEditingStopped();  
                JanelaRecebeItem janela = new JanelaRecebeItem((String) table.getModel().getValueAt(table.getSelectedRow(), 3));
        	}
        	else if(metodo.equals("fonteNaoRecebida")){
        		fireEditingStopped();  
                JanelaRecebeFonte janela = new JanelaRecebeFonte((String) table.getModel().getValueAt(table.getSelectedRow(), 4));
        	}*/
        }
    
    
    }  
