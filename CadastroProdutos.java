import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.sql.*;


public class CadastroProdutos extends JFrame implements ActionListener, FocusListener, DocumentListener, ListSelectionListener
{
	
	
	// objetos para usar banco de dados
	ResultSet resultSet;
	Statement statement;
	
	// int usado para contar o número de cadastros no banco de dados
	int numeroCadastros;
	
	
	//string usada para auxiliar o carregamento de dados do banco para JTable
	String dados[] = new String[3];
	
	// int usado para auxiliar index
	int index2;
	
	
	// string para receber valores de JOptionPane
	String aux;
	
	// double e int para dar entrada no estoque
	double dEntradaEstoque;
	int iEntradaEstoque;

	
	// tableRowSorter usado para pesquisar na table por text
	TableRowSorter<TableModel> organizador; 
		
	// int para ser usado na pesquisa da table, para ver o item selecionado
	int index ;
	

		
	// formato decimal para exibir com duas casas decimais
	
	DecimalFormat exibirValor, exibirPorcentagem;
	
	
	// variáveis para calculos
	double varPrecoCusto, varPrecoVenda, varMargemLucro;
	
	// váriaveis para fazer conversão de valores e detecção de erros
	int intCodigoBarras, intEstoqueCritico;
	
	// strings para  usar a função replace
	String sTPrecoVenda, sTPrecoCusto, sTMargemLucro;
	
	
	// menu, menuitem, menuBar
	
	JMenuBar barraMenu;
	JMenuItem iSair;
	JMenu mSair;
	
	// painel
	
	JPanel p1;
	
	
	
	// table
	JTable tabelaProdutos;
	DefaultTableModel tableModel;
	// scroll para table
	JScrollPane scrollPane;
	
	
	// label e textFields
	JTextField tPesquisar;
	
	JLabel lDescricaoPesquisa, lResultado;
		
	JTextField tCodigo, tDescricao,tFornecedor, tCodigoBarras, tPrecoCusto, tMargemLucro, tPrecoVenda,
		tEstoqueCritico, tQuantidadeEstoque, tSituacao;
		
	JLabel lCodigo, lDescricao, lUnidade, lFornecedor, lCodigoBarras, lPrecoCusto, lMargemLucro, lPrecoVenda,
		lEstoqueCritico, lQuantidadeEstoque, lSituacao;
		
	// JComboBox
	
	JComboBox cUnidade;
	String unidade[] = {"","UN","KG"};
	
	
	
	
	
	// botoes
	
	JButton bAlterar, bCancelar, bSalvar, bExcluir, bLimpar, bNovo, bEntradaEstoque, bCorrecaoEstoque;
	
	CadastroProdutos()
	{
		setTitle("Cadastro de Produtos");
		setVisible(true);
		
		
		setSize(730,670);
		setLocationRelativeTo(null);
		setResizable(false);
		
		
		//setando icone
		
		setIconImage(new ImageIcon("Imagens/cad_pro.png").getImage());
		
		

		
		// paineis
		
		p1 = new JPanel();
		p1.setLayout(null);
		p1.setBackground(new Color(192,192,192));
		getContentPane().add(p1);
		
		
		
		
		// menus
		
		barraMenu = new JMenuBar();
		
		mSair = new JMenu("Sair");
			
		
		iSair = new JMenuItem("Sair");
		iSair.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0));
		iSair.addActionListener(this);
		
		
		mSair.add(iSair);
		barraMenu.add(mSair);
		setJMenuBar(barraMenu);
		
		// labels e textFields
	
		// codigo item	
		lCodigo = new JLabel("Código Item");
		lCodigo.setBounds(10,10,70,20);
		p1.add(lCodigo);
		
		tCodigo = new JTextField(3);
		tCodigo.setEditable(false);
		tCodigo.setBounds(10,30,70,30);
		p1.add(tCodigo);
		
		// descricao
		
		lDescricao = new JLabel("Descrição");
		lDescricao.setBounds(90,10,70,20);
		p1.add(lDescricao);
		
		tDescricao  = new JTextField(30);
		tDescricao.setBounds(90,30,265,30);
		tDescricao.setEditable(false);
		p1.add(tDescricao);
		
		// fornecedor
		
		lFornecedor = new JLabel("Fornecedor");
		lFornecedor.setBounds(365,10,70,20);
		p1.add(lFornecedor);
		
		tFornecedor = new JTextField(30);
		tFornecedor.setEditable(false);
		tFornecedor.setBounds(365,30,265,30);
		p1.add(tFornecedor);
		
		// unidade de medida
		
		lUnidade = new JLabel("Unidade");
		lUnidade.setBounds(10,70,70,20);
		p1.add(lUnidade);
		
		cUnidade = new JComboBox(unidade);
		cUnidade.setEnabled(false);
		cUnidade.setBounds(10,90,70,30);
		p1.add(cUnidade);
		
		
		
		// código barras
		
		lCodigoBarras = new JLabel("Código de Barras");
		lCodigoBarras.setBounds(90,70,100,20);
		p1.add(lCodigoBarras);
		
		tCodigoBarras = new JTextField(15);
		tCodigoBarras.setEditable(false);
		tCodigoBarras.addFocusListener(this);
		tCodigoBarras.setBounds(90,90,200,30);
		p1.add(tCodigoBarras);
		
		// precocusto
		
		lPrecoCusto = new JLabel("Preço de Custo");
		lPrecoCusto.setBounds(300,70,100,20);
		p1.add(lPrecoCusto);
		
		tPrecoCusto	 = new JTextField(8);
		tPrecoCusto.addFocusListener(this);
		tPrecoCusto.setEditable(false);
		tPrecoCusto.setBounds(300,90,100,30); 
		p1.add(tPrecoCusto);
		
		// margem de lucro
		
		lMargemLucro = new JLabel("Margem de Lucro");
		lMargemLucro.setBounds(410,70,110,20);
		p1.add(lMargemLucro);
		
		tMargemLucro = new JTextField(8);
		tMargemLucro.setEditable(false);
		tMargemLucro.addFocusListener(this);
		tMargemLucro.setBounds(410,90,110,30);
		p1.add(tMargemLucro);
		
		// preço venda
		
		lPrecoVenda = new JLabel("Preço de Venda");
		lPrecoVenda.setBounds(530,70,130,20);
		p1.add(lPrecoVenda);
		
		tPrecoVenda = new JTextField(8);
		tPrecoVenda.addFocusListener(this);
		tPrecoVenda.setEditable(false);
		tPrecoVenda.setBounds(530,90,100,30);
		p1.add(tPrecoVenda);
		
		// Estoque Critico
		
		lEstoqueCritico = new JLabel("Estoque Crítico");
		lEstoqueCritico.setBounds(10,130,100,20);
		p1.add(lEstoqueCritico);
		
		tEstoqueCritico = new JTextField(3);
		tEstoqueCritico.addFocusListener(this);
		tEstoqueCritico.setEditable(false);
		tEstoqueCritico.setBounds(10,150,100,30);
		p1.add(tEstoqueCritico);
		
		// quantidade em estoque
		
		lQuantidadeEstoque = new JLabel("Quantidade em Estoque");
		lQuantidadeEstoque.setBounds(120,130,150,20);
		p1.add(lQuantidadeEstoque);
		
		tQuantidadeEstoque = new JTextField();
		tQuantidadeEstoque.setEditable(false);
		tQuantidadeEstoque.setBounds(120,150,100,30);
		p1.add(tQuantidadeEstoque);
		
		// situação em estoque
		
		lSituacao = new JLabel("Situação Estoque");
		lSituacao.setBounds(270,130,150,20);
		p1.add(lSituacao);
		
		tSituacao = new JTextField();
		tSituacao.setEditable(false);
		tSituacao.setBounds(270,150,100,30);
		p1.add(tSituacao);
		
		
		
		
		// botões
		
	
		bAlterar = new JButton("Alterar");
		bAlterar.setMnemonic(KeyEvent.VK_A);
		bAlterar.addActionListener(this);
		bAlterar.setEnabled(false);
		bAlterar.setBounds(10,190,100,30);
		p1.add(bAlterar);
		
		bCancelar = new JButton("Cancelar");
		bCancelar.setMnemonic(KeyEvent.VK_C);
		bCancelar.addActionListener(this);
		bCancelar.setEnabled(false);
		bCancelar.setBounds(120,190,100,30);
		p1.add(bCancelar); 
			
		bSalvar = new JButton("Salvar");
		bSalvar.setMnemonic(KeyEvent.VK_S);
		bSalvar.setEnabled(false);
		bSalvar.addActionListener(this);
		bSalvar.setBounds(230,190,100,30);
		p1.add(bSalvar);
		
		bExcluir = new JButton("Excluir");
		bExcluir.setMnemonic(KeyEvent.VK_E);
		bExcluir.setEnabled(false);
		bExcluir.addActionListener(this);
		bExcluir.setBounds(340,190,100,30);
		p1.add(bExcluir);
		
		bLimpar = new JButton("Limpar");
		bLimpar.setMnemonic(KeyEvent.VK_L);
		bLimpar.setEnabled(false);
		bLimpar.addActionListener(this);
		bLimpar.setBounds(10,230,100,30);
		p1.add(bLimpar);
		
		bNovo = new JButton("Novo");
		bNovo.setMnemonic(KeyEvent.VK_N);
		bNovo.addActionListener(this);
		bNovo.setBounds(120,230,100,30);
		p1.add(bNovo);
		
		// botao para entrada no estoque
		bEntradaEstoque = new JButton("Entrada Estoque");
	    bEntradaEstoque.setMnemonic(KeyEvent.VK_T);
		bEntradaEstoque.addActionListener(this);
		bEntradaEstoque.setEnabled(false);
		bEntradaEstoque.setBounds(230,230,140,30);
	    p1.add(bEntradaEstoque);
	    
	    // botao para correção de estoque
		bCorrecaoEstoque = new JButton("Correção Estoque");
	    bCorrecaoEstoque.setMnemonic(KeyEvent.VK_O);
		bCorrecaoEstoque.addActionListener(this);
		bCorrecaoEstoque.setEnabled(false);
		bCorrecaoEstoque.setBounds(380,230,140,30);
	    p1.add(bCorrecaoEstoque);
		
	
		 // barra pesquisa
		 lDescricaoPesquisa = new JLabel("Pesquisa");
		 lDescricaoPesquisa.setBounds(10,270,150,20);
		 p1.add(lDescricaoPesquisa);
		
		 tPesquisar = new JTextField(6);
		 tPesquisar.setBounds(10,290,300,30);
		 // adicionando documentListener
		 tPesquisar.getDocument().addDocumentListener(this);
		 p1.add(tPesquisar);
		 
		 //label resultado
		 
		 lResultado = new JLabel("Resultado");
		 lResultado.setBounds(10,330,150,20);
		 p1.add(lResultado);
		 
		 
		 //table resultado
		 // sobreescrever este método permite a table não ser editável, e ainda sim ser selecionável
		 tableModel = new DefaultTableModel(){

		 
		 //sobreescrita
		 @Override
		 public boolean isCellEditable(int row, int column)
		 {
		 	return false;
		 }
		 };
	
		 
		 tabelaProdutos = new JTable(tableModel);
		 tableModel.addColumn("Código");
		 tableModel.addColumn("Descrição");
		 tableModel.addColumn("Situação");
		 
		 // adicionando listSelectionListener à JTable
		 tabelaProdutos.getSelectionModel().addListSelectionListener(this);
		 
		 
		 // definindo apenas uma linha selecionável
		 tabelaProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		 
		 
		 // Definindo as celulas do table não arrastáveis e não redimensionáveis
		 tabelaProdutos.getTableHeader().setResizingAllowed(false);
		 tabelaProdutos.getTableHeader().setReorderingAllowed(false);
		 
		 
		 
		 
		 // definindo o valor das células centralizado
		 DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		 centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		 	
		 tabelaProdutos.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		 tabelaProdutos.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		 tabelaProdutos.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

		 
		 // definindo a largura das colunas da table
		 tabelaProdutos.getColumnModel().getColumn(0).setPreferredWidth(60);
		 tabelaProdutos.getColumnModel().getColumn(1).setPreferredWidth(400);
		 tabelaProdutos.getColumnModel().getColumn(2).setPreferredWidth(100);

		 
		 
		 // tableRowSorter usado para pesquisar na table por text
		 organizador = new TableRowSorter<>(tabelaProdutos.getModel());
		 tabelaProdutos.setRowSorter(organizador);
		 
		 
		 // scrollpane
		 scrollPane = new JScrollPane(tabelaProdutos);
		 scrollPane.setBounds(10,350,560,220);
		 p1.add(scrollPane); 
		 
		
		// decimal format
		 exibirValor = new DecimalFormat();
		 exibirValor.applyPattern("######0.00");
		 
		 exibirPorcentagem = new DecimalFormat();
		 exibirPorcentagem.applyPattern("#0.00");
		
		
		//método para ligar o banco de dados com a aplicação
		carregaResultSet();
		
		//método para carregar a table com o dados do banco
		carregaTable();

	
	
	} // fim do construtor
	
	
	
	public void actionPerformed(ActionEvent e)
	{

		
		
		if(e.getSource() == iSair)
		{
			int opcao;
			Object[]botoes = {"Sim", "Não"};
			opcao = JOptionPane.showOptionDialog(null,
				"Deseja mesmo Sair?", "Fechar",
				JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,
				null,botoes,botoes[0]);
			if(opcao == JOptionPane.YES_OPTION)
				setVisible(false);
		} // fim if sair
		
		
		
		if(e.getSource() == bCorrecaoEstoque)
		{

			// verifica se o usuario selecionou alguma coisa, se não ele sai do método sem abrir a janela
			// já que para abrir a mesma precisa-se selecionar um produto
			if(index == -1)
			{
				JOptionPane.showMessageDialog(null,"Selecione um Produto", "Aviso", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
		
			
			
			// verifica se a unidade de medida do produto é KG ou UN
			// para fazer a verificação correta de inserção KG = double, UN = int
			if(cUnidade.getSelectedIndex() == 1)
			{
				
				do
				{
					aux = JOptionPane.showInputDialog("Digite a nova quantidade");
					// verifica se o usuário cancelou
					if(aux == null)
					{
						return;
						
					}
					
					// verifica se o valor é válido
					try
					{
						iEntradaEstoque = Integer.parseInt(aux);
					}
					catch(NumberFormatException erro)
					{
					
						iEntradaEstoque = -1;
					}
				
					if(iEntradaEstoque < 0)
							JOptionPane.showMessageDialog(null,"Valor inválido","Aviso",JOptionPane.WARNING_MESSAGE);
					
					
				}while(iEntradaEstoque < 0);
				
				dEntradaEstoque = 0;
			} // fim if
			else // else que verifica a unidade de medida UN ou KG
			{
				do
				{
					aux = JOptionPane.showInputDialog("Digite a nova quantidade");
					// verifica se o usuário cancelou
					if(aux == null)
					{
						return;
						
					}
					// replace de vírgula
					aux = aux.replace(',','.');
					
					// verifica se o valor é válido
					try
					{
						dEntradaEstoque = Double.parseDouble(aux);
					}
					catch(NumberFormatException erro)
					{
						
						dEntradaEstoque = -1;
					}
					
					if(dEntradaEstoque < 0)	
						JOptionPane.showMessageDialog(null,"Valor inválido","Aviso",JOptionPane.WARNING_MESSAGE);
				}while(dEntradaEstoque < 0); 
					
				iEntradaEstoque = 0;	
			} // fim else
				
				
			// uma varíavel é para conversão de inteiro no caso de unidade e outra para conversão de double
			// no caso de KG, tudo é somado na double dEntradaEstoque no final, porém a variável que não foi usada	 
			// para receber valor do teclado foi previamento zerada
			dEntradaEstoque += iEntradaEstoque;
				
					
				
					
					
			//Adicionando no banco o novo valor
			try
			{
				String sql = "UPDATE produtos SET quantidade_estoque = '"+dEntradaEstoque+"'"+
						"Where codigo = '"+tCodigo.getText()+"'";
						
				statement.executeUpdate(sql);
		
						
				JOptionPane.showMessageDialog(null,"Valor Alterado com Sucesso","Aviso",
					JOptionPane.WARNING_MESSAGE);
							
				//carrega dados nos textfields
				//limpa e refaz a table
				index2 = index;
				limpaTable();	
				carregaTable();
				//selecionando a jtable aps recriá-la
				tabelaProdutos.setRowSelectionInterval(index2,index2);
				carregarDados();
						

				}
				catch(SQLException erro)
				{
					JOptionPane.showMessageDialog(null,"Erro ao Alterar","Aviso",
						JOptionPane.WARNING_MESSAGE);
				}
				

			
		} // fim bCorrecaoEstoque
		
		if(e.getSource() == bEntradaEstoque)
		{
			
		
			// verifica se o usuario selecionou alguma coisa, se não ele sai do método sem abrir a janela
			// já que para abrir a mesma precisa-se selecionar um produto
			if(index == -1)
			{
				JOptionPane.showMessageDialog(null,"Selecione um Produto", "Aviso", JOptionPane.WARNING_MESSAGE);
				return;
			}
			

			
			
			// verifica se a unidade de medida do produto é KG ou UN
			// para fazer a verificação correta de inserção KG = double, UN = int
			if(cUnidade.getSelectedIndex() == 1)
			{
				do
				{
					aux = JOptionPane.showInputDialog("Digite a quantidade a adicionar");
					// verifica se o usuário cancelou
					if(aux == null)
					{
						return;
						
					}
					
					// verifica se o valor é válido
					try
					{
						iEntradaEstoque = Integer.parseInt(aux);
					}
					catch(NumberFormatException erro)
					{
						
						iEntradaEstoque = -1;
					}
				
					if(iEntradaEstoque < 0)
						JOptionPane.showMessageDialog(null,"Valor inválido","Aviso",JOptionPane.WARNING_MESSAGE);
				}while(iEntradaEstoque < 0);
				
				dEntradaEstoque = 0;
				
			} // fim if
			else // else que verifica a unidade de medida UN ou KG
			{
				do
				{
					aux = JOptionPane.showInputDialog("Digite a quantidade a adicionar");
					// verifica se o usuário cancelou
					if(aux == null)
					{
						return;
						
					}
					// replace de vírgula
					aux = aux.replace(',','.');
					
					// verifica se o valor é válido
					try
					{
						dEntradaEstoque = Double.parseDouble(aux);
					}
					catch(NumberFormatException erro)
					{
						
						dEntradaEstoque = -1;
					}
					
					if(dEntradaEstoque < 0)
						JOptionPane.showMessageDialog(null,"Valor inválido","Aviso",JOptionPane.WARNING_MESSAGE);
				}while(dEntradaEstoque < 0); 
					iEntradaEstoque = 0;
					
			} // fim else	
			// uma varíavel é para conversão de inteiro no caso de unidade e outra para conversão de double
			// no caso de KG, tudo é somado na double dEntradaEstoque no final, porém a variável que não foi usada
			// para receber valor do teclado foi previamento zerada
			dEntradaEstoque += iEntradaEstoque;	
				
			
			// soma o valor atual do banco ao valor adicionado
			double soma = Double.parseDouble(tQuantidadeEstoque.getText());
			dEntradaEstoque += soma;
				
			//Adicionando no banco o novo valor
			try
			{
				String sql = "UPDATE produtos SET quantidade_estoque = '"+dEntradaEstoque+"'"+
					"Where codigo = '"+tCodigo.getText()+"'";
				statement.executeUpdate(sql);
				
				JOptionPane.showMessageDialog(null,"Valor Alterado com Sucesso","Aviso",
					JOptionPane.WARNING_MESSAGE);
					
				//carrega dados nos textfields
				//limpa e refaz a table
				index2 = index;
				limpaTable();	
				carregaTable();
				
				//selecionando a jtable aps recriá-la
				tabelaProdutos.setRowSelectionInterval(index2,index2);
				
				carregarDados();
					
			}
			catch(SQLException erro)
			{
				JOptionPane.showMessageDialog(null,"Erro ao Alterar","Aviso",
					JOptionPane.WARNING_MESSAGE);
			}
				
		
			
			
			
		
			
			return;
		} // fim if bEntradaEstoque
		
	
		if(e.getSource() == bAlterar)
		{
			
			// tornando editavel os textField
			setandoEditable(true);
			// habilitando botoes com parametro 2 
			habilitandoBotoes(2);
				
			
							
				
		} // fim if bAlterar
		
		if(e.getSource() == bCancelar)
		{
			// tornando os textFields não editáveis
			setandoEditable(false);	
				
			// setando botões com parametro 3
			habilitandoBotoes(3);
				
			// verifica se a table está ou não selecionada, -1 significa não selecionada
			// se sim carrega os dados denovo, se não limpa tudo e decrementa a variável codigo
			index = tabelaProdutos.getSelectedRow();
			
			if(index == -1)
			{

				limpar();
				tCodigo.setText("");
			}
			else
			{
				carregarDados();
			}
			

			
		} // fim if bCancelar
		
		
		
		if(e.getSource() == bSalvar)
		{
			
			// usa o método para verificar se todos os campos foram preenchidos
			if(!verificaPreenchimento())
			{
				return;
			}
			
			index = tabelaProdutos.getSelectedRow ();
			
			
			
			
			// verifica se salvar veio de um novo cadastro ou de uma alteração
			// salvando no banco de acordo com a operação insert into novo cadastro ou update cadastroAntigo
			// index == -1 significa que JTable não está selecionada, consequentemente indica um novo cadastro
			if(index == -1)
			{
				try
				{
					String sql ="INSERT INTO produtos (descricao, fornecedor, unidade," +
					 	"codigo_barras, preco_custo, margem_lucro, preco_venda,"+
					 	"estoque_critico) Values ('"+
					 	tDescricao.getText() + "','"+
						tFornecedor.getText() + "','"+
						cUnidade.getSelectedItem().toString() +"','"+
						tCodigoBarras.getText() +"','"+
						varPrecoCusto +"','"+
						varMargemLucro +"','"+
						varPrecoVenda +"','"+
						intEstoqueCritico+ "')";
					 
					 statement.executeUpdate(sql);
					 
					 JOptionPane.showMessageDialog(null,"Gravacao realizada com sucesso");
					 // habilitando botoes com parametro 3
				     habilitandoBotoes(3);
					 // tornando os textFields não editáveis
					 setandoEditable(false);
						
					 // limpando e carregando a table novamente 
					 limpaTable();
					 carregaTable();
					 //selecionando ultimo cadastro jTable
					 int linha = tableModel.getRowCount();
					 
					 tabelaProdutos.setRowSelectionInterval(linha -1,linha -1);
	
					 //carregando novo cadastro
					 carregarDados();
					 
					 
				} // fim try
				catch(SQLException erro)
				{
					JOptionPane.showMessageDialog(null,"Falha no cadastros");
				}
				
				
			} // fim if index == -1
			else
			{
				try
				{
					String sql = "UPDATE produtos SET "+
						"descricao = '" + tDescricao.getText() + "',"+
						"fornecedor = '" + tFornecedor.getText() + "',"+
						"unidade = '" + cUnidade.getSelectedItem().toString() +"',"+
						"codigo_barras = '" + tCodigoBarras.getText() +"',"+
						"preco_custo = '"+ varPrecoCusto +"',"+
						"margem_lucro = '"+ varMargemLucro +"',"+
						"preco_venda = '"+ varPrecoVenda +"',"+
						"estoque_critico = '"+ intEstoqueCritico+ "'"+
						"WHERE codigo = '" +tCodigo.getText()+"'";
					
		
				
					int r = statement.executeUpdate(sql);
				
					if(r==1)
					{
						JOptionPane.showMessageDialog(null, "Alteração realizada com sucesso");
						// habilitando botoes com parametro 3
						habilitandoBotoes(3);
						// tornando os textFields não editáveis
						setandoEditable(false);
						
						// limpando e carregando a table novamente e selecionando-a
						index2 = index;
						limpaTable();
						carregaTable();
		
						//selecionando a jtable aps recriá-la
						tabelaProdutos.setRowSelectionInterval(index2,index2);
						//carregando novo cadastro
						carregarDados();
						
					}
					else
					{
						JOptionPane.showMessageDialog(null,"Problemas na Alteração");
					
					}
					
				}
				catch(SQLException erro)
				{
					JOptionPane.showMessageDialog(null,"Atualização não realizada");
				}
		
		
	
			} // fim else index ==-1

	
	
		} // fim if bSalvar
		
		if(e.getSource() == bExcluir)
		{
			try
			{	
				String nome = "Deseja deletar o Item: "+tCodigo.getText()+"\n"+tDescricao.getText();
					
				int n = JOptionPane.showConfirmDialog(null,nome,"",JOptionPane.YES_NO_OPTION);
				
				if(n == JOptionPane.YES_OPTION)
				{
					String sql = "DELETE FROM produtos WHERE codigo = '"+tCodigo.getText()+"'";
					int r = statement.executeUpdate(sql);
					
					if(r==1)
					{
						JOptionPane.showMessageDialog(null,"Exclusão realizada com sucesso");
	
					}
					else
					{
						JOptionPane.showMessageDialog(null,"Não foi possível excluir o item");
					}
					
		
				} // fim if
			
			} // fim try
			catch(SQLException erro)
			{
			
			}
			//limpando a seleção da table para evitar erros
			tabelaProdutos.getSelectionModel().clearSelection();
			limpaTable();
			carregaTable();
			
			// limpar todos os campos
			tCodigo.setText("");	
			limpar();
			habilitandoBotoes(4);
			//voltando o a cor do background de tSituação para neutra
			tSituacao.setBackground(new Color(238,238,238));
				
				
		} // fim if bExcluir
		
		
		
		if(e.getSource() == bLimpar)
		{
			
			
			limpar();
			


			
			return;
		} // fim if bLimpar
		
		if(e.getSource() == bNovo)
		{

			// tornando os textFields editáveis
			setandoEditable(true);	
			limpar();
			tCodigo.setText("");
			tSituacao.setBackground(new Color(238,238,238));
	

			// habilitando botoes com parametro 1
			habilitandoBotoes(1);
			
			
		} // fim if bNovo
		
		
		
		
	} // fim ActionListener
	
	
	// metodos dos FocusListener uma interface que gera um evento quando um objeto recebe ou perde o foco
	public void focusGained(FocusEvent e)
	{
		
	} // fim do focusGained
	
	
	
	// método gera evento quando campo perde o foco
	// esse método é para fazer o calculo automático do preço de venda baseado na margem de lucro
	public void focusLost(FocusEvent e)
	{	
		
		// trocando caracteres
		
		sTMargemLucro = tMargemLucro.getText();
		sTMargemLucro = sTMargemLucro.replace(",",".");
		sTMargemLucro = sTMargemLucro.replace("%","");

		
		sTPrecoCusto = tPrecoCusto.getText();
		sTPrecoCusto = sTPrecoCusto.replace(",",".");
		sTPrecoCusto = sTPrecoCusto.replace("R","");
		sTPrecoCusto = sTPrecoCusto.replace("$","");

		sTPrecoVenda = tPrecoVenda.getText();
		sTPrecoVenda = sTPrecoVenda.replace(",",".");
		sTPrecoVenda = sTPrecoVenda.replace("R","");
		sTPrecoVenda = sTPrecoVenda.replace("$","");

		
		
		if(e.getSource()== tPrecoCusto)
		{
			try
			{
				
				varPrecoCusto = Double.parseDouble(sTPrecoCusto);		
					
			}
			catch (NumberFormatException erro)
			{
				
				return;
			} // fim catch
			
			tPrecoCusto.setText("R$"+exibirValor.format(varPrecoCusto));
		}
		
		
		// se perder o foco de tMargemLucro já com conversão de valores
		if(e.getSource() == tMargemLucro)
		{	
			
				
			try
			{
				
				varMargemLucro = Double.parseDouble(sTMargemLucro);
				varPrecoCusto = Double.parseDouble(sTPrecoCusto);		
				
				
			}
			catch (NumberFormatException erro)
			{
				
				return;
			} // fim catch
			if(varMargemLucro >= 100)
			{
				JOptionPane.showMessageDialog(null,"Margem de lucro não pode ser maior ou igual a 100%");
				tMargemLucro.requestFocus();
				return;
			}
			
	
			
			//colocando sinal de % apenas no textfield
			tMargemLucro.setText(exibirValor.format(varMargemLucro)+"%");
			
			
			//calculando o preço de venda automaticamente, baseado no preço de custo e margem de lucro
			//Equação para calculo = (preco_custo / (100 - margem_lucro)) *100;
			
			varPrecoVenda = (varPrecoCusto / (100 - varMargemLucro))*100;
			tPrecoVenda.setText("R$"+exibirValor.format(varPrecoVenda));
			
			
		} // fim if tMargemLucro
		
		
		// esse método é para fazer o calculo automático da margem de lucro baseado no preço venda
		// se tPrecoVenda perder o foco
		if(e.getSource() == tPrecoVenda)
		{
			try
			{
				varPrecoVenda = Double.parseDouble(sTPrecoVenda);
				varPrecoCusto = Double.parseDouble(sTPrecoCusto);
				
			}
			catch(NumberFormatException erro)
			{
				return;
			}
			
			tPrecoVenda.setText("R$"+exibirValor.format(varPrecoVenda));
		
			varMargemLucro = (varPrecoVenda - varPrecoCusto) / varPrecoVenda * 100;
			tMargemLucro.setText(exibirPorcentagem.format(varMargemLucro)+"%");
			 // fim else
			
			
		} // fim if tPrecoVenda
		
	
		
		
	} // fim do método focusLost
	
	
	
	// método da interface documentListener esse aqui é só para funcionar não esta sendo usado
	public void changedUpdate(DocumentEvent e)
	{
		
	}
	
	
	/* método da interface documentListener, quando um caractere é inserido
	* usando TableRowSorter para organizar a table conforme pesquisa
	*/
	public void insertUpdate(DocumentEvent e)
	{
		String text = tPesquisar.getText();
		if(text.trim().length() == 0)
		{
			organizador.setRowFilter(null);
		}
		else
		{
			organizador.setRowFilter(RowFilter.regexFilter("(?i)" +text));
		}
			
		
		
		
	} // fim insertUpdate
	
	
	/* método da interface documentListener, quando um caractere é apagado
	 * usando TableRowSorter para organizar a table conforme pesquisa
	 */
	public void removeUpdate(DocumentEvent e)
	{
		String text = tPesquisar.getText();
		
		if(text.trim().length() == 0)
		{
			organizador.setRowFilter(null);
		}
		else
		{
			organizador.setRowFilter(RowFilter.regexFilter("(?i)" + text));
		}
		
	} // fim removeUpdate
	
	// método usado quando um valor é mudado em JTable
	public void valueChanged(ListSelectionEvent e)
	{		
			
			
			// verifica se a table esta selecionada ou se o método valueChanged
			// foi acionado por uma deseleção, e retorna
			if(tabelaProdutos.getSelectedRow() == -1 )
			{
				return;
			}
			
			// verifica se a table foi selecionada durante uma inserção ou alteração de cadastro
			// e não permite o carregamento de dados para o usuário não perder suas alterações
			// antes de salvar ou cancelar
			
			if(!bNovo.isEnabled())
			{
				JOptionPane.showMessageDialog(null, "Salve ou cancele antes de selecionar outro item",
				"Aviso", JOptionPane.WARNING_MESSAGE);
				
				//Limpar a seleção
				tabelaProdutos.getSelectionModel().clearSelection();
				return;
			}

			// habilitando botoes com parametro 3
			habilitandoBotoes(3);
			
			
			// seta os textField false pois se durante uma alteração o usuario selecionar a table
			// ele não pode mais alterar antes de clicar em bAlterar novamente
			setandoEditable(false);
	
			
			carregarDados();
		
	} // fim valueChanged
	
	
	
	/* método para setar os textField editaveis ou não
	 *usado quando o botão alterar ou novo são selecionados
	 *possibilitando alteração de campos
	 */
	public void setandoEditable(boolean editavel)
	{
		tFornecedor.setEditable(editavel);
		tDescricao.setEditable(editavel);
		cUnidade.setEnabled(editavel);
		tEstoqueCritico.setEditable(editavel);
		tMargemLucro.setEditable(editavel);
		tPrecoCusto.setEditable(editavel);
		tPrecoVenda.setEditable(editavel);
		tCodigoBarras.setEditable(editavel);
	}
	
	
	// método que verifica preenchimento para ser usado na hora de salvar
	public boolean verificaPreenchimento()
	{
		
		// retirando e trocando caracteres para calculos
		sTPrecoCusto = tPrecoCusto.getText();
		sTPrecoCusto = sTPrecoCusto.replace(",",".");
		sTPrecoCusto = sTPrecoCusto.replace("R","");
		sTPrecoCusto = sTPrecoCusto.replace("$","");

		sTPrecoVenda = tPrecoVenda.getText();
		sTPrecoVenda = sTPrecoVenda.replace(",",".");
		sTPrecoVenda = sTPrecoVenda.replace("$","");
		sTPrecoVenda = sTPrecoVenda.replace("R","");
		
		sTMargemLucro = tMargemLucro.getText();
		sTMargemLucro = sTMargemLucro.replace(",",".");
		sTMargemLucro = sTMargemLucro.replace("%","");
		
		// verifica algum preenchimento inválido ou vazio e retorna false
		if(tDescricao.getText().equals(""))
		{
			JOptionPane.showMessageDialog(null, "Erro!\nPreenchimento do campo descrição\nobrigatório",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			tDescricao.requestFocus();
			return(false);	
		}	
		
		
		if(tFornecedor.getText().equals(""))
		{
			JOptionPane.showMessageDialog(null, "Erro!\nPreenchimento do campo fornecedor\nobrigatório",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			tFornecedor.requestFocus();
			return(false);
		} 
		
			
		if(cUnidade.getSelectedItem().equals(""))
		{
			JOptionPane.showMessageDialog(null, "Erro!\nPreenchimento do campo unidade\nobrigatório",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			cUnidade.requestFocus();
			return(false);	
		}
		
		if(tEstoqueCritico.getText().equals(""))
		{
			JOptionPane.showMessageDialog(null, "Erro!\nPreenchimento do campo estoque crítico\nobrigatório",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			tEstoqueCritico.requestFocus();
			return(false);	
		}
		
		if(tCodigoBarras.getText().equals(""))
		{
			JOptionPane.showMessageDialog(null, "Erro!\nPreenchimento do campo código de barras\nobrigatório",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			tCodigoBarras.requestFocus();
			return(false);	
		}
		
		
		if(tPrecoCusto.getText().equals(""))
		{
			JOptionPane.showMessageDialog(null, "Erro!\nPreenchimento do campo preço de custo\nobrigatório",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			tPrecoCusto.requestFocus();
			return(false);	
		}
		
		if(tPrecoVenda.getText().equals(""))
		{
			JOptionPane.showMessageDialog(null, "Erro!\nPreenchimento do campo preço de venda\nobrigatório",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			tPrecoVenda.requestFocus();
			return(false);	
		}
		
		
		// conferindo valores campo por campo para alertar sobre o campo especificamente
		
		// código de barras
		try
		{
			intCodigoBarras = Integer.parseInt(tCodigoBarras.getText());
		}
		catch(NumberFormatException erro)
		{
			JOptionPane.showMessageDialog(null, "Erro!\nValor de código de barras inválido",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			tCodigoBarras.requestFocus();
			return(false);	
		}	
		if(intCodigoBarras < 0)
		{
			JOptionPane.showMessageDialog(null, "Erro!\nValor de código de barras inválido",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			tCodigoBarras.requestFocus();
			return(false);
		}
		
		
		// estoque crítico	
		try
		{
			intEstoqueCritico = Integer.parseInt(tEstoqueCritico.getText());
		}
		catch(NumberFormatException erro)
		{
			JOptionPane.showMessageDialog(null, "Erro!\nValor de estoque crítico inválido",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			tEstoqueCritico.requestFocus();
			return(false);	
			
		}
		if(intEstoqueCritico < 0)
		{
			JOptionPane.showMessageDialog(null, "Erro!\nValor de código de estoque crítico negativo",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			tEstoqueCritico.requestFocus();
			return(false);
		}
		
		
		// preco custo	
		try
		{
			varPrecoCusto = Double.parseDouble(sTPrecoCusto);
		}
		catch(NumberFormatException erro)
		{
			JOptionPane.showMessageDialog(null, "Erro!\nValor Preço de custo inválido",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			tPrecoCusto.requestFocus();
			return(false);	
			
		}
		if(varPrecoCusto < 0)
		{
			JOptionPane.showMessageDialog(null, "Erro!\nValor de preço de custo negativo",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			tPrecoCusto.requestFocus();
			return(false);
		}	
		
		// preço venda
		try
		{
			varPrecoVenda = Double.parseDouble(sTPrecoVenda);
		}
		catch(NumberFormatException erro)
		{
			JOptionPane.showMessageDialog(null, "Erro!\nValor Preço de venda inválido",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			tPrecoVenda.requestFocus();
			return(false);	
			
		}
		if(varPrecoVenda < 0)
		{
			JOptionPane.showMessageDialog(null, "Erro!\nValor de preço de venda negativo",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			tPrecoVenda.requestFocus();
			return(false);
		}	
 		
 		
 		// margem de lucro
 		try
 		{
 			varMargemLucro = Double.parseDouble(sTMargemLucro);
 		}
 		catch(NumberFormatException erro)
 		{
 			varMargemLucro = (varPrecoVenda - varPrecoCusto) / varPrecoCusto * 100;
			tMargemLucro.setText(exibirPorcentagem.format(varMargemLucro)+"%");
 		}
 		

		if(tMargemLucro.getText().equals(""))
		{
			
			varMargemLucro = (varPrecoVenda - varPrecoCusto) / varPrecoCusto * 100;
			tMargemLucro.setText(exibirPorcentagem.format(varMargemLucro)+"%");
			
		}
		
		// retorna true se não cair em nenhum if
		return(true);
		
	} // fim verificaPreenchimento


	// precisa-se limpar campos em mais de um lugar, fica mais facil usar um método para isso
	public void limpar()
	{
		tDescricao.setText("");
		cUnidade.setSelectedIndex(0);
		tFornecedor.setText(""); 
		tCodigoBarras.setText(""); 
		tPrecoCusto.setText(""); 
		tMargemLucro.setText("");
		tPrecoVenda.setText("");
		tEstoqueCritico.setText("");
		tQuantidadeEstoque.setText("");
		tSituacao.setText("");
	}
	
	
	// método usado para atribuir dados aos textField
	public void carregarDados()
	{
			
		
		
		
			// verifica o item selecionado
			index = tabelaProdutos.getSelectedRow();
			
			// verifica se o usuario selecionou alguma coisa, se não ele sai do método sem abrir a janela
			// já que para abrir a mesma precisa-se selecionar um produto
			if(index == -1)
			{
				return;
			}
			
			// converte o index, pois uma simples pesquisa ou reorganização da table muda o index
			index = organizador.convertRowIndexToModel(index);
			
			// pegando código da seleção da table para fazer a consulta no banco
			Object objeto = tabelaProdutos.getModel().getValueAt(index,0);
			
			
			// transformando código da JTable para string
			aux = objeto.toString();
			// de string para int
			int codigo = Integer.parseInt(aux);
			
			
			
			try
			{
				resultSet = statement.executeQuery("SELECT * FROM produtos WHERE codigo ='"+codigo+"'");
				resultSet.next();
				
				//setando valores
				tCodigo.setText(""+codigo);
				tDescricao.setText(resultSet.getString("descricao"));
				tFornecedor.setText(resultSet.getString("fornecedor"));
				tCodigoBarras.setText(resultSet.getString("codigo_barras"));
				tPrecoCusto.setText("R$"+resultSet.getString("preco_custo"));
				tMargemLucro.setText(resultSet.getString("margem_lucro")+"%");
				tPrecoVenda.setText("R$"+resultSet.getString("preco_venda"));
				tEstoqueCritico.setText(resultSet.getString("estoque_critico"));	
				tQuantidadeEstoque.setText(resultSet.getString("quantidade_estoque"));
				
				
				// verificando situação e setando cores
				
				objeto = tabelaProdutos.getModel().getValueAt(index,2);
				String situacao = objeto.toString();
				
				tSituacao.setText(situacao);
				
				if(situacao.equals("Indisponível"))
				{
					tSituacao.setBackground(new Color(245,105,105));
				}
				else if(situacao.equals("Crítico"))
				{
					tSituacao.setBackground(new Color(255,253,154));
				}
				else
				{
					tSituacao.setBackground(new Color(238,238,238));
				}
			
			
				
				
				// verifica se é KG ou UN
				if(resultSet.getString("unidade").equals("UN"))
				{
					cUnidade.setSelectedIndex(1);
				}
				else
				{
					cUnidade.setSelectedIndex(2);	
				}
							
			} // fim try
			catch(SQLException erro)
			{
				JOptionPane.showMessageDialog(null,"Produto não encontrado");
				return;
			}
			
			

			
			
	} // fim método carregarDados
	
	
	
	
	
	
	
	/* método usado para habilitar e desabilitar botões
	 *
	 * bNovo ao ser clicado precisa deselecionar a JTable e habilitar bCancelar, bSalvar e bLimpar
	 * ele precisa também desabilitar,bAlterar,Bnovo,bEntradaEstoque e bExcluir , seu parametro é 1
	 *
	 * bAlterar  ao ser clicado habilita e desabilita os mesmos botôes que bNovo 
	 * porém não deseleciona a JTable, seu parametro é 2
	 *
	 * bCancelar e bSalvar ao serem clicados habilitam somente bNovo se a JTable não estiver selecionada
	 * e habilitam bAlterar, bNovo, bEntradaEstoque e bExcluir se a table estiver selecionada,
	 * desabilitam sempre, bSalvar, bCancelar e bLimpar o parametro deles é 3
	 *
	 * quando a table é seleciona ela chama o método com o parametro 3 e faz a mesma coisa que bCancelar e bSalvar
	 * 
	 */
	public void habilitandoBotoes(int parametro)
	{
		// 1 parametro de bNovo e bAlterar
		if(parametro == 1 || parametro == 2)
		{
			// somente bNovo deseleciona JTable
			if(parametro == 1)
			{
			
				// deselecionando JTable
				tabelaProdutos.getSelectionModel().clearSelection();
			}
			
			bCancelar.setEnabled(true);
			bSalvar.setEnabled(true);
			bLimpar.setEnabled(true);
			
			bAlterar.setEnabled(false);
			bNovo.setEnabled(false);
			bEntradaEstoque.setEnabled(false);
			bExcluir.setEnabled(false);
			bCorrecaoEstoque.setEnabled(false);
			
			
		} // fim if 1 e 2
		
		
		// 3 parametro de bCancelar e bSalvar
		// e parametro do evento de seleção da JTable
		if(parametro == 3)
		{
			// verifica se a table está ou não selecionada, -1 significa não selecionada
			index = tabelaProdutos.getSelectedRow();
			
			if(index == -1)
			{
				bNovo.setEnabled(true);
			}
			else
			{
				
				bAlterar.setEnabled(true);
				bNovo.setEnabled(true);
				bEntradaEstoque.setEnabled(true);
				bExcluir.setEnabled(true);
				bCorrecaoEstoque.setEnabled(true);
			}
			
			bSalvar.setEnabled(false);
			bCancelar.setEnabled(false);
			bLimpar.setEnabled(false);
			
		} // fim if 3
		
		//botão excluir
		if(parametro == 4)
		{
			bAlterar.setEnabled(false);
			bNovo.setEnabled(true);
			bEntradaEstoque.setEnabled(false);
			bExcluir.setEnabled(false);
			bCorrecaoEstoque.setEnabled(false);
			bSalvar.setEnabled(false);
			bCancelar.setEnabled(false);
			bLimpar.setEnabled(false);
		}
		
	} // fim do método habilitandoBotoes
	
	
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
		
		
		
		
	//método usado para carregar a table baseada no banco	
	public void carregaTable()
	{
		try
		{
			resultSet = statement.executeQuery("SELECT count(*) FROM produtos");
			//verifica a quantidade de cadastros no banco
			while(resultSet.next())
			{
				numeroCadastros = resultSet.getInt(1);
			}
			
			
			// for para carregar table
			for(int i =0; i < numeroCadastros; i++)
			{
				resultSet = statement.executeQuery("SELECT * FROM produtos LIMIT "+i+",1");
				resultSet.next();
				
				//atribuindo a pesquisa ao vetor dados para posteriormente atribuir a jTable
				
				dados[0] = resultSet.getString("codigo");
				dados[1] = resultSet.getString("descricao");
				
				
				
				// faz a verificação de estoque para atribuir a situação : normal, crítica, indisponível
				double qtdEstoque = resultSet.getFloat("quantidade_estoque");
				double qtdEstoqueCritico = resultSet.getFloat("estoque_critico");
				
				if(qtdEstoque <= 0)
				{
					dados[2] = "Indisponível";
				}
				else if(qtdEstoque <= qtdEstoqueCritico)
				{
					dados[2] = "Crítico";
				}
				else
				{
					dados[2] = "Normal";
				}
				
				tableModel.addRow(dados);
				
			} // fim for
			
			
		} // fim try
		catch(SQLException erro)
		{
			
		}
	
	} // fim do método carregaTable
	
	public void limpaTable()
	{

		numeroCadastros = tableModel.getRowCount();
			
		// for para limpar a table
		for(int i =0; i < numeroCadastros; i++)
		{
			
			tableModel.removeRow(0);
				
		} 
	} // fim método limpaTable
	
	
} // fim da classe