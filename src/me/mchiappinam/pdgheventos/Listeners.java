package me.mchiappinam.pdgheventos;

import me.mchiappinam.pdgheventos.Comandos;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Listeners implements Listener {

	private Main plugin;
	public Listeners(Main main) {
		plugin=main;
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	private void onPCmd(PlayerCommandPreprocessEvent e) {
		if(e.getPlayer().hasPermission("pdgh.admin"))
			return;
	    if(e.getPlayer().getWorld()==plugin.getServer().getWorld("world_eventos")) {
	    	if((plugin.participantes.contains(e.getPlayer().getName().toLowerCase()))||(plugin.eliminados.contains(e.getPlayer().getName().toLowerCase()))) {
	    		if(!e.getMessage().toLowerCase().startsWith(("/evento sair"))) {
	    			e.setCancelled(true);
	    			e.getPlayer().sendMessage("§cApenas o comando §f/evento sair §cé liberado.");
	    		}
	    	}
    	}
	}

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
    	if((e.getPlayer().getWorld()==plugin.getServer().getWorld("world_eventos"))||(e.getPlayer().getWorld()==plugin.getServer().getWorld("world_evento"))) {
    		if (plugin.w!=null) {
    	    	e.getPlayer().teleport(plugin.w.getSpawnLocation());
        		e.getPlayer().sendMessage("§d[Evento Automático] §eVocê deslogou no mundo de eventos e foi teleportado para o spawn!");
    	    }else{
    	    	e.getPlayer().sendMessage("§cOcorreu um erro. Notifique alguém da STAFF.");
    	    }
    	}
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent e) {
    	if(plugin.confirmEntrarEvento.contains(e.getPlayer().getName().toLowerCase()))
    		plugin.confirmEntrarEvento.remove(e.getPlayer().getName().toLowerCase());
    	if(plugin.evento!="nenhum") {
    		if(plugin.participantes.contains(e.getPlayer().getName().toLowerCase())) {
    			plugin.participantes.remove(e.getPlayer().getName().toLowerCase());
    			if((plugin.evento=="FrogSR")||(plugin.evento=="FrogCR")||(plugin.evento=="MaratonaCR")||(plugin.evento=="FarolSR")||(plugin.evento=="ParkourCR"))
    				if(plugin.evento.contains("CR"))
	   					e.getPlayer().setHealth(0);
    				if(plugin.participantes.size()<1) {
	    				if(plugin.lobby)
	    					return;
			    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
		  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
			    		plugin.getServer().broadcastMessage("§f"+e.getPlayer().getName()+" foi o último a sair!");
			    		plugin.getServer().broadcastMessage("§fFim do evento! Sem vencedores!");
			    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
			    		if(plugin.evento=="FrogSR")
			    			Comandos.cancelarFrogSR();
			    		else if(plugin.evento=="FrogCR")
			    			Comandos.cancelarFrogCR();
			    		else if(plugin.evento=="MaratonaCR")
			    			Comandos.cancelarMaratonaCR();
			    		else if(plugin.evento=="FarolSR")
			    			Comandos.cancelarFarolSR();
			    		else if(plugin.evento=="ParkourCR")
			    			Comandos.cancelarParkourCR();
			    		return;
		   			}else{
		   				for(String p : plugin.participantes) {
		   					if(plugin.evento.contains("CR"))
		   						plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" desconectou-se no evento e morreu. §e(§c-1§e/"+plugin.participantes.size()+")");
		   					else
		   						plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" desconectou-se no evento. §e(§c-1§e/"+plugin.participantes.size()+")");
		   					
		   				}
		   				for(String el : plugin.eliminados) {
		   					if(plugin.evento.contains("CR"))
		   						plugin.getServer().getPlayer(el).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" desconectou-se no evento e morreu. §e(§c-1§e/"+plugin.participantes.size()+")");
		   					else
		   						plugin.getServer().getPlayer(el).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" desconectou-se no evento. §e(§c-1§e/"+plugin.participantes.size()+")");
		   					
		   				}
		   			}
    			if((plugin.evento=="SpleefSR")||(plugin.evento=="SpleefCR"))
	    			if(plugin.participantes.size()==1) {
	    				if(plugin.lobby)
	    					return;
		    			for(String p : plugin.participantes) {
		    				plugin.getServer().broadcastMessage("§d[Evento Automático]");
			  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
				    		plugin.getServer().broadcastMessage("§f"+plugin.getServer().getPlayer(p).getName()+" venceu!");
				    		plugin.getServer().broadcastMessage("§fFim do evento!");
				    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
				    		if(plugin.premio!=0) {
				    			plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §fVocê recebeu $"+plugin.premio+" coins.");
				    			Main.econ.depositPlayer(plugin.getServer().getPlayer(p).getName(), plugin.premio);
				    		}
				    		

				    		if(plugin.evento=="SpleefSR")
				    			Comandos.cancelarSpleefSR();
				    		else if(plugin.evento=="SpleefCR")
				    			Comandos.cancelarSpleefCR();
				    		return;
	    				}
		   			}else{
		   				for(String p : plugin.participantes) {
		   					if(plugin.evento.contains("CR"))
		   						plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" desconectou-se no evento e morreu. §e(§c-1§e/"+plugin.participantes.size()+")");
		   					else
		   						plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" desconectou-se no evento. §e(§c-1§e/"+plugin.participantes.size()+")");
		   					
		   				}
		   				for(String el : plugin.eliminados) {
		   					if(plugin.evento.contains("CR"))
		   						plugin.getServer().getPlayer(el).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" desconectou-se no evento e morreu. §e(§c-1§e/"+plugin.participantes.size()+")");
		   					else
		   						plugin.getServer().getPlayer(el).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" desconectou-se no evento. §e(§c-1§e/"+plugin.participantes.size()+")");
		   					
		   				}
		   			}
    				
    		}
        	if(plugin.eliminados.contains(e.getPlayer().getName().toLowerCase())) {
        		plugin.eliminados.remove(e.getPlayer().getName().toLowerCase());
        	}
    	}
    	/**if(plugin.confirmEntrarEvento.contains(e.getPlayer().getName().toLowerCase())) {
    		plugin.confirmEntrarEvento.remove(e.getPlayer().getName().toLowerCase());
    	}
		if(plugin.participantes.contains(e.getPlayer().getName().toLowerCase())) {
			plugin.participantes.remove(e.getPlayer().getName().toLowerCase());
			if(plugin.evento.contains("CR")&&!plugin.lobby)
				e.getPlayer().setHealth(0);
			
			if(plugin.participantes.size()<1) {
				if(plugin.lobby)
					return;
	    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
	    		plugin.getServer().broadcastMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" foi o último a desconectar!");
	    		plugin.getServer().broadcastMessage("§d[Evento Automático] §fFim do evento! Sem vencedores!");
	    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
			
	    		Comandos.cancelarFrogSR();
	    		return;
   			}
			
			for(String p : plugin.participantes) {
				
				if(plugin.evento.contains("CR"))
					plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" desconectou-se no evento e morreu. §e(§c-1§e/"+plugin.participantes.size()+")");
				else
					plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" desconectou-se no evento. §e(§c-1§e/"+plugin.participantes.size()+")");
				
			}
		}
    	if(plugin.eliminados.contains(e.getPlayer())) {
    		plugin.eliminados.remove(e.getPlayer().getName().toLowerCase());
    	}*/
    }

    @EventHandler
    private void onKick(PlayerKickEvent e) {
    	if(plugin.confirmEntrarEvento.contains(e.getPlayer().getName().toLowerCase()))
    		plugin.confirmEntrarEvento.remove(e.getPlayer().getName().toLowerCase());
    	if(plugin.evento!="nenhum") {
    		if(plugin.participantes.contains(e.getPlayer().getName().toLowerCase())) {
    			plugin.participantes.remove(e.getPlayer().getName().toLowerCase());
    			if((plugin.evento=="FrogSR")||(plugin.evento=="FrogCR")||(plugin.evento=="MaratonaCR")||(plugin.evento=="FarolSR")||(plugin.evento=="ParkourCR"))
    				if(plugin.evento.contains("CR"))
	   					e.getPlayer().setHealth(0);
    				if(plugin.participantes.size()<1) {
	    				if(plugin.lobby)
	    					return;
			    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
		  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
			    		plugin.getServer().broadcastMessage("§f"+e.getPlayer().getName()+" foi o último a sair!");
			    		plugin.getServer().broadcastMessage("§fFim do evento! Sem vencedores!");
			    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
			    		if(plugin.evento=="FrogSR")
			    			Comandos.cancelarFrogSR();
			    		else if(plugin.evento=="FrogCR")
			    			Comandos.cancelarFrogCR();
			    		else if(plugin.evento=="MaratonaCR")
			    			Comandos.cancelarMaratonaCR();
			    		else if(plugin.evento=="FarolSR")
			    			Comandos.cancelarFarolSR();
			    		else if(plugin.evento=="ParkourCR")
			    			Comandos.cancelarParkourCR();
			    		return;
		   			}else{
		   				for(String p : plugin.participantes) {
		   					if(plugin.evento.contains("CR"))
		   						plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" desconectou-se no evento e morreu. §e(§c-1§e/"+plugin.participantes.size()+")");
		   					else
		   						plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" desconectou-se no evento. §e(§c-1§e/"+plugin.participantes.size()+")");
		   					
		   				}
		   				for(String el : plugin.eliminados) {
		   					if(plugin.evento.contains("CR"))
		   						plugin.getServer().getPlayer(el).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" desconectou-se no evento e morreu. §e(§c-1§e/"+plugin.participantes.size()+")");
		   					else
		   						plugin.getServer().getPlayer(el).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" desconectou-se no evento. §e(§c-1§e/"+plugin.participantes.size()+")");
		   					
		   				}
		   			}
    			if((plugin.evento=="SpleefSR")||(plugin.evento=="SpleefCR"))
	    			if(plugin.participantes.size()==1) {
	    				if(plugin.lobby)
	    					return;
		    			for(String p : plugin.participantes) {
		    				plugin.getServer().broadcastMessage("§d[Evento Automático]");
			  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
				    		plugin.getServer().broadcastMessage("§f"+plugin.getServer().getPlayer(p).getName()+" venceu!");
				    		plugin.getServer().broadcastMessage("§fFim do evento!");
				    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
				    		if(plugin.premio!=0) {
				    			plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §fVocê recebeu $"+plugin.premio+" coins.");
				    			Main.econ.depositPlayer(plugin.getServer().getPlayer(p).getName(), plugin.premio);
				    		}
				    		

				    		if(plugin.evento=="SpleefSR")
				    			Comandos.cancelarSpleefSR();
				    		else if(plugin.evento=="SpleefCR")
				    			Comandos.cancelarSpleefCR();
				    		return;
	    				}
		   			}else{
		   				for(String p : plugin.participantes) {
		   					if(plugin.evento.contains("CR"))
		   						plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" desconectou-se no evento e morreu. §e(§c-1§e/"+plugin.participantes.size()+")");
		   					else
		   						plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" desconectou-se no evento. §e(§c-1§e/"+plugin.participantes.size()+")");
		   					
		   				}
		   				for(String el : plugin.eliminados) {
		   					if(plugin.evento.contains("CR"))
		   						plugin.getServer().getPlayer(el).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" desconectou-se no evento e morreu. §e(§c-1§e/"+plugin.participantes.size()+")");
		   					else
		   						plugin.getServer().getPlayer(el).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" desconectou-se no evento. §e(§c-1§e/"+plugin.participantes.size()+")");
		   					
		   				}
		   			}
    				
    		}
        	if(plugin.eliminados.contains(e.getPlayer().getName().toLowerCase())) {
        		plugin.eliminados.remove(e.getPlayer().getName().toLowerCase());
        	}
    	}
    }

    @EventHandler
    private void onTeleport(PlayerTeleportEvent e) {
    	if(plugin.confirmEntrarEvento.contains(e.getPlayer().getName().toLowerCase()))
    		plugin.confirmEntrarEvento.remove(e.getPlayer().getName().toLowerCase());
		if(e.getTo().getWorld()==plugin.getServer().getWorld("world_eventos")) {
			if(plugin.evento=="API")
				return;
    		if(!plugin.participantes.contains(e.getPlayer().getName().toLowerCase())) {
				if(e.getPlayer().hasPermission("pdgh.moderador"))
					return;
    			e.setCancelled(true);
    			e.getPlayer().sendMessage("§d[Evento Automático] Você não pode ir para os eventos!");
    		}
		}
    	if(plugin.evento!="nenhum") {
    		if(e.getTo().getWorld()!=plugin.getServer().getWorld("world_eventos"))
	    		if(plugin.participantes.contains(e.getPlayer().getName().toLowerCase())) {
	    			plugin.participantes.remove(e.getPlayer().getName().toLowerCase());
	    			if((plugin.evento=="FrogSR")||(plugin.evento=="FrogCR")||(plugin.evento=="MaratonaCR")||(plugin.evento=="FarolSR")||(plugin.evento=="ParkourCR"))
	    				if(plugin.evento.contains("CR"))
		   					e.getPlayer().setHealth(0);
	    				if(plugin.participantes.size()<1) {
		    				if(plugin.lobby)
		    					return;
				    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
			  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
				    		plugin.getServer().broadcastMessage("§f"+e.getPlayer().getName()+" foi o último a sair!");
				    		plugin.getServer().broadcastMessage("§fFim do evento! Sem vencedores!");
				    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
				    		if(plugin.evento=="FrogSR")
				    			Comandos.cancelarFrogSR();
				    		else if(plugin.evento=="FrogCR")
				    			Comandos.cancelarFrogCR();
				    		else if(plugin.evento=="MaratonaCR")
				    			Comandos.cancelarMaratonaCR();
				    		else if(plugin.evento=="FarolSR")
				    			Comandos.cancelarFarolSR();
				    		else if(plugin.evento=="ParkourCR")
				    			Comandos.cancelarParkourCR();
				    		return;
			   			}else{
			   				for(String p : plugin.participantes) {
			   					if(plugin.evento.contains("CR"))
			   						plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" saiu do evento e morreu. §e(§c-1§e/"+plugin.participantes.size()+")");
			   					else
			   						plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" saiu do evento. §e(§c-1§e/"+plugin.participantes.size()+")");
			   					
			   				}
			   				for(String el : plugin.eliminados) {
			   					if(plugin.evento.contains("CR"))
			   						plugin.getServer().getPlayer(el).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" saiu do evento e morreu. §e(§c-1§e/"+plugin.participantes.size()+")");
			   					else
			   						plugin.getServer().getPlayer(el).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" saiu do evento. §e(§c-1§e/"+plugin.participantes.size()+")");
			   					
			   				}
			   			}
	    			if((plugin.evento=="SpleefSR")||(plugin.evento=="SpleefCR"))
		    			if(plugin.participantes.size()==1) {
		    				if(plugin.lobby)
		    					return;
			    			for(String p : plugin.participantes) {
			    				plugin.getServer().broadcastMessage("§d[Evento Automático]");
				  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
					    		plugin.getServer().broadcastMessage("§f"+plugin.getServer().getPlayer(p).getName()+" venceu!");
					    		plugin.getServer().broadcastMessage("§fFim do evento!");
					    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
					    		if(plugin.premio!=0) {
					    			plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §fVocê recebeu $"+plugin.premio+" coins.");
					    			Main.econ.depositPlayer(plugin.getServer().getPlayer(p).getName(), plugin.premio);
					    		}
					    		
	
					    		if(plugin.evento=="SpleefSR")
					    			Comandos.cancelarSpleefSR();
					    		else if(plugin.evento=="SpleefCR")
					    			Comandos.cancelarSpleefCR();
					    		return;
		    				}
			   			}else{
			   				for(String p : plugin.participantes) {
			   					if(plugin.evento.contains("CR"))
			   						plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" saiu do evento e morreu. §e(§c-1§e/"+plugin.participantes.size()+")");
			   					else
			   						plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" saiu do evento. §e(§c-1§e/"+plugin.participantes.size()+")");

			   				}
			   				for(String el : plugin.eliminados) {
			   					if(plugin.evento.contains("CR"))
			   						plugin.getServer().getPlayer(el).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" saiu do evento e morreu. §e(§c-1§e/"+plugin.participantes.size()+")");
			   					else
			   						plugin.getServer().getPlayer(el).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" saiu do evento. §e(§c-1§e/"+plugin.participantes.size()+")");
			   					
			   				}
			   			}
	    		}
        	if(plugin.eliminados.contains(e.getPlayer().getName().toLowerCase()))
        		plugin.eliminados.remove(e.getPlayer().getName().toLowerCase());
    	}
    
    }

    @EventHandler
    private void onMove(PlayerMoveEvent e) {
    	if(plugin.evento!="nenhum") {
    		if(plugin.evento=="FrogSR") {
			    if(plugin.etapa==2) {
			    	if(plugin.participantes.contains(e.getPlayer().getName().toLowerCase()))
			    		if(plugin.lostEventSR(e.getPlayer())) {
				   			e.getPlayer().sendMessage("§d[Evento Automático] §fVocê foi eliminado!");
				   			e.getPlayer().sendMessage("§d[Evento Automático] §fPara sair digite /evento sair");
				   			if(plugin.participantes.size()<1) {
				   				if(plugin.lobby)
				   					return;
					    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
				  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
					    		plugin.getServer().broadcastMessage("§f"+e.getPlayer().getName()+" foi o último a cair!");
					    		plugin.getServer().broadcastMessage("§fFim do evento! Sem vencedores!");
					    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
							
					    		Comandos.cancelarFrogSR();
				   			}else{
					   			plugin.participantes.remove(e.getPlayer().getName().toLowerCase());
					   			plugin.eliminados.add(e.getPlayer().getName().toLowerCase());
								for(String p : plugin.participantes) {
									plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" caiu. §e(§c-1§e/"+plugin.participantes.size()+")");
								}
				   			}
			    		}
			    	if(plugin.vencedorSR!=null)
				    	if(e.getPlayer().getLocation().getBlockX()==plugin.vencedorSR.getBlockX()&&e.getPlayer().getLocation().getBlockZ()==plugin.vencedorSR.getBlockZ()&&plugin.participantes.contains(e.getPlayer().getName().toLowerCase())) {
				    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
			  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
				    		plugin.getServer().broadcastMessage("§f"+e.getPlayer().getName()+" venceu!");
				    		plugin.getServer().broadcastMessage("§fFim do evento!");
				    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
				    		if(plugin.premio!=0) {
				    			e.getPlayer().sendMessage("§d[Evento Automático] §fVocê recebeu $"+plugin.premio+" coins.");
				    			Main.econ.depositPlayer(e.getPlayer().getName(), plugin.premio);
				    		}
						
				    		Comandos.cancelarFrogSR();
				    	}
			    }
    		}else if(plugin.evento=="FrogCR") {
			    if(plugin.etapa==2) {
			    	if(plugin.participantes.contains(e.getPlayer().getName().toLowerCase()))
			    		if(plugin.lostEventCR(e.getPlayer())) {
				   			e.getPlayer().sendMessage("§d[Evento Automático] §fVocê foi eliminado!");
				   			e.getPlayer().sendMessage("§d[Evento Automático] §fPara sair digite /evento sair");
				   			if(plugin.participantes.size()<1) {
				   				if(plugin.lobby)
				   					return;
					    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
				  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
					    		plugin.getServer().broadcastMessage("§f"+e.getPlayer().getName()+" foi o último a cair!");
					    		plugin.getServer().broadcastMessage("§fFim do evento! Sem vencedores!");
					    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
							
					    		Comandos.cancelarFrogCR();
				   			}else{
					   			plugin.participantes.remove(e.getPlayer().getName().toLowerCase());
					   			plugin.eliminados.add(e.getPlayer().getName().toLowerCase());
								for(String p : plugin.participantes) {
									plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" caiu. §e(§c-1§e/"+plugin.participantes.size()+")");
								}
				   			}
			    		}
			    	if(plugin.vencedorCR!=null)
				    	if(e.getPlayer().getLocation().getBlockX()==plugin.vencedorCR.getBlockX()&&e.getPlayer().getLocation().getBlockZ()==plugin.vencedorCR.getBlockZ()&&plugin.participantes.contains(e.getPlayer().getName().toLowerCase())) {
				    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
			  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
				    		plugin.getServer().broadcastMessage("§f"+e.getPlayer().getName()+" venceu!");
				    		plugin.getServer().broadcastMessage("§fFim do evento!");
				    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
				    		if(plugin.premio!=0) {
				    			e.getPlayer().sendMessage("§d[Evento Automático] §fVocê recebeu $"+plugin.premio+" coins.");
				    			Main.econ.depositPlayer(e.getPlayer().getName(), plugin.premio);
				    		}
						
				    		Comandos.cancelarFrogCR();
				    	}
			    }
    		}else if(plugin.evento=="SpleefSR") {
			    if(plugin.etapa==2) {
			    	if(plugin.participantes.contains(e.getPlayer().getName().toLowerCase())) {
			    		if(plugin.lostEventSpleefSR(e.getPlayer())) {
				   			plugin.participantes.remove(e.getPlayer().getName().toLowerCase());
				   			plugin.eliminados.add(e.getPlayer().getName().toLowerCase());
				   			e.getPlayer().sendMessage("§d[Evento Automático] §fVocê foi eliminado!");
				   			e.getPlayer().sendMessage("§d[Evento Automático] §fPara sair digite /evento sair");
				   			if(plugin.participantes.size()>1) {
								for(String p : plugin.participantes) {
									plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" caiu. §e(§c-1§e/"+plugin.participantes.size()+")");
								}
				   			}else if(plugin.participantes.size()==1) {
				   				for(String p : plugin.participantes) {
				    				plugin.getServer().broadcastMessage("§d[Evento Automático]");
					  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
						    		plugin.getServer().broadcastMessage("§f"+plugin.getServer().getPlayer(p).getName()+" venceu!");
						    		plugin.getServer().broadcastMessage("§fFim do evento!");
						    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
						    		if(plugin.premio!=0) {
						    			plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §fVocê recebeu $"+plugin.premio+" coins.");
						    			Main.econ.depositPlayer(plugin.getServer().getPlayer(p).getName(), plugin.premio);
						    		}
						    		Comandos.cancelarSpleefSR();
						    		break;
			    				}
				   			}
			    		}
			    	}
			    }
    		}else if(plugin.evento=="SpleefCR") {
			    if(plugin.etapa==2) {
			    	if(plugin.participantes.contains(e.getPlayer().getName().toLowerCase())) {
			    		if(plugin.lostEventSpleefCR(e.getPlayer())) {
				   			plugin.participantes.remove(e.getPlayer().getName().toLowerCase());
				   			plugin.eliminados.add(e.getPlayer().getName().toLowerCase());
				   			e.getPlayer().sendMessage("§d[Evento Automático] §fVocê foi eliminado!");
				   			e.getPlayer().sendMessage("§d[Evento Automático] §fPara sair digite /evento sair");
				   			if(plugin.participantes.size()>1) {
								for(String p : plugin.participantes) {
									plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §f"+e.getPlayer().getName()+" caiu. §e(§c-1§e/"+plugin.participantes.size()+")");
								}
				   			}else if(plugin.participantes.size()==1) {
				   				for(String p : plugin.participantes) {
				    				plugin.getServer().broadcastMessage("§d[Evento Automático]");
					  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
						    		plugin.getServer().broadcastMessage("§f"+plugin.getServer().getPlayer(p).getName()+" venceu!");
						    		plugin.getServer().broadcastMessage("§fFim do evento!");
						    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
						    		if(plugin.premio!=0) {
						    			plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §fVocê recebeu $"+plugin.premio+" coins.");
						    			Main.econ.depositPlayer(plugin.getServer().getPlayer(p).getName(), plugin.premio);
						    		}
						    		break;
			    				}
					    		Comandos.cancelarSpleefCR();
				   			}
			    		}
			    	}
			    }
    		}else if(plugin.evento=="MaratonaCR") {
			    if(plugin.etapa==2) {
			    	if(plugin.participantes.contains(e.getPlayer().getName().toLowerCase())) {
			    		if(plugin.winEventMaratonaCR(e.getPlayer())) {
				    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
			  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
						    plugin.getServer().broadcastMessage("§f"+e.getPlayer().getName()+" venceu!");
						    plugin.getServer().broadcastMessage("§fFim do evento!");
						    plugin.getServer().broadcastMessage("§d[Evento Automático]");
						    if(plugin.premio!=0) {
						    	e.getPlayer().sendMessage("§d[Evento Automático] §fVocê recebeu $"+plugin.premio+" coins.");
						    	Main.econ.depositPlayer(e.getPlayer().getName(), plugin.premio);
						    }
					    	Comandos.cancelarMaratonaCR();
			    		}
			    	}
			    }
    		}else if(plugin.evento=="FarolSR") {
			    if(plugin.etapa==2) {
			    	if(plugin.participantes.contains(e.getPlayer().getName().toLowerCase())) {
			    		if(((e.getFrom().getBlockX()!=e.getTo().getBlockX())||(e.getFrom().getBlockZ()!=e.getTo().getBlockZ())||(e.getFrom().getBlockY()!=e.getTo().getBlockY())))
				    		if((plugin.farol==1)||(plugin.farol==4)) {
				    			Location locFarolStart=new Location(plugin.getServer().getWorld("world_eventos"), 237.5, 53, 47.5);
				    			locFarolStart.setPitch(0);
				    			locFarolStart.setYaw(270);
				    			e.getPlayer().teleport(locFarolStart);
				    			e.getPlayer().sendMessage("§d[Evento Automático] §cVocê não pode andar com o farol no vermelho.");
				    			e.getPlayer().sendMessage("§d[Evento Automático] §cVocê levou uma multa de trânsito e voltou para o início.");
				    		}else if(plugin.winEventFarolSR(e.getPlayer())) {
					    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
				  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
							    plugin.getServer().broadcastMessage("§f"+e.getPlayer().getName()+" venceu!");
							    plugin.getServer().broadcastMessage("§fFim do evento!");
							    plugin.getServer().broadcastMessage("§d[Evento Automático]");
							    if(plugin.premio!=0) {
							    	e.getPlayer().sendMessage("§d[Evento Automático] §fVocê recebeu $"+plugin.premio+" coins.");
							    	Main.econ.depositPlayer(e.getPlayer().getName(), plugin.premio);
							    }
						    	Comandos.cancelarFarolSR();
				    		}
			    	}
			    }
    		}else if(plugin.evento=="ParkourCR") {
			    if(plugin.etapa==2) {
			    	if(plugin.participantes.contains(e.getPlayer().getName().toLowerCase())) {
			    		if(plugin.winEventParkourCR(e.getPlayer())) {
				    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
			  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
						    plugin.getServer().broadcastMessage("§f"+e.getPlayer().getName()+" venceu!");
						    plugin.getServer().broadcastMessage("§fFim do evento!");
						    plugin.getServer().broadcastMessage("§d[Evento Automático]");
						    if(plugin.premio!=0) {
						    	e.getPlayer().sendMessage("§d[Evento Automático] §fVocê recebeu $"+plugin.premio+" coins.");
						    	Main.econ.depositPlayer(e.getPlayer().getName(), plugin.premio);
						    }
					    	Comandos.cancelarParkourCR();
			    		}
			    	}
			    }
    		}
    	}
    }
    
    @EventHandler
    private void onBFade(BlockFadeEvent e) {
	    //if(plugin.etapa!=0)
	    	//if((plugin.isInsideEventSR(e.getBlock().getLocation()))||(plugin.isInsideEventCR(e.getBlock().getLocation())))
		e.setCancelled(true);
    }
    
    @EventHandler
    private void onDeath(PlayerDeathEvent e) {
    	if(plugin.evento!="nenhum") {
    		if(plugin.evento=="FrogSR") {
			    if((plugin.participantes.contains(e.getEntity().getName().toLowerCase()))||(plugin.eliminados.contains(e.getEntity().getName().toLowerCase()))) {
			    	if(plugin.participantes.contains(e.getEntity().getName().toLowerCase()))
						plugin.participantes.remove(e.getEntity().getName().toLowerCase());
					if(plugin.eliminados.contains(e.getEntity().getName().toLowerCase()))
						plugin.eliminados.remove(e.getEntity().getName().toLowerCase());
		
		   			if(plugin.participantes.size()<1) {
			    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
		  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
			    		plugin.getServer().broadcastMessage("§f"+e.getEntity().getName()+" foi o último a morrer!");
			    		plugin.getServer().broadcastMessage("§fFim do evento! Sem vencedores!");
			    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
					
			    		Comandos.cancelarFrogSR();
		   			}
			    }
    		}else if(plugin.evento=="FrogCR") {
			    if((plugin.participantes.contains(e.getEntity().getName().toLowerCase()))||(plugin.eliminados.contains(e.getEntity().getName().toLowerCase()))) {
			    	if(plugin.participantes.contains(e.getEntity().getName().toLowerCase()))
						plugin.participantes.remove(e.getEntity().getName().toLowerCase());
					if(plugin.eliminados.contains(e.getEntity().getName().toLowerCase()))
						plugin.eliminados.remove(e.getEntity().getName().toLowerCase());
		
		   			if(plugin.participantes.size()<1) {
			    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
		  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
			    		plugin.getServer().broadcastMessage("§f"+e.getEntity().getName()+" foi o último a morrer!");
			    		plugin.getServer().broadcastMessage("§fFim do evento! Sem vencedores!");
			    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
					
			    		Comandos.cancelarFrogCR();
		   			}
			    }
    		}else if(plugin.evento=="SpleefSR") {
			    if((plugin.participantes.contains(e.getEntity().getName().toLowerCase()))||(plugin.eliminados.contains(e.getEntity().getName().toLowerCase()))) {
			    	if(plugin.participantes.contains(e.getEntity().getName().toLowerCase()))
						plugin.participantes.remove(e.getEntity().getName().toLowerCase());
					if(plugin.eliminados.contains(e.getEntity().getName().toLowerCase()))
						plugin.eliminados.remove(e.getEntity().getName().toLowerCase());
		
					if(plugin.participantes.size()==1) {
		   				for(String p : plugin.participantes) {
		    				plugin.getServer().broadcastMessage("§d[Evento Automático]");
			  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
				    		plugin.getServer().broadcastMessage("§f"+plugin.getServer().getPlayer(p).getName()+" venceu!");
				    		plugin.getServer().broadcastMessage("§fFim do evento!");
				    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
				    		if(plugin.premio!=0) {
				    			plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §fVocê recebeu $"+plugin.premio+" coins.");
				    			Main.econ.depositPlayer(plugin.getServer().getPlayer(p).getName(), plugin.premio);
				    		}
				    		Comandos.cancelarSpleefSR();
				    		break;
	    				}
		   			}
			    }
    		}else if(plugin.evento=="SpleefCR") {
			    if((plugin.participantes.contains(e.getEntity().getName().toLowerCase()))||(plugin.eliminados.contains(e.getEntity().getName().toLowerCase()))) {
			    	if(plugin.participantes.contains(e.getEntity().getName().toLowerCase()))
						plugin.participantes.remove(e.getEntity().getName().toLowerCase());
					if(plugin.eliminados.contains(e.getEntity().getName().toLowerCase()))
						plugin.eliminados.remove(e.getEntity().getName().toLowerCase());
		
					if(plugin.participantes.size()==1) {
		   				for(String p : plugin.participantes) {
		    				plugin.getServer().broadcastMessage("§d[Evento Automático]");
			  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
				    		plugin.getServer().broadcastMessage("§f"+plugin.getServer().getPlayer(p).getName()+" venceu!");
				    		plugin.getServer().broadcastMessage("§fFim do evento!");
				    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
				    		if(plugin.premio!=0) {
				    			plugin.getServer().getPlayer(p).sendMessage("§d[Evento Automático] §fVocê recebeu $"+plugin.premio+" coins.");
				    			Main.econ.depositPlayer(plugin.getServer().getPlayer(p).getName(), plugin.premio);
				    		}
				    		Comandos.cancelarSpleefCR();
				    		break;
	    				}
		   			}
			    }
    		}else if(plugin.evento=="MaratonaCR") {
			    if((plugin.participantes.contains(e.getEntity().getName().toLowerCase()))||(plugin.eliminados.contains(e.getEntity().getName().toLowerCase()))) {
			    	if(plugin.participantes.contains(e.getEntity().getName().toLowerCase()))
						plugin.participantes.remove(e.getEntity().getName().toLowerCase());
					if(plugin.eliminados.contains(e.getEntity().getName().toLowerCase()))
						plugin.eliminados.remove(e.getEntity().getName().toLowerCase());
		
		   			if(plugin.participantes.size()<1) {
			    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
		  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
			    		plugin.getServer().broadcastMessage("§f"+e.getEntity().getName()+" foi o último a morrer!");
			    		plugin.getServer().broadcastMessage("§fFim do evento! Sem vencedores!");
			    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
					
			    		Comandos.cancelarMaratonaCR();
		   			}
			    }
    		}else if(plugin.evento=="FarolSR") {
			    if((plugin.participantes.contains(e.getEntity().getName().toLowerCase()))||(plugin.eliminados.contains(e.getEntity().getName().toLowerCase()))) {
			    	if(plugin.participantes.contains(e.getEntity().getName().toLowerCase()))
						plugin.participantes.remove(e.getEntity().getName().toLowerCase());
					if(plugin.eliminados.contains(e.getEntity().getName().toLowerCase()))
						plugin.eliminados.remove(e.getEntity().getName().toLowerCase());
		
		   			if(plugin.participantes.size()<1) {
			    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
		  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
			    		plugin.getServer().broadcastMessage("§f"+e.getEntity().getName()+" foi o último a morrer!");
			    		plugin.getServer().broadcastMessage("§fFim do evento! Sem vencedores!");
			    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
					
			    		Comandos.cancelarFarolSR();
		   			}
			    }
    		}else if(plugin.evento=="ParkourCR") {
			    if((plugin.participantes.contains(e.getEntity().getName().toLowerCase()))||(plugin.eliminados.contains(e.getEntity().getName().toLowerCase()))) {
			    	if(plugin.participantes.contains(e.getEntity().getName().toLowerCase()))
						plugin.participantes.remove(e.getEntity().getName().toLowerCase());
					if(plugin.eliminados.contains(e.getEntity().getName().toLowerCase()))
						plugin.eliminados.remove(e.getEntity().getName().toLowerCase());
		
		   			if(plugin.participantes.size()<1) {
			    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
		  				plugin.getServer().broadcastMessage("§fEvento §l"+plugin.evento.replace("SR", "").replace("CR", ""));
			    		plugin.getServer().broadcastMessage("§f"+e.getEntity().getName()+" foi o último a morrer!");
			    		plugin.getServer().broadcastMessage("§fFim do evento! Sem vencedores!");
			    		plugin.getServer().broadcastMessage("§d[Evento Automático]");
					
			    		Comandos.cancelarParkourCR();
		   			}
			    }
    		}
    	}
    }
    
    @EventHandler
    private void onDrop(PlayerDropItemEvent e) {
	    if((plugin.participantes.contains(e.getPlayer().getName().toLowerCase()))||(plugin.eliminados.contains(e.getPlayer().getName().toLowerCase()))) {
	    	e.setCancelled(true);
	    	e.getPlayer().sendMessage("§d[Evento Automático] §cVocê não pode dropar itens.");
	    }
    }
    
    @EventHandler
    private void onPick(PlayerPickupItemEvent e) {
	    if((plugin.participantes.contains(e.getPlayer().getName().toLowerCase()))||(plugin.eliminados.contains(e.getPlayer().getName().toLowerCase())))
	    	e.setCancelled(true);
    }
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		if(plugin.evento=="nenhum")
		    if(e.getPlayer().getWorld()==plugin.getServer().getWorld("world_eventos")) {
	        	if (plugin.w!=null) {
		        	//e.getPlayer().sendMessage("§d[Evento Automático] §cVocê morreu :(");
		        	e.setRespawnLocation(plugin.w.getSpawnLocation());
		        }else{
		        	e.getPlayer().sendMessage("§cOcorreu um erro. Notifique alguém da STAFF.");
		        }
		        
		    }
	}
    
    @EventHandler
    private void onBlockBreak(final BlockBreakEvent e) {
    	try {
    		if(((plugin.evento=="SpleefSR")&&(!plugin.lobby))||((plugin.evento=="SpleefCR")&&(!plugin.lobby))) {
    		    if(e.getBlock().getWorld()==plugin.getServer().getWorld("world_eventos")) {
	    			if(plugin.participantes.contains(e.getPlayer().getName().toLowerCase())) {
	            		if(e.getBlock().getType()==Material.SNOW_BLOCK) {
	                		if(plugin.etapa!=2) {
	    		    			e.setCancelled(true);
	    		    			e.getPlayer().sendMessage("§d[Evento Automático] §cAguarde o evento começar!");
	    		    			return;
	                		}
			    			e.setCancelled(true);
			    			e.getBlock().setType(Material.AIR);
			    			e.getBlock().getWorld().playSound(e.getBlock().getLocation(), Sound.DIG_SNOW, 1, 1);
			    			plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
			    				public void run() {
			    					if(((plugin.evento=="SpleefSR")&&(!plugin.lobby))||((plugin.evento=="SpleefCR")&&(!plugin.lobby))) {
				    					e.getBlock().getWorld().playSound(e.getBlock().getLocation(), Sound.PISTON_EXTEND, 1, 1);
				    					e.getBlock().setType(Material.SNOW_BLOCK);
			    					}
			    				}
			    			}, 75*20);
	            		}
	    			}else if(plugin.eliminados.contains(e.getPlayer().getName().toLowerCase())) {
		    			e.setCancelled(true);
		    			e.getPlayer().sendMessage("§d[Evento Automático] §cVocê está eliminado!");
	    			}
		    	}
    		}
    	}catch(Exception e2) {}
    }
    
    @EventHandler
    private void onBlockPlace(BlockPlaceEvent e) {
    	if(((plugin.evento=="SpleefSR")&&(!plugin.lobby))||((plugin.evento=="SpleefCR")&&(!plugin.lobby))) {
		    if(e.getBlock().getWorld()==plugin.getServer().getWorld("world_eventos")) {
    			if((plugin.participantes.contains(e.getPlayer().getName().toLowerCase()))||(plugin.eliminados.contains(e.getPlayer().getName().toLowerCase()))) {
	    			e.setCancelled(true);
	    			e.getPlayer().sendMessage("§d[Evento Automático] §cVocê não pode colocar blocos no evento Spleef!");
    			}
	    	}
		}
    }
}
