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
	
	//vari�veis usadas para lan�ar a venda, na tabela vendas do banco, usa-se vari�veis
	// separas, pois as outras s�o manipuladas, e esta precisam ter valores intactos
	// como total de unidade vendidas, total de lucro e total vendido
	double totalVendido, totalLucro, totalUnidades, lucro;
	
	//data usada para lan�ar na tabela venda do banco
	
	Date agora;
	DateFormat formataAgora;
	
	
	
	
	// objetos para usar banco de dados
	ResultSet resultSet;
	Statement statement;
	
	
	// int usado para resolver um problema no m�todo de AdjustmentListener
	int contador;
	
	//string usada para adicionar itens na table
	String dados[] = new String[5];
	
	//vetor usado para armazenar o c�digo dos itens adicionados
	int codigo[] = new int [5000];
	
	//int usado para fazer convers�o de tQuantidade para int e double, baseado na string unidade que pegara unidade
	// do banco
	int quantidadeInt;
	double quantidadeDouble;
	String unidade;
	
	// vari�vel para receber valor de pre�o do item de banco de fazer calculos
	double preco, subTotal;
	
	
	
	// label, password, panel e vari�veis para adicionar senha nas OptionDialog
	JPanel panelPassword;
    JLabel labelPassword;
    JPasswordField password;
    String [] options = new String[]{"OK", "Cancelar"};
	int option;
	String senha;
	 
	 
	// vari�vel usada genericamente nos InputDialog para receber os valores convertidos de aux  
	double valorConversao;

	 // vari�vel usada como auxiliar para zerar o tableModel, declarada aqui pois � usada em mais de um m�todo
	 int linhas;
	 
	 // valor de troco quando necess�rio
	 double troco;
		
	// decimal format para exibir valores monet�rios
	
	DecimalFormat df;
	
	// string para receber valor do teclado
	String aux;
	
	
	// variavel e vetor para op��es sim e n�o
	int opcao;
	Object [] botoes = {"Sim", "N�o"};
	

	// int opcaoPagamento utilizado para selecionar o tipo de pagamento na hora de finaliza a venda
	int opcaoPagamento;
	
	// double somento utilizado quando o pagamento � mesclado, parcialmente de uma forma e parcialmente de outra
	double pagamentoParcial;
	
	
	// double armazenar� o total da compra, devendo ser zerado sempre que uma compra � finalizada
	double totalAPagar;
	
	// armazena a quantidade de itens comprada
	int totalItens;
	
	// int usado para remover itens da table
	
	int numeroItem;
	



	
	// bot�es
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
	
	// definindo essa m�quina como caixa 1
	String caixa = "1";
	
	
	
	Vender(String usuario)
	{
		setTitle("Frente De Caixa");
		
		// cor
		getContentPane().setBackground(Color.gray);
		
		//setando icone
		setIconImage(new ImageIcon("Imagens/vender.png").getImage());
		
		// definindo frame vis�vel ja na classe e definindo ele maximizado n�o redimension�vel
		
		
		// layout nulo
		p1 = new JPanel();
		p1.setLayout(null);
		getContentPane().add(p1);
		
		
		
		
		//data usada para lan�ar na tabela venda do banco,
		agora = new Date();
		//String dar� ao objeto simpleDateFormat o formato de data que o mysql aceita
		String f1 = "yyyyMMdd";
		formataAgora = new SimpleDateFormat(f1);
		
		
		
		
		// decimalFormat para exibir valores monet�rios
		df = new DecimalFormat();
		df.applyPattern("#,##0.00");
		
		
		// label, password e panel para adicionar nas OptionDialog, assim elas aceitam senha
		panelPassword = new JPanel();
    	labelPassword = new JLabel("Digite a Senha");
    	password = new JPasswordField(10);
    	panelPassword.add(labelPassword);
   	    panelPassword.add(password);
		
		
		
		
		// TextField leitor c�digo de barras
		tCodigoBarras = new JTextField(30);
		tCodigoBarras.setFont(new Font("Arial",Font.BOLD,25));
		tCodigoBarras.addActionListener(this);
		tCodigoBarras.setBounds(21,50,600,40);
		p1.add(tCodigoBarras);
		
		
		// label c�digo barras
		lCodigoBarras = new JLabel("C�DIGO DE BARRAS");
		lCodigoBarras.setBounds(21,30,150,20);
		p1.add(lCodigoBarras);
		
		// label quantidade
		lQuantidade = new JLabel("QUANTIDADE");
		lQuantidade.setBounds(655,30,100,20);
		p1.add(lQuantidade);
		
		// TextField da quantidade de itens
		tQuantidade = new JTextField("1"); // come�a com o valor 1 mas pode ser alterado no tempo de execu��o
		tQuantidade.setFont(new Font("Arial", Font.BOLD, 25));
		tQuantidade.addActionListener(this);
		//FocusListener gera a��o quando recebe ou perde o foco, conferir o m�todo para a funcionalidade
		tQuantidade.addFocusListener(this);
		tQuantidade.setBounds(655,50,160,40);
		tQuantidade.setHorizontalAlignment(tQuantidade.CENTER); // setando o valor do textField centralizado
		p1.add(tQuantidade);
		
		
		// table central de vendas com scrollPane
		// adicionando colunas
		tableModel = new DefaultTableModel();
		tableModel.addColumn("Item");
		tableModel.addColumn("Descri��o");
		tableModel.addColumn("Qtd");
		tableModel.addColumn("Valor");
		tableModel.addColumn("SubTotal");

		 
		tabela = new JTable(tableModel);
		
		// Definindo as celulas do table n�o arrast�veis e n�o redimension�veis
	 	tabela.getTableHeader().setResizingAllowed(false);
	 	tabela.getTableHeader().setReorderingAllowed(false);
	 	
	 	// tabela n�o editabel
	 	tabela.setEnabled(false);
	 	
	 	// definindo o valor das c�lulas centralizado
	 	
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
		
		// setando scrollPane com rolamento autom�tico atrav�s do m�todo adjustmentListener
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
		 
		 lValorUnitario = new JLabel("VALOR UNIT�RIO");
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
		 
		 
		 
		//m�todo para ligar o banco de dados com a aplica��o
		carregaResultSet();
		
	
		
		
		
		
	} // fim do construtor
	
	public void actionPerformed(ActionEvent e)
	{
		
		//item usado para receber o numero do item na op��o retirar item
		int numeroItem = 0;
		
		//objeto usado para receber valores da table na hora de fazer consulta na pr�pria
		Object objeto;
		
		// toda a��o volta o foco para tCodigoBarras para o usu�rio n�o ter que voltar manualmente
		tCodigoBarras.requestFocus();
		
		
		// boleano usado no while para verificar validade de valores digitados;
		boolean valorInvalido = false;
	

		
		
		
		if(e.getSource() == tCodigoBarras)
		{
			// testando validade dos valores quantidade e c�digo
			try
			{
				//atribuindo todos os c�digos adicionados para dar baixa dos itens o banco posteriormente e testando
				// validade de valores
				codigo[totalItens] = Integer.parseInt(tCodigoBarras.getText());
				
			}
			catch(NumberFormatException erro)
			{
				JOptionPane.showMessageDialog(null,"Valor inv�lido em c�digo de barras","Aviso",JOptionPane.WARNING_MESSAGE);
				tCodigoBarras.setText("");
				return;
			}
				
			//zerar c�digo de barras ap�s ler seu valor
			tCodigoBarras.setText("");
			
			
			
			//pesquisando o item no banco de dados
			try
			{
				
				
				resultSet = statement.executeQuery("SELECT * FROM produtos WHERE codigo ='"+codigo[totalItens]+"'");
				resultSet.next();
		
				//pegando descri��o e valor do banco baseado no c�digo
				dados[1] = resultSet.getString("descricao");
				preco = resultSet.getFloat("preco_venda");
				unidade = resultSet.getString("unidade");
				lucro = resultSet.getFloat("preco_venda") - resultSet.getFloat("preco_custo") ;
				
				
	
					
			} // fim try
			catch(SQLException erro)
			{
				JOptionPane.showMessageDialog(null,"Produto n�o encontrado");
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
				
				JOptionPane.showMessageDialog(null,"Valor inv�lido em quantidade","Aviso",JOptionPane.WARNING_MESSAGE);
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
			

			

			//atribuindo valor ao vetor que adicionar� a linha na table
			dados[0] = ""+(totalItens +1);
			dados[3] = "R$" + df.format(preco);
			dados[4] = "R$"+ df.format(subTotal);
			
		
			tableModel.addRow(dados);
			
			
			
			// adiciona valor aos textFields da aplica��o
			tValorUnitario.setText("R$"+df.format(preco));
			
			tSubTotal.setText("R$"+df.format(subTotal));
			

			totalAPagar += subTotal;
			
			//variavel para fazer outra inser��o no banco, no historico de vendas
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
			
			// se finalizar venda com nenhum item vendido, n�o entra no m�todo
			if(totalItens == 0)
			{
				JOptionPane.showMessageDialog(null,"Venda ainda n�o iniciada","Aviso",JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			
			//Loop para quanto o usu�rio digitar alguma op��o inv�lida a janela voltar sozinha
			do
			{
			
				// JOptionPane para receber o tipo de pagamento em string
				aux = JOptionPane.showInputDialog("Digite a Forma de Pagamento \n1-Dinheiro \n2-Cart�o de D�bito"+
					"\n3-Cart�o de Cr�dito \n4-Cheque");
					
				// verifica se o usu�rio selecionou cancelar e sai do m�todo
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
					JOptionPane.showMessageDialog(null,"Op��o Inv�lida","Aviso",JOptionPane.WARNING_MESSAGE);
					valorInvalido = true;
				} // fim else
		
			}while(valorInvalido); // fim do while que repetem caso a op��o seja inv�lida
				
			// switch case para verificar o tipo de pagamento
			switch(opcaoPagamento)
			{
				
				// em cada case ir� exibir um JOptionPane para verificar se o pagamento ser� total ou parcial 
				// na forma de pagamento selecionada
				
				case 1: // pagamento em dinheiro
					metodoPagamento("Dinheiro",1);
					
					
					
				break; // fim switch dinheiro
					
				case 2: // d�bito
					metodoPagamento("D�bito",2);
					
				break; // fim switch d�bito
				
				case 3: // cr�dito
					
					metodoPagamento("Cr�dito",3);
					
			
				break; // fim switch cr�dito
				
				case 4: // cheque
					metodoPagamento("Cheque",4);
				
			} // fim switch opcaopagamento
		
		return;	
		} // fim if bFinalizarVenda
		
		
		
		if(e.getSource() == bCancelarVenda)
		{
			// essa a��o requer confirma��o de senha
			if(!verificarSenha())
			{
				// se o m�todo retornar false ele entra aqui e sai do m�todo actionPerformed
				return;
			}
			
			
			
			// verifica se a venda foi iniciada antes de cancelarVenda
			if(totalItens <=0)
			{
				JOptionPane.showMessageDialog(null,"Venda N�o Iniciada","Aviso",JOptionPane.INFORMATION_MESSAGE);
				return;	
			}
			
			opcao = JOptionPane.showOptionDialog(null,
				"Deseja mesmo Cancelar a Venda?", "Cancelar",
				JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,
				null,botoes,botoes[0]);
			if(opcao == JOptionPane.YES_OPTION)
			{
				
				// zera variaveis, textField e a table
				
				
				// c�digo para zerar a table
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
			// essa a��o requer confirma��o de senha
			if(!verificarSenha())
			{
				// se o m�todo retornar false ele entra aqui e sai do m�todo actionPerformed
				return;
			}
			
			
			
			
			// verifica se a venda foi iniciada ou se possui apenas um item antes de remover Item
			if(totalItens <=0)
			{
				JOptionPane.showMessageDialog(null,"Venda N�o Iniciada","Aviso",JOptionPane.INFORMATION_MESSAGE);
				return;	
			}
			else if(totalItens == 1)
			{
				JOptionPane.showMessageDialog(null,"S� um item adicionado cancele a venda","Aviso",JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			
			// loop para repetir quando o usuario digita um valor inv�lido
			do
			{
				// variavel para verificar se o usuario digita um valor valido
				valorInvalido = false;
				
				aux = JOptionPane.showInputDialog("Digite o N�mero do Item");
				// verifica se o usuario selecionou cancelar e sai da aplica��o
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
					JOptionPane.showMessageDialog(null,"C�digo Inv�lido", "Aviso",JOptionPane.WARNING_MESSAGE);
					valorInvalido = true;
				}
				
				
				
				
			}while(valorInvalido);
			// diminui uma unidade porque no array come�a do 0 , e a lista come�a no 1, ent�o 1 representa 0
			// por exemplo
			numeroItem--;
			
	
			
			try
			{
				
				//pagando valores de lucro no banco para subtrair na vari�vel totalLucro
				resultSet = statement.executeQuery("SELECT * FROM produtos WHERE codigo ='"+codigo[numeroItem]+"'");
				resultSet.next();

				lucro = resultSet.getFloat("preco_venda") - resultSet.getFloat("preco_custo") ;
				
				
				
	
					
			} // fim try
			catch(SQLException erro)
			{
				System.out.println("Erro item n�o encontrado");
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
			
			
			//retirando das vari�veis que mandam dados para a tabela vendas
			
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
			// numeroItem digitado pelo usu�rios
			JOptionPane.showMessageDialog(null,
			"Retirado \n\nItem: "+tabela.getModel().getValueAt(numeroItem,0).toString()+
			"\nDescri��o: "+tabela.getModel().getValueAt(numeroItem,1).toString()+
			"\nQuantidade: " + tabela.getModel().getValueAt(numeroItem,2).toString()+
			"\nSubTotal: " + aux , "Aviso",JOptionPane.WARNING_MESSAGE);
		
			
			tableModel.removeRow(numeroItem);

			
			// loop para retirar o item selecionado do array codigo voltando posi��es � frente casa por casa
			for(int i = numeroItem; i < totalItens; i++)
			{
			
				codigo[i] = codigo[i+1];	
			
			}
			

			
			// diminuindo total itens pois foi retirado um
			totalItens--;
			
			// setando novos valores em n�mero do item
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
             
             /* s� se abrir� essa janela ap�s a confirma��o da senha
              * convertendo o valor para double com try catch
              */
              
             // loop para repeti��o quando h� valores inv�lidos
             do
             {
             
	             aux = JOptionPane.showInputDialog("Digite o Valor");
	             
	             // selecionou cancelar? se sim saia do m�todo
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
	             
	             // verifica se o valor esta negativo por inser��o ou se caiu na exce��o acima e recebeu -1
	             if(valorConversao < 0) 
	             {
	             	JOptionPane.showMessageDialog(null,"Valor Inv�lido", "Aviso",JOptionPane.WARNING_MESSAGE);
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
			
			// verifica se a compra n�o est� em andamento para sair
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
	
	
	// m�todo utilizado pelo metodoPagamento
	public double metodoPagamentoParcial(String tipo, int valorTipo)
	{
		
		
		// loop para repetir caso o valor seja inv�lido
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
				// se o usuario digitar um valor inv�lido ele vira negativo para ficar no loop
				valorConversao = -1.0;
			}
				
				
				
			// enquanto o valor for negativo ou maior ou igual que o valor total da compra o loop permanece
			if(valorConversao < 0 )
				JOptionPane.showMessageDialog(null,"Valor Inv�lido", "Aviso",JOptionPane.WARNING_MESSAGE);
				
			if(valorConversao > totalAPagar)
				JOptionPane.showMessageDialog(null,"Valor Maior Que o Total", "Aviso",JOptionPane.WARNING_MESSAGE);
					
			if(valorConversao == totalAPagar)
			{
				
				JOptionPane.showMessageDialog(null,"Valor Igual ao Total, \nSelecione Pagamento Total",
				 "Aviso",JOptionPane.WARNING_MESSAGE);
				 return(0);
			}	
		
			
			
			
		}while(valorConversao < 0 || valorConversao >= totalAPagar);
		
		
		
			// op��o que verifica se o pagamento � em cheque ou dinheiro para voltar o troco
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
						// se o usuario digitar um valor inv�lido ele vira negativo para ficar no loop
						troco = -1.0;
					}
						
						
						
					// enquanto o valor for negativo ou menor que o valor parcial a pagar o loop permanece
					if(troco < 0 )
						JOptionPane.showMessageDialog(null,"Valor Inv�lido", "Aviso",JOptionPane.WARNING_MESSAGE);
						
					if(troco < valorConversao)
						JOptionPane.showMessageDialog(null,"Valor Menor que Valor a Pagar", "Aviso",JOptionPane.WARNING_MESSAGE);	
				
					
					
					
				}while(troco < 0 || troco < valorConversao);
				
				// calcula o troco 
				troco = troco - valorConversao;
				
				JOptionPane.showMessageDialog(null,"Troco: R$"+df.format(troco), "Aviso",JOptionPane.WARNING_MESSAGE);
				
			} // fim if
		
		
		// verifica se o pagamento foi bem sucedido
		opcao = JOptionPane.showOptionDialog(null,
		"Pagamento bem Sucedido? \nA Opera��o n�o poder� ser desfeita depois", "Fechar",
		JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,
		null,botoes,botoes[0]);
		
		
		// sen�o retorna zero
		if(opcao == JOptionPane.NO_OPTION)
		{
			return(0);
		}
		// retorna o valor para a aplica��o 0.0 se for selecionado cancelar ou o valor do pagamento parcial for negativo
		return(valorConversao);
		
		
	
		
	} // fim m�todo metodoPagamentoParcial
	
	
	
	
	// m�todo executa as fun��es pertinentes ao pagamento, perguntando o tipo e atribuindo os resultados
	// ao banco de dados
	public void metodoPagamento(String tipo, int valorTipo)
	{

		boolean valorInvalido;
		int opcaoPagamento = 0;
		// loop para repetir caso uma op��o seja inv�lida
		do
		{
					
			aux = JOptionPane.showInputDialog("Pagamento em "+tipo+"\n\n1-Pagamento Total \n2-Pagamento Parcial");
						
			// verifica se o usu�rio selecionou cancelar e sai do m�todo
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
				JOptionPane.showMessageDialog(null,"Op��o Inv�lida","Aviso",JOptionPane.WARNING_MESSAGE);
				valorInvalido = true;
							
			} // fim else
			
		}while(valorInvalido);
		
			
		
		if(opcaoPagamento == 1) // pagamento total
		{
			
			// op��o que verifica se o pagamento � em cheque ou dinheiro para voltar o troco
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
						// se o usuario digitar um valor inv�lido ele vira negativo para ficar no loop
						valorConversao = -1.0;
					}
						
						
						
					// enquanto o valor for negativo ou menor que o total da compra o loop permanece
					if(valorConversao < 0 )
						JOptionPane.showMessageDialog(null,"Valor Inv�lido", "Aviso",JOptionPane.WARNING_MESSAGE);
						
					if(valorConversao < totalAPagar)
						JOptionPane.showMessageDialog(null,"Valor Menor que o Total", "Aviso",JOptionPane.WARNING_MESSAGE);	
				
					
					
					
				}while(valorConversao < 0 || valorConversao < totalAPagar);
				
			// calcula o troco 
			troco = valorConversao - totalAPagar;
			
			JOptionPane.showMessageDialog(null,"Troco: R$"+df.format(troco), "Aviso",JOptionPane.WARNING_MESSAGE);
				
			} // fim if
			
				
			// verifica se o pagamento foi bem sucedido
			opcao = JOptionPane.showOptionDialog(null,
			"Pagamento bem Sucedido? \nA Opera��o n�o poder� ser desfeita depois", "Fechar",
			JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,
			null,botoes,botoes[0]);
				
			if(opcao == JOptionPane.YES_OPTION)
			{
	
	
				
				//m�todo que da baixa nos itens do banco e acrescentar o valor da compra ao caixa
				lancarCaixa(valorTipo, totalAPagar);
				baixaItens();
				
				
				//setando historico de vendas no banco
				setandoHistoricoVendas();
				
				
				// c�digo para zerar a table
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
			// chama o m�todo com os par�metro tipo e valorTipo, que ja era um parametro deste metodoPagamento
			// o metodo tem retorno double
			pagamentoParcial = metodoPagamentoParcial(tipo, valorTipo);
			
		 
		 	totalAPagar -= pagamentoParcial;
		 	lTotal.setText("R$"+df.format(totalAPagar));
		 	
		 	//m�todo que da baixa nos itens do banco e acrescentar o valor da compra ao caixa
			lancarCaixa(valorTipo, pagamentoParcial);
			
			
	
	
		 
		
	
		} // fim else
	
	
	
	} // fim m�todo metodoPagamento
	
	
	// esse m�todo faz com que quando o usu�rio colocar o foco no textField tQuantidade ele zere automaticamente
	public void focusGained(FocusEvent e)
	{
		if(e.getSource() == tQuantidade)
		{
			tQuantidade.setText("");
		}
		
	} // fim do focusGained
	
	
	
	// esse m�todo por enquanto n�o � usado para nada, s� � colocado para n�o dar erro
	public void focusLost(FocusEvent e)
	{
		
	
	} // fim do m�todo focusLost
	
	// m�todo do adjustmentListener utilizado para rolar automaticamente JScrollPane
	public void adjustmentValueChanged(AdjustmentEvent e)
	{	
		
		/* verifica se o usu�rio est� inserindo itens na compra
		 *pois assim sendo, o contador sera diferente de totalItens,
		 *pois quando as duas vari�veis s�o iguais significa que o usuario
		 *esta acessando este m�todo aqui por apenas rolar a barra de JScrollPane.
		 *Sem este if, seria imposs�vel rolar a barra para cima pois toda vez
		 *iria chama o m�todo abaixo para rolar a barra no maximo de comprimento
		 */
		if(contador != totalItens)
		{
			
			contador = totalItens;
			e.getAdjustable().setValue(e.getAdjustable().getMaximum());
		}
		  
	} // fim adjustmentListener

	
	// algumas a��es da classe requerem senha, esse m�todo evita a escrita de todo esse c�digo mais de uma vez
	public boolean verificarSenha()
	{
			String senhaAdmin ="";
			try
			{
				// s� � poss�vel dar baixa no caixa com a senha do admin
				resultSet = statement.executeQuery("SELECT * FROM usuarios WHERE login = 'admin'");
				resultSet.next();
				
				senhaAdmin = resultSet.getString("senha");
			
					
			} // fim try
			catch(SQLException erro)
			{
			}
	
			
			// loop para senha inv�lida
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
	             
	             // verifica se o usu�rio digitou uma senha inv�lida
	             if(!senha.equals(senhaAdmin)) // colocar aqui as senhas v�lidas
	             {
	             	password.setText("");
	             	JOptionPane.showMessageDialog(null,"Senha inv�lida", "Aviso",JOptionPane.WARNING_MESSAGE);
	             }
	             
	             
             }while(!senha.equals(senhaAdmin)); // colocar aqui as senhas v�lidas
             
             
             return(true);
             
	} // fim verificarSenha()
	
	//m�todo usado para ligar o banco de dados com a aplica��o
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
		// fazer a verifica��o do tipo de pagamento para mandar para o banco no seu respectivo campo
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


	
	} // fim m�todo lancarCaixa
	
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

		//zerando as vari�veis
		totalVendido = 0.0;
		totalLucro  = 0.0;
		totalUnidades = 0.0;
	

		
	} // fim setandoHistorioVendas
	
	
	

				
	
} // fim da classe