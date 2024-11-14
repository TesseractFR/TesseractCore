package onl.tesseract.core.cosmetics;

import java.util.*;

public final class CosmeticPreview {

    private static final Map<UUID, Set<Cosmetic>> previews = new HashMap<>();

    private CosmeticPreview()
    {
    }

    public static void setPreview(final UUID uuid, final Cosmetic cosmetic)
    {
        previews.putIfAbsent(uuid, new HashSet<>());
        previews.get(uuid).add(cosmetic);
    }

    public static boolean hasPreviewed(final UUID uuid, final Cosmetic cosmetic)
    {
        return previews.getOrDefault(uuid, Collections.emptySet()).contains(cosmetic);
    }
}
