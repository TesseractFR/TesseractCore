package onl.tesseract.core.persistence.hibernate.boutique;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import onl.tesseract.lib.translation.PlayerLocaleRepository;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.UUID;


@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TPlayerInfoService implements PlayerLocaleRepository {
    private static final Logger log = LoggerFactory.getLogger(TPlayerInfoService.class);
    TPlayerInfoDAO dao = TPlayerInfoDAO.getInstance();

    public TPlayerInfo get(UUID uniqueId) {
        TPlayerInfo tPlayerInfo = dao.get(uniqueId);
        if (tPlayerInfo == null) {
            tPlayerInfo = new TPlayerInfo();
            tPlayerInfo.setUuid(uniqueId);
            dao.create(tPlayerInfo);
            log.info("Created new TPlayerInfo: {}", tPlayerInfo.getUuid().toString());
        }
        return tPlayerInfo;
    }

    public void addMarketCurrency(TPlayerInfo tPlayerInfo, int amount) {
        dao.addMarketCurrency(tPlayerInfo, amount);
        dao.refresh(tPlayerInfo);
    }

    public void addShopPoint(TPlayerInfo tPlayerInfo, int amount) {
        dao.addShopPoint(tPlayerInfo, amount);
        dao.refresh(tPlayerInfo);
    }

    public void save(TPlayerInfo tPlayerInfo) {
        dao.save(tPlayerInfo);
    }

    public void refresh(TPlayerInfo tPlayerInfo) {
        dao.refresh(tPlayerInfo);
    }

    @Override
    public @NotNull Locale getLocale(@NotNull UUID player) {
        return get(player).getLocale();
    }

    @Override
    public void setLocale(@NotNull UUID player, @NotNull Locale locale) {
        TPlayerInfo tplayerInfo = get(player);
        tplayerInfo.setLocale(locale);
        dao.save(tplayerInfo);
    }

    @Override
    public @NotNull Locale getLocale(@NotNull Player player) {
        return getLocale(player.getUniqueId());
    }

    @Override
    public void setLocale(@NotNull Player player, @NotNull Locale locale) {
        setLocale(player.getUniqueId(), locale);
    }
}
