package me.mchiappinam.pdgheventos;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Tameable;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	protected List<String> materiais=new ArrayList<String>();
	protected HashMap<String, Boolean> material_sumiu=new HashMap<String, Boolean>();
	protected List<Location> material_loc=new ArrayList<Location>();
	protected List<String> participantes=new ArrayList<String>();
	protected List<String> eliminados=new ArrayList<String>();
	protected List<String> confirmEntrarEvento=new ArrayList<String>();
	protected String evento="nenhum"; // evento.replace("SR", "").replace("CR", "")+" ocorrendo no momento."
	protected boolean lobby=false;
	static int teventoauto;
	static int tcancelareventoauto;
	
	protected Location pos1SR=null;
	protected Location pos2SR=null;
	protected Location saidaSR=null;
	protected Location vencedorSR=null;
	
	protected Location pos1CR=null;
	protected Location pos2CR=null;
	protected Location saidaCR=null;
	protected Location vencedorCR=null;
	
	protected Location pos1SpleefSR=null;
	protected Location pos2SpleefSR=null;
	
	protected Location pos1SpleefCR=null;
	protected Location pos2SpleefCR=null;
	
	protected Location pos1MaratonaCR=null;
	protected Location pos2MaratonaCR=null;
	
	protected Location pos1FarolSR=null;
	protected Location pos2FarolSR=null;
	protected int farol=0;
	
	protected Location pos1ParkourCR=null;
	protected Location pos2ParkourCR=null;

	protected int loteriaValor=-1;
	
	protected int etapa=0;
	
	protected static int bpt=50;
	protected int init=100;
	protected int game=240;
	protected int rmsn=100;
	protected int bksn=25;

	protected boolean frogsr=true;
	protected boolean frogcr=true;
	protected boolean spleefsr=true;
	protected boolean spleefcr=true;
	protected boolean maratonacr=true;
	protected boolean farolsr=true;
	protected boolean parkourcr=true;
	protected boolean bau=false;
	protected boolean paz=false;
	protected boolean loteria=true;
	protected boolean drop=true;
	protected int delayStartAuto=25;
	protected int delayCancelarAuto=8;
	
	protected static Economy econ=null;
	protected int premio=0;
	World w=null;
	
	@Override
    public void onEnable() {
		getServer().getPluginCommand("evento").setExecutor(new Comandos(this));
		getServer().getPluginManager().registerEvents(new Listeners(this), this);
		
		File file=new File(getDataFolder(),"config.yml");
		if(!file.exists()) {
			try {
				saveResource("config_template.yml",false);
				File file2=new File(getDataFolder(),"config_template.yml");
				file2.renameTo(new File(getDataFolder(),"config.yml"));
			}
			catch(Exception e) {}
		}
		
		if(setupEconomy()) {
			getServer().getConsoleSender().sendMessage("§3[PDGHEventos] §2Plugin Vault encontrado!");
			if(!getConfig().contains("Premio")) {
				getConfig().set("Premio", 1);
				saveConfig();
			}
			premio=getConfig().getInt("Premio");
		}
		
		if(!ccontains("mundoPrincipal"))
			cfix("mundoPrincipal",w);
		
		if(!ccontains("Bpt"))
			cfix("Bpt",bpt);
		
		if(!ccontains("Timer.Iniciando"))
			cfix("Timer.Iniciando",init);
		
		if(!ccontains("Timer.Rodada"))
			cfix("Timer.Rodada",game);
		
		if(!ccontains("Timer.TirarNeve"))
			cfix("Timer.TirarNeve",rmsn);
		
		if(!ccontains("Timer.VoltarNeve"))
			cfix("Timer.VoltarNeve",bksn);
		
		if(!ccontains("evento.frogsr"))
			cfix("evento.frogsr",frogsr);
		
		if(!ccontains("evento.frogcr"))
			cfix("evento.frogcr",frogcr);
		
		if(!ccontains("evento.spleefsr"))
			cfix("evento.spleefsr",spleefsr);
		
		if(!ccontains("evento.spleefcr"))
			cfix("evento.spleefcr",spleefcr);
		
		if(!ccontains("evento.maratonacr"))
			cfix("evento.maratonacr",maratonacr);
		
		if(!ccontains("evento.farolsr"))
			cfix("evento.farolsr",farolsr);
		
		if(!ccontains("evento.parkourcr"))
			cfix("evento.parkourcr",parkourcr);
		
		if(!ccontains("evento.bau"))
			cfix("evento.bau",bau);
		
		if(!ccontains("evento.paz"))
			cfix("evento.paz",paz);
		
		if(!ccontains("evento.loteria"))
			cfix("evento.loteria",loteria);
		
		if(!ccontains("evento.drop"))
			cfix("evento.drop",drop);
		
		if(!ccontains("evento.delayStartAuto"))
			cfix("evento.delayStartAuto",delayStartAuto);
		
		if(!ccontains("evento.delayCancelarAuto"))
			cfix("evento.delayCancelarAuto",delayCancelarAuto);
		
		saveConfig();

		
		try {w=getServer().getWorld(getConfig().getString("mundoPrincipal"));} catch(Exception e) {w=null;}
		try {bpt=getConfig().getInt("Bpt");} catch(Exception e) {bpt=50;}
		try {init=getConfig().getInt("Timer.Iniciando");} catch(Exception e) {init=100;}
		try {game=getConfig().getInt("Timer.Rodada");} catch(Exception e) {game=240;}
		try {rmsn=getConfig().getInt("Timer.TirarNeve");} catch(Exception e) {rmsn=100;}
		try {bksn=getConfig().getInt("Timer.VoltarNeve");} catch(Exception e) {bksn=25;}
		
		try {frogsr=getConfig().getBoolean("evento.frogsr");} catch(Exception e) {}
		try {frogcr=getConfig().getBoolean("evento.frogcr");} catch(Exception e) {}
		try {spleefsr=getConfig().getBoolean("evento.spleefsr");} catch(Exception e) {}
		try {spleefcr=getConfig().getBoolean("evento.spleefcr");} catch(Exception e) {}
		try {maratonacr=getConfig().getBoolean("evento.maratonacr");} catch(Exception e) {}
		try {farolsr=getConfig().getBoolean("evento.farolsr");} catch(Exception e) {}
		try {parkourcr=getConfig().getBoolean("evento.parkourcr");} catch(Exception e) {}
		try {bau=getConfig().getBoolean("evento.bau");} catch(Exception e) {}
		try {paz=getConfig().getBoolean("evento.paz");} catch(Exception e) {}
		try {loteria=getConfig().getBoolean("evento.loteria");} catch(Exception e) {}
		try {drop=getConfig().getBoolean("evento.drop");} catch(Exception e) {}
		try {delayStartAuto=getConfig().getInt("evento.delayStartAuto");} catch(Exception e) {}
		try {delayCancelarAuto=getConfig().getInt("evento.delayCancelarAuto");} catch(Exception e) {}
		
		startEventoAuto();
		
		getServer().getConsoleSender().sendMessage("§3[PDGHEventos] §2ativado - Plugin by: mchiappinam");
		getServer().getConsoleSender().sendMessage("§3[PDGHEventos] §2Acesse: http://pdgh.com.br/");
    }
 
    @Override
    public void onDisable() {
		if(evento!="nenhum") {
			getServer().broadcastMessage("§d[Evento Automático]");
			getServer().broadcastMessage("§cCancelando evento...");
			getServer().broadcastMessage("§d[Evento Automático]");
		}
		if(evento=="Bau") {
			getServer().dispatchCommand(getServer().getConsoleSender(), "loot time Bom+ 0 0 10 59");
			getServer().dispatchCommand(getServer().getConsoleSender(), "loot time Bom- 0 0 10 59");
			getServer().dispatchCommand(getServer().getConsoleSender(), "loot time Normal 0 0 10 59");
		}
		if(evento=="Paz")
			getServer().dispatchCommand(getServer().getConsoleSender(), "rg flag end -w world_the_end invincible");
		
		for(String p : participantes) {
			if (w!=null) {
				getServer().getPlayer(p).teleport(w.getSpawnLocation());
    	    }else{
    	    	getServer().getPlayer(p).sendMessage("§cOcorreu um erro. Notifique alguém da STAFF.");
    	    }
		}
		for(String e : eliminados) {
			if (w!=null) {
				getServer().getPlayer(e).teleport(w.getSpawnLocation());
    	    }else{
    	    	getServer().getPlayer(e).sendMessage("§cOcorreu um erro. Notifique alguém da STAFF.");
    	    }
		}
		getServer().getConsoleSender().sendMessage("§3[PDGHEventos] §2desativado - Plugin by: mchiappinam");
		getServer().getConsoleSender().sendMessage("§3[PDGHEventos] §2Acesse: http://pdgh.com.br/");
    }
	
	private boolean ccontains(String n) {
		return getConfig().contains(n);
	}
	
	private int cfix(String n,Object n2) {
		getConfig().set(n, n2);
		return 1;
	}

	public void startEventoAleatorio() {
		Random randomgen=new Random();
    	int i=randomgen.nextInt(11)+1;
    	if(i==1) {
    		if(!frogsr) {
    			startEventoAleatorio();
    			return;
    		}
			evento="FrogSR";
			Comandos.startEventoSemRisco();
    	}else if(i==2) {
    		if(!frogcr) {
    			startEventoAleatorio();
    			return;
    		}
			evento="FrogCR";
			Comandos.startEventoComRisco("§2OFF");
    	}else if(i==3) {
    		if(!spleefsr) {
    			startEventoAleatorio();
    			return;
    		}
			evento="SpleefSR";
			Comandos.startEventoSemRisco();
    	}else if(i==4) {
    		if(!spleefcr) {
    			startEventoAleatorio();
    			return;
    		}
			evento="SpleefCR";
			Comandos.startEventoComRisco("§2OFF");
    	}else if(i==5) {
    		if(!maratonacr) {
    			startEventoAleatorio();
    			return;
    		}
			evento="MaratonaCR";
			Comandos.startEventoComRisco("§2OFF");
    	}else if(i==6) {
    		if(!farolsr) {
    			startEventoAleatorio();
    			return;
    		}
			evento="FarolSR";
			Comandos.startEventoSemRisco();
    	}else if(i==7) {
    		if(!parkourcr) {
    			startEventoAleatorio();
    			return;
    		}
			evento="ParkourCR";
			Comandos.startEventoComRisco("§2OFF");
    	}else if(i==8) {
    		if(!bau) {
    			startEventoAleatorio();
    			return;
    		}
			evento="Bau";
			Comandos.startEventoBau();
    	}else if(i==9) {
    		if(!paz) {
    			startEventoAleatorio();
    			return;
    		}
			evento="Paz";
			Comandos.startEventoPaz();
    	}else if(i==10) {
    		if(!loteria) {
    			startEventoAleatorio();
    			return;
    		}
			evento="Loteria";
			Comandos.startEventoLoteria();
    	}else if(i==11) {
    		if(!drop) {
    			startEventoAleatorio();
    			return;
    		}
    		if(getServer().getOnlinePlayers().length<20) {
    			getServer().broadcastMessage("§d[Evento Automático] §cEvento drop cancelado por não ter 20 jogadores ou mais online!");
    			startEventoAleatorio();
    			return;
    		}
			evento="Drop";
			Comandos.startEventoDrop();
    	}
	}
	
	public void helpConsole(ConsoleCommandSender c) {
		c.sendMessage("§d[Evento Automático]");
		c.sendMessage("§c/evento iniciar aleatorio -§4- Evento automático aleatorio");
		c.sendMessage("§e/evento iniciar frogsr -§c- Frog (Sem risco)");
		c.sendMessage("§e/evento iniciar frogcr -§c- Frog (Com risco)");
		c.sendMessage("§e/evento iniciar spleefsr -§c- Spleef (Sem risco)");
		c.sendMessage("§e/evento iniciar spleefcr -§c- Spleef (Com risco)");
		c.sendMessage("§e/evento iniciar maratonacr -§c- Maratona (Com risco)");
		c.sendMessage("§e/evento iniciar farolsr -§c- Farol (Sem risco)");
		c.sendMessage("§e/evento iniciar bau -§c- Baú (10 segundos)");
		c.sendMessage("§e/evento iniciar paz -§c- Paz (Invencibilidade)");
		c.sendMessage("§e/evento iniciar loteria -§c- Loteria");
		c.sendMessage("§e/evento iniciar drop -§c- Drop de itens no spawn");
		c.sendMessage("§e/evento cancelar -§c- Cancela o evento ocorrendo");
		c.sendMessage("§d[Evento Automático]");
	}
	
	public void help(Player p) {
		p.sendMessage("§d[Evento Automático AJUDA]");
		p.sendMessage("§e/evento -§c- Entra no lobby de eventos");
		p.sendMessage("§e/evento sair -§c- Sai do lobby de eventos");
		p.sendMessage("§e/evento loteria <0-300> -§c- Aposta na loteria (Grátis)");
		p.sendMessage("§d[Evento Automático AJUDA]");
		if(p.hasPermission("pdgh.admin"))
			helpAdmin(p);
	}
	
	public void helpAdmin(Player p) {
		if(!p.hasPermission("pdgh.op")) {
			noPerm(p);
			return;
		}
		p.sendMessage("§3[Evento Automático STAFF]");
		p.sendMessage("§c/evento iniciar aleatorio -§4- Evento automático aleatório");
		if(frogsr) p.sendMessage("§c/evento iniciar frogsr -§4- Frog (Sem risco)");else p.sendMessage("§4/evento frogsr -§4- DESATIVADO NESSE SERVIDOR");
		if(frogcr) p.sendMessage("§c/evento iniciar frogcr -§4- Frog (Com risco)");else p.sendMessage("§4/evento frogcr -§4- DESATIVADO NESSE SERVIDOR");
		if(spleefsr) p.sendMessage("§c/evento iniciar spleefsr -§4- Spleef (Sem risco)");else p.sendMessage("§4/evento spleefsr -§4- DESATIVADO NESSE SERVIDOR");
		if(spleefcr) p.sendMessage("§c/evento iniciar spleefcr -§4- Spleef (Com risco)");else p.sendMessage("§4/evento spleefcr -§4- DESATIVADO NESSE SERVIDOR");
		if(maratonacr) p.sendMessage("§c/evento iniciar maratonacr -§4- Maratona (Com risco)");else p.sendMessage("§4/evento maratonacr -§4- DESATIVADO NESSE SERVIDOR");
		if(farolsr) p.sendMessage("§c/evento iniciar farolsr -§4- Farol (Sem risco)");else p.sendMessage("§4/evento farolsr -§4- DESATIVADO NESSE SERVIDOR");
		if(parkourcr) p.sendMessage("§c/evento iniciar parkourcr -§4- Parkour (Com risco)");else p.sendMessage("§4/evento parkourcr -§4- DESATIVADO NESSE SERVIDOR");
		if(bau) p.sendMessage("§c/evento iniciar bau -§4- Baú (10 segundos)");else p.sendMessage("§4/evento bau -§4- DESATIVADO NESSE SERVIDOR");
		if(paz) p.sendMessage("§c/evento iniciar paz -§4- Paz (Invencibilidade)");else p.sendMessage("§4/evento paz -§4- DESATIVADO NESSE SERVIDOR");
		if(loteria) p.sendMessage("§c/evento iniciar loteria -§4- Loteria");else p.sendMessage("§4/evento loteria -§4- DESATIVADO NESSE SERVIDOR");
		if(loteria) p.sendMessage("§c/evento iniciar drop -§4- Drop de itens");else p.sendMessage("§4/evento drop -§4- DESATIVADO NESSE SERVIDOR");
		p.sendMessage("§c/evento cancelar -§4- Cancela o evento ocorrendo");
		p.sendMessage("§3[Evento Automático STAFF]");
	}
	
	public void noPerm(Player p) {
		p.sendMessage("§cSem permissões");
	}
    
    protected boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault")==null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp=getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp==null) {
            return false;
        }
        econ=rsp.getProvider();
        return econ!=null;
    }
	
    public void startEventoAuto() {
		teventoauto=getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			int timer=delayStartAuto;
	  		public void run() {
	  			timer--;
	  			if(timer==-1) {
	  				cteventoauto();
	  				startEventoAuto();
	  			}
	  			if(timer==0) {
					if(evento!="nenhum") {
						if(evento=="API") {
			  				getServer().broadcastMessage("§d[Evento Automático] §fInício do evento aleatório cancelado.");
							return;
						}
		  				getServer().broadcastMessage("§d[Evento Automático] §fInício do evento aleatório cancelado.");
		  				getServer().broadcastMessage("§d[Evento Automático] §fEvento §l"+evento.replace("SR", "").replace("CR", "")+"§f ocorrendo no momento.");
					}else{
		  				getServer().broadcastMessage("§d[Evento Automático] §fIniciando evento aleatório automático...");
		  				startEventoAleatorio();
					}
	  				cteventoauto();
	  			}
	  		}
	  	}, 0, 60*20);
	}

	public void cteventoauto() {
		getServer().getScheduler().cancelTask(teventoauto);
	}
	
    public void startCancelarEventoAuto() {
		tcancelareventoauto=getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			int timer=delayCancelarAuto;
	  		public void run() {
	  			timer--;
	  			if(timer==-1)
					ctcancelareventoauto();
	  			if(timer==2)
	  				getServer().broadcastMessage("§d[Evento Automático] §fCancelando evento em 2 minutos");
	  			if(timer==1)
	  				getServer().broadcastMessage("§d[Evento Automático] §fCancelando evento em 1 minuto");
	  			if(timer==0) {
					if(evento=="FrogSR") {
			    		getServer().broadcastMessage("§d[Evento Automático]");
		  				getServer().broadcastMessage("§fEvento §l"+evento.replace("SR", "").replace("CR", ""));
			    		getServer().broadcastMessage("§fTempo limite excedido!");
			    		getServer().broadcastMessage("§fFim do evento!");
			    		getServer().broadcastMessage("§d[Evento Automático]");
						Comandos.cancelarFrogSR();
	  				}
					if(evento=="FrogCR") {
			    		getServer().broadcastMessage("§d[Evento Automático]");
		  				getServer().broadcastMessage("§fEvento §l"+evento.replace("SR", "").replace("CR", ""));
			    		getServer().broadcastMessage("§fTempo limite excedido!");
			    		getServer().broadcastMessage("§fFim do evento!");
			    		getServer().broadcastMessage("§d[Evento Automático]");
						Comandos.cancelarFrogCR();
	  				}
					if(evento=="SpleefSR") {
			    		getServer().broadcastMessage("§d[Evento Automático]");
		  				getServer().broadcastMessage("§fEvento §l"+evento.replace("SR", "").replace("CR", ""));
			    		getServer().broadcastMessage("§fTempo limite excedido!");
			    		getServer().broadcastMessage("§fFim do evento!");
			    		getServer().broadcastMessage("§d[Evento Automático]");
						Comandos.cancelarSpleefSR();
	  				}
					if(evento=="SpleefCR") {
			    		getServer().broadcastMessage("§d[Evento Automático]");
		  				getServer().broadcastMessage("§fEvento §l"+evento.replace("SR", "").replace("CR", ""));
			    		getServer().broadcastMessage("§fTempo limite excedido!");
			    		getServer().broadcastMessage("§fFim do evento!");
			    		getServer().broadcastMessage("§d[Evento Automático]");
						Comandos.cancelarSpleefCR();
	  				}
					if(evento=="MaratonaCR") {
			    		getServer().broadcastMessage("§d[Evento Automático]");
		  				getServer().broadcastMessage("§fEvento §l"+evento.replace("SR", "").replace("CR", ""));
			    		getServer().broadcastMessage("§fTempo limite excedido!");
			    		getServer().broadcastMessage("§fFim do evento!");
			    		getServer().broadcastMessage("§d[Evento Automático]");
						Comandos.cancelarMaratonaCR();
	  				}
					if(evento=="FarolSR") {
			    		getServer().broadcastMessage("§d[Evento Automático]");
		  				getServer().broadcastMessage("§fEvento §l"+evento.replace("SR", "").replace("CR", ""));
			    		getServer().broadcastMessage("§fTempo limite excedido!");
			    		getServer().broadcastMessage("§fFim do evento!");
			    		getServer().broadcastMessage("§d[Evento Automático]");
						Comandos.cancelarFarolSR();
	  				}
					if(evento=="ParkourCR") {
			    		getServer().broadcastMessage("§d[Evento Automático]");
		  				getServer().broadcastMessage("§fEvento §l"+evento.replace("SR", "").replace("CR", ""));
			    		getServer().broadcastMessage("§fTempo limite excedido!");
			    		getServer().broadcastMessage("§fFim do evento!");
			    		getServer().broadcastMessage("§d[Evento Automático]");
						Comandos.cancelarParkourCR();
	  				}
					ctcancelareventoauto();
	  			}
	  		}
	  	}, 0, 60*20);
	}

	public void ctcancelareventoauto() {
		getServer().getScheduler().cancelTask(tcancelareventoauto);
	}

    public static boolean isIntensiveEntity(Entity entity) {
        return entity instanceof Item
                || entity instanceof TNTPrimed
                || entity instanceof ExperienceOrb
                || entity instanceof FallingBlock
                || (entity instanceof LivingEntity
                    && !(entity instanceof Tameable)
                    && !(entity instanceof Player));
    }
    
    /**protected boolean isInsideEventSR(Location p) {
    	if(p.getBlockX()>=getMinXSR())
    		if(p.getBlockX()<=getMaxXSR())
    			if(p.getBlockZ()>=getMinZSR())
    				if(p.getBlockZ()<=getMaxZSR())
    					return true;
    	return false;
    }*/
    
    protected boolean lostEventSR(Player p) {
    	if(p.getLocation().getBlockX()>=getMinXSR())
    		if(p.getLocation().getBlockX()<=getMaxXSR())
    			if(p.getLocation().getBlockZ()>=getMinZSR())
    				if(p.getLocation().getBlockZ()<=getMaxZSR())
    					if(p.getLocation().getBlockY()<=getYSR())
    						return true;
    	return false;
    }
    
    protected int getMinXSR() {
   		if(pos1SR.getBlockX()<pos2SR.getBlockX())
   			return pos1SR.getBlockX();
   		else
   			return pos2SR.getBlockX();
    }
    protected int getMinZSR() {
    	if(pos1SR.getBlockZ()<pos2SR.getBlockZ())
    		return pos1SR.getBlockZ();
    	else
    		return pos2SR.getBlockZ();
    }
    protected int getMaxXSR() {
    	if(pos1SR.getBlockX()>pos2SR.getBlockX())
    		return pos1SR.getBlockX();
    	else
    		return pos2SR.getBlockX();
    }
    protected int getMaxZSR() {
    	if(pos1SR.getBlockZ()>pos2SR.getBlockZ())
    		return pos1SR.getBlockZ();
    	else
    		return pos2SR.getBlockZ();
    }
    protected int getYSR() {
    	return pos1SR.getBlockY();
    }
    
    
    
    

    
    /**protected boolean isInsideEventCR(Location p) {
    	if(p.getBlockX()>=getMinXCR())
    		if(p.getBlockX()<=getMaxXCR())
    			if(p.getBlockZ()>=getMinZCR())
    				if(p.getBlockZ()<=getMaxZCR())
    					return true;
    	return false;
    }*/
    
    protected boolean lostEventCR(Player p) {
    	if(p.getLocation().getBlockX()>=getMinXCR())
    		if(p.getLocation().getBlockX()<=getMaxXCR())
    			if(p.getLocation().getBlockZ()>=getMinZCR())
    				if(p.getLocation().getBlockZ()<=getMaxZCR())
    					if(p.getLocation().getBlockY()<=getYCR())
    						return true;
    	return false;
    }
    
    protected int getMinXCR() {
   		if(pos1CR.getBlockX()<pos2CR.getBlockX())
   			return pos1CR.getBlockX();
   		else
   			return pos2CR.getBlockX();
    }
    protected int getMinZCR() {
    	if(pos1CR.getBlockZ()<pos2CR.getBlockZ())
    		return pos1CR.getBlockZ();
    	else
    		return pos2CR.getBlockZ();
    }
    protected int getMaxXCR() {
    	if(pos1CR.getBlockX()>pos2CR.getBlockX())
    		return pos1CR.getBlockX();
    	else
    		return pos2CR.getBlockX();
    }
    protected int getMaxZCR() {
    	if(pos1CR.getBlockZ()>pos2CR.getBlockZ())
    		return pos1CR.getBlockZ();
    	else
    		return pos2CR.getBlockZ();
    }
    protected int getYCR() {
    	return pos1CR.getBlockY();
    }
    
    
    
    

    
    
    
    
    
    

    
    protected boolean lostEventSpleefSR(Player p) {
    	if(p.getLocation().getBlockX()>=getMinXSpleefSR())
    		if(p.getLocation().getBlockX()<=getMaxXSpleefSR())
    			if(p.getLocation().getBlockZ()>=getMinZSpleefSR())
    				if(p.getLocation().getBlockZ()<=getMaxZSpleefSR())
    					if(p.getLocation().getBlockY()<=getYSpleefSR())
    						return true;
    	return false;
    }
    
    protected int getMinXSpleefSR() {
   		if(pos1SpleefSR.getBlockX()<pos2SpleefSR.getBlockX())
   			return pos1SpleefSR.getBlockX();
   		else
   			return pos2SpleefSR.getBlockX();
    }
    protected int getMinZSpleefSR() {
    	if(pos1SpleefSR.getBlockZ()<pos2SpleefSR.getBlockZ())
    		return pos1SpleefSR.getBlockZ();
    	else
    		return pos2SpleefSR.getBlockZ();
    }
    protected int getMaxXSpleefSR() {
    	if(pos1SpleefSR.getBlockX()>pos2SpleefSR.getBlockX())
    		return pos1SpleefSR.getBlockX();
    	else
    		return pos2SpleefSR.getBlockX();
    }
    protected int getMaxZSpleefSR() {
    	if(pos1SpleefSR.getBlockZ()>pos2SpleefSR.getBlockZ())
    		return pos1SpleefSR.getBlockZ();
    	else
    		return pos2SpleefSR.getBlockZ();
    }
    protected int getYSpleefSR() {
    	return pos1SpleefSR.getBlockY();
    }
    
    
    
    
    
    
    


    
    protected boolean lostEventSpleefCR(Player p) {
    	if(p.getLocation().getBlockX()>=getMinXSpleefCR())
    		if(p.getLocation().getBlockX()<=getMaxXSpleefCR())
    			if(p.getLocation().getBlockZ()>=getMinZSpleefCR())
    				if(p.getLocation().getBlockZ()<=getMaxZSpleefCR())
    					if(p.getLocation().getBlockY()<=getYSpleefCR())
    						return true;
    	return false;
    }
    
    protected int getMinXSpleefCR() {
   		if(pos1SpleefCR.getBlockX()<pos2SpleefCR.getBlockX())
   			return pos1SpleefCR.getBlockX();
   		else
   			return pos2SpleefCR.getBlockX();
    }
    protected int getMinZSpleefCR() {
    	if(pos1SpleefCR.getBlockZ()<pos2SpleefCR.getBlockZ())
    		return pos1SpleefCR.getBlockZ();
    	else
    		return pos2SpleefCR.getBlockZ();
    }
    protected int getMaxXSpleefCR() {
    	if(pos1SpleefCR.getBlockX()>pos2SpleefCR.getBlockX())
    		return pos1SpleefCR.getBlockX();
    	else
    		return pos2SpleefCR.getBlockX();
    }
    protected int getMaxZSpleefCR() {
    	if(pos1SpleefCR.getBlockZ()>pos2SpleefCR.getBlockZ())
    		return pos1SpleefCR.getBlockZ();
    	else
    		return pos2SpleefCR.getBlockZ();
    }
    protected int getYSpleefCR() {
    	return pos1SpleefCR.getBlockY();
    }
    
    
    
    
    
    
    


    
    protected boolean winEventMaratonaCR(Player p) {
    	if(p.getLocation().getBlockX()>=getMinXMaratonaCR())
    		if(p.getLocation().getBlockX()<=getMaxXMaratonaCR())
    			if(p.getLocation().getBlockZ()>=getMinZMaratonaCR())
    				if(p.getLocation().getBlockZ()<=getMaxZMaratonaCR())
    					if(p.getLocation().getBlockY()>=getYMaratonaCR())
    						return true;
    	return false;
    }
    
    protected int getMinXMaratonaCR() {
   		if(pos1MaratonaCR.getBlockX()<pos2MaratonaCR.getBlockX())
   			return pos1MaratonaCR.getBlockX();
   		else
   			return pos2MaratonaCR.getBlockX();
    }
    protected int getMinZMaratonaCR() {
    	if(pos1MaratonaCR.getBlockZ()<pos2MaratonaCR.getBlockZ())
    		return pos1MaratonaCR.getBlockZ();
    	else
    		return pos2MaratonaCR.getBlockZ();
    }
    protected int getMaxXMaratonaCR() {
    	if(pos1MaratonaCR.getBlockX()>pos2MaratonaCR.getBlockX())
    		return pos1MaratonaCR.getBlockX();
    	else
    		return pos2MaratonaCR.getBlockX();
    }
    protected int getMaxZMaratonaCR() {
    	if(pos1MaratonaCR.getBlockZ()>pos2MaratonaCR.getBlockZ())
    		return pos1MaratonaCR.getBlockZ();
    	else
    		return pos2MaratonaCR.getBlockZ();
    }
    protected int getYMaratonaCR() {
    	return pos1MaratonaCR.getBlockY();
    }
    
    
    
    
    
    
    


    
    protected boolean winEventFarolSR(Player p) {
    	if(p.getLocation().getBlockX()>=getMinXFarolSR())
    		if(p.getLocation().getBlockX()<=getMaxXFarolSR())
    			if(p.getLocation().getBlockZ()>=getMinZFarolSR())
    				if(p.getLocation().getBlockZ()<=getMaxZFarolSR())
    					if(p.getLocation().getBlockY()>=getYFarolSR())
    						return true;
    	return false;
    }
    
    protected int getMinXFarolSR() {
   		if(pos1FarolSR.getBlockX()<pos2FarolSR.getBlockX())
   			return pos1FarolSR.getBlockX();
   		else
   			return pos2FarolSR.getBlockX();
    }
    protected int getMinZFarolSR() {
    	if(pos1FarolSR.getBlockZ()<pos2FarolSR.getBlockZ())
    		return pos1FarolSR.getBlockZ();
    	else
    		return pos2FarolSR.getBlockZ();
    }
    protected int getMaxXFarolSR() {
    	if(pos1FarolSR.getBlockX()>pos2FarolSR.getBlockX())
    		return pos1FarolSR.getBlockX();
    	else
    		return pos2FarolSR.getBlockX();
    }
    protected int getMaxZFarolSR() {
    	if(pos1FarolSR.getBlockZ()>pos2FarolSR.getBlockZ())
    		return pos1FarolSR.getBlockZ();
    	else
    		return pos2FarolSR.getBlockZ();
    }
    protected int getYFarolSR() {
    	return pos1FarolSR.getBlockY();
    }
    
    
    
    
    
    
    


    
    protected boolean winEventParkourCR(Player p) {
    	if(p.getLocation().getBlockX()>=getMinXParkourCR())
    		if(p.getLocation().getBlockX()<=getMaxXParkourCR())
    			if(p.getLocation().getBlockZ()>=getMinZParkourCR())
    				if(p.getLocation().getBlockZ()<=getMaxZParkourCR())
    					if(p.getLocation().getBlockY()>=getYParkourCR())
    						return true;
    	return false;
    }
    
    protected int getMinXParkourCR() {
   		if(pos1ParkourCR.getBlockX()<pos2ParkourCR.getBlockX())
   			return pos1ParkourCR.getBlockX();
   		else
   			return pos2ParkourCR.getBlockX();
    }
    protected int getMinZParkourCR() {
    	if(pos1ParkourCR.getBlockZ()<pos2ParkourCR.getBlockZ())
    		return pos1ParkourCR.getBlockZ();
    	else
    		return pos2ParkourCR.getBlockZ();
    }
    protected int getMaxXParkourCR() {
    	if(pos1ParkourCR.getBlockX()>pos2ParkourCR.getBlockX())
    		return pos1ParkourCR.getBlockX();
    	else
    		return pos2ParkourCR.getBlockX();
    }
    protected int getMaxZParkourCR() {
    	if(pos1ParkourCR.getBlockZ()>pos2ParkourCR.getBlockZ())
    		return pos1ParkourCR.getBlockZ();
    	else
    		return pos2ParkourCR.getBlockZ();
    }
    protected int getYParkourCR() {
    	return pos1ParkourCR.getBlockY();
    }
    
}
