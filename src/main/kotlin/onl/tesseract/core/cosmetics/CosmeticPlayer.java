package onl.tesseract.core.cosmetics;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CosmeticPlayer {
    Map<String, Set<Cosmetic>> cosmetics;

    public CosmeticPlayer(Map<String, Set<Cosmetic>> cosmetics)
    {
        this.cosmetics = cosmetics;
    }

    public boolean hasCosmetics(String type,Cosmetic cosmetic){
        return cosmetics.containsKey(type) && cosmetics.get(type).contains(cosmetic);
    }
    public boolean addCosmetics(String type, Cosmetic cosmetic){
        if(!cosmetics.containsKey(type))
            cosmetics.put(type,new HashSet<>());
        if(cosmetics.get(type).contains(cosmetic))return false;
        cosmetics.get(type).add(cosmetic);
        return true;
    }
    public boolean removeCosmetics(String type,Cosmetic cosmetic){
        if(!cosmetics.containsKey(type))
            return false;
        if(!cosmetics.get(type).contains(cosmetic))
            return false;
        cosmetics.get(type).remove(cosmetic);
        return true;
    }

    public int getTotal(String type)
    {
        if(!cosmetics.containsKey(type))return 0;
        return cosmetics.get(type).size();
    }

    public Set<Cosmetic> getCosmetics(String cosmeticType)
    {
        if (!cosmetics.containsKey(cosmeticType))
            return Set.of();
        return cosmetics.get(cosmeticType);
    }
}
