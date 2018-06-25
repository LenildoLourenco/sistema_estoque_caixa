import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.text.*;
import java.text.*;
import javax.swing.table.*;
import javax.sound.sampled.*;
import java.sql.*;

public class Vender extends JFrame implements ActionListener, FocusListener, AdjustmentListener
{
	
	//variáveis usadas para lançar a venda, na tabela vendas do banco, usa-se variáveis
	// separas, pois as outras são manipuladas, e esta precisam ter valores intactos
	// como total de unidade vendidas, total de lucro e total vendido
	double totalVendido, totalLucro, totalUnidades, lucro;
	
	//data usada para lançar na tabela venda do banco
	
	Date agora;
	DateFormat formataAgora;
	
	
	
	
	// objetos para usar banco de dados
	ResultSet resultSet;
	Statement statement;
	
	
	// int usado para resolver um problema no método de AdjustmentListener
	int contador;
	
	//string usada para adicionar itens na table
	String dados[] = new String[5];
	
	//vetor usado para armazenar o código dos itens adicionados
	int codigo[] = new int [5000];
	
	//int usado para fazer conversão de tQuantidade para int e double, baseado na string unidade que pegara unidade
	// do banco
	int quantidadeInt;
	double quantidadeDouble;
	String unidade;
	
	// variável para receber valor de preço do item de banco de fazer calculos
	double preco, subTotal;
	
	
	
	// label, password, panel e variáveis para adicionar senha nas OptionDialog
	JPanel panelPassword;
    JLabel labelPassword;
    JPasswordField password;
    String [] options = new String[]{"OK", "Cancelar"};
	int option;
	String senha;
	 
	 
	// variável usada genericamente nos InputDialog para receber os valores convertidos de aux  
	double valorConversao;

	 // variável usada como auxiliar para zerar o tableModel, declarada aqui pois é usada em mais de um método
	 int linhas;
	 
	 // valor de troco quando necessário
	 double troco;
		
	// decimal format para exibir valores monetários
	
	DecimalFormat df;
	
	// string para receber valor do teclado
	String aux;
	
	
	// variavel e vetor para opções sim e não
	int opcao;
	Object [] botoes = {"Sim", "Não"};
	

	// int opcaoPagamento utilizado para selecionar o tipo de pagamento na hora de finaliza a venda
	int opcaoPagamento;
	
	// double somento utilizado quando o pagamento é mesclado, parcialmente de uma forma e parcialmente de outra
	double pagamentoParcial;
	
	
	// double armazenará o total da compra, devendo ser zerado sempre que uma compra é finalizada
	double totalAPagar;
	
	// armazena a quantidade de itens comprada
	int totalItens;
	
	// int usado para remover itens da table
	
	int numeroItem;
	



	
	// botões
	JButton bFinalizarVenda, bCancelarVenda, bCancelarItem, bRetirarCaixa, bSair;
	

	


	
	// TextField
	JTextField tCodigoBarras, tQuantidade;
	
	// table central
	
	JTable tabela;
	DefaultTableModel tableModel;
	
	//ScrollPane
	
	JScrollPane pane;
	
	//Labels
	JLabel lCodigoBarras, lQuantidade;
	JLabel lValorVenda, lTotal;
	JLabel lOperador, lFuncionario;
	
	JPanel p1;
	
	// Labels e text field valorUnitario, quantidade adicionada, e subTotal
		 
	JLabel lValorUnitario, lQuantidadeAdicionada, lSubTotal;
		 
	JTextField  tValorUnitario, tQuantidadeAdicionada, tSubTotal;
	
	//som
	AudioInputStream audioInputStream;
	Clip clip;
	
	// definindo essa máquina como caixa 1
	String caixa = "1";
	
	
	
	Vender(String usuario)
	{
		setTitle("Frente De Caixa");
		
		// cor
		getContentPane().setBackground(Color.gray);
		
		//setando icone
		setIconImage(new ImageIcon("Imagens/vender.png").getImage());
		
		// definindo frame visível ja na classe e definindo ele maximizado não redimensionável
		
		
		// layout nulo
		p1 = new JPanel();
		p1.setLayout(null);
		getContentPane().add(p1);
		
		
		
		
		//data usada para lançar na tabela venda do banco,
		agora = new Date();
		//String dará ao objeto simpleDateFormat o formato de data que o mysql aceita
		String f1 = "yyyyMMdd";
		formataAgora = new SimpleDateFormat(f1);
		
		
		
		
		// decimalFormat para exibir valores monetários
		df = new DecimalFormat();
		df.applyPattern("#,##0.00");
		
		
		// label, password e panel para adicionar nas OptionDialog, assim elas aceitam senha
		panelPassword = new JPanel();
    	labelPassword = new JLabel("Digite a Senha");
    	password = new JPasswordField(10);
    	panelPassword.add(labelPassword);
   	    panelPassword.add(password);
		
		
		
		
		// TextField leitor código de barras
		tCodigoBarras = new JTextField(30);
		tCodigoBarras.setFont(new Font("Arial",Font.BOLD,25));
		tCodigoBarras.addActionListener(this);
		tCodigoBarras.setBounds(21,50,600,40);
		p1.add(tCodigoBarras);
		
		
		// label código barras
		lCodigoBarras = new JLabel("CÓDIGO DE BARRAS");
		lCodigoBarras.setBounds(21,30,150,20);
		p1.add(lCodigoBarras);
		
		// label quantidade
		lQuantidade = new JLabel("QUANTIDADE");
		lQuantidade.setBounds(655,30,100,20);
		p1.add(lQuantidade);
		
		// TextField da quantidade de itens
		tQuantidade = new JTextField("1"); // começa com o valor 1 mas pode ser alterado no tempo de execução
		tQuantidade.setFont(new Font("Arial", Font.BOLD, 25));
		tQuantidade.addActionListener(this);
		//FocusListener gera ação quando recebe ou perde o foco, conferir o método para a funcionalidade
		tQuantidade.addFocusListener(this);
		tQuantidade.setBounds(655,50,160,40);
		tQuantidade.setHorizontalAlignment(tQuantidade.CENTER); // setando o valor do textField centralizado
		p1.add(tQuantidade);
		
		
		// table central de vendas com scrollPane
		// adicionando colunas
		tableModel = new DefaultTableModel();
		tableModel.addColumn("Item");
		tableModel.addColumn("Descrição");
		tableModel.addColumn("Qtd");
		tableModel.addColumn("Valor");
		tableModel.addColumn("SubTotal");

		 
		tabela = new JTable(tableModel);
		
		// Definindo as celulas do table não arrastáveis e não redimensionáveis
	 	tabela.getTableHeader().setResizingAllowed(false);
	 	tabela.getTableHeader().setReorderingAllowed(false);
	 	
	 	// tabela não editabel
	 	tabela.setEnabled(false);
	 	
	 	// definindo o valor das células centralizado
	 	
	 	DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	 	centerRenderer.setHorizontalAlignment(JLabel.CENTER);
	 	
	 	tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
	 	tabela.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
	 	tabela.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
	 	tabela.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
	 	tabela.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
	 	
	 	// definindo a largura das colunas da table
	 	
	 	tabela.getColumnModel().getColumn(0).setPreferredWidth(50);
	 	tabela.getColumnModel().getColumn(1).setPreferredWidth(300);
	 	tabela.getColumnModel().getColumn(2).setPreferredWidth(50);
	 	tabela.getColumnModel().getColumn(3).setPreferredWidth(100);
	 	tabela.getColumnModel().getColumn(4).setPreferredWidth(100);

		
		pane = new JScrollPane(tabela);
		// scrollPane com fundo branco, para a table ficar com fundo branco
		pane.getViewport().setBackground(Color.WHITE);
		
		// setando scrollPane com rolamento automático através do método adjustmentListener
		pane.getVerticalScrollBar().addAdjustmentListener(this);
			
		pane.setBounds(21,100,600,450);
		p1.add(pane);
		 
		 // Botoes
		 
		 bFinalizarVenda = new JButton("FINALIZAR VENDA");
		 bFinalizarVenda.setBounds(845,100,175,50);
		 bFinalizarVenda.setMnemonic(KeyEvent.VK_F);
	 	 bFinalizarVenda.addActionListener(this);
		 p1.add(bFinalizarVenda);
		  
		 bCancelarVenda  = new JButton("CANCELAR VENDA");
		 bCancelarVenda.setBounds(845,170,175,50);
		 bCancelarVenda.setMnemonic(KeyEvent.VK_V);
		 bCancelarVenda.addActionListener(this);
		 p1.add(bCancelarVenda);
		 
		 bCancelarItem  = new JButton("CANCELAR ITEM");
		 bCancelarItem.setBounds(845,240,175,50);
		 bCancelarItem.setMnemonic(KeyEvent.VK_C);
		 bCancelarItem.addActionListener(this);
		 p1.add(bCancelarItem);
		 
		 bRetirarCaixa  = new JButton("RETIRAR DO CAIXA");
		 bRetirarCaixa.setBounds(845,310,175,50);
		 bRetirarCaixa.setMnemonic(KeyEvent.VK_R);
		 bRetirarCaixa.addActionListener(this);
		 p1.add(bRetirarCaixa);
		
		 bSair  = new JButton("SAIR");
		 bSair.setBounds(845,380,175,50);
		 bSair.setMnemonic(KeyEvent.VK_S);
		 bSair.addActionListener(this);
		 p1.add(bSair);
		 
		 // Labels total da venda
		 
		 lValorVenda = new JLabel("TOTAL");
		 lValorVenda.setBounds(655,550,100,20);
		 p1.add(lValorVenda);
		 
		 lTotal = new JLabel("R$ 0,00");
		 lTotal.setBounds(655,570,350,25);
		 lTotal.setFont(new Font("Arial",Font.BOLD,22));
		 p1.add(lTotal);
		 
		 // Label operador
		 
		 lOperador = new JLabel ("OPERADOR");
		 lOperador.setBounds(845,30,100,20);
		 p1.add(lOperador);
		 
		 lFuncionario = new JLabel(usuario);
		 lFuncionario.setBounds(845,50,350,20);
		 lFuncionario.setFont(new Font("Arial",Font.BOLD,18));
		 p1.add(lFuncionario);
		 
		 // Labels e text field valorUnitario, quantidade adicionada, e subTotal
		 
		 // label e textfield valorUnitario
		 
		 lValorUnitario = new JLabel("VALOR UNITÁRIO");
		 lValorUnitario.setBounds(655,100,100,20);
		 p1.add(lValorUnitario);
		 
		 tValorUnitario = new JTextField(5);
		 tValorUnitario.setFont(new Font("Arial",Font.BOLD,25));
		 tValorUnitario.setEditable(false);
		 tValorUnitario.setBounds(655,120,160,40);
		 p1.add(tValorUnitario);
		 
		 // label e textfield quantidadaAdicionada
		 	
		 	
		 lQuantidadeAdicionada = new JLabel("QUANTIDADE ADICIONADA");
		 lQuantidadeAdicionada.setBounds(655,170,150,20);
		 p1.add(lQuantidadeAdicionada);
		 
		 tQuantidadeAdicionada = new JTextField(5);
		 tQuantidadeAdicionada.setFont(new Font("Arial",Font.BOLD,25));
		 tQuantidadeAdicionada.setEditable(false);
		 tQuantidadeAdicionada.setBounds(655,190,160,40);
		 p1.add(tQuantidadeAdicionada);
		
		 // label e textfield subTotal
		 
		 lSubTotal = new JLabel("SUBTOTAL");
		 lSubTotal.setBounds(655,240,100,20);
		 p1.add(lSubTotal);
		 
		 tSubTotal = new JTextField(5);
		 tSubTotal.setFont(new Font("Arial",Font.BOLD,25));
		 tSubTotal.setEditable(false);
		 tSubTotal.setBounds(655,260,160,40);
		 p1.add(tSubTotal);
		 
		 
		 
		//método para ligar o banco de dados com a aplicação
		carregaResultSet();
		
	
		
		
		
		
	} // fim do construtor
	
	public void actionPerformed(ActionEvent e)
	{
		
		//item usado para receber o numero do item na opção retirar item
		int numeroItem = 0;
		
		//objeto usado para receber valores da table na hora de fazer consulta na própria
		Object objeto;
		
		// toda ação volta o foco para tCodigoBarras para o usuário não ter que voltar manualmente
		tCodigoBarras.requestFocus();
		
		
		// boleano usado no while para verificar validade de valores digitados;
		boolean valorInvalido = false;
	

		
		
		
		if(e.getSource() == tCodigoBarras)
		{
			// testando validade dos valores quantidade e código
			try
			{
				//atribuindo todos os códigos adicionados para dar baixa dos itens o banco posteriormente e testando
				// validade de valores
				codigo[totalItens] = Integer.parseInt(tCodigoBarras.getText());
				
			}
			catch(NumberFormatException erro)
			{
				JOptionPane.showMessageDialog(null,"Valor inválido em código de barras","Aviso",JOptionPane.WARNING_MESSAGE);
				tCodigoBarras.setText("");
				return;
			}
				
			//zerar código de barras após ler seu valor
			tCodigoBarras.setText("");
			
			
			
			//pesquisando o item no banco de dados
			try
			{
				
				
				resultSet = statement.executeQuery("SELECT * FROM produtos WHERE codigo ='"+codigo[totalItens]+"'");
				resultSet.next();
		
				//pegando descrição e valor do banco baseado no código
				dados[1] = resultSet.getString("descricao");
				preco = resultSet.getFloat("preco_venda");
				unidade = resultSet.getString("unidade");
				lucro = resultSet.getFloat("preco_venda") - resultSet.getFloat("preco_custo") ;
				
				
	
					
			} // fim try
			catch(SQLException erro)
			{
				JOptionPane.showMessageDialog(null,"Produto não encontrado");
				return;
			}
			
			
			

			try
			{
				//verificando unidade,
				if(unidade.equals("UN"))
				{
					quantidadeInt = Integer.parseInt(tQuantidade.getText());

				}
				else
				{
					quantidadeDouble = Double.parseDouble(tQuantidade.getText().replace(",","."));
				}
				
				
			}
			catch(NumberFormatException erro)
			{
				
				JOptionPane.showMessageDialog(null,"Valor inválido em quantidade","Aviso",JOptionPane.WARNING_MESSAGE);
				tQuantidade.requestFocus();
				return;
				
				
				
			} // fim catch
			
			//verificando unidade,
			if(unidade.equals("UN"))
			{
				
				dados[2] = ""+quantidadeInt;
				subTotal = quantidadeInt * preco;
				tQuantidadeAdicionada.setText(""+quantidadeInt);
				totalUnidades +=quantidadeInt;	
				totalLucro += lucro * quantidadeInt;
			}
			else
			{
				dados[2] = ""+quantidadeDouble;
				subTotal = quantidadeDouble * preco;
				tQuantidadeAdicionada.setText(""+quantidadeDouble);	
				totalUnidades +=quantidadeDouble;	
				totalLucro += lucro * quantidadeDouble;
			}
			

			

			//atribuindo valor ao vetor que adicionará a linha na table
			dados[0] = ""+(totalItens +1);
			dados[3] = "R$" + df.format(preco);
			dados[4] = "R$"+ df.format(subTotal);
			
		
			tableModel.addRow(dados);
			
			
			
			// adiciona valor aos textFields da aplicação
			tValorUnitario.setText("R$"+df.format(preco));
			
			tSubTotal.setText("R$"+df.format(subTotal));
			

			totalAPagar += subTotal;
			
			//variavel para fazer outra inserção no banco, no historico de vendas
			totalVendido +=subTotal;
			 
	
			lTotal.setText("R$"+df.format(totalAPagar));
	
			
			// colocando som de bipe na entrada de item	
			try
			{
				audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource("Sons/beep.wav"));
				clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				clip.start();
				
			}
			catch(Exception ex)
			{
				
			}
			
			// incrementa a totalItens
			totalItens++;
			
			
			
			
			
			
		return;		
		} // fim if tCodigoBarras
		
		
		if(e.getSource() == bFinalizarVenda)
		{
			
			// se finalizar venda com nenhum item vendido, não entra no método
			if(totalItens == 0)
			{
				JOptionPane.showMessageDialog(null,"Venda ainda não iniciada","Aviso",JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			
			//Loop para quanto o usuário digitar alguma opção inválida a janela voltar sozinha
			do
			{
			
				// JOptionPane para receber o tipo de pagamento em string
				aux = JOptionPane.showInputDialog("Digite a Forma de Pagamento \n1-Dinheiro \n2-Cartão de Débito"+
					"\n3-Cartão de Crédito \n4-Cheque");
					
				// verifica se o usuário selecionou cancelar e sai do método
				if(aux == null)
				{
					return;
				}
	
				
				// verifica se o valor foi digitado corretamente para previnir erros
				if(aux.equals("1") || aux.equals("2") || aux.equals("3") || aux.equals("4"))
				{
					opcaoPagamento = Integer.parseInt(aux);
					valorInvalido = false;
				} 
				else
				{
					JOptionPane.showMessageDialog(null,"Opção Inválida","Aviso",JOptionPane.WARNING_MESSAGE);
					valorInvalido = true;
				} // fim else
		
			}while(valorInvalido); // fim do while que repetem caso a opção seja inválida
				
			// switch case para verificar o tipo de pagamento
			switch(opcaoPagamento)
			{
				
				// em cada case irá exibir um JOptionPane para verificar se o pagamento será total ou parcial 
				// na forma de pagamento selecionada
				
				case 1: // pagamento em dinheiro
					metodoPagamento("Dinheiro",1);
					
					
					
				break; // fim switch dinheiro
					
				case 2: // débito
					metodoPagamento("Débito",2);
					
				break; // fim switch débito
				
				case 3: // crédito
					
					metodoPagamento("Crédito",3);
					
			
				break; // fim switch crédito
				
				case 4: // cheque
					metodoPagamento("Cheque",4);
				
			} // fim switch opcaopagamento
		
		return;	
		} // fim if bFinalizarVenda
		
		
		
		if(e.getSource() == bCancelarVenda)
		{
			// essa ação requer confirmação de senha
			if(!verificarSenha())
			{
				// se o método retornar false ele entra aqui e sai do método actionPerformed
				return;
			}
			
			
			
			// verifica se a venda foi iniciada antes de cancelarVenda
			if(totalItens <=0)
			{
				JOptionPane.showMessageDialog(null,"Venda Não Iniciada","Aviso",JOptionPane.INFORMATION_MESSAGE);
				return;	
			}
			
			opcao = JOptionPane.showOptionDialog(null,
				"Deseja mesmo Cancelar a Venda?", "Cancelar",
				JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,
				null,botoes,botoes[0]);
			if(opcao == JOptionPane.YES_OPTION)
			{
				
				// zera variaveis, textField e a table
				
				
				// código para zerar a table
				for(int i = 0; i < totalItens; i++)
				{
				
					tableModel.removeRow(0);
				}
				
				totalItens = 0;
				totalAPagar = 0.0;
				totalVendido = 0.0;
				totalLucro  = 0.0;
				totalUnidades = 0.0;
				
	
				tValorUnitario.setText("");
				tQuantidadeAdicionada.setText("");
				tSubTotal.setText("");
				lTotal.setText("R$0.00");
						
				JOptionPane.showMessageDialog(null,"Venda Cancelada","Aviso",JOptionPane.INFORMATION_MESSAGE);
				
				
				
				
				
				
			} // fim if YES_OPTION
			
			
		} // fim if CancelarVenda
		
		if(e.getSource() == bCancelarItem)
		{
			// essa ação requer confirmação de senha
			if(!verificarSenha())
			{
				// se o método retornar false ele entra aqui e sai do método actionPerformed
				return;
			}
			
			
			
			
			// verifica se a venda foi iniciada ou se possui apenas um item antes de remover Item
			if(totalItens <=0)
			{
				JOptionPane.showMessageDialog(null,"Venda Não Iniciada","Aviso",JOptionPane.INFORMATION_MESSAGE);
				return;	
			}
			else if(totalItens == 1)
			{
				JOptionPane.showMessageDialog(null,"Só um item adicionado cancele a venda","Aviso",JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			
			// loop para repetir quando o usuario digita um valor inválido
			do
			{
				// variavel para verificar se o usuario digita um valor valido
				valorInvalido = false;
				
				aux = JOptionPane.showInputDialog("Digite o Número do Item");
				// verifica se o usuario selecionou cancelar e sai da aplicação
				if(aux == null)
				{
					return;
				}
				
				try
				{
					numeroItem = Integer.parseInt(aux);
				}
				catch(NumberFormatException erro)
				{
					valorInvalido = true;
				}
				
				if(numeroItem < 1 || numeroItem > totalItens)
				{
					JOptionPane.showMessageDialog(null,"Código Inválido", "Aviso",JOptionPane.WARNING_MESSAGE);
					valorInvalido = true;
				}
				
				
				
				
			}while(valorInvalido);
			// diminui uma unidade porque no array começa do 0 , e a lista começa no 1, então 1 representa 0
			// por exemplo
			numeroItem--;
			
	
			
			try
			{
				
				//pagando valores de lucro no banco para subtrair na variável totalLucro
				resultSet = statement.executeQuery("SELECT * FROM produtos WHERE codigo ='"+codigo[numeroItem]+"'");
				resultSet.next();

				lucro = resultSet.getFloat("preco_venda") - resultSet.getFloat("preco_custo") ;
				
				
				
	
					
			} // fim try
			catch(SQLException erro)
			{
				System.out.println("Erro item não encontrado");
				return;
			}
			
			//objeto para receber o valor do produto com base na table
			objeto = tabela.getModel().getValueAt(numeroItem,4); 
			//passando objeto para string
			aux = objeto.toString();
			
			//retirando caracteres especiais
			aux = aux.replace("R","");
			aux = aux.replace("$","");
			aux = aux.replace(",",".");
	
			// retirando do total da compra
			totalAPagar -= new Double(Double.parseDouble(aux));
			lTotal.setText("R$"+df.format(totalAPagar));
			
			
			//retirando das variáveis que mandam dados para a tabela vendas
			
			totalVendido -= new Double (Double.parseDouble(aux));
			
			
			//pegando quantidade adicionada com base na table
			objeto = tabela.getModel().getValueAt(numeroItem,2); 
			//passando objeto para string
			aux = objeto.toString();
			//retirando caracteres especiais
			aux = aux.replace(",",".");
			
			quantidadeDouble = Double.parseDouble(aux);
			
			
			totalLucro  = totalLucro - (quantidadeDouble * lucro );
			
			totalUnidades -=quantidadeDouble;
			
			
			
			
			// exibindo mensagem dos itens retirados, pegando os valores na table baseado no
			// numeroItem digitado pelo usuários
			JOptionPane.showMessageDialog(null,
			"Retirado \n\nItem: "+tabela.getModel().getValueAt(numeroItem,0).toString()+
			"\nDescrição: "+tabela.getModel().getValueAt(numeroItem,1).toString()+
			"\nQuantidade: " + tabela.getModel().getValueAt(numeroItem,2).toString()+
			"\nSubTotal: " + aux , "Aviso",JOptionPane.WARNING_MESSAGE);
		
			
			tableModel.removeRow(numeroItem);

			
			// loop para retirar o item selecionado do array codigo voltando posições à frente casa por casa
			for(int i = numeroItem; i < totalItens; i++)
			{
			
				codigo[i] = codigo[i+1];	
			
			}
			

			
			// diminuindo total itens pois foi retirado um
			totalItens--;
			
			// setando novos valores em número do item
			for(int i = 0; i < totalItens; i++)
			{
				
				tableModel.setValueAt((i+1),i,0);
			}

			
			
			return;	
		} // fim if CancelarItem
		
		
		if(e.getSource() == bRetirarCaixa)
		{
             
             // verifica senha antes de continuar
             if(!verificarSenha())
             {
             	return;
             }
             
             /* só se abrirá essa janela após a confirmação da senha
              * convertendo o valor para double com try catch
              */
              
             // loop para repetição quando há valores inválidos
             do
             {
             
	             aux = JOptionPane.showInputDialog("Digite o Valor");
	             
	             // selecionou cancelar? se sim saia do método
	             if(aux == null)
	             {
	             	return;
	             }
	             
	             aux = aux.replace(",",".");
	             
	             try
	             {
	             	valorConversao = Double.parseDouble(aux);
	             }
	             catch(NumberFormatException erro)
	             {
	             	
	             	// atribui-se valor negativo apenas para voltar no loop
					valorConversao = -1;	
	             }
	             
	             // verifica se o valor esta negativo por inserção ou se caiu na exceção acima e recebeu -1
	             if(valorConversao < 0) 
	             {
	             	JOptionPane.showMessageDialog(null,"Valor Inválido", "Aviso",JOptionPane.WARNING_MESSAGE);
	             }
             
             }while(valorConversao < 0);
             
            try
			{
				//retirando dinheiro do caixa
				String sql = "UPDATE caixa SET total_dinheiro = total_dinheiro - "+valorConversao+
					",retirado_caixa = retirado_caixa +"+valorConversao+" WHERE codigo = '"+caixa+"'";
				statement.executeUpdate(sql);
				
			}
			catch(SQLException erro)
			{
							
			}
			
			JOptionPane.showMessageDialog(null,"Retirado : R$" +valorConversao, "Aviso",JOptionPane.WARNING_MESSAGE);
			
			

			return;	
		} // fim if retirarCaixa
		
		if(e.getSource() == bSair)
		{
			
			// verifica se a compra não está em andamento para sair
			if(totalAPagar >0)
			{
				JOptionPane.showMessageDialog(null,"Compra em Andamento! \nFinalize a Compra",
					 "Aviso",JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			opcao = JOptionPane.showOptionDialog(null,
				"Deseja mesmo Sair do Menu de Vendas?", "Fechar",
				JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,
				null,botoes,botoes[0]);
			if(opcao == JOptionPane.YES_OPTION)
				setVisible(false);
			
			
			
			
		} // fim if sair
		
		
		
	} // fim ActionListener
	
	
	// método utilizado pelo metodoPagamento
	public double metodoPagamentoParcial(String tipo, int valorTipo)
	{
		
		
		// loop para repetir caso o valor seja inválido
		do
		{
			// zerar o valor no inicio do loop para atribir zero caso ele esteja negativo e retorne
			aux = JOptionPane.showInputDialog("Pagamento Parcial em "+tipo+"\nDigite o Valor");
			
			// verifica se o usuario selecionou cancelar e sai 
			if(aux == null)
			{
				return(0);
			}
		
			
				
				
			aux = aux.replace(',', '.');
		
			try
			{
				valorConversao = Double.parseDouble(aux);
			}
			catch(NumberFormatException erro)
			{
				// se o usuario digitar um valor inválido ele vira negativo para ficar no loop
				valorConversao = -1.0;
			}
				
				
				
			// enquanto o valor for negativo ou maior ou igual que o valor total da compra o loop permanece
			if(valorConversao < 0 )
				JOptionPane.showMessageDialog(null,"Valor Inválido", "Aviso",JOptionPane.WARNING_MESSAGE);
				
			if(valorConversao > totalAPagar)
				JOptionPane.showMessageDialog(null,"Valor Maior Que o Total", "Aviso",JOptionPane.WARNING_MESSAGE);
					
			if(valorConversao == totalAPagar)
			{
				
				JOptionPane.showMessageDialog(null,"Valor Igual ao Total, \nSelecione Pagamento Total",
				 "Aviso",JOptionPane.WARNING_MESSAGE);
				 return(0);
			}	
		
			
			
			
		}while(valorConversao < 0 || valorConversao >= totalAPagar);
		
		
		
			// opção que verifica se o pagamento é em cheque ou dinheiro para voltar o troco
			if(valorTipo == 1 || valorTipo == 4)
			{
				do
				{
					
					aux = JOptionPane.showInputDialog("Digite o valor pago");
					
					// verifica se o usuario selecionou cancelar e sai 
					if(aux == null)
					{
						return(0);
					}
				
					
						
						
					aux = aux.replace(',', '.');
				
					try
					{
						troco = Double.parseDouble(aux);
					}
					catch(NumberFormatException erro)
					{
						// se o usuario digitar um valor inválido ele vira negativo para ficar no loop
						troco = -1.0;
					}
						
						
						
					// enquanto o valor for negativo ou menor que o valor parcial a pagar o loop permanece
					if(troco < 0 )
						JOptionPane.showMessageDialog(null,"Valor Inválido", "Aviso",JOptionPane.WARNING_MESSAGE);
						
					if(troco < valorConversao)
						JOptionPane.showMessageDialog(null,"Valor Menor que Valor a Pagar", "Aviso",JOptionPane.WARNING_MESSAGE);	
				
					
					
					
				}while(troco < 0 || troco < valorConversao);
				
				// calcula o troco 
				troco = troco - valorConversao;
				
				JOptionPane.showMessageDialog(null,"Troco: R$"+df.format(troco), "Aviso",JOptionPane.WARNING_MESSAGE);
				
			} // fim if
		
		
		// verifica se o pagamento foi bem sucedido
		opcao = JOptionPane.showOptionDialog(null,
		"Pagamento bem Sucedido? \nA Operação não poderá ser desfeita depois", "Fechar",
		JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,
		null,botoes,botoes[0]);
		
		
		// senão retorna zero
		if(opcao == JOptionPane.NO_OPTION)
		{
			return(0);
		}
		// retorna o valor para a aplicação 0.0 se for selecionado cancelar ou o valor do pagamento parcial for negativo
		return(valorConversao);
		
		
	
		
	} // fim método metodoPagamentoParcial
	
	
	
	
	// método executa as funções pertinentes ao pagamento, perguntando o tipo e atribuindo os resultados
	// ao banco de dados
	public void metodoPagamento(String tipo, int valorTipo)
	{

		boolean valorInvalido;
		int opcaoPagamento = 0;
		// loop para repetir caso uma opção seja inválida
		do
		{
					
			aux = JOptionPane.showInputDialog("Pagamento em "+tipo+"\n\n1-Pagamento Total \n2-Pagamento Parcial");
						
			// verifica se o usuário selecionou cancelar e sai do método
			if(aux == null)
			{
				return;
			}
	
			// verifica se o valor foi digitado corretamente
			if(aux.equals("1") || aux.equals("2"))
			{
				opcaoPagamento = Integer.parseInt(aux);
				valorInvalido = false;
							
			} 
			else
			{
				JOptionPane.showMessageDialog(null,"Opção Inválida","Aviso",JOptionPane.WARNING_MESSAGE);
				valorInvalido = true;
							
			} // fim else
			
		}while(valorInvalido);
		
			
		
		if(opcaoPagamento == 1) // pagamento total
		{
			
			// opção que verifica se o pagamento é em cheque ou dinheiro para voltar o troco
			if(valorTipo == 1 || valorTipo == 4)
			{
				do
				{
					
					aux = JOptionPane.showInputDialog("Digite o valor pago");
					
					// verifica se o usuario selecionou cancelar e sai 
					if(aux == null)
					{
						return;
					}
				
					
						
						
					aux = aux.replace(',', '.');
				
					try
					{
						valorConversao = Double.parseDouble(aux);
					}
					catch(NumberFormatException erro)
					{
						// se o usuario digitar um valor inválido ele vira negativo para ficar no loop
						valorConversao = -1.0;
					}
						
						
						
					// enquanto o valor for negativo ou menor que o total da compra o loop permanece
					if(valorConversao < 0 )
						JOptionPane.showMessageDialog(null,"Valor Inválido", "Aviso",JOptionPane.WARNING_MESSAGE);
						
					if(valorConversao < totalAPagar)
						JOptionPane.showMessageDialog(null,"Valor Menor que o Total", "Aviso",JOptionPane.WARNING_MESSAGE);	
				
					
					
					
				}while(valorConversao < 0 || valorConversao < totalAPagar);
				
			// calcula o troco 
			troco = valorConversao - totalAPagar;
			
			JOptionPane.showMessageDialog(null,"Troco: R$"+df.format(troco), "Aviso",JOptionPane.WARNING_MESSAGE);
				
			} // fim if
			
				
			// verifica se o pagamento foi bem sucedido
			opcao = JOptionPane.showOptionDialog(null,
			"Pagamento bem Sucedido? \nA Operação não poderá ser desfeita depois", "Fechar",
			JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,
			null,botoes,botoes[0]);
				
			if(opcao == JOptionPane.YES_OPTION)
			{
	
	
				
				//método que da baixa nos itens do banco e acrescentar o valor da compra ao caixa
				lancarCaixa(valorTipo, totalAPagar);
				baixaItens();
				
				
				//setando historico de vendas no banco
				setandoHistoricoVendas();
				
				
				// código para zerar a table
				for(int i = 0; i < totalItens; i++)
				{
				
					tableModel.removeRow(0);
	
				}
				
				totalItens = 0;
				totalAPagar = 0.0;
				tValorUnitario.setText("");
				tQuantidadeAdicionada.setText("");
				tSubTotal.setText("");
				lTotal.setText("R$0.00");
				
				
				JOptionPane.showMessageDialog(null,"Compra Finalizada","Aviso",JOptionPane.INFORMATION_MESSAGE);
			}
				
		
		} // fim if pagamento total
		else // pagamento parcial
		{
			// chama o método com os parâmetro tipo e valorTipo, que ja era um parametro deste metodoPagamento
			// o metodo tem retorno double
			pagamentoParcial = metodoPagamentoParcial(tipo, valorTipo);
			
		 
		 	totalAPagar -= pagamentoParcial;
		 	lTotal.setText("R$"+df.format(totalAPagar));
		 	
		 	//método que da baixa nos itens do banco e acrescentar o valor da compra ao caixa
			lancarCaixa(valorTipo, pagamentoParcial);
			
			
	
	
		 
		
	
		} // fim else
	
	
	
	} // fim método metodoPagamento
	
	
	// esse método faz com que quando o usuário colocar o foco no textField tQuantidade ele zere automaticamente
	public void focusGained(FocusEvent e)
	{
		if(e.getSource() == tQuantidade)
		{
			tQuantidade.setText("");
		}
		
	} // fim do focusGained
	
	
	
	// esse método por enquanto não é usado para nada, só é colocado para não dar erro
	public void focusLost(FocusEvent e)
	{
		
	
	} // fim do método focusLost
	
	// método do adjustmentListener utilizado para rolar automaticamente JScrollPane
	public void adjustmentValueChanged(AdjustmentEvent e)
	{	
		
		/* verifica se o usuário está inserindo itens na compra
		 *pois assim sendo, o contador sera diferente de totalItens,
		 *pois quando as duas variáveis são iguais significa que o usuario
		 *esta acessando este método aqui por apenas rolar a barra de JScrollPane.
		 *Sem este if, seria impossível rolar a barra para cima pois toda vez
		 *iria chama o método abaixo para rolar a barra no maximo de comprimento
		 */
		if(contador != totalItens)
		{
			
			contador = totalItens;
			e.getAdjustable().setValue(e.getAdjustable().getMaximum());
		}
		  
	} // fim adjustmentListener

	
	// algumas ações da classe requerem senha, esse método evita a escrita de todo esse código mais de uma vez
	public boolean verificarSenha()
	{
			String senhaAdmin ="";
			try
			{
				// só é possível dar baixa no caixa com a senha do admin
				resultSet = statement.executeQuery("SELECT * FROM usuarios WHERE login = 'admin'");
				resultSet.next();
				
				senhaAdmin = resultSet.getString("senha");
			
					
			} // fim try
			catch(SQLException erro)
			{
			}
	
			
			// loop para senha inválida
            do
            {
             	            
	            // criando um JOptionDialog para receber senha
	            option = JOptionPane.showOptionDialog(null,panelPassword,"Senha",
	            	JOptionPane.NO_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,panelPassword);
	            
	            
	            if(option == 0) // selecionou ok
	            {
	            	senha = new String(password.getPassword());
	            	password.setText("");
	            }
	            else // selecionou cancelar
	            {

    				password.setText("");
					return(false);
	             	
    	         }
	             
	             // verifica se o usuário digitou uma senha inválida
	             if(!senha.equals(senhaAdmin)) // colocar aqui as senhas válidas
	             {
	             	password.setText("");
	             	JOptionPane.showMessageDialog(null,"Senha inválida", "Aviso",JOptionPane.WARNING_MESSAGE);
	             }
	             
	             
             }while(!senha.equals(senhaAdmin)); // colocar aqui as senhas válidas
             
             
             return(true);
             
	} // fim verificarSenha()
	
	//método usado para ligar o banco de dados com a aplicação
	public void carregaResultSet()
	{
		
		String url="jdbc:mysql://127.0.0.1:3306/sistema_gerenciamento_estoque";
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection minhaConexao=DriverManager.getConnection(url,"root","1234");
			statement=minhaConexao.createStatement(resultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);;
			
			
		}
		
		catch(ClassNotFoundException erro)
		{
			System.out.println("Driver nao encontrado");
		}
		catch(SQLException ero)
		{
			System.out.println("Problemas na conexao com a fonte de dados");
		}
	} // fim carregaResultSet
	
	
	
	
	
	public void lancarCaixa(int valorTipo, double total)
	{
		String tipo ="";
		// fazer a verificação do tipo de pagamento para mandar para o banco no seu respectivo campo
		switch(valorTipo)
		{
			case 1:
				tipo = "total_dinheiro";
				break;
			case 2:
				tipo = "total_cartao_debito";
				break;
			case 3:
				tipo = "total_cartao_credito";
				break;
			case 4:
				tipo = "total_cheque";
		}	// fim switch case
		
		try
		{
			
			
			
			String sql = "UPDATE caixa SET "+tipo+" = "+tipo+" + "+total+" WHERE codigo = '"+caixa+"'";
			statement.executeUpdate(sql);
			
			//adicionando ao total vendido
			sql = "UPDATE caixa SET total_vendido = total_vendido +"+total+" WHERE codigo ='"+caixa+"'";
			statement.executeUpdate(sql);
			
			
		}
		catch(SQLException erro)
		{
			
				
		}


	
	} // fim método lancarCaixa
	
	public void baixaItens()
	{
		double quantidadeCadaItem = 0.0;
		// criando vetor de objetos para recolher os dados da table e mandar para o banco
		Object objeto;
		
		//for para dar baixa nos itens do banco um a um
		for(int i = 0; i<totalItens; i++)
		{
			//objeto para receber o valor da table
			objeto = tabela.getModel().getValueAt(i,2); 
			//passando objeto para string
			aux = objeto.toString();
			quantidadeCadaItem = Double.parseDouble(aux);
		
			try
			{
				//subtraindo os itens do banco
				String sql = "UPDATE produtos SET quantidade_estoque = quantidade_estoque - '"+quantidadeCadaItem+"'"+
					"Where codigo = '"+codigo[i]+"'";
				statement.executeUpdate(sql);
				
		
			}
			catch(SQLException erro)
			{
			}
			
		} // fim for
		
	} // fim baixaItens
	
	
	public void setandoHistoricoVendas()
	{
		
		
		try
		{
			String sql ="INSERT INTO vendas (data_venda, total_unidades, total_lucro," +
			 	"total_vendido) Values ('"+
				formataAgora.format(agora)+"','"+
				totalUnidades +"','"+
				totalLucro  +"','"+	
			 	totalVendido +"')";		 
		    statement.executeUpdate(sql);
		
		} // fim try
		catch(SQLException erro)
		{
			JOptionPane.showMessageDialog(null,"Falha no historicoVendas");
		}

		//zerando as variáveis
		totalVendido = 0.0;
		totalLucro  = 0.0;
		totalUnidades = 0.0;
	

		
	} // fim setandoHistorioVendas
	
	
	

				
	
} // fim da classe