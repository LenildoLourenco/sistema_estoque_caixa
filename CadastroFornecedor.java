import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import javax.swing.text.*;
import java.text.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.*;
import java.sql.*;

public class CadastroFornecedor extends JFrame implements ActionListener, DocumentListener, ListSelectionListener
{	
		
	// objetos para usar banco de dados
	ResultSet resultSet;
	Statement statement;
	
	// int usado para contar o número de cadastros no banco de dados
	int numeroCadastros;
	
	
	//string usada para auxiliar o carregamento de dados do banco para JTable
	String dados[] = new String[3];


	
	// tableRowSorter usado para pesquisar na table por text
	TableRowSorter<TableModel> organizador; 
	
	// string para receber valores de JOptionPane
	String aux;

	
	// int para ser usado na pesquisa da table, para ver o item selecionado
	int index ;
	// int usado para auxiliar index
	int index2;
	
	// string para replace
	String stringCNPJ, stringCep, stringTelefone, stringCelular;
		
	// int uf, usado para selecionar o estado com base no banco de dados
	int intUf;
	
	// painel
	
	JPanel p1;
	
	// menu, menuitem, menuBar
	
	JMenuBar barraMenu;
	JMenuItem iSair;
	JMenu mSair;
	


	
	JTextField tPesquisar;
	
	// table
	JTable tabela;
	DefaultTableModel tableModel;
	// scroll para table
	JScrollPane scrollPane;
	
	
	JLabel lDescricaoPesquisa, lResultado;

	// labels e textfield
	JLabel lCodigo, lNome, lEndereco,lNumero, lBairro, lCep, lCidade, lUf, lCnpj, lTelefone, lCelular, lEmail;
	
	JTextField tCodigo, tNome, tEndereco, tNumero, tBairro, tCidade, tEmail; 
		
		
	//textfield formatado
	
	// cnpj
	JFormattedTextField ftCnpj;
	MaskFormatter mfCnpj;
	
	// cep
	JFormattedTextField ftCep;
	MaskFormatter mfCep;
	
	//telefone
	JFormattedTextField ftTelefone;
	MaskFormatter mfTelefone;
	
	//celular
	JFormattedTextField ftCelular;
	MaskFormatter mfCelular;
		
	// combobox
	
	JComboBox cUf;
	String estados[] = {"", "Acre(AC)", "Alagoas(AL)", "Amapá(AP)", "Amazonas(AM)", "Bahia(BA)",
		"Ceará(CE)", "Distrito Federal(DF)", "Espirito Santo(ES)", "Goiás(GO)", "Maranhão(MA)",
		"Mato Grosso(MT)", "Mato Grosso do Sul(MS)", "Minas Gerais", "Pará(PA)", "Paraíba(PB)", "Paraná(PR)",
		"Pernambuco(PE)", "Piauí(PI)", "Rio de Janeiro(RJ)", "Rio Grande do Sul(RS)", "Rio Grande do Norte(RN)",
		"Rondônia(RO)", "Roraima(RR)", "Santa Catarina(SC)", "São Paulo(SP)", "Sergipe(SE)", "Tocantins(TO)"};
		
	
	
	// botoes
	
	JButton bAlterar, bCancelar, bSalvar, bExcluir, bLimpar, bNovo;
	
	
	
	CadastroFornecedor()
	{
		setTitle("Cadastro de Fornecedores");
		
		
		
		setSize(750,710);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		
		
		setIconImage(new ImageIcon("Imagens/cad_for.png").getImage());
		
		
	
		
		// painel
		
		p1 = new JPanel();
		p1.setLayout(null);
		p1.setBackground(new Color(192,192,192));
		
		getContentPane().add(p1);
		
		// adicionando paineis a guia

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
		
		// codigo fornecedor	
		lCodigo = new JLabel("Código");
		lCodigo.setBounds(10,10,70,20);
		p1.add(lCodigo);
		
		tCodigo = new JTextField(3);
		tCodigo.setEditable(false);
		tCodigo.setBounds(10,30,70,30);
		p1.add(tCodigo);
		
		// nome
		
		lNome = new JLabel("Nome da Empresa");
		lNome.setBounds(90,10,150,20);
		p1.add(lNome);
		
		tNome  = new JTextField(30);
		tNome.setBounds(90,30,400,30);
		p1.add(tNome);
		
		
		// data de nascimento
		
		lCnpj = new JLabel("CNPJ");
		lCnpj.setBounds(500,10,130,20);
		p1.add(lCnpj);
		
		// maskFormater
		try
		{
			mfCnpj = new MaskFormatter("##.###.###/####-##");
			mfCnpj.setPlaceholderCharacter('0');
			
			mfCep = new MaskFormatter("#####-###");
			mfCep.setPlaceholderCharacter('0');
			
			mfTelefone = new MaskFormatter("(##)####-####");
			mfTelefone.setPlaceholderCharacter('0');
			
			mfCelular = new MaskFormatter("(##)#####-####");
			mfCelular.setPlaceholderCharacter('0');
			
		}
		catch(ParseException excp){}
		
		ftCnpj = new JFormattedTextField(mfCnpj);
		ftCnpj.setBounds(500,30,130,30);
		p1.add(ftCnpj);
		
		// endereço
		
		lEndereco = new JLabel("Endereço");
		lEndereco.setBounds(10,70,70,20);
		p1.add(lEndereco);
		
		tEndereco = new JTextField(30);
		tEndereco.setBounds(10,90,370,30);
		p1.add(tEndereco);
	
	
		// numero
		
		lNumero = new JLabel("Número");
		lNumero.setBounds(390,70,60,20);
		p1.add(lNumero);
			
		tNumero = new JTextField();
		tNumero.setBounds(390,90,60,30);
		p1.add(tNumero);
		
		// bairro
		
		lBairro = new JLabel("Bairro");
		lBairro.setBounds(460,70,100,20);
		p1.add(lBairro);
		
		tBairro = new JTextField();
		tBairro.setBounds(460,90,170,30);
		p1.add(tBairro);
		
		
		// cidade
		
		lCidade = new JLabel("Cidade");
		lCidade.setBounds(10,130,100,20);
		p1.add(lCidade);
		
		tCidade = new JTextField(15);
		tCidade.setBounds(10,150,215,30);
		p1.add(tCidade);
		
		// cep
		
		lCep = new JLabel("Cep");
		lCep.setBounds(235,130,70,20);
		p1.add(lCep);
		
		ftCep= new JFormattedTextField(mfCep);
		ftCep.setBounds(235,150,70,30);
		p1.add(ftCep);
		
		// Uf
		
		lUf = new JLabel("UF");
		lUf.setBounds(315,130,100,20);
		p1.add(lUf);
		
		cUf = new JComboBox(estados);
		cUf.setBounds(315,150,170,30);
		p1.add(cUf);
		
		// telefone
		
		lTelefone = new JLabel("Telefone");
		lTelefone.setBounds(495,130,130,20);
		p1.add(lTelefone);
		
		ftTelefone = new JFormattedTextField(mfTelefone);
		ftTelefone.setBounds(495,150,135,30);
		p1.add(ftTelefone);
			
		// celular
			
		lCelular = new JLabel("Celular");
		lCelular.setBounds(10,190,130,20);
		p1.add(lCelular);
			
		ftCelular = new JFormattedTextField(mfCelular);
		ftCelular.setBounds(10,210,135,30);
		p1.add(ftCelular);
			
		// email
			
		lEmail = new JLabel("Email");
		lEmail.setBounds(155,190,80,20);
		p1.add(lEmail);
			
		tEmail = new JTextField();
		tEmail.setBounds(155,210,300,30);
		p1.add(tEmail);
			
		
		// botões
	 
		bAlterar = new JButton("Alterar");
		bAlterar.setMnemonic(KeyEvent.VK_A);
		bAlterar.addActionListener(this);
		bAlterar.setBounds(10,250,100,30);
		p1.add(bAlterar);
		
		bCancelar = new JButton("Cancelar");
		bCancelar.setMnemonic(KeyEvent.VK_C);
		bCancelar.addActionListener(this);
		bCancelar.setBounds(120,250,100,30);
		p1.add(bCancelar); 
			
		bSalvar = new JButton("Salvar");
		bSalvar.setMnemonic(KeyEvent.VK_S);
		bSalvar.addActionListener(this);
		bSalvar.setBounds(230,250,100,30);
		p1.add(bSalvar);
		
		bExcluir = new JButton("Excluir");
		bExcluir.setMnemonic(KeyEvent.VK_E);
		bExcluir.addActionListener(this);
		bExcluir.setBounds(340,250,100,30);
		p1.add(bExcluir);
		
		bLimpar = new JButton("Limpar");
		bLimpar.setMnemonic(KeyEvent.VK_L);
		bLimpar.addActionListener(this);
		bLimpar.setBounds(450,250,100,30);
		p1.add(bLimpar);
		
		bNovo = new JButton("Novo");
		bNovo.setMnemonic(KeyEvent.VK_N);
		bNovo.addActionListener(this);
		bNovo.setBounds(10,290,100,30);
		p1.add(bNovo);	
		
		
		
		
	
		// barra pesquisa
		 lDescricaoPesquisa = new JLabel("Pesquisa");
		 lDescricaoPesquisa.setBounds(10,330,150,20);
		 p1.add(lDescricaoPesquisa);
		
		 tPesquisar = new JTextField(6);
		 tPesquisar.setBounds(10,350,300,30);
		 // adicionando documentListener
		 tPesquisar.getDocument().addDocumentListener(this);
		 p1.add(tPesquisar);
		 
		 // label resultado
		 lResultado = new JLabel("Resultado");
		 lResultado.setBounds(10,390,150,20);
		 p1.add(lResultado);
	 	 
		 // table resultado
		 
		 // sobreescrever este método permite a table não ser editável, e ainda sim ser selecionável
		 tableModel = new DefaultTableModel()
		 {
		 
			 @Override
			 public boolean isCellEditable(int row, int column)
			 {
			 	return false;
		     }
		 };
		 
		 
		 
		 tabela = new JTable(tableModel);
		 
		 tableModel.addColumn("Código");
		 tableModel.addColumn("Nome");
		 
		 // adicionando SelectionListener
		 tabela.getSelectionModel().addListSelectionListener(this);

	
		     
	     // Definindo as celulas do table não arrastáveis e não redimensionáveis
		 tabela.getTableHeader().setResizingAllowed(false);
		 tabela.getTableHeader().setReorderingAllowed(false);
		 
	
		 
		 // definindo o valor das células centralizado
		 DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		 centerRenderer.setHorizontalAlignment(JLabel.CENTER);
	 	
		 tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		 tabela.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		 
		 // definindo apenas uma linha selecionável
		 tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	
		 
		 // definindo a largura das colunas da table
		 tabela.getColumnModel().getColumn(0).setPreferredWidth(60);
		 tabela.getColumnModel().getColumn(1).setPreferredWidth(500);
	
		 
		 // tableRowSorter usado para pesquisar na table por text
		 organizador = new TableRowSorter<>(tabela.getModel());
		 tabela.setRowSorter(organizador);
		 
		 scrollPane = new JScrollPane(tabela);
		 scrollPane.setBounds(10,410,560,220);
		 p1.add(scrollPane);
		 
		 // botão para exibir item selecionado em JTable tabelaProdutos
	
	
	
	
		//setando botoes e textfield 
		// tornando os textFields não editáveis
		setandoEditable(false);	
				
		// setando botões com parametro 4 que habilita somento bNovo
		habilitandoBotoes(4);
		
		//método para ligar o banco de dados com a aplicação
		carregaResultSet();
		
		//método para carregar a table com o dados do banco
		carregaTable();
	
	} // fim construtor
	
	
	
	public void actionPerformed(ActionEvent e)
	{
		
		
		// painel Pesquisar
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
			index = tabela.getSelectedRow();
			
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
			
			index = tabela.getSelectedRow ();
			
		
			
			// verifica se salvar veio de um novo cadastro ou de uma alteração
			// salvando no banco de acordo com a operação insert into novo cadastro ou update cadastroAntigo
			// index == -1 significa que JTable não está selecionada, consequentemente indica um novo cadastro
			if(index == -1)
			{

				
				try
				{
					
				   	String sql ="INSERT INTO fornecedores (nome, cnpj, endereco," +
					 	"numero, bairro, cidade, cep, uf, telefone, celular, email)"+
					 	 " Values ('"+
					 	 tNome.getText()+"','"+
					 	 stringCNPJ+"','"+
						tEndereco.getText()+"','"+
						tNumero.getText() +"','"+
						tBairro.getText()+"','"+
						tCidade.getText()+"','"+
						stringCep+ "','"+
						cUf.getSelectedIndex()+"','"+
						stringTelefone +"','"+
						stringCelular +"','"+
						tEmail.getText()+"')";

					 
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
					 
					 tabela.setRowSelectionInterval(linha -1,linha -1);
	
					 //carregando novo cadastro
					 carregarDados();
					 
					 
				} // fim try
				catch(SQLException erro)
				{
					JOptionPane.showMessageDialog(null,"Falha no cadastro");
				}
				
				
			} // fim if index == -1
			else
			{
				try
				{
					String sql = "UPDATE fornecedores SET "+
						"nome = '" + tNome.getText() + "',"+
						"cnpj = '" + stringCNPJ +"',"+
						"endereco = '" + tEndereco.getText() +"',"+
						"numero = '"+ tNumero.getText()+"',"+
						"bairro = '"+ tBairro.getText() +"',"+
						"cidade = '"+ tCidade.getText()+"',"+
						"cep = '"+ stringCep +"',"+
						"uf = '"+ cUf.getSelectedIndex()+"',"+
						"telefone = '"+stringTelefone +"',"+
						"celular = '"+stringCelular +"',"+
						"email = '"+ tEmail.getText()+"'"+
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
						tabela.setRowSelectionInterval(index2,index2);
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
				String nome = "Deseja excluir o cadastro : "+tCodigo.getText()+"\nNome: "+tNome.getText();
					
				int n = JOptionPane.showConfirmDialog(null,nome,"",JOptionPane.YES_NO_OPTION);
				
				if(n == JOptionPane.YES_OPTION)
				{
					String sql = "DELETE FROM fornecedores WHERE codigo = '"+tCodigo.getText()+"'";
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
			tabela.getSelectionModel().clearSelection();
			limpaTable();
			carregaTable();
			
			// limpar todos os campos
			tCodigo.setText("");	
			limpar();
			habilitandoBotoes(4);
				
				
				
				
		} // fim if bExcluir
		
		if(e.getSource() == bLimpar)
		{
			
			limpar();
			
			
		} // fim if bLimpar
		
		if(e.getSource() == bNovo)
		{
		
		
			
			// tornando os textFields editavel
			setandoEditable(true);	
			limpar();
			
			tCodigo.setText("");
			
			// habilitando botoes com parametro 1
			habilitandoBotoes(1);		
		} // fim if bNovo



		
	} // fim ActionListener
	
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
		if(tabela.getSelectedRow() == -1 )
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
			tabela.getSelectionModel().clearSelection();
			return;
		}
		// habilitando botoes com parametro 3
		habilitandoBotoes(3);
		
		
		// seta os textField false pois se durante uma alteração o usuario selecionar a table
		// ele não poderá mais alterar antes de clicar em bAlterar novamente
		setandoEditable(false);

		
		carregarDados();
		
	} // fim valueChanged
	
	/* método para setar os textField editaveis ou não
	 *usado quando o botão alterar ou novo são selecionados
	 *possibilitando alteração de campos
	 */
	public void setandoEditable(boolean editavel)
	{
		cUf.setEnabled(editavel);
	    ftCep.setEditable(editavel);
	    ftCnpj.setEditable(editavel);
	    tNome. setEditable(editavel);
	    tEndereco.setEditable(editavel);
	    tNumero.setEditable(editavel);
	    tBairro.setEditable(editavel);
	    tCidade.setEditable(editavel);
	    ftTelefone.setEditable(editavel);
	    ftCelular.setEditable(editavel);
	    tEmail.setEditable(editavel);
		
		
	}
	
	// método que verifica preenchimento para ser usado na hora de salvar
	public boolean verificaPreenchimento()
	{
		
		//replaces
		stringCNPJ = ftCnpj.getText().replace("/","");
		stringCNPJ = stringCNPJ.replace(".","");
		stringCNPJ = stringCNPJ.replace("-","");

		stringCep = ftCep.getText().replace("-","");
		
		stringTelefone = ftTelefone.getText().replace("-","");
		stringTelefone = stringTelefone.replace("(","");
		stringTelefone = stringTelefone.replace(")","");
		
		stringCelular = ftCelular.getText().replace("-","");
		stringCelular = stringCelular.replace("(","");
		stringCelular = stringCelular.replace(")","");
		
		
		
		// verifica algum preenchimento inválido ou vazio e retorna false
		if(tNome.getText().equals(""))
		{
			JOptionPane.showMessageDialog(null, "Erro!\nPreenchimento do campo nome\nobrigatório",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			tNome.requestFocus();
			return(false);	
		}	
			
		if(stringCNPJ.equals("00000000000000"))
		{
			JOptionPane.showMessageDialog(null, "Erro!\nPreenchimento do campo CNPJ\nobrigatório",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			ftCnpj.requestFocus();
			return(false);	
		}	
		
		if(tEndereco.getText().equals(""))
		{
			JOptionPane.showMessageDialog(null, "Erro!\nPreenchimento do campo Endereço\nobrigatório",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			tEndereco.requestFocus();
			return(false);	
		}
		
		if(tNumero.getText().equals(""))
		{
			JOptionPane.showMessageDialog(null, "Erro!\nPreenchimento do campo Número\nobrigatório",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			tNumero.requestFocus();
			return(false);	
		}
		
		if(tBairro.getText().equals(""))
		{
			JOptionPane.showMessageDialog(null, "Erro!\nPreenchimento do campo bairro\nobrigatório",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			tBairro.requestFocus();
			return(false);	
		}
		
		
			
		if(tCidade.getText().equals(""))
		{
			JOptionPane.showMessageDialog(null, "Erro!\nPreenchimento do campo cidade\nobrigatório",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			tCidade.requestFocus();
			return(false);	
		}
		
		if(stringCep.equals("00000000"))
		{
			JOptionPane.showMessageDialog(null, "Erro!\nPreenchimento do campo cep\nobrigatório",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			ftCep.requestFocus();
			return(false);	
		}
		
		if(cUf.getSelectedIndex() == 0)
		{
			JOptionPane.showMessageDialog(null, "Erro!\nSeleção de estado obrigatória ",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			cUf.requestFocus();
			return(false);	
		}
		
		if(stringTelefone.equals("0000000000") && stringCelular.equals("00000000000"))
		{
			JOptionPane.showMessageDialog(null, "Erro!\nPreenchimento de telefone ou celular\nobrigatório",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			ftTelefone.requestFocus();
			return(false);	
		}
		
		
		
		
		return(true);

	} // fim verificaPreenchimento
	
		// precisa-se limpar campos em mais de um lugar, fica mais facil usar um método para isso
	public void limpar()
	{
		
		cUf.setSelectedIndex(0);
	    ftCep.setValue(null);
	    ftCnpj.setValue(null);
	    tNome.setText(""); 
	    tEndereco.setText("");
	    tNumero.setText("");
	    tBairro.setText("");
	    tCidade.setText("");
	    ftTelefone.setValue(null);
	    ftCelular.setValue(null);
	    tEmail.setText("");
	    
	} // fim limpar()
	
	// método usado para atribuir dados aos textField
	public void carregarDados()
	{
							// verifica o item selecionado
			index = tabela.getSelectedRow();
			
			// verifica se o usuario selecionou alguma coisa, se não ele sai do método sem abrir a janela
			// já que para abrir a mesma precisa-se selecionar um produto
			if(index == -1)
			{
				return;
			}
			
			// converte o index, pois uma simples pesquisa ou reorganização da table muda o index
			index = organizador.convertRowIndexToModel(index);
			
			// pegando código da seleção da table para fazer a consulta no banco
			Object objeto = tabela.getModel().getValueAt(index,0);
			
			
			// transformando código da JTable para string
			aux = objeto.toString();
			// de string para int
			int codigo = Integer.parseInt(aux);
			
			
			
			try
			{
				resultSet = statement.executeQuery("SELECT * FROM fornecedores WHERE codigo ='"+codigo+"'");
				resultSet.next();
				
				//setando valores
				tCodigo.setText(""+codigo);
				tNome.setText(resultSet.getString("nome"));
				
			
					
				ftCnpj.setText(resultSet.getString("cnpj"));
				tEndereco.setText(resultSet.getString("endereco"));
				tNumero.setText(resultSet.getString("numero"));
				tBairro.setText(resultSet.getString("bairro"));
				tCidade.setText(resultSet.getString("cidade"));
				
				
				int selecaoEstado = Integer.parseInt(resultSet.getString("uf"));
				cUf.setSelectedIndex(selecaoEstado);
					
				ftCep.setText(resultSet.getString("cep"));
				ftTelefone.setText(resultSet.getString("telefone"));
				ftCelular.setText(resultSet.getString("celular"));
				tEmail.setText(resultSet.getString("email"));
				
		
							
			} // fim try
			catch(SQLException erro)
			{
				JOptionPane.showMessageDialog(null,"fornecedor não encontrado");
				return;
			}
			
	} // fim método carregarDados
	
	/* método usado para habilitar e desabilitar botões
	 *
	 * bNovo ao ser clicado precisa deselecionar a JTable e habilitar bCancelar, bSalvar e bLimpar
	 * ele precisa também desabilitar,bAlterar,Bnovo, e bExcluir , seu parametro é 1
	 *
	 * bAlterar  ao ser clicado habilita e desabilita os mesmos botôes que bNovo 
	 * porém não deseleciona a JTable, seu parametro é 2
	 *
	 * bCancelar e bSalvar ao serem clicados habilitam somente bNovo se a JTable não estiver selecionada
	 * e habilitam bAlterar, bNovo e bExcluir se a table estiver selecionada,
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
				// deselecionando JTable
				tabela.getSelectionModel().clearSelection();
	
			
			bCancelar.setEnabled(true);
			bSalvar.setEnabled(true);
			bLimpar.setEnabled(true);
			
			bAlterar.setEnabled(false);
			bNovo.setEnabled(false);
			bExcluir.setEnabled(false);
			
			
		} // fim if 1 e 2
		
		
		// 3 parametro de bCancelar e bSalvar
		// e parametro do evento de seleção da JTable
		if(parametro == 3)
		{
			// verifica se a table está ou não selecionada, -1 significa não selecionada
			index = tabela.getSelectedRow();
			
			if(index == -1)
			{
				bNovo.setEnabled(true);
			}
			else
			{
				
				bAlterar.setEnabled(true);
				bNovo.setEnabled(true);
				bExcluir.setEnabled(true);
			}
			
			bSalvar.setEnabled(false);
			bCancelar.setEnabled(false);
			bLimpar.setEnabled(false);
			
		} // fim if 3
		
		if(parametro ==4)
		{
			bSalvar.setEnabled(false);
			bCancelar.setEnabled(false);
			bLimpar.setEnabled(false);
			bAlterar.setEnabled(false);
			bNovo.setEnabled(true);
			bExcluir.setEnabled(false);
		} // fim if 4
		
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
			resultSet = statement.executeQuery("SELECT count(*) FROM fornecedores");
			//verifica a quantidade de cadastros no banco
			while(resultSet.next())
			{
				numeroCadastros = resultSet.getInt(1);
			}
			
			
			// for para carregar table
			for(int i =0; i < numeroCadastros; i++)
			{
				resultSet = statement.executeQuery("SELECT * FROM fornecedores LIMIT "+i+",1");
				resultSet.next();
				
				//atribuindo a pesquisa ao vetor dados para posteriormente atribuir a jTable
				
				dados[0] = resultSet.getString("codigo");
				dados[1] = resultSet.getString("nome");

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