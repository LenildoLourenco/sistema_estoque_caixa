import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class Acessos extends JFrame implements ItemListener, ActionListener
{
	
	
	// objetos para usar banco de dados
	ResultSet resultSet;
	Statement statement;
	
	
	// Labels
	
	JLabel lPermissao;
	
	
	// checkBox de acessos
	// menus
	JCheckBox cCadastros, cVendas, cFinanceiro, cRelatorio, cUsuario;
	
	// checkbox de acesso
	// menu itens
	
	JCheckBox cProdutos, cClientes, cFornecedores, cFuncionarios;	
		
	
	JCheckBox cVender;
	
	JCheckBox cCaixaDoDia;
	
	JCheckBox cHistoricoDeVendas, cAnaliticoVendas;
	
	JCheckBox cCadastroUsuario, cTrocarUsuario, cTrocarSenha;
	
	// botoes
	
	JButton bGravar, bCancelar, bAlterar;
	
	// painel
	JPanel p1;
	
	
	// painel para o scrollPane
	JPanel pScroll;
	
	// scrollPane
	JScrollPane scrollPane;
	String login, nome;

	
	Acessos(String pLogin, String pNome)
	{
		setTitle("Definindo Acessos");
		setResizable(false);
		setVisible(true);
		setSize(570,610);
		setLocationRelativeTo(null);
		
		setIconImage(new ImageIcon("Imagens/troca_usu.png").getImage());
		
		
		//atribuindo parametros para variáveis globais
		login = pLogin;
		nome = pNome;
		
		// painel do frame
		
		p1 = new JPanel();
		p1.setLayout(null);
		p1.setBackground(new Color(192,192,192));
		getContentPane().add(p1);
		
		// label
		
		lPermissao = new JLabel("Permissões do Usuário: "+nome);
		lPermissao.setFont(new Font("Arial", Font.PLAIN, 18));
		lPermissao.setBounds(10,10,550,25);
		p1.add(lPermissao);
		
		
		// painel do scrollPane para adicionar os checkboxes
		// assim ele podem ter barra de rolagem caso ultrapassem o tamanho do frame
		
		pScroll = new JPanel();
		pScroll.setLayout(null);
		
		
		// scrollPane para o painel pScroll
		scrollPane = new JScrollPane(pScroll);
		scrollPane.setBounds(10,40,420,495);
		p1.add(scrollPane);
		
		
		
		
		
		// checkboxes
		
		
		// cadastros
		cCadastros = new JCheckBox("Cadastros");
		cCadastros.addItemListener(this);
		cCadastros.setBounds(20,5,150,20);
		pScroll.add(cCadastros);
		
		cProdutos = new JCheckBox("Produtos");
		cProdutos.addItemListener(this);
		cProdutos.setBounds(40,25,150,20);
		pScroll.add(cProdutos);
		
		cClientes = new JCheckBox("Clientes");
		cClientes.addItemListener(this);
		cClientes.setBounds(40,45,150,20);
		pScroll.add(cClientes);
		
		cFornecedores = new JCheckBox("Fornecedores");
		cFornecedores.addItemListener(this);
		cFornecedores.setBounds(40,65,150,20);
		pScroll.add(cFornecedores);
		
		cFuncionarios = new JCheckBox("Funcionários");
		cFuncionarios.addItemListener(this);
		cFuncionarios.setBounds(40,85,150,20);
		pScroll.add(cFuncionarios);
		
		
		// vendas
		
		cVendas = new JCheckBox("Vendas");
		cVendas.addItemListener(this);
		cVendas.setBounds(20,105,120,20);
		pScroll.add(cVendas);
		
		cVender = new JCheckBox("Vender");
		cVender.addItemListener(this);
		cVender.setBounds(40,125,120,20);
		pScroll.add(cVender);
		
		// financeiro
		
		cFinanceiro = new JCheckBox("Financeiro");
		cFinanceiro.addItemListener(this);
		cFinanceiro.setBounds(20,145,130,20);
		pScroll.add(cFinanceiro);
		
		cCaixaDoDia = new JCheckBox("Caixa do Dia");
		cCaixaDoDia.addItemListener(this);
		cCaixaDoDia.setBounds(40,165,150,20);
		pScroll.add(cCaixaDoDia);

	
		// relatorio
		
		cRelatorio = new JCheckBox("Relatório");
		cRelatorio.addItemListener(this);
		cRelatorio.setBounds(20,185,120,20);
		pScroll.add(cRelatorio);
		
		cHistoricoDeVendas = new JCheckBox("Histórico De Vendas");
		cHistoricoDeVendas.addItemListener(this);
		cHistoricoDeVendas.setBounds(40,205,170,20);
		pScroll.add(cHistoricoDeVendas);
		
		cAnaliticoVendas = new JCheckBox("Analítico De Vendas");
		cAnaliticoVendas.addItemListener(this);
		cAnaliticoVendas.setBounds(40,225,170,20);
		pScroll.add(cAnaliticoVendas);
		
		
		// usuario
		
		cUsuario = new JCheckBox("Usuário");
		cUsuario.addItemListener(this);
		cUsuario.setSelected(true);
		cUsuario.setEnabled(false);
		cUsuario.setBounds(20,245,120,20);
		pScroll.add(cUsuario);
		
		cCadastroUsuario = new JCheckBox("Cadastro de Usuário");
		cCadastroUsuario.addItemListener(this);
		cCadastroUsuario.setBounds(40,265,160,20);
		pScroll.add(cCadastroUsuario);
		
		cTrocarUsuario = new JCheckBox("Trocar Usuário");
		cTrocarUsuario.setBounds(40,285,160,20);
		cTrocarUsuario.setSelected(true);
		cTrocarUsuario.setEnabled(false);
		pScroll.add(cTrocarUsuario);
			
		cTrocarSenha = new JCheckBox("Trocar Senha");
		cTrocarSenha.setBounds(40,305,160,20);
		cTrocarSenha.setSelected(true);
		cTrocarSenha.setEnabled(false);
		pScroll.add(cTrocarSenha);	
		
	
		
		
		// botoes
		
		bGravar = new JButton("Gravar");
		bGravar.addActionListener(this);
		bGravar.setMnemonic(KeyEvent.VK_G);
		bGravar.setBounds(440,40,100,30);
		p1.add(bGravar);
		
		
		bCancelar = new JButton("Cancelar");
		bCancelar.addActionListener(this);
		bCancelar.setMnemonic(KeyEvent.VK_C);
		bCancelar.setBounds(440,80,100,30);
		p1.add(bCancelar);
		
		bAlterar = new JButton("Alterar");
		bAlterar.addActionListener(this);
		bAlterar.setMnemonic(KeyEvent.VK_A);
		bAlterar.setBounds(440,120,100,30);
		p1.add(bAlterar);
		
		
		
	
	
		bGravar.setEnabled(false);
		
		
		carregaResultSet();
		carregandoCheckbox();
		setandoCheckBox(false);
		
		
		
		
		
		

	} // fim do construtor
	
	
	public void itemStateChanged(ItemEvent e)
	{
		// OBS: TODOS OS RETURNS NO ITEMLISTENER SÃO APENAS PARA OTIMIZAR O PROGRAMA,
		// VISTO QUE SE UM IF FOR VERDADEIRO OS OUTROS NÃO PRECISAM SER TESTADOS
		
		
		
		// criando uma instância da classe objeto para receber o objeto da ação
		// isso evita ficar digitando sempre e.getSource();
		Object objeto = e.getSource();
		
		
		
		/* faz a verificação se algum cMenuItem foi selecionado sem selecionar o menu.
		  selecionando automaticamente o mesmo.
		  por exemplo abilitar o menuItem de cadastrar produtos sem abilitar o menu Cadastros não funcionaria
		*/
		
		// a ação veio de algum desses checkbox que representam os menuItens de cadastros?
		if(objeto == cProdutos || objeto == cClientes || objeto == cFornecedores || objeto == cFuncionarios)
		{
			// a ação foi de selecionar ou deselecionar?
			if(e.getStateChange() == ItemEvent.SELECTED)
			{
				
				
					cCadastros.setSelected(true);
					return; // o return 
			

			} // fim if getStateChange
			return;
		} // fim if de ação dos checkbox que representam os menuItem de cadastros
		
		
		// se você deselecionar ele, automaticamente deseleciona todos os checkbox aninhados nele
		if(objeto == cCadastros)
		{
			if(!cCadastros.isSelected())
			{
				cProdutos.setSelected(false);
				cClientes.setSelected(false);
				cFornecedores.setSelected(false);
				cFuncionarios.setSelected(false);
				return;
				
			}
		
		} // fim if cCadastros
		
		
		// fazendo a verificação dos demais itens agora, lembrando se um item pai for deselecionado, 
		// todos os filhos serão, se um só filho for selecionado o pai também será
		
		

		
		
		if(objeto == cVender)
		{
			// selecionou ou não?
			if(e.getStateChange() == ItemEvent.SELECTED)
			{
				
				 cVendas.setSelected(true);
				 return;
			
			
			} // fim if
			return;
				
		} // fim if
		
		if(objeto == cVendas)
		{
			if(!cVendas.isSelected())
			{
				cVender.setSelected(false);
			 return;
			}
		
		} // fim if
		
		if(objeto == cCaixaDoDia)
		{
			if(e.getStateChange() == ItemEvent.SELECTED)
			{
			
			
				    cFinanceiro.setSelected(true);
				    return;
				
			} // fim if
			return;
		} // fim if
		
		if(objeto == cFinanceiro)
		{
			if(!cFinanceiro.isSelected())
			{
				cCaixaDoDia.setSelected(false);
				return;
			}
		
		} // fim if
		
		
		if(objeto == cHistoricoDeVendas || objeto == cAnaliticoVendas)
		{ 	
			// selecionou
			if(e.getStateChange() == ItemEvent.SELECTED)
			{
				
				cRelatorio.setSelected(true);
				return;
				 
			} // fim if
			return;
		} // fim if
		
		
		if(objeto == cRelatorio)
		{
			// selecionou?
			if(!cRelatorio.isSelected())
			{
				cHistoricoDeVendas.setSelected(false);
				cAnaliticoVendas.setSelected(false);
				return;
				
			}
		
		}
		
	
		
		
		
		
 
	} // fim do ItemListener

	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == bGravar)
		{
				try
				{
					String sql = "UPDATE usuarios SET "+
						"acesso_cadastros = " +cCadastros.isSelected()+","+
						"acesso_produtos = "+cProdutos.isSelected()+ ","+
						"acesso_clientes = "+ cClientes.isSelected()+","+ 
						"acesso_fornecedores = "+cFornecedores.isSelected() +","+
						"acesso_funcionarios = "+cFuncionarios.isSelected() +","+
						"acesso_vendas = "+cVendas.isSelected() +","+
						"acesso_vender = "+cVender.isSelected() +","+
						"acesso_financeiro = "+cFinanceiro.isSelected() +","+
						"acesso_caixa_dia = "+cCaixaDoDia.isSelected() +","+
						"acesso_relatorio = "+cRelatorio.isSelected() +","+
						"acesso_historico_vendas = "+cHistoricoDeVendas.isSelected() +","+
						"acesso_analitico_vendas = "+cAnaliticoVendas.isSelected() +","+
						"acesso_cadastro_usuario = "+cCadastroUsuario.isSelected() +" "+
						"WHERE login = '" +login+"'";
								
					
					
		
					
					int r = statement.executeUpdate(sql);
				
					if(r==1)
					{
						
						JOptionPane.showMessageDialog(null, "Alteração realizada com sucesso");
						bAlterar.setEnabled(true);
						bGravar.setEnabled(false);
						setandoCheckBox(false);
			
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
			
			
		} // fim if bGravar
		
		if(e.getSource() == bCancelar)
		{
			setVisible(false);
		}
		
		
		if(e.getSource() == bAlterar)
		{
			bAlterar.setEnabled(false);
			bGravar.setEnabled(true);
			setandoCheckBox(true);
		}
		
	} // fim do ActionListener
	
	
	
	public void carregandoCheckbox()
	{
		try
		{
			resultSet = statement.executeQuery("SELECT * FROM usuarios WHERE login ='"+login+"'");
			resultSet.next();
			
			
			
			//fazendo conversão de string para int e depois para boolean
			
			
			int aux = Integer.parseInt(resultSet.getString("acesso_cadastros"));
			cCadastros.setSelected(aux != 0);
			
			//fazendo a mesma coisa que as duas linhas de cima em uma só linha
			// pegando valor do banco, transformando em int, comparando com 0 para retornar true ou false
			// 1 = true, 0 = false
			
			cProdutos.setSelected(new Integer(Integer.parseInt(resultSet.getString("acesso_produtos"))) !=0 );
			cClientes.setSelected(new Integer(Integer.parseInt(resultSet.getString("acesso_clientes"))) !=0 );
			cFornecedores.setSelected(new Integer(Integer.parseInt(resultSet.getString("acesso_fornecedores"))) !=0 );
			cFuncionarios.setSelected(new Integer(Integer.parseInt(resultSet.getString("acesso_funcionarios"))) !=0 );
			cVendas.setSelected(new Integer(Integer.parseInt(resultSet.getString("acesso_vendas"))) !=0 );
			cVender.setSelected(new Integer(Integer.parseInt(resultSet.getString("acesso_vender"))) !=0 );
			cFinanceiro.setSelected(new Integer(Integer.parseInt(resultSet.getString("acesso_financeiro"))) !=0 );
			cCaixaDoDia.setSelected(new Integer(Integer.parseInt(resultSet.getString("acesso_caixa_dia"))) !=0 );
			cRelatorio.setSelected(new Integer(Integer.parseInt(resultSet.getString("acesso_relatorio"))) !=0 );
			cHistoricoDeVendas.setSelected(new Integer(Integer.parseInt(resultSet.getString("acesso_historico_vendas"))) !=0 );
			cAnaliticoVendas.setSelected(new Integer(Integer.parseInt(resultSet.getString("acesso_analitico_vendas"))) !=0 );
			cCadastroUsuario.setSelected(new Integer(Integer.parseInt(resultSet.getString("acesso_cadastro_usuario"))) !=0 );
			
			
						
		} // fim try
		catch(SQLException erro)
		{
			JOptionPane.showMessageDialog(null,"Usuário não encontrado");
			return;
		}
		
		
	} // fim setandoCheckbox
	
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
	
	public void setandoCheckBox(boolean parametro)
	{
		cCadastros.setEnabled(parametro);
		cProdutos.setEnabled(parametro);
		cClientes.setEnabled(parametro);
		cFornecedores.setEnabled(parametro);
		cFuncionarios.setEnabled(parametro);
		cVendas.setEnabled(parametro);
		cVender.setEnabled(parametro);
		cFinanceiro.setEnabled(parametro);
		cCaixaDoDia.setEnabled(parametro);
		cRelatorio.setEnabled(parametro);
		cHistoricoDeVendas.setEnabled(parametro);
		cAnaliticoVendas.setEnabled(parametro);
		cCadastroUsuario.setEnabled(parametro);
	}
	
} // fim da class