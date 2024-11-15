package onl.tesseract.core.persistence.hibernate.boutique;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TPlayerInfoService {
    private static final TPlayerInfoService instance = new TPlayerInfoService();
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

    public static TPlayerInfoService getInstance() {
        return instance;
    }
}
