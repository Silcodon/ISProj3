package common;

import java.util.Scanner;

import javax.jms.JMSException;
import javax.naming.NamingException;

import classes.AsyncReceiver;
import classes.Sender;

public class ClientResearcher {
	public static void main(String[] args) {
		String username,password;
	    Scanner scanner = new Scanner(System.in);  // Create a Scanner object
		boolean done  = false;
		while(!done) {
			System.out.println("****************JMS****************");
			System.out.println("Escolha uma opcao: ");
			System.out.println("(0) Register ");
			System.out.println("(1) Login ");
			System.out.println("(2) Exit ");
			System.out.println("\n");

			int option = lerInt(0, 3);
			
			//REGISTER
			if(option==0) {
				System.out.println("****************Register JMS****************");
				System.out.print("Escolha um username: ");
			    username = scanner.nextLine();  // Read user input
				System.out.print("Escolha uma password: ");
				password = scanner.nextLine();  // Read user input
				System.out.println("\n");
				//Enviar msg ao admin para confirmar registo
				
			}
			
			//LOGIN
			if(option==1) {
				System.out.println("****************Login JMS****************");
				System.out.print("Username: ");
			    username = scanner.nextLine();  // Read user input
				System.out.print("Password: ");
				password = scanner.nextLine();  // Read user input
				System.out.println("\n");
				//Enviar msg ao admin para login
				
				//Após confirmar aceder à app
			}
			
			//EXIT
			if(option==2) {
				System.out.println("You have successfully exited the Application.");
                done = true;
			}
		}
	}
	
	
	//LER INT DE STDIN
		public static int lerInt(int init, int fin ) {
			Scanner scan = new Scanner(System.in);
			int num = 0;
			boolean aux = true;
			System.out.print("Introduza a opcao: ");
			while (aux) {
				if (scan.hasNextInt()) {
					num = scan.nextInt();
					if (num>=init && num< fin) {
						aux = false;
					}
					else {
						System.out.print("Invalid option. Try again: ");
					}
				}
				else {
					System.out.print("Invalid option. Try again: ");
					scan.next();
				}
			}
			return num;
		}
		
		
		//MENU DE USER
		public void appoption() {
			boolean done  = false;
		    Scanner scanner = new Scanner(System.in);  // Create a Scanner object
			String bookname,type,date;
			while(!done) {
				System.out.println("****************JMS App****************");
				System.out.println("Escolha uma opcao: ");
				System.out.println("(0) List Publications ");
				System.out.println("(1) Search Publication ");
				System.out.println("(2) Add Publication ");
				System.out.println("(3) Update Publication ");
				System.out.println("(4) Remove Publication ");
				System.out.println("(5) Logout ");
				System.out.println("\n");

				int option = lerInt(0, 6);
				
				//LIST ALL PUBLICATIONS
				if(option==0) {
					//Enviar msg ao admin
				}
				
				
				//SEARCH PUBLICATION BY NAME
				if(option==1) {
					System.out.println("****************SEARCH JMS****************");
					System.out.print("Nome da publicacao: ");
				    bookname = scanner.nextLine();  // Read user input
					System.out.println("\n");
					//Enviar mensagem a pedir info ao admin
				}
				
				//ADD PUBLICATION
				if(option==2) {
					System.out.println("****************ADD JMS****************");
					System.out.print("Nome da publicacao: ");
				    bookname = scanner.nextLine();  // Read user input
				    System.out.print("Tipo da publicacao: ");
				    type = scanner.nextLine();  // Read user input
				    System.out.print("Data da publicacao (Month Year): ");
				    date = scanner.nextLine();  // Read user input
					System.out.println("\n");
					//Enviar mensagem a pedir info ao admin
				}
				
				//UPDATE PUBLICATION
				if(option==3) {
					System.out.println("****************UPDATE JMS****************");
					System.out.print("Nome da publicacao: ");
				    bookname = scanner.nextLine();  // Read user input
				    System.out.print("Tipo da publicacao: ");
				    type = scanner.nextLine();  // Read user input
				    System.out.print("Data da publicacao (Month Year): ");
				    date = scanner.nextLine();  // Read user input
					System.out.println("\n");
					//Enviar mensagem a pedir info ao admin
				}
				
				//REMOVE PUBLICATION
				if(option==4) {
					System.out.println("****************REMOVE JMS****************");
					System.out.print("Nome da publicacao: ");
				    bookname = scanner.nextLine();  // Read user input
					System.out.println("\n");
					//Enviar mensagem a pedir info ao admin
				}
				
				//EXIT
				if(option==5) {
					System.out.println("You have successfully logged out.");
	                done = true;
				}
			}
		}
		
		//RECEIVE A MESSAGE
		public void printresponse() throws NamingException {
			AsyncReceiver asyncReceiver = new AsyncReceiver();
			asyncReceiver.launch_and_wait();
		}
		
		//RECEIVE AND REPLY
		public void printandrespond() throws NamingException, JMSException {
			AsyncReceiver asyncReceiver = new AsyncReceiver();
			asyncReceiver.receive_and_reply();
		}
		
		
		//SEND A MESSAGE
		public void sendmessage(String message) throws NamingException {
			Sender sender = new Sender();
			sender.send(message);
		}
		
		//SEND MESSAGE AND PRINT RESPONSE
		public void sendandreceive(String message) throws NamingException {
			Sender sender = new Sender();
			sender.send_and_reply(message);
		}
}
