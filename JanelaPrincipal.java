
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class JanelaPrincipal extends JFrame implements ActionListener {

	    // barra de menus
		private JMenuBar barraMenu;
		
		// menus
		private JMenu menuCardapio;
		//private JMenu menuCaixa;
		
		// itens de menus	
		private JMenuItem cardapioGerenciar;
		private JMenuItem fechaCaixa;
		
		// botões
		private Container container;
		private JButton btCliente;
		private JButton btPedido;
		private JButton btPagamento;
			
		public JanelaPrincipal()
		{ 
		
			barraMenu = new JMenuBar();
			menuCardapio = new JMenu ("Cardápio");
			//menuCaixa = new JMenu ("Fechamento do caixa");			 
            
			cardapioGerenciar = new JMenuItem("Gerenciar");
			fechaCaixa = new JMenuItem("Fechamento do Caixa");
			
			btCliente = new JButton("Cadastrar/Alterar Cliente");
			btPedido = new JButton("Novo Pedido");
			btPagamento = new JButton("Confirmar Pagamento");
			
			container = this.getContentPane();
			
			barraMenu.add(menuCardapio);	
			menuCardapio.add(cardapioGerenciar);	
            
			//barraMenu.add(menuCaixa);
			barraMenu.add(fechaCaixa);
			
			container.add(btCliente);         
	        container.add(btPedido);
	        container.add(btPagamento);
							
			fechaCaixa.addActionListener(this);
			btCliente.addActionListener(this);
			btPedido.addActionListener(this);
			btPagamento.addActionListener(this);
			cardapioGerenciar.addActionListener(this);
			
			// propriedades da Janela Principal
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setLayout(new FlowLayout()); 
			this.setJMenuBar(barraMenu);
			this.pack();
			//this.setResizable(false);
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
			this.setVisible(true);
			this.setTitle("Delivery"); 
			this.setSize(800, 800); 
			 
		}
			
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == fechaCaixa)
			{
				//JanelaFechaCaixa jFechaC = new JanelaFechaCaixa();
			}
			else if (e.getSource() == btCliente)
			{
				//JanelaCliente jCli = new JanelaCliente();
			}
			else if (e.getSource() == btPedido)
			{
				JanelaPedido jPedido = new JanelaPedido();
			}
			else if (e.getSource() == btPagamento)
			{
				JanelaPagamento jPagamento = new JanelaPagamento();
			}
			else if (e.getSource() == cardapioGerenciar)
			{
				//JanelaCardapio jCard = new JanelaCardapio();
			}
		}

}