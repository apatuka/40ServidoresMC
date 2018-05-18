package com.cadiducho.cservidoresmc.bukkit.cmd;

import com.cadiducho.cservidoresmc.bukkit.util.ApiResponse;
import com.cadiducho.cservidoresmc.bukkit.util.Util;
import java.util.Arrays;
import java.util.logging.Level;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Comando para obtener las estadísticas de tu servidor en 40ServidoresMC
 * @author Cadiducho
 */
public class StatsCMD extends CommandBase {
    
    public StatsCMD() {
        super("stats40", "40servidores.stats", Arrays.asList());
    }
    
    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (!perm(sender, getPermission(), true)) return;
     
        final Player p = (Player) sender;

        Util.readUrl("https://40servidoresmc.es/api2.php?estadisticas=1&clave=" + plugin.getConfig().getString("clave"), (ApiResponse response) -> {
            if (response.getException().isPresent()) {
                plugin.sendMessage("&cHa ocurrido una excepción. Revisa la consola o avisa a un administrador", sender);
                plugin.log(Level.SEVERE, "Excepción obteniendo estadisticas: " + response.getException().get().getMessage());
                return;
            }
            
            JSONObject jsonData = response.getResult();
            if (jsonData.get("nombre") == null) { //clave mal configurada
                plugin.sendMessage("&cClave incorrecta. Entra en &bhttps://40servidoresmc.es/miservidor.php &cy cambia esta.", sender);
                return;
            }
            
            p.sendMessage(plugin.getMetodos().colorizar("&9======= &7" + jsonData.get("nombre") + " &festá en el TOP &a" + jsonData.get("puesto") + " &9======="));
            plugin.sendMessage("&bVotos hoy: &6" + jsonData.get("votoshoy"), p);
            plugin.sendMessage("&bVotos premiados hoy: &6" + jsonData.get("votoshoypremiados"), p);
            plugin.sendMessage("&bVotos semanales: &6" + jsonData.get("votossemanales"), p);
            plugin.sendMessage("&bVotos premiados semanales: &6" + jsonData.get("votossemanalespremiados"), p);
            JSONArray array = (JSONArray) jsonData.get("ultimos20votos");
            String usuarios = "";
            for (Object obj : array) {
                JSONObject object = (JSONObject) obj;
                String strellita = (Integer.parseInt((String) object.get("recompensado")) == 1) ? "&a" : "&c";
                usuarios += (strellita + object.get("usuario") + "&6, ");
            }
            usuarios = usuarios.substring(0, usuarios.length() - 2) + ".";
            plugin.sendMessage("&bUltimos 20 votos: " + usuarios, p);
        });
    }
}
