package br.com.xti.ouvidoria.helper;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author renato
 */
public class ListHelper {

    public static <ENTIDADE> HashMap<Integer, ENTIDADE> montaMap(List<ENTIDADE> lista)  {
        HashMap<Integer, ENTIDADE> map = new HashMap<>();
        if(lista == null) {
            return map;
        }
        
        for (ENTIDADE e : lista) {
            try {
                map.put(ReflectionHelper.getValorID(e), e);
            } catch (Exception ex) {
                Logger.getLogger(ListHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return map;
    }

    public static <ENTIDADE> void addMap(HashMap<Integer, ENTIDADE> map, ENTIDADE obj)  {
        try {
            map.put(ReflectionHelper.getValorID(obj), obj);
        } catch (Exception ex) {
            Logger.getLogger(ListHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static <ENTIDADE> void removeMap(HashMap<Integer, ENTIDADE> map, ENTIDADE obj)  {
        try {
            map.remove(ReflectionHelper.getValorID(obj));
        } catch (Exception ex) {
            Logger.getLogger(ListHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
