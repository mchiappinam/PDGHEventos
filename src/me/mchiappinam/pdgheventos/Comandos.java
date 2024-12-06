package me.mchiappinam.pdgheventos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class Comandos implements CommandExecutor {
	private static Main plugin;
	static int ttasksP;
	static int tfarol;
	
	public Comandos(Main main) {
		plugin=main;
	}
	//private static FrogBlockQueue classeFBQ;
	
	public Comandos(FrogBlockQueue fbq) {
		//classeFBQ=fbq;
	}
	
	static BukkitTask efrog;
	static boolean first=true;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("evento")) {
			if(args.length==0) {
				if(!plugin.lobby) {
					sender.sendMessage("§d[Evento Automático] §cO lobby de eventos não está aberto.");
					sender.sendMessage("§d[Evento Automático] §cPara ajudas digite §l/evento ajuda");
					return true;
				}
				if((plugin.participantes.contains(sender.getName().toLowerCase()))||(plugin.eliminados.contains(sender.getName().toLowerCase()))) {
					sender.sendMessage("§d[Evento Automático] §cVocê já está nos eventos.");
					return true;
				}
				if(!plugin.confirmEntrarEvento.contains(sender.getName().toLowerCase())) {
					plugin.confirmEntrarEvento.add(sender.getName().toLowerCase());
					sender.sendMessage("§d[Evento Automático]");
					sender.sendMessage("§cVocê está prestes a entrar no lobby dos eventos.");
					sender.sendMessage("§cCaso não saiba como funciona, digite /ajuda eventos");
					sender.sendMessage("§cTem certeza que deseja entrar no lobby dos eventos?");
					sender.sendMessage("§cTodos os comandos são bloqueados no lobby dos eventos!");
					sender.sendMessage("§aPara entrar digite novamente o comando §l/evento");
					sender.sendMessage("§d[Evento Automático]");
					return true;
				}else
					plugin.confirmEntrarEvento.remove(sender.getName().toLowerCase());
    			for(String p : plugin.participantes) {
    				plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §f"+sender.getName()+" entrou. §e(§a+1§e/"+(plugin.participantes.size()+1)+")");
    			}
	            plugin.participantes.add(sender.getName().toLowerCase());
	            Location lobby=new Location(plugin.getServer().getWorld("world_eventos"), 0.5, 52, 0.5);
	            lobby.setPitch(0);
	            lobby.setYaw(180);
				((Player)sender).teleport(lobby);
				sender.sendMessage("§d[Evento Automático]");
				sender.sendMessage("§fTeleportado! §e(§a+1§e/"+plugin.participantes.size()+")");
				sender.sendMessage("§fO evento já irá começar!");
				sender.sendMessage("§fPara sair digite /evento sair");
				sender.sendMessage("§d[Evento Automático]");
				return true;
			}
			if(args[0].equalsIgnoreCase("sair")) {
				if(args.length>1) {
					plugin.help((Player)sender);
					return true;
				}
				if(!plugin.participantes.contains(sender.getName().toLowerCase())&&!plugin.eliminados.contains(sender.getName().toLowerCase())) {
					sender.sendMessage("§d[Evento Automático] §cVocê não está no evento.");
					return true;
				}
				if(plugin.participantes.contains(sender.getName().toLowerCase())) {
					plugin.participantes.remove(sender.getName().toLowerCase());
					
					if(plugin.participantes.size()<=2) {
						if(!plugin.lobby) {
				    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
				    		plugin.getServer().broadcastMessage("§f"+sender.getName()+" foi o último a desconectar!");
				    		plugin.getServer().broadcastMessage("§fFim do evento! Sem vencedores!");
				    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
						
				    		Comandos.cancelarFrogSR();
				    		return true;
						}
		   			}
	    			for(String p : plugin.participantes) {
	    				plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §f"+sender.getName()+" saiu. §e(§c-1§e/"+plugin.participantes.size()+")");
	    			}
				}
				if(plugin.eliminados.contains(sender.getName().toLowerCase()))
					plugin.eliminados.remove(sender.getName().toLowerCase());
				
				if (plugin.w!=null) {
	    	    	((Player)sender).teleport(plugin.w.getSpawnLocation());
	    	    	sender.sendMessage("§d[Evento Automático] §fVocê saiu.");
	    	    }else{
	    	    	sender.sendMessage("§cOcorreu um erro. Notifique alguém da STAFF.");
	    	    }
				return true;
			}else if(args[0].equalsIgnoreCase("loteria")) {
				if(args.length>2) {
					plugin.help((Player)sender);
					return true;
				}
				if(plugin.evento!="Loteria") {
					sender.sendMessage("§d[Evento Automático] §cEvento loteria não acontecendo no momento.");
					return true;
				}
				/**if(!args[1].matches("[0-9]")) {
					sender.sendMessage("§d[Evento Automático] §cApenas números de 0 à 300.");
					return true;
				}*/
				try{
					if (StringUtils.isNumeric(args[1])) {
						int valor=Integer.parseInt(args[1]);
						if((valor>=0)&&(valor<=300)) {
							if(plugin.loteriaValor==valor) {
					    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
				  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
							    plugin.getServer().broadcastMessage("§f"+sender.getName()+" venceu!");
							    plugin.getServer().broadcastMessage("§fO valor era §l"+valor);
							    plugin.getServer().broadcastMessage("§fFim do evento!");
							    plugin.getServer().broadcastMessage("§d[Evento Automático]");
							    if(plugin.premio!=0) {
							    	sender.sendMessage("§d[Evento Automático] §fVocê recebeu $"+plugin.premio+" coins.");
							    	Main.econ.depositPlayer(sender.getName(), plugin.premio);
							    }
								cancelarEventoLoteria();
							}else{
								sender.sendMessage("§d[Evento Automático] §cContinue tentando! O valor não é "+valor);
								return true;
							}
						}else{
							sender.sendMessage("§d[Evento Automático] §cApenas números de 0 à 300.");
							return true;
						}
					}else{
						sender.sendMessage("§d[Evento Automático] §cApenas números de 0 à 300.");
						return true;
					}
				}catch (NumberFormatException nfe){
					sender.sendMessage("§d[Evento Automático] §cApenas números de 0 à 300.");
					return true;
				}
				return true;
			}else if(args[0].equalsIgnoreCase("iniciar")) {
				if(!sender.hasPermission("pdgh.admin")) {
					plugin.noPerm((Player)sender);
					return true;
				}
				if(args.length==1) {
					plugin.helpAdmin((Player)sender);
					return true;
				}
				if(args[1].equalsIgnoreCase("frogsr")) {
					if(args.length>2) {
						plugin.helpAdmin((Player)sender);
						return true;
					}
					if(!plugin.frogsr) {
						sender.sendMessage("§d[Evento Automático] §cEvento desativado.");
						return true;
					}
					if(plugin.evento!="nenhum") {
						if(plugin.lobby)
							sender.sendMessage("§d[Evento Automático] §cLobby aberto para o evento "+plugin.evento.replace("SR", "").replace("CR", "")+".");
						else
							sender.sendMessage("§d[Evento Automático] §cEvento "+plugin.evento.replace("SR", "").replace("CR", "")+" ocorrendo no momento.");
						return true;
					}
					plugin.cteventoauto();
					plugin.evento="FrogSR";
					startEventoSemRisco();
					return true;
				}else if(args[1].equalsIgnoreCase("frogcr")) {
					if(args.length>2) {
						plugin.helpAdmin((Player)sender);
						return true;
					}
					if(!plugin.frogcr) {
						sender.sendMessage("§d[Evento Automático] §cEvento desativado.");
						return true;
					}
					if(plugin.evento!="nenhum") {
						if(plugin.lobby)
							sender.sendMessage("§d[Evento Automático] §cLobby aberto para o evento "+plugin.evento.replace("SR", "").replace("CR", "")+".");
						else
							sender.sendMessage("§d[Evento Automático] §cEvento "+plugin.evento.replace("SR", "").replace("CR", "")+" ocorrendo no momento.");
						return true;
					}
					plugin.cteventoauto();
					plugin.evento="FrogCR";
					startEventoComRisco("§2OFF");
					return true;
				}else if(args[1].equalsIgnoreCase("spleefsr")) {
					if(args.length>2) {
						plugin.helpAdmin((Player)sender);
						return true;
					}
					if(!plugin.spleefsr) {
						sender.sendMessage("§d[Evento Automático] §cEvento desativado.");
						return true;
					}
					if(plugin.evento!="nenhum") {
						if(plugin.lobby)
							sender.sendMessage("§d[Evento Automático] §cLobby aberto para o evento "+plugin.evento.replace("SR", "").replace("CR", "")+".");
						else
							sender.sendMessage("§d[Evento Automático] §cEvento "+plugin.evento.replace("SR", "").replace("CR", "")+" ocorrendo no momento.");
						return true;
					}
					plugin.cteventoauto();
					plugin.evento="SpleefSR";
					startEventoSemRisco();
					return true;
				}else if(args[1].equalsIgnoreCase("spleefcr")) {
					if(args.length>2) {
						plugin.helpAdmin((Player)sender);
						return true;
					}
					if(!plugin.spleefcr) {
						sender.sendMessage("§d[Evento Automático] §cEvento desativado.");
						return true;
					}
					if(plugin.evento!="nenhum") {
						if(plugin.lobby)
							sender.sendMessage("§d[Evento Automático] §cLobby aberto para o evento "+plugin.evento.replace("SR", "").replace("CR", "")+".");
						else
							sender.sendMessage("§d[Evento Automático] §cEvento "+plugin.evento.replace("SR", "").replace("CR", "")+" ocorrendo no momento.");
						return true;
					}
					plugin.cteventoauto();
					plugin.evento="SpleefCR";
					startEventoComRisco("§2OFF");
					return true;
				}else if(args[1].equalsIgnoreCase("maratonacr")) {
					if(args.length>2) {
						plugin.helpAdmin((Player)sender);
						return true;
					}
					if(!plugin.maratonacr) {
						sender.sendMessage("§d[Evento Automático] §cEvento desativado.");
						return true;
					}
					if(plugin.evento!="nenhum") {
						if(plugin.lobby)
							sender.sendMessage("§d[Evento Automático] §cLobby aberto para o evento "+plugin.evento.replace("SR", "").replace("CR", "")+".");
						else
							sender.sendMessage("§d[Evento Automático] §cEvento "+plugin.evento.replace("SR", "").replace("CR", "")+" ocorrendo no momento.");
						return true;
					}
					plugin.cteventoauto();
					plugin.evento="MaratonaCR";
					startEventoComRisco("§2OFF");
					return true;
				}else if(args[1].equalsIgnoreCase("farolsr")) {
					if(args.length>2) {
						plugin.helpAdmin((Player)sender);
						return true;
					}
					if(!plugin.farolsr) {
						sender.sendMessage("§d[Evento Automático] §cEvento desativado.");
						return true;
					}
					if(plugin.evento!="nenhum") {
						if(plugin.lobby)
							sender.sendMessage("§d[Evento Automático] §cLobby aberto para o evento "+plugin.evento.replace("SR", "").replace("CR", "")+".");
						else
							sender.sendMessage("§d[Evento Automático] §cEvento "+plugin.evento.replace("SR", "").replace("CR", "")+" ocorrendo no momento.");
						return true;
					}
					plugin.cteventoauto();
					plugin.evento="FarolSR";
					startEventoSemRisco();
					return true;
				}else if(args[1].equalsIgnoreCase("parkourcr")) {
					if(args.length>2) {
						plugin.helpAdmin((Player)sender);
						return true;
					}
					if(!plugin.parkourcr) {
						sender.sendMessage("§d[Evento Automático] §cEvento desativado.");
						return true;
					}
					if(plugin.evento!="nenhum") {
						if(plugin.lobby)
							sender.sendMessage("§d[Evento Automático] §cLobby aberto para o evento "+plugin.evento.replace("SR", "").replace("CR", "")+".");
						else
							sender.sendMessage("§d[Evento Automático] §cEvento "+plugin.evento.replace("SR", "").replace("CR", "")+" ocorrendo no momento.");
						return true;
					}
					plugin.cteventoauto();
					plugin.evento="ParkourCR";
					startEventoComRisco("§2OFF");
					return true;
				}else if(args[1].equalsIgnoreCase("bau")) {
					if(args.length>2) {
						plugin.helpAdmin((Player)sender);
						return true;
					}
					if(!plugin.bau) {
						sender.sendMessage("§d[Evento Automático] §cEvento desativado.");
						return true;
					}
					if(plugin.evento!="nenhum") {
						if(plugin.lobby)
							sender.sendMessage("§d[Evento Automático] §cLobby aberto para o evento "+plugin.evento.replace("SR", "").replace("CR", "")+".");
						else
							sender.sendMessage("§d[Evento Automático] §cEvento "+plugin.evento.replace("SR", "").replace("CR", "")+" ocorrendo no momento.");
						return true;
					}
					plugin.cteventoauto();
					plugin.evento="Bau";
					startEventoBau();
					return true;
				}else if(args[1].equalsIgnoreCase("paz")) {
					if(args.length>2) {
						plugin.helpAdmin((Player)sender);
						return true;
					}
					if(!plugin.paz) {
						sender.sendMessage("§d[Evento Automático] §cEvento desativado.");
						return true;
					}
					if(plugin.evento!="nenhum") {
						if(plugin.lobby)
							sender.sendMessage("§d[Evento Automático] §cLobby aberto para o evento "+plugin.evento.replace("SR", "").replace("CR", "")+".");
						else
							sender.sendMessage("§d[Evento Automático] §cEvento "+plugin.evento.replace("SR", "").replace("CR", "")+" ocorrendo no momento.");
						return true;
					}
					plugin.cteventoauto();
					plugin.evento="Paz";
					startEventoPaz();
					return true;
				}else if(args[1].equalsIgnoreCase("loteria")) {
					if(args.length>2) {
						plugin.helpAdmin((Player)sender);
						return true;
					}
					if(!plugin.loteria) {
						sender.sendMessage("§d[Evento Automático] §cEvento desativado.");
						return true;
					}
					if(plugin.evento!="nenhum") {
						if(plugin.lobby)
							sender.sendMessage("§d[Evento Automático] §cLobby aberto para o evento "+plugin.evento.replace("SR", "").replace("CR", "")+".");
						else
							sender.sendMessage("§d[Evento Automático] §cEvento "+plugin.evento.replace("SR", "").replace("CR", "")+" ocorrendo no momento.");
						return true;
					}
					plugin.cteventoauto();
					plugin.evento="Loteria";
					startEventoLoteria();
					return true;
				}else if(args[1].equalsIgnoreCase("drop")) {
					if(args.length>2) {
						plugin.helpAdmin((Player)sender);
						return true;
					}
					if(!plugin.drop) {
						sender.sendMessage("§d[Evento Automático] §cEvento desativado.");
						return true;
					}
					if(plugin.evento!="nenhum") {
						if(plugin.lobby)
							sender.sendMessage("§d[Evento Automático] §cLobby aberto para o evento "+plugin.evento.replace("SR", "").replace("CR", "")+".");
						else
							sender.sendMessage("§d[Evento Automático] §cEvento "+plugin.evento.replace("SR", "").replace("CR", "")+" ocorrendo no momento.");
						return true;
					}
					plugin.cteventoauto();
					plugin.evento="Drop";
					startEventoDrop();
					return true;
				}else if(args[1].equalsIgnoreCase("aleatorio")) {
					if(args.length>2) {
						plugin.helpAdmin((Player)sender);
						return true;
					}
					if(plugin.evento!="nenhum") {
						if(plugin.lobby)
							sender.sendMessage("§d[Evento Automático] §cLobby aberto para o evento "+plugin.evento.replace("SR", "").replace("CR", "")+".");
						else
							sender.sendMessage("§d[Evento Automático] §cEvento "+plugin.evento.replace("SR", "").replace("CR", "")+" ocorrendo no momento.");
						return true;
					}
					plugin.cteventoauto();
					sender.sendMessage("§d[Evento Automático] §fIniciando evento aleatório automático...");
					plugin.startEventoAleatorio();
					return true;
				}
				if(args.length>0) {
					plugin.helpAdmin((Player)sender);
					return true;
				}
				return true;
			}else if(args[0].equalsIgnoreCase("cancelar")) {
				if(args.length>2) {
					plugin.helpAdmin((Player)sender);
					return true;
				}
				if(!sender.hasPermission("pdgh.admin")) {
					plugin.noPerm((Player)sender);
					return true;
				}
				if(plugin.evento=="nenhum") {
					sender.sendMessage("§d[Evento Automático] §cNenhum evento ocorrendo no momento.");
					return true;
				}
				if(plugin.evento=="FrogSR") {
					plugin.getServer().broadcastMessage("§d[Evento Automático]");
					plugin.getServer().broadcastMessage("§cCancelando evento Frog...");
					plugin.getServer().broadcastMessage("§d[Evento Automático]");
					cancelarFrogSR();
					sender.sendMessage("§d[Evento Automático] §aFrog cancelado com sucesso.");
  				}
				if(plugin.evento=="FrogCR") {
					plugin.getServer().broadcastMessage("§d[Evento Automático]");
					plugin.getServer().broadcastMessage("§cCancelando evento Frog...");
					plugin.getServer().broadcastMessage("§d[Evento Automático]");
					cancelarFrogCR();
					sender.sendMessage("§d[Evento Automático] §aFrog cancelado com sucesso.");
  				}
				if(plugin.evento=="SpleefSR") {
					plugin.getServer().broadcastMessage("§d[Evento Automático]");
					plugin.getServer().broadcastMessage("§cCancelando evento Spleef...");
					plugin.getServer().broadcastMessage("§d[Evento Automático]");
					cancelarSpleefSR();
					sender.sendMessage("§d[Evento Automático] §aSpleef cancelado com sucesso.");
  				}
				if(plugin.evento=="SpleefCR") {
					plugin.getServer().broadcastMessage("§d[Evento Automático]");
					plugin.getServer().broadcastMessage("§cCancelando evento Spleef...");
					plugin.getServer().broadcastMessage("§d[Evento Automático]");
					cancelarSpleefCR();
					sender.sendMessage("§d[Evento Automático] §aSpleef cancelado com sucesso.");
  				}
				if(plugin.evento=="MaratonaCR") {
					plugin.getServer().broadcastMessage("§d[Evento Automático]");
					plugin.getServer().broadcastMessage("§cCancelando evento Maratona...");
					plugin.getServer().broadcastMessage("§d[Evento Automático]");
					cancelarMaratonaCR();
					sender.sendMessage("§d[Evento Automático] §aMaratona cancelado com sucesso.");
  				}
				if(plugin.evento=="FarolSR") {
					plugin.getServer().broadcastMessage("§d[Evento Automático]");
					plugin.getServer().broadcastMessage("§cCancelando evento Farol...");
					plugin.getServer().broadcastMessage("§d[Evento Automático]");
					cancelarFarolSR();
					sender.sendMessage("§d[Evento Automático] §aFarol cancelado com sucesso.");
  				}
				if(plugin.evento=="ParkourCR") {
					plugin.getServer().broadcastMessage("§d[Evento Automático]");
					plugin.getServer().broadcastMessage("§cCancelando evento Parkour...");
					plugin.getServer().broadcastMessage("§d[Evento Automático]");
					cancelarParkourCR();
					sender.sendMessage("§d[Evento Automático] §aParkour cancelado com sucesso.");
  				}
				if(plugin.evento=="Bau") {
					plugin.getServer().broadcastMessage("§d[Evento Automático]");
					plugin.getServer().broadcastMessage("§cCancelando evento Bau...");
					plugin.getServer().broadcastMessage("§d[Evento Automático]");
					cancelarEventoBau();
					sender.sendMessage("§d[Evento Automático] §aBau cancelado com sucesso.");
  				}
				if(plugin.evento=="Paz") {
					plugin.getServer().broadcastMessage("§d[Evento Automático]");
					plugin.getServer().broadcastMessage("§cCancelando evento Paz...");
					plugin.getServer().broadcastMessage("§d[Evento Automático]");
					cancelarEventoPaz();
					sender.sendMessage("§d[Evento Automático] §aPaz cancelado com sucesso.");
  				}
				if(plugin.evento=="Loteria") {
					plugin.getServer().broadcastMessage("§d[Evento Automático]");
					plugin.getServer().broadcastMessage("§cCancelando evento Loteria...");
					plugin.getServer().broadcastMessage("§d[Evento Automático]");
					cancelarEventoLoteria();
					sender.sendMessage("§d[Evento Automático] §aLoteria cancelado com sucesso.");
  				}
				if(plugin.evento=="Drop") {
					plugin.getServer().broadcastMessage("§d[Evento Automático]");
					plugin.getServer().broadcastMessage("§cCancelando evento Drop...");
					plugin.getServer().broadcastMessage("§d[Evento Automático]");
					cancelarEventoDrop();
					sender.sendMessage("§d[Evento Automático] §aDrop cancelado com sucesso.");
  				}
				return true;
			}else if(args[0].equalsIgnoreCase("admin")) {
				if(!sender.hasPermission("pdgh.op")) {
					plugin.noPerm((Player)sender);
					return true;
				}
				if(args[1].equalsIgnoreCase("frogsr")) {
					if(args[2].equalsIgnoreCase("marcar")) {
						if(args.length>5)
							sender.sendMessage("§d[Evento Automático] §c/evento admin frogsr marcar <1/2/saida>");
						else {
							try {
								if(plugin.etapa==0) {
									Block targetblock=((Player)sender).getTargetBlock(null, 50);
					                Location location=targetblock.getLocation();
									switch(args[3]) {
										case "1": {plugin.pos1SR=location;sender.sendMessage("§d[Evento Automático] §fPosição 1 marcada com sucesso!");break;}
										case "2": {plugin.pos2SR=location;sender.sendMessage("§d[Evento Automático] §fPosição 2 marcada com sucesso!");break;}
										case "saida": {plugin.saidaSR=((Player)sender).getLocation();sender.sendMessage("§d[Evento Automático] §fSaída marcada com sucesso!");break;}
									}
								}
								else
									sender.sendMessage("§d[Evento Automático] §cVocê não pode mudar as posições após preparar/iniciar o plugin!");
							}
							catch(Exception e) {
								sender.sendMessage("§d[Evento Automático] §c/evento admin frogsr marcar <1/2/saida>");
							}
						}
					}else if(args[2].equalsIgnoreCase("salvar")) {
						if(args.length>5)
							sender.sendMessage("§d[Evento Automático] §c/evento admin frogsr salvar <posicoes/blocos/tudo>");
						else {
							try {
								if(plugin.etapa==0) {
									if(args[3].equalsIgnoreCase("posicoes")||args[3].equalsIgnoreCase("tudo")) {
										if(plugin.pos1SR==null)
											sender.sendMessage("§d[Evento Automático] §cPosição 1 não marcada!");
										else if(plugin.pos2SR==null)
											sender.sendMessage("§d[Evento Automático] §cPosição 2 não marcada!");
										else if(plugin.saidaSR==null)
											sender.sendMessage("§d[Evento Automático] §cSaída não marcada!");
										else {
											plugin.getConfig().set("posicoes.pos1sr", plugin.pos1SR.getX()+";"+plugin.pos1SR.getY()+";"+plugin.pos1SR.getZ());
											plugin.getConfig().set("posicoes.pos2sr", plugin.pos2SR.getX()+";"+plugin.pos2SR.getY()+";"+plugin.pos2SR.getZ());
											plugin.getConfig().set("posicoes.saidasr", plugin.saidaSR.getX()+";"+plugin.saidaSR.getY()+";"+plugin.saidaSR.getZ());
											plugin.saveConfig();
											plugin.reloadConfig();
											sender.sendMessage("§d[Evento Automático] §fPosições salvas com sucesso!");
										}
									}
									if(args[3].equalsIgnoreCase("blocos")||args[3].equalsIgnoreCase("tudo")) {
										if(plugin.pos1SR==null)
											sender.sendMessage("§d[Evento Automático] §cPosição 1 não marcada!");
										else if(plugin.pos2SR==null)
											sender.sendMessage("§d[Evento Automático] §cPosição 2 não marcada!");
										else if(plugin.saidaSR==null)
											sender.sendMessage("§d[Evento Automático] §cSaída não marcada!");
										else {
											List<String> lista=new ArrayList<String>();
											for(int x=plugin.getMinXSR();x<=plugin.getMaxXSR();x++)
												for(int z=plugin.getMinZSR();z<=plugin.getMaxZSR();z++) {
														Block B=((Player)sender).getWorld().getBlockAt(x, plugin.getYSR(), z);
														
														if(B.getType()!=Material.SNOW_BLOCK&&B.getType()!=Material.AIR)
															lista.add(B.getX()+";"+B.getY()+";"+B.getZ()+";"+B.getTypeId()+":"+(int)B.getData());
												}
											plugin.getConfig().set("blocosSR", lista);
											plugin.saveConfig();
											plugin.reloadConfig();
											lista.clear();
											sender.sendMessage("§d[Evento Automático] §fBlocos salvos com sucesso!");
										}
									}
								}
								else
									sender.sendMessage("§d[Evento Automático] §cVocê não pode salvar nada após preparar/iniciar o plugin!");
							}
							catch(Exception e) {
								sender.sendMessage("§d[Evento Automático] §c/evento admin frogsr salvar <posicoes/blocos/tudo>");
							}
						}
					}else if(args[2].equalsIgnoreCase("carregar")) {
						if(args.length>4) {
							plugin.helpAdmin((Player)sender);
							return true;
						}
						carregarFrogSR();
					}else if(args[2].equalsIgnoreCase("preparar")) {
						if(args.length>4) {
							plugin.helpAdmin((Player)sender);
							return true;
						}
						prepararFrogSR();
					}else if(args[2].equalsIgnoreCase("iniciar")) {
						if(args.length>4) {
							plugin.helpAdmin((Player)sender);
							return true;
						}
						iniciarFrogSR();
					}
				}
				if(args[1].equalsIgnoreCase("frogcr")) {
					if(args[2].equalsIgnoreCase("marcar")) {
						if(args.length>5)
							sender.sendMessage("§d[Evento Automático] §c/evento admin frogcr marcar <1/2/saida>");
						else {
							try {
								if(plugin.etapa==0) {
									Block targetblock=((Player)sender).getTargetBlock(null, 50);
					                Location location=targetblock.getLocation();
									switch(args[3]) {
										case "1": {plugin.pos1CR=location;sender.sendMessage("§d[Evento Automático] §fPosição 1 marcada com sucesso!");break;}
										case "2": {plugin.pos2CR=location;sender.sendMessage("§d[Evento Automático] §fPosição 2 marcada com sucesso!");break;}
										case "saida": {plugin.saidaCR=((Player)sender).getLocation();sender.sendMessage("§d[Evento Automático] §fSaída marcada com sucesso!");break;}
									}
								}
								else
									sender.sendMessage("§d[Evento Automático] §cVocê não pode mudar as posições após preparar/iniciar o plugin!");
							}
							catch(Exception e) {
								sender.sendMessage("§d[Evento Automático] §c/evento admin frogcr marcar <1/2/saida>");
							}
						}
					}else if(args[2].equalsIgnoreCase("salvar")) {
						if(args.length>5)
							sender.sendMessage("§d[Evento Automático] §c/evento admin frogcr salvar <posicoes/blocos/tudo>");
						else {
							try {
								if(plugin.etapa==0) {
									if(args[3].equalsIgnoreCase("posicoes")||args[3].equalsIgnoreCase("tudo")) {
										if(plugin.pos1CR==null)
											sender.sendMessage("§d[Evento Automático] §cPosição 1 não marcada!");
										else if(plugin.pos2CR==null)
											sender.sendMessage("§d[Evento Automático] §cPosição 2 não marcada!");
										else if(plugin.saidaCR==null)
											sender.sendMessage("§d[Evento Automático] §cSaída não marcada!");
										else {
											plugin.getConfig().set("posicoes.pos1cr", plugin.pos1CR.getX()+";"+plugin.pos1CR.getY()+";"+plugin.pos1CR.getZ());
											plugin.getConfig().set("posicoes.pos2cr", plugin.pos2CR.getX()+";"+plugin.pos2CR.getY()+";"+plugin.pos2CR.getZ());
											plugin.getConfig().set("posicoes.saidacr", plugin.saidaCR.getX()+";"+plugin.saidaCR.getY()+";"+plugin.saidaCR.getZ());
											plugin.saveConfig();
											plugin.reloadConfig();
											sender.sendMessage("§d[Evento Automático] §fPosições salvas com sucesso!");
										}
									}
									if(args[3].equalsIgnoreCase("blocos")||args[3].equalsIgnoreCase("tudo")) {
										if(plugin.pos1CR==null)
											sender.sendMessage("§d[Evento Automático] §cPosição 1 não marcada!");
										else if(plugin.pos2CR==null)
											sender.sendMessage("§d[Evento Automático] §cPosição 2 não marcada!");
										else if(plugin.saidaCR==null)
											sender.sendMessage("§d[Evento Automático] §cSaída não marcada!");
										else {
											List<String> lista=new ArrayList<String>();
											for(int x=plugin.getMinXCR();x<=plugin.getMaxXCR();x++)
												for(int z=plugin.getMinZCR();z<=plugin.getMaxZCR();z++) {
														Block B=((Player)sender).getWorld().getBlockAt(x, plugin.getYCR(), z);
														
														if(B.getType()!=Material.SNOW_BLOCK&&B.getType()!=Material.AIR)
															lista.add(B.getX()+";"+B.getY()+";"+B.getZ()+";"+B.getTypeId()+":"+(int)B.getData());
												}
											plugin.getConfig().set("blocosCR", lista);
											plugin.saveConfig();
											plugin.reloadConfig();
											lista.clear();
											sender.sendMessage("§d[Evento Automático] §fBlocos salvos com sucesso!");
										}
									}
								}
								else
									sender.sendMessage("§d[Evento Automático] §cVocê não pode salvar nada após preparar/iniciar o plugin!");
							}
							catch(Exception e) {
								sender.sendMessage("§d[Evento Automático] §c/evento admin frogcr salvar <posicoes/blocos/tudo>");
							}
						}
					}else if(args[2].equalsIgnoreCase("carregar")) {
						if(args.length>4) {
							plugin.helpAdmin((Player)sender);
							return true;
						}
						carregarFrogCR();
					}else if(args[2].equalsIgnoreCase("preparar")) {
						if(args.length>4) {
							plugin.helpAdmin((Player)sender);
							return true;
						}
						prepararFrogCR();
					}else if(args[2].equalsIgnoreCase("iniciar")) {
						if(args.length>4) {
							plugin.helpAdmin((Player)sender);
							return true;
						}
						iniciarFrogCR();
					}
				}
				if(args[1].equalsIgnoreCase("spleefsr")) {
					if(args[2].equalsIgnoreCase("marcar")) {
						if(args.length>5)
							sender.sendMessage("§d[Evento Automático] §c/evento admin spleefsr marcar <1/2>");
						else {
							try {
								if(plugin.etapa==0) {
									Block targetblock=((Player)sender).getTargetBlock(null, 50);
					                Location location=targetblock.getLocation();
									switch(args[3]) {
										case "1": {plugin.pos1SpleefSR=location;sender.sendMessage("§d[Evento Automático] §fPosição 1 marcada com sucesso!");break;}
										case "2": {plugin.pos2SpleefSR=location;sender.sendMessage("§d[Evento Automático] §fPosição 2 marcada com sucesso!");break;}
									}
								}
								else
									sender.sendMessage("§d[Evento Automático] §cVocê não pode mudar as posições após preparar/iniciar o plugin!");
							}
							catch(Exception e) {
								sender.sendMessage("§d[Evento Automático] §c/evento admin spleefsr marcar <1/2>");
							}
						}
					}else if(args[2].equalsIgnoreCase("salvar")) {
						if(args.length>5)
							sender.sendMessage("§d[Evento Automático] §c/evento admin spleefsr salvar");
						else {
							try {
								if(plugin.etapa==0) {
									if(plugin.pos1SpleefSR==null)
										sender.sendMessage("§d[Evento Automático] §cPosição 1 não marcada!");
									else if(plugin.pos2SpleefSR==null)
										sender.sendMessage("§d[Evento Automático] §cPosição 2 não marcada!");
									else {
										plugin.getConfig().set("posicoes.pos1spleefsr", plugin.pos1SpleefSR.getX()+";"+plugin.pos1SpleefSR.getY()+";"+plugin.pos1SpleefSR.getZ());
										plugin.getConfig().set("posicoes.pos2spleefsr", plugin.pos2SpleefSR.getX()+";"+plugin.pos2SpleefSR.getY()+";"+plugin.pos2SpleefSR.getZ());
										plugin.saveConfig();
										plugin.reloadConfig();
										sender.sendMessage("§d[Evento Automático] §fPosições salvas com sucesso!");
									}
								}
								else
									sender.sendMessage("§d[Evento Automático] §cVocê não pode salvar nada após preparar/iniciar o plugin!");
							}
							catch(Exception e) {
								sender.sendMessage("§d[Evento Automático] §c/evento admin spleefsr salvar");
							}
						}
					}else if(args[2].equalsIgnoreCase("carregar")) {
						if(args.length>4) {
							plugin.helpAdmin((Player)sender);
							return true;
						}
						carregarSpleefSR();
					}else if(args[2].equalsIgnoreCase("preparar")) {
						if(args.length>4) {
							plugin.helpAdmin((Player)sender);
							return true;
						}
						prepararSpleefSR();
					}else if(args[2].equalsIgnoreCase("iniciar")) {
						if(args.length>4) {
							plugin.helpAdmin((Player)sender);
							return true;
						}
						iniciarSpleefSR();
					}
				}
				if(args[1].equalsIgnoreCase("spleefcr")) {
					if(args[2].equalsIgnoreCase("marcar")) {
						if(args.length>5)
							sender.sendMessage("§d[Evento Automático] §c/evento admin spleefcr marcar <1/2>");
						else {
							try {
								if(plugin.etapa==0) {
									Block targetblock=((Player)sender).getTargetBlock(null, 50);
					                Location location=targetblock.getLocation();
									switch(args[3]) {
										case "1": {plugin.pos1SpleefCR=location;sender.sendMessage("§d[Evento Automático] §fPosição 1 marcada com sucesso!");break;}
										case "2": {plugin.pos2SpleefCR=location;sender.sendMessage("§d[Evento Automático] §fPosição 2 marcada com sucesso!");break;}
									}
								}
								else
									sender.sendMessage("§d[Evento Automático] §cVocê não pode mudar as posições após preparar/iniciar o plugin!");
							}
							catch(Exception e) {
								sender.sendMessage("§d[Evento Automático] §c/evento admin spleefcr marcar <1/2>");
							}
						}
					}else if(args[2].equalsIgnoreCase("salvar")) {
						if(args.length>5)
							sender.sendMessage("§d[Evento Automático] §c/evento admin spleefcr salvar");
						else {
							try {
								if(plugin.etapa==0) {
									if(plugin.pos1SpleefCR==null)
										sender.sendMessage("§d[Evento Automático] §cPosição 1 não marcada!");
									else if(plugin.pos2SpleefCR==null)
										sender.sendMessage("§d[Evento Automático] §cPosição 2 não marcada!");
									else {
										plugin.getConfig().set("posicoes.pos1spleefcr", plugin.pos1SpleefCR.getX()+";"+plugin.pos1SpleefCR.getY()+";"+plugin.pos1SpleefCR.getZ());
										plugin.getConfig().set("posicoes.pos2spleefcr", plugin.pos2SpleefCR.getX()+";"+plugin.pos2SpleefCR.getY()+";"+plugin.pos2SpleefCR.getZ());
										plugin.saveConfig();
										plugin.reloadConfig();
										sender.sendMessage("§d[Evento Automático] §fPosições salvas com sucesso!");
									}
								}
								else
									sender.sendMessage("§d[Evento Automático] §cVocê não pode salvar nada após preparar/iniciar o plugin!");
							}
							catch(Exception e) {
								sender.sendMessage("§d[Evento Automático] §c/evento admin spleefcr salvar");
							}
						}
					}else if(args[2].equalsIgnoreCase("carregar")) {
						if(args.length>4) {
							plugin.helpAdmin((Player)sender);
							return true;
						}
						carregarSpleefCR();
					}else if(args[2].equalsIgnoreCase("preparar")) {
						if(args.length>4) {
							plugin.helpAdmin((Player)sender);
							return true;
						}
						prepararSpleefCR();
					}else if(args[2].equalsIgnoreCase("iniciar")) {
						if(args.length>4) {
							plugin.helpAdmin((Player)sender);
							return true;
						}
						iniciarSpleefCR();
					}
				}
				if(args[1].equalsIgnoreCase("maratonacr")) {
					if(args[2].equalsIgnoreCase("marcar")) {
						if(args.length>5)
							sender.sendMessage("§d[Evento Automático] §c/evento admin maratonacr marcar <1/2>");
						else {
							try {
								if(plugin.etapa==0) {
									Block targetblock=((Player)sender).getTargetBlock(null, 50);
					                Location location=targetblock.getLocation();
									switch(args[3]) {
										case "1": {plugin.pos1MaratonaCR=location;sender.sendMessage("§d[Evento Automático] §fPosição 1 marcada com sucesso!");break;}
										case "2": {plugin.pos2MaratonaCR=location;sender.sendMessage("§d[Evento Automático] §fPosição 2 marcada com sucesso!");break;}
									}
								}
								else
									sender.sendMessage("§d[Evento Automático] §cVocê não pode mudar as posições após preparar/iniciar o plugin!");
							}
							catch(Exception e) {
								sender.sendMessage("§d[Evento Automático] §c/evento admin maratonacr marcar <1/2>");
							}
						}
					}else if(args[2].equalsIgnoreCase("salvar")) {
						if(args.length>5)
							sender.sendMessage("§d[Evento Automático] §c/evento admin maratonacr salvar");
						else {
							try {
								if(plugin.etapa==0) {
									if(plugin.pos1MaratonaCR==null)
										sender.sendMessage("§d[Evento Automático] §cPosição 1 não marcada!");
									else if(plugin.pos2MaratonaCR==null)
										sender.sendMessage("§d[Evento Automático] §cPosição 2 não marcada!");
									else {
										plugin.getConfig().set("posicoes.pos1maratonacr", plugin.pos1MaratonaCR.getX()+";"+plugin.pos1MaratonaCR.getY()+";"+plugin.pos1MaratonaCR.getZ());
										plugin.getConfig().set("posicoes.pos2maratonacr", plugin.pos2MaratonaCR.getX()+";"+plugin.pos2MaratonaCR.getY()+";"+plugin.pos2MaratonaCR.getZ());
										plugin.saveConfig();
										plugin.reloadConfig();
										sender.sendMessage("§d[Evento Automático] §fPosições salvas com sucesso!");
									}
								}
								else
									sender.sendMessage("§d[Evento Automático] §cVocê não pode salvar nada após preparar/iniciar o plugin!");
							}
							catch(Exception e) {
								sender.sendMessage("§d[Evento Automático] §c/evento admin maratonacr salvar");
							}
						}
					}else if(args[2].equalsIgnoreCase("carregar")) {
						if(args.length>4) {
							plugin.helpAdmin((Player)sender);
							return true;
						}
						carregarMaratonaCR();
					}else if(args[2].equalsIgnoreCase("iniciar")) {
						if(args.length>4) {
							plugin.helpAdmin((Player)sender);
							return true;
						}
						iniciarMaratonaCR();
					}
				}
				if(args[1].equalsIgnoreCase("farolsr")) {
					if(args[2].equalsIgnoreCase("marcar")) {
						if(args.length>5)
							sender.sendMessage("§d[Evento Automático] §c/evento admin farolsr marcar <1/2>");
						else {
							try {
								if(plugin.etapa==0) {
									Block targetblock=((Player)sender).getTargetBlock(null, 50);
					                Location location=targetblock.getLocation();
									switch(args[3]) {
										case "1": {plugin.pos1FarolSR=location;sender.sendMessage("§d[Evento Automático] §fPosição 1 marcada com sucesso!");break;}
										case "2": {plugin.pos2FarolSR=location;sender.sendMessage("§d[Evento Automático] §fPosição 2 marcada com sucesso!");break;}
									}
								}
								else
									sender.sendMessage("§d[Evento Automático] §cVocê não pode mudar as posições após preparar/iniciar o plugin!");
							}
							catch(Exception e) {
								sender.sendMessage("§d[Evento Automático] §c/evento admin farolsr marcar <1/2>");
							}
						}
					}else if(args[2].equalsIgnoreCase("salvar")) {
						if(args.length>5)
							sender.sendMessage("§d[Evento Automático] §c/evento admin farolsr salvar");
						else {
							try {
								if(plugin.etapa==0) {
									if(plugin.pos1FarolSR==null)
										sender.sendMessage("§d[Evento Automático] §cPosição 1 não marcada!");
									else if(plugin.pos2FarolSR==null)
										sender.sendMessage("§d[Evento Automático] §cPosição 2 não marcada!");
									else {
										plugin.getConfig().set("posicoes.pos1farolsr", plugin.pos1FarolSR.getX()+";"+plugin.pos1FarolSR.getY()+";"+plugin.pos1FarolSR.getZ());
										plugin.getConfig().set("posicoes.pos2farolsr", plugin.pos2FarolSR.getX()+";"+plugin.pos2FarolSR.getY()+";"+plugin.pos2FarolSR.getZ());
										plugin.saveConfig();
										plugin.reloadConfig();
										sender.sendMessage("§d[Evento Automático] §fPosições salvas com sucesso!");
									}
								}
								else
									sender.sendMessage("§d[Evento Automático] §cVocê não pode salvar nada após preparar/iniciar o plugin!");
							}
							catch(Exception e) {
								sender.sendMessage("§d[Evento Automático] §c/evento admin farolsr salvar");
							}
						}
					}else if(args[2].equalsIgnoreCase("carregar")) {
						if(args.length>4) {
							plugin.helpAdmin((Player)sender);
							return true;
						}
						carregarFarolSR();
					}else if(args[2].equalsIgnoreCase("iniciar")) {
						if(args.length>4) {
							plugin.helpAdmin((Player)sender);
							return true;
						}
						iniciarFarolSR();
					}
				}
				if(args[1].equalsIgnoreCase("parkourcr")) {
					if(args[2].equalsIgnoreCase("marcar")) {
						if(args.length>5)
							sender.sendMessage("§d[Evento Automático] §c/evento admin parkourcr marcar <1/2>");
						else {
							try {
								if(plugin.etapa==0) {
									Block targetblock=((Player)sender).getTargetBlock(null, 50);
					                Location location=targetblock.getLocation();
									switch(args[3]) {
										case "1": {plugin.pos1ParkourCR=location;sender.sendMessage("§d[Evento Automático] §fPosição 1 marcada com sucesso!");break;}
										case "2": {plugin.pos2ParkourCR=location;sender.sendMessage("§d[Evento Automático] §fPosição 2 marcada com sucesso!");break;}
									}
								}
								else
									sender.sendMessage("§d[Evento Automático] §cVocê não pode mudar as posições após preparar/iniciar o plugin!");
							}
							catch(Exception e) {
								sender.sendMessage("§d[Evento Automático] §c/evento admin parkourcr marcar <1/2>");
							}
						}
					}else if(args[2].equalsIgnoreCase("salvar")) {
						if(args.length>5)
							sender.sendMessage("§d[Evento Automático] §c/evento admin parkourcr salvar");
						else {
							try {
								if(plugin.etapa==0) {
									if(plugin.pos1ParkourCR==null)
										sender.sendMessage("§d[Evento Automático] §cPosição 1 não marcada!");
									else if(plugin.pos2ParkourCR==null)
										sender.sendMessage("§d[Evento Automático] §cPosição 2 não marcada!");
									else {
										plugin.getConfig().set("posicoes.pos1parkourcr", plugin.pos1ParkourCR.getX()+";"+plugin.pos1ParkourCR.getY()+";"+plugin.pos1ParkourCR.getZ());
										plugin.getConfig().set("posicoes.pos2parkourcr", plugin.pos2ParkourCR.getX()+";"+plugin.pos2ParkourCR.getY()+";"+plugin.pos2ParkourCR.getZ());
										plugin.saveConfig();
										plugin.reloadConfig();
										sender.sendMessage("§d[Evento Automático] §fPosições salvas com sucesso!");
									}
								}
								else
									sender.sendMessage("§d[Evento Automático] §cVocê não pode salvar nada após preparar/iniciar o plugin!");
							}
							catch(Exception e) {
								sender.sendMessage("§d[Evento Automático] §c/evento admin parkourcr salvar");
							}
						}
					}else if(args[2].equalsIgnoreCase("carregar")) {
						if(args.length>4) {
							plugin.helpAdmin((Player)sender);
							return true;
						}
						carregarParkourCR();
					}else if(args[2].equalsIgnoreCase("iniciar")) {
						if(args.length>4) {
							plugin.helpAdmin((Player)sender);
							return true;
						}
						iniciarParkourCR();
					}
				}
					/**sender.sendMessage("[Frog] Comandos do plugin:");
					sender.sendMessage("§b/evento admin frog marcar <1/2/saida> §f- Marca os limites do evento frog.");
					sender.sendMessage("§b/evento admin frog preparar §f- Prepara o evento (chão e posições).");
					sender.sendMessage("§b/evento admin frog iniciar §f- Inicia o evento frog.");
					sender.sendMessage("§b/evento admin frog reset §f- Caso algo dê errado antes de iniciar.");
					sender.sendMessage("§b/evento admin frog reload §f- Reload config.");
					sender.sendMessage("§b/evento admin frog salvar <posicoes/blocos/tudo> §f- Salva no arquivo para não necessitar refazer.");
					sender.sendMessage("§b/evento admin frog carregar <posicoes/blocos/tudo> §f- Carrega os dados salvos.");
					sender.sendMessage("§cLimite de 1 evento por vez.");*/
				if(args.length>=0) {
					plugin.help((Player)sender);
					return true;
				}
			}
			if(args.length>=0) {
				plugin.help((Player)sender);
				return true;
			}
			return true;
		}
		return false;
	}

    public static void cancel() {
        if(efrog!=null) {
            efrog.cancel();
            efrog=null;
        }
    }

	public static void fecharLobbyComRisco(String pvp) {
		if(plugin.participantes.size()>=2) {
			for(String p : plugin.participantes) {
				plugin.getServer().getPlayer(p).getWorld().playSound(plugin.getServer().getPlayer(p).getLocation(), Sound.NOTE_PLING, 1, 1);
				plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §cChão limpo.");
			}
            for (Entity entity : plugin.getServer().getWorld("world_eventos").getEntities()) {
                if (Main.isIntensiveEntity(entity)) {
                    entity.remove();
                }
            }
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
			plugin.getServer().broadcastMessage("§fPvP ["+pvp+"§f]");
			plugin.getServer().broadcastMessage("§cCom risco de morte");
			plugin.getServer().broadcastMessage("§fLobby fechado! §e("+plugin.participantes.size()+")");
			plugin.getServer().broadcastMessage("§fBoa sorte!");
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			plugin.startCancelarEventoAuto();
		}else{
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
			plugin.getServer().broadcastMessage("§fEvento cancelado");
			plugin.getServer().broadcastMessage("§fSem jogadores! §e(§c"+plugin.participantes.size()+"§e/2)");
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			if(plugin.evento=="FrogCR") {
				cancelarFrogCR();
			}
			if(plugin.evento=="SpleefCR") {
				cancelarSpleefCR();
			}
			if(plugin.evento=="MaratonaCR") {
				cancelarMaratonaCR();
			}
			if(plugin.evento=="ParkourCR") {
				cancelarParkourCR();
			}
		}
	}

	public static void broadcastEventoComRisco(String pvp, int timer) {
		plugin.getServer().broadcastMessage("§d[Evento Automático]");
		plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
		plugin.getServer().broadcastMessage("§fPvP ["+pvp+"§f]");
		plugin.getServer().broadcastMessage("§cCom risco de morte");
		plugin.getServer().broadcastMessage("§fDigite §l/evento§f para participar §e("+plugin.participantes.size()+")");
		if(timer==190)
			plugin.getServer().broadcastMessage("§fVocê tem §l3§f minutos para participar");
		if(timer==130)
			plugin.getServer().broadcastMessage("§fVocê tem §l2§f minutos para participar");
		if(timer==70)
			plugin.getServer().broadcastMessage("§fVocê tem §l1§f minuto para participar");
		if(timer<60)
			plugin.getServer().broadcastMessage("§fVocê tem §l"+(timer-10)+"§f segundos para participar");
		plugin.getServer().broadcastMessage("§fBoa sorte!");
		plugin.getServer().broadcastMessage("§d[Evento Automático]");
	}
	
	public static void startEventoComRisco(final String pvp) {
		plugin.lobby=true;
		ttasksP=plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
	  		int timer=130;
	  		public void run() {
	  			if(timer <-10) {
		      		cttasksP();
	  			}
	  			if((timer==190)||timer==130) {
	  				broadcastEventoComRisco(pvp, timer);
	  			}else if((timer==70)||(timer==40)||(timer==30)||(timer==20)||(timer==15)) {
	  				broadcastEventoComRisco(pvp, timer);
	  			}else if(timer==10) {
	  				plugin.lobby=false;
	  				fecharLobbyComRisco(pvp);
	  				if(plugin.evento=="FrogCR") {
		  				carregarFrogCR();
	  				}
	  				if(plugin.evento=="SpleefCR") {
		  				carregarSpleefCR();
	  				}
	  				if(plugin.evento=="MaratonaCR") {
		  				carregarMaratonaCR();
	  				}
	  				if(plugin.evento=="ParkourCR") {
		  				carregarParkourCR();
	  				}
	  			}else if(timer==9) {
	  				if(plugin.evento=="FrogCR") {
		  				prepararFrogCR();
	  				}
	  				if(plugin.evento=="SpleefCR") {
		  				prepararSpleefCR();
	  				}
	  			}else if(timer==6) {
	  				if(plugin.evento=="FrogCR") {
		  				teleportarFrogCR();
	  				}
	  				if(plugin.evento=="SpleefCR") {
		  				teleportarSpleefCR();
	  				}
	  				if(plugin.evento=="MaratonaCR") {
		  				teleportarMaratonaLobbyCR();
	  				}
	  				if(plugin.evento=="ParkourCR") {
		  				teleportarParkourLobbyCR();
	  				}
	  			}else if(timer==-10) {
	  				if(plugin.evento=="FrogCR") {
		  				iniciarFrogCR();
	  				}
	  				if(plugin.evento=="SpleefCR") {
		  				iniciarSpleefCR();
	  				}
	  				if(plugin.evento=="MaratonaCR") {
		  				iniciarMaratonaCR();
	  				}
	  				if(plugin.evento=="ParkourCR") {
		  				iniciarParkourCR();
	  				}
	  			}
	    		timer--;
	  		}
	  	}, 0, 20);
	}

	public static void fecharLobbySemRisco() {
		if(plugin.participantes.size()>=2) {
			for(String p : plugin.participantes) {
				plugin.getServer().getPlayer(p).getWorld().playSound(plugin.getServer().getPlayer(p).getLocation(), Sound.NOTE_PLING, 1, 1);
				//plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §c");
			}
			int quantidade=0;
            for (Entity entity : plugin.getServer().getWorld("world_eventos").getEntities()) {
                if (Main.isIntensiveEntity(entity)) {
                    entity.remove();
                    quantidade++;
                }
            }
            plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cChão limpo. ("+quantidade+")");
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
			plugin.getServer().broadcastMessage("§fPvP [§2OFF§f]");
			plugin.getServer().broadcastMessage("§2Sem risco de morte");
			plugin.getServer().broadcastMessage("§fLobby fechado! §e("+plugin.participantes.size()+")");
			plugin.getServer().broadcastMessage("§fBoa sorte!");
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			plugin.startCancelarEventoAuto();
		}else{
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
			plugin.getServer().broadcastMessage("§fEvento cancelado");
			plugin.getServer().broadcastMessage("§fSem jogadores! §e(§c"+plugin.participantes.size()+"§e/2)");
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			if(plugin.evento=="FrogSR") {
				cancelarFrogSR();
			}
			if(plugin.evento=="SpleefSR") {
				cancelarSpleefSR();
			}
			if(plugin.evento=="FarolSR") {
				cancelarFarolSR();
			}
		}
	}

	public static void broadcastEventoSemRisco(int timer) {
		plugin.getServer().broadcastMessage("§d[Evento Automático]");
		plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
		plugin.getServer().broadcastMessage("§fPvP [§2OFF§f]");
		plugin.getServer().broadcastMessage("§2Sem risco de morte");
		plugin.getServer().broadcastMessage("§fDigite §l/evento§f para participar §e("+plugin.participantes.size()+")");
		if(timer==190)
			plugin.getServer().broadcastMessage("§fVocê tem §l3§f minutos para participar");
		if(timer==130)
			plugin.getServer().broadcastMessage("§fVocê tem §l2§f minutos para participar");
		if(timer==70)
			plugin.getServer().broadcastMessage("§fVocê tem §l1§f minuto para participar");
		if(timer<60)
			plugin.getServer().broadcastMessage("§fVocê tem §l"+(timer-10)+"§f segundos para participar");
		plugin.getServer().broadcastMessage("§fBoa sorte!");
		plugin.getServer().broadcastMessage("§d[Evento Automático]");
	}
	
	public static void startEventoSemRisco() {
		plugin.lobby=true;
		ttasksP=plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
	  		int timer=130;
	  		public void run() {
	  			if(timer <-10) {
		      		cttasksP();
	  			}
	  			if((timer==190)||timer==130) {
	  				broadcastEventoSemRisco(timer);
	  			}else if((timer==70)||(timer==40)||(timer==30)||(timer==20)||(timer==15)) {
	  				broadcastEventoSemRisco(timer);
	  			}else if(timer==10) {
	  				plugin.lobby=false;
	  				fecharLobbySemRisco();
	  				if(plugin.evento=="FrogSR") {
		  				carregarFrogSR();
	  				}else if(plugin.evento=="SpleefSR") {
	  					carregarSpleefSR();
	  				}else if(plugin.evento=="FarolSR") {
	  					carregarFarolSR();
	  				}
	  			}else if(timer==9) {
	  				if(plugin.evento=="FrogSR") {
		  				prepararFrogSR();
	  				}else if(plugin.evento=="SpleefSR") {
	  					prepararSpleefSR();
	  				}
	  			}else if(timer==6) {
	  				if(plugin.evento=="FrogSR") {
		  				teleportarFrogSR();
	  				}else if(plugin.evento=="SpleefSR") {
	  					teleportarSpleefSR();
	  				}else if(plugin.evento=="FarolSR") {
	  					teleportarFarolLobbySR();
	  				}
	  			}else if(timer==-10) {
	  				if(plugin.evento=="FrogSR") {
		  				iniciarFrogSR();
	  				}else if(plugin.evento=="SpleefSR") {
	  					iniciarSpleefSR();
	  				}else if(plugin.evento=="FarolSR") {
	  					iniciarFarolSR();
	  				}
	  			}
	    		timer--;
	  		}
	  	}, 0, 20);
	}

	public static void cttasksP() {
		plugin.getServer().getScheduler().cancelTask(ttasksP);
	}

	public static void ctfarol() {
		plugin.getServer().getScheduler().cancelTask(tfarol);
	}
	
	public static void BackSnows() {
		plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
			public void run() {
				List<FrogBlock2> lt=new ArrayList<FrogBlock2>();
				for(Location i : plugin.material_loc) {
					Block B=plugin.getServer().getWorld("world_eventos").getBlockAt(i);
					
					if(B.getType()==Material.AIR)
						lt.add(new FrogBlock2(B.getLocation(),Material.SNOW_BLOCK,(byte)0));
				}
				FrogBlockQueue bq=new FrogBlockQueue(lt);
				bq.start();
			}
		}, plugin.bksn);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
/**
		┏━━━┳┓        ┏┳━━━┳━┓    ┏┳━━━━┳━━━┓	┏━━━┳━━━┳━━━┳━━━┓  ┏━━━┳━━━┓
		┃┏━━┫┗┓┏┛┃┏━━┫┃┗┓┃┃┏┓┏┓┃┏━┓┃	┃┏━━┫┏━┓┃┏━┓┃┏━┓┃  ┃┏━┓┃┏━┓┃
		┃┗━━    ┓┃┃┏┫┗━━┫┏┓┗┛┣┛┃┃┗┫┃    ┃┃	┃┗━━┫┗━┛┃┃    ┃┃┃    ┗┛  ┃┗━━┫┗━┛┃
		┃┏━━┛┃┗┛┃┃┏━━┫┃┗┓┃┃    ┃┃    ┃┃    ┃┃	┃┏━━┫┏┓┏┫┃    ┃┃┃┏━┓  ┗━━┓┃┏┓┏┛
		┃┗━━┓┗┓┏┛┃┗━━┫┃    ┃┃┃    ┃┃    ┃┗━┛┃	┃┃        ┃┃┃┗┫┗━┛┃┗┻━┃  ┃┗━┛┃┃┃┗┓
		┗━━━┛    ┗┛    ┗━━━┻┛    ┗━┛    ┗┛    ┗━━━┛	┗┛        ┗┛┗━┻━━━┻━━━┛  ┗━━━┻┛┗━┛
*/
	public static void cancelarFrogSR() {
		plugin.evento="nenhum";
		for(String p : plugin.participantes) {
			if(plugin.getServer().getPlayer(p).isOnline())
				if (plugin.w!=null) {
					plugin.getServer().getPlayer(p).teleport(plugin.w.getSpawnLocation());
				}else{
					plugin.getServer().getPlayer(p).sendMessage("§cOcorreu um erro. Notifique alguém da STAFF.");
				}
		}
	
		for(String e : plugin.eliminados) {
			if(plugin.getServer().getPlayer(e).isOnline())
				if (plugin.w!=null) {
					plugin.getServer().getPlayer(e).teleport(plugin.w.getSpawnLocation());
	    	    }else{
	    	    	plugin.getServer().getPlayer(e).sendMessage("§cOcorreu um erro. Notifique alguém da STAFF.");
	    	    }
		}
		plugin.ctcancelareventoauto();
		plugin.startEventoAuto();
		plugin.etapa=0;
		plugin.materiais.clear();
		plugin.material_sumiu.clear();
		plugin.pos1SR=null;
		plugin.pos2SR=null;
		plugin.saidaSR=null;
		plugin.vencedorSR=null;
		plugin.lobby=false;
		first=true;
		cttasksP();
		cancel();
		plugin.material_loc.clear();
		plugin.participantes.clear();
		plugin.confirmEntrarEvento.clear();
		plugin.eliminados.clear();
		FrogBlockQueue.blocos.clear();
	}
	
	public static void iniciarFrogSR() {
		plugin.vencedorSR=null;
		plugin.getServer().broadcastMessage("§d[Evento Automático] §fIniciando evento!");
		plugin.etapa=2;
		efrog=plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
			public void run() {
				try {
					if(first) {
						RemoveSnowsInicioSR();
						first=false;
					}
					else {
						if(plugin.materiais.size()>1) {
							int idx=new Random().nextInt(plugin.materiais.size());
							final String sorteado=plugin.materiais.get(idx);
							List<FrogBlock2> lt=new ArrayList<FrogBlock2>();
							for(int x=plugin.getMinXSR();x<=plugin.getMaxXSR();x++)
								for(int z=plugin.getMinZSR();z<=plugin.getMaxZSR();z++) {
									Block B=plugin.getServer().getWorld("world_eventos").getBlockAt(x, plugin.getYSR(), z);
									
									if(B.getType()==Material.getMaterial(Integer.parseInt(sorteado.split(":")[0]))&&B.getData()==(byte)Integer.parseInt(sorteado.split(":")[1]))
										lt.add(new FrogBlock2(B.getLocation(),Material.SNOW_BLOCK,(byte)0));
								}
							FrogBlockQueue bq=new FrogBlockQueue(lt);
							bq.start();
							RemoveSnowsSR();
							plugin.materiais.remove(idx);
						}
						else {
							List<FrogBlock2> lt=new ArrayList<FrogBlock2>();
							int certo=new Random().nextInt(plugin.material_loc.size());
							for(Location num : plugin.material_loc) {
								if(!num.equals(plugin.material_loc.get(certo))) {
									Block B=plugin.getServer().getWorld("world_eventos").getBlockAt(num);
									
									lt.add(new FrogBlock2(B.getLocation(),Material.SNOW_BLOCK,(byte)0));
								}
							}
							lt.add(new FrogBlock2(plugin.material_loc.get(certo).clone(),Material.WOOL,(byte)14));
							plugin.vencedorSR=plugin.material_loc.get(certo).clone().add(0, 1, 0);
				    		plugin.getServer().broadcastMessage("§d[Evento Automático] §fA lã vermelha apareceu!");
				    		plugin.getServer().broadcastMessage("§d[Evento Automático] §fO primeiro a subir nela vence!");
							FrogBlockQueue bq=new FrogBlockQueue(lt);
							bq.start();
							efrog.cancel();
						}
					}
				}
				catch(Exception e) {
				}
			}
		}, plugin.init, plugin.game);
	}
	
	public static void prepararFrogSR() {
		plugin.material_sumiu.clear();
		plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
			public void run() {
				List<FrogBlock2> lt=new ArrayList<FrogBlock2>();
				for(int x=plugin.getMinXSR();x<=plugin.getMaxXSR();x++)
					for(int z=plugin.getMinZSR();z<=plugin.getMaxZSR();z++) {
						Block B=plugin.getServer().getWorld("world_eventos").getBlockAt(x, plugin.getYSR(), z);
						
						if(B.getType()!=Material.AIR&&B.getType()!=Material.SNOW_BLOCK) {
							plugin.material_loc.add(B.getLocation());
							if(!plugin.material_sumiu.containsKey(B.getTypeId()+":"+(int)B.getData())) {
								plugin.material_sumiu.put(B.getTypeId()+":"+(int)B.getData(),false);
								plugin.materiais.add(B.getTypeId()+":"+(int)B.getData());
							}
						}
						else {
							if(B.getType()!=Material.SNOW_BLOCK)
								lt.add(new FrogBlock2(B.getLocation(),Material.WOOL,(byte)15));
						}
					}
				FrogBlockQueue bq=new FrogBlockQueue(lt);
				bq.start();
			}
		}, 1L);
		plugin.etapa=1;
		plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §fTudo pronto para iniciar o Frog!");
	}
	
	public static void carregarFrogSR() {
		if(plugin.etapa==0) {
			if(plugin.getConfig().contains("posicoes")) {
				if(!plugin.getConfig().contains("posicoes.pos1sr"))
					plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cPosição 1 não encontrada!");
				else if(!plugin.getConfig().contains("posicoes.pos2sr"))
					plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cPosição 2 não encontrada!");
				else if(!plugin.getConfig().contains("posicoes.saidasr"))
					plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cPosição de saída não encontrada!");
				else {
					String[] me=plugin.getConfig().getString("posicoes.pos1sr").split(";");
					Location l=new Location(plugin.getServer().getWorld("world_eventos"),Double.parseDouble(me[0]),Double.parseDouble(me[1]),Double.parseDouble(me[2]));
					plugin.pos1SR=l;
					me=plugin.getConfig().getString("posicoes.pos2sr").split(";");
					l=new Location(plugin.getServer().getWorld("world_eventos"),Double.parseDouble(me[0]),Double.parseDouble(me[1]),Double.parseDouble(me[2]));
					plugin.pos2SR=l;
					me=plugin.getConfig().getString("posicoes.saidasr").split(";");
					l=new Location(plugin.getServer().getWorld("world_eventos"),Double.parseDouble(me[0]),Double.parseDouble(me[1]),Double.parseDouble(me[2]));
					plugin.saidaSR=l;
					plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §fPosições carregadas com sucesso!");
				}
			}
			else
				plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cNão há posições salvas!");
			if(plugin.pos1SR!=null&&plugin.pos2SR!=null&&plugin.pos1SR.getBlockY()==plugin.pos2SR.getY()) {
				for(int x=plugin.getMinXSR();x<=plugin.getMaxXSR();x++) {
					for(int z=plugin.getMinZSR();z<=plugin.getMaxZSR();z++) {
						Block B=plugin.getServer().getWorld("world_eventos").getBlockAt(x, plugin.getYSR(), z);
						
						B.setType(Material.AIR);
					}
				}
			}
			if(plugin.getConfig().contains("blocosSR")) {
				for(String n : plugin.getConfig().getStringList("blocosSR")) {
					String[] me=n.split(";");
					Location l=new Location(plugin.getServer().getWorld("world_eventos"),Double.parseDouble(me[0]),Double.parseDouble(me[1]),Double.parseDouble(me[2]));
					plugin.getServer().getWorld("world_eventos").getBlockAt(l).setType(Material.getMaterial(Integer.parseInt(me[3].split(":")[0])));
					plugin.getServer().getWorld("world_eventos").getBlockAt(l).setData((byte)Integer.parseInt(me[3].split(":")[1]));
				}
				plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §fBlocos colocados com sucesso!");
			}
			else
				plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cNão há blocos salvos!");
		}else
			plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cVocê não pode carregar nada após preparar/iniciar o plugin!");
	}

	public static void teleportarFrogSR() {
		for(String p : plugin.participantes) {
			Location lobby=new Location(plugin.getServer().getWorld("world_eventos"), -64.0, 65, -108.0);
			lobby.setPitch(0);
			lobby.setYaw(180);
			plugin.getServer().getPlayer(p).teleport(lobby);
		}
	}
	
	
	
	
	
	
	public static void RemoveSnowsSR() {
		plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
			public void run() {
				List<FrogBlock2> lt=new ArrayList<FrogBlock2>();
				for(int x=plugin.getMinXSR();x<=plugin.getMaxXSR();x++)
					for(int z=plugin.getMinZSR();z<=plugin.getMaxZSR();z++) {
						Block b=plugin.getServer().getWorld("world_eventos").getBlockAt(x, plugin.getYSR(), z);
						
						if(b.getType()==Material.SNOW_BLOCK)
							lt.add(new FrogBlock2(b.getLocation(),Material.AIR,(byte)0));
					}
				FrogBlockQueue bq=new FrogBlockQueue(lt);
				bq.start();
				BackSnows();
			}
		},plugin.rmsn);
	}
	
	public static void RemoveSnowsInicioSR() {
		plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
			public void run() {
				List<FrogBlock2> lt=new ArrayList<FrogBlock2>();
				for(int x=plugin.getMinXSR();x<=plugin.getMaxXSR();x++)
					for(int z=plugin.getMinZSR();z<=plugin.getMaxZSR();z++) {
						Block b=plugin.getServer().getWorld("world_eventos").getBlockAt(x, plugin.getYSR(), z);
						
						if(b.getType()==Material.WOOL)
							lt.add(new FrogBlock2(b.getLocation(),Material.AIR,(byte)0));
					}
				FrogBlockQueue bq=new FrogBlockQueue(lt);
				bq.start();
			}
		},1L);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
/**
		┏━━━┳┓        ┏┳━━━┳━┓    ┏┳━━━━┳━━━┓	┏━━━┳━━━┳━━━┳━━━┓  ┏━━━┳━━━┓
		┃┏━━┫┗┓┏┛┃┏━━┫┃┗┓┃┃┏┓┏┓┃┏━┓┃	┃┏━━┫┏━┓┃┏━┓┃┏━┓┃  ┃┏━┓┃┏━┓┃
		┃┗━━    ┓┃┃┏┫┗━━┫┏┓┗┛┣┛┃┃┗┫┃    ┃┃	┃┗━━┫┗━┛┃┃    ┃┃┃    ┗┛  ┃┃    ┗┫┗━┛┃
		┃┏━━┛┃┗┛┃┃┏━━┫┃┗┓┃┃    ┃┃    ┃┃    ┃┃	┃┏━━┫┏┓┏┫┃    ┃┃┃┏━┓  ┃┃    ┏┫┏┓┏┛
		┃┗━━┓┗┓┏┛┃┗━━┫┃    ┃┃┃    ┃┃    ┃┗━┛┃	┃┃        ┃┃┃┗┫┗━┛┃┗┻━┃  ┃┗━┛┃┃┃┗┓
		┗━━━┛    ┗┛    ┗━━━┻┛    ┗━┛    ┗┛    ┗━━━┛	┗┛        ┗┛┗━┻━━━┻━━━┛  ┗━━━┻┛┗━┛
*/
	public static void cancelarFrogCR() {
		plugin.evento="nenhum";
		for(String p : plugin.participantes) {
			if(plugin.getServer().getPlayer(p).isOnline())
				if (plugin.w!=null) {
					plugin.getServer().getPlayer(p).teleport(plugin.w.getSpawnLocation());
				}else{
					plugin.getServer().getPlayer(p).sendMessage("§cOcorreu um erro. Notifique alguém da STAFF.");
				}
		}
	
		for(String e : plugin.eliminados) {
			if(plugin.getServer().getPlayer(e).isOnline())
				if (plugin.w!=null) {
					plugin.getServer().getPlayer(e).teleport(plugin.w.getSpawnLocation());
	    	    }else{
	    	    	plugin.getServer().getPlayer(e).sendMessage("§cOcorreu um erro. Notifique alguém da STAFF.");
	    	    }
		}

		plugin.ctcancelareventoauto();
		plugin.startEventoAuto();
		plugin.etapa=0;
		plugin.materiais.clear();
		plugin.material_sumiu.clear();
		plugin.pos1CR=null;
		plugin.pos2CR=null;
		plugin.saidaCR=null;
		plugin.vencedorCR=null;
		plugin.lobby=false;
		first=true;
		cttasksP();
		cancel();
		plugin.material_loc.clear();
		plugin.participantes.clear();
		plugin.confirmEntrarEvento.clear();
		plugin.eliminados.clear();
		FrogBlockQueue.blocos.clear();
	}
	
	public static void iniciarFrogCR() {
		plugin.vencedorCR=null;
		plugin.getServer().broadcastMessage("§d[Evento Automático] §fIniciando evento!");
		plugin.etapa=2;
		efrog=plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
			public void run() {
				try {
					if(first) {
						RemoveSnowsInicioCR();
						first=false;
					}
					else {
						if(plugin.materiais.size()>1) {
							int idx=new Random().nextInt(plugin.materiais.size());
							final String sorteado=plugin.materiais.get(idx);
							List<FrogBlock2> lt=new ArrayList<FrogBlock2>();
							for(int x=plugin.getMinXCR();x<=plugin.getMaxXCR();x++)
								for(int z=plugin.getMinZCR();z<=plugin.getMaxZCR();z++) {
									Block B=plugin.getServer().getWorld("world_eventos").getBlockAt(x, plugin.getYCR(), z);
									
									if(B.getType()==Material.getMaterial(Integer.parseInt(sorteado.split(":")[0]))&&B.getData()==(byte)Integer.parseInt(sorteado.split(":")[1]))
										lt.add(new FrogBlock2(B.getLocation(),Material.SNOW_BLOCK,(byte)0));
								}
							FrogBlockQueue bq=new FrogBlockQueue(lt);
							bq.start();
							RemoveSnowsCR();
							plugin.materiais.remove(idx);
						}
						else {
							List<FrogBlock2> lt=new ArrayList<FrogBlock2>();
							int certo=new Random().nextInt(plugin.material_loc.size());
							for(Location num : plugin.material_loc) {
								if(!num.equals(plugin.material_loc.get(certo))) {
									Block B=plugin.getServer().getWorld("world_eventos").getBlockAt(num);
									
									lt.add(new FrogBlock2(B.getLocation(),Material.SNOW_BLOCK,(byte)0));
								}
							}
							lt.add(new FrogBlock2(plugin.material_loc.get(certo).clone(),Material.WOOL,(byte)14));
							plugin.vencedorCR=plugin.material_loc.get(certo).clone().add(0, 1, 0);
				    		plugin.getServer().broadcastMessage("§d[Evento Automático] §fA lã vermelha apareceu!");
				    		plugin.getServer().broadcastMessage("§d[Evento Automático] §fO primeiro a subir nela vence!");
							FrogBlockQueue bq=new FrogBlockQueue(lt);
							bq.start();
							efrog.cancel();
						}
					}
				}
				catch(Exception e) {
				}
			}
		}, plugin.init, plugin.game);
	}
	
	public static void prepararFrogCR() {
		plugin.material_sumiu.clear();
		plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
			public void run() {
				List<FrogBlock2> lt=new ArrayList<FrogBlock2>();
				for(int x=plugin.getMinXCR();x<=plugin.getMaxXCR();x++)
					for(int z=plugin.getMinZCR();z<=plugin.getMaxZCR();z++) {
						Block B=plugin.getServer().getWorld("world_eventos").getBlockAt(x, plugin.getYCR(), z);
						
						if(B.getType()!=Material.AIR&&B.getType()!=Material.SNOW_BLOCK) {
							plugin.material_loc.add(B.getLocation());
							if(!plugin.material_sumiu.containsKey(B.getTypeId()+":"+(int)B.getData())) {
								plugin.material_sumiu.put(B.getTypeId()+":"+(int)B.getData(),false);
								plugin.materiais.add(B.getTypeId()+":"+(int)B.getData());
							}
						}
						else {
							if(B.getType()!=Material.SNOW_BLOCK)
								lt.add(new FrogBlock2(B.getLocation(),Material.WOOL,(byte)15));
						}
					}
				FrogBlockQueue bq=new FrogBlockQueue(lt);
				bq.start();
			}
		}, 1L);
		plugin.etapa=1;
		plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §fTudo pronto para iniciar o Frog!");
	}
	
	public static void carregarFrogCR() {
		if(plugin.etapa==0) {
			if(plugin.getConfig().contains("posicoes")) {
				if(!plugin.getConfig().contains("posicoes.pos1cr"))
					plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cPosição 1 não encontrada!");
				else if(!plugin.getConfig().contains("posicoes.pos2cr"))
					plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cPosição 2 não encontrada!");
				else if(!plugin.getConfig().contains("posicoes.saidacr"))
					plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cPosição de saída não encontrada!");
				else {
					String[] me=plugin.getConfig().getString("posicoes.pos1cr").split(";");
					Location l=new Location(plugin.getServer().getWorld("world_eventos"),Double.parseDouble(me[0]),Double.parseDouble(me[1]),Double.parseDouble(me[2]));
					plugin.pos1CR=l;
					me=plugin.getConfig().getString("posicoes.pos2cr").split(";");
					l=new Location(plugin.getServer().getWorld("world_eventos"),Double.parseDouble(me[0]),Double.parseDouble(me[1]),Double.parseDouble(me[2]));
					plugin.pos2CR=l;
					me=plugin.getConfig().getString("posicoes.saidacr").split(";");
					l=new Location(plugin.getServer().getWorld("world_eventos"),Double.parseDouble(me[0]),Double.parseDouble(me[1]),Double.parseDouble(me[2]));
					plugin.saidaCR=l;
					plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §fPosições carregadas com sucesso!");
				}
			}
			else
				plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cNão há posições salvas!");
			if(plugin.pos1CR!=null&&plugin.pos2CR!=null&&plugin.pos1CR.getBlockY()==plugin.pos2CR.getY()) {
				for(int x=plugin.getMinXCR();x<=plugin.getMaxXCR();x++) {
					for(int z=plugin.getMinZCR();z<=plugin.getMaxZCR();z++) {
						Block B=plugin.getServer().getWorld("world_eventos").getBlockAt(x, plugin.getYCR(), z);
						
						B.setType(Material.AIR);
					}
				}
			}
			if(plugin.getConfig().contains("blocosCR")) {
				for(String n : plugin.getConfig().getStringList("blocosCR")) {
					String[] me=n.split(";");
					Location l=new Location(plugin.getServer().getWorld("world_eventos"),Double.parseDouble(me[0]),Double.parseDouble(me[1]),Double.parseDouble(me[2]));
					plugin.getServer().getWorld("world_eventos").getBlockAt(l).setType(Material.getMaterial(Integer.parseInt(me[3].split(":")[0])));
					plugin.getServer().getWorld("world_eventos").getBlockAt(l).setData((byte)Integer.parseInt(me[3].split(":")[1]));
				}
				plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §fBlocos colocados com sucesso!");
			}
			else
				plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cNão há blocos salvos!");
		}else
			plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cVocê não pode carregar nada após preparar/iniciar o plugin!");
	}

	public static void teleportarFrogCR() {
		for(String p : plugin.participantes) {
			Location lobby=new Location(plugin.getServer().getWorld("world_eventos"), 65.0, 65, -108.0);
			lobby.setPitch(0);
			lobby.setYaw(180);
			plugin.getServer().getPlayer(p).teleport(lobby);
		}
	}
	
	
	
	
	
	
	
	
	

	
	
	public static void RemoveSnowsCR() {
		plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
			public void run() {
				List<FrogBlock2> lt=new ArrayList<FrogBlock2>();
				for(int x=plugin.getMinXCR();x<=plugin.getMaxXCR();x++)
					for(int z=plugin.getMinZCR();z<=plugin.getMaxZCR();z++) {
						Block b=plugin.getServer().getWorld("world_eventos").getBlockAt(x, plugin.getYCR(), z);
						
						if(b.getType()==Material.SNOW_BLOCK)
							lt.add(new FrogBlock2(b.getLocation(),Material.AIR,(byte)0));
					}
				FrogBlockQueue bq=new FrogBlockQueue(lt);
				bq.start();
				BackSnows();
			}
		},plugin.rmsn);
	}
	
	public static void RemoveSnowsInicioCR() {
		plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
			public void run() {
				List<FrogBlock2> lt=new ArrayList<FrogBlock2>();
				for(int x=plugin.getMinXCR();x<=plugin.getMaxXCR();x++)
					for(int z=plugin.getMinZCR();z<=plugin.getMaxZCR();z++) {
						Block b=plugin.getServer().getWorld("world_eventos").getBlockAt(x, plugin.getYCR(), z);
						
						if(b.getType()==Material.WOOL)
							lt.add(new FrogBlock2(b.getLocation(),Material.AIR,(byte)0));
					}
				FrogBlockQueue bq=new FrogBlockQueue(lt);
				bq.start();
			}
		},1L);
	}
	
	
	
	
	
	
	
	
	
	
/**
		┏━━━┳┓        ┏┳━━━┳━┓    ┏┳━━━━┳━━━┓	┏━━━┳━━━┳┓        ┏━━━┳━━━┳━━━┓  ┏━━━┳━━━┓
		┃┏━━┫┗┓┏┛┃┏━━┫┃┗┓┃┃┏┓┏┓┃┏━┓┃	┃┏━┓┃┏━┓┃┃        ┃┏━━┫┏━━┫┏━━┛  ┃┏━┓┃┏━┓┃
		┃┗━━    ┓┃┃┏┫┗━━┫┏┓┗┛┣┛┃┃┗┫┃    ┃┃	┃┗━━┫┗━┛┃┃        ┃┗━━┫┗━━┫┗━━┓  ┃┗━━┫┗━┛┃
		┃┏━━┛┃┗┛┃┃┏━━┫┃┗┓┃┃    ┃┃    ┃┃    ┃┃	┗━━┓┃┏━━┫┃    ┏┫┏━━┫┏━━┫┏━━┛  ┗━━┓┃┏┓┏┛
		┃┗━━┓┗┓┏┛┃┗━━┫┃    ┃┃┃    ┃┃    ┃┗━┛┃	┃┗━┛┃┃        ┃┗━┛┃┗━━┫┗━━┫┃              ┃┗━┛┃┃┃┗┓
		┗━━━┛    ┗┛    ┗━━━┻┛    ┗━┛    ┗┛    ┗━━━┛	┗━━━┻┛        ┗━━━┻━━━┻━━━┻┛              ┗━━━┻┛┗━┛
*/
	public static void cancelarSpleefSR() {
		plugin.evento="nenhum";
		for(String p : plugin.participantes) {
			if(plugin.getServer().getPlayer(p).isOnline())
				if (plugin.w!=null) {
					plugin.getServer().getPlayer(p).teleport(plugin.w.getSpawnLocation());
				}else{
					plugin.getServer().getPlayer(p).sendMessage("§cOcorreu um erro. Notifique alguém da STAFF.");
				}
		}
	
		for(String e : plugin.eliminados) {
			if(plugin.getServer().getPlayer(e).isOnline())
				if (plugin.w!=null) {
					plugin.getServer().getPlayer(e).teleport(plugin.w.getSpawnLocation());
	    	    }else{
	    	    	plugin.getServer().getPlayer(e).sendMessage("§cOcorreu um erro. Notifique alguém da STAFF.");
	    	    }
		}

		plugin.ctcancelareventoauto();
		plugin.startEventoAuto();
		plugin.etapa=0;
		plugin.pos1SpleefSR=null;
		plugin.pos2SpleefSR=null;
		plugin.lobby=false;
		plugin.participantes.clear();
		plugin.confirmEntrarEvento.clear();
		plugin.eliminados.clear();
		cttasksP();
	}
	
	public static void iniciarSpleefSR() {
		plugin.getServer().broadcastMessage("§d[Evento Automático] §fIniciando evento!");
		plugin.etapa=2;
	}
	
	public static void prepararSpleefSR() {
		colocarNeveSpleefSR();
		plugin.etapa=1;
		plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §fTudo pronto para iniciar o Spleef!");
	}
	
	public static void carregarSpleefSR() {
		if(plugin.etapa==0) {
			if(plugin.getConfig().contains("posicoes")) {
				if(!plugin.getConfig().contains("posicoes.pos1spleefsr"))
					plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cPosição 1 não encontrada!");
				else if(!plugin.getConfig().contains("posicoes.pos2spleefsr"))
					plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cPosição 2 não encontrada!");
				else {
					String[] me=plugin.getConfig().getString("posicoes.pos1spleefsr").split(";");
					Location l=new Location(plugin.getServer().getWorld("world_eventos"),Double.parseDouble(me[0]),Double.parseDouble(me[1]),Double.parseDouble(me[2]));
					plugin.pos1SpleefSR=l;
					me=plugin.getConfig().getString("posicoes.pos2spleefsr").split(";");
					l=new Location(plugin.getServer().getWorld("world_eventos"),Double.parseDouble(me[0]),Double.parseDouble(me[1]),Double.parseDouble(me[2]));
					plugin.pos2SpleefSR=l;
					plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §fPosições carregadas com sucesso!");
				}
			}
			else
				plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cNão há posições salvas!");
		}else
			plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cVocê não pode carregar nada após preparar/iniciar o plugin!");
	}

	public static void teleportarSpleefSR() {
		for(String p : plugin.participantes) {
			Location lobby=new Location(plugin.getServer().getWorld("world_eventos"), 49.0, 65, 93.0);
			lobby.setPitch(0);
			lobby.setYaw(180);
			plugin.getServer().getPlayer(p).teleport(lobby);
		}
	}
	
	public static void colocarNeveSpleefSR() {
		plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
			public void run() {
				List<FrogBlock2> lt=new ArrayList<FrogBlock2>();
				for(int x=plugin.getMinXSpleefSR();x<=plugin.getMaxXSpleefSR();x++)
					for(int z=plugin.getMinZSpleefSR();z<=plugin.getMaxZSpleefSR();z++) {
						Block b=plugin.getServer().getWorld("world_eventos").getBlockAt(x, plugin.getYSpleefSR(), z);
						
						lt.add(new FrogBlock2(b.getLocation(),Material.SNOW_BLOCK,(byte)0));
					}
				FrogBlockQueue bq=new FrogBlockQueue(lt);
				bq.start();
			}
		},1L);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
/**
		┏━━━┳┓        ┏┳━━━┳━┓    ┏┳━━━━┳━━━┓	┏━━━┳━━━┳┓        ┏━━━┳━━━┳━━━┓  ┏━━━┳━━━┓
		┃┏━━┫┗┓┏┛┃┏━━┫┃┗┓┃┃┏┓┏┓┃┏━┓┃	┃┏━┓┃┏━┓┃┃        ┃┏━━┫┏━━┫┏━━┛  ┃┏━┓┃┏━┓┃
		┃┗━━    ┓┃┃┏┫┗━━┫┏┓┗┛┣┛┃┃┗┫┃    ┃┃	┃┗━━┫┗━┛┃┃        ┃┗━━┫┗━━┫┗━━┓  ┃┃    ┗┫┗━┛┃
		┃┏━━┛┃┗┛┃┃┏━━┫┃┗┓┃┃    ┃┃    ┃┃    ┃┃	┗━━┓┃┏━━┫┃    ┏┫┏━━┫┏━━┫┏━━┛  ┃┃    ┏┫┏┓┏┛
		┃┗━━┓┗┓┏┛┃┗━━┫┃    ┃┃┃    ┃┃    ┃┗━┛┃	┃┗━┛┃┃        ┃┗━┛┃┗━━┫┗━━┫┃              ┃┗━┛┃┃┃┗┓
		┗━━━┛    ┗┛    ┗━━━┻┛    ┗━┛    ┗┛    ┗━━━┛	┗━━━┻┛        ┗━━━┻━━━┻━━━┻┛              ┗━━━┻┛┗━┛
*/
	public static void cancelarSpleefCR() {
		plugin.evento="nenhum";
		for(String p : plugin.participantes) {
			if(plugin.getServer().getPlayer(p).isOnline())
				if (plugin.w!=null) {
					plugin.getServer().getPlayer(p).teleport(plugin.w.getSpawnLocation());
				}else{
					plugin.getServer().getPlayer(p).sendMessage("§cOcorreu um erro. Notifique alguém da STAFF.");
				}
		}
	
		for(String e : plugin.eliminados) {
			if(plugin.getServer().getPlayer(e).isOnline())
				if (plugin.w!=null) {
					plugin.getServer().getPlayer(e).teleport(plugin.w.getSpawnLocation());
	    	    }else{
	    	    	plugin.getServer().getPlayer(e).sendMessage("§cOcorreu um erro. Notifique alguém da STAFF.");
	    	    }
		}

		plugin.ctcancelareventoauto();
		plugin.startEventoAuto();
		plugin.etapa=0;
		plugin.pos1SpleefCR=null;
		plugin.pos2SpleefCR=null;
		plugin.lobby=false;
		plugin.participantes.clear();
		plugin.confirmEntrarEvento.clear();
		plugin.eliminados.clear();
		cttasksP();
	}
	
	public static void iniciarSpleefCR() {
		plugin.getServer().broadcastMessage("§d[Evento Automático] §fIniciando evento!");
		plugin.etapa=2;
	}
	
	public static void prepararSpleefCR() {
		colocarNeveSpleefCR();
		plugin.etapa=1;
		plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §fTudo pronto para iniciar o Spleef!");
	}
	
	public static void carregarSpleefCR() {
		if(plugin.etapa==0) {
			if(plugin.getConfig().contains("posicoes")) {
				if(!plugin.getConfig().contains("posicoes.pos1spleefcr"))
					plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cPosição 1 não encontrada!");
				else if(!plugin.getConfig().contains("posicoes.pos2spleefcr"))
					plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cPosição 2 não encontrada!");
				else {
					String[] me=plugin.getConfig().getString("posicoes.pos1spleefcr").split(";");
					Location l=new Location(plugin.getServer().getWorld("world_eventos"),Double.parseDouble(me[0]),Double.parseDouble(me[1]),Double.parseDouble(me[2]));
					plugin.pos1SpleefCR=l;
					me=plugin.getConfig().getString("posicoes.pos2spleefcr").split(";");
					l=new Location(plugin.getServer().getWorld("world_eventos"),Double.parseDouble(me[0]),Double.parseDouble(me[1]),Double.parseDouble(me[2]));
					plugin.pos2SpleefCR=l;
					plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §fPosições carregadas com sucesso!");
				}
			}
			else
				plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cNão há posições salvas!");
		}else
			plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cVocê não pode carregar nada após preparar/iniciar o plugin!");
	}

	public static void teleportarSpleefCR() {
		for(String p : plugin.participantes) {
			Location lobby=new Location(plugin.getServer().getWorld("world_eventos"), -48.0, 64, 93.0);
			lobby.setPitch(0);
			lobby.setYaw(180);
			plugin.getServer().getPlayer(p).teleport(lobby);
		}
	}
	
	public static void colocarNeveSpleefCR() {
		plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
			public void run() {
				List<FrogBlock2> lt=new ArrayList<FrogBlock2>();
				for(int x=plugin.getMinXSpleefCR();x<=plugin.getMaxXSpleefCR();x++)
					for(int z=plugin.getMinZSpleefCR();z<=plugin.getMaxZSpleefCR();z++) {
						Block b=plugin.getServer().getWorld("world_eventos").getBlockAt(x, plugin.getYSpleefCR(), z);
						
						lt.add(new FrogBlock2(b.getLocation(),Material.SNOW_BLOCK,(byte)0));
					}
				FrogBlockQueue bq=new FrogBlockQueue(lt);
				bq.start();
			}
		},1L);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

	
/**
		╔═══╦╗  ╔╦═══╦═╗ ╔╦════╦═══╗	╔═╗╔═╦═══╦═══╦═══╦════╦═══╦═╗ ╔╦═══╗  ╔═══╦═══╗
		║╔══╣╚╗╔╝║╔══╣║╚╗║║╔╗╔╗║╔═╗║	║║╚╝║║╔═╗║╔═╗║╔═╗║╔╗╔╗║╔═╗║║╚╗║║╔═╗║  ║╔═╗║╔═╗║
		║╚══╬╗║║╔╣╚══╣╔╗╚╝╠╝║║╚╣║ ║║	║╔╗╔╗║║ ║║╚═╝║║ ║╠╝║║╚╣║ ║║╔╗╚╝║║ ║║  ║║ ╚╣╚═╝║
		║╔══╝║╚╝║║╔══╣║╚╗║║ ║║ ║║ ║║	║║║║║║╚═╝║╔╗╔╣╚═╝║ ║║ ║║ ║║║╚╗║║╚═╝║  ║║ ╔╣╔╗╔╝
		║╚══╗╚╗╔╝║╚══╣║ ║║║ ║║ ║╚═╝║	║║║║║║╔═╗║║║╚╣╔═╗║ ║║ ║╚═╝║║ ║║║╔═╗║  ║╚═╝║║║╚╗
		╚═══╝ ╚╝ ╚═══╩╝ ╚═╝ ╚╝ ╚═══╝	╚╝╚╝╚╩╝ ╚╩╝╚═╩╝ ╚╝ ╚╝ ╚═══╩╝ ╚═╩╝ ╚╝  ╚═══╩╝╚═╝
*/	public static void cancelarMaratonaCR() {
		plugin.evento="nenhum";
		for(String p : plugin.participantes) {
			if(plugin.getServer().getPlayer(p).isOnline())
				if (plugin.w!=null) {
					plugin.getServer().getPlayer(p).teleport(plugin.w.getSpawnLocation());
				}else{
					plugin.getServer().getPlayer(p).sendMessage("§cOcorreu um erro. Notifique alguém da STAFF.");
				}
		}
	
		for(String e : plugin.eliminados) {
			if(plugin.getServer().getPlayer(e).isOnline())
				if (plugin.w!=null) {
					plugin.getServer().getPlayer(e).teleport(plugin.w.getSpawnLocation());
	    	    }else{
	    	    	plugin.getServer().getPlayer(e).sendMessage("§cOcorreu um erro. Notifique alguém da STAFF.");
	    	    }
		}

		plugin.ctcancelareventoauto();
		plugin.startEventoAuto();
		plugin.etapa=0;
		plugin.pos1MaratonaCR=null;
		plugin.pos2MaratonaCR=null;
		plugin.lobby=false;
		plugin.participantes.clear();
		plugin.confirmEntrarEvento.clear();
		plugin.eliminados.clear();
		cttasksP();
	}
	
	public static void iniciarMaratonaCR() {
		teleportarMaratonaInicioCR();
		plugin.getServer().broadcastMessage("§d[Evento Automático] §fIniciando evento!");
		plugin.etapa=2;
	}
	
	public static void carregarMaratonaCR() {
		if(plugin.etapa==0) {
			if(plugin.getConfig().contains("posicoes")) {
				if(!plugin.getConfig().contains("posicoes.pos1maratonacr"))
					plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cPosição 1 não encontrada!");
				else if(!plugin.getConfig().contains("posicoes.pos2maratonacr"))
					plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cPosição 2 não encontrada!");
				else {
					String[] me=plugin.getConfig().getString("posicoes.pos1maratonacr").split(";");
					Location l=new Location(plugin.getServer().getWorld("world_eventos"),Double.parseDouble(me[0]),Double.parseDouble(me[1]),Double.parseDouble(me[2]));
					plugin.pos1MaratonaCR=l;
					me=plugin.getConfig().getString("posicoes.pos2maratonacr").split(";");
					l=new Location(plugin.getServer().getWorld("world_eventos"),Double.parseDouble(me[0]),Double.parseDouble(me[1]),Double.parseDouble(me[2]));
					plugin.pos2MaratonaCR=l;
					plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §fPosições carregadas com sucesso!");
				}
			}
			else
				plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cNão há posições salvas!");
		}else
			plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cVocê não pode carregar nada após preparar/iniciar o plugin!");
	}

	public static void teleportarMaratonaInicioCR() {
		for(String p : plugin.participantes) {
        	Random randomgen=new Random();
        	int i=randomgen.nextInt(10)+1;
        	if(i==1) {
    			Location locMaratonaStart=new Location(plugin.getServer().getWorld("world_eventos"), 228.0, 51, -44.0);
    			locMaratonaStart.setPitch(0);
    			locMaratonaStart.setYaw(270);
    			plugin.getServer().getPlayer(p).teleport(locMaratonaStart);
        	}else if(i==2) {
    			Location locMaratonaStart=new Location(plugin.getServer().getWorld("world_eventos"), 228.0, 51, -43.0);
    			locMaratonaStart.setPitch(0);
    			locMaratonaStart.setYaw(270);
    			plugin.getServer().getPlayer(p).teleport(locMaratonaStart);
        	}else if(i==3) {
    			Location locMaratonaStart=new Location(plugin.getServer().getWorld("world_eventos"), 228.0, 51, -42.0);
    			locMaratonaStart.setPitch(0);
    			locMaratonaStart.setYaw(270);
    			plugin.getServer().getPlayer(p).teleport(locMaratonaStart);
        	}else if(i==4) {
    			Location locMaratonaStart=new Location(plugin.getServer().getWorld("world_eventos"), 228.0, 51, -41.0);
    			locMaratonaStart.setPitch(0);
    			locMaratonaStart.setYaw(270);
    			plugin.getServer().getPlayer(p).teleport(locMaratonaStart);
        	}else if(i==5) {
    			Location locMaratonaStart=new Location(plugin.getServer().getWorld("world_eventos"), 228.0, 51, -40.0);
    			locMaratonaStart.setPitch(0);
    			locMaratonaStart.setYaw(270);
    			plugin.getServer().getPlayer(p).teleport(locMaratonaStart);
        	}else if(i==6) {
    			Location locMaratonaStart=new Location(plugin.getServer().getWorld("world_eventos"), 228.0, 51, -39.0);
    			locMaratonaStart.setPitch(0);
    			locMaratonaStart.setYaw(270);
    			plugin.getServer().getPlayer(p).teleport(locMaratonaStart);
        	}else if(i==7) {
    			Location locMaratonaStart=new Location(plugin.getServer().getWorld("world_eventos"), 228.0, 51, -38.0);
    			locMaratonaStart.setPitch(0);
    			locMaratonaStart.setYaw(270);
    			plugin.getServer().getPlayer(p).teleport(locMaratonaStart);
        	}else if(i==8) {
    			Location locMaratonaStart=new Location(plugin.getServer().getWorld("world_eventos"), 228.0, 51, -37.0);
    			locMaratonaStart.setPitch(0);
    			locMaratonaStart.setYaw(270);
    			plugin.getServer().getPlayer(p).teleport(locMaratonaStart);
        	}else if(i==9) {
    			Location locMaratonaStart=new Location(plugin.getServer().getWorld("world_eventos"), 228.0, 51, -36.0);
    			locMaratonaStart.setPitch(0);
    			locMaratonaStart.setYaw(270);
    			plugin.getServer().getPlayer(p).teleport(locMaratonaStart);
        	}else if(i==10) {
    			Location locMaratonaStart=new Location(plugin.getServer().getWorld("world_eventos"), 228.0, 51, -35.0);
    			locMaratonaStart.setPitch(0);
    			locMaratonaStart.setYaw(270);
    			plugin.getServer().getPlayer(p).teleport(locMaratonaStart);
        	}
		}
	}

	public static void teleportarMaratonaLobbyCR() {
		plugin.etapa=2;
		for(String p : plugin.participantes) {
			Location lobby=new Location(plugin.getServer().getWorld("world_eventos"), 224.0, 51, -39.0);
			lobby.setPitch(0);
			lobby.setYaw(270);
			plugin.getServer().getPlayer(p).teleport(lobby);
			plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §fPrepare-se! O evento já vai começar!");
		}
	}
	
	
	
	
	
	
	
/**
		┏━━━┳┓        ┏┳━━━┳━┓    ┏┳━━━━┳━━━┓	┏━━━┳━━━┳━━━┳━━━┳┓              ┏━━━┳━━━┓
		┃┏━━┫┗┓┏┛┃┏━━┫┃┗┓┃┃┏┓┏┓┃┏━┓┃	┃┏━━┫┏━┓┃┏━┓┃┏━┓┃┃              ┃┏━┓┃┏━┓┃
		┃┗━━    ┓┃┃┏┫┗━━┫┏┓┗┛┣┛┃┃┗┫┃    ┃┃	┃┗━━┫┃    ┃┃┗━┛┃┃    ┃┃┃              ┃┗━━┫┗━┛┃
		┃┏━━┛┃┗┛┃┃┏━━┫┃┗┓┃┃    ┃┃    ┃┃    ┃┃	┃┏━━┫┗━┛┃┏┓┏┫┃    ┃┃┃    ┏┓  ┗━━┓┃┏┓┏┛
		┃┗━━┓┗┓┏┛┃┗━━┫┃    ┃┃┃    ┃┃    ┃┗━┛┃	┃┃        ┃┏━┓┃┃┃┗┫┗━┛┃┗━┛┃  ┃┗━┛┃┃┃┗┓
		┗━━━┛    ┗┛    ┗━━━┻┛    ┗━┛    ┗┛    ┗━━━┛	┗┛        ┗┛    ┗┻┛┗━┻━━━┻━━━┛  ┗━━━┻┛┗━┛
*/	public static void cancelarFarolSR() {
		plugin.evento="nenhum";
		for(String p : plugin.participantes) {
			if(plugin.getServer().getPlayer(p).isOnline())
				if (plugin.w!=null) {
					plugin.getServer().getPlayer(p).teleport(plugin.w.getSpawnLocation());
				}else{
					plugin.getServer().getPlayer(p).sendMessage("§cOcorreu um erro. Notifique alguém da STAFF.");
				}
		}
	
		for(String e : plugin.eliminados) {
			if(plugin.getServer().getPlayer(e).isOnline())
				if (plugin.w!=null) {
					plugin.getServer().getPlayer(e).teleport(plugin.w.getSpawnLocation());
	    	    }else{
	    	    	plugin.getServer().getPlayer(e).sendMessage("§cOcorreu um erro. Notifique alguém da STAFF.");
	    	    }
		}

		plugin.ctcancelareventoauto();
		plugin.startEventoAuto();
		plugin.etapa=0;
		plugin.pos1FarolSR=null;
		plugin.pos2FarolSR=null;
		plugin.farol=0;
		plugin.lobby=false;
		plugin.participantes.clear();
		plugin.confirmEntrarEvento.clear();
		plugin.eliminados.clear();
		cttasksP();
		ctfarol();
	}
	
	public static void iniciarFarolSR() {
		teleportarFarolInicioSR();
		plugin.getServer().broadcastMessage("§d[Evento Automático] §fIniciando evento!");
		startTimerFarol();
		plugin.etapa=2;
	}
	
	public static void carregarFarolSR() {
		if(plugin.etapa==0) {
			if(plugin.getConfig().contains("posicoes")) {
				if(!plugin.getConfig().contains("posicoes.pos1farolsr"))
					plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cPosição 1 não encontrada!");
				else if(!plugin.getConfig().contains("posicoes.pos2farolsr"))
					plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cPosição 2 não encontrada!");
				else {
					String[] me=plugin.getConfig().getString("posicoes.pos1farolsr").split(";");
					Location l=new Location(plugin.getServer().getWorld("world_eventos"),Double.parseDouble(me[0]),Double.parseDouble(me[1]),Double.parseDouble(me[2]));
					plugin.pos1FarolSR=l;
					me=plugin.getConfig().getString("posicoes.pos2farolsr").split(";");
					l=new Location(plugin.getServer().getWorld("world_eventos"),Double.parseDouble(me[0]),Double.parseDouble(me[1]),Double.parseDouble(me[2]));
					plugin.pos2FarolSR=l;
					plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §fPosições carregadas com sucesso!");
				}
			}
			else
				plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cNão há posições salvas!");
		}else
			plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cVocê não pode carregar nada após preparar/iniciar o plugin!");
	}

	public static void teleportarFarolInicioSR() {
		for(String p : plugin.participantes) {
        	Random randomgen=new Random();
        	int i=randomgen.nextInt(6)+1;
        	if(i==1) {
    			Location locMaratonaStart=new Location(plugin.getServer().getWorld("world_eventos"), 237.5, 53, 44.5);
    			locMaratonaStart.setPitch(0);
    			locMaratonaStart.setYaw(270);
    			plugin.getServer().getPlayer(p).teleport(locMaratonaStart);
        	}else if(i==2) {
    			Location locMaratonaStart=new Location(plugin.getServer().getWorld("world_eventos"), 237.5, 53, 45.5);
    			locMaratonaStart.setPitch(0);
    			locMaratonaStart.setYaw(270);
    			plugin.getServer().getPlayer(p).teleport(locMaratonaStart);
        	}else if(i==3) {
    			Location locMaratonaStart=new Location(plugin.getServer().getWorld("world_eventos"), 237.5, 53, 46.5);
    			locMaratonaStart.setPitch(0);
    			locMaratonaStart.setYaw(270);
    			plugin.getServer().getPlayer(p).teleport(locMaratonaStart);
        	}else if(i==4) {
    			Location locMaratonaStart=new Location(plugin.getServer().getWorld("world_eventos"), 237.5, 53, 47.5);
    			locMaratonaStart.setPitch(0);
    			locMaratonaStart.setYaw(270);
    			plugin.getServer().getPlayer(p).teleport(locMaratonaStart);
        	}else if(i==5) {
    			Location locMaratonaStart=new Location(plugin.getServer().getWorld("world_eventos"), 237.5, 53, 48.5);
    			locMaratonaStart.setPitch(0);
    			locMaratonaStart.setYaw(270);
    			plugin.getServer().getPlayer(p).teleport(locMaratonaStart);
        	}else if(i==6) {
    			Location locMaratonaStart=new Location(plugin.getServer().getWorld("world_eventos"), 237.5, 53, 49.5);
    			locMaratonaStart.setPitch(0);
    			locMaratonaStart.setYaw(270);
    			plugin.getServer().getPlayer(p).teleport(locMaratonaStart);
        	}
		}
	}

	public static void teleportarFarolLobbySR() {
		plugin.etapa=2;
		for(String p : plugin.participantes) {
			Location lobby=new Location(plugin.getServer().getWorld("world_eventos"), 234.0, 54, 47.5);
			lobby.setPitch(0);
			lobby.setYaw(270);
			plugin.getServer().getPlayer(p).teleport(lobby);
			plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §fPrepare-se! O evento já vai começar!");
		}
	}
	
	public static void startTimerFarol() {
		tfarol=plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
	  		public void run() {
	  			if((plugin.farol==0)||(plugin.farol==1)) {
	  				for(String p : plugin.participantes) {
	  					plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §2Farol: ▇");
	  				}
		  			plugin.farol=2;
	  			}else if(plugin.farol==2) {
	  				for(String p : plugin.participantes) {
	  					plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §eFarol: ▇");
	  				}
		  			plugin.farol=3;
	  			}else if(plugin.farol==3) {
	  				for(String p : plugin.participantes) {
	  					plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §4Farol: ▇");
	  				}
		  			plugin.farol=4;
	  			}else if(plugin.farol==4) {
	  				for(String p : plugin.participantes) {
	  					plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §eFarol: ▇");
	  				}
		  			plugin.farol=1;
	  			}
	  		}
	  	}, 0, 3*20);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

	
//EVENTO PARKOUR
	
	public static void cancelarParkourCR() {
		plugin.evento="nenhum";
		for(String p : plugin.participantes) {
			if(plugin.getServer().getPlayer(p).isOnline())
				if (plugin.w!=null) {
					plugin.getServer().getPlayer(p).teleport(plugin.w.getSpawnLocation());
				}else{
					plugin.getServer().getPlayer(p).sendMessage("§cOcorreu um erro. Notifique alguém da STAFF.");
				}
		}
	
		for(String e : plugin.eliminados) {
			if(plugin.getServer().getPlayer(e).isOnline())
				if (plugin.w!=null) {
					plugin.getServer().getPlayer(e).teleport(plugin.w.getSpawnLocation());
	    	    }else{
	    	    	plugin.getServer().getPlayer(e).sendMessage("§cOcorreu um erro. Notifique alguém da STAFF.");
	    	    }
		}

		plugin.ctcancelareventoauto();
		plugin.startEventoAuto();
		plugin.etapa=0;
		plugin.pos1ParkourCR=null;
		plugin.pos2ParkourCR=null;
		plugin.lobby=false;
		plugin.participantes.clear();
		plugin.confirmEntrarEvento.clear();
		plugin.eliminados.clear();
		cttasksP();
	}
	
	public static void iniciarParkourCR() {
		teleportarParkourInicioCR();
		plugin.getServer().broadcastMessage("§d[Evento Automático] §fIniciando evento!");
		plugin.etapa=2;
	}
	
	public static void carregarParkourCR() {
		if(plugin.etapa==0) {
			if(plugin.getConfig().contains("posicoes")) {
				if(!plugin.getConfig().contains("posicoes.pos1parkourcr"))
					plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cPosição 1 não encontrada!");
				else if(!plugin.getConfig().contains("posicoes.pos2parkourcr"))
					plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cPosição 2 não encontrada!");
				else {
					String[] me=plugin.getConfig().getString("posicoes.pos1parkourcr").split(";");
					Location l=new Location(plugin.getServer().getWorld("world_eventos"),Double.parseDouble(me[0]),Double.parseDouble(me[1]),Double.parseDouble(me[2]));
					plugin.pos1ParkourCR=l;
					me=plugin.getConfig().getString("posicoes.pos2parkourcr").split(";");
					l=new Location(plugin.getServer().getWorld("world_eventos"),Double.parseDouble(me[0]),Double.parseDouble(me[1]),Double.parseDouble(me[2]));
					plugin.pos2ParkourCR=l;
					plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §fPosições carregadas com sucesso!");
				}
			}
			else
				plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cNão há posições salvas!");
		}else
			plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §cVocê não pode carregar nada após preparar/iniciar o plugin!");
	}

	public static void teleportarParkourInicioCR() {
		for(String p : plugin.participantes) {
        	Random randomgen=new Random();
        	int i=randomgen.nextInt(10)+1;
        	if(i==1) {
    			Location locParkourStart=new Location(plugin.getServer().getWorld("world_eventos"), -223.5, 66, 38.5);
    			locParkourStart.setPitch(0);
    			locParkourStart.setYaw(270);
    			plugin.getServer().getPlayer(p).teleport(locParkourStart);
        	}else if(i==2) {
    			Location locParkourStart=new Location(plugin.getServer().getWorld("world_eventos"), -224.5, 66, 38.5);
    			locParkourStart.setPitch(0);
    			locParkourStart.setYaw(270);
    			plugin.getServer().getPlayer(p).teleport(locParkourStart);
        	}else if(i==3) {
    			Location locParkourStart=new Location(plugin.getServer().getWorld("world_eventos"), -225.5, 66, 38.5);
    			locParkourStart.setPitch(0);
    			locParkourStart.setYaw(270);
    			plugin.getServer().getPlayer(p).teleport(locParkourStart);
        	}else if(i==4) {
    			Location locParkourStart=new Location(plugin.getServer().getWorld("world_eventos"), -226.5, 66, 38.5);
    			locParkourStart.setPitch(0);
    			locParkourStart.setYaw(270);
    			plugin.getServer().getPlayer(p).teleport(locParkourStart);
        	}else if(i==5) {
    			Location locParkourStart=new Location(plugin.getServer().getWorld("world_eventos"), -227.5, 66, 38.5);
    			locParkourStart.setPitch(0);
    			locParkourStart.setYaw(270);
    			plugin.getServer().getPlayer(p).teleport(locParkourStart);
        	}else if(i==6) {
    			Location locParkourStart=new Location(plugin.getServer().getWorld("world_eventos"), -223.5, 66, 39.5);
    			locParkourStart.setPitch(0);
    			locParkourStart.setYaw(270);
    			plugin.getServer().getPlayer(p).teleport(locParkourStart);
        	}else if(i==7) {
    			Location locParkourStart=new Location(plugin.getServer().getWorld("world_eventos"), -224.5, 66, 39.5);
    			locParkourStart.setPitch(0);
    			locParkourStart.setYaw(270);
    			plugin.getServer().getPlayer(p).teleport(locParkourStart);
        	}else if(i==8) {
    			Location locParkourStart=new Location(plugin.getServer().getWorld("world_eventos"), -225.5, 66, 39.5);
    			locParkourStart.setPitch(0);
    			locParkourStart.setYaw(270);
    			plugin.getServer().getPlayer(p).teleport(locParkourStart);
        	}else if(i==9) {
    			Location locParkourStart=new Location(plugin.getServer().getWorld("world_eventos"), -226.5, 66, 39.5);
    			locParkourStart.setPitch(0);
    			locParkourStart.setYaw(270);
    			plugin.getServer().getPlayer(p).teleport(locParkourStart);
        	}else if(i==10) {
    			Location locParkourStart=new Location(plugin.getServer().getWorld("world_eventos"), -227.5, 66, 39.5);
    			locParkourStart.setPitch(0);
    			locParkourStart.setYaw(270);
    			plugin.getServer().getPlayer(p).teleport(locParkourStart);
        	}
		}
	}

	public static void teleportarParkourLobbyCR() {
		plugin.etapa=2;
		for(String p : plugin.participantes) {
			Location lobby=new Location(plugin.getServer().getWorld("world_eventos"), -225.5, 66, 35.5);
			lobby.setPitch(0);
			lobby.setYaw(270);
			plugin.getServer().getPlayer(p).teleport(lobby);
			plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §fPrepare-se! O evento já vai começar!");
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	public static void startEventoBau() {
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "loot time Bom+ 0 0 0 11");
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "loot time Bom- 0 0 0 11");
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "loot time Normal 0 0 0 11");
		ttasksP=plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
	  		int timer=9;
	  		public void run() {
	  			if(timer==0) {
	  				plugin.getServer().broadcastMessage("§d[Evento Automático]");
	  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
	  				plugin.getServer().broadcastMessage("§fFim do evento!");
	  				plugin.getServer().broadcastMessage("§d[Evento Automático]");
	  				cancelarEventoBau();
	  			}
	  			if(timer!=0) {
	  				plugin.getServer().broadcastMessage("§d[Evento Automático]");
	  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
	  				plugin.getServer().broadcastMessage("§fTodos os baús estão com");
	  				plugin.getServer().broadcastMessage("§f10 segundos de intervalo");
	  				plugin.getServer().broadcastMessage("§fBom aproveito!");
	  				plugin.getServer().broadcastMessage("§d[Evento Automático]");
	  			}
	    		timer--;
	  		}
	  	}, 0, 10*20);
	}
	
	public static void cancelarEventoBau() {
		plugin.evento="nenhum";
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "loot time Bom+ 0 0 10 59");
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "loot time Bom- 0 0 10 59");
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "loot time Normal 0 0 10 59");
		cttasksP();
		plugin.ctcancelareventoauto();
		plugin.startEventoAuto();
	}
	

	
	public static void startEventoPaz() {
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "rg flag end -w world_the_end invincible allow");
		ttasksP=plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
	  		int timer=9;
	  		public void run() {
	  			if(timer==0) {
	  				plugin.getServer().broadcastMessage("§d[Evento Automático]");
	  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
	  				plugin.getServer().broadcastMessage("§fFim do evento!");
	  				plugin.getServer().broadcastMessage("§d[Evento Automático]");
	  				cancelarEventoPaz();
	  			}
	  			if(timer!=0) {
	  				plugin.getServer().broadcastMessage("§d[Evento Automático]");
	  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
	  				plugin.getServer().broadcastMessage("§fTodos invencíveis no §l/warp end");
	  				plugin.getServer().broadcastMessage("§cA invencibilidade acabará sem aviso prévio");
	  				plugin.getServer().broadcastMessage("§fBom aproveito!");
	  				plugin.getServer().broadcastMessage("§d[Evento Automático]");
	  			}
	    		timer--;
	  		}
	  	}, 0, 10*20);
	}
	
	public static void cancelarEventoPaz() {
		plugin.evento="nenhum";
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "rg flag end -w world_the_end invincible");
		cttasksP();
		plugin.ctcancelareventoauto();
		plugin.startEventoAuto();
	}
	

	
	public static void startEventoLoteria() {
		final Random r=new Random();
		plugin.loteriaValor=r.nextInt(299)+1;
		ttasksP=plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
	  		int timer=10;
	  		int exemplo=r.nextInt(299)+1;
	  		public void run() {
	  			if(timer==0) {
	  				plugin.getServer().broadcastMessage("§d[Evento Automático]");
	  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
	  				plugin.getServer().broadcastMessage("§fFim do evento! Sem vencedores!");
	  				plugin.getServer().broadcastMessage("§d[Evento Automático]");
	  				cancelarEventoLoteria();
	  			}
	  			if(timer!=0) {
	  				plugin.getServer().broadcastMessage("§d[Evento Automático]");
	  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
	  				plugin.getServer().broadcastMessage("§fUse §l/evento loteria <0-300>");
	  				plugin.getServer().broadcastMessage("§fExemplo: /evento loteria "+exemplo);
	  				plugin.getServer().broadcastMessage("§fBom aproveito!");
	  				plugin.getServer().broadcastMessage("§d[Evento Automático]");
	  				plugin.getServer().getConsoleSender().sendMessage("§d[Evento Automático] §fLoteria: "+plugin.loteriaValor);
	  			}
	    		timer--;
	  		}
	  	}, 0, 30*20);
	}
	
	public static void cancelarEventoLoteria() {
		plugin.evento="nenhum";
		plugin.loteriaValor=-1;
		cttasksP();
		plugin.ctcancelareventoauto();
		plugin.startEventoAuto();
	}
	

	
	public static void startEventoDrop() {
		ttasksP=plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
	  		int timer=12;
	  		public void run() {
	  			if(timer==0) {
	  				plugin.getServer().broadcastMessage("§d[Evento Automático]");
	  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
	  				plugin.getServer().broadcastMessage("§fFim do evento!");
	  				plugin.getServer().broadcastMessage("§d[Evento Automático]");
	  				cancelarEventoDrop();
	  			}else if(timer<=10) {
	  				plugin.getServer().broadcastMessage("§d[Evento Automático]");
	  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
	  				plugin.getServer().broadcastMessage("§fPegue os itens no §e/spawn§f!");
	  				plugin.getServer().broadcastMessage("§cCorra, o evento acabará em breve!");
	  				dropAleatorio();
	  			}else if(timer>=11) {
	  				plugin.getServer().broadcastMessage("§d[Evento Automático]");
	  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
	  				plugin.getServer().broadcastMessage("§fPegue os itens no §e/spawn§f!");
	  				plugin.getServer().broadcastMessage("§aCorra, o evento já vai começar!");
	  				plugin.getServer().broadcastMessage("§fBom aproveito!");
	  				plugin.getServer().broadcastMessage("§d[Evento Automático]");
	  			}
	    		timer--;
	  		}
	  	}, 0, 10*20);
	}
	
	public static void dropAleatorio() {
		if(!plugin.getConfig().contains("itensDrop")) {
			plugin.getServer().broadcastMessage("§cNenhum item para dropar disponível.");
			plugin.getServer().broadcastMessage("§fBom aproveito!");
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			return;
		}
		plugin.getServer().getConsoleSender().sendMessage("§3[PDGHEventos] §2carregando locais...");
		List<String> lista=new ArrayList<String>();
		lista=plugin.getConfig().getStringList("itensDrop");
		plugin.getServer().getConsoleSender().sendMessage("§3[PDGHEventos] §2locais carregados com sucesso!");
		
		int id=0;
		int data=0;
		int quantidade=0;
		String itemName=null;
    	Random r = new Random();
    	int idls = r.nextInt(lista.size());
    	String item = (String)lista.get(idls);

    	int etapa=0;
		for(String lo : item.split(";")) {
			if(etapa==0) {
				id=Integer.parseInt(lo);
			}else if(etapa==1){
				data=Integer.parseInt(lo);
			}else if(etapa==2){
				if(!lo.contains("-")){
					quantidade=Integer.parseInt(lo);
				}else{
			    	int etapaa=0;
			    	int min=0;
			    	int max=0;
					for(String quantity : lo.split("-")) {
						if(etapaa==0){
							min=Integer.parseInt(quantity);
						}else if(etapaa==1){
							max=Integer.parseInt(quantity);
						}
						etapaa++;
					}
		        	Random randomgen = new Random();
		        	quantidade=randomgen.nextInt(max-min)+min;
				}
			}else if(etapa==3){
				itemName=lo;
			}
			etapa++;
		}
		plugin.getServer().broadcastMessage("§aItem dropado: "+itemName);
		plugin.getServer().broadcastMessage("§aQuantidade: "+quantidade);
		plugin.getServer().broadcastMessage("§fBom aproveito!");
		plugin.getServer().broadcastMessage("§d[Evento Automático]");
    	Location loc = plugin.w.getSpawnLocation();
    	loc.setY(loc.getY()+8);
		int x=1;
		while(x <= quantidade) {
        	Random randomgen = new Random();
        	int xx = randomgen.nextInt(2) + 1;
        	if(xx == 1)
        		loc.setX(plugin.w.getSpawnLocation().getX()+randomgen.nextInt(6-1)+1);
        	else if(xx==2)
        		loc.setX(plugin.w.getSpawnLocation().getX()-randomgen.nextInt(6-1)+1);
        	int zz = randomgen.nextInt(2) + 1;
        	if(zz == 1)
        		loc.setZ(plugin.w.getSpawnLocation().getZ()+randomgen.nextInt(6-1)+1);
        	else if(zz==2)
        		loc.setZ(plugin.w.getSpawnLocation().getZ()-randomgen.nextInt(6-1)+1);
        	plugin.w.playEffect(loc, Effect.EXTINGUISH, 1);
        	plugin.w.playEffect(loc, Effect.SMOKE, 1);
        	plugin.w.playSound(loc, Sound.EXPLODE, 10, 1);
	    	plugin.w.dropItem(loc, new ItemStack(Material.getMaterial(id), 1, (short) data));
			x++;
		}
	}
	
	public static void cancelarEventoDrop() {
		plugin.evento="nenhum";
		cttasksP();
		plugin.ctcancelareventoauto();
		plugin.startEventoAuto();
	}

	public static void setEvento(String e) {
		plugin.evento=e;
	}
	
	public static void cancelarTodosEventos() {
		if(plugin.evento=="FrogSR") {
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			plugin.getServer().broadcastMessage("§cCancelando evento Frog...");
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			cancelarFrogSR();
		}
		if(plugin.evento=="FrogCR") {
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			plugin.getServer().broadcastMessage("§cCancelando evento Frog...");
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			cancelarFrogCR();
		}
		if(plugin.evento=="SpleefSR") {
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			plugin.getServer().broadcastMessage("§cCancelando evento Spleef...");
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			cancelarSpleefSR();
		}
		if(plugin.evento=="SpleefCR") {
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			plugin.getServer().broadcastMessage("§cCancelando evento Spleef...");
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			cancelarSpleefCR();
		}
		if(plugin.evento=="MaratonaCR") {
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			plugin.getServer().broadcastMessage("§cCancelando evento Maratona...");
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			cancelarMaratonaCR();
		}
		if(plugin.evento=="FarolSR") {
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			plugin.getServer().broadcastMessage("§cCancelando evento Farol...");
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			cancelarFarolSR();
		}
		if(plugin.evento=="ParkourCR") {
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			plugin.getServer().broadcastMessage("§cCancelando evento Parkour...");
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			cancelarParkourCR();
		}
		if(plugin.evento=="Bau") {
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			plugin.getServer().broadcastMessage("§cCancelando evento Bau...");
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			cancelarEventoBau();
		}
		if(plugin.evento=="Paz") {
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			plugin.getServer().broadcastMessage("§cCancelando evento Paz...");
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			cancelarEventoPaz();
		}
		if(plugin.evento=="Loteria") {
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			plugin.getServer().broadcastMessage("§cCancelando evento Loteria...");
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			cancelarEventoLoteria();
		}
		if(plugin.evento=="Drop") {
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			plugin.getServer().broadcastMessage("§cCancelando evento Drop...");
			plugin.getServer().broadcastMessage("§d[Evento Automático]");
			cancelarEventoDrop();
		}
	}
}
