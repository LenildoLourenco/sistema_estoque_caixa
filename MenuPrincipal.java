import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.text.DateFormat;
import java.sql.*;

public class MenuPrincipal extends JFrame implements ActionListener
{
		
		
	// objetos para usar banco de dados
	ResultSet resultSet;
	Statement statement;

	// Declarando todos os menuItem e menus da tela principal
	JMenuBar barraMenu;
	
	JMenu cadastros, vendas, financeiro, relatorios, usuarios, mSair;
	
	JMenuItem produtos, clientes, fornecedores, funcionarios;
	
	JMenuItem vender;
	
	JMenuItem caixaDoDia;
	
	JMenuItem historicoVendas, analiticoVendas;
	
	JMenuItem cadastroUsuario, trocarUsuario, trocarSenha;
	
	JMenuItem iSair;
	
	// Painel
	JPanel p1;
	
	// Colocando data
	String data;
	JLabel lData;
	Date agora;
	DateFormat df;	
		
	// Labels usuario
	
	JLabel lUsuario;
		
		
	// Jlabel e TextField estoque crítico
	JLabel lQuantidadeProdutosEstoqueCritico, lQuantidadeProdutosIndisponiveis;
	JTextField tQuantidadeProdutosEstoqueCritico, tQuantidadeProdutosIndisponiveis;
		

	// objeto global que ajuda a verificar se algum frame ja está aberto
	JFrame objeto = new JFrame();
	
	// método que verifica se algum frame já esta aberto
	// todos os frames são atribuidos a objeto assim ele pode fazer a verificação
	private boolean aberto()
	{
		return(objeto.isVisible());
	}
	
	//icone
	ImageIcon icone;
	
	//label e icone para imagem
	
	JLabel lImagem;
	Icon iImagem;
	
	//variavel global de login de usuario
	String login, usuario;

	
	//botão para atualizar, para que não precise fechar e abrir a janela para ver itens indisponiveis ou criticos
	JButton bAtualizar;
	
	
	//parametro do usuario que esta acessando
	MenuPrincipal(String pLogin, String pUsuario)	
	{
		setTitle("Sistema de Gerenciamento de Estoque e Frente de Caixa - Não Fiscal");
		setSize(1100,700);
		setLocationRelativeTo(null);
		
		// painel
		
		p1 = new JPanel();
		p1.setLayout(null);
		p1.setBackground(new Color(192,192,192));
		getContentPane().add(p1);
		
		//recebendo parametro
		
		login = pLogin;
		usuario = pUsuario;
		
		
		
		//icone
		icone = new ImageIcon("Imagens/icon_sge.jpg");
		setIconImage(icone.getImage());

		
		//definindo imagem em JLabel

		
		iImagem = new ImageIcon("Imagens/logo_sge.png");

		lImagem = new JLabel(iImagem);
		
		lImagem.setBounds(20,78,1040,526);
		p1.add(lImagem);
		
		
		// Data
		
		agora = new Date();
		df = DateFormat.getDateInstance(DateFormat.FULL);
		data = df.format(agora);
		
		lData = new JLabel(data);
		
		lData.setBounds(5,615,310,30);
		lData.setForeground(Color.white);
		lData.setFont(new Font("Arial",Font.PLAIN,16));
		p1.add(lData);
		
		// usuario
		
		lUsuario = new JLabel("Usuário: "+usuario);
		lUsuario.setFont(new Font("Arial",Font.BOLD,16));
		lUsuario.setBounds(315,615,200,30);
		p1.add(lUsuario);
		
		
		
		
		
		
		
		// Estoque crítico e produtos indisponiveis
		
		lQuantidadeProdutosEstoqueCritico = new JLabel("Quantidade de Produtos em Estoque Crítico");
		lQuantidadeProdutosEstoqueCritico.setBounds(10,10,250,20);
		p1.add(lQuantidadeProdutosEstoqueCritico);
		
		
		tQuantidadeProdutosEstoqueCritico = new JTextField();	
		tQuantidadeProdutosEstoqueCritico.setBounds(10,30,100,30);
		tQuantidadeProdutosEstoqueCritico.setEditable(false);
		p1.add(tQuantidadeProdutosEstoqueCritico);
		
		
		// indisponiveis
		lQuantidadeProdutosIndisponiveis = new JLabel("Quantidade de Produtos Indisponíveis");
		lQuantidadeProdutosIndisponiveis.setBounds(280,10,250,20);
		p1.add(lQuantidadeProdutosIndisponiveis);
		
		
		tQuantidadeProdutosIndisponiveis = new JTextField();
		tQuantidadeProdutosIndisponiveis.setBounds(280,30,100,30);
		tQuantidadeProdutosIndisponiveis.setEditable(false);
		p1.add(tQuantidadeProdutosIndisponiveis);
		
		bAtualizar = new JButton("Atualizar");
		bAtualizar.addActionListener(this);
		bAtualizar.setBounds(515,30,100,30);
		p1.add(bAtualizar);
		
		
		// menu
		
		barraMenu = new JMenuBar();
		
		
		//Instanciando JMenu e declarando Mnemonics
		cadastros = new JMenu("Cadastros");
		cadastros.setMnemonic(KeyEvent.VK_C);
				
		vendas = new JMenu("Vendas");
		vendas.setMnemonic(KeyEvent.VK_V);
		
		financeiro = new JMenu("Financeiro");
		financeiro.setMnemonic(KeyEvent.VK_F);
		
		relatorios = new JMenu("Relatórios");
		relatorios.setMnemonic(KeyEvent.VK_R);
		
		usuarios = new JMenu("Usuários");
		usuarios.setMnemonic(KeyEvent.VK_U);
		
		mSair = new JMenu("Sair");
		mSair.setMnemonic(KeyEvent.VK_S);
		
		
		
		//Inserindo icones
		//Instanciando menuItens e declarando os ActionListener para esta classe
		//Declarando accelerator e mnemonic
		produtos = new JMenuItem("Produtos", new ImageIcon("Imagens/cad_pro.png"));
		produtos.addActionListener(this);
		produtos.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
		produtos.setMnemonic(KeyEvent.VK_P);
		
		clientes = new JMenuItem("Clientes", new ImageIcon("Imagens/cad_cli.png"));
		clientes.addActionListener(this);
		clientes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
		clientes.setMnemonic(KeyEvent.VK_L);
		
		fornecedores = new JMenuItem("Fornecedores", new ImageIcon("Imagens/cad_for.png"));
		fornecedores.addActionListener(this);
		fornecedores.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
		fornecedores.setMnemonic(KeyEvent.VK_F);
		
		funcionarios = new JMenuItem("Funcionários", new ImageIcon("Imagens/cad_fun.png"));
		funcionarios.addActionListener(this);
		funcionarios.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK));
		funcionarios.setMnemonic(KeyEvent.VK_U);
		
				
		vender = new JMenuItem("Vender", new ImageIcon("Imagens/vender.png"));
		vender.addActionListener(this);
		vender.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		
		caixaDoDia = new JMenuItem("Caixa do Dia", new ImageIcon("Imagens/cx_dia.png"));
		caixaDoDia.addActionListener(this);
		caixaDoDia.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
		caixaDoDia.setMnemonic(KeyEvent.VK_A);
		
		historicoVendas = new JMenuItem("Histórico de Vendas", new ImageIcon("Imagens/historico.png"));
		historicoVendas.addActionListener(this);
		historicoVendas.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));
		historicoVendas.setMnemonic(KeyEvent.VK_H);
		
		analiticoVendas = new JMenuItem("Analítico de Vendas", new ImageIcon("Imagens/analitico.png"));
		analiticoVendas.addActionListener(this);
		analiticoVendas.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
		analiticoVendas.setMnemonic(KeyEvent.VK_N);
		
		cadastroUsuario = new JMenuItem("Cadastro de Usuário", new ImageIcon("Imagens/cad_usu.png"));
		cadastroUsuario.addActionListener(this);
		cadastroUsuario.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.ALT_MASK));
		cadastroUsuario.setMnemonic(KeyEvent.VK_D);
		
		trocarUsuario = new JMenuItem("Trocar Usuário", new ImageIcon("Imagens/troca_usu.png"));
		trocarUsuario.addActionListener(this);
		trocarUsuario.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.ALT_MASK));
		trocarUsuario.setMnemonic(KeyEvent.VK_T);
		
		trocarSenha = new JMenuItem("Trocar Senha", new ImageIcon("Imagens/troca_usu.png"));
		trocarSenha.addActionListener(this);
		trocarSenha.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
		trocarSenha.setMnemonic(KeyEvent.VK_O);
		
		iSair = new JMenuItem("Sair", new ImageIcon("Imagens/sair.png"));
		iSair.addActionListener(this);
		iSair.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0));
		iSair.setMnemonic(KeyEvent.VK_ESCAPE);
		
		
		//Adicionando JMenuItem aos JMenu
		cadastros.add(produtos);
		cadastros.add(clientes);
		cadastros.add(fornecedores);
		cadastros.add(funcionarios);
		
		
		vendas.add(vender);
		
		financeiro.add(caixaDoDia);

		
	
		relatorios.add(historicoVendas);
		relatorios.add(analiticoVendas);
		
		usuarios.add(cadastroUsuario);
		usuarios.add(trocarUsuario);
		usuarios.add(trocarSenha);
		
		mSair.add(iSair);
		
		//Adicionando JMenu ao JMenuBar
		barraMenu.add(cadastros);
		barraMenu.add(vendas);
		barraMenu.add(financeiro);
		barraMenu.add(relatorios);
		barraMenu.add(usuarios);
		barraMenu.add(mSair);
		
		//Setando JMenuBAr
		setJMenuBar(barraMenu);
		
		carregaResultSet();
		setandoAcessos();
		
		carregarTextField();
		
		
	} // fim do construtor
	
	public void actionPerformed(ActionEvent e)
	{	
		
		if(e.getSource() == bAtualizar)
		{
			carregarTextField();
		}		
		
		if(aberto())
		{
			JOptionPane.showMessageDialog(null,"Feche a janela '" + objeto.getTitle()+"' para abrir esta",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}
	
		if(e.getSource() == produtos)
		{
			
			objeto = new CadastroProdutos();
		
		
		} // fim if cadastroProdutos
		
		if(e.getSource() == clientes)
		{
			objeto = new CadastroClientes();
			
			
		} // fim if cadastroClientes 
		
		if(e.getSource() == fornecedores)
		{
			objeto = new CadastroFornecedor();
			
		} // fim if cadastroFornecedor
		
		if(e.getSource() == funcionarios)
		{
			objeto = new CadastroFuncionario();
			
		} // fim if cadastroFuncionario
				
		
		if(e.getSource() == vender)
		{
			objeto = new Vender(usuario);
			objeto.setUndecorated(true);
			objeto.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
			objeto.setVisible(true);
			objeto.setExtendedState(Frame.MAXIMIZED_BOTH);

			
		} // fim if vender
		
		if(e.getSource() == caixaDoDia)
		{
			objeto = new CaixaDia();
			
		} // fim if caixaDoDia
		
		if(e.getSource() == historicoVendas)
		{
			objeto = new HistoricoVendas();
		} // fim if historicoDeVendas
		
		if(e.getSource() == analiticoVendas)
		{
			objeto = new AnaliticoVendas();
		} // fim if historicoDeVendas
		
		if(e.getSource() == cadastroUsuario)
		{
			objeto = new CadastroUsuario();
		} // fim if cadastroUsuario
		
		if(e.getSource() == trocarUsuario)
		{
			objeto = new Login(true, "Trocar Usuário",login,usuario);
			objeto.setVisible(true);
			objeto.setResizable(false);
			objeto.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			
			setVisible(false);
			

		} // fim if trocarUsuario
		
		if(e.getSource() == trocarSenha)
		{
			objeto = new TrocarSenha(login);
			
		} // fim if trocarSenha
		
		if(e.getSource() == iSair)
		{
			int opcao;
			Object[]botoes = {"Sim", "Não"};
			opcao = JOptionPane.showOptionDialog(null,
				"Deseja mesmo Sair?", "Fechar",
				JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,
				null,botoes,botoes[0]);
			if(opcao == JOptionPane.YES_OPTION)
				System.exit(0);
				
		} // fim if sair
		 
		
		
		
	} // fim do actionPerformed	
	
	public void setandoAcessos()
	{
		try
		{
			resultSet = statement.executeQuery("SELECT * FROM usuarios WHERE login ='"+login+"'");
			resultSet.next();
			
			
			
			//fazendo conversão de string para int e depois para boolean
			
			
			int aux = Integer.parseInt(resultSet.getString("acesso_cadastros"));
			cadastros.setEnabled(aux != 0);
			
			//fazendo a mesma coisa que as duas linhas de cima em uma só linha
			// pegando valor do banco, transformando em int, comparando com 0 para retornar true ou false
			// 1 = true, 0 = false
			
			produtos.setEnabled(new Integer(Integer.parseInt(resultSet.getString("acesso_produtos"))) !=0 );
			clientes.setEnabled(new Integer(Integer.parseInt(resultSet.getString("acesso_clientes"))) !=0 );
			fornecedores.setEnabled(new Integer(Integer.parseInt(resultSet.getString("acesso_fornecedores"))) !=0 );
			funcionarios.setEnabled(new Integer(Integer.parseInt(resultSet.getString("acesso_funcionarios"))) !=0 );
			vendas.setEnabled(new Integer(Integer.parseInt(resultSet.getString("acesso_vendas"))) !=0 );
			vender.setEnabled(new Integer(Integer.parseInt(resultSet.getString("acesso_vender"))) !=0 );
			financeiro.setEnabled(new Integer(Integer.parseInt(resultSet.getString("acesso_financeiro"))) !=0 );
			caixaDoDia.setEnabled(new Integer(Integer.parseInt(resultSet.getString("acesso_caixa_dia"))) !=0 );
			relatorios.setEnabled(new Integer(Integer.parseInt(resultSet.getString("acesso_relatorio"))) !=0 );
			historicoVendas.setEnabled(new Integer(Integer.parseInt(resultSet.getString("acesso_historico_vendas"))) !=0 );
			analiticoVendas.setEnabled(new Integer(Integer.parseInt(resultSet.getString("acesso_analitico_vendas"))) !=0 );
			cadastroUsuario.setEnabled(new Integer(Integer.parseInt(resultSet.getString("acesso_cadastro_usuario"))) !=0 );
			
			
						
		} // fim try
		catch(SQLException erro)
		{
		
		}
		
		
	} // fim setando acessos
	
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
	
	
	public void carregarTextField()
	{
		int numeroCadastros = 0;
		int indisponiveis = 0, criticos = 0;
		try
		{
			resultSet = statement.executeQuery("SELECT count(*) FROM produtos");
			//verifica a quantidade de cadastros no banco
			while(resultSet.next())
			{
				numeroCadastros = resultSet.getInt(1);
			}
			
			
			// for para verificar itens em estoque crítico ou indisponiveis
			for(int i =0; i < numeroCadastros; i++)
			{
				resultSet = statement.executeQuery("SELECT * FROM produtos LIMIT "+i+",1");
				resultSet.next();
				

				
				// faz a verificação de estoque para atribuir a situação : crítica, indisponível
				double qtdEstoque = resultSet.getFloat("quantidade_estoque");
				double qtdEstoqueCritico = resultSet.getFloat("estoque_critico");
				
				if(qtdEstoque <= 0)
				{
					indisponiveis++;
				}
				else if(qtdEstoque <= qtdEstoqueCritico)
				{
					criticos++;
				}
				
			} // fim for
			
			
		} // fim try
		catch(SQLException erro)
		{
			
		}
		
		
		//setando valores e cor nos textfields
		if(indisponiveis>0)
		{
			tQuantidadeProdutosIndisponiveis.setBackground(new Color(245,105,105));
			tQuantidadeProdutosIndisponiveis.setText(""+indisponiveis);
		}
		else
		{
			tQuantidadeProdutosIndisponiveis.setBackground(new Color(238,238,238));
			tQuantidadeProdutosIndisponiveis.setText("0");
		}
		
		if(criticos>0)
		{
			tQuantidadeProdutosEstoqueCritico.setBackground(new Color(255,253,154));
			tQuantidadeProdutosEstoqueCritico.setText(""+criticos);
				
		}
		else
		{
			tQuantidadeProdutosEstoqueCritico.setBackground(new Color(238,238,238));
			tQuantidadeProdutosEstoqueCritico.setText("0");
			
		}
						
			
	
	} // fim do método carregaTextField
	
		
} // fim da Classe