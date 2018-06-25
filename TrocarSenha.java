import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.sql.*;

public class TrocarSenha extends JFrame implements ActionListener
{

	// objetos para usar banco de dados
	ResultSet resultSet;
	Statement statement;

	
	// e labels
	
	JLabel lSenhaAtual,lRepetirNovaSenha,lNovaSenha;
	
	
	//senha
	JPasswordField pSenhaAtual, pNovaSenha, pRepetirNovaSenha;
	
	// botoes
	
	JButton bConfirmar, bCancelar;
	
	// painel
	JPanel p1;
	
	String login;
	
	
	TrocarSenha(String pLogin)
	{
		setTitle("Trocar Senha");
		setSize(245,290);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		
		
		setIconImage(new ImageIcon("Imagens/troca_usu.png").getImage());
		
		login = pLogin;		
		
		// painel
		
		p1 = new JPanel();
		p1.setLayout(null);
		p1.setBackground(new Color(192,192,192));
		getContentPane().add(p1);
		
		// textfield labels e password
		// senha atual
		lSenhaAtual = new JLabel("Senha");
		lSenhaAtual.setBounds(10,10,100,20);
		p1.add(lSenhaAtual);
		
		pSenhaAtual = new JPasswordField();
		pSenhaAtual.setBounds(10,30,210,30);
		pSenhaAtual.setEchoChar('*');
		p1.add(pSenhaAtual);
		
		
	    // nova senha
		
		lNovaSenha = new JLabel("Nova Senha");
		lNovaSenha.setBounds(10,70,100,20);
		p1.add(lNovaSenha);
		
		pNovaSenha = new JPasswordField();
		pNovaSenha.setBounds(10,90,210,30);
		pNovaSenha.setEchoChar('*');
		p1.add(pNovaSenha);
		
		// repetir nova senha 
		
		lRepetirNovaSenha = new JLabel("Repetir Nova Senha");
		lRepetirNovaSenha.setBounds(10,130,150,20);
		p1.add(lRepetirNovaSenha);
		
		pRepetirNovaSenha = new JPasswordField();
		pRepetirNovaSenha.setBounds(10,150,210,30);
		pRepetirNovaSenha.addActionListener(this);
		pRepetirNovaSenha.setEchoChar('*');
		p1.add(pRepetirNovaSenha);
	
		
		
		// botoes
		
		bConfirmar = new JButton("Confirmar");
		bConfirmar.setBounds(10,190,100,30);
		bConfirmar.addActionListener(this);
		bConfirmar.setMnemonic(KeyEvent.VK_C);
		p1.add(bConfirmar);
		
		bCancelar = new JButton("Cancelar");
		bCancelar.setBounds(120,190,100,30);
		bCancelar.addActionListener(this);
		bCancelar.setMnemonic(KeyEvent.VK_A);
		p1.add(bCancelar);
		
		carregaResultSet();
		
		
		
		
	} // fim do construtor
	
	public void actionPerformed(ActionEvent e)
	{
		
		
		
		
		// se a ação vier do ultimo campo de senha ou do botão confirmar faça
		if(e.getSource() == pRepetirNovaSenha || e.getSource() == bConfirmar)
		{	
			//sair do método se retornar false
			if(!verificarSenha())
			{
				return;
			}
			
			try
			{
				String sql = "UPDATE usuarios SET "+
					"senha = '" + new String(pNovaSenha.getPassword()) +"'"+
					"WHERE login = '" +login+"'";
				
	
			
				int r = statement.executeUpdate(sql);
			
				if(r==1)
				{
					JOptionPane.showMessageDialog(null, "Alteração realizada com sucesso");
					setVisible(false);
				
			
						
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
			
		} // fim do if pRepetirNovaSenha ou botao bConfirmar
		
		
		
		if(e.getSource() == bCancelar)
		{
			setVisible(false);	
			
		} // fim bCancelar
		
		
	} // fim actionListener
	
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
	
	
	
	public boolean verificarSenha()
	{
		
		try
		{
			resultSet = statement.executeQuery("SELECT * FROM usuarios WHERE login ='"+login+"'");
			resultSet.next();
			
	
			//pegando senha do banco para comparação
			String senha = resultSet.getString("senha");
			
			//verificando senha com base no usuario
			if(!new String(pSenhaAtual.getPassword()).equals(senha))
			{
				JOptionPane.showMessageDialog(null,"Senha inválida");
				pSenhaAtual.setText("");
				pNovaSenha.setText("");
				pRepetirNovaSenha.setText("");
				pSenhaAtual.requestFocus();
				return(false);
				
			}
			
	
	
						
		} // fim try
		catch(SQLException erro)
		{
		}

		// verifica se a nova senha foi repetida corretamente
		if(!new String(pNovaSenha.getPassword()).equals(new String(pRepetirNovaSenha.getPassword())))
		{
			JOptionPane.showMessageDialog(null,"Senha repetida incorretamente!",
				"Aviso", JOptionPane.INFORMATION_MESSAGE);
				
			pNovaSenha.setText("");
			pRepetirNovaSenha.setText("");
			pNovaSenha.requestFocus();
			return(false);
		}
		
		if(new String(pNovaSenha.getPassword()).length() < 5)
		{
			JOptionPane.showMessageDialog(null,"Nova senha precisa ter pelo menos 5 caracteres",
				"Aviso", JOptionPane.INFORMATION_MESSAGE);
			pNovaSenha.setText("");
			pRepetirNovaSenha.setText("");
			pNovaSenha.requestFocus();
			return(false);
				
		}
	
	
		
		// se não cair na exceção ou no if é verdadeira a senha
		return(true);
		
	} // fim vericarSenha
	
} // fim da classe