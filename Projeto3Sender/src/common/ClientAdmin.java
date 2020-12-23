package common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import classes.AppUser;
import classes.AsyncReceiver;
import classes.Publication;
import classes.Sender;
import main.actionbeanRemote;

public class ClientAdmin {
	
	//MENU DE ADMIN
			public static void main(String[] args) throws NamingException {
				boolean done  = false;
			    Scanner scanner = new Scanner(System.in);  // Create a Scanner object
			    ArrayList<String> tasks = new ArrayList<String>();
				String AppUser,bookname;
				actionbeanRemote ejb = null;
				//criar queues
				Context context;
				Properties jndiProperties = new Properties();

				jndiProperties.setProperty("java.naming.factory.initial", "org.jboss.naming.remote.client.InitialContextFactory");

				jndiProperties.setProperty("java.naming.provider.url","http-remoting://localhost:8080");

				jndiProperties.setProperty("jboss.naming.client.ejb.context","true");
				
				try {
					
					context=new InitialContext(jndiProperties);
					ejb = (actionbeanRemote) context.lookup("Projeto3Beans/actionbean!main.actionbeanRemote");
					
			    	
				}catch (NamingException e) {

					// TODO Auto-generated catch block

					e.printStackTrace();

					}
				


				while(!done) {
					System.out.println("****************JMS App****************");
					System.out.println("Escolha uma opcao: ");
					System.out.println("(0) List AppUsers ");
					System.out.println("(1) List Pending Tasks ");
					System.out.println("(2) Deactivate AppUser ");
					System.out.println("(3) List Publications ");
					System.out.println("(4) Search Publication ");
					System.out.println("(5) Logout ");
					System.out.println("\n");

					int option = lerInt(0, 6);

					//LIST ALL AppUserS
					if(option==0) {
						List<AppUser> mylist = ejb.GetallAppUsers();
						printallAppUsers(mylist);
					}


					//LIST PENDING TASKS
					if(option==1) {
						String task="";
						AsyncReceiver asyncReceiver = new AsyncReceiver("queue/AddQueue");
						while (task!=null) {
							task = asyncReceiver.receive();
							if(task!=null) {
								tasks.add(task);	
							}
						}
						System.out.println("****************TASKS JMS****************");
						PrintTasks(tasks);
						if(tasks.size()==0) {
							System.out.println("No tasks!");
						}
						System.out.print("Choose a pending task (Type 0 to exit): ");
						int option2=lerInt(0,tasks.size()+1);
						if(option2!=0) {
							SelectTask(tasks,option2-1,ejb);
						}

						Sender sender=new Sender("queue/AddQueue");
						//Se não fizer nada voltar a mandar para a queue para não se perderem e outro admin puder executa-las
						for(int j=0;j<tasks.size();j++) {
							sender.send(tasks.get(j));
						}
						tasks.clear();
						
					}

					//DEACTIVATE AppUser
					if(option==2) {
						System.out.println("****************DEACTIVATE JMS****************");
						System.out.print("Nome do AppUser (Type nothing to exit): ");
					    AppUser = scanner.nextLine();  // Read AppUser input
						System.out.println("\n");
					    if(!AppUser.equals("")) {
					    	String status = ejb.ActivateAppUser(AppUser);
							System.out.println("O User foi "+ status +"!");
					    }
					}

					//LIST PUBLICATIONS
					if(option==3) {
						printallpubs(ejb.GetallPubs());
					}

					//SEARCH PUBLICATION
					if(option==4) {
						System.out.println("****************SEARCH JMS****************");
						System.out.print("Nome da publicacao (Type nothing to exit): ");
					    bookname = scanner.nextLine();  // Read AppUser input
						System.out.println("\n");
						if(!bookname.equals("")) {
							printallpubs(ejb.GetPublicationByNome(bookname));	
						}
					}

					//EXIT
					if(option==5) {
						System.out.println("You have successfully logged out.");
		                done = true;
					}
				}
			}
			
//=========================AUX=============================================================

			
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
			
	


	//=========================PRINT DATABASE INFO=============================================================
			//PRINT ALL AppUserS INFO
			public static void printallAppUsers(List<AppUser> mylist) {
				
				for(int i=0;i<mylist.size();i++) {
					System.out.println("Utilizador " + (i+1) +":");
					System.out.println("AppUsername: " + mylist.get(i).getUsername());
					System.out.println("Activated: " + String.valueOf(mylist.get(i).isActivated()));
					System.out.println("\n");
				}
				System.out.println("\n\n");
			}
			
			
			//PRINT ALL PUBS INFO
			public static void printallpubs(List<Publication> mylist) {
				for(int i=0;i<mylist.size();i++) {
					System.out.println("Publication " + (i+1) + ":");
					System.out.println("Name: " + mylist.get(i).getName());
					System.out.println("Type: " + mylist.get(i).getType());
					System.out.println("Date: " + mylist.get(i).getDate());
					System.out.print("\n");
				}
				System.out.println("\n");
			}


	//=========================PENDING TASKS AUX=================================================================
			

			

			//PRINT ALL TASKS
			public static void PrintTasks(ArrayList<String> tasks) {
				for(int i=0;i<tasks.size();i++) {
					String[] tokens = tasks.get(i).split(":");
					System.out.println("("+(i+1)+") " + tokens[0] + ": " + tokens[1]);
				}
			}

			//SELECT A TASK
			//RETURNS ARRAYLIST WITH DECLINED TASK OR ACCEPTED
			public static ArrayList<String> SelectTask(ArrayList<String> tasks, int choice,actionbeanRemote ejb) throws NamingException {
			    Scanner scanner = new Scanner(System.in);  // Create a Scanner object
			    boolean done=false;
			    Sender a = null;
				System.out.println("Task selected: " + tasks.get(choice));
				while(!done) {
					System.out.println("Do you want to accept or decline?");
					System.out.println("Type 'exit' to cancel");
				    String answer = scanner.nextLine();  // Read AppUser input
					if(answer.compareTo("accept")==0) {
						a = new Sender("topic/playTopic");
						DoTask(tasks.get(choice),ejb);
						
						
						if(tasks.get(choice).startsWith("Registo")) {
							a.note_ALL("USER REGISTADO");
						}else {
							a.note_ALL( tasks.get(choice) + " ACEITE PELO ADMIN!\n");
						}
						tasks.remove(choice);
						done=true;
					}
					else if(answer.compareTo("decline")==0) {
						tasks.remove(choice);
						done=true;
					}
					else if(answer.compareTo("exit")==0) {
						done=true;
					}
					else {
						System.out.println("Please type 'accept' or 'decline'");
					}
				}
				return tasks;
			}


			//PARSE TASK AND EXECUTE
			public static void DoTask(String task,actionbeanRemote ejb) {
				String[] tokens = task.split(":");
				//REGISTAR(Registo:AppUsername:Password)
				if(tokens[0].compareTo("Registo")==0) {
					AppUser novo= new AppUser(tokens[1],tokens[2]);
					ejb.AddAppUser(novo);
					System.out.println("Utilizador " + tokens[1] + " adicionado com sucesso!");
					//Mandar mensagem de volta ao AppUser
				}
				//ADICIONAR(Adicionar:TestePub:Book:March 2013)
				else if(tokens[0].compareTo("Adicionar")==0) {
					Publication novo = new Publication(tokens[1],tokens[2],tokens[3]);
					ejb.AddPublication(novo);
					System.out.println("Publication " + tokens[1] + " adicionada com sucesso!");
					//Mandar notificacao para todos
				}
				//UPDATE(Update:TestePub:TestePubv2:Book:March 2014)
				else if(tokens[0].compareTo("Update")==0) {
					Publication updated=new Publication(tokens[2],tokens[3],tokens[4]);
					String oldname=tokens[1];
					ejb.UpdatePublication(updated,oldname);
					System.out.println("Publication atualizada!");
					//Mandar notificacao para todos
				}
				//REMOVE(Remover:TestePubv2)
				else if(tokens[0].compareTo("Remover")==0) {
					ejb.RemovePublication(tokens[1]);
					System.out.println("Publication removida!");
					//Mandar notificacao para todos
				}
				else {
					System.out.println("Something went wrong with parsing! :(");
				}
			}
	}



