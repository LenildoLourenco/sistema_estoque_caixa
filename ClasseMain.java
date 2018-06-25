import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class ClasseMain
{
	public static void main(String[]args)
	{
		// parâmetro para mudar o título do frame pois a mesma frame é usada para logar e trocar de usuário
		Login janela = new Login(false, "Login","","");
		janela.setUndecorated(true);
		janela.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.setVisible(true);
		janela.setResizable(false);
		
	}
}